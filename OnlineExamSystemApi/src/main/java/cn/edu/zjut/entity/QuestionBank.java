package cn.edu.zjut.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "question_banks") // 指定 MongoDB 存储集合
public class QuestionBank {

    @Id
    private String questionBankId; // 使用 MongoDB 的默认 ID 类型
    private String name;
    private Integer teacherId;
    private List<Questions> questions; // 存储题目列表

    // Getters and Setters
    public String getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(String questionBankId) {
        this.questionBankId = questionBankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
}
