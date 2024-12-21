package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.DTO.StudentAnswerDTO;
import cn.edu.zjut.DTO.StudentExamDTO;
import cn.edu.zjut.entity.StudentExam;
import cn.edu.zjut.mapper.StudentAnswerMapper;
import cn.edu.zjut.mapper.StudentExamMapper;
import cn.edu.zjut.service.StudentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentExamServiceImpl implements StudentExamService {

    @Autowired
    private StudentExamMapper studentExamMapper;

    @Autowired
    private StudentAnswerMapper studentAnswerMapper;
    @Override
    public List<StudentExam> getStudentExams(String studentNumber) {
        return studentExamMapper.findExamsByStudentNumber(studentNumber);
    }

    @Override
    public void updateExamStatus(int examId, String status) {
        studentExamMapper.updateExamStatus(examId, status);
    }
    @Override
    public void updateExamStatusForStudent(int examId, String studentNumber, String status) {
        studentExamMapper.updateStudentExamStatus(examId, studentNumber, status);
    }
    @Override
    public List<StudentExamDTO> findPendingStudentPapers(int examId) {
        List<StudentExam> studentExams = studentExamMapper.findPendingStudentExams(examId);
        return studentExams.stream().map(studentExam -> {
            StudentExamDTO dto = new StudentExamDTO();
            dto.setStudentNumber(studentExam.getStudentNumber());
            dto.setStudentName("Mock Name"); // Replace with a query to fetch student name

            List<StudentAnswerDTO> answers = studentAnswerMapper.findAnswersByStudentExamId(studentExam.getStudentExamId());
            dto.setAnswers(answers);

            return dto;
        }).collect(Collectors.toList());
    }
}
