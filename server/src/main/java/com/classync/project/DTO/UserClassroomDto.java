package com.classync.project.DTO;

import com.classync.project.entity.Classroom;

public class UserClassroomDto {
    private Classroom classroom;
    private String role;

    public UserClassroomDto(Classroom classroom, String role) {
        this.classroom = classroom;
        this.role = role;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public String getRole() {
        return role;
    }
}

