package cn.edu.zjut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface MajorMapper {
    @Select("SELECT majorId FROM Major WHERE name = #{majorName} AND collegeId IN (#{collegeIds})")
    List<Integer> findMajorIdsByNameAndCollege(@Param("collegeIds") List<Integer> collegeIds, @Param("majorName") String majorName);

}
