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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LecturerServiceImpl implements LecturerService {

    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final LectureMaterialRepository lectureMaterialRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Override
    public AssignmentSubmission gradeSubmission(GradeSubmissionRequest request) {
        log.info("Cham diem bai nop: lecturerId={}, submissionId={}, score={}", request.getLecturerId(), request.getSubmissionId(), request.getScore());
        User lecturer = getLecturer(request.getLecturerId());
        AssignmentSubmission submission = assignmentSubmissionRepository.findById(request.getSubmissionId())
                .orElseThrow(() -> {
                    log.warn("Cham diem that bai: khong tim thay bai nop submissionId={}", request.getSubmissionId());
                    return new EntityNotFoundException("Khong tim thay bai nop");
                });

        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());
        submission.setLecturer(lecturer);
        submission.setGradedAt(LocalDateTime.now());
        AssignmentSubmission savedSubmission = assignmentSubmissionRepository.save(submission);
        log.info("Cham diem bai nop thanh cong: submissionId={}", savedSubmission.getId());
        return savedSubmission;
    }

    @Override
    public LectureMaterial uploadMaterial(UploadLectureMaterialRequest request) {
        log.info("Tai len tai lieu: lecturerId={}, courseId={}, title={}", request.getLecturerId(), request.getCourseId(), request.getTitle());
        User lecturer = getLecturer(request.getLecturerId());
        Course course = courseService.findEntity(request.getCourseId());

        LectureMaterial material = new LectureMaterial();
        material.setLecturer(lecturer);
        material.setCourse(course);
        material.setTitle(request.getTitle());
        material.setMaterialLink(request.getMaterialLink());
        material.setDescription(request.getDescription());
        material.setUploadedAt(LocalDateTime.now());
        LectureMaterial savedMaterial = lectureMaterialRepository.save(material);
        log.info("Tai len tai lieu thanh cong: materialId={}", savedMaterial.getId());
        return savedMaterial;
    }

    @Override
    public List<LectureMaterial> getMaterialsByCourse(Long courseId) {
        log.info("Lay danh sach tai lieu theo khoa hoc: courseId={}", courseId);
        return lectureMaterialRepository.findByCourseId(courseId);
    }

    private User getLecturer(Long lecturerId) {
        User lecturer = userService.findEntity(lecturerId);
        if (lecturer.getRole() != UserRole.TEACHER) {
            log.warn("Chuc nang giang vien that bai: user khong phai giang vien userId={}", lecturerId);
            throw new IllegalArgumentException("Chi giang vien moi duoc thuc hien chuc nang nay");
        }
        return lecturer;
    }
}
