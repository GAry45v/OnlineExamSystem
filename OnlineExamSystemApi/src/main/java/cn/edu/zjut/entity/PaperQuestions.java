package cn.edu.zjut.entity;

import javax.persistence.*;

@Entity
@Table(name = "PaperQuestions")
public class PaperQuestions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paperQuestionId;

    private int paperId;
    private String questionType;
    private int marks;
    private int questionOrder;
    private String questionId; // 新增字段，用于关联题库中的题目

    // Getters and Setters
    public int getPaperQuestionId() {
        return paperQuestionId;
    }

    public void setPaperQuestionId(int paperQuestionId) {
        this.paperQuestionId = paperQuestionId;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public int getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(int questionOrder) {
        this.questionOrder = questionOrder;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
