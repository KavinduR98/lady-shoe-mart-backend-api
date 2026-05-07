package com.ushan.lady_shoe_mart.auth.domain;

import com.ushan.lady_shoe_mart.common.util.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDto extends AbstractModel {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private String mobile;
    private Boolean isActive;
    private Date dateCreated;
    private Date dateUpdated;
    private Boolean active;
    private Integer roleId;
    private String roleName;
}
