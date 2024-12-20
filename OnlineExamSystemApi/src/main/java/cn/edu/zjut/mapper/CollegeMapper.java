package cn.edu.zjut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface CollegeMapper {
    @Select("<script>" +
            "SELECT collegeId FROM College " +
            "WHERE 1=1 " +
            "<if test='collegeName != null'> AND name = #{collegeName} </if>" +
            "</script>")
    List<Integer> findCollegeIdsByName(@Param("collegeName") String collegeName);

    @Select("<script>" +
            "SELECT collegeId FROM College " +
            "WHERE 1=1 " +
            "<if test='collegeName != null'> AND name = #{collegeName} </if>" +
            "LIMIT 1" + // 限制只返回一条记录
            "</script>")
    Integer findCollegeIdByName(@Param("collegeName") String collegeName);

}
