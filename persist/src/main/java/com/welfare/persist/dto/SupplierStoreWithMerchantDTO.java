package com.welfare.persist.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class SupplierStoreWithMerchantDTO {

    /**
     * id
     */
    @ApiModelProperty("id")
	private Long id;

    /**
     * 门店状态
     */
    @ApiModelProperty("status")
    private String status;

    /**
     * 门店状态转义
     */
    @ApiModelProperty("statusName")
    private String statusName;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")  
    private String merCode;
    /**
     * 门店代码
     */
    @ApiModelProperty("门店代码")  
    private String storeCode;
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")  
    private String storeName;
    /**
     * 门店层级
     */
    @ApiModelProperty("门店层级")  
    private Integer storeLevel;
    /**
     * 父级门店
     */
    @ApiModelProperty("父级门店")  
    private String storeParent;
    /**
     * 门店路径
     */
    @ApiModelProperty("门店路径")  
    private String storePath;
    /**
     * 备注
     */
    @ApiModelProperty("备注")  
    private String remark;
    @ApiModelProperty("虚拟收银机号")
    private String cashierNo;

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
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 外部编码
     */
    @ApiModelProperty("外部编码")  
    private String externalCode;

    /**
     * 商户名称
     */
    @ApiModelProperty("商户名称")
    private String merName;

}