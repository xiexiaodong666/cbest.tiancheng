package com.welfare.service;


import com.welfare.persist.entity.AccountAmountTypeGroup;
import com.welfare.service.dto.nhc.NhcFamilyMemberDTO;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 2:46 下午
 */
public interface AccountAmountTypeGroupService {

    /**
     * 移除员工
     * @param accountCode
     * @param merAccountTypeCode
     * @return
     */
    boolean removeByAccountCode(Long accountCode, String merAccountTypeCode);

    /**
     * 加入组
     * @param joinAccountCode 新加入的员工编码
     * @param groupAccountCode 组内的某个员工的编码
     * @param merAccountTypeCode 福利类型
     * @return
     */
    boolean addByAccountCodeAndMerAccountTypeCode(Long joinAccountCode, Long groupAccountCode, String merAccountTypeCode);

    /**
     * 根据账户号查询组
     * @param accountCode
     * @return
     */
    AccountAmountTypeGroup queryByAccountCode(Long accountCode);


}
