package com.ushan.lady_shoe_mart.auth.domain;

import com.ushan.lady_shoe_mart.common.util.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RolePermission extends AbstractModel {
    private Integer roleId;
    private String roleName;
    private Long permissionId;
    private String permissionName;
    private Boolean active;
    private Date dateCreated;
    private Date dateUpdated;
    private Boolean isActive;
}
