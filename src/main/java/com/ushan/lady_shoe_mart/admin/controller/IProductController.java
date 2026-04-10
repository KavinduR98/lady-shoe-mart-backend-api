package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.admin.domain.Product;
import com.ushan.lady_shoe_mart.admin.domain.response.PaginateShoeMartResponse;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IProductController {

    @PostMapping("/product")
    @ResponseBody
    ApiResponse<Product> save(@RequestBody Product product);

    @GetMapping("/product")
    @ResponseBody
    List<Product> findAllProduct();

    @GetMapping("/product/searchCriteria")
    @ResponseBody
    PaginateShoeMartResponse<Product> productSearchCriteria(
            @RequestParam String productSearchCriteria,
            @RequestParam(required = false) String productSearch,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize);
}
