package com.welfare.persist.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.MonthSettle;
import com.welfare.persist.mapper.MonthSettleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.persist.dao
 * @ClassName: OrderDao
 * @Author: jian.zhou
 * @Description: 订单Dao
 * @Date: 2021/1/9 15:45
 * @Version: 1.0
 */
@Slf4j
@Repository
public class OrderDao extends ServiceImpl<MonthSettleMapper, MonthSettle> {
}
