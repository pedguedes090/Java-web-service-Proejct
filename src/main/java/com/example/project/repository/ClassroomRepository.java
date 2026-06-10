package com.example.project.repository;

import com.example.project.models.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    boolean existsByCode(String code);

    Page<Classroom> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
            String name,
            String code,
            Pageable pageable
    );
}
