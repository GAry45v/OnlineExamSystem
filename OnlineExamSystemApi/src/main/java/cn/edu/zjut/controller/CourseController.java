package cn.edu.zjut.controller;

import cn.edu.zjut.entity.Course;
import cn.edu.zjut.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 教师创建课程
    @PostMapping("/create")
    public String createCourse(@RequestBody Course course) {
        // 假设教师的ID是通过Session或Token等方式获取到的，这里使用一个固定的教师ID作为示例
        Integer teacherId = 1; // 这里需要获取当前登录的教师ID

        course.setCreatedByTeacherId(teacherId); // 设置创建课程的教师ID

        // 调用Service层来保存课程
        courseService.createCourse(course);

        return "课程创建成功";
    }

    // 教师删除课程
    @DeleteMapping("/delete/{courseId}")
    public String deleteCourse(@PathVariable Integer courseId) {
        courseService.deleteCourse(courseId);
        return "课程删除成功";
    }
    // 查询某个教师所创建的所有课程
    @GetMapping("/teacher/{teacherId}")
    public List<Course> getCoursesByTeacherId(@PathVariable Integer teacherId) {
        return courseService.getCoursesByTeacherId(teacherId);
    }
}
