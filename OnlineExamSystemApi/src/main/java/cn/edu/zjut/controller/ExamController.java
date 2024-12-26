package cn.edu.zjut.controller;
import cn.edu.zjut.DTO.AnswerPaperDTO;
import cn.edu.zjut.DTO.ExamPaperDTO;
import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.Exam;
import cn.edu.zjut.entity.ExamDTO;
import cn.edu.zjut.service.ExamService;
import cn.edu.zjut.vo.ResponseResult;
import com.beust.jcommander.Parameter;
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
    @GetMapping("/{examId}")
    public Exam getExamById(@PathVariable int examId) {
        return examService.findExamById(examId);
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
    @PostMapping("/getStudentexamIdby{examId}")
    public ResponseResult<List<ExamPaperDTO>> getExamPapers(@PathVariable int examId) {
        try {
            // 调用Service层获取考试信息
            List<ExamPaperDTO> examPapers = examService.findPaperbyexamId(examId);

            // 如果没有找到考试信息
            if (examPapers == null || examPapers.isEmpty()) {
                return ResponseResult.error(404, "No exam information found for the given examId.");
            }

            // 返回成功的结果
            return ResponseResult.success("Exam information retrieved successfully.", examPapers);

        } catch (Exception e) {
            // 异常处理
            return ResponseResult.error("An error occurred while retrieving exam information: " + e.getMessage());
        }
    }
    @PostMapping("/getStudentAnswerPaper/{studentExamId}")
    public ResponseResult<List<AnswerPaperDTO>> getAnswerPaper(@PathVariable int studentExamId, @RequestParam String bankid) {
        try {
            List<AnswerPaperDTO> answerPaper = examService.getAnswerPaperByStudentExamId(studentExamId,bankid);
            return ResponseResult.success(answerPaper);
        } catch (Exception e) {
            return ResponseResult.error("Error retrieving answer paper: " + e.getMessage());
        }
    }
}
