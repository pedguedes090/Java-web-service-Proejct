package com.example.project.service.impl;

import com.example.project.models.dto.req.EnrollCourseRequest;
import com.example.project.models.entity.Course;
import com.example.project.models.entity.CourseEnrollment;
import com.example.project.models.entity.EnrollmentStatus;
import com.example.project.models.entity.User;
import com.example.project.models.entity.UserRole;
import com.example.project.repository.CourseEnrollmentRepository;
import com.example.project.service.CourseService;
import com.example.project.service.EnrollmentService;
import com.example.project.service.UserService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final CourseEnrollmentRepository enrollmentRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Override
    @Transactional
    public CourseEnrollment enroll(EnrollCourseRequest request) {
        log.info("Dang ky khoa hoc: studentId={}, courseId={}", request.getStudentId(), request.getCourseId());
        User student = userService.findEntity(request.getStudentId());
        if (student.getRole() != UserRole.STUDENT) {
            log.warn("Dang ky khoa hoc that bai: user khong phai sinh vien userId={}", student.getId());
            throw new IllegalArgumentException("Chi sinh vien moi duoc dang ky khoa hoc");
        }
        if (enrollmentRepository.existsByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())) {
            log.warn("Dang ky khoa hoc that bai: sinh vien da dang ky studentId={}, courseId={}", request.getStudentId(), request.getCourseId());
            throw new IllegalArgumentException("Sinh vien da dang ky khoa hoc nay");
        }

        Course course = courseService.findEntity(request.getCourseId());
        CourseEnrollment enrollment = new CourseEnrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.REGISTERED);
        enrollment.setEnrolledAt(LocalDateTime.now());
        CourseEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        log.info("Dang ky khoa hoc thanh cong: enrollmentId={}", savedEnrollment.getId());
        return savedEnrollment;
    }

    @Override
    public List<CourseEnrollment> getStudentEnrollments(Long studentId) {
        log.info("Lay danh sach dang ky khoa hoc: studentId={}", studentId);
        return enrollmentRepository.findByStudentId(studentId);
    }
}
