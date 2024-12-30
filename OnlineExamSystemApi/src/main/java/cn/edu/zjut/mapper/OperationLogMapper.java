package cn.edu.zjut.mapper;

import cn.edu.zjut.entity.OperationLog;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OperationLogMapper {

    // 插入日志
    @Insert("<script>" +
            "INSERT INTO OperationLog (userId, userName, operationDescription) " +
            "SELECT #{userId}, " +
            "       CASE " +
            "           WHEN roleId = 1 THEN (SELECT name FROM Teacher WHERE employeeNumber = userNumber) " +
            "           WHEN roleId = 2 THEN (SELECT name FROM Student WHERE studentNumber = userNumber) " +
            "           ELSE NULL " +
            "       END AS userName, " +
            "       #{operationDescription} " +
            "FROM User " +
            "WHERE userNumber = #{userId}" +
            "</script>")
    int insertOperationLog(@Param("userId") String userId, @Param("operationDescription") String operationDescription);

    // 查询所有日志
    @Select("SELECT * FROM OperationLog")
    List<OperationLog> selectAllOperationLogs();

    // 根据用户ID查询日志
    @Select("SELECT * FROM OperationLog WHERE userId = #{userId}")
    List<OperationLog> selectLogsByUserId(@Param("userId") String userId);


}
