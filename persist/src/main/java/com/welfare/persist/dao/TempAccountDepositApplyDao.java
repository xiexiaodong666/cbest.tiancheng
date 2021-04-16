package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.TempAccountDepositApply;
import com.welfare.persist.mapper.TempAccountDepositApplyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * (temp_account_deposit_apply)数据DAO
 *
 * @author kancy
 * @since 2021-01-09 13:01:22
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class TempAccountDepositApplyDao extends ServiceImpl<TempAccountDepositApplyMapper, TempAccountDepositApply> {

    public List<TempAccountDepositApply> listByRequestId(String requestId) {
        QueryWrapper<TempAccountDepositApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(TempAccountDepositApply.REQUEST_ID, requestId);
        return list(queryWrapper);
    }
}