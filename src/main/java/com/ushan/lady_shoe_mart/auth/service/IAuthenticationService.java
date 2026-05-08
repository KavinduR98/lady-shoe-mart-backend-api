package com.ushan.lady_shoe_mart.auth.service;

import com.ushan.lady_shoe_mart.auth.domain.request.AuthRequest;
import com.ushan.lady_shoe_mart.auth.domain.response.AuthResponse;
import com.ushan.lady_shoe_mart.common.util.ApiResponse;

public interface IAuthenticationService {

    ApiResponse<AuthResponse> createAuthenticationToken(AuthRequest authRequest);
}
