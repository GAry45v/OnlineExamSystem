package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.data.mongodb.repository.Query;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO User (phoneNumber, password) VALUES (#{phoneNumber}, #{password})")
    void registerUser(User user);

    // 查询学校 ID 根据学校名称
    @Select("SELECT schoolId FROM School WHERE name = #{schoolName}")
    Integer searchSchoolIdByName(String schoolName);

    // 查询教师是否存在
    @Select("SELECT COUNT(1) > 0 FROM Teacher WHERE employeeNumber = #{userNumber} AND schoolId = #{schoolId}")
    boolean checkTeacherExist(String userNumber, int schoolId);

    // 查询学生是否存在
    @Select("SELECT COUNT(1) > 0 FROM Student WHERE studentNumber = #{userNumber} AND schoolId = #{schoolId}")
    boolean checkStudentExist(String userNumber, int schoolId);

    // 更新用户信息
    @Update("UPDATE User SET schoolId = #{schoolId}, userNumber = #{userNumber}, roleId = #{roleId} WHERE phoneNumber = #{phoneNumber}")
    void bindUserInfo(User user);

        // 根据用户的 userNumber 查询用户信息
    @Select("SELECT * FROM User WHERE phoneNumber = #{phonenumber}")
    User getUserByphonenumber(String phonenumber);

}
