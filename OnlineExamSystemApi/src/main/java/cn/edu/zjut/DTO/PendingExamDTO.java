package cn.edu.zjut.DTO;

import java.sql.Timestamp;

public class PendingExamDTO {
    private int examId;             // 考试 ID
    private String examName;        // 考试名称
    private String examStatus;      // 考试状态（进行中/待批阅）
    private Timestamp startTime;    // 开始时间
    private Timestamp endTime;      // 结束时间

    // Getters and Setters
    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
