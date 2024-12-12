package cn.edu.zjut.controller;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 按学号或姓名查找学生
    @GetMapping("/find")
    public List<Student> findStudent(@RequestParam(required = false) String studentNumber,
                                     @RequestParam(required = false) String name) {
        return studentService.findStudentByStudentNumberOrName(studentNumber, name);
    }

    // 按学院、专业、行政班级查找学生
    @GetMapping("/findByClass")
    public List<Student> findStudentsByClass(@RequestParam(required = false) Integer collegeId,
                                             @RequestParam(required = false) Integer majorId,
                                             @RequestParam(required = false) Integer classId) {
        return studentService.findStudentsByCollegeMajorClass(collegeId, majorId, classId);
    }
}
