package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountApplyTotalDTO;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.AccountDepositApplyDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

  List<AccountDepositApplyDetail> listByApplyCodeIfAccountExist(@Param("applyCode") String applyCode);

  Page<TempAccountDepositApplyDTO> listByApplyCodeIfAccountExist2(@Param("page") Page page, @Param("applyCode") String applyCode);

  AccountApplyTotalDTO getUserCountAndTotalmount(@Param("applyCode") String applyCode);
}
