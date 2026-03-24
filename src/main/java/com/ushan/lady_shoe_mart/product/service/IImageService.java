package com.ushan.lady_shoe_mart.product.service;

import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    String uploadFile(MultipartFile multipartFile, String folderName, String imagePrefix);
}
