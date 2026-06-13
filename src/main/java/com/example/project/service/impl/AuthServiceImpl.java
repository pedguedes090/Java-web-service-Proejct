package com.example.project.service.impl;

import com.example.project.models.dto.req.LoginRequest;
import com.example.project.models.dto.req.LogoutRequest;
import com.example.project.models.dto.req.RefreshTokenRequest;
import com.example.project.models.entity.RefreshToken;
import com.example.project.models.entity.TokenBlacklist;
import com.example.project.models.entity.User;
import com.example.project.repository.RefreshTokenRepository;
import com.example.project.repository.TokenBlacklistRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.AuthService;
import com.example.project.service.JwtService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-token-expiration-days}")
    private Long refreshTokenExpirationDays;

    @Value("${jwt.access-token-expiration-minutes}")
    private Long accessTokenExpirationMinutes;

    @Override
    @Transactional
    public Map<String, Object> login(LoginRequest request) {
        log.info("Dang nhap: email={}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Dang nhap that bai: khong tim thay email={}", request.getEmail());
                    return new IllegalArgumentException("Email hoac mat khau khong dung");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Dang nhap that bai: sai mat khau userId={}", user.getId());
            throw new IllegalArgumentException("Email hoac mat khau khong dung");
        }
        if (Boolean.FALSE.equals(user.getEnabled())) {
            log.warn("Dang nhap that bai: tai khoan bi khoa userId={}", user.getId());
            throw new IllegalArgumentException("Tai khoan da bi khoa");
        }

        log.info("Dang nhap thanh cong: userId={}, role={}", user.getId(), user.getRole());
        return buildTokenData(user, createRefreshToken(user));
    }

    @Override
    @Transactional
    public Map<String, Object> refreshToken(RefreshTokenRequest request) {
        log.info("Lam moi token");
        RefreshToken oldToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> {
                    log.warn("Lam moi token that bai: refresh token khong ton tai");
                    return new IllegalArgumentException("Refresh token khong hop le");
                });

        if (Boolean.TRUE.equals(oldToken.getRevoked()) || oldToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Lam moi token that bai: token het han hoac da thu hoi userId={}", oldToken.getUser().getId());
            throw new IllegalArgumentException("Refresh token da het han hoac da bi thu hoi");
        }

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        RefreshToken newToken = createRefreshToken(oldToken.getUser());
        log.info("Lam moi token thanh cong: userId={}", oldToken.getUser().getId());
        return buildTokenData(oldToken.getUser(), newToken);
    }

    @Override
    @Transactional
    public void logout(LogoutRequest request, String accessToken) {
        log.info("Dang xuat");
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> {
                    log.warn("Dang xuat that bai: refresh token khong ton tai");
                    return new IllegalArgumentException("Refresh token khong hop le");
                });
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        addAccessTokenToBlacklist(accessToken);
        log.info("Dang xuat thanh cong: userId={}", refreshToken.getUser().getId());
    }

    private void addAccessTokenToBlacklist(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            log.warn("Khong them access token vao blacklist vi token rong");
            return;
        }
        if (tokenBlacklistRepository.existsByToken(accessToken)) {
            log.info("Access token da ton tai trong blacklist");
            return;
        }

        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(accessToken);
        tokenBlacklist.setExpiresAt(jwtService.getExpirationFromToken(accessToken));
        tokenBlacklist.setCreatedAt(LocalDateTime.now());
        tokenBlacklistRepository.save(tokenBlacklist);
        log.info("Da them access token vao blacklist, expiresAt={}", tokenBlacklist.getExpiresAt());
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
