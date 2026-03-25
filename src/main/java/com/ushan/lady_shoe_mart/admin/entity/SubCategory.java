package com.ushan.lady_shoe_mart.admin.entity;

import com.ushan.lady_shoe_mart.common.util.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name = "sub_category")
public class SubCategory extends AbstractEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "sub_category_code")
    private String subCategoryCode;

    @Column(name = "description")
    private String description;

    @Column(name = "index_seq")
    private Integer indexSeq;

    @Column(name = "active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 0")
    private Boolean active = Boolean.FALSE;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @Column(name = "is_active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 1")
    private Boolean isActive = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
}
