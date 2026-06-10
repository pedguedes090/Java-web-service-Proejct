package com.example.project.service.impl;

import com.example.project.models.dto.req.ClassroomRequest;
import com.example.project.models.entity.Classroom;
import com.example.project.repository.ClassroomRepository;
import com.example.project.service.ClassroomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Override
    public Page<Classroom> search(String keyword, Pageable pageable) {
        return isBlank(keyword)
                ? classroomRepository.findAll(pageable)
                : classroomRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Override
    public Classroom getById(Long id) {
        return findEntity(id);
    }

    @Override
    public Classroom create(ClassroomRequest request) {
        if (classroomRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ma lop da ton tai");
        }
        Classroom classroom = new Classroom();
        updateFields(classroom, request);
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom update(Long id, ClassroomRequest request) {
        Classroom classroom = findEntity(id);
        if (!classroom.getCode().equals(request.getCode()) && classroomRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Ma lop da ton tai");
        }
        updateFields(classroom, request);
        return classroomRepository.save(classroom);
    }

    @Override
    public void delete(Long id) {
        if (!classroomRepository.existsById(id)) {
            throw new EntityNotFoundException("Khong tim thay lop hoc");
        }
        classroomRepository.deleteById(id);
    }

    private Classroom findEntity(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay lop hoc"));
    }

    private void updateFields(Classroom classroom, ClassroomRequest request) {
        classroom.setCode(request.getCode());
        classroom.setName(request.getName());
        classroom.setDescription(request.getDescription());
        classroom.setSemester(request.getSemester());
        classroom.setTeacherName(request.getTeacherName());
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
