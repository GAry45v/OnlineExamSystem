package cn.edu.zjut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface CollegeMapper {
    @Select("SELECT collegeId FROM College WHERE name = #{collegeName}")
    List<Integer> findCollegeIdsByName(@Param("collegeName") String collegeName);

}
