package com.classync.project.services.impl;

import com.classync.project.entity.Announcement;
import com.classync.project.entity.Classroom;
import com.classync.project.entity.User;
import com.classync.project.repository.AnnouncementRepository;
import com.classync.project.repository.ClassRepository;
import com.classync.project.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ClassRepository classroomRepository;
    private final UserRepository userRepository;

    private final FileUploadService fileUploadService;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository,
            ClassRepository classroomRepository, UserRepository userRepository ,
                                   FileUploadService fileUploadService) {
        this.announcementRepository = announcementRepository;
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;
        this.fileUploadService = new FileUploadService();
    }

    @Override
    public Announcement createAnnouncement(String title, String content, Long classId,
                                           String userEmail, MultipartFile file) throws IOException {
        Classroom classroom = classroomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setCourseClass(classroom);
        announcement.setAuthor(user);
        announcement.setCreatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_A_" + file.getOriginalFilename();
            String fileUrl = fileUploadService.uploadAnnouncement(file, fileName);
            announcement.setFileUrl(fileUrl);
        }

        return announcementRepository.save(announcement);
    }

    @Override
    public List<Announcement> getAnnouncementsByClass(Long classId) {
        return announcementRepository.findByCourseClassId(classId);
    }

}
