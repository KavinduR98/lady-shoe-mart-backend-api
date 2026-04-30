package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.PermissionDto;
import com.ushan.lady_shoe_mart.auth.domain.response.PermissionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface IPermissionController {

    @PostMapping("/permission/save")
    @ResponseBody
    List<PermissionDto> save(@RequestBody List<PermissionDto> permission);

    @GetMapping("/permission")
    @ResponseBody
    List<PermissionDto> findAllPermission();

    @GetMapping("/permission/group")
    @ResponseBody
    List<PermissionResponse> findAllPermissionGroup();
}
