package cn.edu.zjut.service;

import cn.edu.zjut.DTO.StudentAnswerDTO;
import cn.edu.zjut.entity.StudentAnswerAndGrading;

public interface StudentAnswerService {
    void saveAnswer(StudentAnswerAndGrading answer);

    void updateAnswerGrading(StudentAnswerDTO answer);
}
