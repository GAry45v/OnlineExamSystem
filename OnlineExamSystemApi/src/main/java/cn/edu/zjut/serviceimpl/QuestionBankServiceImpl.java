package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.repository.QuestionBankRepository;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Override
    public void createQuestionBank(QuestionBank questionBank) {
        questionBankRepository.save(questionBank);
    }

    @Override
    public void addQuestionToBank(Questions question) {
        // 获取题库ID
        String questionBankId = question.getQuestionBankId();

        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        // 将题目添加到题库的题目列表中
        questionBank.getQuestions().add(question);

        // 更新题库
        questionBankRepository.save(questionBank);
    }

    @Override
    public void addQuestionsToBank(List<Questions> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new RuntimeException("题目列表不能为空");
        }

        // 获取题库ID
        String questionBankId = questions.get(0).getQuestionBankId();

        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        // 为每个题目设置正确的 questionBankId 并将其添加到题库中
        for (Questions question : questions) {
            question.setQuestionBankId(questionBankId);  // 确保每个题目都有正确的题库ID
        }

        // 添加题目到题库的题目列表
        questionBank.getQuestions().addAll(questions);

        // 更新题库
        questionBankRepository.save(questionBank);
    }

    @Override
    public void deleteQuestionFromBank(Questions question) {
        String questionBankId = question.getQuestionBankId();
        String questionId = question.getQuestionId();

        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        // 删除题目
        questionBank.getQuestions().removeIf(q -> q.getQuestionId().equals(questionId));

        // 更新题库
        questionBankRepository.save(questionBank);
    }

    @Override
    public void deleteQuestionBank(String questionBankId) {
        Optional<QuestionBank> questionBank = questionBankRepository.findById(questionBankId);
        if (questionBank.isPresent()) {
            questionBankRepository.delete(questionBank.get());
        } else {
            throw new RuntimeException("题库不存在");
        }
    }

    @Override
    public List<QuestionBank> findBanksByEmployee(String employeeNumber) {
        return questionBankRepository.findAll().stream()
                .filter(bank -> bank.getEmployeeNumber().equals(employeeNumber))
                .collect(Collectors.toList());
    }

    @Override
    public List<Questions> findQuestionsByBankId(String questionBankId) {
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));
        return questionBank.getQuestions();
    }

    @Override
    public List<Questions> findQuestionsByConditions(String questionBankId, String type, Integer difficulty) {
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));
        return questionBank.getQuestions().stream()
                .filter(question -> (type == null || question.getType().equals(type)) &&
                        (difficulty == null || question.getDifficulty().equals(difficulty)))
                .collect(Collectors.toList());
    }
}
