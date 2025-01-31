package com.classync.project.services.impl;

import com.classync.project.entity.Announcement;

import java.util.List;

public interface AnnouncementService {
    Announcement createAnnouncement(String title, String content, Long classId, String userEmail);

    List<Announcement> getAnnouncementsByClass(Long classId);
}
