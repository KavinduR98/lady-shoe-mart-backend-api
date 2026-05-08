package com.ushan.lady_shoe_mart.auth.controller;

import com.ushan.lady_shoe_mart.auth.domain.request.AuthRequest;
import com.ushan.lady_shoe_mart.auth.domain.response.AuthResponse;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IAuthenticationController {

    @PostMapping("/authenticate")
    @ResponseBody
    ApiResponse<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest);
}
