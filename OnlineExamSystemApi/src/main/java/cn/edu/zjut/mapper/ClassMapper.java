package cn.edu.zjut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface ClassMapper {
    @Select("SELECT classId FROM Class WHERE name = #{className} AND majorId IN (#{majorIds})")
    List<Integer> findClassIdsByNameAndMajor(@Param("majorIds") List<Integer> majorIds, @Param("className") String className);

}
