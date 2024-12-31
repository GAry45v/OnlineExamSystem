package cn.edu.zjut.controller;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.DTO.QuestionWithFilesDTO;
import cn.edu.zjut.entity.QuestionBank;
import cn.edu.zjut.entity.Questions;
import cn.edu.zjut.service.QuestionBankService;
import cn.edu.zjut.util.AliOSSUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private AliOSSUtils aliOSSUtils;

    @PostMapping
    public ResponseEntity<String> createQuestionBank(@RequestBody QuestionBank questionBank) {
        try {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            questionBank.setEmployeeNumber(employeeNumber);
            questionBankService.createQuestionBank(questionBank);
            return ResponseEntity.status(HttpStatus.CREATED).body("题库创建成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("题库创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/questions")
    public ResponseEntity<String> addQuestionToBank(@RequestBody Questions question) {
        try {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            question.setEmployeeNumber(employeeNumber);
            questionBankService.addQuestionToBank(question);
            return ResponseEntity.status(HttpStatus.CREATED).body("题目添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("题目添加失败: " + e.getMessage());
        }
    }

    @PostMapping("/questions/batch")
    public ResponseEntity<String> addQuestionsToBank(@RequestBody List<Questions> questions) {
        try {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            questions.forEach(question -> question.setEmployeeNumber(employeeNumber));
            questionBankService.addQuestionsToBank(questions);
            return ResponseEntity.status(HttpStatus.CREATED).body("批量题目添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("批量题目添加失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/questions")
    public ResponseEntity<String> deleteQuestionFromBank(@RequestBody Questions question) {
        try {
            questionBankService.deleteQuestionFromBank(question);
            return ResponseEntity.status(HttpStatus.OK).body("题目删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("题目删除失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{questionBankId}")
    public ResponseEntity<String> deleteQuestionBank(@PathVariable String questionBankId) {
        try {
            questionBankService.deleteQuestionBank(questionBankId);
            return ResponseEntity.status(HttpStatus.OK).body("题库删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("题库删除失败: " + e.getMessage());
        }
    }

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

    @GetMapping("/employee/own")
    public ResponseEntity<List<QuestionBank>> getBanksOwn() {
        try {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            List<QuestionBank> banks = questionBankService.findBanksByEmployee(employeeNumber);
            if (banks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(banks);
            }
            return ResponseEntity.status(HttpStatus.OK).body(banks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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

    @PostMapping("/upload")
    public ResponseEntity<String> addQuestionWithResources(
            @RequestPart("question") Questions question,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("fileMetadata") String fileMetadataJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<FileMetadata> fileMetadataList = objectMapper.readValue(fileMetadataJson, new TypeReference<>() {});

            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            question.setEmployeeNumber(employeeNumber);

            questionBankService.addQuestionWithResources(question, files, fileMetadataList);

            return ResponseEntity.status(HttpStatus.CREATED).body("题目添加成功，资源上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("题目添加失败: " + e.getMessage());
        }
    }

    @GetMapping("/bank/{questionBankId}/question/{questionId}")
    public ResponseEntity<?> getQuestionByBankIdAndQuestionId(
            @PathVariable String questionBankId,
            @PathVariable String questionId) {
        try {
            Questions question = questionBankService.findQuestionByBankIdAndQuestionId(questionBankId, questionId);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("获取题目失败: " + e.getMessage());
        }
    }

//    @PutMapping("/{questionBankId}/questions")
//    public ResponseEntity<String> updateQuestionInBank(
//            @PathVariable String questionBankId,
//            @RequestBody Questions updatedQuestion) {
//        try {
//            questionBankService.updateQuestionInBank(questionBankId, updatedQuestion);
//            return ResponseEntity.ok("题目更新成功");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("题目更新失败: " + e.getMessage());
//        }
//    }
    @PutMapping("/{questionBankId}/questions")
    public ResponseEntity<String> updateQuestionInBank(
            @PathVariable String questionBankId,
            @RequestPart("question") Questions updatedQuestion,
            @RequestPart("files") List<MultipartFile> files,
            @RequestPart("fileMetadata") String fileMetadataJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<FileMetadata> fileMetadataList = objectMapper.readValue(fileMetadataJson, new TypeReference<>() {});

            questionBankService.updateQuestionInBank(questionBankId, updatedQuestion, files, fileMetadataList);
            return ResponseEntity.ok("题目更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("题目更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/questions/batch/upload")
    public ResponseEntity<String> addQuestionsWithResources(
            @RequestPart("questionsWithFiles") List<QuestionWithFilesDTO> questionsWithFilesList) {
        try {
            questionsWithFilesList.forEach(dto -> {
                try {
                    List<String> resourcePaths = new ArrayList<>();
                    for (MultipartFile file : dto.getFiles()) {
                        String resourcePath = aliOSSUtils.upload(file);
                        resourcePaths.add(resourcePath);
                    }
                    dto.getQuestion().setResourcePaths(resourcePaths);
                } catch (IOException e) {
                    throw new RuntimeException("文件上传失败: " + e.getMessage());
                }
            });

            List<Questions> questions = questionsWithFilesList.stream()
                    .map(QuestionWithFilesDTO::getQuestion)
                    .collect(Collectors.toList());
            questionBankService.addQuestionsToBank(questions);

            return ResponseEntity.status(HttpStatus.CREATED).body("批量题目添加成功，资源上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("批量题目添加失败: " + e.getMessage());
        }
    }

    @Data
    public static class FileMetadata {
        private String fileName;
        private String type;
        private String optionKey;
    }
}
