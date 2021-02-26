package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.mapper.AccountDeductionDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户交易流水明细表(account_deduction_detail)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class AccountDeductionDetailDao extends ServiceImpl<AccountDeductionDetailMapper, AccountDeductionDetail> {

    public List<AccountDeductionDetail> queryByTransNoAndTransType(String transNo, String transType) {
        QueryWrapper<AccountDeductionDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountDeductionDetail.TRANS_NO, transNo);
        queryWrapper.eq(AccountDeductionDetail.TRANS_TYPE,transType);
        return list(queryWrapper);
    }

    public List<AccountDeductionDetail> queryByRelatedTransNoAndTransType(String relatedTransNo, String transType) {
        QueryWrapper<AccountDeductionDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountDeductionDetail.RELATED_TRANS_NO, relatedTransNo);
        queryWrapper.eq(AccountDeductionDetail.TRANS_TYPE,transType);
        return list(queryWrapper);
    }

    public List<AccountDeductionDetail> queryByStoreNosAndDate(Collection<String> storeNos, Date from, Date end){
        QueryWrapper<AccountDeductionDetail> wrapper = new QueryWrapper<>();
        wrapper.in(AccountBillDetail.STORE_CODE,storeNos).gt(AccountBillDetail.TRANS_TIME,from).le(AccountBillDetail.TRANS_TIME,end);
        return super.list(wrapper);
    }
}