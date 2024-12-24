package cn.edu.zjut.controller;

import cn.edu.zjut.entity.User;
import cn.edu.zjut.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.edu.zjut.vo.ResponseResult;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    private ObjectMapper objectMapper=new ObjectMapper();
    // 用户注册接口
    @PostMapping("/register")
    public ResponseResult<User> registerUser(@RequestBody User user) {
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
    public ResponseResult<User> bindUserInfo(@RequestBody Map<String, Object> requestData) {

        User user = objectMapper.convertValue(requestData.get("user"), User.class);
        String schoolname =(String)requestData.get("schoolname");
        System.out.println(schoolname);
        try {
            // 调用 Service 层的绑定方法
            userService.bindUserInfo(user,schoolname);

            User uservo=new User();
            uservo.setPhoneNumber(user.getPhoneNumber());
            uservo.setUserId(user.getUserId());

            // 返回成功响应
            return ResponseResult.success("User bound successfully", uservo);
        } catch (RuntimeException e) {
            // 返回错误响应
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            // 捕获其他未知异常
            return ResponseResult.error("Binding failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseResult<Map<String, String>> login(@RequestBody User user) {
        try {
            // 调用 Service 层的登录方法
            String result = userService.login(user);

            // 返回 token 给前端
            Map<String, String> response = new HashMap<>();
            response.put("roleId", result.substring(result.length() - 1));
            response.put("token",result.substring(0, result.length() - 1));
            return ResponseResult.success("Login successful", response);
        } catch (RuntimeException e) {
            return ResponseResult.error(e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error("Login failed: " + e.getMessage());
        }
    }
}
