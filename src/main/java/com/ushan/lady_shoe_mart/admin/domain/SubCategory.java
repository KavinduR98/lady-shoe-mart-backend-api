package com.ushan.lady_shoe_mart.admin.domain;

import com.ushan.lady_shoe_mart.common.util.AbstractModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SubCategory extends AbstractModel {
    private String name;
    private String subCategoryCode;
    private Long categoryId;
    private String categoryCode;
    private String categoryName;
    private Boolean showOnWeb;
    private String description;
    private Integer indexSeq;
    private Boolean active;
    private Date createdDate;
    private Date updatedDate;
    private Boolean isActive;
}
