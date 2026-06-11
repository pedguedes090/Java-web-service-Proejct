package com.example.project.repository;

import com.example.project.models.entity.LectureMaterial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureMaterialRepository extends JpaRepository<LectureMaterial, Long> {

    List<LectureMaterial> findByCourseId(Long courseId);

    List<LectureMaterial> findByLecturerId(Long lecturerId);
}
