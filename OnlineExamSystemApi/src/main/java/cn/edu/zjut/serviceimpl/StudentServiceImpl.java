package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.mapper.StudentMapper;
import cn.edu.zjut.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<Student> findStudentsByStudentNumbers(List<String> studentNumbers) {
        if (studentNumbers == null || studentNumbers.isEmpty()) {
            return new ArrayList<>();
        }
        return studentMapper.findStudentsByStudentNumbers(studentNumbers);
    }

}
