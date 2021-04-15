package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welfare.common.constants.CacheConstant.TOTAL_ACCOUNT_AMOUNT_TYPE_GROUP_COUNT;

/**
 * (account_amount_type)数据DAO
 *
 * @author kancy
 * @since 2021-01-08 21:33:49
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class AccountAmountTypeDao extends ServiceImpl<AccountAmountTypeMapper, AccountAmountType> {

    /**
     * 根据账户号查询AccountAmountType列表
     * @param accountCode
     * @return
     */
    public List<AccountAmountType> queryByAccountCode(Long accountCode){
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(AccountAmountType.ACCOUNT_CODE,accountCode);
        return list(queryWrapper);
    }
    /**
     * 根据账户号查询AccountAmountType map
     * @param accountCodes
     * @param merAccountTypeCode
     * @return
     */
    public Map<Long, AccountAmountType> mapByAccountCodes(List<Long> accountCodes, String merAccountTypeCode){
        Map<Long, AccountAmountType> map = new HashMap<>();
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(AccountAmountType.ACCOUNT_CODE, accountCodes);
        queryWrapper.eq(AccountAmountType.MER_ACCOUNT_TYPE_CODE, merAccountTypeCode);
        List<AccountAmountType> list = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(AccountAmountType::getAccountCode, a -> a,(k1, k2)->k1));
        }
        return map;
    }

    public AccountAmountType queryByAccountCodeAndAmountType(Long accountCode, String merAccountType){
        return getOne(Wrappers.<AccountAmountType>lambdaQuery()
                .eq(AccountAmountType::getAccountCode, accountCode)
                .eq(AccountAmountType::getMerAccountTypeCode, merAccountType)
        );
    }

    public List<AccountAmountType> queryByGroupId(Long accountAmountGroupId){
        return list(Wrappers.<AccountAmountType>lambdaQuery()
                .eq(AccountAmountType::getAccountAmountTypeGroupId, accountAmountGroupId)
        );
    }

    public List<AccountAmountType> listByAccountCodes(List<Long> accountCodes, String merAccountTypeCode){
        QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(AccountAmountType.ACCOUNT_CODE, accountCodes);
        queryWrapper.eq(AccountAmountType.MER_ACCOUNT_TYPE_CODE, merAccountTypeCode);
        return list(queryWrapper);
    }
}