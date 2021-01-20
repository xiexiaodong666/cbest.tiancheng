package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountTypeMapperDTO;
import com.welfare.persist.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AccountTypeCustomizeMapper extends BaseMapper<Account> {

  public List<AccountTypeMapperDTO> queryAccountType(
      @Param("merCode") String merCode,
      @Param("typeCode") String typeCode,
      @Param("typeName")String typeName,
      @Param("createTimeStart") Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

  IPage<AccountTypeMapperDTO> queryAccountType(Page<AccountTypeMapperDTO> page,
      @Param("merCode") String merCode,
      @Param("typeCode") String typeCode,
      @Param("typeName")String typeName,
      @Param("createTimeStart") Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

}
