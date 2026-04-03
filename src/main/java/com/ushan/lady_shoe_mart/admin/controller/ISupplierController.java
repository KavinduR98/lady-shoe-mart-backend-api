package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.admin.domain.Supplier;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ISupplierController {

    @PostMapping("/supplier")
    @ResponseBody
    ApiResponse<Supplier> save(@RequestBody Supplier supplier);

    @GetMapping("/supplier")
    @ResponseBody
    List<Supplier> findAllSupplier();

    @GetMapping("/supplier/{id}")
    @ResponseBody
    ApiResponse<Supplier> getSupplierById(@PathVariable Long id);

    @PutMapping("/supplier/active")
    @ResponseBody
    ApiResponse<Boolean> active(@RequestParam Long id, @RequestParam Boolean active);
}
