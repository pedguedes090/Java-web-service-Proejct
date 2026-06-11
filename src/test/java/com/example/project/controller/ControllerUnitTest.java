package com.example.project.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.project.models.dto.req.ChangePasswordRequest;
import com.example.project.models.dto.req.GradeSubmissionRequest;
import com.example.project.models.dto.req.LoginRequest;
import com.example.project.models.dto.req.SubmitAssignmentRequest;
import com.example.project.models.dto.req.UserRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.AssignmentSubmission;
import com.example.project.models.entity.User;
import com.example.project.models.entity.UserRole;
import com.example.project.service.AssignmentSubmissionService;
import com.example.project.service.AuthService;
import com.example.project.service.LecturerService;
import com.example.project.service.UserService;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @InjectMocks
    private AdminUserController adminUserController;

    @Mock
    private AssignmentSubmissionService assignmentSubmissionService;

    @InjectMocks
    private StudentAssignmentController studentAssignmentController;

    @Mock
    private LecturerService lecturerService;

    @InjectMocks
    private LecturerController lecturerController;

    @Test
    void loginReturnsTokenData() {
        LoginRequest request = new LoginRequest();
        request.setEmail("a@gmail.com");
        request.setPassword("123456");

        Map<String, Object> tokenData = Map.of("accessToken", "access-token");
        when(authService.login(request)).thenReturn(tokenData);

        responseDto<Map<String, Object>> response = authController.login(request);

        assertEquals(true, response.getSuccess());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("access-token", response.getData().get("accessToken"));
    }

    @Test
    void changePasswordReturnsSuccess() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setUserId(1L);
        request.setOldPassword("123456");
        request.setNewPassword("654321");

        responseDto<Void> response = authController.changePassword(request);

        assertEquals(true, response.getSuccess());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertNull(response.getData());
        verify(userService).changePassword(request);
    }

    @Test
    void createUserReturnsCreatedResponse() {
        UserRequest request = new UserRequest();
        request.setFullName("Admin");
        request.setEmail("admin@gmail.com");
        request.setRole(UserRole.ADMIN);

        User user = new User();
        user.setId(1L);
        user.setFullName("Admin");
        user.setRole(UserRole.ADMIN);

        when(userService.create(any(UserRequest.class))).thenReturn(user);

        responseDto<User> response = adminUserController.create(request);

        assertEquals(true, response.getSuccess());
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(1L, response.getData().getId());
    }

    @Test
    void submitAssignmentReturnsCreatedResponse() {
        SubmitAssignmentRequest request = new SubmitAssignmentRequest();
        request.setStudentId(1L);
        request.setCourseId(2L);
        request.setTitle("Bai tap 1");
        request.setSubmissionLink("https://example.com");

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setId(10L);
        submission.setTitle("Bai tap 1");

        when(assignmentSubmissionService.submit(request)).thenReturn(submission);

        responseDto<AssignmentSubmission> response = studentAssignmentController.submit(request);

        assertEquals(true, response.getSuccess());
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(10L, response.getData().getId());
    }

    @Test
    void gradeSubmissionReturnsSuccessResponse() {
        GradeSubmissionRequest request = new GradeSubmissionRequest();
        request.setLecturerId(3L);
        request.setSubmissionId(4L);

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setId(4L);

        when(lecturerService.gradeSubmission(request)).thenReturn(submission);

        responseDto<AssignmentSubmission> response = lecturerController.gradeSubmission(request);

        assertEquals(true, response.getSuccess());
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(4L, response.getData().getId());
    }
}
