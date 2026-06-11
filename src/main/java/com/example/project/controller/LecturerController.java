package com.example.project.controller;

import com.example.project.models.dto.req.GradeSubmissionRequest;
import com.example.project.models.dto.req.UploadLectureMaterialRequest;
import com.example.project.models.dto.res.responseDto;
import com.example.project.models.entity.AssignmentSubmission;
import com.example.project.models.entity.LectureMaterial;
import com.example.project.service.LecturerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final LecturerService lecturerService;

    @PostMapping("/submissions/grade")
    public responseDto<AssignmentSubmission> gradeSubmission(@Valid @RequestBody GradeSubmissionRequest request) {
        return responseDto.<AssignmentSubmission>builder()
                .success(true)
                .message("Cham diem bai nop thanh cong")
                .data(lecturerService.gradeSubmission(request))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping("/materials/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public responseDto<LectureMaterial> uploadMaterial(@Valid @RequestBody UploadLectureMaterialRequest request) {
        return responseDto.<LectureMaterial>builder()
                .success(true)
                .message("Tai len tai lieu bai giang thanh cong")
                .data(lecturerService.uploadMaterial(request))
                .errors(null)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/materials/courses/{courseId}")
    public responseDto<List<LectureMaterial>> getMaterialsByCourse(@PathVariable Long courseId) {
        return responseDto.<List<LectureMaterial>>builder()
                .success(true)
                .message("Lay danh sach tai lieu thanh cong")
                .data(lecturerService.getMaterialsByCourse(courseId))
                .errors(null)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
