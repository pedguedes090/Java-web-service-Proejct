package com.example.project.models.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    private Integer credits;

    private String teacherName;
}
