package com.welfare.persist.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Rongya.huang
 * @date 10:42 2021/3/11
 * @description 用户授信消费查询门店
 **/
@Data
public class EmployeeSettleStoreDTO implements Serializable {
    private String storeCode;
    private String storeName;
}
