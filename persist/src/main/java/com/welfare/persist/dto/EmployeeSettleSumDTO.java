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
public class EmployeeSettleSumDTO {

    @ApiModelProperty(value = "第三方消费金额")
    private String thirdConsumeAmountSum;

    @ApiModelProperty(value = "自营消费金额")
    private String selfConsumeAmountSum;

    @ApiModelProperty(value = "消费总金额")
    private String totalAmount;

}
