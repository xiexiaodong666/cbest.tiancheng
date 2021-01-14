package com.welfare.persist.mapper;

import com.welfare.persist.dto.MonthSettleDetailDTO;
import com.welfare.persist.dto.query.MonthSettleDetailQuery;
import com.welfare.persist.entity.SettleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
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
}
