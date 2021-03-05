package com.welfare.persist.dto.query;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: chengang
 * @Version: 0.0.1
 * @Date: 2021/3/3 5:10 下午
 */
@Data
@ApiModel("员工授信额度查询对象封装")
public class EmployeeSettleConsumeQuery {

    @ApiModelProperty("员工姓名")
    private String accountName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("机构编码")
    private String departmentCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("消费起始时间")
    private Date transTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("消费截止时间")
    private Date transTimeEnd;

    @ApiModelProperty("结算标志")
    private String settleFlag;
}
