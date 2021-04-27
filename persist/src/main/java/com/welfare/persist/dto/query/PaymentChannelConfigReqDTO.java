package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/26 5:53 下午
 */
@Data
public class PaymentChannelConfigReqDTO {

    @ApiModelProperty(value = "商户", required = true)
    @NotEmpty(message = "商户不能为空")
    private List<MerchantReq> merchants;

    @Data
    public static class MerchantReq {
        @ApiModelProperty(value = "商户编码", required = true)
        @NotEmpty(message = "商户编码不能为空")
        private String merCode;

        @ApiModelProperty(value = "门店和场景", required = true)
        @NotEmpty(message = "门店和场景不能为空")
        private List<StoreCodeAndConsumeType> storeCodeAndConsumeTypes;

        @Data
        public static class StoreCodeAndConsumeType {

            @ApiModelProperty(value = "门店编码", required = true)
            @NotEmpty(message = "门店编码不能为空")
            private String storeCode;

            @ApiModelProperty(value = "消费场景", required = true)
            @NotEmpty(message = "消费场景不能为空")
            private Set<String> consumeTypes;
        }
    }
}
