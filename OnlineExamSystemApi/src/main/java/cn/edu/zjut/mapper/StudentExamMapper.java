package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.StudentExam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentExamMapper {

    @Insert("INSERT INTO StudentExam (examId, studentNumber, status, score) " +
            "VALUES (#{examId}, #{studentNumber}, #{status}, #{score})")
    void insertStudentExam(StudentExam studentExam);

    @Select("SELECT * FROM StudentExam WHERE studentNumber = #{studentNumber}")
    List<StudentExam> findExamsByStudentNumber(String studentNumber);

    @Update("UPDATE StudentExam SET status = #{status} WHERE examId = #{examId}")
    void updateExamStatus(@Param("examId") int examId, @Param("status") String status);

    @Update("UPDATE StudentExam SET status = #{status} WHERE examId = #{examId} AND studentNumber = #{studentNumber}")
    void updateStudentExamStatus(@Param("examId") int examId, @Param("studentNumber") String studentNumber, @Param("status") String status);

    @Select("SELECT * FROM StudentExam WHERE examId = #{examId} AND status = '待批阅'")
    List<StudentExam> findPendingStudentExams(@Param("examId") int examId);

    @Update("UPDATE StudentExam SET status = #{status} WHERE examId = #{examId} AND studentNumber = #{studentNumber}")
    void updateExamStatusForStudent(@Param("examId") int examId, @Param("studentNumber") String studentNumber, @Param("status") String status);
}
