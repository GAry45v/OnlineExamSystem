package cn.edu.zjut.controller;

import cn.edu.zjut.DTO.ExamDTO;
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
            Integer studentExamId = studentExam.getStudentExamId();
            Exam exam = examService.findExamById(studentExam.getExamId());

            Teacher teacher = teacherMapper.findTeacherByEmployeeNumber(exam.getCreatedByEmployeeNumber());

            // 构造 ExamDTO
            ExamDTO examDTO = new ExamDTO();
            examDTO.setExamId(exam.getExamId());
            examDTO.setStudentExamId(studentExamId);
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
            // 首先检查接收到的数据
            System.out.println("收到的考试ID: " + examId);
            System.out.println("答案列表大小: " + answers.size());

            QianfanChatModel model = QianfanChatModel.builder()
                    .apiKey("lL8p3Rm75yzLGHqlyDjZ809S")
                    .secretKey("TlZrivfttBAnkLI2BepyVg9hrj6snOHp")
                    .modelName("Yi-34B-Chat")
                    .build();

            for (StudentAnswerAndGrading answer : answers) {
                // 打印每个答案的详细信息
                System.out.println("处理答案 ID: " + answer.getPaperQuestionId());
                System.out.println("题干内容: " + answer.getContent());
                System.out.println("学生答案: " + answer.getAnswerContent());

                answer.setAnswerStatus("已提交");
                answer.setGradingTime(Timestamp.from(Instant.now()));

                try {
                    studentAnswerService.saveAnswer(answer);
                    System.out.println("答案保存成功");
                } catch (Exception e) {
                    System.out.println("答案保存失败: " + e.getMessage());
                }

                int paperQuestionId = answer.getPaperQuestionId();
                PaperQuestions paperQuestion = paperQuestionsMapper.findPaperQuestionById(paperQuestionId);

                if (paperQuestion == null) {
                    System.out.println("未找到试题信息: " + paperQuestionId);
                    continue;
                }

                // 检查内容是否为空的逻辑优化
                String content = answer.getContent();
                if (content == null || content.trim().isEmpty() || "null".equals(content)) {
                    System.out.println("题目内容为空，跳过AI评分");
                    continue;
                }

                try {
                    int mark = paperQuestion.getMarks();
                    String prompt = String.format("""
                            你现在是一位专业的阅卷老师，请根据以下信息进行评分：
                            题目满分：%d分
                            题目内容：%s
                            学生答案：%s
                            
                            请严格按照以下格式输出评分结果：
                            得分：[数字]
                            评价：[具体评价内容]
                            """,
                            mark,
                            content,
                            answer.getAnswerContent()
                    );
                    System.out.println("发送给AI的prompt: " + prompt);

                    String aianswer = model.generate(prompt);
                    System.out.println("AI返回结果: " + aianswer);

                    if (aianswer != null && !aianswer.trim().isEmpty()) {
                        studentExamMapper.updateaicomment(
                                answer.getStudentExamId(),
                                paperQuestionId,
                                aianswer
                        );
                        System.out.println("AI评分保存成功");
                    }
                } catch (Exception e) {
                    System.out.println("AI评分处理失败: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 更新考试状态
            try {
                studentExamService.updateExamStatus(examId, "待批阅");
                System.out.println("考试状态更新成功");
            } catch (Exception e) {
                System.out.println("考试状态更新失败: " + e.getMessage());
            }

            return "交卷成功！";
        } catch (Exception e) {
            e.printStackTrace();
            return "交卷失败：" + e.getMessage();
        }
    }

}
