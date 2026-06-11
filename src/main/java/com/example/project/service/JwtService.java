package com.example.project.service;

import com.example.project.models.entity.User;

public interface JwtService {

    String generateAccessToken(User user);
}
