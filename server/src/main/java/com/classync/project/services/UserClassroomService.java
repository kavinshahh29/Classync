package com.classync.project.services;


public interface UserClassroomService {
    public boolean updateUserRole(int userId, Long classroomId, String newRole);
}
