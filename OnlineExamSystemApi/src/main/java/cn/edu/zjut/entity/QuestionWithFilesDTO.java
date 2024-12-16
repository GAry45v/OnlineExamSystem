package cn.edu.zjut.entity;

import cn.edu.zjut.entity.Questions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class QuestionWithFilesDTO {
    private Questions question; // 题目信息
    private List<MultipartFile> files; // 文件列表

    public Questions getQuestion() {
        return question;
    }

    public void setQuestion(Questions question) {
        this.question = question;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
