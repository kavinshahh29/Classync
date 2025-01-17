package com.classync.project.repository;

import com.classync.project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Integer authorId);

    List<Comment> findByAnnouncementId(Long announcementId);

    List<Comment> findByAssignmentId(Long assignmentId);
}
