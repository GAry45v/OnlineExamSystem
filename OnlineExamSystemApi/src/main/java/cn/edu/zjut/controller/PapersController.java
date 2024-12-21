package cn.edu.zjut.controller;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.*;
import cn.edu.zjut.service.PaperService;
import cn.edu.zjut.service.QuestionBankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/api/papers")
public class PapersController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private QuestionBankService questionBankService;

    // 获取教师的所有题库
    @GetMapping("/question-banks")
    public List<QuestionBank> getTeacherQuestionBanks() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber(); // 从 Token 中获取教师编号
        return questionBankService.findBanksByEmployee(employeeNumber);
    }

    // 获取题库中的题目
    @GetMapping("/{questionBankId}/questions")
    public List<Questions> getQuestionsByBank(@PathVariable String questionBankId) {
        return questionBankService.findQuestionsByBankId(questionBankId);
    }

    // 创建试卷
    @PostMapping("/create")
    public Papers createPaper(@RequestBody Papers paper) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber(); // 从 Token 中获取教师编号
        paper.setEmployeeNumber(employeeNumber); // 设置到 Papers 对象中
        paperService.createPaper(paper); // 在这里调用服务层，MyBatis 会将生成的 ID 填充到 paper 对象中
        return paper; // 返回包含新试卷信息的对象
    }


    // 智能组卷
    @PostMapping("/{paperId}/auto-generate")
    public String autoGeneratePaper(@PathVariable int paperId,
                                    @RequestParam String questionBankId,
                                    @RequestBody cn.edu.zjut.entity.AutoGeneratePaperDTO autoGeneratePaperDTO) {
        try {
            // 从 DTO 中获取参数
            Map<String, Integer> questionTypeCount = autoGeneratePaperDTO.getQuestionTypeCount();
            Map<String, Integer> questionTypeScore = autoGeneratePaperDTO.getQuestionTypeScore();
            Integer targetDifficulty = autoGeneratePaperDTO.getTargetDifficulty();

            // 调用 Service
            paperService.autoGeneratePaper(paperId, questionBankId, questionTypeCount, questionTypeScore, targetDifficulty);

            return "智能组卷成功！";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    // 手动组卷 - 批量导入题目
    @PostMapping("/{paperId}/import-questions")
    public String importQuestions(
            @PathVariable int paperId,
            @RequestParam String questionBankId, // 从参数中获取 questionBankId
            @RequestBody List<PaperQuestionDTO> questionsWithMarks) {
        try {
            // 遍历 DTO，提取题目信息和分数
            List<Questions> questions = new ArrayList<>();
            List<Integer> marks = new ArrayList<>();

            for (PaperQuestionDTO dto : questionsWithMarks) {
                Questions question = dto.getQuestion();
                question.setQuestionBankId(questionBankId); // 设置题库ID
                questions.add(question);
                marks.add(dto.getMarks()); // 获取对应的分数
            }

            // 批量关联题目到试卷
            paperService.importQuestionsToPaper(paperId, questions, marks);

            return "批量导入题目成功";
        } catch (Exception e) {
            return "批量导入失败：" + e.getMessage();
        }
    }

    @PostMapping("/{paperId}/add-question")
    public String addQuestionToPaper(@PathVariable int paperId,
                                     @RequestParam("questionBankId") String questionBankId,
                                     @RequestPart("question") Questions question,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                     @RequestPart("fileMetadata") String fileMetadataJson, // 新增文件元数据 JSON
                                     @RequestParam("marks") int marks) { // 增加单题分数参数
        try {
            // 从 Token 中获取教师编号
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();

            // 设置题目的 questionBankId 和 employeeNumber
            question.setQuestionBankId(questionBankId);
            question.setEmployeeNumber(employeeNumber);

            // 解析 fileMetadataJson 为 List<QuestionBankController.FileMetadata>
            ObjectMapper objectMapper = new ObjectMapper();
            List<QuestionBankController.FileMetadata> fileMetadataList = objectMapper.readValue(fileMetadataJson,
                    new TypeReference<List<QuestionBankController.FileMetadata>>() {});

            // 判断是否有文件参数，调用不同的方法将题目添加到题库
            if (files != null && !files.isEmpty()) {
                questionBankService.addQuestionWithResources(question, files, fileMetadataList); // 调用带文件的方法
            } else {
                questionBankService.addQuestionToBank(question); // 调用无文件的方法
            }

            // 关联题目到试卷，同时设置分数
            paperService.addQuestionToPaper(paperId, question, marks);

            return "题目添加成功";
        } catch (Exception e) {
            return "题目添加失败：" + e.getMessage();
        }
    }


    // 删除试卷中的题目
    @DeleteMapping("/delete-question/{paperQuestionId}")
    public String deleteQuestionFromPaper(@PathVariable int paperQuestionId) {
        paperService.deleteQuestionFromPaper(paperQuestionId);
        return "题目删除成功";
    }

    // 获取教师的所有试卷
    @GetMapping("/teacher")
    public List<Papers> getPapersByTeacher() {
        // 从 Token 中获取当前登录的用户编号（employeeNumber）
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber(); // 获取当前用户的 employeeNumber
        return paperService.getPapersByTeacher(employeeNumber); // 使用获取的 employeeNumber 调用 service 层
    }

    // 获取试卷的题目详细信息
    @GetMapping("/{paperId}/questions-details")
    public List<PaperQuestionDTO> getQuestionsWithDetails(@PathVariable int paperId) {
        return paperService.getQuestionsWithDetailsByPaperId(paperId);
    }
}
