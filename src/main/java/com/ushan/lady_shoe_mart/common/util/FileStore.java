package com.ushan.lady_shoe_mart.common.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class FileStore {

    private final AmazonS3 amazonS3;

    @Value("${amazon.aws.bucket-name}")
    private String bucketName;

    public FileStore(@Qualifier("lsmS3Client") AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     * Upload file to S3 with metadata
     */
    public void uploadFile(String bucketName,
                           String path,
                           Optional<Map<String, String>> optionalMetadata,
                           InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();

        optionalMetadata.ifPresent(metadata -> {
            if (!metadata.isEmpty()) {
                metadata.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            amazonS3.putObject(bucketName, path, inputStream, objectMetadata);
            log.info("File uploaded successfully to S3: {}", path);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to upload the file", e);
        }
    }

    public String getFileUrl(String bucketName, String path) {
        return amazonS3.getUrl(bucketName, path).toString();
    }

    public boolean deleteFile(String filePath) throws FileUploadException {
        try {
            if (!fileExists(filePath)) {
                log.warn("File does not exist: {}", filePath);
                return false;
            }

            amazonS3.deleteObject(bucketName, filePath);
            log.info("File deleted successfully from S3: {}", filePath);
            return true;

        } catch (AmazonServiceException e) {
            log.error("Failed to delete file from S3: {}", e.getMessage(), e);
            throw new FileUploadException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    public boolean fileExists(String filePath) {
        try {
            return amazonS3.doesObjectExist(bucketName, filePath);
        } catch (AmazonServiceException e) {
            log.error("Error checking file existence: {}", e.getMessage(), e);
            return false;
        }
    }
}
