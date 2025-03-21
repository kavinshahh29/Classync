package com.classync.project.controllers;

import com.classync.project.entity.Announcement;
import com.classync.project.services.impl.AnnouncementService;
import com.classync.project.services.impl.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
  public ResponseEntity<?> createAnnouncement(
          @RequestParam("title") String title,
          @RequestParam("content") String content,
          @RequestParam("classId") Long classId,
          @RequestParam("userEmail") String userEmail,
          @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {



    Announcement announcement = announcementService.createAnnouncement(title, content, classId, userEmail , file);
    return ResponseEntity.ok(announcement);
  }

  @GetMapping("/{classId}")
  public ResponseEntity<List<Announcement>> getAnnouncements(@PathVariable Long classId) {
    return ResponseEntity.ok(announcementService.getAnnouncementsByClass(classId));
  }
}
