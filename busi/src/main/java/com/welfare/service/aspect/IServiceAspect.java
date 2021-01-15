package com.welfare.service.aspect;

import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.AccountChangeEventRecordDao;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountChangeEventRecord;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/15/2021
 */
@Aspect
@Component
public class AccountDaoAspect {

    /**
     * AccountDao.update*
     */
    @Pointcut("execution(public * com.baomidou.mybatisplus.extension.service.IService.updateById(..))")
    public void onUpdateById(){

    }

    @Before(value = "onUpdateById()")
    public void before(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        if(!(args[0] instanceof Account)){
            return ;
        }
        Account accountToUpdate = (Account)args[0];
        AccountDao accountDao = SpringBeanUtils.getBean(AccountDao.class);
        Account accountInDb = accountDao.getById(accountToUpdate.getId());
        BigDecimal balanceToUpdate = accountToUpdate.getAccountBalance();
        BigDecimal balanceInDb = accountInDb.getAccountBalance();
        if(balanceInDb.compareTo(balanceToUpdate)!=0){
            AccountChangeEventRecord accountChangeEventRecord = new AccountChangeEventRecord();
            accountChangeEventRecord.setAccountCode(accountToUpdate.getAccountCode());
            accountChangeEventRecord.setChangeType(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeType());
            accountChangeEventRecord.setChangeValue(AccountChangeType.ACCOUNT_BALANCE_CHANGE.getChangeValue());
            AccountChangeEventRecordDao accountChangeEventRecordDao = SpringBeanUtils.getBean(AccountChangeEventRecordDao.class);
            accountChangeEventRecordDao.save(accountChangeEventRecord);
        }
    }
}
