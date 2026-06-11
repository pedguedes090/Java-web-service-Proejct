package com.example.project.service.impl;

import com.example.project.models.dto.req.SubmitAssignmentRequest;
import com.example.project.models.entity.AssignmentSubmission;
import com.example.project.models.entity.Course;
import com.example.project.models.entity.User;
import com.example.project.models.entity.UserRole;
import com.example.project.repository.AssignmentSubmissionRepository;
import com.example.project.repository.CourseEnrollmentRepository;
import com.example.project.service.AssignmentSubmissionService;
import com.example.project.service.CourseService;
import com.example.project.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Override
    public AssignmentSubmission submit(SubmitAssignmentRequest request) {
        User student = userService.findEntity(request.getStudentId());
        if (student.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException("Chi sinh vien moi duoc nop bai");
        }

        Course course = courseService.findEntity(request.getCourseId());
        if (!courseEnrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new IllegalArgumentException("Sinh vien chua dang ky khoa hoc nay");
        }

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudent(student);
        submission.setCourse(course);
        submission.setTitle(request.getTitle());
        submission.setSubmissionLink(request.getSubmissionLink());
        submission.setNote(request.getNote());
        submission.setSubmittedAt(LocalDateTime.now());
        return assignmentSubmissionRepository.save(submission);
    }

    @Override
    public List<AssignmentSubmission> getByStudent(Long studentId) {
        return assignmentSubmissionRepository.findByStudentId(studentId);
    }
}
