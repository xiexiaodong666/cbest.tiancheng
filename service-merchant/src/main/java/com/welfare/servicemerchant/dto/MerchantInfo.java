package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class MerchantInfo {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;
    /**
     * 员工自主充值
     */
    @ApiModelProperty("员工自主充值")
    private String selfRecharge;
    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")
    private String merName;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    private String merCode;
    /**
     * 商户类型
     */
    @ApiModelProperty("商户类型")
    private String merType;
    /**
     * 身份属性
     */
    @ApiModelProperty("身份属性")
    private String merIdentity;
    /**
     * 合作方式
     */
    @ApiModelProperty("合作方式")
    private String merCooperationMode;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createUser;
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateUser;
    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")
    private Date updateTime;

}
