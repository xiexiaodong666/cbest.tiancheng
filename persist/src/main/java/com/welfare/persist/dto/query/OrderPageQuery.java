package com.welfare.persist.dto.query;

import lombok.Data;

import java.util.List;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.persist.dto.query
 * @ClassName: OrderPageQuery
 * @Author: jian.zhou
 * @Description: 订单查询参数
 * @Date: 2021/1/10 15:00
 * @Version: 1.0
 */
@Data
public class OrderPageQuery {
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 最低价格
     */
    private String lowPrice;
    /**
     * 最高价格
     */
    private String highPrice;
    /**
     * 商户编码
     */
    private String merchantCode;
    /**
     * 
     */
    private String supplierMerCode;
    /**
     * 订单开始时间
     */
    private String startDateTime;
    /**
     * 订单结束时间
     */
    private String endDateTime;
    /**
     * 无返利门店列表
     */
    private List<String> noRebateStoreList;
    /**
     * 卡支付方式返利门店列表
     */
    private List<String> cardRebateStoreList;
    /**
     * 其他支付方式返利门店列表
     */
    private List<String> otherRebateStoreList;
    /**
     * 员工卡支付和其他支付方式返利门店列表
     */
    private List<String> allRebateStoreList;

    private List<String> storeList;
    /**
     * 页码
     */
    private Integer pageNo;
    /**
     * 每页显示数量
     */
    private Integer pageSize;

    private Integer timeInterval;

    private String accountType;

    private String organizationCode;

}
