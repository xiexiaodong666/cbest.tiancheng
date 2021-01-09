package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class Tree<T extends Tree> {

    @ApiModelProperty("父级")
    private String parentCode;
    @ApiModelProperty("父级")
    private String code;
    @ApiModelProperty("子节点")
    private List<T> children;

}
