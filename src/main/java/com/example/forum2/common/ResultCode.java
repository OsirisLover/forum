package com.example.forum2.common;

public enum ResultCode {
    SUCCESS (0, "成功"),
    FAILED (1000, "失败"),
    FAILED_UNAUTHORIZED (1001, "未授权"),
    FAILED_PARAMS_VALIDATE (1002, "参数校验失败"),


    FAILED_FORBIDDEN (1003, "禁⽌访问"),
    FAILED_CREATE (1004, "新增失败"),

    FAILED_NOT_EXISTS (1005, "资源不存在"),
    FAILED_UPDATE (1006, "更新失败"),
    FAILED_DELETE (1007, "删除失败"),
    FAILED_USER_EXISTS (1101,"⽤⼾已存在"),
    FAILED_USER_NOT_EXISTS (1102,"⽤⼾不存在"),
    FAILED_USER_BANNED (1103,"您已被禁⾔, 请联系管理员, 并重新登录."),

    FAILED_TWO_PWD_NOT_SAME (1103,"两次输⼊的密码不⼀致"),
    FAILED_LOGIN (1103,"⽤⼾名或密码错误"),

    ERROR_SERVICES (2000, "服务器内部错误"),
    ERROR_IS_NULL (2001, "IS NULL."),

    FAILED_STATE  (1006, "资源状态异常, 请稍候再试."),;


    private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }
    public long getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
