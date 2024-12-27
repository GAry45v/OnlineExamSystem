
package cn.edu.zjut.controller;

import cn.edu.zjut.DTO.PendingExamDTO;
import cn.edu.zjut.DTO.StudentAnswerDTO;
import cn.edu.zjut.DTO.StudentExamDTO;
import cn.edu.zjut.entity.Exam;
import cn.edu.zjut.service.ExamService;
import cn.edu.zjut.service.StudentAnswerService;
import cn.edu.zjut.service.StudentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exams")
public class GradingController {

    @Autowired
    private ExamService examService;

    @Autowired
    private StudentExamService studentExamService;

    @Autowired
    private StudentAnswerService studentAnswerService;

    // 查询待批阅的考试
//    @GetMapping("/pending-exams")
//    public List<PendingExamDTO> getPendingExams() {
//        List<Exam> exams = examService.findPendingExams();
//        return exams.stream().map(exam -> {
//            PendingExamDTO dto = new PendingExamDTO();
//            dto.setExamId(exam.getExamId());
//            dto.setExamName(exam.getExamName());
//            dto.setExamStatus(exam.getExamStatus());
//            dto.setStartTime(exam.getStartTime());
//            dto.setEndTime(exam.getEndTime());
//            return dto;
//        }).collect(Collectors.toList());
//    }

    // 查询某场考试的所有学生待批阅试卷
    @GetMapping("/{examId}/pending-student-papers")
    public List<StudentExamDTO> getPendingStudentPapers(@PathVariable int examId) {
        return studentExamService.findPendingStudentPapers(examId);
    }

    // 保存教师批阅信息
    @PostMapping("/{examId}/grade")
    public String gradeStudentPaper(@PathVariable int examId, @RequestBody StudentExamDTO studentExamDTO) {
        try {
            for (StudentAnswerDTO answer : studentExamDTO.getAnswers()) {
                if ("简答题".equals(answer.getQuestionType())) {
                    studentAnswerService.updateAnswerGrading(answer);
                }
            }

            studentExamService.updateExamStatusForStudent(examId, studentExamDTO.getStudentNumber(), "已批阅");

            return "批阅信息保存成功！";
        } catch (Exception e) {
            return "批阅信息保存失败：" + e.getMessage();
        }
    }
}
