package cn.edu.zjut.service;

import java.util.List;

public interface StudentTeachingClassService {

    // 添加指定学生到教学班
    void addStudentToTeachingClass(Integer studentId, Integer teachingClassId);

    // 从学生库中调入多个学生到教学班
    void addStudentsToTeachingClass(List<Integer> studentIds, Integer teachingClassId);
}
