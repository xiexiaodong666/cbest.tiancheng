package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountApplyTotalDTO;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.TempAccountDepositApply;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  1:04 PM
 */
public interface TempAccountDepositApplyService {

  /**
   * 批量保存
   * @param applys
   * @return
   */
  Boolean saveAll(List<TempAccountDepositApply> applys);

  /**
   * 通过fileId删除文件内容
   * @param fileId
   * @return
   */
  Boolean delByFileId(String fileId);

  /**
   * 通过fileId分页查询批量员工申请上传文件内容
   * @param current
   * @param size
   * @param fileId
   * @return
   */
  Page<TempAccountDepositApplyDTO> pageByFileIdByExistAccount(int current, int size, String fileId, String merCode);

  /**
   * 通过fileId查询批量员工申请上传文件内容
   * @param fileId
   * @return
   */
  List<TempAccountDepositApplyDTO> listByFileIdExistAccount(String fileId, String merCode);

  /**
   * 通过fileId分页查询批量员工申请上传文件内容
   * @param fileId
   * @return
   */
  List<TempAccountDepositApply> getAllByFileId(String fileId);

  /**
   * 申请账号额度文件上传
   * @param multipartFile
   * @param requestId
   * @return
   */
  String upload(MultipartFile multipartFile, String requestId);

  /**
   * 通过requestId获取上传文件的fileId
   * @param requestId
   * @return
   */
  String getFileIdByRequestId(String requestId);

  /**
   * 通过fileId分页查询批量员工申请上传文件里的总人数和总金额
   * @param fileId
   * @return
   */
  AccountApplyTotalDTO getUserCountAndTotalmount(String fileId, String merCode);

}