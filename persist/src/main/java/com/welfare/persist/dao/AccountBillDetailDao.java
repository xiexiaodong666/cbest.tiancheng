package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.mapper.AccountBillDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
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

    public List<AccountBillDetail> queryByTransNoOrderNoAndTransType(String transNo, String orderNo, String transType) {
        QueryWrapper<AccountBillDetail> wrapper = new QueryWrapper<>();
        wrapper.eq(AccountBillDetail.TRANS_NO,transNo);
        if(!StringUtils.isEmpty(orderNo)){
            wrapper.eq(AccountBillDetail.ORDER_NO,orderNo);
        }
        wrapper.eq(AccountBillDetail.TRANS_TYPE,transType);
        return list(wrapper);
    }

    public AccountBillDetail getOneByTransNoAndTransType(String transNo, String transType) {
        QueryWrapper<AccountBillDetail> wrapper = new QueryWrapper<>();
        wrapper.eq(AccountBillDetail.TRANS_NO,transNo);
        wrapper.eq(AccountBillDetail.TRANS_TYPE,transType);
        wrapper.last("limit 1");
        return getOne(wrapper);
    }

    /**
     * 根据门店编码，tranDate区间（左开右闭）查询，
     * @param storeNos
     * @param from
     * @param end
     * @return
     */
    public List<AccountBillDetail> queryByStoreNosAndDate(Collection<String> storeNos, Date from, Date end){
        QueryWrapper<AccountBillDetail> wrapper = new QueryWrapper<>();
        wrapper.in(AccountBillDetail.STORE_CODE,storeNos).gt(AccountBillDetail.TRANS_TIME,from).le(AccountBillDetail.TRANS_TIME,end);
        return super.list(wrapper);
    }
}