package cn.edu.zjut.entity;

import java.util.Map;

public class AutoGeneratePaperDTO {
    private Map<String, Integer> questionTypeCount;
    private Map<String, Integer> questionTypeScore;
    private Integer targetDifficulty; // 可选参数

    // Getters and Setters
    public Map<String, Integer> getQuestionTypeCount() {
        return questionTypeCount;
    }

    public void setQuestionTypeCount(Map<String, Integer> questionTypeCount) {
        this.questionTypeCount = questionTypeCount;
    }

    public Map<String, Integer> getQuestionTypeScore() {
        return questionTypeScore;
    }

    public void setQuestionTypeScore(Map<String, Integer> questionTypeScore) {
        this.questionTypeScore = questionTypeScore;
    }

    public Integer getTargetDifficulty() {
        return targetDifficulty;
    }

    public void setTargetDifficulty(Integer targetDifficulty) {
        this.targetDifficulty = targetDifficulty;
    }
}
