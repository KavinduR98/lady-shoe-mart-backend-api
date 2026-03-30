package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.SubCategory;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

import java.util.List;

public interface ISubcategoryService {

    ApiResponse<SubCategory> save(SubCategory subCategory);

    List<SubCategory> findAllSubCategory();

    ApiResponse<Boolean> active(Long id, Boolean active);

    List<SubCategory> findByCategoryId(Long id);

    ApiResponse<SubCategory> update(SubCategory subCategory);
}
