package com.welfare.persist.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/17 11:46 上午
 * @desc
 */
@Data
public class MerTransDetailDTO {
    private String transNo;

    private Date transTime;

    private String inOrOutType;

    private String transType;

    private BigDecimal inOrOutAmount;

    private BigDecimal allAmount;
}
