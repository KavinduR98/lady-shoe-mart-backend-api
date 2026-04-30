package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.PermissionDto;
import com.ushan.lady_shoe_mart.auth.domain.response.PermissionResponse;
import com.ushan.lady_shoe_mart.auth.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/v1")
@RequiredArgsConstructor
@RestController
public class PermissionController implements IPermissionController{

    private final IPermissionService permissionService;

    @Override
    public List<PermissionDto> save(List<PermissionDto> permission) {
        return permissionService.save(permission);
    }

    @Override
    public List<PermissionDto> findAllPermission() {
        return permissionService.findAllPermission();
    }

    @Override
    public List<PermissionResponse> findAllPermissionGroup() {
        return permissionService.findAllPermissionGroup();
    }
}
