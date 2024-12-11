package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface CourseMapper {

    // 教师创建课程
    @Insert("INSERT INTO Course(courseName, semester, createdByTeacherId) " +
            "VALUES(#{course.courseName}, #{course.semester}, #{course.createdByTeacherId})")
    void createCourse(@Param("course") Course course);
}
