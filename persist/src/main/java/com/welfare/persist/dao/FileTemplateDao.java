package com.welfare.persist.dao;

import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.FileTemplate;
import com.welfare.persist.mapper.FileTemplateMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 文件模板(file_template)数据DAO
 *
 * @author hao.yin
 * @since 2021-01-18 17:24:46
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class FileTemplateDao extends ServiceImpl<FileTemplateMapper, FileTemplate> {

}