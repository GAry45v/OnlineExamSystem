package cn.edu.zjut.service;

import cn.edu.zjut.entity.Course;
import cn.edu.zjut.entity.TeacherCourse;

import java.util.List;

public interface CourseService {

    // 教师创建课程
    void createCourse(Course course, String employeeNumber);

    // 教师删除课程
    void deleteCourse(Integer courseId, String employeeNumber);

    // 获取某个教师所创建的所有课程
    List<Course> getCoursesByEmployeeNumber(String employeeNumber);

    // 关联课程与其他教师
    void associateTeacherWithCourse(TeacherCourse teacherCourse, String createdByEmployeeNumber);

    // 解绑课程与其他教师
    void disassociateTeacherFromCourse(TeacherCourse teacherCourse, String createdByEmployeeNumber);

    // 查询课程的所有教师
    List<String> findTeachersByCourseId(Integer courseId);
}
