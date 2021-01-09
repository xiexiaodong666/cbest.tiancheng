package com.welfare.servicemerchant.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.dto.AccountConsumeSceneDTO;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.servicemerchant.dto.AccountConsumeSceneReq;
import com.welfare.servicemerchant.dto.AccountConsumeSceneResp;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountConsumeSceneConverter{
  AccountConsumeScene toEntity(AccountConsumeSceneReq accountConsumeSceneReq);

  List<AccountConsumeScene> toEntityList(List<AccountConsumeSceneReq> accountConsumeSceneReqList);

  Page<AccountConsumeScenePageDTO> toD(IPage<AccountConsumeScenePageDTO> entityList);

  AccountConsumeSceneResp toResp(AccountConsumeSceneDTO accountConsumeSceneDTO);

}
