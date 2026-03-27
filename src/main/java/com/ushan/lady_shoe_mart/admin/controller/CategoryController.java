package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.admin.domain.Category;
import com.ushan.lady_shoe_mart.admin.service.ICategoryService;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class CategoryController implements ICategoryController{

    private final ICategoryService categoryService;

    @Override
    public ApiResponse<Category> save(Category category) {
        return categoryService.save(category);
    }

    @Override
    public List<Category> findAllCategory() {
        return categoryService.findAllCategory();
    }
}
