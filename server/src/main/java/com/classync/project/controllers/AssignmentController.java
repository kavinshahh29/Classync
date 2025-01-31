package com.classync.project.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.classync.project.DTO.AssignmentDto;
import com.classync.project.entity.Assignment;
import com.classync.project.entity.Submission;
import com.classync.project.repository.AssignmentRepository;
import com.classync.project.services.impl.AssignmentService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/classrooms/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentRepository assignmentRepository;

    public AssignmentController(AssignmentService assignmentService, AssignmentRepository assignmentRepository) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createAssignment(
            @RequestParam String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam Long classroomId,
            @RequestParam int createdById,
            @RequestParam LocalDateTime dueDate) {
        try {
            Assignment assignment = assignmentService.createAssignment(title, content, file, classroomId, createdById,
                    dueDate);
            return ResponseEntity.ok(assignment);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{classroomId}/assignments")
    public ResponseEntity<?> getAssignmentsByClassroom(@PathVariable Long classroomId) {
        try {
            List<AssignmentDto> assignments = assignmentService.getAssignmentsByClassroom(classroomId);
            if (assignments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NO_CONTENT).body("No assignments found for this classroom.");
            }
            return ResponseEntity.ok(assignments);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Invalid classroom ID: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching assignments: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignment(@PathVariable Long id) {
        try {
            Optional<Assignment> assignment = assignmentRepository.findById(id);
            if (assignment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.SC_NO_CONTENT).body("No assignments found for this classroom.");
            }
            return ResponseEntity.ok(assignment);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body("Invalid classroom ID: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching assignments: " + ex.getMessage());
        }
    }

    @PostMapping("/submissions/add")
    public ResponseEntity<?> submitAssignment(
            @RequestParam Long classroomId,
            @RequestParam int submittedById,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam Long assignmentId) {

        Optional<Assignment> assignmentOpt = assignmentRepository.findById(assignmentId);

        Assignment assignment = assignmentOpt.get();

        try {
            Submission submission = assignmentService.createSubmission(assignment, file, classroomId, submittedById);
            return ResponseEntity.ok(submission);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
