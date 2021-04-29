package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import com.welfare.persist.entity.WholesaleReceivableSettleDetail;
import com.welfare.persist.mapper.WholesaleReceivableSettleDetailMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (wholesale_receivable_settle_detail)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-04-13 09:55:40
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class WholesaleReceivableSettleDetailDao extends ServiceImpl<WholesaleReceivableSettleDetailMapper, WholesaleReceivableSettleDetail> {
    public List<WholesaleReceivableSettleDetail> queryByTransNo(String transNo){
        return list(Wrappers.<WholesaleReceivableSettleDetail>lambdaQuery()
                        .eq(WholesaleReceivableSettleDetail::getTransNo, transNo)
        );
    }
}