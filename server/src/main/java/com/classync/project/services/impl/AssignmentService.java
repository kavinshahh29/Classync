package com.classync.project.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.classync.project.DTO.AssignmentDto;
import com.classync.project.dao.AssignmentDAO;
import com.classync.project.dao.ClassroomDAO;
import com.classync.project.dao.UserDAO;
import com.classync.project.entity.Assignment;
import com.classync.project.entity.Classroom;
import com.classync.project.entity.User;

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

    public AssignmentService(AssignmentDAO assignmentDAO, ClassroomDAO classroomDAO,
            UserDAO userDAO, FileUploadService fileUpload) {
        this.assignmentDAO = assignmentDAO;
        this.classroomDAO = classroomDAO;
        this.userDAO = userDAO;
        this.fileUploadService = fileUpload;
    }

    public Assignment createAssignment(String title, String content, MultipartFile file, Long classroomId,
            int createdById , LocalDateTime dueDate) throws IOException {
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

        // Handle file upload (if applicable)
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File convertedFile = fileUploadService.convertToFile(file, fileName);
            String uploadedLink = fileUploadService.uploadPdf(convertedFile, fileName);
            assignment.setFilePath(uploadedLink);
            System.out.println("Uploaded link : " + uploadedLink);
        }
        // String fileName = System.currentTimeMillis() + "_" +
        // file.getOriginalFilename();
        // Path filePath = Paths.get(UPLOAD_DIR + fileName);
        // Files.createDirectories(filePath.getParent());
        // Files.write(filePath, file.getBytes());
        // assignment.setFilePath(filePath.toString());

        return assignmentDAO.save(assignment);
    }

    public List<AssignmentDto> getAssignmentsByClassroom(Long classroomId) {
        // Check if the classroom exists
        Classroom classroom = classroomDAO.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));

        // Retrieve assignments from the database
        List<Assignment> assignments = assignmentDAO.findByClassroom(classroom);

        return assignments.stream().map(assignment -> {
            String downloadUrl = fileUploadService.getDownloadUrl(assignment.getFilePath());
            return new AssignmentDto(
                    assignment.getId(),
                    assignment.getTitle(),
                    assignment.getContent(),
                    assignment.getFilePath(),
                    downloadUrl,
                    assignment.getCreatedAt(),
                    assignment.getDueDate());
        }).collect(Collectors.toList());
    }
}