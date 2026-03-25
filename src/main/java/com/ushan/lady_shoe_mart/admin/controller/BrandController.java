package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.admin.domain.Brand;
import com.ushan.lady_shoe_mart.admin.domain.request.BrandRequest;
import com.ushan.lady_shoe_mart.admin.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class BrandController implements IBrandController{

    @Autowired
    IBrandService brandService;

    @Override
    public ApiResponse<Brand> save(BrandRequest brand) {
        return brandService.save(brand);
    }

    @Override
    public List<Brand> findAllBrand() {
        return brandService.findAllBrand();
    }

    @Override
    public ApiResponse<Brand> getBrand(@PathVariable Long id) {
        return brandService.getBrand(id);
    }

    @Override
    public ApiResponse<Brand> update(Long id, BrandRequest brand) {
        return brandService.update(id, brand);
    }

    @Override
    public ApiResponse<Boolean> active(Long id, Boolean active) {
        return brandService.active(id, active);
    }

}
