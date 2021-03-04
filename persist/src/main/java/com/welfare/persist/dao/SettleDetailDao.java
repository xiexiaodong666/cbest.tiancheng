package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.SettleDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * (settle_detail)数据DAO
 *
 * @author kancy
 * @since 2021-01-09 15:21:12
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class SettleDetailDao extends ServiceImpl<SettleDetailMapper, SettleDetail> {
    public boolean updateToSettled(String settleNo){
        UpdateWrapper<SettleDetail> updateWrapper = new UpdateWrapper<>();
        SettleDetail settleDetail = new SettleDetail();
        settleDetail.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.SETTLED.code());
        updateWrapper.eq(SettleDetail.SETTLE_NO,settleNo);
        return update(settleDetail,updateWrapper);
    }
}