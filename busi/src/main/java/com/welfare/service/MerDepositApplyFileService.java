package com.welfare.service;

import com.welfare.persist.entity.MerDepositApplyFile;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/14  3:16 PM
 */
public interface MerDepositApplyFileService {

  boolean delByMerDepositApplyCode(String merDepositApplyCode);

  List<MerDepositApplyFile> listByMerDepositApplyCode(String merDepositApplyCode);

  void save(String merDepositApplyCode, List<String> fileUrls);
}