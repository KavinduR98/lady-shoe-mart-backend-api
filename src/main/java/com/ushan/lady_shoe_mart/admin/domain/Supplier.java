package com.ushan.lady_shoe_mart.admin.domain;

import com.ushan.lady_shoe_mart.common.util.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Supplier extends AbstractModel {
    private String supplierId;
    private String name;
    private String email;
    private String mobile;
    private Boolean active;
    private Date dateCreated;
    private Date dateUpdated;
    private Boolean isActive;
}
