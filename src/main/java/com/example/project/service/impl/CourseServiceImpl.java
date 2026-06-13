package com.example.project.service.impl;

import com.example.project.models.dto.req.CourseRequest;
import com.example.project.models.entity.Course;
import com.example.project.repository.CourseRepository;
import com.example.project.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public Page<Course> search(String keyword, Pageable pageable) {
        log.info("Tim kiem khoa hoc: keyword={}, page={}, size={}", keyword, pageable.getPageNumber(), pageable.getPageSize());
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
        log.info("Tao khoa hoc: code={}", request.getCode());
        if (courseRepository.existsByCode(request.getCode())) {
            log.warn("Tao khoa hoc that bai: ma khoa hoc da ton tai code={}", request.getCode());
            throw new IllegalArgumentException("Ma khoa hoc da ton tai");
        }
        Course course = new Course();
        updateFields(course, request);
        Course savedCourse = courseRepository.save(course);
        log.info("Tao khoa hoc thanh cong: courseId={}", savedCourse.getId());
        return savedCourse;
    }

    @Override
    public Course update(Long id, CourseRequest request) {
        log.info("Cap nhat khoa hoc: courseId={}", id);
        Course course = findEntity(id);
        if (!course.getCode().equals(request.getCode()) && courseRepository.existsByCode(request.getCode())) {
            log.warn("Cap nhat khoa hoc that bai: ma khoa hoc da ton tai code={}", request.getCode());
            throw new IllegalArgumentException("Ma khoa hoc da ton tai");
        }
        updateFields(course, request);
        Course savedCourse = courseRepository.save(course);
        log.info("Cap nhat khoa hoc thanh cong: courseId={}", savedCourse.getId());
        return savedCourse;
    }

    @Override
    public void delete(Long id) {
        log.info("Xoa khoa hoc: courseId={}", id);
        if (!courseRepository.existsById(id)) {
            log.warn("Xoa khoa hoc that bai: khong tim thay courseId={}", id);
            throw new EntityNotFoundException("Khong tim thay khoa hoc");
        }
        courseRepository.deleteById(id);
        log.info("Xoa khoa hoc thanh cong: courseId={}", id);
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
