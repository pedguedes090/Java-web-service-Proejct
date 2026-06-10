package com.example.project.service.impl;

import com.example.project.models.dto.req.CourseRequest;
import com.example.project.models.entity.Course;
import com.example.project.repository.CourseRepository;
import com.example.project.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public Page<Course> search(String keyword, Pageable pageable) {
        return isBlank(keyword)
                ? courseRepository.findAll(pageable)
                : courseRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Override
    public Course getById(Long id) {
        return findEntity(id);
    }

    @Override
    public Course create(CourseRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ma khoa hoc da ton tai");
        }
        Course course = new Course();
        updateFields(course, request);
        return courseRepository.save(course);
    }

    @Override
    public Course update(Long id, CourseRequest request) {
        Course course = findEntity(id);
        if (!course.getCode().equals(request.getCode()) && courseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ma khoa hoc da ton tai");
        }
        updateFields(course, request);
        return courseRepository.save(course);
    }

    @Override
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Khong tim thay khoa hoc");
        }
        courseRepository.deleteById(id);
    }

    @Override
    public Course findEntity(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay khoa hoc"));
    }

    private void updateFields(Course course, CourseRequest request) {
        course.setCode(request.getCode());
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setTeacherName(request.getTeacherName());
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
