package cn.edu.zjut.entity;

import cn.edu.zjut.entity.Questions;

public class PaperQuestionWithDetails {
    private int paperQuestionId;
    private Questions question;

    // Getters and Setters
    public int getPaperQuestionId() {
        return paperQuestionId;
    }

    public void setPaperQuestionId(int paperQuestionId) {
        this.paperQuestionId = paperQuestionId;
    }

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }
}
