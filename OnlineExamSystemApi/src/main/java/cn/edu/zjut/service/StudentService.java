package cn.edu.zjut.service;

import cn.edu.zjut.entity.Student;
import java.util.List;

public interface StudentService {

    // 按学号或姓名查找学生
    List<Student> findStudentByStudentNumberOrName(String studentNumber, String name);

    // 按学院、专业、行政班级查找学生
    List<Student> findStudentsByCollegeMajorClass(Integer collegeId, Integer majorId, Integer classId);
}
