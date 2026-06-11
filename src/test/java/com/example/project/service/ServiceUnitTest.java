package com.example.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.project.models.dto.req.CourseRequest;
import com.example.project.models.dto.req.EnrollCourseRequest;
import com.example.project.models.dto.req.GradeSubmissionRequest;
import com.example.project.models.dto.req.RegisterStudentRequest;
import com.example.project.models.entity.AssignmentSubmission;
import com.example.project.models.entity.Course;
import com.example.project.models.entity.CourseEnrollment;
import com.example.project.models.entity.EnrollmentStatus;
import com.example.project.models.entity.User;
import com.example.project.models.entity.UserRole;
import com.example.project.repository.AssignmentSubmissionRepository;
import com.example.project.repository.CourseEnrollmentRepository;
import com.example.project.repository.CourseRepository;
import com.example.project.repository.LectureMaterialRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.impl.CourseServiceImpl;
import com.example.project.service.impl.EnrollmentServiceImpl;
import com.example.project.service.impl.LecturerServiceImpl;
import com.example.project.service.impl.UserServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Mock
    private CourseEnrollmentRepository enrollmentRepository;

    @Mock
    private UserService userServiceMock;

    @Mock
    private CourseService courseServiceMock;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Mock
    private AssignmentSubmissionRepository assignmentSubmissionRepository;

    @Mock
    private LectureMaterialRepository lectureMaterialRepository;

    @InjectMocks
    private LecturerServiceImpl lecturerService;

    @Test
    void registerStudentCreatesStudentAccount() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setFullName("Nguyen Van A");
        request.setEmail("a@gmail.com");
        request.setPassword("123456");
        request.setPhone("0909000000");
        request.setStudentCode("SV001");

        when(userRepository.existsByEmail("a@gmail.com")).thenReturn(false);
        when(userRepository.existsByStudentCode("SV001")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerStudent(request);

        assertEquals(UserRole.STUDENT, result.getRole());
        assertEquals("encoded-password", result.getPassword());
        assertTrue(result.getEnabled());
    }

    @Test
    void registerStudentThrowsWhenEmailExists() {
        RegisterStudentRequest request = new RegisterStudentRequest();
        request.setEmail("a@gmail.com");

        when(userRepository.existsByEmail("a@gmail.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.registerStudent(request));
    }

    @Test
    void createCourseThrowsWhenCodeExists() {
        CourseRequest request = new CourseRequest();
        request.setCode("JAVA01");
        request.setName("Lap trinh Java");

        when(courseRepository.existsByCode("JAVA01")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> courseService.create(request));
    }

    @Test
    void enrollCreatesCourseEnrollment() {
        User student = new User();
        student.setId(1L);
        student.setRole(UserRole.STUDENT);

        Course course = new Course();
        course.setId(2L);
        course.setName("Java");

        EnrollCourseRequest request = new EnrollCourseRequest();
        request.setStudentId(1L);
        request.setCourseId(2L);

        when(userServiceMock.findEntity(1L)).thenReturn(student);
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(false);
        when(courseServiceMock.findEntity(2L)).thenReturn(course);
        when(enrollmentRepository.save(any(CourseEnrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CourseEnrollment result = enrollmentService.enroll(request);

        assertEquals(student, result.getStudent());
        assertEquals(course, result.getCourse());
        assertEquals(EnrollmentStatus.REGISTERED, result.getStatus());
    }

    @Test
    void lecturerCanGradeSubmission() {
        User lecturer = new User();
        lecturer.setId(3L);
        lecturer.setRole(UserRole.TEACHER);

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setId(4L);

        GradeSubmissionRequest request = new GradeSubmissionRequest();
        request.setLecturerId(3L);
        request.setSubmissionId(4L);
        request.setScore(BigDecimal.valueOf(8.5));
        request.setFeedback("Tot");

        when(userServiceMock.findEntity(3L)).thenReturn(lecturer);
        when(assignmentSubmissionRepository.findById(4L)).thenReturn(Optional.of(submission));
        when(assignmentSubmissionRepository.save(any(AssignmentSubmission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssignmentSubmission result = lecturerService.gradeSubmission(request);

        assertEquals(BigDecimal.valueOf(8.5), result.getScore());
        assertEquals("Tot", result.getFeedback());
        assertEquals(lecturer, result.getLecturer());
        verify(assignmentSubmissionRepository).save(submission);
    }
}
