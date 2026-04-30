package com.ushan.lady_shoe_mart.auth.entity;

import com.ushan.lady_shoe_mart.common.util.AbstractEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Entity(name = "role_permission")
@Getter
@Setter
public class RolePermission extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Column(name = "active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 1")
    private Boolean active = Boolean.TRUE;

    @Column(name = "is_active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 1")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;
}
