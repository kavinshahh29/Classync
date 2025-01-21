package com.classync.project.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classync.project.entity.Classroom;
import com.classync.project.entity.User;
import com.classync.project.entity.UserClassroom;
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

    @GetMapping("/myclassrooms")
    public ResponseEntity<List<Classroom>> getMyClassrooms(@RequestParam String useremail) {
        List<Classroom> classrooms = classroomService.getUserClassrooms(useremail);
        return ResponseEntity.ok(classrooms);
    }

    // @GetMapping("/{classroomId}/participants")
    // public ResponseEntity<?> getParticipants(@PathVariable Long classroomId) {
    // try {
    // List<User> participants =
    // classroomService.getParticipantsByClassroomId(classroomId);
    // return ResponseEntity.ok(participants);
    // } catch (Exception e) {
    // Map<String, String> errorResponse = new HashMap<>();
    // errorResponse.put("error", e.getMessage());
    // return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    // }
    // }

    @GetMapping("/{classroomId}/participants")
    public ResponseEntity<?> getParticipants(@PathVariable Long classroomId) {
        try {
            List<Map<String, Object>> participants = classroomService.getParticipantsByClassroomId(classroomId);
            return ResponseEntity.ok(participants);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
