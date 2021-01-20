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

  /**
   * 通过商户额度申请编码删除所有附件
   * @param merDepositApplyCode
   * @return
   */
  boolean delByMerDepositApplyCode(String merDepositApplyCode);

  /**
   * 通过商户额度申请编码查询所有附件
   * @param merDepositApplyCode
   * @return
   */
  List<MerDepositApplyFile> listByMerDepositApplyCode(String merDepositApplyCode);

  /**
   * 保存商户额度申请附件并删除之前的附件
   * @param merDepositApplyCode
   * @param fileUrls
   */
  void save(String merDepositApplyCode, List<String> fileUrls);
}