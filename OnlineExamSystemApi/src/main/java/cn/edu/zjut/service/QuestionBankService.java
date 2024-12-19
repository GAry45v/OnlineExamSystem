package cn.edu.zjut.service;

import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionBankService {
    void createQuestionBank(QuestionBank questionBank);

    void addQuestionToBank(Questions question);

    void addQuestionsToBank(List<Questions> questions);

    void deleteQuestionFromBank(Questions question);

    void deleteQuestionBank(String questionBankId);

    List<QuestionBank> findBanksByEmployee(String employeeNumber);

    List<Questions> findQuestionsByBankId(String questionBankId);

    List<Questions> findQuestionsByConditions(String questionBankId, String type, Integer difficulty);

    void addQuestionWithResources(Questions question, List<MultipartFile> files);
    Questions findQuestionByBankIdAndQuestionId(String questionBankId, String questionId);

    //更新题库中题目的信息
    void updateQuestionInBank(String questionBankId, Questions updatedQuestion);
}
