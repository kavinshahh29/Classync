package com.classync.project.services.impl;

import com.classync.project.entity.Announcement;
import com.classync.project.entity.Classroom;
import com.classync.project.entity.User;
import com.classync.project.repository.AnnouncementRepository;
import com.classync.project.repository.ClassRepository;
import com.classync.project.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ClassRepository classroomRepository;
    private final UserRepository userRepository;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository,
            ClassRepository classroomRepository, UserRepository userRepository) {
        this.announcementRepository = announcementRepository;
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Announcement createAnnouncement(String title, String content, Long classId, String userEmail) {
        Classroom classroom = classroomRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));
        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        System.out.println("User id is" + user.getEmail());
        System.out.println("CLASSROOM NAME IS" + classroom.getClassName());

        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setCourseClass(classroom);
        announcement.setAuthor(user);

        return announcementRepository.save(announcement);
    }

    @Override
    public List<Announcement> getAnnouncementsByClass(Long classId) {
        return announcementRepository.findByCourseClassId(classId);
    }

}
