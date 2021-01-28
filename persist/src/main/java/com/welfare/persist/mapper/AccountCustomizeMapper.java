package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.*;
import com.welfare.persist.entity.Account;
import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AccountCustomizeMapper extends BaseMapper<Account> {

  int increaseAccountSurplusQuota(@Param("surplusQuota") BigDecimal surplusQuota,
      @Param("updateUser")String updateUser,
      @Param("accountCode")String accountCode);

  int updateMaxAndSurplusQuota(@Param("accountCode")String accountCode,
      @Param("maxQuota") BigDecimal maxQuota,
      @Param("surplusQuota") BigDecimal surplusQuota,
      @Param("updateUser")String updateUser);

  int restoreAccountSurplusQuota(@Param("accountCode")Long accountCode,
                               @Param("updateUser")String updateUser);

  IPage<AccountPageDTO> queryPageDTO(Page<AccountPageDTO> page,
      @Param("merCode") String merCode,
      @Param("accountName")String accountName,
      @Param("departmentPathList")List<String>  departmentPathList,
      @Param("accountStatus")Integer accountStatus,
      @Param("accountTypeCode")String accountTypeCode,
      @Param("binding") Integer binding,
      @Param("cardId")String cardId,
      @Param("phone")String phone);

  List<AccountPageDTO> queryPageDTO(@Param("merCode") String merCode,
      @Param("accountName")String accountName,
      @Param("departmentPathList")List<String>  departmentPathList,
      @Param("accountStatus")Integer accountStatus,
      @Param("accountTypeCode")String accountTypeCode,
      @Param("binding") Integer binding,
      @Param("cardId")String cardId,
      @Param("phone")String phone);

  AccountDetailMapperDTO queryDetail(@Param("id") Long id);

  AccountDetailMapperDTO queryDetailByParam(@Param("id") Long id,@Param("accountCode") Long accountCode,
      @Param("phone") String phone,
      @Param("merCode") String merCode);


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
      @Param("changeEventId")Long changeEventId,
      @Param("consumeType")String consumeType);
  void batchUpdateChangeEventId(List<Map<String,Object>> list);

  List<Account> queryByConsumeSceneIdList(List<Long> list);

  AccountDetailMapperDTO queryDetailPhoneAndMer(@Param("phone") String phone, @Param("merCode") String merCode);
}
