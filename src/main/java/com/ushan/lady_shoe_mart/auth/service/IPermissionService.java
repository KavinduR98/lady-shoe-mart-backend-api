package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.PermissionDto;
import com.ushan.lady_shoe_mart.auth.domain.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {

    List<PermissionDto> save(List<PermissionDto> permission);

    List<PermissionDto> findAllPermission();

    List<PermissionResponse> findAllPermissionGroup();
}
