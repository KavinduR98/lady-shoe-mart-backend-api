package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.RoleDto;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

import java.util.List;

public interface IRoleService {

    ApiResponse<RoleDto> save(RoleDto role);

    List<RoleDto> findAllRole();

    RoleDto findAllRoleById(Long roleId);

    ApiResponse<RoleDto> update(RoleDto role);
}
