package com.classync.project.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.classync.project.entity.Assignment;
import com.classync.project.services.impl.AssignmentService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/classrooms/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createAssignment(
            @RequestParam String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam Long classroomId,
            @RequestParam int createdById) {
        try {
            Assignment assignment = assignmentService.createAssignment(title, content, file, classroomId, createdById);
            return ResponseEntity.ok(assignment);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
