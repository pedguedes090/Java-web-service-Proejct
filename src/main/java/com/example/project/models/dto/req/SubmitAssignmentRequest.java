package com.example.project.models.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubmitAssignmentRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;

    @NotBlank
    private String title;

    @NotBlank
    private String submissionLink;

    private String note;
}
