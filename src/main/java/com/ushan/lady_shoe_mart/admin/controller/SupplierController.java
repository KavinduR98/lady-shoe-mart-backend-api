package com.ushan.lady_shoe_mart.admin.controller;

import com.ushan.lady_shoe_mart.admin.domain.Supplier;
import com.ushan.lady_shoe_mart.admin.service.ISupplierService;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class SupplierController implements ISupplierController{

    private final ISupplierService supplierService;

    @Override
    public ApiResponse<Supplier> save(Supplier supplier) {
        return supplierService.save(supplier);
    }

    @Override
    public List<Supplier> findAllSupplier() {
        return supplierService.findAllSupplier();
    }

    @Override
    public ApiResponse<Supplier> getSupplierById(Long id) {
        return supplierService.getSupplierById(id);
    }

    @Override
    public ApiResponse<Boolean> active(Long id, Boolean active) {
        return active(id, active);
    }
}
