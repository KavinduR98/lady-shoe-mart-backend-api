package com.ushan.lady_shoe_mart.product.controller;

import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.product.domain.Brand;
import com.ushan.lady_shoe_mart.product.domain.request.BrandRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IBrandController {

    @PostMapping("/brand")
    @ResponseBody
    ApiResponse<Brand> save(@ModelAttribute BrandRequest brand);
}
