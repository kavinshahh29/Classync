package com.classync.project.controllers;

import com.classync.project.entity.Comment;
import com.classync.project.services.impl.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public ResponseEntity<Comment> addComment(@RequestBody Map<String, String> request) {
        Integer userId = Integer.parseInt(request.get("userId"));
        Long announcementId = Long.parseLong(request.get("announcementId"));
        String content = request.get("content");

        Comment comment = commentService.addComment(userId, announcementId, content);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/announcement/{announcementId}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long announcementId) {
        List<Comment> comments = commentService.getCommentsByAnnouncement(announcementId);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @RequestParam String userEmail) {
        try {
            commentService.deleteComment(commentId, userEmail);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete comment.");
        }
    }
}
