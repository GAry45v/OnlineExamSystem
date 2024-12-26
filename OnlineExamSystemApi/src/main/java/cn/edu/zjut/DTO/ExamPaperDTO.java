package cn.edu.zjut.DTO;

import lombok.Data;

@Data
public class ExamPaperDTO {
    private int studentExamId;
    private String studentNumber;
    private String name;
    private String questionBankId;
}
