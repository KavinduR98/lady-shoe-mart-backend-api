package com.ushan.lady_shoe_mart.admin.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductImage {
    private String mainImage;
    private Long mainImageId;
    private Integer sequence;
    private List<ProductAdditionalImage> productAdditionalImageList;
}
