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
public class HEADER implements Serializable {
    private static final long serialVersionUID = 1L;
    // 门店代码
    private String RETAILSTOREID;
    // 记账日期
    private Long BUSINESSDAYDATE;
    // 交易类型
    private String TRANSTYPECODE;
    // 收款台ID
    private String WORKSTATIONID;
    // 小票号
    private Number TRANSNUMBER;
    // 开票时间
    private Long BEGINTIMESTAMP;
    // 收款时间
    private Long ENDTIMESTAMP;
    // 收款员姓名
    private String OPERATORID;
    // 营业员姓名
    private String PARTNERID;
    // TODO 业态标记 ?

    ///////////// ITEM7字段 //////////////

}
