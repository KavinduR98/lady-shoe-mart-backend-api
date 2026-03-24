package com.ushan.lady_shoe_mart.product.domain;

import com.ushan.lady_shoe_mart.common.util.AbstractModel;
import com.ushan.lady_shoe_mart.common.util.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Product extends AbstractModel {
    private String productNumber;
    private String productName;
    private Boolean isActive;
    private Double stock;
    private Boolean active;
    private Boolean isPromotional;
    private String shortDescription;
    private String longDescription;
    private String erpCode;
    private Boolean outOfSock;
    private Double costPrice;
    private Double sellingPriceLk;
    private String image;
    private DiscountType discountType;
    private Double discountedPrice;
    private Boolean discountedTimer;
    private Date discountStart;
    private Date discountEnd;
    private Integer expireDuration;
    private Boolean bestSeller;
    private Integer brandId;
    private String brandName;
    private Date dateCreated;
    private Date dateUpdated;
}
