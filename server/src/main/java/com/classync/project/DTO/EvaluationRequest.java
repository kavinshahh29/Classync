package com.classync.project.DTO;

public class EvaluationRequest {
    private String submittedFileUrl;
    private String solutionFileUrl;

    public EvaluationRequest() {
    }

    public EvaluationRequest(String submittedFileUrl, String solutionFileUrl) {
        this.submittedFileUrl = submittedFileUrl;
        this.solutionFileUrl = solutionFileUrl;
    }

    public String getSubmittedFileUrl() {
        return submittedFileUrl;
    }

    public void setSubmittedFileUrl(String submittedFileUrl) {
        this.submittedFileUrl = submittedFileUrl;
    }

    public String getSolutionFileUrl() {
        return solutionFileUrl;
    }

    public void setSolutionFileUrl(String solutionFileUrl) {
        this.solutionFileUrl = solutionFileUrl;
    }
}
