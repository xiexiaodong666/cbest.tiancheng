package com.welfare.servicesettlement.dto.wholesale;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/27/2021
 */
@Data
public class WholesaleSettleStatusDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty("发送状态")
    private String sendStatus;
    @ApiModelProperty("结算状态")
    private String settleStatus;
}
