package cn.edu.zjut.controller;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.Course;
import cn.edu.zjut.service.CourseService;
import cn.edu.zjut.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 教师创建课程
    @PostMapping("/teacher/create_course")
    public ResponseResult<Void> createCourse(@RequestBody Course course) {
        // 获取当前用户的认证信息
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber();
        Integer userId = authentication.getUserId();
        Integer roleId = authentication.getRoleId();
        // 设置创建课程的教师ID
        course.setCreatedByEmployeeNumber(employeeNumber);
        try {
            // 调用Service层来保存课程
            courseService.createCourse(course);
            return ResponseResult.success("课程创建成功", null);
        } catch (Exception e) {
            // 处理异常并返回错误信息
            return ResponseResult.error("课程创建失败：" + e.getMessage());
        }
    }

    // 教师删除课程
    @DeleteMapping("/teacher/delete_course/courseId")
    public ResponseResult<Void> deleteCourse(@RequestParam Integer courseId) {
        try {
            System.out.println("123"+courseId);
            // 调用Service层删除课程
            courseService.deleteCourse(courseId);
            return ResponseResult.success("课程删除成功", null);
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return ResponseResult.error("课程删除失败：" + e.getMessage());
        }
    }
    // 查询某个教师所创建的所有课程

    @GetMapping("/teacher/search_course")
    public ResponseResult<List<Course>> getCoursesByEmployeeNumber() {
        // 获取当前用户的认证信息
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber();

        try {
            // 调用Service层获取课程列表
            List<Course> courses = courseService.getCoursesByEmployeeNumber(employeeNumber);
            if (courses.isEmpty()) {
                return ResponseResult.success("该教师未创建任何课程", courses);
            }
            return ResponseResult.success("课程列表获取成功", courses);
        } catch (Exception e) {
            // 处理异常并返回错误信息
            return ResponseResult.error("获取课程列表失败：" + e.getMessage());
        }
    }
}
