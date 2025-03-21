package com.classync.project.services.impl;

import com.classync.project.entity.Announcement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AnnouncementService {
    Announcement createAnnouncement(String title, String content, Long classId, String userEmail , MultipartFile file) throws IOException;

    List<Announcement> getAnnouncementsByClass(Long classId);
}
