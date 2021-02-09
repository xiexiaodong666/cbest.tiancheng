package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.dto.AccountDepositIncreDTO;
import com.welfare.persist.entity.AccountAmountType;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (account_amount_type)数据Mapper
 *
 * @author kancy
 * @since 2021-01-08 21:33:49
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface AccountAmountTypeMapper extends BaseMapper<AccountAmountType> {

  int batchSaveOrUpdate(List<AccountDepositIncreDTO> list);

  /**
   *
   * @param accountCode
   * @param merAccountTypeCode
   * @param accountBalance 负值就是减少,此方法余额减少到0一下会不成功
   * @param updateUser
   * @return
   */
  int incrBalance(@Param("accountCode") Long accountCode,
      @Param("merAccountTypeCode") String merAccountTypeCode,
      @Param("accountBalance") BigDecimal accountBalance,
      @Param("updateUser") String updateUser);

  int updateBalance(@Param("accountCode") Long accountCode,
      @Param("merAccountTypeCode") String merAccountTypeCode,
      @Param("accountBalance") BigDecimal accountBalance,
      @Param("updateUser") String updateUser);
}
