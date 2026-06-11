package com.example.project.service.impl;

import com.example.project.models.dto.req.LoginRequest;
import com.example.project.models.dto.req.LogoutRequest;
import com.example.project.models.dto.req.RefreshTokenRequest;
import com.example.project.models.entity.RefreshToken;
import com.example.project.models.entity.User;
import com.example.project.repository.RefreshTokenRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.AuthService;
import com.example.project.service.JwtService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-expiration-days}")
    private Long refreshTokenExpirationDays;

    @Value("${jwt.access-token-expiration-minutes}")
    private Long accessTokenExpirationMinutes;

    @Override
    @Transactional
    public Map<String, Object> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email hoac mat khau khong dung"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Email hoac mat khau khong dung");
        }
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new IllegalArgumentException("Tai khoan da bi khoa");
        }

        return buildTokenData(user, createRefreshToken(user));
    }

    @Override
    @Transactional
    public Map<String, Object> refreshToken(RefreshTokenRequest request) {
        RefreshToken oldToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token khong hop le"));

        if (Boolean.TRUE.equals(oldToken.getRevoked()) || oldToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token da het han hoac da bi thu hoi");
        }

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        RefreshToken newToken = createRefreshToken(oldToken.getUser());
        return buildTokenData(oldToken.getUser(), newToken);
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token khong hop le"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(refreshTokenExpirationDays));
        refreshToken.setRevoked(false);
        refreshToken.setCreatedAt(LocalDateTime.now());
        return refreshTokenRepository.save(refreshToken);
    }

    private Map<String, Object> buildTokenData(User user, RefreshToken refreshToken) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("accessToken", jwtService.generateAccessToken(user));
        data.put("refreshToken", refreshToken.getToken());
        data.put("tokenType", "Bearer");
        data.put("expiresInMinutes", accessTokenExpirationMinutes);
        data.put("userId", user.getId());
        data.put("role", user.getRole());
        return data;
    }
}
