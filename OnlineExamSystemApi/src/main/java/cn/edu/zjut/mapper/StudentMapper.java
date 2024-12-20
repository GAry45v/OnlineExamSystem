package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {
    // 根据班级ID列表查找学生
    @Select("<script>" +
            "SELECT * FROM Student " +
            "WHERE 1=1 " +
            "<if test='classIds != null and classIds.size() > 0'> AND classId IN " +
            "<foreach collection='classIds' item='id' open='(' separator=',' close=')'> #{id} </foreach> </if>" +
            "</script>")
    List<Student> findStudentsByClassIds(@Param("classIds") List<Integer> classIds);
    // 根据教学班ID查询所有学生的学号
    @Select("SELECT studentNumber FROM StudentTeachingClass WHERE teachingClassId = #{teachingClassId}")
    List<String> findStudentNumbersByTeachingClassId(Integer teachingClassId);

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
    // 根据学院名称查询学院ID

    @Select("<script>" +
            "SELECT s.studentId, s.studentNumber, s.name, s.enrollmentYear," +
            "c.name AS collegeName, m.name AS majorName, cl.name AS className " +
            "FROM Student s " +
            "LEFT JOIN Class cl ON s.classId = cl.classId " +
            "LEFT JOIN Major m ON cl.majorId = m.majorId " +
            "LEFT JOIN College c ON m.collegeId = c.collegeId " +
            "WHERE 1=1 " +
            "<if test='studentNumber != null'> AND s.studentNumber = #{studentNumber} </if>" +
            "<if test='name != null'> AND s.name = #{name} </if>" +
            "<if test='collegeName != null'> AND c.name = #{collegeName} </if>" +
            "<if test='majorName != null'> AND m.name = #{majorName} </if>" +
            "<if test='className != null'> AND cl.name = #{className} </if>" +
            "</script>")
    List<StudentDTO> findStudentsWithDetails(@Param("studentNumber") String studentNumber,
                                             @Param("name") String name,
                                             @Param("collegeName") String collegeName,
                                             @Param("majorName") String majorName,
                                             @Param("className") String className);

    // 根据学生学号查找学生详细信息
    @Select("SELECT s.studentId, s.studentNumber, s.name, s.enrollmentYear, " +
            "c.name AS collegeName, m.name AS majorName, cl.name AS className " +
            "FROM Student s " +
            "LEFT JOIN Class cl ON s.classId = cl.classId " +
            "LEFT JOIN Major m ON cl.majorId = m.majorId " +
            "LEFT JOIN College c ON m.collegeId = c.collegeId " +
            "WHERE s.studentNumber = #{studentNumber}")
    StudentDTO findStudentByStudentNumber(@Param("studentNumber") String studentNumber);
}
