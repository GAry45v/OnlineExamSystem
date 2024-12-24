package cn.edu.zjut.service;

import cn.edu.zjut.entity.Exam;
import cn.edu.zjut.entity.StudentExam;

import java.util.List;

public interface ExamService {

    void createExam(Exam exam);
    <ExamDTO> List<ExamDTO> findExamsByTeacher(String employeeNumber);
    void updateExam(Exam exam);
    void deleteExam(int examId);
    Exam findExamById(int examId);

    void publishExamToStudent(StudentExam studentExam);
//    List<Exam> findPendingExams();
}
