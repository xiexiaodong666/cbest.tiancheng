package com.welfare.serviceaccount.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 15:31
 */
@Data
@ApiModel("员工消费场景配置")
public class AccountConsumeSceneDTO implements Serializable {

  @ApiModelProperty("编号")
  private Long id;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 员工类型名称
   */
  @ApiModelProperty("员工类型名称")
  private String accountTypeName;
  /**
   * 配置门店
   */
  @ApiModelProperty("配置门店")
  private List<String> storeCodeName;
  /**
   * 使用状态
   */
  @ApiModelProperty("使用状态")
  private Integer status;
  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date createTime;
  /**
   * 删除标志
   */
  @ApiModelProperty("删除标志")
  private Integer flag;
}
