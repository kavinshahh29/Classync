package com.classync.project.repository;

import com.classync.project.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentId(Long assignmentId);

    List<Submission> findByStudentId(Integer studentId);
}
