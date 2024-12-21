package cn.edu.zjut.DTO;

import java.util.List;

public class StudentExamDTO {
    private String studentNumber; // 学生学号
    private String studentName;   // 学生姓名
    private List<StudentAnswerDTO> answers; // 学生答题列表

    // Getters and Setters
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<StudentAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<StudentAnswerDTO> answers) {
        this.answers = answers;
    }
}
