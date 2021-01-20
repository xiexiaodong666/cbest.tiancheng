package com.welfare.persist.dto.query;

import lombok.Data;

import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/17 11:46 上午
 * @desc
 */
@Data
public class MerTransDetailQuery {
    private String merCode;

    private String inOrOutType;

    private String transType;

    private Date startTime;

    private Date endTime;
}
