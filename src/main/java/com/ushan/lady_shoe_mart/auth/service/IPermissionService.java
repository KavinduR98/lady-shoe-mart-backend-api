package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.Permission;
import com.ushan.lady_shoe_mart.auth.domain.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {

    List<Permission> save(List<Permission> permission);

    List<Permission> findAllPermission();

    List<PermissionResponse> findAllPermissionGroup();
}
