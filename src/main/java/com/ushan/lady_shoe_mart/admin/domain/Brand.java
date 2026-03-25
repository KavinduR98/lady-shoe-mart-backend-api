package com.ushan.lady_shoe_mart.admin.domain;

import com.ushan.lady_shoe_mart.common.util.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Brand extends AbstractModel {
    private String brandCode;
    private String name;
    private String image;
    private Boolean active;
    private Integer indexSeq;
    private Boolean isActive;
    private Date dateCreated;
    private Date dateUpdated;
    private List<Product> productList;
}
