package cn.edu.zjut.DTO;

import cn.edu.zjut.entity.Questions;
import lombok.Data;

@Data
public class AnswerPaperDTO {
    private Questions question_bone;
    private String answerContent;
    private String Aicomment;
    private int paperId;
    private int questionMark;
}
