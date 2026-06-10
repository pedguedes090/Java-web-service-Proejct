package com.example.project.repository;

import com.example.project.models.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);

    Page<Course> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
            String name,
            String code,
            Pageable pageable
    );
}
