package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.mapper.TeachingClassMapper;
import cn.edu.zjut.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeachingClassServiceImpl implements TeachingClassService {

    @Autowired
    private TeachingClassMapper teachingClassMapper;

    @Override
    public void createTeachingClass(TeachingClass teachingClass) {
        teachingClassMapper.createTeachingClass(teachingClass);
    } public void enrollStudentInTeachingClass(StudentTeachingClass studentTeachingClass) {
        teachingClassMapper.enrollStudentInTeachingClass(studentTeachingClass);
    }
}
