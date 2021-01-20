package com.welfare.servicemerchant.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.service.dto.AccountConsumeSceneDTO;
import com.welfare.service.dto.AccountConsumeSceneReq;
import com.welfare.servicemerchant.dto.AccountConsumeSceneResp;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountConsumeSceneConverter{
  AccountConsumeScene toEntity(AccountConsumeSceneReq accountConsumeSceneReq);

  List<AccountConsumeScene> toEntityList(List<AccountConsumeSceneReq> accountConsumeSceneReqList);

  Page<AccountConsumeScenePageDTO> toD(IPage<AccountConsumeScenePageDTO> entityList);

  AccountConsumeSceneResp toResp(AccountConsumeSceneDTO accountConsumeSceneDTO);

}
