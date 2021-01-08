package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.entity.AccountAmountType;
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

  int batchSaveOrUpdate(@Param("list") List<AccountAmountType> list);

}
