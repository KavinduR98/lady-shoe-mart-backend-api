package com.ushan.lady_shoe_mart.product.service;

import com.ushan.lady_shoe_mart.common.exception.LsmException;
import com.ushan.lady_shoe_mart.common.util.FileStore;
import com.ushan.lady_shoe_mart.common.util.ShoeMartConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService implements IImageService{

    private final FileStore fileStore;

    @Value("${amazon.aws.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile multipartFile, String folderName, String imagePrefix) {
        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        validateFile(multipartFile, ShoeMartConstant.ALLOWED_IMAGE_MIME_TYPES,
                ShoeMartConstant.ALLOWED_IMAGE_EXTENSIONS,
                ShoeMartConstant.MAX_IMAGE_SIZE, "Image");

        Map<String, String> metadata = buildMetadata(multipartFile);
        //Save Image in S3
        String extension = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
        String fileName = String.format("%s", generateFileName(imagePrefix, extension.toLowerCase()));
        String path = String.format("%s/%s", folderName, fileName);
        log.info("PATH: {}", path);
        log.info("FILE NAME: {}", fileName);
        try {
            fileStore.uploadFile(bucketName, path, Optional.of(metadata), multipartFile.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        return fileName;
    }

    private void validateFile(MultipartFile file, String[] allowedMimeTypes,
                              String[] allowedExtensions, long maxSize, String fileType) {

        if (file == null || file.isEmpty()) {
            throw new LsmException(fileType + " file is required");
        }

        log.info("{} file size: {} KB", fileType, file.getSize() / 1024);
        if (file.getSize() > maxSize) {
            throw new LsmException(String.format("%s file size exceeds maximum limit of %d MB",
                    fileType, maxSize / (1024 * 1024)));
        }

        // Check MIME type
        String contentType = file.getContentType();
        if (!Arrays.asList(allowedMimeTypes).contains(contentType)) {
            throw new LsmException(String.format("Invalid %s file type. Allowed types: %s",
                    fileType, String.join(", ", allowedMimeTypes)));
        }

        // Check file extension
        String originalFilename = file.getOriginalFilename();

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!Arrays.asList(allowedExtensions).contains(extension)) {
            throw new LsmException(String.format("Invalid %s file extension. Allowed extensions: %s",
                    fileType, String.join(", ", allowedExtensions)));
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastDotIndex + 1);
    }

    private Map<String, String> buildMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private String generateFileName(String fileNamePrefix, String fileNamePostfix) {
        long randomNum = generateRandNo();
        String fileName = fileNamePrefix + "-" + new Date().getTime() + randomNum + "." + fileNamePostfix;
        return fileName.replace(" ", "_");
    }

    private Long generateRandNo() {
        int min = 0;
        int max = 200;
        Random random = new Random();
        return (long) random.nextInt(max - min + 1) + min;
    }
}
