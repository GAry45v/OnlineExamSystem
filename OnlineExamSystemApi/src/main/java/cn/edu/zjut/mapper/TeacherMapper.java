package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper {
    @Select("SELECT * FROM Teacher WHERE employeeNumber = #{employeeNumber}")
    Teacher findTeacherByEmployeeNumber(String employeeNumber);
}
