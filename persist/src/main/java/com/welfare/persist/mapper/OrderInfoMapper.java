package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.query.OrderPageQuery;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.persist.entity.OrderSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (order_info)数据Mapper
 *
 * @author kancy
 * @since 2021-01-12 17:25:14
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    Integer saveOrUpdate(@Param("list") List<OrderInfo> list);

    Page<OrderInfo> searchOrder(Page page , @Param("orderPageQuery") OrderPageQuery orderPageQuery);

    List<OrderInfo> searchOrder(@Param("orderPageQuery") OrderPageQuery orderPageQuery);

    OrderSummary searchOrderSum(@Param("orderPageQuery") OrderPageQuery orderPageQuery);
}
