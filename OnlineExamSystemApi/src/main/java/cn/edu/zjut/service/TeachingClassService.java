package cn.edu.zjut.service;

import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.entity.TeacherTeachingClass;

import java.util.List;

public interface TeachingClassService {

    // 创建教学班
    void createTeachingClass(TeachingClass teachingClass, Integer teacherId, String role);

    // 删除教学班
    void deleteTeachingClass(Integer teachingClassId);

    // 查询某个课程的所有教学班
    List<TeachingClass> findTeachingClassesByCourseId(Integer courseId);

    // 查询某个教师的所有教学班
    List<TeachingClass> findTeachingClassesByTeacherId(Integer teacherId);

    // 关联教学班和教师
    void associateTeachingClassWithTeacher(Integer teacherId, Integer teachingClassId, String role);

    // 解绑教师与教学班
    void disassociateTeachingClassWithTeacher(Integer teacherId, Integer teachingClassId);
}
