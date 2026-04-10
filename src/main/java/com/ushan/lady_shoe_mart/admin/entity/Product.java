package com.ushan.lady_shoe_mart.admin.entity;

import com.ushan.lady_shoe_mart.admin.domain.ProductImage;
import com.ushan.lady_shoe_mart.common.converter.ProductImageConvertor;
import com.ushan.lady_shoe_mart.common.util.AbstractEntity;
import com.ushan.lady_shoe_mart.common.util.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name = "product")
public class Product extends AbstractEntity {

    @Column(name = "product_number", unique = true)
    private String productNumber;

    @Column(name = "product_name")
    private String productName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category", referencedColumnName = "id")
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_category", referencedColumnName = "id")
    private SubCategory subCategory;

    @Column(name = "is_active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 1")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "stock")
    private Double stock;

    @Column(name = "product_tags")
    private String productTags;

    @Column(name = "active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 0")
    private Boolean active = Boolean.FALSE;

    @Column(name = "is_promotional")
    private Boolean isPromotional;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "erp_code", unique = true)
    private String erpCode;

    @Column(name = "out_of_stock")
    private Boolean outOfSock;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "selling_price_lk")
    private Double sellingPriceLk;

    @Convert(converter = ProductImageConvertor.class)
    @Column(name = "images", columnDefinition = "json")
    private ProductImage image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier", referencedColumnName = "id")
    private Supplier supplier;

    @Column(name = "discount_type", columnDefinition = "ENUM('VALUE', 'PERCENTAGE')")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(name = "discounted_price")
    private Double discountedPrice;

    @Column(name = "discounted_timer")
    private Boolean discountedTimer;

    @Column(name = "discount_start")
    private Date discountStart;

    @Column(name = "discount_end")
    private Date discountEnd;

    @Column(name = "expire_duration")
    private Integer expireDuration;

    @Column(name = "new_arrival")
    private Boolean newArrival;

    @Column(name = "child_product")
    private Boolean childProduct;

    @Column(name = "min_order_level")
    private Double minOrderLevel;

    @Column(name = "max_order_level")
    private Double maxOrderLevel;

    @Column(name = "best_seller", columnDefinition = "BOOLEAN NOT NULL DEFAULT 0")
    private Boolean bestSeller = Boolean.FALSE;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;
}
