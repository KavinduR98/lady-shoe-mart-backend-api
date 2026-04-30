package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.Role;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

import java.util.List;

public interface IRoleService {

    ApiResponse<Role> save(Role role);

    List<Role> findAllRole();

    Role findAllRoleById(Long roleId);

    ApiResponse<Role> update(Role role);
}
