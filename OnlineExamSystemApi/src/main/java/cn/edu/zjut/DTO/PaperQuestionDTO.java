package cn.edu.zjut.DTO;

import cn.edu.zjut.entity.Questions;

public class PaperQuestionDTO {
    private int paperQuestionId;
    private Questions question;
    private int marks;          // 新增字段，表示题目的分数
    private int questionOrder;  // 新增字段，表示题目的顺序

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

    public int getMarks() { // 新增 getter 方法
        return marks;
    }

    public void setMarks(int marks) { // 新增 setter 方法
        this.marks = marks;
    }

    public int getQuestionOrder() { // 新增 getter 方法
        return questionOrder;
    }

    public void setQuestionOrder(int questionOrder) { // 新增 setter 方法
        this.questionOrder = questionOrder;
    }
}
