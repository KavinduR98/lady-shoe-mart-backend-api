package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.UserDto;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

import java.util.List;

public interface IUserService {
    ApiResponse<UserDto> save(UserDto user);

    List<UserDto> findAllUser();

    ApiResponse<UserDto> update(UserDto user);
}
