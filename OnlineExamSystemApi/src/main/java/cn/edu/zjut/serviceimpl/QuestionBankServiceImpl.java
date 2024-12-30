package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.controller.QuestionBankController;
import cn.edu.zjut.entity.OperationLog;
import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.repository.QuestionBankRepository;
import cn.edu.zjut.service.OperationLogService;
import cn.edu.zjut.service.QuestionBankService;
import cn.edu.zjut.util.AliOSSUtils;
import cn.edu.zjut.util.OperationLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    private OperationLogService operationLogService;
    @Override
    public void createQuestionBank(QuestionBank questionBank) {
        if (questionBank == null || questionBank.getName() == null || questionBank.getEmployeeNumber() == null) {
            throw new RuntimeException("题库信息不完整，创建失败");
        }
        questionBank.setQuestionBankId(UUID.randomUUID().toString());
        questionBankRepository.save(questionBank);
        OperationLog log = OperationLogUtil.createOperationLog("教师创建题库："+questionBank.getName());
        operationLogService.addOperationLog(log);
    }

//    @Override
//    public void addQuestionWithResources(Questions question, List<MultipartFile> files, List<QuestionBankController.FileMetadata> fileMetadataList) {
//        if (question == null || question.getQuestionBankId() == null) {
//            throw new RuntimeException("题目信息不完整，无法添加");
//        }
//
//        try {
//            // 初始化资源路径列表
//            if (question.getResourcePaths() == null) {
//                question.setResourcePaths(new ArrayList<>());
//            }
//
//            // 遍历文件元数据，并匹配文件进行上传
//            for (QuestionBankController.FileMetadata metadata : fileMetadataList) {
//                // 根据元数据的文件名，找到对应的文件
//                MultipartFile file = files.stream()
//                        .filter(f -> f.getOriginalFilename().equals(metadata.getFileName()))
//                        .findFirst()
//                        .orElseThrow(() -> new RuntimeException("文件 " + metadata.getFileName() + " 不存在"));
//
//                // 上传文件到阿里云 OSS
//                String resourcePath = aliOSSUtils.upload(file);
//
//                // 根据文件的用途绑定到题目
//                if ("content".equals(metadata.getType())) {
//                    // 如果是题目内容相关的文件
//                    question.getResourcePaths().add(resourcePath);
//                } else if ("options".equals(metadata.getType()) && metadata.getOptionKey() != null) {
//                    // 如果是选项相关的文件
//                    String optionContent = question.getOptions().get(metadata.getOptionKey());
//                    if (optionContent == null) {
//                        throw new RuntimeException("选项 " + metadata.getOptionKey() + " 不存在");
//                    }
//                    // 更新选项内容，将图片路径附加到选项内容后
//                    question.getOptions().put(metadata.getOptionKey(), optionContent + " [图片: " + resourcePath + "]");
//                }
//            }
//
//            // 将题目添加到题库
//            addQuestionToBank(question);
//
//        } catch (IOException e) {
//            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
//        } catch (Exception e) {
//            throw new RuntimeException("添加题目失败: " + e.getMessage(), e);
//        }
//    }
private static final Logger logger = LoggerFactory.getLogger(QuestionBankServiceImpl.class);
    @Override
    public void addQuestionWithResources(Questions question, List<MultipartFile> files, List<QuestionBankController.FileMetadata> fileMetadataList) {
        if (question == null || question.getQuestionBankId() == null) {
            logger.error("题目信息不完整，无法添加：question={}, questionBankId={}", question, question != null ? question.getQuestionBankId() : null);
            throw new RuntimeException("题目信息不完整，无法添加");
        }

        try {
            // 初始化资源路径列表
            if (question.getResourcePaths() == null) {
                question.setResourcePaths(new ArrayList<>());
                logger.info("初始化资源路径列表");
            }

            // 遍历文件元数据并处理每个文件
            for (QuestionBankController.FileMetadata metadata : fileMetadataList) {
                logger.info("处理文件元数据：{}", metadata);

                MultipartFile file = files.stream()
                        .filter(f -> f.getOriginalFilename().equals(metadata.getFileName()))
                        .findFirst()
                        .orElseThrow(() -> {
                            logger.error("文件 {} 不存在", metadata.getFileName());
                            return new RuntimeException("文件 " + metadata.getFileName() + " 不存在");
                        });

                String resourcePath = aliOSSUtils.upload(file);
                logger.info("文件 {} 上传成功，路径：{}", file.getOriginalFilename(), resourcePath);

                // 添加路径到 resourcePaths
                question.getResourcePaths().add(resourcePath);
                logger.info("资源路径添加到 resourcePaths：{}", resourcePath);

                // 根据用途绑定路径
                if ("content".equals(metadata.getType())) {
                    // 如果是题目内容的图片
                    question.setContent(question.getContent() + " [图片: " + resourcePath + "]");
                    logger.info("文件绑定到题目内容，路径：{}", resourcePath);
                } else if ("options".equals(metadata.getType()) && metadata.getOptionKey() != null) {
                    // 如果是选项相关的图片
                    String optionContent = question.getOptions().get(metadata.getOptionKey());
                    if (optionContent == null) {
                        logger.error("选项 {} 不存在", metadata.getOptionKey());
                        throw new RuntimeException("选项 " + metadata.getOptionKey() + " 不存在");
                    }
                    question.getOptions().put(metadata.getOptionKey(), optionContent + " [图片: " + resourcePath + "]");
                    logger.info("文件绑定到选项 {}，路径：{}", metadata.getOptionKey(), resourcePath);
                }
            }

            logger.info("绑定后的题目资源路径：{}", question.getResourcePaths());
            logger.info("绑定后的选项内容：{}", question.getOptions());
            logger.info("绑定后的题目内容：{}", question.getContent());

            // 保存题目到题库
            addQuestionToBank(question);
            logger.info("题目成功添加到题库：questionId={}, questionBankId={}", question.getQuestionId(), question.getQuestionBankId());

        } catch (IOException e) {
            logger.error("文件上传失败：{}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("添加题目失败：{}", e.getMessage(), e);
            throw new RuntimeException("添加题目失败: " + e.getMessage(), e);
        }
    }




    @Override
    public void addQuestionToBank(Questions question) {
        String questionBankId = question.getQuestionBankId();
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在，无法添加题目"));

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

        String questionBankId = questions.get(0).getQuestionBankId();
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在，无法添加题目"));

        questions.forEach(question -> {
            question.setQuestionId(UUID.randomUUID().toString());
            question.setQuestionBankId(questionBankId);
        });

        questionBank.getQuestions().addAll(questions);

        questionBankRepository.save(questionBank);
    }

    @Override
    public void deleteQuestionFromBank(Questions question) {
        if (question == null || question.getQuestionBankId() == null || question.getQuestionId() == null) {
            throw new RuntimeException("题目信息不完整，无法删除");
        }

        QuestionBank questionBank = questionBankRepository.findById(question.getQuestionBankId())
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        boolean removed = questionBank.getQuestions().removeIf(q -> q.getQuestionId().equals(question.getQuestionId()));

        if (!removed) {
            throw new RuntimeException("题目不存在，无法删除");
        }

        questionBankRepository.save(questionBank);
    }

    @Override
    public void deleteQuestionBank(String questionBankId) {
        if (questionBankId == null) {
            throw new RuntimeException("题库ID不能为空");
        }

        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在"));

        questionBankRepository.delete(questionBank);
        OperationLog log = OperationLogUtil.createOperationLog("教师删除题库："+questionBank.getName());
        operationLogService.addOperationLog(log);
    }

    @Override
    public List<QuestionBank> findBanksByEmployee(String employeeNumber) {
        if (employeeNumber == null) {
            throw new RuntimeException("教师编号不能为空");
        }

        return questionBankRepository.findAll().stream()
                .filter(bank -> employeeNumber.equals(bank.getEmployeeNumber()))
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
                .filter(question -> (type == null || type.equals(question.getType())) &&
                        (difficulty == null || difficulty.equals(question.getDifficulty())))
                .collect(Collectors.toList());
    }

    @Override
    public Questions findQuestionByBankIdAndQuestionId(String questionBankId, String questionId) {
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在，ID: " + questionBankId));

        return questionBank.getQuestions().stream()
                .filter(question -> questionId.equals(question.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + questionId));
    }

    @Override
    public void updateQuestionInBank(String questionBankId, Questions updatedQuestion) {
        QuestionBank questionBank = questionBankRepository.findById(questionBankId)
                .orElseThrow(() -> new RuntimeException("题库不存在，ID: " + questionBankId));

        Questions existingQuestion = questionBank.getQuestions().stream()
                .filter(q -> q.getQuestionId().equals(updatedQuestion.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("题目不存在，ID: " + updatedQuestion.getQuestionId()));

        // 更新题目信息，只更新非空字段
        Optional.ofNullable(updatedQuestion.getContent()).ifPresent(existingQuestion::setContent);
        Optional.ofNullable(updatedQuestion.getOptions()).ifPresent(existingQuestion::setOptions);
        Optional.ofNullable(updatedQuestion.getAnswers()).ifPresent(existingQuestion::setAnswers);
        Optional.ofNullable(updatedQuestion.getDifficulty()).ifPresent(existingQuestion::setDifficulty);
        Optional.ofNullable(updatedQuestion.getTags()).ifPresent(existingQuestion::setTags);
        Optional.ofNullable(updatedQuestion.getType()).ifPresent(existingQuestion::setType);
        Optional.ofNullable(updatedQuestion.getResourcePaths()).ifPresent(existingQuestion::setResourcePaths);

        questionBankRepository.save(questionBank);
    }
}
