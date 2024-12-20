package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.*;
import cn.edu.zjut.entity.Class;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

@Mapper
public interface AdminMapper {

    /**
     * 插入单条教师信息
     */
    @Insert("INSERT INTO Teacher (name, employeeNumber, collegeId, schoolId, faceImageId) " +
            "VALUES (#{name}, #{employeeNumber}, #{collegeId}, #{schoolId}, #{faceImageId})")
    void insertTeacher(
            @Param("name") String name,
            @Param("employeeNumber") String employeeNumber,
            @Param("collegeId") Integer collegeId,
            @Param("schoolId") Integer schoolId,
            @Param("faceImageId") String faceImageId
    );

    /**
     * 批量插入教师信息
     */
    @Insert({
            "<script>",
            "INSERT INTO Teacher (name, employeeNumber, collegeId, schoolId, faceImageId) VALUES ",
            "<foreach collection='teachers' item='teacher' index='index' separator=','>",
            "(#{teacher.name}, #{teacher.employeeNumber}, #{teacher.collegeId}, #{teacher.schoolId}, #{teacher.faceImageId})",
            "</foreach>",
            "</script>"
    })
    void batchInsertTeachers(@Param("teachers") List<Teacher> teachers);

    /**
     * 插入单个学生信息
     *
     * @param student 学生对象
     */
    @Insert("INSERT INTO Student (name, enrollmentYear, studentNumber, faceImageId, schoolId, collegeId, majorId, classId) " +
            "VALUES (#{name}, #{enrollmentYear}, #{studentNumber}, #{faceImageId}, #{schoolId}, #{collegeId}, #{majorId}, #{classId})")
    void insertStudent(Student student);

    /**
     * 批量插入学生信息
     *
     * @param students 学生列表
     */
    @Insert({
            "<script>",
            "INSERT INTO Student (name, enrollmentYear, studentNumber, faceImageId, schoolId, collegeId, majorId, classId) VALUES ",
            "<foreach collection='students' item='student' separator=','>",
            "(#{student.name}, #{student.enrollmentYear}, #{student.studentNumber}, #{student.faceImageId}, ",
            "#{student.schoolId}, #{student.collegeId}, #{student.majorId}, #{student.classId})",
            "</foreach>",
            "</script>"
    })
    void batchInsertStudents(@Param("students") List<Student> students);

    /**
     * 插入单个学校信息
     *
     * @param school 学校对象
     */
    @Insert("INSERT INTO School (name, location) VALUES (#{name}, #{location})")
    void insertSchool(School school);

    /**
     * 批量插入学校信息
     *
     * @param schools 学校列表
     */
    @Insert({
            "<script>",
            "INSERT INTO School (name, location) VALUES ",
            "<foreach collection='schools' item='school' separator=','>",
            "(#{school.name}, #{school.location})",
            "</foreach>",
            "</script>"
    })
    void batchInsertSchools(@Param("schools") List<School> schools);

    @Insert("INSERT INTO Class (majorId, name) VALUES (#{majorId}, #{name})")
    void insertClass(Class classEntity);

    /**
     * 批量插入班级信息
     *
     * @param classes 班级列表
     */
    @Insert({
            "<script>",
            "INSERT INTO Class (majorId, name) VALUES ",
            "<foreach collection='classes' item='classEntity' separator=','>",
            "(#{classEntity.majorId}, #{classEntity.name})",
            "</foreach>",
            "</script>"
    })
    void batchInsertClasses(@Param("classes") List<Class> classes);

    @Insert("INSERT INTO College (schoolId, name) VALUES (#{schoolId}, #{name})")
    void insertCollege(College college);
    @Insert({
            "<script>",
            "INSERT INTO College (schoolId, name) VALUES ",
            "<foreach collection='colleges' item='college' separator=','>",
            "(#{college.schoolId}, #{college.name})",
            "</foreach>",
            "</script>"
    })
    void batchInsertColleges(@Param("colleges") List<College> colleges);
    @Insert("INSERT INTO Course (courseName, semester, createdByEmployeeNumber) " +
            "VALUES (#{courseName}, #{semester}, #{createdByEmployeeNumber})")
    void insertCourse(Course course);
    @Insert({
            "<script>",
            "INSERT INTO Course (courseName, semester, createdByEmployeeNumber) VALUES ",
            "<foreach collection='courses' item='course' separator=','>",
            "(#{course.courseName}, #{course.semester}, #{course.createdByEmployeeNumber})",
            "</foreach>",
            "</script>"
    })
    void batchInsertCourses(@Param("courses") List<Course> courses);
    @Insert("INSERT INTO Major (collegeId, name) VALUES (#{collegeId}, #{name})")
    void insertMajor(Major major);

    /**
     * 批量插入专业信息
     *
     * @param majors 专业列表
     */
    @Insert({
            "<script>",
            "INSERT INTO Major (collegeId, name) VALUES ",
            "<foreach collection='majors' item='major' separator=','>",
            "(#{major.collegeId}, #{major.name})",
            "</foreach>",
            "</script>"
    })
    void batchInsertMajors(@Param("majors") List<Major> majors);
}
