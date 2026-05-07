package com.ushan.lady_shoe_mart.auth.domain.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthResponse {
    private String token;
    private String cookieValue;
    private RoleResponse roleResponse;
}
