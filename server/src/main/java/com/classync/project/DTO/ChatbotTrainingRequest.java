package com.classync.project.DTO;

public class ChatbotTrainingRequest {
    private String title;
    private String content;
    private Long classId;

    public ChatbotTrainingRequest(String title, String content, Long classId) {
        this.title = title;
        this.content = content;
        this.classId = classId;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Long getClassId() { return classId; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setClassId(Long classId) { this.classId = classId; }
}
