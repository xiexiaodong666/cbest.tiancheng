package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.dto.MonthSettleDetailDTO;
import com.welfare.persist.dto.SettleStatisticsInfoDTO;
import com.welfare.persist.dto.WelfareSettleDTO;
import com.welfare.persist.dto.WelfareSettleDetailDTO;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.dto.query.WelfareSettleDetailQuery;
import com.welfare.persist.dto.query.WelfareSettleQuery;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.entity.SettleDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * (settle_detail)数据Mapper
 *
 * @author kancy
 * @since 2021-01-09 15:21:12
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface SettleDetailMapper extends BaseMapper<SettleDetail> {

    /**
     * 查询账单明细（限制每次最多拉取两千条数据）
     * @param monthSettleDetailQuery
     * @return
     */
    List<MonthSettleDetailDTO> selectMonthSettleDetail(MonthSettleDetailQuery monthSettleDetailQuery);

    /**
     * 查询获取账户交易明细
     * @param params
     * @return
     */
    List<SettleDetail> getSettleDetailFromAccountDetail(Map<String, Object> params);

    /**
     * 查询商户未结算列表信息
     * @param welfareSettleQuery
     * @return
     */
    List<WelfareSettleDTO> getWelfareSettle(WelfareSettleQuery welfareSettleQuery);

    /**
     * 查询商户未结算详细信息列表
     * @param welfareSettleDetailQuery
     * @return
     */
    List<WelfareSettleDetailDTO> getSettleDetailInfo(WelfareSettleDetailQuery welfareSettleDetailQuery);

    /**
     * 查询商户未结算详细信息统计
     * @param welfareSettleDetailQuery
     * @return
     */
    Map<String, Object> getSettleDetailExtInfo(WelfareSettleDetailQuery welfareSettleDetailQuery);

    /**
     * 按条件获取结算账单
     * @param welfareSettleDetailQuery
     * @return
     */
    MonthSettle getSettleByCondition(WelfareSettleDetailQuery welfareSettleDetailQuery);

    /**
     * 查询结算统计信息
     * @param welfareSettleDetailQuery
     * @return
     */
    List<SettleStatisticsInfoDTO> getSettleStatisticsInfoByCondition(WelfareSettleDetailQuery welfareSettleDetailQuery);

    List<Long> getSettleDetailIdList(WelfareSettleDetailQuery welfareSettleDetailQuery);
}
