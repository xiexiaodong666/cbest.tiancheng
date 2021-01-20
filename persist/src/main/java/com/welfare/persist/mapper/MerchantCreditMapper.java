package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.entity.MerchantCredit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 商户额度信(merchant_credit)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MerchantCreditMapper extends BaseMapper<MerchantCredit> {

  int decreaseRechargeLimit(@Param("increaseLimit")BigDecimal increaseLimit, @Param("id")Long id);

}
