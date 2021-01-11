package com.welfare.persist.entity;

import lombok.Data;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.persist.entity
 * @ClassName: Order
 * @Author: jian.zhou
 * @Description:
 * @Date: 2021/1/9 17:16
 * @Version: 1.0
 */
@Data
public class OrderInfo {

    private String orderId;         //订单id
    private String goods;           //商品
    private String merchantCode;    //商户代码
    private String merchantName;    //商户名称
    private String accountCode;     //账户
    private String accountName;     //账户名称
    private String cardId;          //卡号
    private String storeCode;       //门店code
    private String storeName;       //门店名称
    private String orderAmount;     //订单金额
    private String orderTime;       //订单时间
    private String amount;          //总金额
    private Integer orderNum;        //订单数量

}
