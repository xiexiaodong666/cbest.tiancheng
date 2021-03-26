package com.welfare.service.dto.paymentChannel;

import com.welfare.service.dto.DictDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/25 4:35 下午
 */
@Data
public class PayChannelConfigDTO {

  @ApiModelProperty("商户编码")
  private String merchantCode;

  @ApiModelProperty("商户名称")
  private String merchantName;

  @ApiModelProperty("表格头")
  private List<ConsumeType> header;

  @ApiModelProperty("行数据")
  private List<PayChannelConfigRowDTO> rows;

  @Data
  public static class ConsumeType {

    @ApiModelProperty("消费场景")
    private String consumeType;

    @ApiModelProperty("消费场景名称")
    private String consumeTypeName;

    public static List<ConsumeType> of(List<DictDTO> dtos) {
      List<ConsumeType> consumeTypes = new ArrayList<>();
      if (CollectionUtils.isNotEmpty(dtos)) {
        dtos.forEach(dto -> {
          ConsumeType consumeType = new ConsumeType();
          consumeType.setConsumeType(dto.getDictCode());
          consumeType.setConsumeTypeName(dto.getDictName());
          consumeTypes.add(consumeType);
        });
      }
      return consumeTypes;
    }
  }
}
