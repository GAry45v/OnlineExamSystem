package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 教师创建课程
    @Insert("INSERT INTO Course(courseName, semester, createdByTeacherId) " +
            "VALUES(#{course.courseName}, #{course.semester}, #{course.createdByTeacherId})")
    void createCourse(@Param("course") Course course);

    // 教师删除课程
    @Delete("DELETE FROM Course WHERE courseId = #{courseId}")
    void deleteCourse(@Param("courseId") Integer courseId);

    // 查询某个教师所创建的所有课程
    @Select("SELECT * FROM Course WHERE createdByTeacherId = #{teacherId}")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Integer teacherId);
}
