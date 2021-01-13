package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountTypeDTO;
import com.welfare.persist.entity.Account;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountTypeCustomizeMapper extends BaseMapper<Account> {

  public List<AccountTypeDTO> queryAccountType(
      @Param("merCode") String merCode,
      @Param("typeCode") String typeCode,
      @Param("typeName")String typeName,
      @Param("createTimeStart") Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

  IPage<AccountTypeDTO> queryAccountType(Page<AccountTypeDTO> page,
      @Param("merCode") String merCode,
      @Param("typeCode") String typeCode,
      @Param("typeName")String typeName,
      @Param("createTimeStart") Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

}
