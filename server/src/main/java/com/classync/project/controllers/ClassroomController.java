package com.classync.project.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.classync.project.controllers.UserController.UserDetails;
import com.classync.project.entity.Classroom;
import com.classync.project.services.impl.ClassroomService;

import lombok.Getter;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClass(@RequestBody Map<String, String> request) {
        String className = request.get("className");
        String useremail = request.get("useremail");
        Classroom classroom = classroomService.createClass(className, useremail);

        return ResponseEntity.ok(new ClassroomDetails(
                classroom.getClassName(),
                classroom.getClassroomCode()));
    }

    @Getter
    public static class ClassroomDetails {
        private final String ClassName;
        private final String Classcode;

        public ClassroomDetails(String className, String classcode) {
            this.ClassName = className;
            this.Classcode = classcode;
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinClass(@RequestBody Map<String, String> request) {
        String classCode = request.get("classCode");
        String useremail = request.get("useremail");
        Classroom classroom = classroomService.joinClass(classCode, useremail);
        return ResponseEntity.ok(new ClassroomDetails(
                classroom.getClassName(),
                classroom.getClassroomCode()));
    }
}
