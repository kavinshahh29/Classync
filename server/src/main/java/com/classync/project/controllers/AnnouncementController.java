
package com.classync.project.controllers;

import com.classync.project.entity.Announcement;
import com.classync.project.services.impl.AnnouncementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAnnouncement(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String content = request.get("content");
        Long classId = Long.parseLong(request.get("classId"));
        String userEmail = request.get("userEmail");

        Announcement announcement = announcementService.createAnnouncement(title, content, classId, userEmail);
        return ResponseEntity.ok(announcement);
    }

    @GetMapping("/{classId}")
    public ResponseEntity<List<Announcement>> getAnnouncements(@PathVariable Long classId) {
        return ResponseEntity.ok(announcementService.getAnnouncementsByClass(classId));
    }
}
