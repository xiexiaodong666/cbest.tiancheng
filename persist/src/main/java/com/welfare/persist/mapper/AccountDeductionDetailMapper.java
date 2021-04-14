package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.entity.AccountDeductionDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 用户交易流水明细表(account_deduction_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface AccountDeductionDetailMapper extends BaseMapper<AccountDeductionDetail> {

    /**
     * 求和商户下某种福利类型充值总额
     * @param merCode
     * @param merAccountTypeCode
     * @return
     */
    BigDecimal sumDepositDetailAmount(@Param("merCode") String merCode, @Param("merAccountTypeCode") String merAccountTypeCode);
}
