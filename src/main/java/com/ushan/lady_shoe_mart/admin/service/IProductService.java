package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.Product;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

public interface IProductService {

    ApiResponse<Product> save(Product product);
}
