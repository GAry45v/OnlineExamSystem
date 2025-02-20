package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.OperationLog;
import cn.edu.zjut.entity.Teacher;
import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.entity.TeacherTeachingClass;
import cn.edu.zjut.mapper.TeachingClassMapper;
import cn.edu.zjut.service.OperationLogService;
import cn.edu.zjut.service.TeachingClassService;
import cn.edu.zjut.util.OperationLogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeachingClassServiceImpl implements TeachingClassService {

    @Autowired
    private TeachingClassMapper teachingClassMapper;
    @Autowired
    private OperationLogService operationLogService;
    @Override
    public boolean isMainLecturer(String employeeNumber, Integer teachingClassId) {
        int count = teachingClassMapper.countMainLecturer(employeeNumber, teachingClassId);
        return count > 0;
    }

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
        OperationLog log = OperationLogUtil.createOperationLog("教师创建教学班"+teachingClass.getClassName());
        operationLogService.addOperationLog(log);
    }

    @Override
    public void deleteTeachingClass(Integer teachingClassId,String employeeNumber) {
        // 删除教师与教学班的关联
        teachingClassMapper.deleteTeacherTeachingClass_all(teachingClassId); // 删除所有关联的教师

        // 删除教学班
        teachingClassMapper.deleteTeachingClass(teachingClassId);
        OperationLog log = OperationLogUtil.createOperationLog("教师创建教学班:"+teachingClassId);
        operationLogService.addOperationLog(log);
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
    @Override
    public List<Teacher> findTeachersByEmployeeNumberOrName(String employeeNumber, String name) {
        // 调用 Mapper 层方法查询教师信息
        return teachingClassMapper.findTeacherByEmployeeNumberOrName(employeeNumber, name);
    }
    @Override
    public List<Teacher> findTeachersByTeachingClassId(Integer teachingClassId) {
        // 调用 Mapper 层方法查询教师信息
        return teachingClassMapper.findTeachersByTeachingClassId(teachingClassId);
    }
}
