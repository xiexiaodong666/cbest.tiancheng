package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.welfare.persist.entity.Merchant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商户信息(merchant)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {
    /**
     * 更新所有字段（除了deleted）
     * @return
     */
    Integer alwaysUpdateSomeColumnById(@Param(Constants.ENTITY) Merchant merchant);

    //List<Merchant> selectRebateMerList();
}
