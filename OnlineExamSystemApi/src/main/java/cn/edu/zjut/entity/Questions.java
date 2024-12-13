package cn.edu.zjut.entity;

import com.fasterxml.jackson.databind.JsonNode;

public class Questions {

    private String questionId; // 使用 MongoDB 默认 ID 类型
    private String content; // 题目内容
    private JsonNode options; // 选项 JSON
    private JsonNode correctAnswers; // 正确答案 JSON
    private Integer difficulty; // 难度
    private String tags; // 标签
    private String employeeNumber; // 教师 ID

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

    public JsonNode getOptions() {
        return options;
    }

    public void setOptions(JsonNode options) {
        this.options = options;
    }

    public JsonNode getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(JsonNode correctAnswers) {
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

    public void setQuestionBankId(int i) {

    }
}
