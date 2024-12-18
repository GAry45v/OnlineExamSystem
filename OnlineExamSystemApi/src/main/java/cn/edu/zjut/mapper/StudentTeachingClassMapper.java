package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.StudentTeachingClass;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;
@Mapper
public interface StudentTeachingClassMapper {

    // 向学生教学班关联表添加学生
    @Insert("INSERT INTO StudentTeachingClass (studentNumber, teachingClassId, enrollmentDate) " +
            "VALUES (#{studentNumber}, #{teachingClassId}, #{enrollmentDate})")
    void addStudentToTeachingClass(@Param("studentNumber")String studentNumber,
                                   @Param("teachingClassId") Integer teachingClassId,
                                   @Param("enrollmentDate") Timestamp enrollmentDate);

    // 根据条件从学生库调入多个学生到教学班
    @Insert({
            "<script>",
            "INSERT INTO StudentTeachingClass (studentNumber, teachingClassId, enrollmentDate) VALUES ",
            "<foreach collection='studentNumbers' item='studentNumber' separator=','>",
            "(#{studentNumber}, #{teachingClassId}, #{enrollmentDate})",
            "</foreach>",
            "</script>"
    })
    void addStudentsToTeachingClass(@Param("studentNumbers") List<String> studentNumbers,
                                    @Param("teachingClassId") Integer teachingClassId,
                                    @Param("enrollmentDate") Timestamp enrollmentDate);
    @Select("SELECT studentNumber FROM StudentTeachingClass WHERE teachingClassId = #{teachingClassId}")
    List<String> findStudentNumbersByTeachingClassId(@Param("teachingClassId") Integer teachingClassId);
    // 删除单个学生
    @Delete("DELETE FROM StudentTeachingClass WHERE studentNumber = #{studentNumber} AND teachingClassId = #{teachingClassId}")
    void deleteStudentFromTeachingClass(@Param("studentNumber") String studentNumber, @Param("teachingClassId") Integer teachingClassId);

    // 批量删除学生
    @Delete({
            "<script>",
            "DELETE FROM StudentTeachingClass WHERE teachingClassId = #{teachingClassId} AND studentNumber IN ",
            "<foreach collection='studentNumbers' item='studentNumber' open='(' separator=',' close=')'>",
            "#{studentNumber}",
            "</foreach>",
            "</script>"
    })
    void deleteStudentsFromTeachingClass(@Param("studentNumbers") List<String> studentNumbers, @Param("teachingClassId") Integer teachingClassId);
}

