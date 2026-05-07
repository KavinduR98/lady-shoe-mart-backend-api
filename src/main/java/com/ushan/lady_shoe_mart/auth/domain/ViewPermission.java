package com.ushan.lady_shoe_mart.auth.domain;

import com.ushan.lady_shoe_mart.common.util.enums.PermissionCategory;
import com.ushan.lady_shoe_mart.common.util.enums.PermissionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewPermission {
    private Long id;
    private String value;
    private PermissionType type;
    private PermissionCategory permissionCategory;
}
