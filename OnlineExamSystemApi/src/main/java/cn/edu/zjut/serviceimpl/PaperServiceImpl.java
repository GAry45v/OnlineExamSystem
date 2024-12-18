package cn.edu.zjut.serviceimpl;

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
    public Papers autoGeneratePaper(int paperId, String questionBankId, Map<String, Integer> questionTypeCount, int targetDifficulty) {
        List<Questions> allQuestions = new ArrayList<>();
        double totalDifficulty = 0.0;

        for (Map.Entry<String, Integer> entry : questionTypeCount.entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue();

            List<Questions> filteredQuestions = questionBankService.findQuestionsByConditions(questionBankId, type, null);

            List<Questions> suitableQuestions = filteredQuestions.stream()
                    .filter(q -> Math.abs(q.getDifficulty() - targetDifficulty) <= 1)
                    .collect(Collectors.toList());

            if (suitableQuestions.size() < count) {
                throw new RuntimeException("出卷失败！题库中的 " + type + " 题目不足，请调整试卷参数。");
            }

            Collections.shuffle(suitableQuestions);
            List<Questions> selectedQuestions = suitableQuestions.subList(0, count);

            allQuestions.addAll(selectedQuestions);
            totalDifficulty += selectedQuestions.stream().mapToInt(Questions::getDifficulty).sum();
        }

        double averageDifficulty = totalDifficulty / allQuestions.size();
        if (Math.abs(averageDifficulty - targetDifficulty) > 0.5) {
            throw new RuntimeException("出卷失败！试卷平均难度不符合要求，请调整参数。");
        }

        for (int i = 0; i < allQuestions.size(); i++) {
            Questions q = allQuestions.get(i);
            PaperQuestions pq = new PaperQuestions();
            pq.setPaperId(paperId);
            pq.setQuestionType(q.getType());
            pq.setQuestionOrder(i + 1);
            pq.setQuestionId(q.getQuestionId());
            paperQuestionsMapper.addQuestionToPaper(pq);
        }

        return papersMapper.findPaperById(paperId);
    }

    @Override
    public void importQuestionsToPaper(int paperId, List<Questions> questions) {
        for (Questions question : questions) {
            PaperQuestions pq = new PaperQuestions();
            pq.setPaperId(paperId);
            pq.setQuestionType(question.getType());
            pq.setQuestionOrder(questions.indexOf(question) + 1);
            pq.setQuestionId(question.getQuestionId());
            paperQuestionsMapper.addQuestionToPaper(pq);
        }
    }

    @Override
    public void addQuestionManually(int paperId, Questions question, List<MultipartFile> files) throws Exception {
        // 生成唯一的题目ID
        question.setQuestionId(UUID.randomUUID().toString());

        // 判断是否有文件信息
        if (files != null && !files.isEmpty()) {
            // 调用带文件的题目添加方法
            questionBankService.addQuestionWithResources(question, files);
        } else {
            // 调用普通题目添加方法
            questionBankService.addQuestionToBank(question);
        }
        // 查询试卷中已有题目的数量
        int currentQuestionCount = paperQuestionsMapper.getQuestionCountByPaperId(paperId);
        // 关联题目到试卷
        PaperQuestions paperQuestion = new PaperQuestions();
        paperQuestion.setPaperId(paperId);
        paperQuestion.setQuestionType(question.getType());
        paperQuestion.setQuestionOrder(currentQuestionCount + 1); // 设置排序顺序：已有题目数量 + 1
        paperQuestion.setQuestionId(question.getQuestionId());

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
    public List<Questions> getQuestionsWithDetailsByPaperId(int paperId) {
        // 1. 获取试卷中所有题目的关联信息
        List<PaperQuestions> paperQuestionsList = paperQuestionsMapper.findQuestionsByPaperId(paperId);

        // 2. 遍历所有题目，调用 QuestionBankService 获取详细信息
        List<Questions> detailedQuestions = new ArrayList<>();
        for (PaperQuestions paperQuestion : paperQuestionsList) {
            String questionBankId = paperQuestion.getQuestionId().split("-")[0]; // 假设 questionId 包含 bankId 信息
            String questionId = paperQuestion.getQuestionId();
            Questions question = questionBankService.findQuestionByBankIdAndQuestionId(questionBankId, questionId);
            if (question != null) {
                detailedQuestions.add(question);
            }
        }
        return detailedQuestions;
    }
}
