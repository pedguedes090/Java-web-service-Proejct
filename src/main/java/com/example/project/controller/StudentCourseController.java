package com.example.project.controller;

import com.example.project.models.dto.req.EnrollCourseRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.Course;
import com.example.project.models.entity.CourseEnrollment;
import com.example.project.service.CourseService;
import com.example.project.service.EnrollmentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/courses")
@RequiredArgsConstructor
public class StudentCourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @GetMapping
    public responseDto<Page<Course>> searchCourses(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return responseDto.<Page<Course>>builder()
                .success(true)
                .message("Lay danh sach khoa hoc thanh cong")
                .data(courseService.search(keyword, pageable))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping("/enroll")
    @ResponseStatus(HttpStatus.CREATED)
    public responseDto<CourseEnrollment> enroll(@Valid @RequestBody EnrollCourseRequest request) {
        return responseDto.<CourseEnrollment>builder()
                .success(true)
                .message("Dang ky khoa hoc thanh cong")
                .data(enrollmentService.enroll(request))
                .errors(null)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/students/{studentId}/enrollments")
    public responseDto<List<CourseEnrollment>> getStudentEnrollments(@PathVariable Long studentId) {
        return responseDto.<List<CourseEnrollment>>builder()
                .success(true)
                .message("Lay danh sach dang ky thanh cong")
                .data(enrollmentService.getStudentEnrollments(studentId))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
