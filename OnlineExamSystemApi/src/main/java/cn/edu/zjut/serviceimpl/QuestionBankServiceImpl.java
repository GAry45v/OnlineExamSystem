package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.repository.QuestionBankRepository;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.zjut.util.AliOSSUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Override
    public void addQuestionWithResources(Questions question, List<MultipartFile> files) {
        try {
            // 1. 上传文件并获取资源路径
            List<String> resourcePaths = files.stream()
                    .map(file -> {
                        try {
                            return aliOSSUtils.upload(file); // 上传文件到阿里OSS
                        } catch (IOException e) {
                            throw new RuntimeException("文件上传失败: " + file.getOriginalFilename(), e);
                        }
                    })
                    .collect(Collectors.toList());

            // 2. 将资源路径设置到题目对象中
            question.setResourcePaths(resourcePaths);

            // 3. 添加题目到对应题库
            addQuestionToBank(question);

        } catch (Exception e) {
            throw new RuntimeException("添加题目失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void addQuestionToBank(Questions question) {
        String questionBankId = question.getQuestionBankId();

        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        // 为题目生成唯一ID
        question.setQuestionId(UUID.randomUUID().toString());

        // 添加题目到题库
        questionBank.getQuestions().add(question);

        // 保存更新后的题库
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
    @Override
    public Questions findQuestionByBankIdAndQuestionId(String questionBankId, String questionId) {
        // 查找题库
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在，ID: " + questionBankId));

        // 在题库中查找题目
        return questionBank.getQuestions().stream()
                .filter(question -> question.getQuestionId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + questionId));
    }

}
