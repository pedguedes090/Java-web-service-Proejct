package com.example.project.service.impl;

import com.example.project.models.dto.req.GradeSubmissionRequest;
import com.example.project.models.dto.req.UploadLectureMaterialRequest;
import com.example.project.models.entity.AssignmentSubmission;
import com.example.project.models.entity.Course;
import com.example.project.models.entity.LectureMaterial;
import com.example.project.models.entity.User;
import com.example.project.models.entity.UserRole;
import com.example.project.repository.AssignmentSubmissionRepository;
import com.example.project.repository.LectureMaterialRepository;
import com.example.project.service.CourseService;
import com.example.project.service.LecturerService;
import com.example.project.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LecturerServiceImpl implements LecturerService {

    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final LectureMaterialRepository lectureMaterialRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Override
    public AssignmentSubmission gradeSubmission(GradeSubmissionRequest request) {
        User lecturer = getLecturer(request.getLecturerId());
        AssignmentSubmission submission = assignmentSubmissionRepository.findById(request.getSubmissionId())
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay bai nop"));

        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());
        submission.setLecturer(lecturer);
        submission.setGradedAt(LocalDateTime.now());
        return assignmentSubmissionRepository.save(submission);
    }

    @Override
    public LectureMaterial uploadMaterial(UploadLectureMaterialRequest request) {
        User lecturer = getLecturer(request.getLecturerId());
        Course course = courseService.findEntity(request.getCourseId());

        LectureMaterial material = new LectureMaterial();
        material.setLecturer(lecturer);
        material.setCourse(course);
        material.setTitle(request.getTitle());
        material.setMaterialLink(request.getMaterialLink());
        material.setDescription(request.getDescription());
        material.setUploadedAt(LocalDateTime.now());
        return lectureMaterialRepository.save(material);
    }

    @Override
    public List<LectureMaterial> getMaterialsByCourse(Long courseId) {
        return lectureMaterialRepository.findByCourseId(courseId);
    }

    private User getLecturer(Long lecturerId) {
        User lecturer = userService.findEntity(lecturerId);
        if (lecturer.getRole() != UserRole.TEACHER) {
            throw new IllegalArgumentException("Chi giang vien moi duoc thuc hien chuc nang nay");
        }
        return lecturer;
    }
}
