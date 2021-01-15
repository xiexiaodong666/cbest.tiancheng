package com.welfare.common.exception;

public class BusiException extends RuntimeException{

    private ExceptionCode code;
    private String msg;  //异常信息

    public BusiException(ExceptionCode code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }
    public BusiException(String msg) {
        super(msg);
        this.code = ExceptionCode.UNKNOWON_EXCEPTION;
        this.msg = msg;
    }


    public String getCode() {
        return code.getCode();
    }

    public ExceptionCode getCodeEnum(){
        return code;
    }
}
