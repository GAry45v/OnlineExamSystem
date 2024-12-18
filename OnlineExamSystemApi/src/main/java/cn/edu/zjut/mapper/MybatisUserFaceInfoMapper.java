package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.UserFaceInfo;
import cn.edu.zjut.face_detect.dto.FaceUserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MybatisUserFaceInfoMapper {

    /**
     * 查询所有用户的面部信息列表
     * @return List<UserFaceInfo>
     */
    @Select("SELECT id, group_id, face_id, phone_number, create_time, face_feature " +
            "FROM user_face_info")
    @Results(id = "userFace", value = {
            @Result(column = "face_id", property = "faceId"),
            @Result(column = "name", property = "name"),
            @Result(column = "face_feature", property = "faceFeature")
    })
    List<UserFaceInfo> findUserFaceInfoList();

    /**
     * 插入新的用户面部信息
     * @param userFaceInfo 用户面部信息对象
     */
    @Insert("INSERT INTO user_face_info (group_id, face_id, name, face_feature) " +
            "VALUES (#{groupId}, #{faceId}, #{name}, #{faceFeature})")
    void insertUserFaceInfo(UserFaceInfo userFaceInfo);

    /**
     * 根据组ID查询用户的面部信息
     * @param faceId 分组ID
     * @return List<FaceUserInfo>
     */
    @Select("SELECT id, group_id, face_id, phone_number, create_time, face_feature " +
            "FROM user_face_info WHERE face_id = #{faceId}")
    @Results(id = "userFace2", value = {
            @Result(column = "face_id", property = "faceId"),
            @Result(column = "name", property = "name"),
            @Result(column = "face_feature", property = "faceFeature")
    })
    List<FaceUserInfo> getUserFaceInfoByfaceId(String  faceId);

}
