package com.welfare.persist.dao;

import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.mapper.AccountDeductionDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

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

}