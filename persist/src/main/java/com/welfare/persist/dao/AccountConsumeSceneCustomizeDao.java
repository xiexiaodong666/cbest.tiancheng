package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.mapper.AccountConsumeSceneCustomizeMapper;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 17:12
 */
public class AccountConsumeSceneCustomizeDao extends
    ServiceImpl<AccountConsumeSceneCustomizeMapper, AccountConsumeScene> {
}
