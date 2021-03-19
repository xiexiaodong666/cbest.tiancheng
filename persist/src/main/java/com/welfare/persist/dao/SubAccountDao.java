package com.welfare.persist.dao;

import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.SubAccount;
import com.welfare.persist.mapper.SubAccountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * 子账户信息(sub_account)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-03-10 15:43:18
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class SubAccountDao extends ServiceImpl<SubAccountMapper, SubAccount> {

}