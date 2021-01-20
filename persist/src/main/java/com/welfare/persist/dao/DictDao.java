package com.welfare.persist.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.Dict;
import com.welfare.persist.mapper.DictMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 字典(dict)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class DictDao extends ServiceImpl<DictMapper, Dict> {

}