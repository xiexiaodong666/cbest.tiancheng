package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountTypeMapperDTO;
import com.welfare.persist.dto.MerSupplierStoreDTO;
import com.welfare.persist.entity.AccountType;
import com.welfare.service.dto.AccountTypeDTO;
import java.util.Date;
import java.util.List;

/**
 * 员工类型服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountTypeService {
  Page<AccountType> pageQuery(Page<AccountType> page, QueryWrapper<AccountType> queryWrapper);

  Page<AccountTypeDTO> getPageDTO(Page<AccountTypeMapperDTO> page,
      String merCode,String typeCode,String typeName, Date startDate,Date endDate);
  List<AccountTypeDTO> queryAccountTypeDTO(String merCode,String typeCode,String typeName, Date startDate,Date endDate);

  List<MerSupplierStoreDTO> queryMerSupplierStoreDTList(String merCode);
  public AccountType getAccountType(Long id);
  public Boolean save(AccountType accountType);
  public Boolean update(AccountType accountType);
  public Boolean delete(Long id);
  AccountType queryByTypeCode(String merCode,String typeCode);
}
