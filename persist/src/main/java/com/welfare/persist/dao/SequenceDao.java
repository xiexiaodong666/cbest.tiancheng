package com.welfare.persist.dao;

import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.Sequence;
import com.welfare.persist.mapper.SequenceMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * (sequence)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-11 19:54:01
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class SequenceDao extends ServiceImpl<SequenceMapper, Sequence> {

}