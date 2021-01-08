package com.welfare.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.AccountConsumeScene;
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
  public Boolean save(AccountConsumeScene accountConsumeScene);
  public Boolean saveList(List<AccountConsumeScene> accountConsumeSceneList);
  public Boolean update(AccountConsumeScene accountConsumeScene);
  public Boolean updateList(List<AccountConsumeScene> accountConsumeSceneList);
  public Boolean delete(Long id);

  public Boolean updateStatus(Long id ,Integer status);

  public IPage<AccountConsumeScenePageDTO> getPageDTO(Page<AccountConsumeScenePageDTO> page,
      AccountConsumePageQuery accountConsumePageReq);
}
