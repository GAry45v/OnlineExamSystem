package cn.edu.zjut.mapper;

import cn.edu.zjut.DTO.StudentAnswerDTO;
import cn.edu.zjut.entity.StudentAnswerAndGrading;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentAnswerMapper {
    @Insert("INSERT INTO StudentAnswerAndGrading (studentExamId, paperQuestionId, answerContent, graderEmployeeNumber, score, gradingTime, comments, answerStatus) " +
            "VALUES (#{studentExamId}, #{paperQuestionId}, #{answerContent}, #{graderEmployeeNumber}, #{score}, #{gradingTime}, #{comments}, #{answerStatus})")
    void insertAnswer(StudentAnswerAndGrading answer);

    @Select("SELECT * FROM StudentAnswerAndGrading WHERE studentExamId = #{studentExamId}")
    List<StudentAnswerDTO> findAnswersByStudentExamId(@Param("studentExamId") int studentExamId);

    @Update("UPDATE StudentAnswerAndGrading SET score = #{score}, comments = #{comments}, answerStatus = '已批阅' WHERE answerId = #{answerId}")
    void updateAnswerGrading(StudentAnswerDTO answer);
}
