package com.classync.project.controllers;

import com.classync.project.DTO.ChatbotTrainingRequest;
import com.classync.project.entity.Announcement;
import com.classync.project.services.impl.AnnouncementService;
import com.classync.project.services.impl.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
public class AnnouncementController {

  private final AnnouncementService announcementService;

  private final RestTemplate restTemplate;


  public AnnouncementController(AnnouncementService announcementService , RestTemplate restTemplate) {
    this.announcementService = announcementService;
    this.restTemplate = restTemplate;

  }

  @PostMapping("/create")
  public ResponseEntity<?> createAnnouncement(
          @RequestParam("title") String title,
          @RequestParam("content") String content,
          @RequestParam("classId") Long classId,
          @RequestParam("userEmail") String userEmail,
          @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {



    Announcement announcement = announcementService.createAnnouncement(title, content, classId, userEmail , file);
    String flaskApiUrl = "http://127.0.0.1:5000/api/chatbot/train";
    ChatbotTrainingRequest chatbotRequest = new ChatbotTrainingRequest(title, content, classId);

    try {
      restTemplate.postForEntity(flaskApiUrl, chatbotRequest, String.class);
    } catch (Exception e) {
      System.out.println("Error sending announcement to chatbot training: " + e.getMessage());
    }
    return ResponseEntity.ok(announcement);
  }

  @GetMapping("/{classId}")
  public ResponseEntity<List<Announcement>> getAnnouncements(@PathVariable Long classId) {
    return ResponseEntity.ok(announcementService.getAnnouncementsByClass(classId));
  }
}
