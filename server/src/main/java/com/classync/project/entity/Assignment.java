package com.classync.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// @Data
// @Entity
// public class Assignment {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false)
//     private String title;

//     @Column(columnDefinition = "TEXT")
//     private String description;

//     @Column(nullable = false)
//     private LocalDateTime dueDate;

//     @ManyToOne
//     @JoinColumn(name = "class_id", nullable = false)
//     private Classroom courseClass;

//     private LocalDateTime createdAt = LocalDateTime.now();

//     private LocalDateTime updatedAt;

//     @PreUpdate
//     public void setUpdatedAt() {
//         this.updatedAt = LocalDateTime.now();
//     }
// }

@Data
@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content; // For text-based assignments

    @Column
    private String filePath; // For PDF file path (if applicable)

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy; // The teacher who created the assignment

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}