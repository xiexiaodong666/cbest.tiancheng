package com.welfare.persist.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.BarcodeSalt;
import com.welfare.persist.mapper.BarcodeSaltMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 条码加盐信息(barcode_salt)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-08 18:06:33
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class BarcodeSaltDao extends ServiceImpl<BarcodeSaltMapper, BarcodeSalt> {

}