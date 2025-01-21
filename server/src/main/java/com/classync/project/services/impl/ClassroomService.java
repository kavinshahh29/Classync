package com.classync.project.services.impl;

import com.classync.project.controllers.UserController.UserDetails;
import com.classync.project.dao.ClassroomDAO;
import com.classync.project.dao.RoleDAO;
import com.classync.project.dao.UserDAO;
import com.classync.project.dao.UserclassroomDAO;
import com.classync.project.entity.Classroom;
import com.classync.project.entity.Role;
import com.classync.project.entity.User;
import com.classync.project.entity.UserClassroom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClassroomService {

    private final ClassroomDAO classroomDAO;
    private final UserDAO userDAO;
    private final UserclassroomDAO userClassroomDAO;
    private final RoleDAO roleDAO;

    public ClassroomService(ClassroomDAO classroomDAO, UserDAO userDAO, UserclassroomDAO userClassroomDAO,
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
        Role teacherRole = roleDAO.findByName("TEACHER")
                .orElseThrow(() -> {
                    System.err.println("TEACHER role not found in the database");
                    return new IllegalArgumentException("TEACHER role not found");
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

    public List<Classroom> getUserClassrooms(String useremail) {
        // Fetch the user by email
        Optional<User> userOpt = userDAO.findByEmail(useremail);
        User user = userOpt.orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<UserClassroom> userClassrooms = userClassroomDAO.findByUser(user);

        List<Classroom> classrooms = userClassrooms.stream()
                .map(UserClassroom::getClassroom)
                .collect(Collectors.toList());

        return classrooms;
    }
}
