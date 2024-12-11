package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.mapper.StudentTeachingClassMapper;
import cn.edu.zjut.service.StudentTeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;  // 确保导入的是 java.sql.Timestamp
import java.util.List;

@Service
public class StudentTeachingClassServiceImpl implements StudentTeachingClassService {

    @Autowired
    private StudentTeachingClassMapper studentTeachingClassMapper;

    @Override
    public void addStudentToTeachingClass(Integer studentId, Integer teachingClassId) {
        // 生成入学日期
        Timestamp enrollmentDate = new Timestamp(System.currentTimeMillis());
        studentTeachingClassMapper.addStudentToTeachingClass(studentId, teachingClassId, enrollmentDate);
    }

    @Override
    public void addStudentsToTeachingClass(List<Integer> studentIds, Integer teachingClassId) {
        // 生成入学日期
        Timestamp enrollmentDate = new Timestamp(System.currentTimeMillis());
        studentTeachingClassMapper.addStudentsToTeachingClass(studentIds, teachingClassId, enrollmentDate);
    }
}
