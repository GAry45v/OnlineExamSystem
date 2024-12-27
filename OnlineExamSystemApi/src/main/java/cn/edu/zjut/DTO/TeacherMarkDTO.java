package cn.edu.zjut.DTO;

import cn.edu.zjut.entity.Questions;
import lombok.Data;

@Data
public class TeacherMarkDTO {
    private Questions question_bone;
    private String teachermark;
    private String teachercomment;
    private int paperId;
}
