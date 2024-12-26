package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.StudentAnswerAndGrading;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentAnswerAndGradingMapper {

    @Select("SELECT * FROM StudentAnswerAndGrading WHERE studentExamId = #{studentExamId}")
    List<StudentAnswerAndGrading> findAnswersByStudentExamId(int studentExamId);
}
