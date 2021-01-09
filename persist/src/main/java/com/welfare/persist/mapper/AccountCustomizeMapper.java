package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountDetailMapperDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.entity.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountCustomizeMapper extends BaseMapper<Account> {

  IPage<AccountPageDTO> queryPageDTO(Page<AccountPageDTO> page,
      @Param("merCode") String merCode,
      @Param("accountName")String accountName,
      @Param("departmentCode")String  departmentCode,
      @Param("accountStatus")Integer accountStatus,
      @Param("accountTypeCode")String accountTypeCode);

  AccountDetailMapperDTO queryDetail(@Param("id") Long id);
}
