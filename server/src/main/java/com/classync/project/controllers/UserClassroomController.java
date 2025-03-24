package com.classync.project.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.classync.project.services.UserClassroomService;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/userclassroom")
public class UserClassroomController {

    @Autowired
    private UserClassroomService userClassroomService;

    @PutMapping("/updaterole")
    public ResponseEntity<String> updateUserRole(@RequestBody Map<String, String> request) {
        try {
            int userId = Integer.parseInt(request.get("userId"));
            Long classroomId = Long.parseLong(request.get("classroomId"));
            String newRoleName = request.get("newRoleName");

            userClassroomService.updateUserRole(userId, classroomId, newRoleName);
            return ResponseEntity.ok("Role updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/unenroll")
    public ResponseEntity<String> unenrollFromClassroom(@RequestBody Map<String, String> request) {
        try {
            String useremail = request.get("useremail");
            Long classroomId = Long.parseLong(request.get("classroomId"));

            boolean success = userClassroomService.unenrollStudent(useremail, classroomId);

            if (success) {
                return ResponseEntity.ok("Successfully unenrolled from classroom");
            } else {
                return ResponseEntity.badRequest()
                        .body("Unable to unenroll. Only students can unenroll from classrooms.");
            }
        } catch (RuntimeException e) {
            System.out.println("===================> " + e.getMessage());
            
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}