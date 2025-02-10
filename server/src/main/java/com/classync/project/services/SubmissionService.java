package com.classync.project.services;

import java.util.List;

import com.classync.project.entity.Submission;

public interface SubmissionService {
    List<Submission> getSubmissionsByAssignmentAndUser(Long assignmentId, Long submittedById);

    void deleteSubmission(Long submissionId);
}
