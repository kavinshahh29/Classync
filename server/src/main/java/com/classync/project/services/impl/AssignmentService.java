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

    public AssignmentService(AssignmentDAO assignmentDAO, ClassroomDAO classroomDAO,
            UserDAO userDAO, FileUploadService fileUpload, SubmissionRepository submissionRepository) {
        this.assignmentDAO = assignmentDAO;
        this.classroomDAO = classroomDAO;
        this.userDAO = userDAO;
        this.fileUploadService = fileUpload;
        this.submissionRepository = submissionRepository;
    }

    public Assignment createAssignment(String title, String content, MultipartFile questionFile, MultipartFile solutionFile, Long classroomId,
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

        // Handle question file upload
        if (questionFile != null && !questionFile.isEmpty()) {
            String questionFileName = System.currentTimeMillis() + "_Q_" + questionFile.getOriginalFilename();
            File convertedFile = fileUploadService.convertToFile(questionFile, questionFileName);
            String questionFileUrl = fileUploadService.uploadPdf(convertedFile, questionFileName);
            assignment.setQuestionFilePath(questionFileUrl);
        }

        // Handle solution file upload
        if (solutionFile != null && !solutionFile.isEmpty()) {
            String solutionFileName = System.currentTimeMillis() + "_S_" + solutionFile.getOriginalFilename();
            File convertedFile = fileUploadService.convertToFile(solutionFile, solutionFileName);
            String solutionFileUrl = fileUploadService.uploadPdf(convertedFile, solutionFileName);
            assignment.setSolutionFilePath(solutionFileUrl);
        }

        return assignmentDAO.save(assignment);
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
            File convertedFile = fileUploadService.convertToFile(file, fileName);
            String uploadedLink = fileUploadService.uploadSubmission(convertedFile, fileName);
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

//        return assignments.stream().map(assignment -> {
//            String downloadUrl = fileUploadService.getDownloadUrl(assignment.getFilePath());
//            return new AssignmentDto(
//                    assignment.getId(),
//                    assignment.getTitle(),
//                    assignment.getContent(),
//                    assignment.getFilePath(),
//                    downloadUrl,
//                    assignment.getCreatedAt(),
//                    assignment.getDueDate());
//        }).collect(Collectors.toList());
        return assignments.stream().map(assignment -> {
            String questionDownloadUrl = fileUploadService.getDownloadUrl(assignment.getQuestionFilePath());
//            String solutionDownloadUrl = fileUploadService.getDownloadUrl(assignment.getSolutionFilePath());
            return new AssignmentDto(
                    assignment.getId(),
                    assignment.getTitle(),
                    assignment.getContent(),
                    assignment.getQuestionFilePath(),
                    assignment.getSolutionFilePath(),
                    questionDownloadUrl,
//                    solutionDownloadUrl,
                    assignment.getCreatedAt(),
                    assignment.getDueDate());
        }).collect(Collectors.toList());
    }
}