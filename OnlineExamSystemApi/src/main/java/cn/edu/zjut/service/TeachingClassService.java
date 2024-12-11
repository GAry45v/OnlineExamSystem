package cn.edu.zjut.service;

import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.entity.TeachingClass;

public interface TeachingClassService {

    // 教师创建教学班
    void createTeachingClass(TeachingClass teachingClass);
    void enrollStudentInTeachingClass(StudentTeachingClass studentTeachingClass);
}
