package cn.edu.zjut.service;

import cn.edu.zjut.entity.Course;

import java.util.List;

public interface CourseService {

    // 教师创建课程
    void createCourse(Course course);

    // 教师删除课程
    void deleteCourse(Integer courseId);
    List<Course> getCoursesByemployeeNumber(String employeeNumber);
}
