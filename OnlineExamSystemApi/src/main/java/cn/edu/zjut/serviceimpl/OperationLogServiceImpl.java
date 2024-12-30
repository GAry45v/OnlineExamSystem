package cn.edu.zjut.serviceimpl;

import cn.edu.zjut.entity.OperationLog;
import cn.edu.zjut.mapper.OperationLogMapper;
import cn.edu.zjut.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Override
    public boolean addOperationLog(OperationLog operationLog) {
        return operationLogMapper.insertOperationLog(operationLog.getUserId(),operationLog.getOperationDescription()) > 0;
    }

    @Override
    public List<OperationLog> getAllOperationLogs() {
        return operationLogMapper.selectAllOperationLogs();
    }

    @Override
    public List<OperationLog> getLogsByUserId(String userId) {
        return operationLogMapper.selectLogsByUserId(userId);
    }
}
