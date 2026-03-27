package com.ushan.lady_shoe_mart.admin.entity;

import com.ushan.lady_shoe_mart.common.util.AbstractEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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

    @Column(name = "show_on_web", columnDefinition = "BOOLEAN NOT NULL DEFAULT 1")
    private Boolean showOnWeb = Boolean.TRUE;

    @Column(name = "active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 0")
    private Boolean active = Boolean.FALSE;

    @Column(name = "is_active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 1")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SubCategory> subCategoryList;
}
