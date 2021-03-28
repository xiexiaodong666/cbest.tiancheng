package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.PayChannelConfigSimple;
import com.welfare.persist.dto.PaymentChannelConfigDetailDTO;
import com.welfare.persist.dto.query.PayChannelConfigDelReq;
import com.welfare.persist.dto.query.PayChannelConfigQuery;
import com.welfare.persist.entity.PaymentChannelConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (payment_channel_config)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-03-23 09:35:43
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface PaymentChannelConfigMapper extends BaseMapper<PaymentChannelConfig> {

  Page<PayChannelConfigSimple> simplePage(Page<PayChannelConfigSimple> page, @Param("merName") String merName);

  List<PaymentChannelConfigDetailDTO> list(@Param("query") PayChannelConfigQuery query);

}
