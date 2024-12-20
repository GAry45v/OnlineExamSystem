package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.*;
import cn.edu.zjut.entity.Class;
import cn.edu.zjut.mapper.AdminMapper;
import cn.edu.zjut.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Override
    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        adminMapper.insertTeacher(
                teacher.getName(),
                teacher.getEmployeeNumber(),
                teacher.getCollegeId(),
                teacher.getSchoolId(),
                teacher.getFaceImageId()
        );
    }

    @Override
    public void addTeachersBatch(List<Teacher> teachers) {
        if (teachers == null || teachers.isEmpty()) {
            throw new IllegalArgumentException("Teacher list cannot be null or empty");
        }
        adminMapper.batchInsertTeachers(teachers);
    }

    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("学生信息不能为空！");
        }
        adminMapper.insertStudent(student);
    }
    @Transactional
    public void addStudentsBatch(List<Student> students) {
        if (students == null || students.isEmpty()) {
            throw new IllegalArgumentException("学生列表不能为空！");
        }
        adminMapper.batchInsertStudents(students);
    }
    public void addSchool(School school) {
        if (school == null || school.getName() == null || school.getName().isEmpty()) {
            throw new IllegalArgumentException("学校信息不完整！");
        }
        adminMapper.insertSchool(school);
    }
    @Transactional
    public void addSchoolsBatch(List<School> schools) {
        if (schools == null || schools.isEmpty()) {
            throw new IllegalArgumentException("学校列表不能为空！");
        }
        adminMapper.batchInsertSchools(schools);
    }
    public void addClass(Class classEntity) {
        if (classEntity == null || classEntity.getName() == null || classEntity.getName().isEmpty()) {
            throw new IllegalArgumentException("班级信息不完整！");
        }
        adminMapper.insertClass(classEntity);
    }
    @Transactional
    public void addClassesBatch(List<Class> classes) {
        if (classes == null || classes.isEmpty()) {
            throw new IllegalArgumentException("班级列表不能为空！");
        }
        adminMapper.batchInsertClasses(classes);
    }
    public void addCollege(College college) {
        if (college == null || college.getName() == null || college.getName().isEmpty()) {
            throw new IllegalArgumentException("学院信息不完整！");
        }
        adminMapper.insertCollege(college);
    }

    /**
     * 批量添加学院信息
     *
     * @param colleges 学院列表
     */
    @Transactional
    public void addCollegesBatch(List<College> colleges) {
        if (colleges == null || colleges.isEmpty()) {
            throw new IllegalArgumentException("学院列表不能为空！");
        }
        adminMapper.batchInsertColleges(colleges);
    }
    public void addCourse(Course course) {
        if (course == null || course.getCourseName() == null || course.getCourseName().isEmpty()) {
            throw new IllegalArgumentException("课程信息不完整！");
        }
        adminMapper.insertCourse(course);
    }

    /**
     * 批量添加课程信息
     *
     * @param courses 课程列表
     */
    @Transactional
    public void addCoursesBatch(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            throw new IllegalArgumentException("课程列表不能为空！");
        }
        adminMapper.batchInsertCourses(courses);
    }
    public void addMajor(Major major) {
        if (major == null || major.getName() == null || major.getName().isEmpty()) {
            throw new IllegalArgumentException("专业信息不完整！");
        }
        adminMapper.insertMajor(major);
    }

    /**
     * 批量添加专业信息
     *
     * @param majors 专业列表
     */
    @Transactional
    public void addMajorsBatch(List<Major> majors) {
        if (majors == null || majors.isEmpty()) {
            throw new IllegalArgumentException("专业列表不能为空！");
        }
        adminMapper.batchInsertMajors(majors);
    }
}
