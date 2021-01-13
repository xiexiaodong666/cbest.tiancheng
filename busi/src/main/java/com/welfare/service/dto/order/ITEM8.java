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
public class ITEM8 implements Serializable {
    private static final long serialVersionUID = 1L;

    // 收款方式项目编号
    private Number TENDERNUMBER;
    // 收款方式
    private String TENDERTYPECODE;
    // 收款金额
    private Number TENDERAMOUNT;
    // 收单行科目
    private String ACCOUNTNUMBER;
    // 手续费
    private String REFERENCEID;

    // TODO ITEM8 2个字段名字一样,和XML对应不上

}
