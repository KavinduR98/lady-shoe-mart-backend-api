package com.ushan.lady_shoe_mart.auth.domain.request;

import com.ushan.lady_shoe_mart.auth.domain.UserDto;
import com.ushan.lady_shoe_mart.auth.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private final User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return this.user.getFirstName();
    }

    public String getFullName() {
        if (user.getLastName() != null) {
            return this.user.getFirstName() + " " + user.getLastName();
        } else {
            return this.user.getFirstName();
        }
    }

    public UserDto getUserTokenData() {
        UserDto sendUser = new UserDto();
        sendUser.setFirstName(user.getFirstName());
        sendUser.setLastName(user.getLastName());
        sendUser.setEmail(user.getEmail());
        sendUser.setMobile(user.getMobile());
        return sendUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public @Nullable String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
