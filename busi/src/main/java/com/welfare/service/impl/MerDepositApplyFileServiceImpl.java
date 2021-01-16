package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dao.MerDepositApplyFileDao;
import com.welfare.persist.entity.MerDepositApplyFile;
import com.welfare.service.MerDepositApplyFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/14  3:16 PM
 */
@Service
@Slf4j
public class MerDepositApplyFileServiceImpl implements MerDepositApplyFileService{

  @Autowired
  private MerDepositApplyFileDao merDepositApplyFileDao;

  @Override
  public boolean delByMerDepositApplyCode(String merDepositApplyCode) {
    QueryWrapper<MerDepositApplyFile> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerDepositApplyFile.MER_DEPOSIT_APPLY_CODE, merDepositApplyCode);
    return merDepositApplyFileDao.remove(queryWrapper);
  }

  @Override
  public List<MerDepositApplyFile> listByMerDepositApplyCode(String merDepositApplyCode) {
    QueryWrapper<MerDepositApplyFile> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerDepositApplyFile.MER_DEPOSIT_APPLY_CODE, merDepositApplyCode);
    return merDepositApplyFileDao.list(queryWrapper);
  }

  @Override
  @Transactional
  public void save(String merDepositApplyCode, List<String> fileUrls) {
    delByMerDepositApplyCode(merDepositApplyCode);
    if (CollectionUtils.isNotEmpty(fileUrls)) {
      List<MerDepositApplyFile> files = new ArrayList<>();
      fileUrls.forEach(url -> {
        MerDepositApplyFile file = new MerDepositApplyFile();
        file.setFileUrl(url);
        file.setMerDepositApplyCode(merDepositApplyCode);
        files.add(file);
      });
      merDepositApplyFileDao.saveBatch(files);
    }
  }
}