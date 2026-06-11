package com.example.project.models.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequest {

    @NotBlank
    private String refreshToken;
}
