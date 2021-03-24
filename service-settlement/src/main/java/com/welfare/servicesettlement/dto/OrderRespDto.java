package com.welfare.servicesettlement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ProjectName: e-welfare
 * @Package: welfare.servicesettlement.dto
 * @ClassName: OrderRespDto
 * @Author: jian.zhou
 * @Description: 订单实体
 * @Date: 2021/1/7 20:42
 * @Version: 1.0
 */
@ApiModel("订单实体")
@Data
public class OrderRespDto implements Serializable {

    @ApiModelProperty("订单编号")
    private String orderId;
    @ApiModelProperty("商品")
    private String goods;
    @ApiModelProperty("供应商名称")
    private String merchantName;
    @ApiModelProperty("供应商编码")
    private String merchantCode;
    @ApiModelProperty("买家名称")
    private String accountName;
    @ApiModelProperty("买家账号")
    private Long accountCode;
    @ApiModelProperty("买家所属客户编码")
    private String accountMerCode;
    @ApiModelProperty("买家所属客户名称")
    private String accountMerName;
    @ApiModelProperty("买家卡号")
    private Integer accountCardId;
    @ApiModelProperty("门店编码")
    private String storeCode;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("订单总金额")
    private String orderAmount;
    @ApiModelProperty("订单创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date orderTime;
    @ApiModelProperty("电话号码")
    private String phone;
    @ApiModelProperty("就餐时段")
    private String timeInterval;
    @ApiModelProperty("所属组织")
    private String departmentName;
    @ApiModelProperty("账户类别")
    private String accountType;
    @ApiModelProperty("账号类别名称")
    private String accountTypeName;
    @ApiModelProperty("支付渠道")
    private String paymentChannel;
}
