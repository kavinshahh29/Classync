package com.classync.project.DTO;




public class DoubtDto {
    private String title;
    private String content;
    private int userId;
    private Long classroomId;


    public DoubtDto() {
    }

    public DoubtDto(String title, String content, int userId, Long classroomId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.classroomId = classroomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public Long getClassroomId() {
        return classroomId;
    }




}
