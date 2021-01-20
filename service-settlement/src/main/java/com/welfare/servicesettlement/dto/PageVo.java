package com.welfare.servicesettlement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.servicesettlement.dto
 * @ClassName: PageVo
 * @Author: jian.zhou
 * @Description: 定制分页对象
 * @Date: 2021/1/10 19:43
 * @Version: 1.0
 */
@ApiModel("分页对象")
@Data
public class PageVo<T> {
    @ApiModelProperty("数据")
    private List<T> records;    //数据
    @ApiModelProperty("总条数")
    private long total;         //总数
    @ApiModelProperty("每页显示数量")
    private long size;          //每页显示数量
    @ApiModelProperty("当前页")
    private long current;       //当前页
    @ApiModelProperty("汇总数据")
    private Ext ext;//额外数据

    @ApiModel("汇总数据")
    @Data
    public static class Ext{
        @ApiModelProperty("总金额")
        private String amount ; //总金额
        @ApiModelProperty("交易笔数")
        private Integer orderNum;    //订单数
    }
}
