package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.StudentTeachingClass;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.security.Timestamp;
import java.util.List;

public interface StudentTeachingClassMapper {

    // 向学生教学班关联表添加学生
    @Insert("INSERT INTO StudentTeachingClass (studentId, teachingClassId, enrollmentDate) " +
            "VALUES (#{studentId}, #{teachingClassId}, #{enrollmentDate})")
    void addStudentToTeachingClass(@Param("studentId") Integer studentId,
                                   @Param("teachingClassId") Integer teachingClassId,
                                   @Param("enrollmentDate") Timestamp enrollmentDate);

    // 根据条件从学生库调入多个学生到教学班
    @Insert({
            "<script>",
            "INSERT INTO StudentTeachingClass (studentId, teachingClassId, enrollmentDate) VALUES ",
            "<foreach collection='studentIds' item='studentId' separator=','>",
            "(#{studentId}, #{teachingClassId}, #{enrollmentDate})",
            "</foreach>",
            "</script>"
    })
    void addStudentsToTeachingClass(@Param("studentIds") List<Integer> studentIds,
                                    @Param("teachingClassId") Integer teachingClassId,
                                    @Param("enrollmentDate") Timestamp enrollmentDate);
}
