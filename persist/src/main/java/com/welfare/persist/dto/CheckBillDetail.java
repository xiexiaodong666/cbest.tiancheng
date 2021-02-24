package com.welfare.persist.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/23/2021
 */
@Data
public class CheckBillDetail {
    private String storeCode;
    private String merCode;
    private String merName;
    private Long accountCode;
    private String phone;
    private String transNo;
    private String relatedTransNo;
    private Date transTime;
    private String transType;
    private BigDecimal transAmount;
}
