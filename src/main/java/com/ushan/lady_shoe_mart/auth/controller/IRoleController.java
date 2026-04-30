package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.RoleDto;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IRoleController {

    @PostMapping("/role")
    @ResponseBody
    ApiResponse<RoleDto> save(@RequestBody RoleDto role);

    @GetMapping("/role")
    @ResponseBody
    List<RoleDto> findAllRole();

    @GetMapping("/roleId")
    @ResponseBody
    RoleDto findAllRoleById(@RequestParam Long roleId);

    @PutMapping("/role")
    @ResponseBody
    ApiResponse<RoleDto> update(@RequestBody RoleDto role);
}
