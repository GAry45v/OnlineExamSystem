package cn.edu.zjut.mapper;

import cn.edu.zjut.DTO.ExamPaperDTO;
import cn.edu.zjut.entity.Exam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamMapper {


        @Insert("INSERT INTO Exam (examName, createdByEmployeeNumber, startTime, durationMinutes, isAntiCheatingEnabled, paperId, examStatus, endTime) " +
                "VALUES (#{examName}, #{createdByEmployeeNumber}, #{startTime}, #{durationMinutes}, #{isAntiCheatingEnabled}, #{paperId}, #{examStatus}, #{endTime})")
        @Options(useGeneratedKeys = true, keyProperty = "examId")
        void createExam(Exam exam);

        @Select("SELECT * FROM Exam WHERE createdByEmployeeNumber = #{employeeNumber}")
        List<Exam> findExamsByTeacher(@Param("employeeNumber") String employeeNumber);

        @Update("UPDATE Exam SET examName = #{examName}, startTime = #{startTime}, durationMinutes = #{durationMinutes}, " +
                "isAntiCheatingEnabled = #{isAntiCheatingEnabled}, paperId = #{paperId}, endTime = #{endTime} WHERE examId = #{examId}")
        void updateExam(Exam exam);

        @Delete("DELETE FROM Exam WHERE examId = #{examId}")
        void deleteExam(@Param("examId") int examId);

        @Select("SELECT * FROM Exam WHERE examId = #{examId}")
        Exam findExamById(@Param("examId") int examId);

        @Select("SELECT se.studentExamId, se.studentNumber, s.name, p.questionBankId " +
                "FROM StudentExam se " +
                "JOIN Student s ON se.studentNumber = s.studentNumber " +
                "JOIN Exam e ON se.examId = e.examId " +
                "JOIN Papers p ON e.paperId = p.paperId " +
                "WHERE se.examId = #{examId} AND se.status = '待批阅'")
        List<ExamPaperDTO> findPendingPaperByExamId(@Param("examId") int examId);
        @Select("""
        SELECT e.paperId
        FROM Exam e
        JOIN StudentExam se ON e.examId = se.examId
        WHERE se.studentExamId = #{studentExamId}
    """)
        Integer getPaperIdByStudentExamId(@Param("studentExamId") int studentExamId);

    }



