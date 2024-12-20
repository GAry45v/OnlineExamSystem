package cn.edu.zjut.controller;
import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.Exam;
import cn.edu.zjut.entity.ExamDTO;
import cn.edu.zjut.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @PostMapping("/publish")
    public String publishExam(@RequestBody Exam exam) {
        try {
            // 从 Token 中获取当前教师的员工编号
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();

            // 设置考试的创建人
            exam.setCreatedByEmployeeNumber(employeeNumber);

            // 设置考试状态为 "已发放"
            exam.setExamStatus("已发放");

            // 创建考试
            examService.createExam(exam);

            return "考试发布成功";
        } catch (Exception e) {
            return "考试发布失败：" + e.getMessage();
        }
    }
    // 获取教师发布的所有考试
    @GetMapping("/teacher")
    public List<ExamDTO> getExamsByTeacher() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber(); // 从 Token 中获取教师编号
        return examService.findExamsByTeacher(employeeNumber);
    }

    // 修改考试信息
    @PutMapping("/{examId}")
    public String updateExam(@PathVariable int examId, @RequestBody Exam exam) {
        try {
            System.out.println("1111111111"+exam.isisAntiCheatingEnabled());
            System.out.println(exam.isisAntiCheatingEnabled());
            exam.setExamId(examId);
            examService.updateExam(exam);
            return "考试信息更新成功";
        } catch (Exception e) {
            return "考试信息更新失败：" + e.getMessage();
        }
    }

    // 删除考试
    @DeleteMapping("/{examId}")
    public String deleteExam(@PathVariable int examId) {
        try {
            examService.deleteExam(examId);
            return "考试删除成功";
        } catch (Exception e) {
            return "考试删除失败：" + e.getMessage();
        }
    }
}
