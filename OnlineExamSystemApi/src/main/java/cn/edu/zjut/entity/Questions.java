package cn.edu.zjut.entity;

import java.util.Map;

public class Questions {
    private String questionId; // 使用 MongoDB 默认 ID 类型
    private String content; // 题目内容
    private Map<String, String> options; // 选项 Map
    private Map<String, String> correctAnswers; // 正确答案 Map
    private Integer difficulty; // 难度
    private String tags; // 标签
    private String employeeNumber; // 教师 ID
    private String type; // 新增的题目类型字段
    private String questionBankId; // 题库ID

    // Getters and Setters
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public Map<String, String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Map<String, String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(String questionBankId) {
        this.questionBankId = questionBankId;
    }
}
