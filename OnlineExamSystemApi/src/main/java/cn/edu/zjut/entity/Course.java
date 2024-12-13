package cn.edu.zjut.entity;

public class Course {
    private int courseId;
    private String courseName;
    private String semester;
    private String createdByemployeeNumber;

    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCreatedByTeacherId() {
        return createdByemployeeNumber;
    }

    public void setCreatedByTeacherId(String createdByTeacherId) {
        this.createdByemployeeNumber = createdByTeacherId;
    }
}
