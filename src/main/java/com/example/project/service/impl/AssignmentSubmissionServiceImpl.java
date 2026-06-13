package com.example.project.service.impl;

import com.example.project.models.dto.req.SubmitAssignmentRequest;
import com.example.project.models.entity.AssignmentSubmission;
import com.example.project.models.entity.Course;
import com.example.project.models.entity.User;
import com.example.project.models.entity.UserRole;
import com.example.project.repository.AssignmentSubmissionRepository;
import com.example.project.repository.CourseEnrollmentRepository;
import com.example.project.service.AssignmentSubmissionService;
import com.example.project.service.CourseService;
import com.example.project.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Override
    public AssignmentSubmission submit(SubmitAssignmentRequest request) {
        log.info("Nop bai tap: studentId={}, courseId={}, title={}", request.getStudentId(), request.getCourseId(), request.getTitle());
        User student = userService.findEntity(request.getStudentId());
        if (student.getRole() != UserRole.STUDENT) {
            log.warn("Nop bai tap that bai: user khong phai sinh vien userId={}", student.getId());
            throw new IllegalArgumentException("Chi sinh vien moi duoc nop bai");
        }

        Course course = courseService.findEntity(request.getCourseId());
        if (!courseEnrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            log.warn("Nop bai tap that bai: sinh vien chua dang ky khoa hoc studentId={}, courseId={}", student.getId(), course.getId());
            throw new IllegalArgumentException("Sinh vien chua dang ky khoa hoc nay");
        }

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudent(student);
        submission.setCourse(course);
        submission.setTitle(request.getTitle());
        submission.setSubmissionLink(request.getSubmissionLink());
        submission.setNote(request.getNote());
        submission.setSubmittedAt(LocalDateTime.now());
        AssignmentSubmission savedSubmission = assignmentSubmissionRepository.save(submission);
        log.info("Nop bai tap thanh cong: submissionId={}", savedSubmission.getId());
        return savedSubmission;
    }

    @Override
    public List<AssignmentSubmission> getByStudent(Long studentId) {
        log.info("Lay danh sach bai da nop: studentId={}", studentId);
        return assignmentSubmissionRepository.findByStudentId(studentId);
    }
}
