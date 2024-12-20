package cn.edu.zjut.service;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.entity.StudentTeachingClassDTO;

import java.util.List;

public interface StudentTeachingClassService {

    // 添加单个学生到教学班
    void addStudentToTeachingClass(StudentTeachingClass studentTeachingClass);

    // 批量添加多个学生到教学班
    void addStudentsToTeachingClass(List<StudentTeachingClass> studentTeachingClasses);
    // 查询教学班内的所有学生信息
    List<Student> findStudentsByTeachingClassId(Integer teachingClassId);

    void deleteStudentFromTeachingClass(String studentNumber, Integer teachingClassId);

    void deleteStudentsFromTeachingClass(List<String> studentNumbers, Integer teachingClassId);
    //查询某个教学班里的所有学生的学号
    List<String> getStudentNumbersByTeachingClassId(int teachingClassId);
    // 查询教学班内所有学生的详细信息，返回 StudentTeachingClassDTO 列表
    List<StudentTeachingClassDTO> findStudentsInTeachingClass(Integer teachingClassId);
}
