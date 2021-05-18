package com.welfare.persist.mapper;

import com.welfare.persist.dto.query.GroupByTaxRateQuery;
import com.welfare.persist.entity.OrderInfoDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (order_info_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-04-27 15:27:24
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface OrderInfoDetailMapper extends BaseMapper<OrderInfoDetail> {

    /**
     * 根据订单号查询，根据税率分组统计
     * @param orderNos 订单号
     * @return 分组后的orderInfoDetail
     */
    List<OrderInfoDetail> queryGroupByTaxRate(List<String> orderNos);



    /**
     * 根据订单号及交易类型查询，根据税率分组统计
     * @param orderNos 订单号
     * @return 分组后的orderInfoDetail
     */
    List<OrderInfoDetail> queryByOrderIdAndTransNoGroupByTaxRate(@Param("query") List<GroupByTaxRateQuery> queries);
}
