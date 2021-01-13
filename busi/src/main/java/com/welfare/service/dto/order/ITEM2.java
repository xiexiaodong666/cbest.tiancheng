package com.msxf.trans.receipt.sync.servie.protocal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ITEM2 implements Serializable {
    private static final long serialVersionUID = 1L;

    /////////////// ITEM2字段 ////////////////
    // 销售项目编号
    private Number RETAILNUMBER;
    // 销售项目类型
    private String RETAILTYPECODE;
    // 商品代码
    private String ITEMID;
    // 销售数量
    private Number RETAILQUANTITY;
    // 销售成交总金额
    private Number SALESAMOUNT;
    // 原小票号+原小票序号
    private String SERIALNUMBER;
    // 促销档期号
    private String PROMOTIONID;
    // 物权公司代码
    private String ENTRYMETHODCODE;
    // 零售原价
    private Number ACTUALUNITPRICE;
    // TODO 寄售控制 ?

    /////////////// ITEM3字段 ////////////////
    // 折扣项目编号
    private Number DISCNUMBER;
    // 折扣类型
    private String DISCTYPECODE;
    // 折扣金额
    private Number REDUCTIONAMOUNT;
    // 促销单号
    private String DISCID;
    // 促销分摊比例
    private Number BONUSBUYID;

    /////////////// ITEM4字段 ////////////////
    // 字段组
    private String FIELDGROUP;
    // 字段名
    private String FIELDNAME;
    // 字段值
    private String FIELDVALUE;

    /////////////// ITEM5字段 ////////////////
    // 供应商代码
    private String SUPPLIERID;

    /////////////// ITEM6字段 ////////////////
    // 会员项目编号
    private String LOYNUMBER;
    // 会员卡号
    private String CUSTCARDNUMBER;
    // 临时卡标记
    private String CUSTCARDTYPE;
}
