package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.TempAccountDepositApply;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  1:04 PM
 */
public interface TempAccountDepositApplyService {

  Boolean saveAll(List<TempAccountDepositApply> applys);

  Boolean delByFileId(String fileId);

  Page<TempAccountDepositApplyDTO> pageByFileId(int current, int size, String fileId);

  List<TempAccountDepositApply> getAllByFileId(String fileId);

}