package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Course;
import cn.edu.zjut.entity.TeacherCourse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 教师创建课程
    @Insert("INSERT INTO Course(courseName, semester, createdByEmployeeNumber) " +
            "VALUES(#{course.courseName}, #{course.semester}, #{course.createdByEmployeeNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "courseId")
    void createCourse(@Param("course") Course course);

    // 教师删除课程，同时级联删除 TeacherCourse 表中的记录
    @Delete("DELETE FROM TeacherCourse WHERE courseId = #{courseId}")
    void deleteFromTeacherCourse(@Param("courseId") Integer courseId);

    @Delete("DELETE FROM Course WHERE courseId = #{courseId}")
    void deleteFromCourse(@Param("courseId") Integer courseId);

    // 查询某个教师所创建的所有课程
    @Select("SELECT * FROM Course WHERE createdByEmployeeNumber = #{employeeNumber}")
    List<Course> findCoursesByEmployeeNumber(@Param("employeeNumber") String employeeNumber);

    // 添加教师与课程的关联记录
    @Insert("INSERT INTO TeacherCourse (employeeNumber, courseId) VALUES (#{teacherCourse.employeeNumber}, #{teacherCourse.courseId})")
    void addTeacherToCourse(@Param("teacherCourse") TeacherCourse teacherCourse);

    // 删除教师与课程的关联记录
    @Delete("DELETE FROM TeacherCourse WHERE employeeNumber = #{teacherCourse.employeeNumber} AND courseId = #{teacherCourse.courseId}")
    void removeTeacherFromCourse(@Param("teacherCourse") TeacherCourse teacherCourse);

    // 查询课程中的所有教师
    @Select("SELECT employeeNumber FROM TeacherCourse WHERE courseId = #{courseId}")
    List<String> findTeachersByCourseId(@Param("courseId") Integer courseId);
}
