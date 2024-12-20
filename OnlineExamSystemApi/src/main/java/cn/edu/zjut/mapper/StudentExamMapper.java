package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.StudentExam;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentExamMapper {

    @Insert("INSERT INTO StudentExam (examId, studentNumber, status, score) " +
            "VALUES (#{examId}, #{studentNumber}, #{status}, #{score})")
    void insertStudentExam(StudentExam studentExam);
}
