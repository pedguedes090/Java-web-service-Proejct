package com.example.project.service;

import com.example.project.models.dto.req.CourseRequest;
import com.example.project.models.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {

    Page<Course> search(String keyword, Pageable pageable);

    Course getById(Long id);

    Course create(CourseRequest request);

    Course update(Long id, CourseRequest request);

    void delete(Long id);

    Course findEntity(Long id);
}
