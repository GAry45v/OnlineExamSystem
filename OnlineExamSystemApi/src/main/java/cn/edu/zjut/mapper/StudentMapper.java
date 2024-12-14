package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {

    // 按学号或姓名查找学生
    @Select("<script>" +
            "SELECT * FROM Student " +
            "WHERE 1=1 " +
            "<if test='studentNumber != null and studentNumber != \"\"'> " +
            "AND studentNumber = #{studentNumber} " +
            "</if>" +
            "<if test='name != null and name != \"\"'> " +
            "AND name LIKE CONCAT('%', #{name}, '%') " +
            "</if>" +
            "</script>")
    List<Student> findStudentByStudentNumberOrName(@Param("studentNumber") String studentNumber,
                                                   @Param("name") String name);

    // 按学院、专业、行政班级查找学生
    @Select("<script>" +
            "SELECT * FROM Student " +
            "WHERE 1=1 " +
            "<if test='collegeId != null'> " +
            "AND collegeId = #{collegeId} " +
            "</if>" +
            "<if test='majorId != null'> " +
            "AND majorId = #{majorId} " +
            "</if>" +
            "<if test='classId != null'> " +
            "AND classId = #{classId} " +
            "</if>" +
            "</script>")
    List<Student> findStudentsByCollegeMajorClass(@Param("collegeId") Integer collegeId,
                                                  @Param("majorId") Integer majorId,
                                                  @Param("classId") Integer classId);

    // 根据学号列表查询学生信息
    @Select({
            "<script>",
            "SELECT * FROM Student WHERE studentNumber IN ",
            "<foreach collection='studentNumbers' item='studentNumber' open='(' separator=',' close=')'>",
            "#{studentNumber}",
            "</foreach>",
            "</script>"
    })
    List<Student> findStudentsByStudentNumbers(@Param("studentNumbers") List<String> studentNumbers);
}
