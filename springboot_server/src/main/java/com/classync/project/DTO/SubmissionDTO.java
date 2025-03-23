package com.classync.project.DTO;

import java.time.LocalDateTime;


public class SubmissionDTO {
    private Long id;
    private Long assignmentId;
    private int submittedById;
    private String submitterName;
    private String submitterEmail;
    private String fileUrl;
    private LocalDateTime submissionDate;
    private String grade;
    private String feedback;
    
    // Constructor
    public SubmissionDTO(Long id, Long assignmentId, int submittedById, 
                         String submitterName, String submitterEmail, 
                         String fileUrl, LocalDateTime submissionDate, 
                         String grade, String feedback) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.submittedById = submittedById;
        this.submitterName = submitterName;
        this.submitterEmail = submitterEmail;
        this.fileUrl = fileUrl;
        this.submissionDate = submissionDate;
        this.grade = grade;
        this.feedback = feedback;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
    
    public int getSubmittedById() { return submittedById; }
    public void setSubmittedById(int submittedById) { this.submittedById = submittedById; }
    
    public String getSubmitterName() { return submitterName; }
    public void setSubmitterName(String submitterName) { this.submitterName = submitterName; }
    
    public String getSubmitterEmail() { return submitterEmail; }
    public void setSubmitterEmail(String submitterEmail) { this.submitterEmail = submitterEmail; }
    
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    
    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}