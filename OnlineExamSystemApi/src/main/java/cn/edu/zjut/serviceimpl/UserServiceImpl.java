package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.User;
import cn.edu.zjut.mapper.UserMapper;
import cn.edu.zjut.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    public void bindUserInfo(User user,String schoolname) {
        // 查询学校 ID 是否存在
        Integer schoolId = userMapper.searchSchoolIdByName(schoolname);
        if (schoolId == null) {
            throw new RuntimeException("School does not exist");
        }

        // 根据角色选择查询 Teacher 或 Student 表
        boolean exists = false;
        if (user.getRoleId() == 1) { // 教师角色
            exists = userMapper.checkTeacherExist(user.getUserNumber(), schoolId);
        } else if (user.getRoleId() == 2) { // 学生角色
            exists = userMapper.checkStudentExist(user.getUserNumber(), schoolId);
        } else {
            throw new RuntimeException("Invalid role");
        }

        // 如果对应角色的信息存在，更新 User 表
        if (exists) {
            user.setSchoolId(schoolId);
            userMapper.bindUserInfo(user);
        } else {
            throw new RuntimeException("User number does not match school ID");
        }
    }

    public String login(User user) {
        // 查询数据库，验证用户名和密码是否匹配
        User dbUser = userMapper.getUserByUserNumber(user.getUserNumber());
        if (dbUser == null || !passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // 生成 JWT Token
        String token = generateToken(dbUser);

        return token;
    }

    // 生成 JWT Token
    private String generateToken(User user) {
        long expirationTime = 1000 * 60 * 60 * 24; // 24小时有效期

        // 获取当前时间
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        // 设置 JWT 的签发者、主题、用户信息、过期时间等
        String token = Jwts.builder()
                .setSubject(user.getUserNumber()) // 设置主题为用户名
                .claim("userNumber", user.getUserNumber())
                .claim("userId", user.getUserId()) // 设置用户ID
                .claim("roleId", user.getRoleId()) // 设置角色ID
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(expirationDate) // 设置过期时间
                .signWith(SignatureAlgorithm.HS512, "javaxcryptospecSecretKeySpecfa779f14javaxcryptospecSecretKeySpecfa779f14javaxcryptospecSecretKeySpecfa779f14") // 签名使用密钥
                .compact();

        return token;
    }

}
