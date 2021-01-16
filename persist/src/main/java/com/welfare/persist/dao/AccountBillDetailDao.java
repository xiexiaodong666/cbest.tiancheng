package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.mapper.AccountBillDetailMapper;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户流水明细(account_bill_detail)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class AccountBillDetailDao extends ServiceImpl<AccountBillDetailMapper, AccountBillDetail> {

    public List<AccountBillDetail> queryByTransNoAndTransType(String transNo, String transType) {
        QueryWrapper<AccountBillDetail> wrapper = new QueryWrapper<>();
        wrapper.eq(AccountBillDetail.TRANS_NO,transNo);
        wrapper.eq(AccountBillDetail.TRANS_TYPE,transType);
        return list(wrapper);
    }
}