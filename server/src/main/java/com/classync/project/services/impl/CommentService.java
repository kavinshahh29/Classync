package com.classync.project.services.impl;

import com.classync.project.entity.Comment;
import com.classync.project.entity.Announcement;
import com.classync.project.entity.User;
import com.classync.project.repository.CommentRepository;
import com.classync.project.repository.AnnouncementRepository;
import com.classync.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, AnnouncementRepository announcementRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
    }

    public Comment addComment(Integer userId, Long announcementId, String content) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Announcement> announcementOpt = announcementRepository.findById(announcementId);

        if (userOpt.isEmpty() || announcementOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid user or announcement ID");
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(userOpt.get());
        comment.setAnnouncement(announcementOpt.get());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        return commentRepository.findByAnnouncementOrderByCreatedAtAsc(announcement);
    }
}
