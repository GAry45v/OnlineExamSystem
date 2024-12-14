package cn.edu.zjut.controller;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.service.TeachingClassService;
import cn.edu.zjut.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teaching-class")
public class TeachingClassController {

    @Autowired
    private TeachingClassService teachingClassService;

    // 创建教学班，并将教师与教学班关联
    @PostMapping("/create_teaching-class")
    public ResponseResult<String> createTeachingClass(@RequestBody TeachingClass teachingClass) {
        try {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            String role = "主讲";  // 假设角色为 "主讲"
            teachingClassService.createTeachingClass(teachingClass, employeeNumber, role);
            return ResponseResult.success("教学班和教师关联成功");
        } catch (Exception e) {
            return ResponseResult.error("创建教学班失败: " + e.getMessage());
        }
    }

    // 删除教学班
    @DeleteMapping("/delete/{teachingClassId}")
    public ResponseResult<String> deleteTeachingClass(@PathVariable Integer teachingClassId) {
        try {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            teachingClassService.deleteTeachingClass(teachingClassId,employeeNumber);
            return ResponseResult.success("教学班删除成功");
        } catch (Exception e) {
            return ResponseResult.error("删除教学班失败: " + e.getMessage());
        }
    }

    // 查询某个课程所有教学班
    @GetMapping("/course/search_teaching-classby{courseId}")
    public ResponseResult<List<TeachingClass>> getTeachingClassesByCourseId(@PathVariable Integer courseId) {
        try {
            List<TeachingClass> teachingClasses = teachingClassService.findTeachingClassesByCourseId(courseId);
            return ResponseResult.success(teachingClasses);
        } catch (Exception e) {
            return ResponseResult.error("查询课程的教学班失败: " + e.getMessage());
        }
    }

    // 查询某个教师的所有教学班
    @GetMapping("/teacher/search_teaching-classbyemployeeNumber")
    public ResponseResult<List<TeachingClass>> getTeachingClassesByEmployeeNumber() {
        try {
            JwtAuthenticationToken authentication =
                    (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();

            List<TeachingClass> teachingClasses = teachingClassService.findTeachingClassesByEmployeeNumber(employeeNumber);
            return ResponseResult.success(teachingClasses);
        } catch (Exception e) {
            return ResponseResult.error("查询教学班失败: " + e.getMessage());
        }
    }

    // 关联教学班和教师
    @PostMapping("/associate_teaching-class")
    public ResponseResult<String> associateTeachingClassWithTeacher(@RequestParam Integer teachingClassId,
                                                                    @RequestParam String role) {
        try {
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            teachingClassService.associateTeachingClassWithTeacher(employeeNumber, teachingClassId, role);
            return ResponseResult.success("教学班和教师关联成功");
        } catch (Exception e) {
            return ResponseResult.error("教学班与教师关联失败: " + e.getMessage());
        }
    }

    // 解绑教师和教学班
    @DeleteMapping("/disassociate_teaching-class")
    public ResponseResult<String> disassociateTeachingClassWithTeacher(@RequestParam Integer teachingClassId) {
        try {
            JwtAuthenticationToken authentication =
                    (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            String employeeNumber = authentication.getUserNumber();
            teachingClassService.disassociateTeachingClassWithTeacher(employeeNumber, teachingClassId);
            return ResponseResult.success("教学班和教师解绑成功");
        } catch (Exception e) {
            return ResponseResult.error("解绑教学班和教师失败: " + e.getMessage());
        }
    }
}
