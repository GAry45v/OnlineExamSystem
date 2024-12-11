package cn.edu.zjut.controller;

import cn.edu.zjut.entity.Course;
import cn.edu.zjut.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 获取当前教师ID（假设已通过认证获取教师信息）
    @GetMapping("/teacher/id")
    public ResponseEntity<?> getTeacherId() {
        // 假设获取当前教师的ID，可以通过JWT或Session获取
        Long teacherId = 1L;  // 临时使用1L模拟
        return ResponseEntity.ok(new TeacherResponse(teacherId));
    }

    // 教师创建课程
    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        courseService.createCourse(course);
        return ResponseEntity.ok(new CourseResponse((long) course.getCourseId()));
    }
}

class TeacherResponse {
    private Long teacherId;

    public TeacherResponse(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}

class CourseResponse {
    private Long courseId;

    public CourseResponse(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
