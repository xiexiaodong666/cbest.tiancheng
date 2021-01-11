package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hao.yin
 * @version 0.0.1
 * @date 2021/1/8 11:34
 */
@Data
@NoArgsConstructor
public class MerchantAccountTypeSortReq {
    @ApiModelProperty("id")
    @NotNull
    Long id;
    @ApiModelProperty("移动方向  up：上移，down：下移")
    @NotBlank
    String direction;
}
