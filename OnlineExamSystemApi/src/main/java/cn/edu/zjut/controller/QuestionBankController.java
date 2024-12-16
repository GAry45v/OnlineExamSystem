package cn.edu.zjut.controller;
import cn.edu.zjut.util.AliOSSUtils;
import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.service.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/question-bank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;
    @Autowired
    private AliOSSUtils aliOSSUtils; // 注入 AliOSSUtils
    // 创建题库
    @PostMapping
    public ResponseEntity<String> createQuestionBank(@RequestBody QuestionBank questionBank) {
        try {
            questionBankService.createQuestionBank(questionBank);
            return ResponseEntity.status(HttpStatus.CREATED).body("题库创建成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("题库创建失败: " + e.getMessage());
        }
    }

    // 添加单个题目
    @PostMapping("/questions")
    public ResponseEntity<String> addQuestionToBank(@RequestBody Questions question) {
        try {
            questionBankService.addQuestionToBank(question);
            return ResponseEntity.status(HttpStatus.CREATED).body("题目添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("题目添加失败: " + e.getMessage());
        }
    }

    // 批量添加题目
    @PostMapping("/questions/batch")
    public ResponseEntity<String> addQuestionsToBank(@RequestBody List<Questions> questions) {
        try {
            questionBankService.addQuestionsToBank(questions);  // 调用服务方法
            return ResponseEntity.status(HttpStatus.CREATED).body("批量题目添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("批量题目添加失败: " + e.getMessage());
        }
    }

    // 删除题目
    @DeleteMapping("/questions")
    public ResponseEntity<String> deleteQuestionFromBank(@RequestBody Questions question) {
        try {
            questionBankService.deleteQuestionFromBank(question);  // 调用服务方法
            return ResponseEntity.status(HttpStatus.OK).body("题目删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("题目删除失败: " + e.getMessage());
        }
    }

    // 删除题库
    @DeleteMapping("/{questionBankId}")
    public ResponseEntity<String> deleteQuestionBank(@PathVariable String questionBankId) {
        try {
            questionBankService.deleteQuestionBank(questionBankId);
            return ResponseEntity.status(HttpStatus.OK).body("题库删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("题库删除失败: " + e.getMessage());
        }
    }

    // 查询老师创建的所有题库
    @GetMapping("/employee/{employeeNumber}")
    public ResponseEntity<List<QuestionBank>> getBanksByEmployee(@PathVariable String employeeNumber) {
        try {
            List<QuestionBank> banks = questionBankService.findBanksByEmployee(employeeNumber);
            if (banks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(banks);
            }
            return ResponseEntity.status(HttpStatus.OK).body(banks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 查询某个题库中的所有题目
    @GetMapping("/{questionBankId}/questions")
    public ResponseEntity<List<Questions>> getQuestionsByBankId(@PathVariable String questionBankId) {
        try {
            List<Questions> questions = questionBankService.findQuestionsByBankId(questionBankId);
            if (questions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(questions);
            }
            return ResponseEntity.status(HttpStatus.OK).body(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 多条件查询题目
    @GetMapping("/{questionBankId}/questions/search")
    public ResponseEntity<List<Questions>> searchQuestionsByConditions(
            @PathVariable String questionBankId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer difficulty) {
        try {
            List<Questions> questions = questionBankService.findQuestionsByConditions(questionBankId, type, difficulty);
            if (questions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(questions);
            }
            return ResponseEntity.status(HttpStatus.OK).body(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/questions/upload")
    public ResponseEntity<String> addQuestionWithResources(
            @RequestPart("question") Questions question,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            // 使用 AliOSSUtils 上传文件并获取路径
            List<String> resourcePaths = files.stream()
                    .map(file -> {
                        try {
                            return aliOSSUtils.upload(file); // 上传文件到 OSS
                        } catch (IOException e) {
                            throw new RuntimeException("文件上传失败: " + file.getOriginalFilename(), e);
                        }
                    })
                    .collect(Collectors.toList());

            // 设置资源路径到题目
            question.setResourcePaths(resourcePaths);

            // 添加题目
            questionBankService.addQuestionToBank(question);

            return ResponseEntity.status(HttpStatus.CREATED).body("题目添加成功，资源上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("题目添加失败: " + e.getMessage());
        }
    }
    @PostMapping("/questions/batch/upload")
    public ResponseEntity<String> addQuestionsWithResources(
            @RequestPart("questions") List<Questions> questions,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            // 上传文件并获取路径（假设文件的顺序与题目顺序对应）
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String resourcePath = aliOSSUtils.upload(file); // 上传到阿里云 OSS
                if (i < questions.size()) {
                    // 添加路径到题目的资源列表
                    Questions question = questions.get(i);
                    if (question.getResourcePaths() == null) {
                        question.setResourcePaths(new ArrayList<>());
                    }
                    question.getResourcePaths().add(resourcePath);
                }
            }

            // 添加题目到题库
            questionBankService.addQuestionsToBank(questions);

            return ResponseEntity.status(HttpStatus.CREATED).body("批量题目添加成功，资源上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("批量题目添加失败: " + e.getMessage());
        }
    }


}
