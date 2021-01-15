package com.welfare.service.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ITEM1 implements Serializable {
    private static final long serialVersionUID = 1L;

    // TODO 来源于文档
    //private String DATAELEMENTVALUE;
    // TODO 来源于XML文件
    private String CUSTOMERDETAILS;
}
