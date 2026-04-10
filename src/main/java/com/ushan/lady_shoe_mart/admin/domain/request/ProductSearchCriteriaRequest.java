package com.ushan.lady_shoe_mart.admin.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCriteriaRequest {
    private Boolean productNumber;
    private Boolean erpCode;
    private Boolean productName;
    private Boolean barCode;
    private Boolean category;
    private Boolean subCategory;
    private Boolean brand;
    private Boolean supplier;
}
