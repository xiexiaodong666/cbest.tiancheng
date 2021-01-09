package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dao.TempAccountDepositApplyDao;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.TempAccountDepositApply;
import com.welfare.persist.mapper.TempAccountDepositApplyMapper;
import com.welfare.service.TempAccountDepositApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  1:05 PM
 */
@Service
@Slf4j
public class TempAccountDepositApplyServiceImpl implements TempAccountDepositApplyService {

  @Autowired
  private TempAccountDepositApplyDao tempAccountDepositApplyDao;

  @Autowired
  private TempAccountDepositApplyMapper tempAccountDepositApplyMapper;


  @Override
  public Boolean saveAll(List<TempAccountDepositApply> applys) {
    return tempAccountDepositApplyDao.saveBatch(applys);
  }

  @Override
  public Boolean delByFileId(String fileId) {
    QueryWrapper<TempAccountDepositApply> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(TempAccountDepositApply.FILE_ID, fileId);
    return tempAccountDepositApplyDao.remove(queryWrapper);
  }

  @Override
  public Page<TempAccountDepositApplyDTO> pageByFileId(int current, int size, String fileId) {
    Page<TempAccountDepositApply> page = new Page<>();
    page.setCurrent(current);
    page.setSize(size);
    return tempAccountDepositApplyMapper.pageByFileId(page, fileId);
  }

  @Override
  public List<TempAccountDepositApply> getAllByFileId(String fileId) {
    QueryWrapper<TempAccountDepositApply> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(TempAccountDepositApply.FILE_ID, fileId);
    return tempAccountDepositApplyDao.getBaseMapper().selectList(queryWrapper);
  }
}