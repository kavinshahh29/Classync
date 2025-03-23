package com.classync.project.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classync.project.DTO.UserClassroomDto;
import com.classync.project.entity.Classroom;
import com.classync.project.services.UserClassroomService;
import com.classync.project.services.impl.ClassroomService;

import lombok.Getter;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;
    private final UserClassroomService userClassroomService;

    public ClassroomController(ClassroomService classroomService, UserClassroomService userClassroomService) {
        this.classroomService = classroomService;
        this.userClassroomService = userClassroomService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();
        return ResponseEntity.ok(classrooms);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createClass(@RequestBody Map<String, String> request) {
        String className = request.get("className");
        String useremail = request.get("useremail");
        Classroom classroom = classroomService.createClass(className, useremail);

        return ResponseEntity.ok(new ClassroomDetails(
                classroom.getId(),
                classroom.getClassName(),
                classroom.getClassroomCode()));
    }

    @Getter
    public static class ClassroomDetails {
        private final Long ClassroomId;
        private final String ClassName;
        private final String Classcode;

        public ClassroomDetails(Long classroomId, String className, String classcode) {
            this.ClassroomId = classroomId;
            this.ClassName = className;
            this.Classcode = classcode;
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinClass(@RequestBody Map<String, String> request) {
        try {
            String classroomCode = request.get("classCode");
            String useremail = request.get("useremail");

            Classroom classroom = classroomService.joinClass(classroomCode, useremail);

            return ResponseEntity.ok(classroom);
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while joining the classroom. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/myclassrooms")
    public ResponseEntity<List<UserClassroomDto>> getMyClassrooms(@RequestParam String useremail) {
        List<UserClassroomDto> classrooms = classroomService.getUserClassrooms(useremail);
        return ResponseEntity.ok(classrooms);
    }

    @GetMapping("/{classroomId}")
    public ResponseEntity<?> getClassroomInfo(@PathVariable Long classroomId) {
        try {
            Classroom classroom = classroomService.getClassroomById(classroomId);
            return ResponseEntity.ok(classroom);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

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

    @PutMapping("/{classRoomId}/participants/{userId}/role")
    public ResponseEntity<?> updateParticipantRole(
            @PathVariable Long classRoomId,
            @PathVariable int userId,
            @RequestBody Map<String, String> request) {

        String newRole = request.get("role");
        if (newRole == null || (!newRole.equals("TEACHER") && !newRole.equals("STUDENT"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role"));
        }

        boolean updated = userClassroomService.updateUserRole(userId, classRoomId, newRole);
        if (updated) {
            return ResponseEntity.ok(Map.of("message", "Role updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    @GetMapping("/{classroomId}/check-access")
    public ResponseEntity<?> checkUserAccess(
            @PathVariable Long classroomId,
            @RequestParam Long userId) {

        boolean isMember = userClassroomService.isUserInClassroom(userId, classroomId);
        return ResponseEntity.ok(Map.of("isMember", isMember));
    }
}
