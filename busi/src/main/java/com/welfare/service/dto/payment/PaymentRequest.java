package com.welfare.service.dto.payment;

import com.alibaba.fastjson.JSON;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.SupplierStoreStatusEnum;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.ConsumeTypeJson;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
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


    @ApiModelProperty(value = "支付请求id",required = true)
    private String requestId;
    @ApiModelProperty(value = "重百付支付流水号",required = true)
    private String transNo;
    @ApiModelProperty(value = "门店号",required = true)
    private String storeNo;
    @ApiModelProperty(value = "支付机器号",required = true)
    private String machineNo;
    @ApiModelProperty(value = "金额",required = true)
    private BigDecimal amount = BigDecimal.ZERO;
    @ApiModelProperty(value = "是否离线，用于区分是离线支付还是在线支付",required = true)
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
    @ApiModelProperty(value = "支付时间",required = true)
    private Date paymentDate;
    @ApiModelProperty("退款交易流水号,返回参数")
    private String refundTransNo;
    @ApiModelProperty("电话号码")
    private String phone;
    private String paymentScene;

    public String calculatePaymentScene(){
        String consumeType = o2oOrOnlineShopping(machineNo);
        //不是O2O或者ONLINE_SHOPPING,则为到店消费
        this.paymentScene =  consumeType == null ? ConsumeTypeEnum.SHOP_SHOPPING.getCode() :consumeType;
        return paymentScene;
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
            Assert.notNull(consumType,"门店没有配置消费场景");
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
