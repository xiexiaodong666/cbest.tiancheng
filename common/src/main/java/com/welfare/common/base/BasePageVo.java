package com.welfare.common.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/13 3:30 下午
 * @desc
 */
@Data
public class BasePageVo<T> extends Page<T> {
    @ApiModelProperty("额外信息")
    private Map<String, Object> ext;

    public BasePageVo(long current, long size, long total){
        super(current, size, total);
    }

    public BasePageVo(long current, long size, long total, List<T> record){
        super(current, size, total);
        this.setRecords(record);
    }

    public BasePageVo(long current, long size, long total, List<T> record, Map<String, Object> ext){
        super(current, size, total);
        this.setRecords(record);
        this.setExt(ext);
    }
}
