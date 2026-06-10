package com.example.project.models.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollCourseRequest {

    @NotNull
    private Long studentId;

    @NotNull
    private Long courseId;
}
