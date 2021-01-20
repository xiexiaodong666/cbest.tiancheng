package com.welfare.common.enums;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/15 4:50 PM
 */
public enum CardApplyMediumEnum {
  /**
   *
   */
  MAGNETIC_CARD("MAGNETIC_CARD", "磁卡"),
  IC_CARD("IC_CARD", "ic卡"),
  RFID_CARD("RFID_CARD", "rfid卡"),
  BARCODE_CARD("BARCODE_CARD", "条码卡"),
  VIRTUAL_CARD("VIRTUAL_CARD", "虚拟卡"),
  PAPER_VOUCHER("PAPER_VOUCHER", "纸券");



  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  private final String code;

  private final String desc;

  CardApplyMediumEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
}
