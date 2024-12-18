package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.PaperQuestions;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper  // 添加此注解
public interface PaperQuestionsMapper {
    //关联试卷和题目
    @Insert("INSERT INTO PaperQuestions (paperId, questionType, marks, questionOrder, questionId) " +
            "VALUES (#{paperId}, #{questionType}, #{marks}, #{questionOrder}, #{questionId})")
    @Options(useGeneratedKeys = true, keyProperty = "paperQuestionId")
    void addQuestionToPaper(PaperQuestions question);
    @Select("SELECT COUNT(*) FROM PaperQuestions WHERE paperId = #{paperId}")
    int getQuestionCountByPaperId(@Param("paperId") int paperId);
    //返回某张试卷的所有信息
    @Select("SELECT * FROM PaperQuestions WHERE paperId = #{paperId}")
    List<PaperQuestions> findQuestionsByPaperId(@Param("paperId") int paperId);
    //删除某张试卷的所有题目记录
    @Delete("DELETE FROM PaperQuestions WHERE paperQuestionId = #{paperQuestionId}")
    void deleteQuestionFromPaper(@Param("paperQuestionId") int paperQuestionId);
}
