package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.User;
import cn.edu.zjut.mapper.UserMapper;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void registerUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        try {
            // 调用 Mapper 层插入用户数据
            userMapper.registerUser(user);
        } catch (Exception e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new RuntimeException("Phone number already exists");
            }
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public void bindUserInfo(User user) {
        userMapper.bindUserInfo(user);
    }
}
