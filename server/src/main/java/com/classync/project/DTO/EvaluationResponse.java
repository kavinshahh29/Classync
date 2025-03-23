package com.classync.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class EvaluationResponse {
    public double score;
    public String feedback;
    public String grade;

    public EvaluationResponse() {
    }

    public EvaluationResponse(double score, String feedback, String grade) {
        this.score = score;
        this.feedback = feedback;
        this.grade = grade;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }


    public String getFeedback() {
        return feedback;
    }


    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }


    public String getGrade() {
        return grade;
    }


    public void setGrade(String grade) {
        this.grade = grade;
    }


}
