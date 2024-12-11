package cn.edu.zjut.entity;

public class TeachingClass {
    private int teachingClassId;
    private int courseId;
    private String className;

    // Getters and Setters
    public int getTeachingClassId() {
        return teachingClassId;
    }

    public void setTeachingClassId(int teachingClassId) {
        this.teachingClassId = teachingClassId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
