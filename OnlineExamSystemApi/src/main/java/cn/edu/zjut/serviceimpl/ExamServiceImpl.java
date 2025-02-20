package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.DTO.AnswerPaperDTO;
import cn.edu.zjut.DTO.ExamDTO;
import cn.edu.zjut.DTO.ExamPaperDTO;
import cn.edu.zjut.DTO.TeacherMarkDTO;
import cn.edu.zjut.entity.*;
import cn.edu.zjut.mapper.*;
import cn.edu.zjut.service.ExamService;
import cn.edu.zjut.service.OperationLogService;
import cn.edu.zjut.service.QuestionBankService;
import cn.edu.zjut.util.OperationLogUtil;
import dev.langchain4j.community.model.qianfan.QianfanChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private PaperQuestionsMapper paperQuestionsMapper;

    @Autowired
    private StudentAnswerAndGradingMapper studentAnswerAndGradingMapper;
    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private PapersMapper papersMapper;

    @Autowired
    private TeacherMapper teacherMapper; // 注入 TeacherMapper
    @Autowired
    private StudentExamMapper studentExamMapper;
    @Autowired
    private OperationLogService operationLogService;
    @Override
    public void createExam(Exam exam) {

        examMapper.createExam(exam);
        OperationLog log = OperationLogUtil.createOperationLog("创建考试:"+exam.getExamName());
        operationLogService.addOperationLog(log);
    }
    @Override
    public List<ExamDTO> findExamsByTeacher(String employeeNumber) {
        // 查询教师发布的所有考试
        List<Exam> exams = examMapper.findExamsByTeacher(employeeNumber);

        // 查询教师的姓名
        Teacher teacher = teacherMapper.findTeacherByEmployeeNumber(employeeNumber);

        // 将考试信息与试卷信息结合
        return exams.stream().map(exam -> {
            ExamDTO dto = new ExamDTO();
            dto.setExamId(exam.getExamId());
            dto.setExamName(exam.getExamName());
            dto.setCreatedByEmployeeNumber(exam.getCreatedByEmployeeNumber());
            dto.setStartTime(exam.getStartTime().toString());
            dto.setDurationMinutes(exam.getDurationMinutes());
            dto.setAntiCheatingEnabled(exam.isisAntiCheatingEnabled());
            dto.setExamStatus(exam.getExamStatus());
            dto.setPaperId(exam.getPaperId());
            dto.setEndTime(exam.getEndTime().toString());

            // 获取试卷信息
            Papers paper = papersMapper.findPaperById(exam.getPaperId());
            if (paper != null) {
                dto.setPaperName(paper.getName());
                dto.setPaperDescription(paper.getDescription());
                dto.setPaperTotalMarks(paper.getTotalMarks());
            }

            // 设置教师姓名
            if (teacher != null) {
                dto.setTeacherName(teacher.getName());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateExam(Exam exam) {
        examMapper.updateExam(exam);
    }

    @Override
    public void deleteExam(int examId) {
        examMapper.deleteExam(examId);
        OperationLog log = OperationLogUtil.createOperationLog("删除考试ID:"+examId);
        operationLogService.addOperationLog(log);
    }

    @Override
    public void publishExamToStudent(StudentExam studentExam) {
        studentExamMapper.insertStudentExam(studentExam);

    }

    @Override
    public List<Exam> findPendingExams() {
        return null;
    }

    @Override
    public List<ExamPaperDTO> findPaperbyexamId(int examId) {
        return examMapper.findPendingPaperByExamId(examId);
    }


    @Override
    public List<AnswerPaperDTO> getAnswerPaperByStudentExamId(int studentExamId,String bankid) {

        int paperId=examMapper.getPaperIdByStudentExamId(studentExamId); //获取paperID
        List<StudentAnswerAndGrading> studentAnswers = studentAnswerAndGradingMapper.findAnswersByStudentExamId(studentExamId); //获取所有学生的回答题目
        if (studentAnswers.isEmpty()) {
            throw new RuntimeException("No answers found for studentExamId: " + studentExamId);
        }
        List<AnswerPaperDTO> result = new ArrayList<>();
        for (StudentAnswerAndGrading answer : studentAnswers) {
            int paperQuestionId = answer.getPaperQuestionId(); //获取paperQuestionId
            String aicomment  = answer.getAicomment();
            // Query PaperQuestions table to get the questionId
            PaperQuestions paperQuestion = paperQuestionsMapper.findPaperQuestionById(paperQuestionId);

            if (paperQuestion == null) {
                throw new RuntimeException("Paper question not found for paperQuestionId: " + paperQuestionId);
            }
            String questionId = paperQuestion.getQuestionId(); //获取questionId
            int questionMark = paperQuestion.getMarks();

            // Use QuestionBankService to fetch detailed question data from MongoDB
            Questions question = questionBankService.findQuestionByBankIdAndQuestionId(bankid,questionId); //获取question

            if (question == null) {
                throw new RuntimeException("Question not found for questionId: " + questionId);
            }

            AnswerPaperDTO answerPaperDTO = new AnswerPaperDTO();
            answerPaperDTO.setQuestion_bone(question);
            answerPaperDTO.setAnswerContent(answer.getAnswerContent());
            answerPaperDTO.setAicomment(aicomment);
            answerPaperDTO.setPaperId(paperId);
            answerPaperDTO.setQuestionMark(questionMark);
            result.add(answerPaperDTO);
        }

        return result;
    }

    @Override
    public void updateTeacherMarks(int studentExamId, List<TeacherMarkDTO> teacherMarkDTOList,String graderEmployeeNumber) {
        BigDecimal totalScore = BigDecimal.ZERO;
        for (TeacherMarkDTO markDTO : teacherMarkDTOList) {
            String questionId = markDTO.getQuestion_bone().getQuestionId();
            int paperId=markDTO.getPaperId();
            // 从 PaperQuestions 表中查找 paperQuestionId
            Integer paperQuestionId = paperQuestionsMapper.findPaperQuestionIdByQuestionId(questionId,paperId);
            if (paperQuestionId == null) {
                throw new RuntimeException("Invalid questionId: " + questionId);
            }
            // 更新 StudentAnswerAndGrading 表
            studentAnswerAndGradingMapper.teacher_updateStudentAnswerAndGrading(studentExamId, paperQuestionId, markDTO.getTeachermark(), markDTO.getTeachercomment(), graderEmployeeNumber);
            totalScore = totalScore.add(markDTO.getTeachermark());
        }
        studentExamMapper.updateExamStatusAndScore(studentExamId, "已批阅", totalScore);
        OperationLog log = OperationLogUtil.createOperationLog("教师批卷，考试Id"+studentExamId);
        operationLogService.addOperationLog(log);
    }

    @Override
    public Exam findExamById(int examId) {
        Exam exam = examMapper.findExamById(examId);
        if (exam == null) {
            throw new RuntimeException("考试不存在，ID: " + examId);
        }
        return exam;
    }


}