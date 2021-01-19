package com.welfare.service.dto.payment;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.ConsumeTypeJson;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@Data
public abstract class PaymentRequest {
    private static transient final String ONLINE_MACHINE_NO = "9002";


    @ApiModelProperty("支付请求id")
    private String requestId;
    @ApiModelProperty("重百付支付流水号")
    private String transNo;
    @ApiModelProperty("门店号")
    private String storeNo;
    @ApiModelProperty("支付机器号")
    private String machineNo;
    @ApiModelProperty("金额")
    private BigDecimal amount = BigDecimal.ZERO;
    @ApiModelProperty("是否离线，用于区分是离线支付还是在线支付")
    private Boolean offline;
    @ApiModelProperty("支付状态，0：新增，1：处理中，2：处理成功，3:已退款，-1：处理失败")
    private Integer paymentStatus;
    @ApiModelProperty("账户号")
    private Long accountCode;
    @ApiModelProperty("账户姓名，返回参数")
    private String accountName;
    @ApiModelProperty("账户余额，返回参数")
    private BigDecimal accountBalance;
    @ApiModelProperty("账户信用额度，返回参数")
    private BigDecimal accountCredit;
    @ApiModelProperty("支付时间")
    private Date paymentDate;
    @ApiModelProperty("退款交易流水号,返回参数")
    private String refundTransNo;

    public String calculatePaymentScene(){
        String consumeType = o2oOrOnlineShopping(machineNo);
        //不是O2O或者ONLINE_SHOPPING,则为到店消费
        return consumeType == null ? ConsumeTypeEnum.SHOP_SHOPPING.getCode() :consumeType;
    }

    /**
     * 是不是O2O或者ONLINE_SHOPPING
     * @param machineNo
     * @return
     */
    private String o2oOrOnlineShopping(String machineNo) {
        SupplierStoreDao supplierStoreDao = SpringBeanUtils.getBean(SupplierStoreDao.class);
        SupplierStore oneByCashierNo = supplierStoreDao.getOneByCashierNo(machineNo);
        if(Objects.isNull(oneByCashierNo)){
            return null;
        }else{
            String consumType = oneByCashierNo.getConsumType();
            ConsumeTypeJson consumeTypeJson = JSON.parseObject(consumType,ConsumeTypeJson.class);
            return consumeTypeJson.getONLINE_MALL()?ConsumeTypeEnum.ONLINE_MALL.getCode():ConsumeTypeEnum.O2O.getCode();
        }
    }


    public Long calculateAccountCode(){
        return getAccountCode();
    }

    public String getCardNo() {
        return null;
    }
}
