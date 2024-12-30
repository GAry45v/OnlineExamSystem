package cn.edu.zjut.util;

import cn.edu.zjut.config.JwtAuthenticationToken;
import cn.edu.zjut.entity.OperationLog;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class OperationLogUtil {

    /**
     * 封装 OperationLog 对象
     *
     * @param operationDescription 操作描述
     * @return 封装好的 OperationLog 对象
     */
    public static OperationLog createOperationLog(String operationDescription) {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String UserNumber = authentication.getUserNumber();

        // 创建并设置 OperationLog 对象
        OperationLog operationLog = new OperationLog();
        operationLog.setUserId(UserNumber);
        operationLog.setOperationDescription(operationDescription);

        return operationLog;
    }
}
