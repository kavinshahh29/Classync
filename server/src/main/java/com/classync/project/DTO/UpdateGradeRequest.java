package com.classync.project.DTO;

public class UpdateGradeRequest {
    private String grade;
    private String feedback;
    
    // Default constructor
    public UpdateGradeRequest() {
    }
    
    // Constructor with parameters
    public UpdateGradeRequest(String grade, String feedback) {
        this.grade = grade;
        this.feedback = feedback;
    }
    
    // Getters and setters
    public String getGrade() {
        return grade;
    }
    
    public void setGrade(String grade) {
        this.grade = grade;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}