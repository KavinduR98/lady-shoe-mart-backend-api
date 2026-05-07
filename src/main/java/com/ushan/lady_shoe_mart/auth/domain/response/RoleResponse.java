package com.ushan.lady_shoe_mart.auth.domain.response;

import com.ushan.lady_shoe_mart.auth.domain.ActionPermission;
import com.ushan.lady_shoe_mart.auth.domain.ViewPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleResponse {
    private Long id;
    private String name;
    private Boolean isSuper;
    private List<ActionPermission> actionPermissionList;
    private List<ViewPermission> viewPermissionList;
}
