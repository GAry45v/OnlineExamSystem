package cn.edu.zjut.controller;

import cn.edu.zjut.entity.TeachingClass;
import cn.edu.zjut.service.TeachingClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teaching-class")
public class TeachingClassController {

    @Autowired
    private TeachingClassService teachingClassService;

    // 教师创建教学班
    @PostMapping("/create")
    public ResponseEntity<?> createTeachingClass(@RequestBody TeachingClass teachingClass) {
        teachingClassService.createTeachingClass(teachingClass);
        return ResponseEntity.ok("教学班创建成功");
    }
}
