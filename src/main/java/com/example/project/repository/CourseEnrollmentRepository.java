package com.example.project.repository;

import com.example.project.models.entity.CourseEnrollment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<CourseEnrollment> findByStudentId(Long studentId);
}
