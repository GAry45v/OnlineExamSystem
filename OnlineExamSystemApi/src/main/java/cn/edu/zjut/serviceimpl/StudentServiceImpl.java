package cn.edu.zjut.service.impl;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.mapper.StudentMapper;
import cn.edu.zjut.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> findStudentByStudentNumberOrName(String studentNumber, String name) {
        return studentMapper.findStudentByStudentNumberOrName(studentNumber, name);
    }

    @Override
    public List<Student> findStudentsByCollegeMajorClass(Integer collegeId, Integer majorId, Integer classId) {
        return studentMapper.findStudentsByCollegeMajorClass(collegeId, majorId, classId);
    }
}
