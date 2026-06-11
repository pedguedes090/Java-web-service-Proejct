package com.example.project.service;

import com.example.project.models.dto.req.RegisterStudentRequest;
import com.example.project.models.dto.req.UserRequest;
import com.example.project.models.dto.req.ChangePasswordRequest;
import com.example.project.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User registerStudent(RegisterStudentRequest request);

    Page<User> search(String keyword, Pageable pageable);

    User getById(Long id);

    User create(UserRequest request);

    User update(Long id, UserRequest request);

    void delete(Long id);

    User findEntity(Long id);

    void changePassword(ChangePasswordRequest request);
}
