package cn.edu.zjut.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OperationLog {
    private Integer logId; // 日志ID
    private String userId; // 用户ID
    private Timestamp operationTime; // 操作时间
    private String operationDescription; // 操作描述
    private String userName; // 用户名

}
