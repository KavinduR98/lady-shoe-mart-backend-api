package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.UserDto;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IUserController {

    @PostMapping("/user")
    @ResponseBody
    ApiResponse<UserDto> save(@RequestBody UserDto user);

    @GetMapping("/user")
    @ResponseBody
    List<UserDto> findAllUser();

    @PutMapping("/user")
    @ResponseBody
    ApiResponse<UserDto> update(@RequestBody UserDto user);

}
