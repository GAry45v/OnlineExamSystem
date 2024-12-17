package cn.edu.zjut.controller;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.Course;
import cn.edu.zjut.entity.TeacherCourse;
import cn.edu.zjut.service.CourseService;
import cn.edu.zjut.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 创建课程
    @PostMapping("/teacher/create_course")
    public ResponseResult<Void> createCourse(@RequestBody Course course) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber();
        try {
            courseService.createCourse(course, employeeNumber);
            return ResponseResult.success("课程创建成功", null);
        } catch (Exception e) {
            return ResponseResult.error("课程创建失败：" + e.getMessage());
        }
    }

    // 删除课程
    @DeleteMapping("/teacher/delete_course")
    public ResponseResult<Void> deleteCourse(@RequestParam Integer courseId) {
        System.out.println("courseId");
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber();

        try {
            courseService.deleteCourse(courseId, employeeNumber);
            return ResponseResult.success("课程删除成功", null);
        } catch (Exception e) {
            return ResponseResult.error("课程删除失败：" + e.getMessage());
        }
    }

    @PostMapping("/teacher/get_course")
    public ResponseResult<List<Course>> getCourse() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String createdByEmployeeNumber = authentication.getUserNumber();

        try {
            List<Course> ret=new ArrayList<>();
            ret=courseService.getCoursesByEmployeeNumber(createdByEmployeeNumber);
            return ResponseResult.success("查询成功", ret);
        } catch (Exception e) {
            return ResponseResult.error("查询失败：" + e.getMessage());
        }
    }
    // 关联课程与其他教师
    @PostMapping("/teacher/associate_teacher")
    public ResponseResult<Void> associateTeacherWithCourse(@RequestBody TeacherCourse teacherCourse) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String createdByEmployeeNumber = authentication.getUserNumber();
        try {
            courseService.associateTeacherWithCourse(teacherCourse, createdByEmployeeNumber);
            return ResponseResult.success("教师关联成功", null);
        } catch (Exception e) {
            return ResponseResult.error("教师关联失败：" + e.getMessage());
        }
    }

    // 解绑课程与教师
    @DeleteMapping("/teacher/disassociate_teacher")
    public ResponseResult<Void> disassociateTeacherFromCourse(@RequestBody TeacherCourse teacherCourse) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String createdByEmployeeNumber = authentication.getUserNumber();

        try {
            courseService.disassociateTeacherFromCourse(teacherCourse, createdByEmployeeNumber);
            return ResponseResult.success("教师解绑成功", null);
        } catch (Exception e) {
            return ResponseResult.error("教师解绑失败：" + e.getMessage());
        }
    }
}
