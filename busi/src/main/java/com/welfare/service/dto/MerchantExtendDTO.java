package com.welfare.service.dto;

import com.welfare.persist.entity.MerchantExtend;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/16 1:46 下午
 */
@Data
public class MerchantExtendDTO {

    /**
     * 商户编码
     */
    private String merCode;
    /**
     * 行业标签(多个逗号隔开)
     */
    @ApiModelProperty("行业标签(多个逗号隔开)")
    private String industryTag;
    /**
     * 积分商城是否开启
     */
    @ApiModelProperty("积分商城是否开启")
    @NotNull(message = "积分商城是否开启 不能为空")
    private Boolean pointMall;


    @ApiModelProperty("结算方式")
    private String supplierWholesaleSettleMethod;

    public static MerchantExtendDTO of(MerchantExtend merchantExtend) {
        MerchantExtendDTO dto = new MerchantExtendDTO();
        if (Objects.nonNull(merchantExtend)) {
            BeanUtils.copyProperties(merchantExtend, dto);
        }
        return dto;
    }
}
