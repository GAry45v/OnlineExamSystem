package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.DTO.AnswerPaperDTO;
import cn.edu.zjut.DTO.ExamPaperDTO;
import cn.edu.zjut.DTO.TeacherMarkDTO;
import cn.edu.zjut.entity.*;
import cn.edu.zjut.mapper.*;
import cn.edu.zjut.service.ExamService;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public void createExam(Exam exam) {
        examMapper.createExam(exam);
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
        int paperId=examMapper.getPaperIdByStudentExamId(studentExamId);
        List<StudentAnswerAndGrading> studentAnswers = studentAnswerAndGradingMapper.findAnswersByStudentExamId(studentExamId);
        if (studentAnswers.isEmpty()) {
            throw new RuntimeException("No answers found for studentExamId: " + studentExamId);
        }
        List<AnswerPaperDTO> result = new ArrayList<>();
        for (StudentAnswerAndGrading answer : studentAnswers) {
            int paperQuestionId = answer.getPaperQuestionId();

            // Query PaperQuestions table to get the questionId
            PaperQuestions paperQuestion = paperQuestionsMapper.findPaperQuestionById(paperQuestionId);

            if (paperQuestion == null) {
                throw new RuntimeException("Paper question not found for paperQuestionId: " + paperQuestionId);
            }
            String questionId = paperQuestion.getQuestionId();

            // Use QuestionBankService to fetch detailed question data from MongoDB
            Questions question = questionBankService.findQuestionByBankIdAndQuestionId(bankid,questionId);

            if (question == null) {
                throw new RuntimeException("Question not found for questionId: " + questionId);
            }

            // Create AnswerPaperDTO and populate data
            AnswerPaperDTO answerPaperDTO = new AnswerPaperDTO();
            answerPaperDTO.setQuestion_bone(question);
            answerPaperDTO.setAnswerContent(answer.getAnswerContent());
            answerPaperDTO.setPaperId(paperId);
            result.add(answerPaperDTO);
        }

        return result;
    }

    @Override
    public void updateTeacherMarks(int studentExamId, List<TeacherMarkDTO> teacherMarkDTOList,String graderEmployeeNumber) {
        for (TeacherMarkDTO markDTO : teacherMarkDTOList) {
            String questionId = markDTO.getQuestion_bone().getQuestionId();
            int paperId=markDTO.getPaperId();
            // 从 PaperQuestions 表中查找 paperQuestionId
            Integer paperQuestionId = paperQuestionsMapper.findPaperQuestionIdByQuestionId(questionId,paperId);
            if (paperQuestionId == null) {
                throw new RuntimeException("Invalid questionId: " + questionId);
            }
            System.out.println(markDTO.getTeachermark()+markDTO.getTeachermark());
            // 更新 StudentAnswerAndGrading 表
            studentAnswerAndGradingMapper.teacher_updateStudentAnswerAndGrading(studentExamId, paperQuestionId, markDTO.getTeachermark(), markDTO.getTeachercomment(), graderEmployeeNumber);
        }
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