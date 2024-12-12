package cn.edu.zjut.controller;

import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question-bank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    // 创建题库
    @PostMapping("/create")
    public String createQuestionBank(@RequestBody QuestionBank questionBank) {
        try {
            questionBankService.createQuestionBank(questionBank);
            return "题库创建成功！";
        } catch (Exception e) {
            return "创建题库失败: " + e.getMessage();
        }
    }

    // 添加单个题目到题库
    @PostMapping("/{questionBankId}/add-question")
    public String addQuestionToBank(@PathVariable String questionBankId, @RequestBody Questions question) {
        try {
            questionBankService.addQuestionToBank(questionBankId, question);
            return "题目添加成功！";
        } catch (Exception e) {
            return "添加题目失败: " + e.getMessage();
        }
    }

    // 批量添加题目到题库
    @PostMapping("/{questionBankId}/add-questions")
    public String addMultipleQuestionsToBank(@PathVariable String questionBankId, @RequestBody List<Questions> questions) {
        try {
            questionBankService.addQuestionsToBank(questionBankId, questions);
            return "题目批量添加成功！";
        } catch (Exception e) {
            return "批量添加题目失败: " + e.getMessage();
        }
    }

    // 删除题目
    @DeleteMapping("/{questionBankId}/delete-question/{questionId}")
    public String deleteQuestionFromBank(@PathVariable String questionBankId, @PathVariable Integer questionId) {
        try {
            questionBankService.deleteQuestionFromBank(questionBankId, questionId);
            return "题目删除成功！";
        } catch (Exception e) {
            return "删除题目失败: " + e.getMessage();
        }
    }

    // 删除题库
    @DeleteMapping("/delete/{questionBankId}")
    public String deleteQuestionBank(@PathVariable String questionBankId) {
        try {
            questionBankService.deleteQuestionBank(questionBankId);
            return "题库删除成功！";
        } catch (Exception e) {
            return "删除题库失败: " + e.getMessage();
        }
    }
}
