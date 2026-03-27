package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.Category;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ICategoryService {

    ApiResponse<Category> save(Category category);

    List<Category> findAllCategory();

    ApiResponse<String> categoryImageUpload(MultipartFile file, Long categoryId);

    ApiResponse<Boolean> active(Long id, Boolean active);
}
