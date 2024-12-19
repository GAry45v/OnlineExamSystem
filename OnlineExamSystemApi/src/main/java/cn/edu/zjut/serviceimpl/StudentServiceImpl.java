package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.College;
import cn.edu.zjut.entity.Student;
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
    public List<Student> findStudentByStudentNumberOrName(String studentNumber, String name) {
        if(studentNumber.equals("空")){
            studentNumber = null;
        }
        if(name.equals("空")){
            name = null;
        }
        return studentMapper.findStudentByStudentNumberOrName(studentNumber, name);
    }
    @Override
    public List<Student> findStudentsByCollegeMajorClass(String collegeName, String majorName, String className) {
        // 通过学院名称获取学院ID
        List<Integer> collegeIds = collegeMapper.findCollegeIdsByName(collegeName);
        // 通过专业名称和学院ID获取专业ID
        List<Integer> majorIds = majorMapper.findMajorIdsByNameAndCollege(collegeIds, majorName);
        // 通过班级名称和专业ID获取班级ID
        List<Integer> classIds = classMapper.findClassIdsByNameAndMajor(majorIds, className);

        // 根据班级ID查询学生
        return studentMapper.findStudentsByClassIds(classIds);
    }



    @Override
    public List<Student> findStudentsByStudentNumbers(List<String> studentNumbers) {
        if (studentNumbers == null || studentNumbers.isEmpty()) {
            return new ArrayList<>();
        }
        return studentMapper.findStudentsByStudentNumbers(studentNumbers);
    }

}
