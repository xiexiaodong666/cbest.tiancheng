package com.welfare.persist.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.mapper.AccountChangeEventRecordCustomizeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 17:40
 */
@Slf4j
@Repository
public class AccountChangeEventRecordDao extends
    ServiceImpl<AccountChangeEventRecordCustomizeMapper, AccountChangeEventRecord> {

}
