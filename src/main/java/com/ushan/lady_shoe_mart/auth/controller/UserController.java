package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.UserDto;
import com.ushan.lady_shoe_mart.auth.service.IUserService;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/v1")
@RestController
public class UserController implements IUserController{

    private final IUserService userService;

    @Override
    public ApiResponse<UserDto> save(UserDto user) {
        return userService.save(user);
    }

    @Override
    public List<UserDto> findAllUser() {
        return userService.findAllUser();
    }

    @Override
    public ApiResponse<UserDto> update(UserDto user) {
        return userService.update(user);
    }
}
