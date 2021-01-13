package com.welfare.persist.dto;

import java.util.Date;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/13 5:26 PM
 */
@Data
public class CardInfoDTO {
  /**
   * 卡片id
   */
  private String cardId;
  /**
   * 卡片名称
   */
  private String cardName;
  /**
   * 卡片类型
   */
  private String cardType;
  /**
   * 卡片介质
   */
  private String cardMedium;
  /**
   * 卡片状态
   */
  private Integer cardStatus;
  /**
   * 商户名称
   */
  private String merName;

  /**
   * 创建时间
   */
  private Date createTime;
  /**
   * 入库时间
   */
  private Date writtenTime;
  /**
   * 绑定时间
   */
  private Date bindTime;
  /**
   * 绑定账号
   */
  private String accountCode;
}
