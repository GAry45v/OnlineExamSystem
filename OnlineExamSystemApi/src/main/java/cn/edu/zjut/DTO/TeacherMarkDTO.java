package cn.edu.zjut.DTO;

import cn.edu.zjut.entity.Questions;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherMarkDTO {
    private Questions question_bone;
    private BigDecimal teachermark;
    private String teachercomment;
    private int paperId;
}
