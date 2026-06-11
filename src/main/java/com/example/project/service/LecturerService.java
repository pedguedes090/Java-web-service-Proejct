package com.example.project.service;

import com.example.project.models.dto.req.GradeSubmissionRequest;
import com.example.project.models.dto.req.UploadLectureMaterialRequest;
import com.example.project.models.entity.AssignmentSubmission;
import com.example.project.models.entity.LectureMaterial;
import java.util.List;

public interface LecturerService {

    AssignmentSubmission gradeSubmission(GradeSubmissionRequest request);

    LectureMaterial uploadMaterial(UploadLectureMaterialRequest request);

    List<LectureMaterial> getMaterialsByCourse(Long courseId);
}
