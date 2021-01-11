package com.welfare.service.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 11:49
 */
@Data
@NoArgsConstructor
public class AccountConsumeSceneReq implements Serializable {
  /**
   * 场景ID
   */
  @ApiModelProperty("id")
  private Long id;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  @NotEmpty(message = "商户代码为空")
  private String merCode;
  /**
   * 员工类型编码
   */
  @ApiModelProperty("员工类型编码")
  @NotEmpty(message = "员工类型编码为空")
  private Long accountTypeId;

  /**
   * 员工类型编码
   */
  @ApiModelProperty("对应门店")
  private List<AccountConsumeSceneStoreRelationReq> accountConsumeSceneStoreRelationReqList;
  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String remark;
  /**
   * 状态
   */
  @ApiModelProperty("状态")
  private Integer status;
  /**
   * 创建人
   */
  @ApiModelProperty("创建人")
  private String createUser;
  /**
   * 更新人
   */
  @ApiModelProperty("更新人")
  private String updateUser;

  /**
   * 删除标志  1-删除、0-未删除
   */
  @ApiModelProperty("删除标志  1-删除、0-未删除") @TableLogic
  private Boolean deleted;

}
