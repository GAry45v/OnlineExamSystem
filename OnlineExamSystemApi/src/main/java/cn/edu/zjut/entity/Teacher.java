package cn.edu.zjut.entity;

public class Teacher {
    private int teacherId;
    private String name;
    private String employeeNumber;
    private int collegeId;
    private int schoolId;
    private String faceImageId;

    // Getters and Setters
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getFaceImageId() {
        return faceImageId;
    }

    public void setFaceImageId(String faceImageId) {
        this.faceImageId = faceImageId;
    }
}
