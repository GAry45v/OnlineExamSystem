package cn.edu.zjut.controller;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.*;
import cn.edu.zjut.service.PaperService;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        System.out.println(employeeNumber);
        return questionBankService.findBanksByEmployee(employeeNumber);
    }

    // 获取题库中的题目
    @GetMapping("/{questionBankId}/questions")
    public List<Questions> getQuestionsByBank(@PathVariable String questionBankId) {
        return questionBankService.findQuestionsByBankId(questionBankId);
    }

    // 创建试卷
    @PostMapping("/create")
    public String createPaper(@RequestBody Papers paper) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String employeeNumber = authentication.getUserNumber(); // 从 Token 中获取教师编号
        paper.setEmployeeNumber(employeeNumber); // 设置到 Papers 对象中
        paperService.createPaper(paper);
        return "试卷创建成功";
    }

    // 智能组卷
    @PostMapping("/{paperId}/auto-generate")
    public String autoGeneratePaper(@PathVariable int paperId,
                                    @RequestParam String questionBankId,
                                    @RequestBody Map<String, Integer> questionTypeCount,
                                    @RequestParam(required = false) Integer targetDifficulty) {  // targetDifficulty 是可选的
        try {
            paperService.autoGeneratePaper(paperId, questionBankId, questionTypeCount, targetDifficulty);
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
            @RequestBody List<Questions> questions) {
        try {
            // 从 Token 中获取教师编号
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber(); // 从 Token 中获取 employeeNumber

            // 遍历题目列表，设置 questionBankId 和 employeeNumber
            for (Questions question : questions) {
                question.setQuestionBankId(questionBankId); // 设置题库ID
                question.setEmployeeNumber(employeeNumber); // 设置教师编号

                // 添加题目到题库
                if (question.getQuestionId() == null || question.getQuestionId().isEmpty()) {
                    // 如果题目ID为空，生成新的ID，并添加到题库
                    question.setQuestionId(UUID.randomUUID().toString());
                    questionBankService.addQuestionToBank(question);
                }
            }

            // 将题目与试卷关联
            paperService.importQuestionsToPaper(paperId, questions);

            return "批量导入题目成功";
        } catch (Exception e) {
            return "批量导入失败：" + e.getMessage();
        }
    }


    // 手动添加题目
    @PostMapping("/{paperId}/add-question")
    public String addQuestionToPaper(@PathVariable int paperId,
                                     @RequestParam("questionBankId") String questionBankId,
                                     @RequestPart("question") Questions question,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            // 从 Token 中获取教师编号
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();

            // 设置题目的 questionBankId 和 employeeNumber
            question.setQuestionBankId(questionBankId);
            question.setEmployeeNumber(employeeNumber);

            // 判断是否有文件参数，调用不同的方法将题目添加到题库
            if (files != null && !files.isEmpty()) {
                questionBankService.addQuestionWithResources(question, files); // 调用带文件的方法
            } else {
                questionBankService.addQuestionToBank(question); // 调用无文件的方法
            }

            // 关联题目到试卷
            paperService.addQuestionToPaper(paperId, question);

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
    public List<PaperQuestionWithDetails> getQuestionsWithDetails(@PathVariable int paperId) {
        return paperService.getQuestionsWithDetailsByPaperId(paperId);
    }
}
