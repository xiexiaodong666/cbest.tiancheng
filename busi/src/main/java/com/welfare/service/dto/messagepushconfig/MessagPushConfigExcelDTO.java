package com.welfare.service.dto.messagepushconfig;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.welfare.persist.entity.Merchant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 11:21 上午
 */
@Data
@ExcelIgnoreUnannotated
public class MessagPushConfigExcelDTO {

  @ApiModelProperty("主键")
  @ExcelProperty(value = "主键", index = 0)
  private String id;

  @ApiModelProperty("用户电话")
  @ExcelProperty(value = "用户电话", index = 1)
  private String contact;

  @ApiModelProperty("用户姓名")
  @ExcelProperty(value = "用户姓名", index = 2)
  private String contactPerson;

  @ApiModelProperty("格式：23:00")
  @ExcelProperty(value = "发送时间", index = 3)
  private String pushTimes;

  @ApiModelProperty("配置所属商户")
  @ExcelProperty(value = "配置所属商户", index = 4)
  private String merName;

  public static List<MessagPushConfigExcelDTO> of(List<MessagPushConfigContactDTO> dtos, Merchant merchant){
    List<MessagPushConfigExcelDTO> list = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(dtos)) {
      dtos.forEach(dto -> {
        MessagPushConfigExcelDTO excelDTO = new MessagPushConfigExcelDTO();
        excelDTO.setId(dto.getId());
        excelDTO.setContact(dto.getContact());
        excelDTO.setContactPerson(dto.getContactPerson());
        excelDTO.setPushTimes(String.join(";", dto.getPushTimes()));
        excelDTO.setMerName(merchant.getMerName());
        list.add(excelDTO);
      });
    }
    return list;
  }
}
