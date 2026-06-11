package com.example.project.service;

import com.example.project.models.dto.req.SubmitAssignmentRequest;
import com.example.project.models.entity.AssignmentSubmission;
import java.util.List;

public interface AssignmentSubmissionService {

    AssignmentSubmission submit(SubmitAssignmentRequest request);

    List<AssignmentSubmission> getByStudent(Long studentId);
}
