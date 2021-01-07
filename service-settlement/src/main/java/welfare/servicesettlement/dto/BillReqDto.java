package welfare.servicesettlement.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:47 下午
 * @desc 账单列表请求dto
 */
@Data
public class BillReqDto {

    @ApiModelProperty(value = "商户代码")
    private String merCode;

    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ApiModelProperty(value = "起始月份 yyyy-MM")
    private String startMonthStr;

    @ApiModelProperty(value = "结束月份 yyyy-MM")
    private String endMonthStr;

    @ApiModelProperty(value = "合作方式")
    private String merCooperationMode;

    @ApiModelProperty(value = "结算状态 待结算unsettled 已结算settled")
    private String settleStatus;

    @ApiModelProperty(value = "对账状态 待确认unconfirmed 已确认confirmed")
    private String recStatus;

}
