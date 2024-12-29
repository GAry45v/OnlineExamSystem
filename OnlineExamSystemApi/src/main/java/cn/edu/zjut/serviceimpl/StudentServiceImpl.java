package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.DTO.StudentDTO;
import cn.edu.zjut.mapper.*;
import cn.edu.zjut.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CollegeMapper collegeMapper; // 假设有 CollegeMapper 处理学院查询

    @Autowired
    private MajorMapper majorMapper; // 假设有 MajorMapper 处理专业查询

    @Autowired
    private StudentTeachingClassMapper studentTeachingClassMapper;

    private ClassMapper classMapper; // 假设有 ClassMapper 处理班级查询
    @Override
    public List<StudentDTO> findStudentByStudentNumberOrName(String studentNumber, String name) {
        if(studentNumber.equals("空")){
            studentNumber = null;
        }
        if(name.equals("空")){
            name = null;
        }
        return studentMapper.findStudentsWithDetails(studentNumber, name, null, null, null);
    }
    @Override
    public List<StudentDTO> findStudentsByCollegeMajorClass(String collegeName, String majorName, String className) {
        // 提前声明变量
        if(collegeName.equals("空")){
            collegeName = null;
        }
        if(majorName.equals("空")){
            majorName = null;
        }
        if(className.equals("空")){
            className = null;
        }
        // 使用提前声明的变量
        return studentMapper.findStudentsWithDetails(null, null, collegeName, majorName, className);
    }




    @Override
    public List<Student> findStudentsByStudentNumbers(List<String> studentNumbers) {
        if (studentNumbers == null || studentNumbers.isEmpty()) {
            return new ArrayList<>();
        }
        return studentMapper.findStudentsByStudentNumbers(studentNumbers);
    }

    @Override
    public StudentDTO getStudentByStudentNumber(String studentNumber) {
        return studentMapper.findStudentByStudentNumber(studentNumber);
    }
    @Override
    public List<StudentDTO> getStudentsByTeachingClassId(Integer teachingClassId) {
        // 1. 获取该教学班的所有学生学号
        List<String> studentNumbers = studentTeachingClassMapper.findStudentNumbersByTeachingClassId(teachingClassId);

        // 2. 根据学号查询学生的详细信息
        List<StudentDTO> studentDTOs = studentNumbers.stream()
                .map(studentNumber -> studentMapper.findStudentByStudentNumber(studentNumber))
                .collect(Collectors.toList());

        return studentDTOs;
    }
}
