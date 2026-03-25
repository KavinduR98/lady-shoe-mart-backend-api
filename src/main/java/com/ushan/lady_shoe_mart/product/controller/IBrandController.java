package com.ushan.lady_shoe_mart.product.controller;

import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.product.domain.Brand;
import com.ushan.lady_shoe_mart.product.domain.request.BrandRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IBrandController {

    @PostMapping("/brand")
    @ResponseBody
    ApiResponse<Brand> save(@ModelAttribute BrandRequest brand);

    @GetMapping("/brand")
    @ResponseBody
    List<Brand> findAllBrand();

    @GetMapping("brand/{id}")
    @ResponseBody
    ApiResponse<Brand> getBrand(@PathVariable Long id);

    @PutMapping("brand/{id}")
    @ResponseBody
    ApiResponse<Brand> update(@PathVariable Long id, @ModelAttribute BrandRequest brand);
}
