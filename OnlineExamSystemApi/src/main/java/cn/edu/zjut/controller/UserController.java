package cn.edu.zjut.controller;

import cn.edu.zjut.entity.User;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册接口
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // 可以在这里添加更多的验证逻辑（如手机号格式、密码复杂度等）
        userService.registerUser(user);
        return "注册成功";
    }
    @PostMapping("/bind")
    public String bindUserInfo(@RequestBody User user) {
        userService.bindUserInfo(user);
        return "绑定成功";
    }
}
