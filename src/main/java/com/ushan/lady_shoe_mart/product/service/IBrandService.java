package com.ushan.lady_shoe_mart.product.service;

import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import com.ushan.lady_shoe_mart.product.domain.Brand;
import com.ushan.lady_shoe_mart.product.domain.request.BrandRequest;

import java.util.List;

public interface IBrandService {

    ApiResponse<Brand> save(BrandRequest brand);

    List<Brand> findAllBrand();
}
