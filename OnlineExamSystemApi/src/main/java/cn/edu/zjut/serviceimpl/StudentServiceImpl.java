package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.College;
import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentDTO;
import cn.edu.zjut.mapper.ClassMapper;
import cn.edu.zjut.mapper.CollegeMapper;
import cn.edu.zjut.mapper.MajorMapper;
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
    @Autowired
    private CollegeMapper collegeMapper; // 假设有 CollegeMapper 处理学院查询

    @Autowired
    private MajorMapper majorMapper; // 假设有 MajorMapper 处理专业查询

    @Autowired
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

}
