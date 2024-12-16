package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.repository.QuestionBankRepository;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        String questionBankId = question.getQuestionBankId();

        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));
        // 为题目生成唯一ID
        question.setQuestionId(UUID.randomUUID().toString());
        // 检查题目是否包含资源路径并处理
        if (question.getResourcePaths() != null && !question.getResourcePaths().isEmpty()) {
            // 这里可以根据需要执行额外的校验或处理逻辑
        }

        // 添加题目到题库
        questionBank.getQuestions().add(question);

        // 更新题库
        questionBankRepository.save(questionBank);
    }
    @Override
    public void addQuestionsToBank(List<Questions> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new RuntimeException("题目列表不能为空");
        }

        // 获取题库ID（假设所有题目属于同一个题库）
        String questionBankId = questions.get(0).getQuestionBankId();

        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        // 遍历题目列表
        for (Questions question : questions) {
            // 自动生成题目ID
            question.setQuestionId(UUID.randomUUID().toString());

            // 确保题目所属的题库ID正确
            question.setQuestionBankId(questionBankId);

            // 检查题目是否包含资源路径
            if (question.getResourcePaths() != null && !question.getResourcePaths().isEmpty()) {
                // 这里可以添加额外的验证逻辑（如果需要）
            }
        }

        // 将题目列表添加到题库
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
