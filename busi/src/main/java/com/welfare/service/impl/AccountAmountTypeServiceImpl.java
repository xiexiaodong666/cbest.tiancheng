package com.welfare.service.impl;

import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.service.AccountAmountTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  9:29 PM
 */
@Service
@Slf4j
public class AccountAmountTypeServiceImpl implements AccountAmountTypeService {

  @Autowired
  private AccountAmountTypeMapper accountAmountTypeMapper;

  @Override
  public int batchSaveOrUpdate(List<AccountAmountType> list) {
    return accountAmountTypeMapper.batchSaveOrUpdate(list);
  }
}