package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.admin.domain.SubCategory;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ISubcategoryController {

    @PostMapping("/subCategory")
    @ResponseBody
    ApiResponse<SubCategory> save(@RequestBody SubCategory subCategory);

    @GetMapping("/subCategory")
    @ResponseBody
    List<SubCategory> findAllSubCategory();

    @PutMapping("/subCategory/active")
    @ResponseBody
    ApiResponse<Boolean> active(@RequestParam Long id, @RequestParam Boolean active);

    @GetMapping("/subCategory/{id}")
    @ResponseBody
    List<SubCategory> findByCategoryId(@PathVariable Long id);

    @PutMapping("/subCategory")
    @ResponseBody
    ApiResponse<SubCategory> update(@RequestBody SubCategory subCategory);

}
