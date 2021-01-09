package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountConsumeSceneMapperDTO;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.entity.AccountConsumeScene;
import java.util.Date;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface AccountConsumeSceneCustomizeMapper extends BaseMapper<AccountConsumeScene> {
  IPage<AccountConsumeScenePageDTO> getPageDTO(Page<AccountConsumeScenePageDTO> page,
      @Param("merCode") String merCode,
      @Param("accountTypeId")Long accountTypeId,
      @Param("status")Integer status,
      @Param("createTimeStart")Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);

  AccountConsumeSceneMapperDTO queryAccountConsumerScene4Detail(@Param("id")Long id);


  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  @Insert("insert  into  account_consume_scene ( id,`mer_code`, `account_type_id`, `remark`, `status`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) "
      + " values (#{id},#{merCode},#{accountTypeId},#{remark},#{status},#{createUser},#{createTime},#{updateUser},#{updateTime},#{deleted},#{version})")
  int insert(AccountConsumeScene accountConsumeScene);
}
