package cn.edu.zjut.controller;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.service.StudentService;
import cn.edu.zjut.service.StudentTeachingClassService;
import cn.edu.zjut.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            System.out.println(studentNumber);
            if (!studentNumber.equals("空") || !name.equals("空")) {
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
    // 根据教学班 ID 查询所有学生信息
    @GetMapping("/get-students")
    public ResponseResult<List<Student>> getStudentsByTeachingClassId(@RequestParam Integer teachingClassId) {
        try {
            List<Student> students = studentTeachingClassService.findStudentsByTeachingClassId(teachingClassId);
            return ResponseResult.success(students);
        } catch (Exception e) {
            return ResponseResult.error("查询教学班内学生信息失败: " + e.getMessage());
        }
    }
    // 删除单个学生
    @DeleteMapping("/delete-student")
    public ResponseResult<String> deleteStudentFromTeachingClass(@RequestParam String studentNumber, @RequestParam Integer teachingClassId) {
        try {
            studentTeachingClassService.deleteStudentFromTeachingClass(studentNumber, teachingClassId);
            return ResponseResult.success("学生成功从教学班移除");
        } catch (Exception e) {
            return ResponseResult.error("删除学生失败: " + e.getMessage());
        }
    }

    // 批量删除学生
    @DeleteMapping("/delete-students")
    public ResponseResult<String> deleteStudentsFromTeachingClass(@RequestBody Map<String, Object> requestBody) {
        try {
            List<String> studentNumbers = (List<String>) requestBody.get("studentNumbers");
            Integer teachingClassId = (Integer) requestBody.get("teachingClassId");
            studentTeachingClassService.deleteStudentsFromTeachingClass(studentNumbers, teachingClassId);
            return ResponseResult.success("学生批量从教学班移除成功");
        } catch (Exception e) {
            return ResponseResult.error("批量删除学生失败: " + e.getMessage());
        }
    }

}
