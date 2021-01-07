package com.welfare.serviceaccount.dto;
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
   * 消费配置配置门店
   */
  @ApiModelProperty("配置门店")
  private List<SupplierStoreDTO> supplierStoreDTOList;
  /**
   * 员工类型消费方式
   */
  @ApiModelProperty("员工类型消费方式")
  private String consumType;
  /**
   * 使用状态
   */
  @ApiModelProperty("使用状态")
  private Integer status;
  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  private Date createTime;
  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
}
