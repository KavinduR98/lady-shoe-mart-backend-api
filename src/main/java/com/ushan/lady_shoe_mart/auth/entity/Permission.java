package com.ushan.lady_shoe_mart.auth.entity;

import com.ushan.lady_shoe_mart.common.util.AbstractEntity;
import com.ushan.lady_shoe_mart.common.util.enums.PermissionCategory;
import com.ushan.lady_shoe_mart.common.util.enums.PermissionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity(name = "permissions")
@Getter
@Setter
public class Permission extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false, unique = true)
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type", columnDefinition = "ENUM('VIEW', 'BTN_ACTION')")
    private PermissionType permissionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_category", columnDefinition = "ENUM('PRODUCT', 'CATEGORY', 'BRAND', 'ORDER', 'CART', 'PROMO', 'REPORT', 'BANNER', 'ADMIN', 'OTHER')")
    private PermissionCategory permissionCategory;

    @Column(name = "is_active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 1")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<RolePermission> rolePermissionList;
}
