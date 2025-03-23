package com.classync.project.DTO;

import java.time.LocalDateTime;

public class AssignmentDto {
    private Long id;
    private String title;
    private String content;
    private String questionFilePath;
    private String solutionFilePath;
    private String downloadUrl;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

    public AssignmentDto(Long id, String title, String content, String questionFilePath, String solutionFilePath, String downloadUrl, LocalDateTime createdAt, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.questionFilePath = questionFilePath;
        this.solutionFilePath = solutionFilePath;
        this.downloadUrl = downloadUrl;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getQuestionFilePath() {
        return questionFilePath;
    }

    public String getSolutionFilePath() {
        return solutionFilePath;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }
}