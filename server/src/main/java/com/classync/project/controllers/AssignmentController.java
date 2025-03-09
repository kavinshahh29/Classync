package com.classync.project.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.classync.project.DTO.EvaluationRequest;
import com.classync.project.DTO.EvaluationResponse;
import com.classync.project.repository.SubmissionRepository;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.classync.project.DTO.AssignmentDto;
import com.classync.project.entity.Assignment;
import com.classync.project.entity.Submission;
import com.classync.project.repository.AssignmentRepository;
import com.classync.project.services.SubmissionService;
import com.classync.project.services.impl.AssignmentService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/classrooms/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentRepository assignmentRepository;

    private final SubmissionRepository submissionRepository;
    private final SubmissionService submissionService;
    private final RestTemplate restTemplate;

    public AssignmentController(AssignmentService assignmentService, AssignmentRepository assignmentRepository,
            SubmissionService submissionService , SubmissionRepository submissionRepository , RestTemplate restTemplate) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.submissionService = submissionService;
        this.submissionRepository = submissionRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/add")
    public ResponseEntity<?> createAssignment(
            @RequestParam String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) MultipartFile solutionFile,
            @RequestParam Long classroomId,
            @RequestParam int createdById,
            @RequestParam LocalDateTime dueDate) {
        try {
            Assignment assignment = assignmentService.createAssignment(title, content, file, solutionFile, classroomId, createdById, dueDate);
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
        String solutionUrl = assignment.getSolutionFilePath();

        try {
            Submission submission = assignmentService.createSubmission(assignment, file, classroomId, submittedById);
            return ResponseEntity.ok(submission);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/submissions")
    public ResponseEntity<?> getSubmissions(
            @RequestParam Long assignmentId,
            @RequestParam Long submittedById) {
        try {
            List<Submission> submissions = submissionService.getSubmissionsByAssignmentAndUser(assignmentId,
                    submittedById);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/submissions/{submissionId}")
    public ResponseEntity<?> deleteSubmission(@PathVariable Long submissionId) {
        try {
            submissionService.deleteSubmission(submissionId);
            return ResponseEntity.ok("Submission deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/submissions/{submissionId}/evaluate")
    public ResponseEntity<?> evaluateSubmission(@PathVariable Long submissionId) {
        System.out.println("Evaluating submission with ID: " + submissionId);
        Optional<Submission> submissionOpt = submissionRepository.findById(submissionId);
        if (submissionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Submission not found.");
        }

        Submission submission = submissionOpt.get();
        Optional<Assignment> assignmentOpt = assignmentRepository.findById(submission.getAssignment().getId());

        if (assignmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Assignment not found.");
        }

        Assignment assignment = assignmentOpt.get();
        String submissionUrl = submission.getFileUrl();
        String solutionUrl = assignment.getSolutionFilePath();



        // Calling Flask API for evaluation
        String flaskApiUrl = "http://127.0.0.1:5000/api/evaluate/submission";
        EvaluationRequest request = new EvaluationRequest(submissionUrl, solutionUrl);

        ResponseEntity<EvaluationResponse> response;
        try {
            response = restTemplate.postForEntity(flaskApiUrl, request, EvaluationResponse.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error during evaluation: " + e.getMessage());
        }



        double points = response.getBody().getScore();

        // points to grade
        String grade = convertPointsToGrade(points);

        submission.setGrade(grade);
        submissionRepository.save(submission);

        //  response with updated grade
        EvaluationResponse evaluationResponse = new EvaluationResponse(points, response.getBody().getFeedback(), grade);

        return ResponseEntity.ok(evaluationResponse);
    }

    private String convertPointsToGrade(double points) {
        if (points >= 90) return "A+";
        if (points >= 80) return "A";
        if (points >= 70) return "B";
        if (points >= 60) return "C";
        if (points >= 50) return "D";
        return "F";
    }



}
