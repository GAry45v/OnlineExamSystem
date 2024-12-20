package cn.edu.zjut.service;

import cn.edu.zjut.entity.*;
import cn.edu.zjut.entity.Class;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface AdminService {
    public void addTeacher(Teacher teacher);

    public void addTeachersBatch(List<Teacher> teachers);

    public void addStudent(Student student);

    public void addStudentsBatch(List<Student> students);
    public void addSchool(School school);
    @Transactional
    public void addSchoolsBatch(List<School> schools);

    public void addClass(Class classEntity);
    @Transactional
    public void addClassesBatch(List<Class> classes);
    public void addCollege(College college);
    @Transactional
    public void addCollegesBatch(List<College> colleges);
    public void addCourse(Course course);
    @Transactional
    public void addCoursesBatch(List<Course> courses);
    public void addMajor(Major major);
    @Transactional
    public void addMajorsBatch(List<Major> majors);
}
