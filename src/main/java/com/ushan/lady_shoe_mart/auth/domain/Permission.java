package com.ushan.lady_shoe_mart.auth.domain;

import com.ushan.lady_shoe_mart.common.util.AbstractModel;
import com.ushan.lady_shoe_mart.common.util.enums.PermissionCategory;
import com.ushan.lady_shoe_mart.common.util.enums.PermissionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Permission extends AbstractModel {
    private String name;
    private String value;
    private PermissionType permissionType;
    private PermissionCategory permissionCategory;
    private Boolean active;
    private Date dateCreated;
    private Date dateUpdated;
    private Boolean isActive;
}
