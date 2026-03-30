package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.admin.domain.SubCategory;
import com.ushan.lady_shoe_mart.admin.service.ISubcategoryService;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class SubcategoryController implements ISubcategoryController{

    private final ISubcategoryService subcategoryService;

    @Override
    public ApiResponse<SubCategory> save(SubCategory subCategory) {
        return subcategoryService.save(subCategory);
    }

    @Override
    public List<SubCategory> findAllSubCategory() {
        return subcategoryService.findAllSubCategory();
    }

    @Override
    public ApiResponse<Boolean> active(Long id, Boolean active) {
        return subcategoryService.active(id, active);
    }

    @Override
    public List<SubCategory> findByCategoryId(Long id) {
        return subcategoryService.findByCategoryId(id);
    }

    @Override
    public ApiResponse<SubCategory> update(SubCategory subCategory) {
        return subcategoryService.update(subCategory);
    }
}
