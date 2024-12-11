package cn.edu.zjut.entity;

import java.sql.Timestamp;

public class StudentAnswerAndGrading {
    private int answerId;
    private int studentExamId;
    private int paperQuestionId;
    private String answerContent;
    private int graderId;
    private double score;
    private Timestamp gradingTime;
    private String comments;
    private String answerStatus;

    // Getters and Setters
    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getStudentExamId() {
        return studentExamId;
    }

    public void setStudentExamId(int studentExamId) {
        this.studentExamId = studentExamId;
    }

    public int getPaperQuestionId() {
        return paperQuestionId;
    }

    public void setPaperQuestionId(int paperQuestionId) {
        this.paperQuestionId = paperQuestionId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public int getGraderId() {
        return graderId;
    }

    public void setGraderId(int graderId) {
        this.graderId = graderId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Timestamp getGradingTime() {
        return gradingTime;
    }

    public void setGradingTime(Timestamp gradingTime) {
        this.gradingTime = gradingTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(String answerStatus) {
        this.answerStatus = answerStatus;
    }
}
