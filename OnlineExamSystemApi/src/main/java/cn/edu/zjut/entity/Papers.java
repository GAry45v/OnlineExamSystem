package cn.edu.zjut.entity;

public class Papers {
    private int paperId;
    private String name;
    private String description;
    private int totalMarks;
    private String employeeNumber;
    private String questionBankId;  // 新增字段

    // Getters and Setters
    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getQuestionBankId() {   // 新增 getter 方法
        return questionBankId;
    }

    public void setQuestionBankId(String questionBankId) {  // 新增 setter 方法
        this.questionBankId = questionBankId;
    }
}
