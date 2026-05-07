package com.ushan.lady_shoe_mart.auth.domain;

import com.ushan.lady_shoe_mart.common.util.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RoleDto extends AbstractModel {
    private String name;
    private Boolean active;
    private Date dateCreated;
    private Date dateUpdated;
    private Boolean isActive;
    private Boolean isSuper;
    private List<RolePermissionDto> rolePermissionList;
    private List<UserDto> userList;
}
