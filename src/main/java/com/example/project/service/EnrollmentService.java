package com.example.project.service;

import com.example.project.models.dto.req.EnrollCourseRequest;
import com.example.project.models.entity.CourseEnrollment;
import java.util.List;

public interface EnrollmentService {

    CourseEnrollment enroll(EnrollCourseRequest request);

    List<CourseEnrollment> getStudentEnrollments(Long studentId);
}
