package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.request.AuthRequest;
import com.ushan.lady_shoe_mart.auth.domain.response.AuthResponse;
import com.ushan.lady_shoe_mart.auth.service.IAuthenticationService;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class AuthenticationController implements IAuthenticationController{

    private final IAuthenticationService authenticationService;

    @Override
    public ApiResponse<AuthResponse> createAuthenticationToken(AuthRequest authRequest) {
        return authenticationService.createAuthenticationToken(authRequest);
    }
}
