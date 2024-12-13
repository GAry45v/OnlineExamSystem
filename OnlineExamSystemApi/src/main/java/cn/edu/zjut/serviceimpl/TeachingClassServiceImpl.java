package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.entity.TeacherTeachingClass;
import cn.edu.zjut.mapper.TeachingClassMapper;
import cn.edu.zjut.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeachingClassServiceImpl implements TeachingClassService {

    @Autowired
    private TeachingClassMapper teachingClassMapper;

    @Override
    public void createTeachingClass(TeachingClass teachingClass, String employeeNumber, String role) {
        // 创建教学班
        teachingClassMapper.createTeachingClass(teachingClass);

        // 关联教学班和教师
        TeacherTeachingClass teacherTeachingClass = new TeacherTeachingClass();
        teacherTeachingClass.setEmployeeNumber(employeeNumber);
        teacherTeachingClass.setTeachingClassId(teachingClass.getTeachingClassId());
        teacherTeachingClass.setRole(role);
        teachingClassMapper.createTeacherTeachingClass(teacherTeachingClass);
    }

    @Override
    public void deleteTeachingClass(Integer teachingClassId) {
        // 删除教师与教学班的关联
        teachingClassMapper.deleteTeacherTeachingClass(null, teachingClassId); // 删除所有关联的教师

        // 删除教学班
        teachingClassMapper.deleteTeachingClass(teachingClassId);
    }

    @Override
    public List<TeachingClass> findTeachingClassesByCourseId(Integer courseId) {
        return teachingClassMapper.findByCourseId(courseId);
    }

    @Override
    public List<TeachingClass> findTeachingClassesByEmployeeNumber(String employeeNumber) {
        return teachingClassMapper.findByEmployeeNumber(employeeNumber);
    }

    @Override
    public void associateTeachingClassWithTeacher(String employeeNumber, Integer teachingClassId, String role) {
        // 关联教师和教学班
        TeacherTeachingClass teacherTeachingClass = new TeacherTeachingClass();
        teacherTeachingClass.setEmployeeNumber(employeeNumber);
        teacherTeachingClass.setTeachingClassId(teachingClassId);
        teacherTeachingClass.setRole(role);
        teachingClassMapper.createTeacherTeachingClass(teacherTeachingClass);
    }

    @Override
    public void disassociateTeachingClassWithTeacher(String employeeNumber, Integer teachingClassId) {
        // 解除教师与教学班的关联
        teachingClassMapper.deleteTeacherTeachingClass(employeeNumber, teachingClassId);
    }
}
