package com.classync.project.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoubtDto {
    private String title;
    private String content;
    private int userId;
    private Long classroomId;
}
