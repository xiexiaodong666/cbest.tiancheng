package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.AccountConsumeScene;

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
  public Boolean update(AccountConsumeScene accountConsumeScene);
  public Boolean delete(Long id);
}
