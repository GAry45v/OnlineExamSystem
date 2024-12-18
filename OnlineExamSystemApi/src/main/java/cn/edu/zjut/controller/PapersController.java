package cn.edu.zjut.controller;

import cn.edu.zjut.entity.Papers;
import cn.edu.zjut.entity.PaperQuestions;
import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.service.PaperService;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/papers")
public class PapersController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private QuestionBankService questionBankService;

    // 获取教师的所有题库
//    @GetMapping("/question-banks/{employeeNumber}")
//    public List<QuestionBank> getTeacherQuestionBanks() {
//        return questionBankService.findBanksByEmployee(employeeNumber);
//    }

    // 获取题库中的题目
    @GetMapping("/{questionBankId}/questions")
    public List<Questions> getQuestionsByBank(@PathVariable String questionBankId) {
        return questionBankService.findQuestionsByBankId(questionBankId);
    }

    // 创建试卷
    @PostMapping("/create")
    public String createPaper(@RequestBody Papers paper) {
        paperService.createPaper(paper);
        return "试卷创建成功";
    }

    // 智能组卷
    @PostMapping("/{paperId}/auto-generate")
    public String autoGeneratePaper(@PathVariable int paperId,
                                    @RequestParam String questionBankId,
                                    @RequestBody Map<String, Integer> questionTypeCount,
                                    @RequestParam int targetDifficulty) {
        try {
            paperService.autoGeneratePaper(paperId, questionBankId, questionTypeCount, targetDifficulty);
            return "智能组卷成功！";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    // 手动组卷 - 批量导入题目
    @PostMapping("/{paperId}/import-questions")
    public String importQuestions(@PathVariable int paperId, @RequestBody List<Questions> questions) {
        paperService.importQuestionsToPaper(paperId, questions);
        return "批量导入题目成功";
    }

    // 手动添加题目
    @PostMapping("/{paperId}/add-question")
    public String addQuestionToPaper(@PathVariable int paperId,
                                     @RequestPart("question") Questions question,
                                     @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            paperService.addQuestionManually(paperId, question, files);
            return "题目添加成功";
        } catch (Exception e) {
            return "题目添加失败：" + e.getMessage();
        }
    }

    // 删除试卷中的题目
    @DeleteMapping("/{paperId}/delete-question/{paperQuestionId}")
    public String deleteQuestionFromPaper(@PathVariable int paperId, @PathVariable int paperQuestionId) {
        paperService.deleteQuestionFromPaper(paperQuestionId);
        return "题目删除成功";
    }
    @GetMapping("/teacher/{employeeNumber}")
    public List<Papers> getPapersByTeacher(@PathVariable String employeeNumber) {
        return paperService.getPapersByTeacher(employeeNumber);
    }
    @GetMapping("/{paperId}/questions-details")
    public List<Questions> getQuestionsWithDetails(@PathVariable int paperId) {
        return paperService.getQuestionsWithDetailsByPaperId(paperId);
    }
}
