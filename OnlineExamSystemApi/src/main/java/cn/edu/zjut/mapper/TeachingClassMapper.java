package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Teacher;
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
    @Select({
            "<script>",
            "SELECT * FROM Teacher",
            "WHERE 1=1",
            "<if test='employeeNumber != null and employeeNumber.trim() != \"\"'>",
            "AND employeeNumber = #{employeeNumber}",
            "</if>",
            "<if test='name != null and name.trim() != \"\"'>",
            "AND name = #{name}",
            "</if>",
            "</script>"
    })
    List<Teacher> findTeacherByEmployeeNumberOrName(
            @Param("employeeNumber") String employeeNumber,
            @Param("name") String name
    );
    // 插入教师与教学班的关联
    @Insert("INSERT INTO TeacherTeachingClass(employeeNumber, teachingClassId, role) " +
            "VALUES(#{teacherTeachingClass.employeeNumber}, #{teacherTeachingClass.teachingClassId}, #{teacherTeachingClass.role})")
    void createTeacherTeachingClass(@Param("teacherTeachingClass") TeacherTeachingClass teacherTeachingClass);

    // 删除教师与教学班的关联
    @Delete("DELETE FROM TeacherTeachingClass WHERE teachingClassId = #{teachingClassId} AND employeeNumber = #{employeeNumber}")
    void deleteTeacherTeachingClass(@Param("employeeNumber") String employeeNumber, @Param("teachingClassId") Integer teachingClassId);

    @Delete("DELETE FROM TeacherTeachingClass WHERE teachingClassId = #{teachingClassId} ")
    void deleteTeacherTeachingClass_all(@Param("teachingClassId") Integer teachingClassId);
    // 检测教师是否为某教学班的主讲
    @Select("SELECT COUNT(*) FROM TeacherTeachingClass " +
            "WHERE teachingClassId = #{teachingClassId} " +
            "AND employeeNumber = #{employeeNumber} " +
            "AND role = '主讲'")
    int countMainLecturer(@Param("employeeNumber") String employeeNumber,
                          @Param("teachingClassId") Integer teachingClassId);

    // 删除 TeacherTeachingClass 表中关联记录
    @Delete("""
        DELETE FROM TeacherTeachingClass 
        WHERE teachingClassId IN (
            SELECT teachingClassId 
            FROM TeachingClass 
            WHERE courseId = #{courseId}
        )
    """)
    void deleteTeacherTeachingClassByCourseId(@Param("courseId") Integer courseId);

    // 删除 TeachingClass 表中与 courseId 相关的记录
    @Delete("""
        DELETE FROM TeachingClass 
        WHERE courseId = #{courseId}
    """)
    void deleteTeachingClassByCourseId(@Param("courseId") Integer courseId);
    @Select("""
        SELECT t.* 
        FROM Teacher t
        JOIN TeacherTeachingClass tt ON t.employeeNumber = tt.employeeNumber
        WHERE tt.teachingClassId = #{teachingClassId}
    """)
    List<Teacher> findTeachersByTeachingClassId(@Param("teachingClassId") Integer teachingClassId);
}
