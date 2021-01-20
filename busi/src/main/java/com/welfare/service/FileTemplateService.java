package com.welfare.service;


import com.welfare.persist.entity.FileTemplate;

/**
 * 文件模板服务接口
 *
 * @author hao.yin
 * @since 2021-01-18 17:24:46
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface FileTemplateService {
    boolean add(FileTemplate fileTemplate);
    String getByType(String type);
}
