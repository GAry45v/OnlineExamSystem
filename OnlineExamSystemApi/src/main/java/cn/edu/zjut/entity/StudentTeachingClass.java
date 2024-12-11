package cn.edu.zjut.entity;

import java.sql.Timestamp;

public class StudentTeachingClass {

    private Integer studentTeachingClassId;
    private Integer studentId;
    private Integer teachingClassId;
    private Timestamp enrollmentDate;

    // Getters and Setters
    public Integer getStudentTeachingClassId() {
        return studentTeachingClassId;
    }

    public void setStudentTeachingClassId(Integer studentTeachingClassId) {
        this.studentTeachingClassId = studentTeachingClassId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getTeachingClassId() {
        return teachingClassId;
    }

    public void setTeachingClassId(Integer teachingClassId) {
        this.teachingClassId = teachingClassId;
    }

    public Timestamp getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Timestamp enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
