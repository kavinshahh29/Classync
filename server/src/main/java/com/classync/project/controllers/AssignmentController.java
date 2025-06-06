package com.classync.project.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.classync.project.DTO.EvaluationRequest;
import com.classync.project.DTO.EvaluationResponse;
import com.classync.project.DTO.SubmissionDTO;
import com.classync.project.DTO.UpdateGradeRequest;
import com.classync.project.repository.SubmissionRepository;
import com.classync.project.repository.UserRepository;

import org.apache.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.classync.project.entity.Classroom;
import com.classync.project.entity.Submission;
import com.classync.project.entity.User;
import com.classync.project.repository.AssignmentRepository;
import com.classync.project.services.SubmissionService;
import com.classync.project.services.impl.AssignmentService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    private final UserRepository userRepository;

    public AssignmentController(AssignmentService assignmentService, AssignmentRepository assignmentRepository,
            SubmissionService submissionService, SubmissionRepository submissionRepository, RestTemplate restTemplate,
            UserRepository userRepository) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.submissionService = submissionService;
        this.submissionRepository = submissionRepository;
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
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

            if (dueDate.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("Due date must be in the future.");
            }

            Assignment assignment = assignmentService.createAssignment(title, content, file, solutionFile, classroomId,
                    createdById, dueDate);
            return ResponseEntity.ok(assignment);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user-assignments")
    public List<AssignmentDto> getAssignments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String classroom,
            @RequestParam String useremail) {
        System.out.println("Received classroom parameter: " + classroom); // Log the classroom parameter
        if (classroom == null || classroom.equals("All classes")) {
            System.out.println("============>assignment fetching for all classrooms");
            return assignmentService.getAssignmentsByDueDateRangeAndUser(startDate, endDate, useremail);
        } else {
            try {
                Long classroomId = Long.parseLong(classroom); // Convert classroom to Long
                return assignmentService.getAssignmentsByDueDateRangeAndClassroom(startDate, endDate, classroomId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid classroom ID: " + classroom);
            }
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

    @PutMapping("/{assignmentId}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long assignmentId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate,
            @RequestParam(required = false) MultipartFile questionFile,
            @RequestParam(required = false) MultipartFile solutionFile) {

        try {
            if (dueDate != null && dueDate.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("Due date must be in the future.");
            }

            Assignment updatedAssignment = assignmentService.updateAssignment(
                    assignmentId, title, content, dueDate, questionFile, solutionFile);

            return ResponseEntity.ok(updatedAssignment);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

        String flaskApiUrl = "http://127.0.0.1:5000/api/evaluate/submission";
        EvaluationRequest request = new EvaluationRequest(submissionUrl, solutionUrl);

        ResponseEntity<EvaluationResponse> response;
        try {
            response = restTemplate.postForEntity(flaskApiUrl, request, EvaluationResponse.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error during evaluation: " + e.getMessage());
        }

        double points = response.getBody().getScore();
        String feedback = response.getBody().getFeedback();
        // points to grade
        String grade = convertPointsToGrade(points);

        submission.setGrade(grade);
        submission.setFeedback(feedback);

        submissionRepository.save(submission);

        // response with updated grade
        EvaluationResponse evaluationResponse = new EvaluationResponse(points, response.getBody().getFeedback(), grade);

        return ResponseEntity.ok(evaluationResponse);
    }

    private String convertPointsToGrade(double points) {
        if (points >= 90)
            return "A+";
        if (points >= 80)
            return "A";
        if (points >= 70)
            return "B";
        if (points >= 60)
            return "C";
        if (points >= 50)
            return "D";
        return "F";
    }

    @PutMapping("/submissions/{submissionId}/update-grade")
    public ResponseEntity<?> updateSubmissionGrade(
            @PathVariable Long submissionId,
            @RequestBody UpdateGradeRequest updateGradeRequest) {

        try {
            Optional<Submission> submissionOpt = submissionRepository.findById(submissionId);
            if (submissionOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Submission not found.");
            }

            Submission submission = submissionOpt.get();

            // Update the grade and feedback
            submission.setGrade(updateGradeRequest.getGrade());
            submission.setFeedback(updateGradeRequest.getFeedback());

            // Save the updated submission
            submissionRepository.save(submission);

            return ResponseEntity.ok(submission);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .body("Error updating grade: " + e.getMessage());
        }
    }

    @GetMapping("/{assignmentId}/all-submissions")
    public ResponseEntity<?> getAllSubmissionsForAssignment(@PathVariable Long assignmentId) {
        try {
            // Check if the assignment exists
            Optional<Assignment> assignmentOpt = assignmentRepository.findById(assignmentId);
            if (assignmentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Assignment not found.");
            }

            // Get all submissions for this assignment
            List<Submission> submissions = submissionService.getAllSubmissionsByAssignment(assignmentId);

            // Transform to include user information
            List<SubmissionDTO> submissionDTOs = submissions.stream()
                    .map(submission -> {
                        User submitter = userRepository.findById(submission.getStudent().getId()).orElse(null);
                        String submitterName = submitter != null ? submitter.getFullName() : "Unknown";
                        String submitterEmail = submitter != null ? submitter.getEmail() : "Unknown";

                        return new SubmissionDTO(
                                submission.getId(),
                                submission.getAssignment().getId(),
                                submission.getStudent().getId(),
                                submitterName,
                                submitterEmail,
                                submission.getFileUrl(),
                                submission.getSubmittedAt(),
                                submission.getGrade(),
                                submission.getFeedback());
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(submissionDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
