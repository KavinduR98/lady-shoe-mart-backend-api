package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.Role;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IRoleController {

    @PostMapping("/role")
    @ResponseBody
    ApiResponse<Role> save(@RequestBody Role role);

    @GetMapping("/role")
    @ResponseBody
    List<Role> findAllRole();

    @GetMapping("/roleId")
    @ResponseBody
    Role findAllRoleById(@RequestParam Long roleId);

    @PutMapping("/role")
    @ResponseBody
    ApiResponse<Role> update(@RequestBody Role role);
}
