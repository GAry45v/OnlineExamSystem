package cn.edu.zjut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface MajorMapper {
    @Select("<script>" +
            "SELECT majorId FROM Major " +
            "WHERE 1=1 " +
            "<if test='collegeIds != null and collegeIds.size() > 0'> AND collegeId IN " +
            "<foreach collection='collegeIds' item='id' open='(' separator=',' close=')'> #{id} </foreach> </if>" +
            "<if test='majorName != null'> AND name = #{majorName} </if>" +
            "</script>")
    List<Integer> findMajorIdsByNameAndCollege(@Param("collegeIds") List<Integer> collegeIds, @Param("majorName") String majorName);

    @Select("<script>" +
            "SELECT majorId FROM Major " +
            "WHERE 1=1 " +
            "<if test='majorName != null'> AND name = #{majorName} </if>" +
            "LIMIT 1" + // 限制返回一条记录
            "</script>")
    Integer findMajorIdByName(@Param("majorName") String majorName);

}
