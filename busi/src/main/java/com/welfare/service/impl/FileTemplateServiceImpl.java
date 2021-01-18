package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dao.FileTemplateDao;
import com.welfare.persist.entity.FileTemplate;
import com.welfare.service.FileTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件模板服务接口实现
 *
 * @author hao.yin
 * @since 2021-01-18 17:24:46
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class FileTemplateServiceImpl implements FileTemplateService {
    private final FileTemplateDao fileTemplateDao;

    @Override
    @Transactional
    public boolean add(FileTemplate fileTemplate) {
        QueryWrapper<FileTemplate> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(FileTemplate.FILE_TYPE,fileTemplate.getFileType());
        fileTemplateDao.remove(queryWrapper);
        return fileTemplateDao.save(fileTemplate);
    }

    @Override
    public String getByType(String type) {
        QueryWrapper<FileTemplate> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(FileTemplate.FILE_TYPE,type);
        return fileTemplateDao.list(queryWrapper).get(0).getUrl();
    }
}