package com.ushan.lady_shoe_mart.admin.controller;

import com.google.gson.Gson;
import com.ushan.lady_shoe_mart.admin.domain.Product;
import com.ushan.lady_shoe_mart.admin.domain.request.ProductSearchCriteriaRequest;
import com.ushan.lady_shoe_mart.admin.domain.response.PaginateShoeMartResponse;
import com.ushan.lady_shoe_mart.admin.service.IProductService;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ProductController implements IProductController{

    private final IProductService productService;

    @Override
    public ApiResponse<Product> save(Product product) {
        return productService.save(product);
    }

    @Override
    public List<Product> findAllProduct() {
        return productService.findAllProduct();
    }

    @Override
    public PaginateShoeMartResponse<Product> productSearchCriteria(String productSearchCriteria, String productSearch, Integer pageNo, Integer pageSize) {
        ProductSearchCriteriaRequest productSearchCriteriaRequest = new Gson().fromJson(productSearchCriteria, ProductSearchCriteriaRequest.class);
        return productService.productSearchCriteria(productSearchCriteriaRequest, productSearch, pageNo, pageSize);
    }
}
