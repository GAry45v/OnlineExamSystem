package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.*;
import cn.edu.zjut.mapper.ExamMapper;
import cn.edu.zjut.mapper.PapersMapper;
import cn.edu.zjut.mapper.StudentExamMapper;
import cn.edu.zjut.mapper.TeacherMapper;
import cn.edu.zjut.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

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
    public Exam findExamById(int examId) {
        Exam exam = examMapper.findExamById(examId);
        if (exam == null) {
            throw new RuntimeException("考试不存在，ID: " + examId);
        }
        return exam;
    }

}