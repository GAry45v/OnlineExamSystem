package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.entity.TeachingClass;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface TeachingClassMapper {

    // 教师创建教学班
    @Insert("INSERT INTO TeachingClass(courseId, className) " +
            "VALUES(#{teachingClass.courseId}, #{teachingClass.className})")
    void createTeachingClass(@Param("teachingClass") TeachingClass teachingClass);@Insert("INSERT INTO StudentTeachingClass(studentId, teachingClassId, enrollmentDate) " +
            "VALUES(#{studentTeachingClass.studentId}, #{studentTeachingClass.teachingClassId}, NOW())")
    void enrollStudentInTeachingClass(@Param("studentTeachingClass") StudentTeachingClass studentTeachingClass);
}
