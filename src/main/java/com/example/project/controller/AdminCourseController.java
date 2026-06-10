package com.example.project.controller;

import com.example.project.models.dto.req.CourseRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.Course;
import com.example.project.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final CourseService courseService;

    @GetMapping
    public responseDto<Page<Course>> search(
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

    @GetMapping("/{id}")
    public responseDto<Course> getById(@PathVariable Long id) {
        return responseDto.<Course>builder()
                .success(true)
                .message("Lay thong tin khoa hoc thanh cong")
                .data(courseService.getById(id))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public responseDto<Course> create(@Valid @RequestBody CourseRequest request) {
        return responseDto.<Course>builder()
                .success(true)
                .message("Tao khoa hoc thanh cong")
                .data(courseService.create(request))
                .errors(null)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public responseDto<Course> update(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return responseDto.<Course>builder()
                .success(true)
                .message("Cap nhat khoa hoc thanh cong")
                .data(courseService.update(id, request))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public responseDto<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return responseDto.<Void>builder()
                .success(true)
                .message("Xoa khoa hoc thanh cong")
                .data(null)
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
