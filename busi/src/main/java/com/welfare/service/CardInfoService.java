package com.welfare.service;


import com.welfare.persist.entity.CardInfo;

import java.util.List;

/**
 * 卡信息服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface CardInfoService {
    /**
     * 根据卡号获取卡信息
     * @param cardNo
     * @return
     */
    CardInfo getByCardNo(String cardNo);

    /**
     * 根据申请号查询出所有
     * @param applyCode
     * @param status
     * @return
     */
    List<CardInfo> listByApplyCode(String applyCode,Integer status);

    /**
     * 更新卡信息为已写入
     * @param cardInfo
     * @return
     */
    CardInfo updateWritten(CardInfo cardInfo);
}
