package cn.edu.zjut.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ResponseResult<T> implements Serializable {
    private int code;               // 状态码
    private String message;         // 返回消息
    private T data;                 // 返回数据
    private LocalDateTime timestamp; // 时间戳

    // 私有构造函数
    private ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // 成功响应静态方法
    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(200, "Success", data);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(200, message, data);
    }

    // 错误响应静态方法
    public static <T> ResponseResult<T> error(int code, String message) {
        return new ResponseResult<>(code, message, null);
    }

    public static <T> ResponseResult<T> error(String message) {
        return new ResponseResult<>(500, message, null);
    }

    // Getters 和 Setters
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
