package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO User (phoneNumber, password, schoolId, userNumber, roleId) " +
            "VALUES (#{phoneNumber}, #{password}, #{schoolId}, #{userNumber}, #{roleId})")
    void registerUser(User user);
    @Update("UPDATE User SET schoolId = #{schoolId}, userNumber = #{userNumber}, roleId = #{roleId} WHERE userId = #{userId}")
    void bindUserInfo(User user);
}
