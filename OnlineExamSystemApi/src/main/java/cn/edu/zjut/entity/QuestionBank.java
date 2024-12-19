package cn.edu.zjut.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Document(collection = "question_banks") // 指定 MongoDB 存储集合
public class QuestionBank {

    // Getters and Setters
    @Id
    private String questionBankId; // 使用 MongoDB 的默认 ID 类型
    private String name;
    private String employeeNumber;
    private List<Questions> questions= new ArrayList<>(); // 存储题目列表

    public void setQuestionBankId(String questionBankId) {
        this.questionBankId = questionBankId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
}
