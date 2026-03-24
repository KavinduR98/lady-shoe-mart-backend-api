package com.ushan.lady_shoe_mart.product.controller;

import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.product.domain.Brand;
import com.ushan.lady_shoe_mart.product.domain.request.BrandRequest;
import com.ushan.lady_shoe_mart.product.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class BrandController implements IBrandController{

    @Autowired
    IBrandService brandService;

    @Override
    public ApiResponse<Brand> save(BrandRequest brand) {
        return brandService.save(brand);
    }
}
