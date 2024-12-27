package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.StudentAnswerAndGrading;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface StudentAnswerAndGradingMapper {

    @Select("SELECT * FROM StudentAnswerAndGrading WHERE studentExamId = #{studentExamId}")
    List<StudentAnswerAndGrading> findAnswersByStudentExamId(int studentExamId);

    @Update("UPDATE StudentAnswerAndGrading " +
            "SET score = #{score}, comments = #{comments}, graderEmployeeNumber = #{graderEmployeeNumber} " +
            "WHERE studentExamId = #{studentExamId} AND paperQuestionId = #{paperQuestionId}")
    void teacher_updateStudentAnswerAndGrading(
            @Param("studentExamId") int studentExamId,
            @Param("paperQuestionId") int paperQuestionId,
            @Param("score") String score,
            @Param("comments") String comments,
            @Param("graderEmployeeNumber") String graderEmployeeNumber
    );
}
