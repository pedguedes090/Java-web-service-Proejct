package com.example.project.service;

import com.example.project.models.dto.req.ClassroomRequest;
import com.example.project.models.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClassroomService {

    Page<Classroom> search(String keyword, Pageable pageable);

    Classroom getById(Long id);

    Classroom create(ClassroomRequest request);

    Classroom update(Long id, ClassroomRequest request);

    void delete(Long id);
}
