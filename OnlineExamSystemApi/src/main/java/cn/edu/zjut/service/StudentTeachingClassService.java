package cn.edu.zjut.service;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentTeachingClass;

import java.util.List;

public interface StudentTeachingClassService {

    // 添加单个学生到教学班
    void addStudentToTeachingClass(StudentTeachingClass studentTeachingClass);

    // 批量添加多个学生到教学班
    void addStudentsToTeachingClass(List<StudentTeachingClass> studentTeachingClasses);
    // 查询教学班内的所有学生信息
    List<Student> findStudentsByTeachingClassId(Integer teachingClassId);
}
