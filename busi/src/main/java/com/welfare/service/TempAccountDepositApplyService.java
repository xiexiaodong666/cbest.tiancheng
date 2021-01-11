package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.TempAccountDepositApply;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  1:04 PM
 */
public interface TempAccountDepositApplyService {

  Boolean saveAll(List<TempAccountDepositApply> applys);

  Boolean delByFileId(String fileId);

  Page<TempAccountDepositApplyDTO> pageByFileIdByExistAccount(int current, int size, String fileId);

  List<TempAccountDepositApplyDTO> listByFileIdExistAccount(String fileId);

  List<TempAccountDepositApply> getAllByFileId(String fileId);

  /**
   * 申请账号额度文件上传
   * @param multipartFile
   * @param requestId
   * @return
   */
  String upload(MultipartFile multipartFile, String requestId, ThreadPoolExecutor executor);

  /**
   * 通过requestId获取上传文件的fileId
   * @param requestId
   * @return
   */
  String getFileIdByRequestId(String requestId);



}