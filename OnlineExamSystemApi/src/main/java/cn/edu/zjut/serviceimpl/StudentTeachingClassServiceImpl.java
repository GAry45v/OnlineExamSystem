package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.Student;
import cn.edu.zjut.entity.StudentTeachingClass;
import cn.edu.zjut.entity.StudentTeachingClassDTO;
import cn.edu.zjut.mapper.StudentTeachingClassMapper;
import cn.edu.zjut.service.StudentService;
import cn.edu.zjut.service.StudentTeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StudentTeachingClassServiceImpl implements StudentTeachingClassService {

    @Autowired
    private StudentTeachingClassMapper studentTeachingClassMapper;
    @Autowired
    private StudentService studentService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
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
        String redisKey = "teachingClass:students:" + studentTeachingClass.getTeachingClassId();
        redisTemplate.delete(redisKey);
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
        String redisKey = "teachingClass:students:" + studentTeachingClasses.get(0).getTeachingClassId();
        redisTemplate.delete(redisKey);
    }
    @Override
    public List<Student> findStudentsByTeachingClassId(Integer teachingClassId) {
        // 查询教学班内所有学生的学号
        List<String> studentNumbers = studentTeachingClassMapper.findStudentNumbersByTeachingClassId(teachingClassId);
        // 根据学号列表查询学生信息
        return studentService.findStudentsByStudentNumbers(studentNumbers);
    }
    //查询的是详细信息
    @Override
    public List<StudentTeachingClassDTO> findStudentsInTeachingClass(Integer teachingClassId) {
        String redisKey = "teachingClass:students:" + teachingClassId;
        List<StudentTeachingClassDTO> cachedStudents =
                (List<StudentTeachingClassDTO>) redisTemplate.opsForValue().get(redisKey);
        if (cachedStudents != null) {
            System.out.println("Cache hit for teaching class: " + teachingClassId);
            return cachedStudents;
        }
        System.out.println("Cache miss! Fetching from DB for teaching class: " + teachingClassId);
        List<StudentTeachingClassDTO> students = studentTeachingClassMapper.findStudentsInTeachingClass(teachingClassId);
        redisTemplate.opsForValue().set(redisKey, students, 1, TimeUnit.HOURS);

        return students;
    }

    @Override
    public void deleteStudentFromTeachingClass(String studentNumber, Integer teachingClassId) {
        // 删除单个学生
        studentTeachingClassMapper.deleteStudentFromTeachingClass(studentNumber, teachingClassId);
        String redisKey = "teachingClass:students:" + teachingClassId;
        redisTemplate.delete(redisKey);
    }

    @Override
    public void deleteStudentsFromTeachingClass(List<String> studentNumbers, Integer teachingClassId) {
        // 批量删除学生
        studentTeachingClassMapper.deleteStudentsFromTeachingClass(studentNumbers, teachingClassId);
        String redisKey = "teachingClass:students:" + teachingClassId;
        redisTemplate.delete(redisKey);
    }
    @Override
    public List<String> getStudentNumbersByTeachingClassId(int teachingClassId) {
        return studentTeachingClassMapper.findStudentNumbersByTeachingClassId(teachingClassId);
    }

}

