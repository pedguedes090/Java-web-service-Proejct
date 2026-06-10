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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final CourseEnrollmentRepository enrollmentRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Override
    @Transactional
    public CourseEnrollment enroll(EnrollCourseRequest request) {
        User student = userService.findEntity(request.getStudentId());
        if (student.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException("Chi sinh vien moi duoc dang ky khoa hoc");
        }
        if (enrollmentRepository.existsByStudentIdAndCourseId(request.getStudentId(), request.getCourseId())) {
            throw new IllegalArgumentException("Sinh vien da dang ky khoa hoc nay");
        }

        Course course = courseService.findEntity(request.getCourseId());
        CourseEnrollment enrollment = new CourseEnrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(EnrollmentStatus.REGISTERED);
        enrollment.setEnrolledAt(LocalDateTime.now());
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public List<CourseEnrollment> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }
}
