package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * (商户账号类型)详情返回数据
 *
 * @author hao.yin
 * @since 2021-01-08 11:23:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
public class MerchantAccountTypeUpdateDTO {



    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("商户编码")
    private String merCode;
    @ApiModelProperty("更新人")
    private String updateUser;
    List<TypeItem> typeList;
    @Data
    @NoArgsConstructor
    public static class  TypeItem{
        private Long id;
        @ApiModelProperty("扣款序号")
        private Integer deductionOrder;
        @ApiModelProperty("商户账户类型名称")
        private String merAccountTypeName;
        @ApiModelProperty("商户账户类型编码")
        private String merAccountTypeCode;
    }

}