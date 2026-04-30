package com.ushan.lady_shoe_mart.auth.domain.response;

import com.ushan.lady_shoe_mart.auth.domain.Permission;
import com.ushan.lady_shoe_mart.common.util.enums.PermissionCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PermissionResponse {
    private PermissionCategory category;
    private List<Permission> permissions;
}
