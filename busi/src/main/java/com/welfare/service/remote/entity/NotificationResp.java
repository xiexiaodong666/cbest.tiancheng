package com.welfare.service.remote.entity;

import lombok.Data;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/27/2021
 */
@Data
public class NotificationResp {
    private String code;
    private Object data;
    private String msg;
    private String traceId;
    private Object failureData;
}
