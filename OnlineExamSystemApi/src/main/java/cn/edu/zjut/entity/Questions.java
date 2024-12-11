package cn.edu.zjut.entity;

import com.fasterxml.jackson.databind.JsonNode;

public class Questions {

    private Integer questionId;
    private Integer questionBankId;
    private String content;
    private JsonNode options;         // 用于存储选项的JSON格式数据
    private JsonNode correctAnswers;  // 用于存储正确答案的JSON格式数据
    private Integer difficulty;
    private String tags;
    private Integer teacherId;

    // Getters and Setters
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(Integer questionBankId) {
        this.questionBankId = questionBankId;
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

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
}
