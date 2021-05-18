package com.welfare.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.service.dto.*;

import java.util.List;

/**
 * 员工消费场景配置服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountConsumeSceneService {
  public AccountConsumeScene getAccountConsumeScene(Long id);
  public Boolean save(AccountConsumeSceneAddReq accountConsumeSceneAddReq);
  public Boolean update(AccountConsumeSceneReq accountConsumeSceneReq);
  public Boolean delete(Long id);

  public Boolean updateStatus(Long id ,Integer status);

  public IPage<AccountConsumeScenePageDTO> getPageDTO(Page<AccountConsumeScenePageDTO> page,
      AccountConsumePageQuery accountConsumePageReq);

  public List<AccountConsumeScenePageDTO> export(AccountConsumePageQuery accountConsumePageReq);

  public AccountConsumeSceneDTO findAccountConsumeSceneDTOById(Long id);

  /**
   * 查询商户下所有已配置的员工类型消费门店配置
   * @param merCode
   * @return
   */
  List<AccountConsumeSceneResp> findAllAccountConsumeSceneDTO(String merCode);

  /**
   * 查询商户下所有已配置的福利类型门店配置
   * @param merCode
   * @return
   */
  List<AccountConsumeSceneResp> findAllAccountWelfareConsumeSceneDTO(String merCode);


  /**
   * 变更商户下对应员工类型的消费配置
   * @param consumeSceneEditReq
   * @return
   */
  Boolean edit(List<AccountConsumeSceneEditReq> consumeSceneEditReq);


  /**
   * 变更商户下对应福利类型的消费配置
   * @param consumeSceneEditReq
   * @return
   */
  Boolean editWelfare(List<AccountWelfareConsumeSceneEditReq> consumeSceneEditReq);
}
