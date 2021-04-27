package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 3:33 下午
 */
@Data
public class PageReq {

    @ApiModelProperty("页码从1开始")
    private int current = 1;

    @ApiModelProperty("每页大小")
    private int size = 10;
}
