package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.User;
import cn.edu.zjut.mapper.UserMapper;
import cn.edu.zjut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void registerUser(User user) {userMapper.registerUser(user);
    }

    @Override
    public void bindUserInfo(User user) {
        userMapper.bindUserInfo(user);
    }
}
