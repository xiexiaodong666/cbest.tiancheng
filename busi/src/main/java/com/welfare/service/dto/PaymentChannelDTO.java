package com.welfare.service.dto;

import com.welfare.persist.entity.PaymentChannel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/11 5:15 下午
 */
@Data
public class PaymentChannelDTO {

  @ApiModelProperty("支付渠道编码")
  private String paymentChannelCode;

  @ApiModelProperty("支付渠道名称")
  private String paymentChannelName;

  public static List<PaymentChannelDTO> of(List<PaymentChannel> paymentChannels) {
    List<PaymentChannelDTO> list = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(paymentChannels)) {
      paymentChannels.forEach(paymentChannel -> {
        PaymentChannelDTO dto = new PaymentChannelDTO();
        dto.setPaymentChannelCode(paymentChannel.getCode());
        dto.setPaymentChannelName(paymentChannel.getName());
        list.add(dto);
      });
    }
    return list;
  }

  public static List<PaymentChannelDTO> of2(List<DictDTO> dictDTOS) {
    List<PaymentChannelDTO> list = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(dictDTOS)) {
      dictDTOS.forEach(dictDTO -> {
        PaymentChannelDTO dto = new PaymentChannelDTO();
        dto.setPaymentChannelCode(dictDTO.getDictCode());
        dto.setPaymentChannelName(dictDTO.getDictName());
        list.add(dto);
      });
    }
    return list;
  }
}
