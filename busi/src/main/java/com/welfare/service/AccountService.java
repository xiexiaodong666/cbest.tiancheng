package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.entity.Account;
import com.welfare.service.dto.AccountBillDTO;
import com.welfare.service.dto.AccountBillDetailDTO;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountDetailDTO;
import com.welfare.service.dto.AccountPageReq;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 账户信息服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountService {
    Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page,
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

    AccountDetailDTO queryDetail(Long id);

    Boolean save(Account account);
    Boolean update(Account account);

    Page<AccountBillDetailDTO> queryAccountBillDetail(Integer currentPage,Integer pageSize,
        String accountCode, Date createTimeStart,Date createTimeEnd);

    AccountBillDTO quertBill(String accountCode, Date createTimeStart,Date createTimeEnd);

    List<String> getAccountCodeList(List<String> accountCodes);
}
