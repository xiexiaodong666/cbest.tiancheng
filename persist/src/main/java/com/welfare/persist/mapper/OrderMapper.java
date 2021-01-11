package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.dto.query.OrderPageQuery;
import com.welfare.persist.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.persist.mapper
 * @ClassName: OrderMapper
 * @Author: jian.zhou
 * @Description: 订单
 * @Date: 2021/1/9 16:50
 * @Version: 1.0
 */
@Mapper
public interface OrderMapper extends BaseMapper {

    List<OrderInfo> searchOrder(@Param("orderQuery") OrderPageQuery orderPageQuery);
}
