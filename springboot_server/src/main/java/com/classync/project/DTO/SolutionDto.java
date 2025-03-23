package com.classync.project.DTO;

import lombok.Getter;
import lombok.Setter;


public class SolutionDto {
    private Long doubtId;
    private int userId;
    private String content;


    public SolutionDto() {
    }

    public SolutionDto(Long doubtId, int userId, String content) {
        this.doubtId = doubtId;
        this.userId = userId;
        this.content = content;
    }

    public Long getDoubtId() {
        return doubtId;
    }

    public void setDoubtId(Long doubtId) {
        this.doubtId = doubtId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}