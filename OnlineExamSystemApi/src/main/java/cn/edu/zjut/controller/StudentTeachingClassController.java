package cn.edu.zjut.controller;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.service.StudentService;
import cn.edu.zjut.service.StudentTeachingClassService;
import cn.edu.zjut.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student-teaching-class")
public class StudentTeachingClassController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentTeachingClassService studentTeachingClassService;

    // 查询学生信息
    @GetMapping("/students/search")
    public ResponseResult<List<Student>> findStudentByStudentNumberOrName(
            @RequestParam(required = false) String studentNumber,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer collegeId,
            @RequestParam(required = false) Integer majorId,
            @RequestParam(required = false) Integer classId) {
        try {
            List<Student> students;
            if (studentNumber != null || name != null) {
                // 按学号或姓名查询
                students = studentService.findStudentByStudentNumberOrName(studentNumber, name);
            } else {
                // 按学院、专业、行政班级查询
                students = studentService.findStudentsByCollegeMajorClass(collegeId, majorId, classId);
            }
            return ResponseResult.success(students);
        } catch (Exception e) {
            return ResponseResult.error("查询学生信息失败: " + e.getMessage());
        }
    }

    // 添加单个学生到教学班
    @PostMapping("/add-student")
    public ResponseResult<String> addStudentToTeachingClass(@RequestBody StudentTeachingClass studentTeachingClass) {
        try {
            studentTeachingClassService.addStudentToTeachingClass(studentTeachingClass);
            return ResponseResult.success("学生成功加入教学班");
        } catch (Exception e) {
            return ResponseResult.error("添加学生到教学班失败: " + e.getMessage());
        }
    }

    // 批量添加学生到教学班
    @PostMapping("/add-students")
    public ResponseResult<String> addStudentsToTeachingClass(@RequestBody List<StudentTeachingClass> studentTeachingClasses) {
        try {
            studentTeachingClassService.addStudentsToTeachingClass(studentTeachingClasses);
            return ResponseResult.success("学生批量加入教学班成功");
        } catch (Exception e) {
            return ResponseResult.error("批量添加学生到教学班失败: " + e.getMessage());
        }
    }
}
