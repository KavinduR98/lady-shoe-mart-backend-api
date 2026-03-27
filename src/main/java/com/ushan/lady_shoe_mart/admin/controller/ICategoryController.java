package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.admin.domain.Category;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


public interface ICategoryController {

    @PostMapping("/category")
    @ResponseBody
    ApiResponse<Category> save(@RequestBody Category category);

    @GetMapping("/category")
    @ResponseBody
    List<Category> findAllCategory();
}
