package com.example.project.models.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
