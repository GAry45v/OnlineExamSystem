package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.Course;
import cn.edu.zjut.mapper.CourseMapper;
import cn.edu.zjut.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public void createCourse(Course course) {
        courseMapper.createCourse(course);  // 调用Mapper层的插入方法
    }

    @Override
    public void deleteCourse(Integer courseId) {
        courseMapper.deleteCourse(courseId);  // 调用Mapper层的删除方法
    }
    @Override
    public List<Course> getCoursesByTeacherId(Integer teacherId) {
        return courseMapper.findCoursesByTeacherId(teacherId);
    }
}
