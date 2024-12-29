package cn.edu.zjut.controller;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.StudentExam;
import cn.edu.zjut.DTO.StudentTeachingClassDTO;
import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.service.ExamService;
import cn.edu.zjut.service.StudentService;
import cn.edu.zjut.service.StudentTeachingClassService;
import cn.edu.zjut.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamPublishController {

    @Autowired
    private TeachingClassService teachingClassService;

    @Autowired
    private StudentTeachingClassService studentTeachingClassService;

    @Autowired
    private ExamService examService;
    @Autowired
    private StudentService studentService;

    // 查询与教师相关的所有教学班
    @GetMapping("/teaching-classes")
    public List<TeachingClass> getTeachingClassesForTeacher() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber(); // 获取教师的编号
        return teachingClassService.findTeachingClassesByEmployeeNumber(employeeNumber);
    }



    // 查询教学班内所有学生的详细信息
    @GetMapping("/{teachingClassId}/students")
    public List<StudentTeachingClassDTO> getStudentsInTeachingClass(@PathVariable Integer teachingClassId) {
        return studentTeachingClassService.findStudentsInTeachingClass(teachingClassId);
    }
//    // 根据学号查询学生详细信息
//    @GetMapping("/{studentNumber}")
//    public StudentDTO getStudentByStudentNumber(@PathVariable String studentNumber) {
//        return studentService.getStudentByStudentNumber(studentNumber);
//    }
// 发布考试到指定学生列表
@PostMapping("/{examId}/publish-to-students")
public String publishExamToStudents(@PathVariable int examId, @RequestBody List<String> studentNumbers) {
    try {
        // 遍历学生学号列表，将考试发布到每个学生
        for (String studentNumber : studentNumbers) {
            StudentExam studentExam = new StudentExam();
            studentExam.setExamId(examId);
            studentExam.setStudentNumber(studentNumber);
            studentExam.setStatus("未完成");
            studentExam.setScore(0.0); // 将分数设置为 null
            examService.publishExamToStudent(studentExam);
        }
        return "考试成功发布到指定学生列表";
    } catch (Exception e) {
        return "考试发布失败: " + e.getMessage();
    }
}

    //发布考试到教学班所有学生
    @PostMapping("/{examId}/publish-to-teaching-class/{teachingClassId}")
    public String publishExamToTeachingClass(@PathVariable int examId, @PathVariable int teachingClassId) {
        try {
            // 获取教学班中的所有学生
            List<String> studentNumbers = studentTeachingClassService.getStudentNumbersByTeachingClassId(teachingClassId);

            // 将考试发布到所有学生
            for (String studentNumber : studentNumbers) {
                StudentExam studentExam = new StudentExam();
                studentExam.setExamId(examId);
                studentExam.setStudentNumber(studentNumber);
                studentExam.setStatus("未完成");
                studentExam.setScore(0.0);
                examService.publishExamToStudent(studentExam);
            }
            return "考试成功发布到教学班 ID: " + teachingClassId;
        } catch (Exception e) {
            return "考试发布失败: " + e.getMessage();
        }
    }
}
