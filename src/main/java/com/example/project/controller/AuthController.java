package com.example.project.controller;

import com.example.project.models.dto.req.ChangePasswordRequest;
import com.example.project.models.dto.req.LoginRequest;
import com.example.project.models.dto.req.LogoutRequest;
import com.example.project.models.dto.req.RefreshTokenRequest;
import com.example.project.models.dto.req.RegisterStudentRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.User;
import com.example.project.service.AuthService;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register/student")
    @ResponseStatus(HttpStatus.CREATED)
    public responseDto<User> registerStudent(@Valid @RequestBody RegisterStudentRequest request) {
        return responseDto.<User>builder()
                .success(true)
                .message("Dang ky sinh vien thanh cong")
                .data(userService.registerStudent(request))
                .errors(null)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/login")
    public responseDto<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        return responseDto.<Map<String, Object>>builder()
                .success(true)
                .message("Dang nhap thanh cong")
                .data(authService.login(request))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping("/refresh-token")
    public responseDto<Map<String, Object>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return responseDto.<Map<String, Object>>builder()
                .success(true)
                .message("Lam moi token thanh cong")
                .data(authService.refreshToken(request))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping("/logout")
    public responseDto<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request);
        return responseDto.<Void>builder()
                .success(true)
                .message("Dang xuat thanh cong")
                .data(null)
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping("/change-password")
    public responseDto<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return responseDto.<Void>builder()
                .success(true)
                .message("Doi mat khau thanh cong")
                .data(null)
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
