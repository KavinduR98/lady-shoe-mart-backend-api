package com.ushan.lady_shoe_mart.admin.domain;

import com.ushan.lady_shoe_mart.admin.entity.SubCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Category {
    private String name;
    private String categoryCode;
    private String description;
    private String imageLink;
    private String bgColor;
    private Integer indexSeq;
    private Boolean showOnWeb;
    private Boolean active;
    private Boolean isActive;
    private Date dateCreated;
    private Date dateUpdated;
    private List<SubCategory> subCategoryList;
}
