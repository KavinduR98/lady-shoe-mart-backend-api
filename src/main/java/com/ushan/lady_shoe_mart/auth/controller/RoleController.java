package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.Role;
import com.ushan.lady_shoe_mart.auth.service.IRoleService;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class RoleController implements IRoleController{

    private final IRoleService roleService;

    @Override
    public ApiResponse<Role> save(Role role) {
        return roleService.save(role);
    }

    @Override
    public List<Role> findAllRole() {
        return roleService.findAllRole();
    }

    @Override
    public Role findAllRoleById(Long roleId) {
        return roleService.findAllRoleById(roleId);
    }

    @Override
    public ApiResponse<Role> update(Role role) {
        return roleService.update(role);
    }
}
