package com.example.project.repository;

import com.example.project.models.entity.AssignmentSubmission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    List<AssignmentSubmission> findByStudentId(Long studentId);

    List<AssignmentSubmission> findByCourseId(Long courseId);
}
