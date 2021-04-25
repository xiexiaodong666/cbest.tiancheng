package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountConsumeSceneMapperDTO;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.AccountConsumeStoreInfoDTO;
import com.welfare.persist.entity.AccountConsumeScene;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AccountConsumeSceneCustomizeMapper extends BaseMapper<AccountConsumeScene> {
  IPage<AccountConsumeScenePageDTO> getPageDTO(Page<AccountConsumeScenePageDTO> page,
      @Param("merCode") String merCode,
      @Param("accountTypeName")String accountTypeName,
      @Param("status")Integer status,
      @Param("createTimeStart")Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

  List<AccountConsumeScenePageDTO> getPageDTO(@Param("merCode") String merCode,
      @Param("accountTypeName")String accountTypeName,
      @Param("status")Integer status,
      @Param("createTimeStart")Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

  AccountConsumeSceneMapperDTO queryAccountConsumerScene4Detail(@Param("id")Long id);
  AccountConsumeScene queryById(@Param("id")Long id);

  List<AccountConsumeScene> queryByIdList(@Param("idList") List<Long> idList);

  Integer getCountByMerCodeAndAccountTypeAndStoreCode(
      @Param("merCode")String merCode,
      @Param("typeCode")String typeCode,
      @Param("storeCode")String storeCode
  );
  List<AccountConsumeScene> queryDeleteScene(@Param("merCode") String merCode);
  List<AccountConsumeScene> queryDeleteSceneByMerCodeAndStoreCodeList(@Param("merCode") String merCode,
      @Param("storeCodeList")List<String> storeCodeList);
  List<Long> queryDeleteConsumeIdList(@Param("merCode") String merCode);

  List<AccountConsumeStoreInfoDTO> findAllAccountConsumeSceneDTO(String merCode);

  List<AccountConsumeStoreInfoDTO> findAllAccountWelfareConsumeSceneDTO(String merCode);

}
