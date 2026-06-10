package com.example.project.models.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class responseDto<T> {
    private Boolean success;
    private String message;
    private T data;
    private Object errors;
    private HttpStatus httpStatus;
}
