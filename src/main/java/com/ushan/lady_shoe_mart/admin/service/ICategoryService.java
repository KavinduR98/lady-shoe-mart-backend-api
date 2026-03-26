package com.ushan.lady_shoe_mart.admin.service;

import com.ushan.lady_shoe_mart.admin.domain.Category;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

public interface ICategoryService {

    ApiResponse<Category> save(Category category);
}
