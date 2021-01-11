package com.welfare.service;


import com.welfare.service.dto.DictDTO;
import com.welfare.service.dto.DictReq;

import java.util.List;

/**
 * 字典服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface DictService {

    List<DictDTO> getByType(DictReq dictType);

}
