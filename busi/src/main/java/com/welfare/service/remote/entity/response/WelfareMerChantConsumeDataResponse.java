package com.welfare.service.remote.entity.response;

import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 10:13 AM
 */
@Data
public class WelfareMerChantConsumeDataResponse {

  private List<WelfareMerChantConsumeDataBaiscResponse> datas;

  private Integer isSuccess;

  private String code;

  private Boolean success;
}
