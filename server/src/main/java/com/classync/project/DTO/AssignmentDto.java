package com.classync.project.DTO;

import java.time.LocalDateTime;

public class AssignmentDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String filePath;
    private final String downloadUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime dueDate;

    public AssignmentDto(Long id, String title, String content, String filePath, String downloadUrl,
            LocalDateTime createdAt, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.filePath = filePath;
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

    public String getFilePath() {
        return filePath;
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