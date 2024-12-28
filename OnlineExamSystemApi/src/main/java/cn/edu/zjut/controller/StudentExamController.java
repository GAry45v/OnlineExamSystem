package cn.edu.zjut.controller;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.*;
import cn.edu.zjut.mapper.PaperQuestionsMapper;
import cn.edu.zjut.mapper.StudentExamMapper;
import cn.edu.zjut.mapper.TeacherMapper;
import cn.edu.zjut.service.*;
import dev.langchain4j.community.model.qianfan.QianfanChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/student/exams")
public class StudentExamController {
    @Autowired
    private StudentExamMapper studentExamMapper;

    @Autowired
    private PaperQuestionsMapper paperQuestionsMapper;
    @Autowired
    private StudentExamService studentExamService;

    @Autowired
    private ExamService examService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private StudentAnswerService studentAnswerService;
    @Autowired private TeacherMapper teacherMapper;
    @GetMapping
    public List<ExamDTO> getStudentExams() {
        // 获取当前学生的学号
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String studentNumber = authentication.getUserNumber(); // 从 Token 中获取学生编号

        // 查询学生的考试记录
        List<StudentExam> studentExams = studentExamService.getStudentExams(studentNumber);

        // 构造返回的 ExamDTO 列表
        List<ExamDTO> examDTOs = new ArrayList<>();

        for (StudentExam studentExam : studentExams) {
            // 根据 examId 查询 Exam 信息
            Exam exam = examService.findExamById(studentExam.getExamId());


            Teacher teacher = teacherMapper.findTeacherByEmployeeNumber(exam.getCreatedByEmployeeNumber());

            // 构造 ExamDTO
            ExamDTO examDTO = new ExamDTO();
            examDTO.setExamId(exam.getExamId());
            examDTO.setExamName(exam.getExamName());
            examDTO.setCreatedByEmployeeNumber(exam.getCreatedByEmployeeNumber());
            examDTO.setStartTime(exam.getStartTime().toString());
            examDTO.setDurationMinutes(exam.getDurationMinutes());
            examDTO.setAntiCheatingEnabled(exam.isisAntiCheatingEnabled());
            examDTO.setExamStatus(studentExam.getStatus()); // 学生考试状态
            examDTO.setEndTime(String.valueOf(exam.getEndTime()));
            // 设置教师信息
            if (teacher != null) {
                examDTO.setTeacherName(teacher.getName());
            }

            // 将 DTO 添加到列表
            examDTOs.add(examDTO);
        }

        return examDTOs;
    }


    // 获取考试的题目（判断是否开始）
    @GetMapping("/{examId}")
    public Object getExamQuestions(@PathVariable int examId) {
        // 1. 获取考试信息
        Exam exam = examService.findExamById(examId);
        Timestamp now = Timestamp.from(Instant.now());

        // 2. 判断考试是否未开始
        if (now.before(exam.getStartTime())) {
            return "考试尚未开始，请稍后再试。";
        }

        // 3. 判断考试是否已经结束
        if (now.after(exam.getEndTime())) {
            // 获取当前学生的学号
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String studentNumber = authentication.getUserNumber();

            // 更新学生的考试状态为“缺考”
            studentExamService.updateExamStatusForStudent(examId, studentNumber, "缺考");

            return "考试已结束，您无法进入考试，状态已记录为缺考。";
        }

        // 4. 获取试卷题目
        int paperId = exam.getPaperId();
        return paperService.getQuestionsWithDetailsByPaperId(paperId);
    }


    // 提交考试答案
    @PostMapping("/{examId}/submit")
    public String submitExamAnswers(@PathVariable int examId, @RequestBody List<StudentAnswerAndGrading> answers) {
        try {
            QianfanChatModel model = QianfanChatModel.builder()
                .apiKey("lL8p3Rm75yzLGHqlyDjZ809S")
                .secretKey("TlZrivfttBAnkLI2BepyVg9hrj6snOHp")
                .modelName("Yi-34B-Chat") // 一个免费的模型名称
                .build();
            // 遍历答案列表，保存到数据库
            for (StudentAnswerAndGrading answer : answers) {
                answer.setAnswerStatus("已提交");
                answer.setGradingTime(Timestamp.from(Instant.now())); // 设置提交时间
                studentAnswerService.saveAnswer(answer);

                int paperQuestionId = answer.getPaperQuestionId();
                PaperQuestions paperQuestion = paperQuestionsMapper.findPaperQuestionById(paperQuestionId);
                if(!(answer.getContent().isEmpty()||answer.getContent().equals("null"))){
                    String content=answer.getContent();
                    int mark=paperQuestion.getMarks();
                    String prompt="现在你是阅卷老师，这道题目的满分为:"+mark+"题干为："+content+"。请给出该答案的得分和评语"+answer.getAnswerContent()+"给出的内容按照 得分： 评价： ";
                    String aianswer = model.generate(prompt);
                    studentExamMapper.updateaicomment(answer.getStudentExamId(),paperQuestionId,aianswer);
                }
            }

            // 更新学生考试状态为已完成
            studentExamService.updateExamStatus(examId, "待批阅");

            return "交卷成功！";
        } catch (Exception e) {
            return "交卷失败：" + e.getMessage();
        }
    }
}
