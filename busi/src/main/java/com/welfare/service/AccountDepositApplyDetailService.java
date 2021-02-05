package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountApplyTotalDTO;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.AccountDepositApplyDetail;

import java.util.List;

/**
 * 充值申请明细服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountDepositApplyDetailService {

  /**
   * 通过员工额度当申请编码查询明细
   * @param applyCode
   * @return
   */
  List<AccountDepositApplyDetail> listByApplyCode(String applyCode);

  /**
   * 通过员工额度当申请编码删除明细
   * @param applyCode
   * @return
   */
  Boolean physicalDelByApplyCode(String applyCode);

  /**
   * 通过员工额度当申请id分页查询明细
   * @param id
   * @param current
   * @param size
   * @return
   */
  Page<TempAccountDepositApplyDTO> pageById(Long id, int current, int size);

  /**
   * 通过员工额度申请编码查询员工申请明细（只返回未删除的员工)
   * @param applyCode
   * @return
   */
  List<AccountDepositApplyDetail> listByApplyCodeIfAccountExist(String applyCode);

  /**
   * 通过员工额度申请编码查询申请总人数和总金额
   * @param id
   * @return
   */
  AccountApplyTotalDTO getUserCountAndTotalmount(Long id);
}
