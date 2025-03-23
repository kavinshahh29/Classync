package com.classync.project.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.classync.project.DTO.AssignmentDto;
import com.classync.project.dao.AssignmentDAO;
import com.classync.project.dao.ClassroomDAO;
import com.classync.project.dao.UserDAO;
import com.classync.project.entity.Assignment;
import com.classync.project.entity.Classroom;
import com.classync.project.entity.Submission;
import com.classync.project.entity.User;
import com.classync.project.repository.AssignmentRepository;
import com.classync.project.repository.SubmissionRepository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentDAO assignmentDAO;

    private final ClassroomDAO classroomDAO;

    private final UserDAO userDAO;

    private final FileUploadService fileUploadService;

    private final SubmissionRepository submissionRepository;

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentDAO assignmentDAO, ClassroomDAO classroomDAO,
            UserDAO userDAO, FileUploadService fileUpload, SubmissionRepository submissionRepository,
            AssignmentRepository assignmentRepository) {
        this.assignmentDAO = assignmentDAO;
        this.classroomDAO = classroomDAO;
        this.userDAO = userDAO;
        this.fileUploadService = fileUpload;
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public Assignment createAssignment(String title, String content, MultipartFile questionFile,
            MultipartFile solutionFile, Long classroomId,
            int createdById, LocalDateTime dueDate) throws IOException {
        Classroom classroom = classroomDAO.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));

        User createdBy = userDAO.findById(createdById)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setContent(content);
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setDueDate(dueDate);
        assignment.setClassroom(classroom);
        assignment.setCreatedBy(createdBy);

        if (questionFile != null && !questionFile.isEmpty()) {
            String questionFileName = System.currentTimeMillis() + "_Q_" + questionFile.getOriginalFilename();
            String questionFileUrl = fileUploadService.uploadAssignment(questionFile, questionFileName);
            assignment.setQuestionFilePath(questionFileUrl);
        }

        if (solutionFile != null && !solutionFile.isEmpty()) {
            String solutionFileName = System.currentTimeMillis() + "_S_" + solutionFile.getOriginalFilename();
            String solutionFileUrl = fileUploadService.uploadTeacherSolution(solutionFile, solutionFileName);
            assignment.setSolutionFilePath(solutionFileUrl);
        }

        return assignmentDAO.save(assignment);
    }

    public Assignment updateAssignment(
            Long assignmentId, String title, String content, LocalDateTime dueDate,
            MultipartFile questionFile, MultipartFile solutionFile) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setTitle(title);
        assignment.setContent(content);
        assignment.setDueDate(dueDate);

        // Update assignment file only if a new file is provided
        if (questionFile != null && !questionFile.isEmpty()) {
            String questionFileName = System.currentTimeMillis() + "_Q_" + questionFile.getOriginalFilename();
            String questionFileUrl = fileUploadService.uploadAssignment(questionFile, questionFileName);
            assignment.setQuestionFilePath(questionFileUrl);
        }

        // Update solution file only if a new file is provided
        if (solutionFile != null && !solutionFile.isEmpty()) {
            String solutionFileName = System.currentTimeMillis() + "_S_" + solutionFile.getOriginalFilename();
            String solutionFileUrl = fileUploadService.uploadTeacherSolution(solutionFile, solutionFileName);
            assignment.setSolutionFilePath(solutionFileUrl);
        }

        return assignmentRepository.save(assignment);
    }

    public Submission createSubmission(Assignment assignment, MultipartFile file, Long classroomId, int submittedbyId)
            throws IOException {
        Classroom classroom = classroomDAO.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));

        User createdBy = userDAO.findById(submittedbyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setClassroom(classroom);
        submission.setStudent(createdBy);
        submission.setSubmittedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            // File convertedFile = fileUploadService.convertToFile(file, fileName);
            String uploadedLink = fileUploadService.uploadSubmission(file, fileName);
            submission.setFileUrl(uploadedLink);
            System.out.println("Uploaded link : " + uploadedLink);
        }

        return submissionRepository.save(submission);
    }

    public List<AssignmentDto> getAssignmentsByClassroom(Long classroomId) {
        // Check if the classroom exists
        Classroom classroom = classroomDAO.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));

        // Retrieve assignments from the database
        List<Assignment> assignments = assignmentDAO.findByClassroom(classroom);

        // return assignments.stream().map(assignment -> {
        // String downloadUrl =
        // fileUploadService.getDownloadUrl(assignment.getFilePath());
        // return new AssignmentDto(
        // assignment.getId(),
        // assignment.getTitle(),
        // assignment.getContent(),
        // assignment.getFilePath(),
        // downloadUrl,
        // assignment.getCreatedAt(),
        // assignment.getDueDate());
        // }).collect(Collectors.toList());
        return assignments.stream().map(assignment -> {
            String questionDownloadUrl = fileUploadService.getDownloadUrl(assignment.getQuestionFilePath());
            // String solutionDownloadUrl =
            // fileUploadService.getDownloadUrl(assignment.getSolutionFilePath());
            return new AssignmentDto(
                    assignment.getId(),
                    assignment.getTitle(),
                    assignment.getContent(),
                    assignment.getQuestionFilePath(),
                    assignment.getSolutionFilePath(),
                    questionDownloadUrl,
                    // solutionDownloadUrl,
                    assignment.getCreatedAt(),
                    assignment.getDueDate());
        }).collect(Collectors.toList());
    }

    public List<AssignmentDto> getAssignmentsByDueDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Assignment> assignments = assignmentRepository.findByDueDateBetween(startDate, endDate);

        return assignments.stream().map(assignment -> {
            String questionDownloadUrl = fileUploadService.getDownloadUrl(assignment.getQuestionFilePath());
            return new AssignmentDto(
                    assignment.getId(),
                    assignment.getTitle(),
                    assignment.getContent(),
                    assignment.getQuestionFilePath(),
                    assignment.getSolutionFilePath(),
                    questionDownloadUrl,
                    assignment.getCreatedAt(),
                    assignment.getDueDate());
        }).collect(Collectors.toList());
    }

    public List<AssignmentDto> getAssignmentsByDueDateRangeAndClassroom(
            LocalDateTime startDate, LocalDateTime endDate, Long classroomId) {
        List<Assignment> assignments = assignmentRepository.findByDueDateBetweenAndClassroomId(startDate, endDate,
                classroomId);

        return assignments.stream().map(assignment -> {
            String questionDownloadUrl = fileUploadService.getDownloadUrl(assignment.getQuestionFilePath());
            return new AssignmentDto(
                    assignment.getId(),
                    assignment.getTitle(),
                    assignment.getContent(),
                    assignment.getQuestionFilePath(),
                    assignment.getSolutionFilePath(),
                    questionDownloadUrl,
                    assignment.getCreatedAt(),
                    assignment.getDueDate());
        }).collect(Collectors.toList());
    }
}