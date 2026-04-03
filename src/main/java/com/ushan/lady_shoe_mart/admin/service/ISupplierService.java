package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.Supplier;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

import java.util.List;

public interface ISupplierService {

    ApiResponse<Supplier> save(Supplier supplier);

    List<Supplier> findAllSupplier();

    ApiResponse<Supplier> getSupplierById(Long id);

    ApiResponse<Boolean> active(Long id, Boolean active);
}
