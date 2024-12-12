package cn.edu.zjut.entity;

import java.sql.Timestamp;

public class Exam {
    private int examId;
    private String examName;
    private int createdByTeacherId;
    private Timestamp startTime;
    private int durationMinutes;
    private boolean isAntiCheatingEnabled;
    private int paperId;
    private String examStatus;

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

    public int getCreatedByTeacherId() {
        return createdByTeacherId;
    }

    public void setCreatedByTeacherId(int createdByTeacherId) {
        this.createdByTeacherId = createdByTeacherId;
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

    public boolean isAntiCheatingEnabled() {
        return isAntiCheatingEnabled;
    }

    public void setAntiCheatingEnabled(boolean antiCheatingEnabled) {
        isAntiCheatingEnabled = antiCheatingEnabled;
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
}
