package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.entity.TeacherTeachingClass;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeachingClassMapper {

    // 创建教学班
    @Insert("INSERT INTO TeachingClass(courseId, className) " +
            "VALUES(#{teachingClass.courseId}, #{teachingClass.className})")
    @Options(useGeneratedKeys = true, keyProperty = "teachingClassId")
    void createTeachingClass(@Param("teachingClass") TeachingClass teachingClass);

    // 删除教学班
    @Delete("DELETE FROM TeachingClass WHERE teachingClassId = #{teachingClassId}")
    void deleteTeachingClass(@Param("teachingClassId") Integer teachingClassId);

    // 查询某个课程的所有教学班
    @Select("SELECT * FROM TeachingClass WHERE courseId = #{courseId}")
    List<TeachingClass> findByCourseId(@Param("courseId") Integer courseId);

    // 查询某个教师的所有教学班
    @Select("SELECT t.* FROM TeachingClass t " +
            "JOIN TeacherTeachingClass tt ON t.teachingClassId = tt.teachingClassId " +
            "WHERE tt.employeeNumber = #{employeeNumber}")
    List<TeachingClass> findByEmployeeNumber(@Param("employeeNumber") String employeeNumber);

    // 插入教师与教学班的关联
    @Insert("INSERT INTO TeacherTeachingClass(employeeNumber, teachingClassId, role) " +
            "VALUES(#{teacherTeachingClass.employeeNumber}, #{teacherTeachingClass.teachingClassId}, #{teacherTeachingClass.role})")
    void createTeacherTeachingClass(@Param("teacherTeachingClass") TeacherTeachingClass teacherTeachingClass);

    // 删除教师与教学班的关联
    @Delete("DELETE FROM TeacherTeachingClass WHERE teachingClassId = #{teachingClassId} AND employeeNumber = #{employeeNumber}")
    void deleteTeacherTeachingClass(@Param("employeeNumber") String employeeNumber, @Param("teachingClassId") Integer teachingClassId);
}
