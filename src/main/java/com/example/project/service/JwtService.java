package com.example.project.service;

import com.example.project.models.entity.User;

public interface JwtService {

    String generateAccessToken(User user);

    boolean isTokenValid(String token);

    String getEmailFromToken(String token);

    String getRoleFromToken(String token);

    java.time.LocalDateTime getExpirationFromToken(String token);
}
