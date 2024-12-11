package cn.edu.zjut.service;

import cn.edu.zjut.entity.User;

public interface UserService {
    // 用户注册
    void registerUser(User user);
    void bindUserInfo(User user);
}
