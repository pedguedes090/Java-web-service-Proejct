package com.example.project.service.impl;

import com.example.project.models.dto.req.ChangePasswordRequest;
import com.example.project.models.dto.req.RegisterStudentRequest;
import com.example.project.models.dto.req.UserRequest;
import com.example.project.models.entity.User;
import com.example.project.models.entity.UserRole;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerStudent(RegisterStudentRequest request) {
        log.info("Dang ky sinh vien: email={}, studentCode={}", request.getEmail(), request.getStudentCode());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Dang ky sinh vien that bai: email da ton tai email={}", request.getEmail());
            throw new IllegalArgumentException("Email da ton tai");
        }
        if (userRepository.existsByStudentCode(request.getStudentCode())) {
            log.warn("Dang ky sinh vien that bai: ma sinh vien da ton tai studentCode={}", request.getStudentCode());
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
        User savedUser = userRepository.save(user);
        log.info("Dang ky sinh vien thanh cong: userId={}", savedUser.getId());
        return savedUser;
    }

    @Override
    public Page<User> search(String keyword, Pageable pageable) {
        log.info("Tim kiem user: keyword={}, page={}, size={}", keyword, pageable.getPageNumber(), pageable.getPageSize());
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
        log.info("Admin tao user: email={}, role={}", request.getEmail(), request.getRole());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Tao user that bai: email da ton tai email={}", request.getEmail());
            throw new IllegalArgumentException("Email da ton tai");
        }
        if (!isBlank(request.getStudentCode()) && userRepository.existsByStudentCode(request.getStudentCode())) {
            log.warn("Tao user that bai: ma sinh vien da ton tai studentCode={}", request.getStudentCode());
            throw new IllegalArgumentException("Ma sinh vien da ton tai");
        }

        User user = new User();
        updateUserFields(user, request);
        user.setPassword(passwordEncoder.encode(isBlank(request.getPassword()) ? "123456" : request.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("Tao user thanh cong: userId={}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User update(Long id, UserRequest request) {
        log.info("Cap nhat user: userId={}", id);
        User user = findEntity(id);
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            log.warn("Cap nhat user that bai: email da ton tai email={}", request.getEmail());
            throw new IllegalArgumentException("Email da ton tai");
        }
        if (!isBlank(request.getStudentCode())
                && (user.getStudentCode() == null || !user.getStudentCode().equals(request.getStudentCode()))
                && userRepository.existsByStudentCode(request.getStudentCode())) {
            log.warn("Cap nhat user that bai: ma sinh vien da ton tai studentCode={}", request.getStudentCode());
            throw new IllegalArgumentException("Ma sinh vien da ton tai");
        }

        updateUserFields(user, request);
        if (!isBlank(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        User savedUser = userRepository.save(user);
        log.info("Cap nhat user thanh cong: userId={}", savedUser.getId());
        return savedUser;
    }

    @Override
    public void delete(Long id) {
        log.info("Xoa user: userId={}", id);
        if (!userRepository.existsById(id)) {
            log.warn("Xoa user that bai: khong tim thay userId={}", id);
            throw new EntityNotFoundException("Khong tim thay user");
        }
        userRepository.deleteById(id);
        log.info("Xoa user thanh cong: userId={}", id);
    }

    @Override
    public User findEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Khong tim thay user"));
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        log.info("Doi mat khau: userId={}", request.getUserId());
        User user = findEntity(request.getUserId());
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            log.warn("Doi mat khau that bai: mat khau cu khong dung userId={}", request.getUserId());
            throw new IllegalArgumentException("Mat khau cu khong dung");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Doi mat khau thanh cong: userId={}", request.getUserId());
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
