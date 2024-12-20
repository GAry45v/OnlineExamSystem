package cn.edu.zjut.service;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentDTO;

import java.util.List;

public interface StudentService {

    // 按学号或姓名查找学生
    List<StudentDTO> findStudentByStudentNumberOrName(String studentNumber, String name);
    List<StudentDTO> getStudentsByTeachingClassId(Integer teachingClassId);
    // 按学院、专业、行政班级查找学生

    List<StudentDTO> findStudentsByCollegeMajorClass(String collegeName, String majorName, String className);

    List<Student> findStudentsByStudentNumbers(List<String> studentNumbers);

    StudentDTO getStudentByStudentNumber(String studentNumber);
}
