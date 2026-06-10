package com.example.project.service.impl;

import com.example.project.models.dto.req.RegisterStudentRequest;
import com.example.project.models.dto.req.UserRequest;
import com.example.project.models.entity.User;
import com.example.project.models.entity.UserRole;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerStudent(RegisterStudentRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email da ton tai");
        }
        if (userRepository.existsByStudentCode(request.getStudentCode())) {
            throw new IllegalArgumentException("Ma sinh vien da ton tai");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setStudentCode(request.getStudentCode());
        user.setRole(UserRole.STUDENT);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public Page<User> search(String keyword, Pageable pageable) {
        return isBlank(keyword)
                ? userRepository.findAll(pageable)
                : userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Override
    public User getById(Long id) {
        return findEntity(id);
    }

    @Override
    public User create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email da ton tai");
        }
        if (!isBlank(request.getStudentCode()) && userRepository.existsByStudentCode(request.getStudentCode())) {
            throw new IllegalArgumentException("Ma sinh vien da ton tai");
        }

        User user = new User();
        updateUserFields(user, request);
        user.setPassword(passwordEncoder.encode(isBlank(request.getPassword()) ? "123456" : request.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(Long id, UserRequest request) {
        User user = findEntity(id);
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email da ton tai");
        }
        if (!isBlank(request.getStudentCode())
                && (user.getStudentCode() == null || !user.getStudentCode().equals(request.getStudentCode()))
                && userRepository.existsByStudentCode(request.getStudentCode())) {
            throw new IllegalArgumentException("Ma sinh vien da ton tai");
        }

        updateUserFields(user, request);
        if (!isBlank(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Khong tim thay user");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay user"));
    }

    private void updateUserFields(User user, UserRequest request) {
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setStudentCode(request.getStudentCode());
        user.setRole(request.getRole());
        user.setEnabled(request.getEnabled() == null || request.getEnabled());
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
