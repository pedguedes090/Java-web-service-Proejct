package com.example.project.controller;

import com.example.project.models.dto.req.RegisterStudentRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.User;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
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
}
