package com.ushan.lady_shoe_mart.auth.entity;

import com.ushan.lady_shoe_mart.common.util.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name = "user")
@Getter
@Setter
public class User extends AbstractEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "is_active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 1")
    private Boolean isActive = Boolean.TRUE;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @Column(name = "active", columnDefinition = "BOOLEAN NOT NULL DEFAULT 0")
    private Boolean active = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private Role role;
}
