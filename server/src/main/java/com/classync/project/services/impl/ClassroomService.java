package com.classync.project.services.impl;

import com.classync.project.DTO.UserClassroomDto;
import com.classync.project.dao.ClassroomDAO;
import com.classync.project.dao.RoleDAO;
import com.classync.project.dao.UserDAO;
import com.classync.project.dao.UserClassroomDAO;
import com.classync.project.entity.Classroom;
import com.classync.project.entity.Role;
import com.classync.project.entity.User;
import com.classync.project.entity.UserClassroom;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassroomService {

    private final ClassroomDAO classroomDAO;
    private final UserDAO userDAO;
    private final UserClassroomDAO userClassroomDAO;
    private final RoleDAO roleDAO;


    public List<Classroom> getAllClassrooms(){
        return  this.classroomDAO.findAll();
    }

    public ClassroomService(ClassroomDAO classroomDAO, UserDAO userDAO, UserClassroomDAO userClassroomDAO,
            RoleDAO roleDAO) {
        this.classroomDAO = classroomDAO;
        this.userDAO = userDAO;
        this.userClassroomDAO = userClassroomDAO;
        this.roleDAO = roleDAO;
    }

    public Classroom createClass(String className, String useremail) {

        Classroom classroom = new Classroom();
        classroom.setClassName(className);
        classroomDAO.save(classroom);

        // Fetch the user
        User user = userDAO.findByEmail(useremail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch the TEACHER role
        // Role teacherRole = roleDAO.findByName("TEACHER")
        // .orElseThrow(() -> {
        // System.err.println("TEACHER role not found in the database");
        // return new IllegalArgumentException("TEACHER role not found");
        // });
        Role teacherRole = roleDAO.findByName("CREATOR")
                .orElseThrow(() -> {
                    System.err.println("CREATOR role not found in the database");
                    return new IllegalArgumentException("CREATOR role not found");
                });

        // Assign the creator as a TEACHER
        UserClassroom userClassroom = new UserClassroom();
        userClassroom.setUser(user);
        userClassroom.setClassroom(classroom);
        userClassroom.setRole(teacherRole); // Set role as TEACHER
        userClassroom.setJoinedAt(LocalDateTime.now());
        userClassroomDAO.save(userClassroom);

        return classroom;
    }

    public Classroom joinClass(String classroomCode, String useremail) {

        Optional<Classroom> classroomOpt = classroomDAO.findByClassroomCode(classroomCode);

        if (classroomOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid classroom code");
        }
        if (useremail == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        Classroom classroom = classroomOpt.get();
        // Fetch the user
        User user = userDAO.findByEmail(useremail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the user is already a member of the classroom
        Optional<UserClassroom> existingEntry = userClassroomDAO.findByUserAndClassroom(user, classroom);
        if (existingEntry.isPresent()) {
            throw new IllegalArgumentException("User is already a member of this classroom");
        }

        // Fetch the TEACHER role
        Role studentRole = roleDAO.findByName("STUDENT")
                .orElseThrow(() -> {
                    System.err.println("STUDENT role not found in the database");
                    return new IllegalArgumentException("STUDENT role not found");
                });

        // Create a new entry in the userClassroom table
        UserClassroom userClassroom = new UserClassroom();
        userClassroom.setUser(user);
        userClassroom.setClassroom(classroom);
        userClassroom.setRole(studentRole); // Set role as STUDENT
        userClassroom.setJoinedAt(LocalDateTime.now());
        userClassroomDAO.save(userClassroom);

        return classroom;
    }

    public List<UserClassroomDto> getUserClassrooms(String useremail) {
        // Fetch the user by email
        Optional<User> userOpt = userDAO.findByEmail(useremail);
        User user = userOpt.orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<UserClassroom> userClassrooms = userClassroomDAO.findByUser(user);

        // List<Classroom> classrooms = userClassrooms.stream()
        // .map(UserClassroom::getClassroom)
        // .collect(Collectors.toList());

        List<UserClassroomDto> classroomsWithRoles = userClassrooms.stream()
                .map(uc -> new UserClassroomDto(uc.getClassroom(), uc.getRole().getName()))
                .collect(Collectors.toList());

        return classroomsWithRoles;
    }

    public List<Map<String, Object>> getParticipantsByClassroomId(Long classroomId) {
        // Fetch the classroom by ID
        Classroom classroom = classroomDAO.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));

        // Fetch all UserClassroom entries for the classroom
        List<UserClassroom> userClassrooms = userClassroomDAO.findByClassroom(classroom);

        // Map UserClassroom entries to a List of Maps
        return userClassrooms.stream()
                .map(userClassroom -> {
                    User user = userClassroom.getUser();
                    Role role = userClassroom.getRole();
                    // Create a Map to represent the participant
                    Map<String, Object> participant = new HashMap<>();
                    participant.put("id", user.getId());
                    participant.put("fullName", user.getFullName());
                    participant.put("email", user.getEmail());
                    participant.put("picture", user.getPicture());
                    participant.put("role", role.getName());

                    return participant;
                })
                .collect(Collectors.toList());
    }

    public void updateParticipantRole(Long classroomId, String participantEmail, Role newRole) {
        
    }

    public Classroom getClassroomById(Long classroomId) {
        return classroomDAO.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid classroom ID"));
    }
    
}
