package cn.edu.zjut.entity;

public class StudentExam {
    private int studentExamId;
    private int examId;
    private String studentNumber;
    private String status;
    private double score;

    // Getters and Setters
    public int getStudentExamId() {
        return studentExamId;
    }

    public void setStudentExamId(int studentExamId) {
        this.studentExamId = studentExamId;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
