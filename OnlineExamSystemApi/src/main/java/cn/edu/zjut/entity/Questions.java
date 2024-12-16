package cn.edu.zjut.entity;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Map;

public class Questions {
    @Id
    private String questionId; // 题目 ID
    private String content; // 题目内容
    private Map<String, String> options; // 选项（仅选择题适用）
    private List<String> answers; // 正确答案（所有题型通用）
    private Boolean isAnswerOrdered; // 填空题是否要求答案顺序（仅填空题适用）
    private Integer difficulty; // 难度
    private String tags; // 标签
    private String employeeNumber; // 教师 ID
    private String type; // 题目类型：选择题、填空题、判断题、简答题
    private String questionBankId; // 所属题库 ID
    private List<String> resourcePaths; // 资源路径列表（存储上传到 OSS 的文件路径）

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

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public Boolean getIsAnswerOrdered() {
        return isAnswerOrdered;
    }

    public void setIsAnswerOrdered(Boolean isAnswerOrdered) {
        this.isAnswerOrdered = isAnswerOrdered;
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

    public List<String> getResourcePaths() {
        return resourcePaths;
    }

    public void setResourcePaths(List<String> resourcePaths) {
        this.resourcePaths = resourcePaths;
    }
}
