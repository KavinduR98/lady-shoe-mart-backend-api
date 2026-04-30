package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.Permission;
import com.ushan.lady_shoe_mart.auth.domain.response.PermissionResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface IPermissionController {

    @PostMapping("/permission/save")
    @ResponseBody
    List<Permission> save(@RequestBody List<Permission> permission);

    @GetMapping("/permission")
    @ResponseBody
    List<Permission> findAllPermission();

    @GetMapping("/permission/group")
    @ResponseBody
    List<PermissionResponse> findAllPermissionGroup();
}
