package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.mapper.StudentTeachingClassMapper;
import cn.edu.zjut.service.StudentService;
import cn.edu.zjut.service.StudentTeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentTeachingClassServiceImpl implements StudentTeachingClassService {

    @Autowired
    private StudentTeachingClassMapper studentTeachingClassMapper;
    @Autowired
    private StudentService studentService;
    @Override
    public void addStudentToTeachingClass(StudentTeachingClass studentTeachingClass) {
        // 确保入班日期非空
        if (studentTeachingClass.getEnrollmentDate() == null) {
            studentTeachingClass.setEnrollmentDate(new Timestamp(System.currentTimeMillis()));
        }

        // 调用 Mapper 添加学生到教学班
        studentTeachingClassMapper.addStudentToTeachingClass(
                studentTeachingClass.getStudentNumber(),
                studentTeachingClass.getTeachingClassId(),
                studentTeachingClass.getEnrollmentDate()
        );
    }

    @Override
    public void addStudentsToTeachingClass(List<StudentTeachingClass> studentTeachingClasses) {
        // 统一设置入班日期，如果未提供
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        studentTeachingClasses.forEach(student -> {
            if (student.getEnrollmentDate() == null) {
                student.setEnrollmentDate(currentTimestamp);
            }
        });

        // 调用 Mapper 批量添加学生
        studentTeachingClassMapper.addStudentsToTeachingClass(
                studentTeachingClasses.stream()
                        .map(StudentTeachingClass::getStudentNumber)
                        .collect(Collectors.toList()), // 使用 Collectors.toList() 替代 toList()
                studentTeachingClasses.get(0).getTeachingClassId(),
                currentTimestamp
        );
    }
    @Override
    public List<Student> findStudentsByTeachingClassId(Integer teachingClassId) {
        // 查询教学班内所有学生的学号
        List<String> studentNumbers = studentTeachingClassMapper.findStudentNumbersByTeachingClassId(teachingClassId);

        // 根据学号列表查询学生信息
        return studentService.findStudentsByStudentNumbers(studentNumbers);
    }
    @Override
    public void deleteStudentFromTeachingClass(String studentNumber, Integer teachingClassId) {
        // 删除单个学生
        studentTeachingClassMapper.deleteStudentFromTeachingClass(studentNumber, teachingClassId);
    }

    @Override
    public void deleteStudentsFromTeachingClass(List<String> studentNumbers, Integer teachingClassId) {
        // 批量删除学生
        studentTeachingClassMapper.deleteStudentsFromTeachingClass(studentNumbers, teachingClassId);
    }
}

