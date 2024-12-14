package cn.edu.zjut.service;

import cn.edu.zjut.entity.Teacher;
import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.entity.TeacherTeachingClass;

import java.util.List;

public interface TeachingClassService {

    public boolean isMainLecturer(String employeeNumber, Integer teachingClassId);
    // 创建教学班
    void createTeachingClass(TeachingClass teachingClass, String employeeNumber, String role);

    // 删除教学班
    void deleteTeachingClass(Integer teachingClassId,String employeeNumber);

    // 查询某个课程的所有教学班
    List<TeachingClass> findTeachingClassesByCourseId(Integer courseId);

    // 查询某个教师的所有教学班
    List<TeachingClass> findTeachingClassesByEmployeeNumber(String employeeNumber);

    // 关联教学班和教师
    void associateTeachingClassWithTeacher(String employeeNumber, Integer teachingClassId, String role);

    // 解绑教师与教学班
    void disassociateTeachingClassWithTeacher(String employeeNumber, Integer teachingClassId);
    // 按工号或姓名查询教师
    List<Teacher> findTeachersByEmployeeNumberOrName(String employeeNumber, String name);
}
