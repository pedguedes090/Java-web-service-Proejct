package com.example.project.models.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClassroomRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;

    private String semester;

    private String teacherName;
}
