package com.example.project.service;

import com.example.project.models.dto.req.LoginRequest;
import com.example.project.models.dto.req.LogoutRequest;
import com.example.project.models.dto.req.RefreshTokenRequest;
import java.util.Map;

public interface AuthService {

    Map<String, Object> login(LoginRequest request);

    Map<String, Object> refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request, String accessToken);
}
