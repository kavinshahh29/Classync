package com.classync.project.services;


public interface UserClassroomService {
    public boolean updateUserRole(int userId, Long classroomId, String newRole);
    boolean isUserInClassroom(Long userId, Long classroomId);
    public boolean unenrollStudent(String useremail, Long classroomId);
}
