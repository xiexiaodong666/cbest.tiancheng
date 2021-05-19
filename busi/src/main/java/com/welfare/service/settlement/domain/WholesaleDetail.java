package com.welfare.service.settlement.domain;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.*;
import com.welfare.persist.entity.*;
import io.swagger.annotations.ApiModelProperty;
import jodd.util.StringUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
@Data
public class WholesaleDetail {
    /**
     * 订单编码
     */
    @ApiModelProperty("订单编码")
    private String orderId;
    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")
    private String transNo;
    /**
     * 账户
     */
    @ApiModelProperty("账户")
    private Long accountCode;
    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    private String accountName;
    /**
     * 卡号
     */
    @ApiModelProperty("卡号")
    private String cardId;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    private String merCode;
    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")
    private String merName;
    /**
     * 门店编码
     */
    @ApiModelProperty("门店编码")
    private String storeCode;
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    private String storeName;
    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")
    private Date transTime;
    /**
     * pos机器编码
     */
    @ApiModelProperty("pos机器编码")
    private String pos;
    /**
     * 支付编码
     */
    @ApiModelProperty("支付编码")
    private String payCode;
    /**
     * 支付名称
     */
    @ApiModelProperty("支付名称")
    private String payName;
    /**
     * 交易类型
     */
    @ApiModelProperty("交易类型")
    private String transType;
    /**
     * 交易类型名
     */
    @ApiModelProperty("交易类型名")
    private String transTypeName;
    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")
    private BigDecimal transAmount;
    /**
     * 福利类型(餐费、交通费等)
     */
    @ApiModelProperty("福利类型(餐费、交通费等)")
    private String merAccountType;
    /**
     * 福利类型(餐费、交通费等)
     */
    @ApiModelProperty("福利类型(餐费、交通费等)")
    private String merAccountTypeName;
    /**
     * 子账户扣款金额
     */
    @ApiModelProperty("子账户扣款金额")
    private BigDecimal accountAmount;
    /**
     * 子账户余额
     */
    @ApiModelProperty("子账户余额")
    private BigDecimal accountBalance;
    /**
     * 商户余额扣款金额
     */
    @ApiModelProperty("商户余额扣款金额")
    private BigDecimal merDeductionAmount;
    /**
     * 商户信用扣款金额
     */
    @ApiModelProperty("商户信用扣款金额")
    private BigDecimal merCreditDeductionAmount;
    /**
     * 自费扣款金额
     */
    @ApiModelProperty("自费扣款金额")
    private BigDecimal selfDeductionAmount;
    @ApiModelProperty("商户批发信用扣款金额")
    private BigDecimal orderWholesaleAmount;
    /**
     * 数据支付类型 welfare-员工卡支付 third-其它三方支付
     */
    @ApiModelProperty("数据支付类型 welfare-员工卡支付 third-其它三方支付")
    private String dataType;
    /**
     * 结算标志 settled已结算 unsettled未结算
     */
    @ApiModelProperty("结算标志 settled已结算 unsettled未结算")
    private String settleFlag;
    /**
     * 订单渠道
     */
    @ApiModelProperty("订单渠道")
    private String orderChannel;
    /**
     * 支付渠道
     */
    @ApiModelProperty("支付渠道")
    private String paymentChannel;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
    /**
     * 返点标志 unrebated未返点 rebated 已返点
     */
    @ApiModelProperty("返点标志 unrebated未返点 rebated 已返点")
    private BigDecimal rebateAmount;
    /**
     * 商户授信额度
     */
    @ApiModelProperty("商户授信额度")
    private BigDecimal merCredit;
    /**
     * 商户余额
     */
    @ApiModelProperty("商户余额")
    private BigDecimal merBalance;

    public static List<WholesaleDetail> fromAccountDeductionDetail(AccountDeductionDetail deductionDetail, List<OrderInfo> orderInfos){
        AccountDao accountDao = SpringBeanUtils.getBean(AccountDao.class);
        MerchantDao merchantDao = SpringBeanUtils.getBean(MerchantDao.class);
        SupplierStoreDao supplierStoreDao = SpringBeanUtils.getBean(SupplierStoreDao.class);
        MerchantAccountTypeDao merchantAccountTypeDao = SpringBeanUtils.getBean(MerchantAccountTypeDao.class);
        Account account = accountDao.queryByAccountCode(deductionDetail.getAccountCode());
        Merchant merchant = merchantDao.queryByCode(account.getMerCode());
        SupplierStore store = supplierStoreDao.getOneByCode(deductionDetail.getStoreCode());
        MerchantAccountType merchantAccountType = merchantAccountTypeDao
                .queryAllByMerCodeAndType(account.getMerCode(), deductionDetail.getMerAccountType());

        return orderInfos.stream().map(orderInfo -> {
            WholesaleDetail wholesaleDetail = new WholesaleDetail();
            wholesaleDetail.setOrderId(orderInfo.getOrderId());
            wholesaleDetail.setAccountCode(account.getAccountCode());
            wholesaleDetail.setAccountName(account.getAccountName());
            wholesaleDetail.setCardId(deductionDetail.getCardId());
            wholesaleDetail.setDataType("e-welfare");
            wholesaleDetail.setMerAccountType(deductionDetail.getMerAccountType());
            wholesaleDetail.setMerAccountTypeName(merchantAccountType.getMerAccountTypeName());
            wholesaleDetail.setMerBalance(BigDecimal.ZERO);
            wholesaleDetail.setMerCode(merchant.getMerCode());
            wholesaleDetail.setMerName(merchant.getMerName());
            wholesaleDetail.setStoreCode(store.getStoreCode());
            wholesaleDetail.setStoreName(store.getStoreName());
            wholesaleDetail.setTransNo(deductionDetail.getTransNo());
            wholesaleDetail.setTransTime(deductionDetail.getTransTime());
            wholesaleDetail.setPos(deductionDetail.getPos());
            wholesaleDetail.setPayCode(deductionDetail.getPayCode());
            wholesaleDetail.setPayName(WelfareConstant.PayCode.parseByCode(deductionDetail.getPayCode()).name());
            wholesaleDetail.setTransType(deductionDetail.getTransType());
            wholesaleDetail.setTransTypeName(WelfareConstant.TransType.parseByCode(deductionDetail.getTransType()).name());
            wholesaleDetail.setTransAmount(orderInfo.getOrderAmount());
            wholesaleDetail.setAccountBalance(deductionDetail.getAccountAmountTypeBalance());
            wholesaleDetail.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code());
            wholesaleDetail.setRebateAmount(BigDecimal.ZERO);
            wholesaleDetail.setOrderWholesaleAmount(orderInfo.getOrderWholesaleAmount());
            wholesaleDetail.setPaymentChannel(deductionDetail.getPaymentChannel());
            return wholesaleDetail;
        }).collect(Collectors.toList());

    }

    public WholesaleReceivableSettleDetail toWholesaleReceivableDetail(){
        WholesaleReceivableSettleDetail wholesaleReceivableSettleDetail = new WholesaleReceivableSettleDetail();
        BeanUtil.copyProperties(this,wholesaleReceivableSettleDetail);
        return wholesaleReceivableSettleDetail;
    }

    public WholesalePayableSettleDetail toWholesalePayableDetail(){
        WholesalePayableSettleDetail wholesalePayableSettleDetail = new WholesalePayableSettleDetail();
        BeanUtil.copyProperties(this,wholesalePayableSettleDetail);
        return wholesalePayableSettleDetail;
    }
}
