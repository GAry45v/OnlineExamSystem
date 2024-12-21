package cn.edu.zjut.entity;

import java.sql.Timestamp;

public class Exam {
    private int examId;
    private String examName;
    private String createdByEmployeeNumber;
    private Timestamp startTime;
    private int durationMinutes;
    private boolean isAntiCheatingEnabled;
    private int paperId;
    private String examStatus;
    private Timestamp endTime; // 新增字段：考试结束时间

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

    public String getCreatedByEmployeeNumber() {
        return createdByEmployeeNumber;
    }

    public void setCreatedByEmployeeNumber(String createdByEmployeeNumber) {
        this.createdByEmployeeNumber = createdByEmployeeNumber;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public boolean isisAntiCheatingEnabled() {
        return isAntiCheatingEnabled;
    }

    public void setAntiCheatingEnabled(boolean isAntiCheatingEnabled) {
        this.isAntiCheatingEnabled = isAntiCheatingEnabled;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
}
