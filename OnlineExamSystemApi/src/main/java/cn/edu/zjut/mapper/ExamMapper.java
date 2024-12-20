package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Exam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamMapper {
    @Insert("INSERT INTO Exam (examName, createdByEmployeeNumber, startTime, durationMinutes, isAntiCheatingEnabled, paperId, examStatus) " +
            "VALUES (#{examName}, #{createdByEmployeeNumber}, #{startTime}, #{durationMinutes}, #{isAntiCheatingEnabled}, #{paperId}, #{examStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "examId")
    void createExam(Exam exam);

    @Select("SELECT * FROM Exam WHERE createdByEmployeeNumber = #{employeeNumber}")
    List<Exam> findExamsByTeacher(@Param("employeeNumber") String employeeNumber);

    @Update("UPDATE Exam SET examName = #{examName}, startTime = #{startTime}, durationMinutes = #{durationMinutes}, " +
            "isAntiCheatingEnabled = #{isAntiCheatingEnabled}, paperId = #{paperId} WHERE examId = #{examId}")
    void updateExam(Exam exam);

    @Delete("DELETE FROM Exam WHERE examId = #{examId}")
    void deleteExam(@Param("examId") int examId);
    @Select("SELECT * FROM Exam WHERE examId = #{examId}")
    Exam findExamById(@Param("examId") int examId);

}
