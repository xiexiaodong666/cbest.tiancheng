package com.welfare.service.dto.nhc;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountAmountTypeGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 5:57 下午
 */
@Data
public class NhcUserMallPointDTO {

  @ApiModelProperty("用户编码")
  private String accountCode;

  @ApiModelProperty("用户积分")
  private BigDecimal mallPoint;

  public static List<NhcUserMallPointDTO> of(List<AccountAmountTypeGroup> groups, List<AccountAmountType> accountAmountTypes) {
    List<NhcUserMallPointDTO> mallPointList = new ArrayList<>();
    Map<Long, AccountAmountTypeGroup> groupMap = new HashMap<>();
    if (CollectionUtils.isNotEmpty(groups)) {
      groupMap = groups.stream().collect(Collectors.toMap(AccountAmountTypeGroup::getId, a->a));
    }
    if (CollectionUtils.isNotEmpty(accountAmountTypes)) {
      Map<Long, AccountAmountTypeGroup> finalGroupMap = groupMap;
      accountAmountTypes.forEach(accountAmountType -> {
        NhcUserMallPointDTO mallPointDTO = new NhcUserMallPointDTO();
        if (Objects.nonNull(accountAmountType.getAccountAmountTypeGroupId()) && Objects.nonNull(accountAmountType.getJoinedGroup())
                && accountAmountType.getJoinedGroup()) {
          mallPointDTO.setMallPoint(accountAmountType.getAccountBalance());
          if (finalGroupMap.containsKey(accountAmountType.getAccountAmountTypeGroupId())) {
            mallPointDTO.setMallPoint(finalGroupMap.get(accountAmountType.getAccountAmountTypeGroupId()).getBalance());
          }
        } else {
          mallPointDTO.setMallPoint(accountAmountType.getAccountBalance());
        }
        mallPointDTO.setAccountCode(String.valueOf(accountAmountType.getAccountCode()));
        mallPointList.add(mallPointDTO);
      });
    }
    return mallPointList;
  }
}
