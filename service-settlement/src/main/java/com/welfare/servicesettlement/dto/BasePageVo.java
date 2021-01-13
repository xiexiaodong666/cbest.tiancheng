package com.welfare.servicesettlement.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/13 3:30 下午
 * @desc
 */
@Data
public class BasePageVo extends Page {
    @ApiModelProperty("额外信息")
    private Map Ext;
}
