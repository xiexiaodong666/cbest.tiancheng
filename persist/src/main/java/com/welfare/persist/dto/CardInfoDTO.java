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

  private String cardId;
  private String cardName;
  private String cardType;
  private String cardMedium;
  private Integer cardStatus;
  private String merName;
  private Date createTime;
  private Date writtenTime;
  private Date bindTime;
  private Date accountCode;
}
