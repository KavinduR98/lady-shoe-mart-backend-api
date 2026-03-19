package com.ushan.lady_shoe_mart.product.entity;

import com.ushan.lady_shoe_mart.common.util.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name = "category")
public class Category extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "description")
    private String description;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "bg_color")
    private String bgColor;

    @Column(name = "index_seq")
    private Integer indexSeq;

    @Column(name = "active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 'false'")
    private Boolean active = Boolean.FALSE;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @Column(name = "is_active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 'true'")
    private Boolean isActive = Boolean.TRUE;
}
