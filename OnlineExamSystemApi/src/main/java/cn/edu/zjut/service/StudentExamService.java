package cn.edu.zjut.service;

import cn.edu.zjut.DTO.StudentExamDTO;
import cn.edu.zjut.entity.StudentExam;

import java.util.List;

public interface StudentExamService {
    List<StudentExam> getStudentExams(String studentNumber);
    void updateExamStatus(int examId, String status);
    void updateExamStatusForStudent(int examId, String studentNumber, String status);
    List<StudentExamDTO> findPendingStudentPapers(int examId);

}
