package cn.edu.zjut.controller;

import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teaching-class")
public class TeachingClassController {

    @Autowired
    private TeachingClassService teachingClassService;

    // 创建教学班，并将教师与教学班关联
    @PostMapping("/create")
    public String createTeachingClass(@RequestBody TeachingClass teachingClass) {
        String employeeNumber = "1"; // 假设从登录的教师信息中获得教师ID
        String role = "主讲";  // 假设角色为 "主讲"
        teachingClassService.createTeachingClass(teachingClass, employeeNumber, role);
        return "教学班和教师关联成功";
    }

    // 删除教学班
    @DeleteMapping("/delete/{teachingClassId}")
    public String deleteTeachingClass(@PathVariable Integer teachingClassId) {
        teachingClassService.deleteTeachingClass(teachingClassId);
        return "教学班删除成功";
    }

    // 查询某个课程所有教学班
    @GetMapping("/course/{courseId}")
    public List<TeachingClass> getTeachingClassesByCourseId(@PathVariable Integer courseId) {
        return teachingClassService.findTeachingClassesByCourseId(courseId);
    }

    // 查询某个教师的所有教学班
    @GetMapping("/teacher/{teacherId}")
    public List<TeachingClass> getTeachingClassesByEmployeeNumber(@PathVariable String employeeNumber) {
        return teachingClassService.findTeachingClassesByEmployeeNumber(employeeNumber);
    }

    // 关联教学班和教师
    @PostMapping("/associate")
    public String associateTeachingClassWithTeacher(@RequestParam String employeeNumber,
                                                    @RequestParam Integer teachingClassId,
                                                    @RequestParam String role) {
        teachingClassService.associateTeachingClassWithTeacher(employeeNumber, teachingClassId, role);
        return "教学班和教师关联成功";
    }

    // 解绑教师和教学班
    @DeleteMapping("/disassociate")
    public String disassociateTeachingClassWithTeacher(@RequestParam String employeeNumber,
                                                       @RequestParam Integer teachingClassId) {
        teachingClassService.disassociateTeachingClassWithTeacher(employeeNumber, teachingClassId);
        return "教学班和教师解绑成功";
    }
}
