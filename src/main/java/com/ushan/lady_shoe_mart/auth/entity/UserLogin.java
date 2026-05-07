package com.ushan.lady_shoe_mart.auth.entity;

import com.ushan.lady_shoe_mart.common.util.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity(name = "user_login")
@Getter
@Setter
public class UserLogin extends AbstractEntity {

    @Column(name = "token_key")
    private String tokenKey;

    @Column(name = "token_expired")
    private Boolean tokenExpired;

    @Column(name = "date_login")
    private Date dateLogin;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "date_updated")
    private Date dateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
