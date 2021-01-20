package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountApplyTotalDTO;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.AccountDepositApplyDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 充值申请明细(account_deposit_apply_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface AccountDepositApplyDetailMapper extends BaseMapper<AccountDepositApplyDetail> {

  /**
   * 通过员工额度申请编码查询员工申请明细（只返回未删除的员工)
   * @param applyCode
   * @return
   */
  List<AccountDepositApplyDetail> listByApplyCodeIfAccountExist(@Param("applyCode") String applyCode);

  /**
   * 通过员工额度申请编码分页查询员工申请明细（只返回未删除的员工)
   * @param page
   * @param applyCode
   * @return
   */
  Page<TempAccountDepositApplyDTO> listByApplyCodeIfAccountExist2(@Param("page") Page page, @Param("applyCode") String applyCode);

  /**
   * 通过员工额度申请编码查询申请总人数和总金额
   * @param applyCode
   * @return
   */
  AccountApplyTotalDTO getUserCountAndTotalmount(@Param("applyCode") String applyCode);
}
