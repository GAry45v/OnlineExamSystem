package cn.edu.zjut.controller;

import cn.edu.zjut.entity.User;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.edu.zjut.vo.ResponseResult;
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册接口
    @PostMapping("/register")
    public ResponseResult registerUser(@RequestBody User user) {
        try {
            // 调用 Service 层注册方法
            userService.registerUser(user);

            User uservo=new User();
            uservo.setPhoneNumber(user.getPhoneNumber());

            return ResponseResult.success("User registered successfully", uservo);
        } catch (RuntimeException e) {
            // 返回错误响应
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            // 捕获其他未知异常
            return ResponseResult.error("Registration failed: " + e.getMessage());

        }
    }

    @PostMapping("/bind")
    public String bindUserInfo(@RequestBody User user) {
        userService.bindUserInfo(user);
        return "绑定成功";
    }
}
