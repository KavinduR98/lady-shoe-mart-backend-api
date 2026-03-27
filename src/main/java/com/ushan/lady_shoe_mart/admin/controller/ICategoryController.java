package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.admin.domain.Category;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ICategoryController {

    @PostMapping("/category")
    @ResponseBody
    ApiResponse<Category> save(@RequestBody Category category);

    @GetMapping("/category")
    @ResponseBody
    List<Category> findAllCategory();

    @PutMapping("/category/image/upload")
    @ResponseBody
    ApiResponse<String> categoryImageUpload(@RequestPart(value = "file")MultipartFile file, @RequestParam Long categoryId);

    @PutMapping("/category/active")
    @ResponseBody
    ApiResponse<Boolean> active(@RequestParam Long id, @RequestParam Boolean active);

}
