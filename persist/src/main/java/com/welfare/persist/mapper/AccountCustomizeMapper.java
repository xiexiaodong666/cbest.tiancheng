package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountBillDetailMapperDTO;
import com.welfare.persist.dto.AccountBillMapperDTO;
import com.welfare.persist.dto.AccountDetailMapperDTO;
import com.welfare.persist.dto.AccountIncrementDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.entity.Account;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface AccountCustomizeMapper extends BaseMapper<Account> {

  IPage<AccountPageDTO> queryPageDTO(Page<AccountPageDTO> page,
      @Param("merCode") String merCode,
      @Param("accountName")String accountName,
      @Param("departmentCode")String  departmentCode,
      @Param("accountStatus")Integer accountStatus,
      @Param("accountTypeCode")String accountTypeCode);

  List<AccountPageDTO> queryPageDTO(@Param("merCode") String merCode,
      @Param("accountName")String accountName,
      @Param("departmentCode")String  departmentCode,
      @Param("accountStatus")Integer accountStatus,
      @Param("accountTypeCode")String accountTypeCode);

  AccountDetailMapperDTO queryDetail(@Param("id") Long id);

  AccountDetailMapperDTO queryDetailByAccountCode(@Param("accountCode") String accountCode);

  IPage<AccountBillDetailMapperDTO> queryAccountBillDetail(Page<AccountBillDetailMapperDTO> page,
      @Param("accountCode") String accountCode,
      @Param("createTimeStart") Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

  List<AccountBillDetailMapperDTO> queryAccountBillDetail(@Param("accountCode") String accountCode,
      @Param("createTimeStart") Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

  AccountBillMapperDTO queryBill( @Param("accountCode") String accountCode,
      @Param("createTimeStart") Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

  List<AccountIncrementDTO> queryIncrementDTO(@Param("storeCode") String storeCode,
      @Param("size")Integer  size,
      @Param("changeEventId")Long changeEventId);
  void batchUpdateChangeEventId(List<Map<String,Object>> list);
}
