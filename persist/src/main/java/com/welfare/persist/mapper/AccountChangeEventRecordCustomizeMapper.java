package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.entity.AccountChangeEventRecord;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 17:31
 */
public interface AccountChangeEventRecordCustomizeMapper extends BaseMapper<AccountChangeEventRecord> {
  Long insertAccountChangeEvent(AccountChangeEventRecord accountChangeEventRecord);
  void batchInsert(List<AccountChangeEventRecord> accountChangeEventRecordList);
}
