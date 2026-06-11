package com.example.project.models.dto.req;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GradeSubmissionRequest {

    @NotNull
    private Long lecturerId;

    @NotNull
    private Long submissionId;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private BigDecimal score;

    private String feedback;
}
