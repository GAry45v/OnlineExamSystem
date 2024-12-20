package cn.edu.zjut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface ClassMapper {
    @Select("<script>" +
            "SELECT classId FROM Class " +
            "WHERE 1=1 " +
            "<if test='majorIds != null and majorIds.size() > 0'> AND majorId IN " +
            "<foreach collection='majorIds' item='id' open='(' separator=',' close=')'> #{id} </foreach> </if>" +
            "<if test='className != null'> AND name = #{className} </if>" +
            "</script>")
    List<Integer> findClassIdsByNameAndMajor(@Param("majorIds") List<Integer> majorIds, @Param("className") String className);

    @Select("<script>" +
            "SELECT classId FROM Class " +
            "WHERE 1=1 " +
            "<if test='className != null'> AND name = #{className} </if>" +
            "LIMIT 1" + // 限制只返回一条记录
            "</script>")
    Integer findClassIdByName(@Param("className") String className);

}
