package com.welfare.common.exception;

/**
 * 业务异常
 * @author liyx1
 */
public class BizException extends RuntimeException{

    private ExceptionCode code;
    private String msg;

    public BizException(ExceptionCode code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }
    public BizException(String msg) {
        super(msg);
        this.code = ExceptionCode.UNKNOWON_EXCEPTION;
        this.msg = msg;
    }

    public BizException(ExceptionCode exceptionCode,String msg) {
        super(msg);
        this.code = exceptionCode;
        this.msg = msg;
    }


    public String getCode() {
        return String.valueOf(code.getCode());
    }

    public ExceptionCode getCodeEnum(){
        return code;
    }

    public BizException(ExceptionCode exceptionCode){
        super(exceptionCode.getMsg());
        this.code = exceptionCode;
        this.msg = exceptionCode.getMsg();
    }
}
