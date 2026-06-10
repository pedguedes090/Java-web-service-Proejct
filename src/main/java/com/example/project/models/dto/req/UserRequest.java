package com.example.project.models.dto.req;

import com.example.project.models.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    private String password;

    private String phone;

    private String studentCode;

    @NotNull
    private UserRole role;

    private Boolean enabled = true;
}
