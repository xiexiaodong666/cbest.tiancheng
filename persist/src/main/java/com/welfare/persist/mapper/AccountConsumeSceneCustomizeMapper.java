package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.AccountConsumeScene;
import java.util.Date;
import org.apache.ibatis.annotations.Param;

public interface AccountConsumeSceneCustomizeMapper extends BaseMapper<AccountConsumeScene> {
  IPage<AccountConsumeScenePageDTO> getPageDTO(Page<AccountConsumeScenePageDTO> page,
      @Param("merCode") String merCode,
      @Param("accountTypeId")Long accountTypeId,
      @Param("status")Integer status,
      @Param("createTimeStart")Date createTimeStart,
      @Param("createTimeEnd")Date createTimeEnd);
}
