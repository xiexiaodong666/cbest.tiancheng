package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author chengang
 * @date 2/23/2021
 */
@Data
public class EmployeeSettleDTO {

    @ApiModelProperty("员工Id")
    private Long accountId;

    @ApiModelProperty("员工姓名")
    private String accountName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("组织机构")
    private String departmentName;

    @ApiModelProperty("授信额度")
    private String quota;

    @ApiModelProperty("自营消费金额")
    private String selfConsumerAmount;

    @ApiModelProperty("第三方消费金额")
    private String thirdConsumerAmount;

    @ApiModelProperty("消费总额")
    private String totalConsumerAmount;

    @ApiModelProperty("消费笔数")
    private String orderNum;
}
