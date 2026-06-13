package com.example.project.service.impl;

import com.example.project.models.dto.req.ClassroomRequest;
import com.example.project.models.entity.Classroom;
import com.example.project.repository.ClassroomRepository;
import com.example.project.service.ClassroomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Override
    public Page<Classroom> search(String keyword, Pageable pageable) {
        log.info("Tim kiem lop hoc: keyword={}, page={}, size={}", keyword, pageable.getPageNumber(), pageable.getPageSize());
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
        log.info("Tao lop hoc: code={}", request.getCode());
        if (classroomRepository.existsByCode(request.getCode())) {
            log.warn("Tao lop hoc that bai: ma lop da ton tai code={}", request.getCode());
            throw new IllegalArgumentException("Ma lop da ton tai");
        }
        Classroom classroom = new Classroom();
        updateFields(classroom, request);
        Classroom savedClassroom = classroomRepository.save(classroom);
        log.info("Tao lop hoc thanh cong: classroomId={}", savedClassroom.getId());
        return savedClassroom;
    }

    @Override
    public Classroom update(Long id, ClassroomRequest request) {
        log.info("Cap nhat lop hoc: classroomId={}", id);
        Classroom classroom = findEntity(id);
        if (!classroom.getCode().equals(request.getCode()) && classroomRepository.existsByCode(request.getCode())) {
            log.warn("Cap nhat lop hoc that bai: ma lop da ton tai code={}", request.getCode());
            throw new IllegalArgumentException("Ma lop da ton tai");
        }
        updateFields(classroom, request);
        Classroom savedClassroom = classroomRepository.save(classroom);
        log.info("Cap nhat lop hoc thanh cong: classroomId={}", savedClassroom.getId());
        return savedClassroom;
    }

    @Override
    public void delete(Long id) {
        log.info("Xoa lop hoc: classroomId={}", id);
        if (!classroomRepository.existsById(id)) {
            log.warn("Xoa lop hoc that bai: khong tim thay classroomId={}", id);
            throw new EntityNotFoundException("Khong tim thay lop hoc");
        }
        classroomRepository.deleteById(id);
        log.info("Xoa lop hoc thanh cong: classroomId={}", id);
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
