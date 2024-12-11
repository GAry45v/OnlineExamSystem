package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.Course;
import cn.edu.zjut.mapper.CourseMapper;
import cn.edu.zjut.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public void createCourse(Course course) {
        courseMapper.createCourse(course);
    }
}
