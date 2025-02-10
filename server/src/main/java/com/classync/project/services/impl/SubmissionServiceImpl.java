package com.classync.project.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.classync.project.entity.Submission;
import com.classync.project.repository.SubmissionRepository;
import com.classync.project.services.SubmissionService;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private SubmissionRepository submissionRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public List<Submission> getSubmissionsByAssignmentAndUser(Long assignmentId, Long submittedById) {
        return submissionRepository.findByAssignmentIdAndStudentId(assignmentId, submittedById);
    }

    @Override
    public void deleteSubmission(Long submissionId) {
        submissionRepository.deleteById(submissionId);
    }

}
