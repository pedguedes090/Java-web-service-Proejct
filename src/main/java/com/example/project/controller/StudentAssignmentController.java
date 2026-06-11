package com.example.project.controller;

import com.example.project.models.dto.req.SubmitAssignmentRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.AssignmentSubmission;
import com.example.project.service.AssignmentSubmissionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/assignments")
@RequiredArgsConstructor
public class StudentAssignmentController {

    private final AssignmentSubmissionService assignmentSubmissionService;

    @PostMapping("/submit")
    @ResponseStatus(HttpStatus.CREATED)
    public responseDto<AssignmentSubmission> submit(@Valid @RequestBody SubmitAssignmentRequest request) {
        return responseDto.<AssignmentSubmission>builder()
                .success(true)
                .message("Nop bai tap thanh cong")
                .data(assignmentSubmissionService.submit(request))
                .errors(null)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/students/{studentId}/submissions")
    public responseDto<List<AssignmentSubmission>> getByStudent(@PathVariable Long studentId) {
        return responseDto.<List<AssignmentSubmission>>builder()
                .success(true)
                .message("Lay danh sach bai da nop thanh cong")
                .data(assignmentSubmissionService.getByStudent(studentId))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
