package com.example.project.models.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadLectureMaterialRequest {

    @NotNull
    private Long lecturerId;

    @NotNull
    private Long courseId;

    @NotBlank
    private String title;

    @NotBlank
    private String materialLink;

    private String description;
}
