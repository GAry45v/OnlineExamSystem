package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.Papers;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper  // 添加此注解
public interface PapersMapper {
    // 创建考试
    @Insert("INSERT INTO Papers (name, description, totalMarks, employeeNumber, questionBankId) " +
            "VALUES (#{name}, #{description}, #{totalMarks}, #{employeeNumber}, #{questionBankId})")  // 添加了 questionBankId
    @Options(useGeneratedKeys = true, keyProperty = "paperId")
    void createPaper(Papers paper);

    // 寻找对应的试卷
    @Select("SELECT * FROM Papers WHERE paperId = #{paperId}")
    Papers findPaperById(@Param("paperId") int paperId);

    // 删除试卷
    @Delete("DELETE FROM Papers WHERE paperId = #{paperId}")
    void deletePaper(@Param("paperId") int paperId);

    // 返回某个老师的所有试卷
    @Select("SELECT * FROM Papers WHERE employeeNumber = #{employeeNumber}")
    List<Papers> findPapersByEmployee(@Param("employeeNumber") String employeeNumber);
}
