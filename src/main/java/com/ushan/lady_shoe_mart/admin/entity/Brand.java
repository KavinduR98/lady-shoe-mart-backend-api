package com.ushan.lady_shoe_mart.admin.entity;

import com.ushan.lady_shoe_mart.common.util.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "brand")
public class Brand extends AbstractEntity {

    @Column(name = "brand_code", unique = true, length = 20)
    private String brandCode;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "index_seq")
    private Integer indexSeq;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Product> productList;
}
