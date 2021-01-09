package com.welfare.service.dto;

import com.welfare.persist.entity.BarcodeSalt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
@Data
@ApiModel("条码加盐参数")
public class BarcodeSaltDO {
    @ApiModelProperty("有效期间")
    private String validPeriod;
    @ApiModelProperty("有效时间数字表示")
    private Long validPeriodNumeric;
    @ApiModelProperty("加盐参数")
    private Long saltValue;

    public static BarcodeSaltDO of(BarcodeSalt barcodeSalt){
        BarcodeSaltDO barcodeSaltDO = new BarcodeSaltDO();
        barcodeSaltDO.setValidPeriod(barcodeSalt.getValidPeriod());
        barcodeSaltDO.setValidPeriodNumeric(barcodeSalt.getValidPeriodNumeric());
        barcodeSaltDO.setSaltValue(barcodeSalt.getSaltValue());
        return barcodeSaltDO;
    }
}
