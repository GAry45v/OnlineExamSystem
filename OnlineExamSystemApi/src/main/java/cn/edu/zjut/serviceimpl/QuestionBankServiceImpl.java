package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.repository.QuestionBankRepository;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Override
    public void createQuestionBank(QuestionBank questionBank) {
        questionBankRepository.save(questionBank);
    }

    @Override
    public void addQuestionToBank(String questionBankId, Questions question) {
        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));
        // 将题目添加到题库的题目列表中
        questionBank.getQuestions().add(question);
        questionBankRepository.save(questionBank); // 更新题库
    }

    @Override
    public void addQuestionsToBank(String questionBankId, List<Questions> questions) {
        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        // 将所有题目添加到题库的题目列表中
        questionBank.getQuestions().addAll(questions);
        questionBankRepository.save(questionBank); // 更新题库
    }

    @Override
    public void deleteQuestionFromBank(String questionBankId, Integer questionId) {
        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        // 删除指定题目
        questionBank.getQuestions().removeIf(question -> question.getQuestionId().equals(questionId));
        questionBankRepository.save(questionBank); // 更新题库
    }

    @Override
    public void deleteQuestionBank(String questionBankId) {
        // 查找题库
        Optional<QuestionBank> questionBank = questionBankRepository.findById(questionBankId);
        if (questionBank.isPresent()) {
            questionBankRepository.delete(questionBank.get()); // 删除题库
        } else {
            throw new RuntimeException("题库不存在");
        }
    }
}
