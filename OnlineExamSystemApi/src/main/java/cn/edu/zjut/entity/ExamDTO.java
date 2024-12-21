package cn.edu.zjut.entity;

public class ExamDTO {
    private int examId;
    private String examName;
    private String createdByEmployeeNumber;
    private String startTime;
    private int durationMinutes;
    private boolean isAntiCheatingEnabled;
    private String examStatus;
    private int paperId;
    private String paperName;
    private String paperDescription;
    private int paperTotalMarks;
    private String teacherName;
    private String endTime; // 新增字段：考试结束时间
    // Getters and Setters

    // Getters and Setters
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
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

    public void setAntiCheatingEnabled(boolean isAntiCheatingEnabled) {
        this.isAntiCheatingEnabled = isAntiCheatingEnabled;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperDescription() {
        return paperDescription;
    }

    public void setPaperDescription(String paperDescription) {
        this.paperDescription = paperDescription;
    }

    public int getPaperTotalMarks() {
        return paperTotalMarks;
    }

    public void setPaperTotalMarks(int paperTotalMarks) {
        this.paperTotalMarks = paperTotalMarks;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
