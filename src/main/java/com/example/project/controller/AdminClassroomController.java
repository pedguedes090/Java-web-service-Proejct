package com.example.project.controller;

import com.example.project.models.dto.req.ClassroomRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.Classroom;
import com.example.project.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/classes")
@RequiredArgsConstructor
public class AdminClassroomController {

    private final ClassroomService classroomService;

    @GetMapping
    public responseDto<Page<Classroom>> search(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return responseDto.<Page<Classroom>>builder()
                .success(true)
                .message("Lay danh sach lop hoc thanh cong")
                .data(classroomService.search(keyword, pageable))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{id}")
    public responseDto<Classroom> getById(@PathVariable Long id) {
        return responseDto.<Classroom>builder()
                .success(true)
                .message("Lay thong tin lop hoc thanh cong")
                .data(classroomService.getById(id))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public responseDto<Classroom> create(@Valid @RequestBody ClassroomRequest request) {
        return responseDto.<Classroom>builder()
                .success(true)
                .message("Tao lop hoc thanh cong")
                .data(classroomService.create(request))
                .errors(null)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public responseDto<Classroom> update(@PathVariable Long id, @Valid @RequestBody ClassroomRequest request) {
        return responseDto.<Classroom>builder()
                .success(true)
                .message("Cap nhat lop hoc thanh cong")
                .data(classroomService.update(id, request))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public responseDto<Void> delete(@PathVariable Long id) {
        classroomService.delete(id);
        return responseDto.<Void>builder()
                .success(true)
                .message("Xoa lop hoc thanh cong")
                .data(null)
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
