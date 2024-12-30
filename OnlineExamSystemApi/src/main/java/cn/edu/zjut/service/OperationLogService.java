package cn.edu.zjut.service;

import cn.edu.zjut.entity.OperationLog;

import java.util.List;

public interface OperationLogService {

    boolean addOperationLog(OperationLog operationLog);
    List<OperationLog> getAllOperationLogs();
    List<OperationLog> getLogsByUserId(String userId);
}
