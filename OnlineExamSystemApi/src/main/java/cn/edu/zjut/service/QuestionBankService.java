package cn.edu.zjut.service;

import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;

import java.util.List;

public interface QuestionBankService {

    void createQuestionBank(QuestionBank questionBank);

    void addQuestionToBank(String questionBankId, Questions question);

    void addQuestionsToBank(String questionBankId, List<Questions> questions);

    void deleteQuestionFromBank(String questionBankId, Integer questionId);

    void deleteQuestionBank(String questionBankId);
}
