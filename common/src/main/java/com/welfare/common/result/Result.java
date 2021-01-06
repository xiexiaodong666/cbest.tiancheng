package com.welfare.common.result;

import com.welfare.common.exception.ExceptionCode;
import org.springframework.data.domain.PageImpl;

import java.io.Serializable;

/**
 * 统一的接口数据返回对象
 */
public class Result<T> implements Serializable {

    private ExceptionCode code; //结果状态
    private PageImpl page;

    public Result(ExceptionCode code, PageImpl page){
        this.code = code;
        this.page = page;
    }

    public ExceptionCode getCode() {
        return code;
    }

    public void setCode(ExceptionCode code) {
        this.code = code;
    }

    public PageImpl getPage() {
        return page;
    }

    public void setPage(PageImpl page) {
        this.page = page;
    }
}
