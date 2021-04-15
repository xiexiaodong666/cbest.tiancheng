package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.PayChannelMerchantDTO;
import com.welfare.persist.entity.PaymentChannel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * (payment_channel)数据Mapper
 *
 * @author kancy
 * @since 2021-03-11 17:28:32
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface PaymentChannelMapper extends BaseMapper<PaymentChannel> {


  Page<PayChannelMerchantDTO> merchantPayChannelList(@Param("merName")String merName, Page<PayChannelMerchantDTO> page);

}
