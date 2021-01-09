package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.entity.Account;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountPageReq;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * 账户信息服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountService {
    public Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page,
        AccountPageReq accountPageReq);

    /**
     * 增加员工账号余额
     * @param increaseBalance
     * @param updateUser
     * @param accountCode
     * @return
     */
    int increaseAccountBalance(BigDecimal increaseBalance, String updateUser, String accountCode);

    Account getByAccountCode(String accountCode);

    Boolean delete(Long id);

    Boolean active(Long id,Integer active);
}
