package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.controller.QuestionBankController;
import cn.edu.zjut.entity.PaperQuestionDTO;
import cn.edu.zjut.entity.Papers;
import cn.edu.zjut.entity.PaperQuestions;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.mapper.PapersMapper;
import cn.edu.zjut.mapper.PaperQuestionsMapper;
import cn.edu.zjut.service.PaperService;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PapersMapper papersMapper;

    @Autowired
    private PaperQuestionsMapper paperQuestionsMapper;

    @Autowired
    private QuestionBankService questionBankService;

    @Override
    public void createPaper(Papers paper) {
        papersMapper.createPaper(paper);
    }

    @Override
    public Papers autoGeneratePaper(int paperId, String questionBankId, Map<String, Integer> questionTypeCount, Map<String, Integer> questionTypeScore, Integer targetDifficulty) {
        List<Questions> allQuestions = new ArrayList<>();
        double totalDifficulty = 0.0;

        // 查询当前试卷中已有题目的数量
        int questionOrderCounter = paperQuestionsMapper.getQuestionCountByPaperId(paperId) + 1;

        for (Map.Entry<String, Integer> entry : questionTypeCount.entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue();

            // 获取该题型的单题分数
            Integer singleScore = questionTypeScore.get(type);
            if (singleScore == null) {
                throw new RuntimeException("出卷失败！未提供题型 " + type + " 的单题分数，请检查参数。");
            }

            List<Questions> filteredQuestions = questionBankService.findQuestionsByConditions(questionBankId, type, null);

            List<Questions> suitableQuestions;

            if (targetDifficulty != null) {  // 如果提供了 targetDifficulty，按难度进行筛选
                suitableQuestions = filteredQuestions.stream()
                        .filter(q -> Math.abs(q.getDifficulty() - targetDifficulty) <= 1)  // 根据难度筛选
                        .collect(Collectors.toList());
            } else {  // 如果没有提供 targetDifficulty，直接选择题目，不考虑难度
                suitableQuestions = new ArrayList<>(filteredQuestions);
            }

            if (suitableQuestions.size() < count) {
                throw new RuntimeException("出卷失败！题库中的 " + type + " 题目不足，请调整试卷参数。");
            }

            Collections.shuffle(suitableQuestions);
            List<Questions> selectedQuestions = suitableQuestions.subList(0, count);

            allQuestions.addAll(selectedQuestions);
            totalDifficulty += selectedQuestions.stream().mapToInt(Questions::getDifficulty).sum();

            // 将选中的题目添加到试卷中，同时设置单题分数和顺序
            for (Questions q : selectedQuestions) {
                PaperQuestions pq = new PaperQuestions();
                pq.setPaperId(paperId);
                pq.setQuestionType(q.getType());
                pq.setQuestionOrder(questionOrderCounter++); // 根据已有数量计算顺序并递增
                pq.setQuestionId(q.getQuestionId());
                pq.setMarks(singleScore); // 设置单题分数
                paperQuestionsMapper.addQuestionToPaper(pq);
            }
        }

        // 如果提供了 targetDifficulty，计算试卷的平均难度
        if (targetDifficulty != null) {
            double averageDifficulty = totalDifficulty / allQuestions.size();
            if (Math.abs(averageDifficulty - targetDifficulty) > 0.5) {
                throw new RuntimeException("出卷失败！试卷平均难度不符合要求，请调整参数。");
            }
        }

        return papersMapper.findPaperById(paperId);
    }

    @Override
    public void importQuestionsToPaper(int paperId, List<Questions> questions, List<Integer> questionScores) {
        if (questions.size() != questionScores.size()) {
            throw new IllegalArgumentException("题目数量与分数数量不匹配！");
        }

        // 查询当前试卷中已有题目的数量
        int questionOrderCounter = paperQuestionsMapper.getQuestionCountByPaperId(paperId) + 1;

        for (int i = 0; i < questions.size(); i++) {
            Questions question = questions.get(i);
            Integer score = questionScores.get(i);

            PaperQuestions pq = new PaperQuestions();
            pq.setPaperId(paperId);
            pq.setQuestionType(question.getType());
            pq.setQuestionOrder(questionOrderCounter++); // 根据已有数量计算顺序并递增
            pq.setQuestionId(question.getQuestionId());
            pq.setMarks(score); // 设置单题分数

            paperQuestionsMapper.addQuestionToPaper(pq);
        }
    }

    @Override
    public void addQuestionToPaper(int paperId, Questions question, int marks) {
        // 查询试卷中已有题目的数量
        int questionOrderCounter = paperQuestionsMapper.getQuestionCountByPaperId(paperId) + 1;

        // 创建 PaperQuestions 对象
        PaperQuestions paperQuestion = new PaperQuestions();
        paperQuestion.setPaperId(paperId);
        paperQuestion.setQuestionType(question.getType());
        paperQuestion.setQuestionOrder(questionOrderCounter); // 根据已有数量设置顺序
        paperQuestion.setQuestionId(question.getQuestionId());
        paperQuestion.setMarks(marks); // 设置单题分数

        // 添加题目到试卷
        paperQuestionsMapper.addQuestionToPaper(paperQuestion);
    }

    @Override
    public void addQuestionManually(int paperId, Questions question, List<MultipartFile> files, List<QuestionBankController.FileMetadata> fileMetadataList, int marks) throws Exception {
        // 生成唯一的题目ID
        question.setQuestionId(UUID.randomUUID().toString());

        // 判断是否有文件信息
        if (files != null && !files.isEmpty()) {
            // 调用带文件的题目添加方法
            questionBankService.addQuestionWithResources(question, files, fileMetadataList);
        } else {
            // 调用普通题目添加方法
            questionBankService.addQuestionToBank(question);
        }

        // 查询试卷中已有题目的数量
        int questionOrderCounter = paperQuestionsMapper.getQuestionCountByPaperId(paperId) + 1;

        // 关联题目到试卷
        PaperQuestions paperQuestion = new PaperQuestions();
        paperQuestion.setPaperId(paperId);
        paperQuestion.setQuestionType(question.getType());
        paperQuestion.setQuestionOrder(questionOrderCounter); // 根据已有数量设置顺序
        paperQuestion.setQuestionId(question.getQuestionId());
        paperQuestion.setMarks(marks); // 设置题目分数

        // 添加题目到试卷
        paperQuestionsMapper.addQuestionToPaper(paperQuestion);
    }

    @Override
    public void deleteQuestionFromPaper(int paperQuestionId) {
        paperQuestionsMapper.deleteQuestionFromPaper(paperQuestionId);
    }

    @Override
    public List<Papers> getPapersByTeacher(String employeeNumber) {
        return papersMapper.findPapersByEmployee(employeeNumber);
    }

    @Override
    public List<PaperQuestionDTO> getQuestionsWithDetailsByPaperId(int paperId) {
        // 1. 获取试卷中所有题目的关联信息
        List<PaperQuestions> paperQuestionsList = paperQuestionsMapper.findQuestionsByPaperId(paperId);

        // 2. 根据 paperId 获取对应的 questionBankId
        Papers paper = papersMapper.findPaperById(paperId); // 通过 paperId 查找试卷
        if (paper == null) {
            throw new RuntimeException("试卷不存在，ID: " + paperId);
        }
        String questionBankId = paper.getQuestionBankId(); // 获取对应的 questionBankId

        // 3. 遍历所有题目，调用 QuestionBankService 获取详细信息
        List<PaperQuestionDTO> result = new ArrayList<>();
        for (PaperQuestions paperQuestion : paperQuestionsList) {
            String questionId = paperQuestion.getQuestionId(); // 获取题目ID

            // 调用 QuestionBankService 根据 questionBankId 和 questionId 获取题目详细信息
            Questions question = questionBankService.findQuestionByBankIdAndQuestionId(questionBankId, questionId);

            // 如果找到题目，封装成 DTO，并加入返回列表
            if (question != null) {
                PaperQuestionDTO dto = new PaperQuestionDTO();
                dto.setPaperQuestionId(paperQuestion.getPaperQuestionId()); // 设置 PaperQuestion ID
                dto.setQuestion(question); // 设置题目详情
                dto.setMarks(paperQuestion.getMarks()); // 设置分数
                dto.setQuestionOrder(paperQuestion.getQuestionOrder()); // 设置题目顺序
                result.add(dto);
            }
        }

        return result;
    }
}
