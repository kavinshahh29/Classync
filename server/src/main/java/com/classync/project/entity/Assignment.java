package com.classync.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private String questionFilePath; // File for assignment question

    @Column
    private String solutionFilePath; // File for teacher's solution

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;



    public Assignment() {
    }

    public Assignment(String title, String content, String questionFilePath, String solutionFilePath, LocalDateTime createdAt, LocalDateTime dueDate, Classroom classroom, User createdBy) {
        this.title = title;
        this.content = content;
        this.questionFilePath = questionFilePath;
        this.solutionFilePath = solutionFilePath;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.classroom = classroom;
        this.createdBy = createdBy;
    }

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

    public String getQuestionFilePath() {
        return questionFilePath;
    }

    public void setQuestionFilePath(String questionFilePath) {
        this.questionFilePath = questionFilePath;
    }

    public String getSolutionFilePath() {
        return solutionFilePath;
    }

    public void setSolutionFilePath(String solutionFilePath) {
        this.solutionFilePath = solutionFilePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
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

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", questionFilePath='" + questionFilePath + '\'' +
                ", solutionFilePath='" + solutionFilePath + '\'' +
                ", createdAt=" + createdAt +
                ", dueDate=" + dueDate +
                ", classroom=" + classroom +
                ", createdBy=" + createdBy +
                '}';
    }

}
