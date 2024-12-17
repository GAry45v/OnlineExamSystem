package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.Course;
import cn.edu.zjut.entity.TeacherCourse;
import cn.edu.zjut.mapper.CourseMapper;
import cn.edu.zjut.mapper.TeachingClassMapper;
import cn.edu.zjut.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TeachingClassMapper teachingClassMapper;

    @Override
    public void createCourse(Course course, String employeeNumber) {
        // 验证创建人身份
        course.setCreatedByEmployeeNumber(employeeNumber);

        // 保存课程记录
        courseMapper.createCourse(course);

        // 自动添加 TeacherCourse 记录
        TeacherCourse teacherCourse = new TeacherCourse();
        teacherCourse.setEmployeeNumber(employeeNumber);
        teacherCourse.setCourseId(course.getCourseId());
        courseMapper.addTeacherToCourse(teacherCourse);
    }

    @Override
    public void deleteCourse(Integer courseId, String employeeNumber) {
        System.out.println(employeeNumber);
        // 验证课程创建人
        List<Course> courses = courseMapper.findCoursesByEmployeeNumber(employeeNumber);
        boolean isCreator = courses.stream()
                .anyMatch(course -> Integer.valueOf(course.getCourseId()).equals(courseId));
        if (!isCreator) {
            throw new IllegalArgumentException("当前用户无权删除课程，课程ID：" + courseId);
        }

        // 删除课程并级联删除关联记录
        courseMapper.deleteFromTeacherCourse(courseId);
        courseMapper.deleteFromCourse(courseId);
        //删除课程相关教学班
        teachingClassMapper.deleteTeacherTeachingClassByCourseId(courseId);
        teachingClassMapper.deleteTeachingClassByCourseId(courseId);
    }

    @Override
    public List<Course> getCoursesByEmployeeNumber(String employeeNumber) {
        return courseMapper.findCoursesByEmployeeNumber(employeeNumber);
    }

    @Override
    public void associateTeacherWithCourse(TeacherCourse teacherCourse, String createdByEmployeeNumber) {

        courseMapper.addTeacherToCourse(teacherCourse);
    }

    @Override
    public void disassociateTeacherFromCourse(TeacherCourse teacherCourse, String createdByEmployeeNumber) {
        // 验证是否为课程创建人
        if (!createdByEmployeeNumber.equals(teacherCourse.getEmployeeNumber())) {
            throw new IllegalArgumentException("只有课程创建人可以解绑教师，课程ID：" + teacherCourse.getCourseId());
        }

        // 删除关联
        courseMapper.removeTeacherFromCourse(teacherCourse);
    }

    @Override
    public List<String> findTeachersByCourseId(Integer courseId) {
        return courseMapper.findTeachersByCourseId(courseId);
    }
}
