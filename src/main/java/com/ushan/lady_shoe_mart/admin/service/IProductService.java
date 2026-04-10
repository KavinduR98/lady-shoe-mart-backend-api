package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.Product;
import com.ushan.lady_shoe_mart.admin.domain.request.ProductSearchCriteriaRequest;
import com.ushan.lady_shoe_mart.admin.domain.response.PaginateShoeMartResponse;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

import java.util.List;

public interface IProductService {

    ApiResponse<Product> save(Product product);

    List<Product> findAllProduct();

    PaginateShoeMartResponse<Product> productSearchCriteria(ProductSearchCriteriaRequest productSearchCriteriaRequest, String productSearch,
                                                            Integer pageNo, Integer pageSize);
}
