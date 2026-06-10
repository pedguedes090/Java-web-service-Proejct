package com.example.project.controller;

import com.example.project.models.dto.req.UserRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.User;
import com.example.project.service.UserService;
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
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public responseDto<Page<User>> search(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return responseDto.<Page<User>>builder()
                .success(true)
                .message("Lay danh sach user thanh cong")
                .data(userService.search(keyword, pageable))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{id}")
    public responseDto<User> getById(@PathVariable Long id) {
        return responseDto.<User>builder()
                .success(true)
                .message("Lay thong tin user thanh cong")
                .data(userService.getById(id))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public responseDto<User> create(@Valid @RequestBody UserRequest request) {
        return responseDto.<User>builder()
                .success(true)
                .message("Tao user thanh cong")
                .data(userService.create(request))
                .errors(null)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public responseDto<User> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return responseDto.<User>builder()
                .success(true)
                .message("Cap nhat user thanh cong")
                .data(userService.update(id, request))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public responseDto<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return responseDto.<Void>builder()
                .success(true)
                .message("Xoa user thanh cong")
                .data(null)
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
