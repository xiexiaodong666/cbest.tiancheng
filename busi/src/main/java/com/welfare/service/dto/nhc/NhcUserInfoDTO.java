package com.welfare.service.dto.nhc;

import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountAmountTypeGroup;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.NhcService;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 9:53 上午
 */
@Data
public class NhcUserInfoDTO {

  @ApiModelProperty(value = "用户名称")
  private String userName;

  @ApiModelProperty("用户编码")
  private String accountCode;

  @ApiModelProperty(value = "商户CODE")
  private String merCode;

  @ApiModelProperty(value = "商户名称")
  private String merName;

  @ApiModelProperty(value = "用户手机号码")
  private String phone;

  @ApiModelProperty(value = "积分")
  private BigDecimal mallPoint;

  public static NhcUserInfoDTO of(AccountAmountTypeGroup group, Account account, AccountAmountType accountAmountType, Merchant merchant) {
    BizAssert.notNull(account, ExceptionCode.ILLEGALITY_ARGURMENTS, "员工不存在");
    BizAssert.notNull(merchant, ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在");

    NhcUserInfoDTO userInfoDTO = new NhcUserInfoDTO();
    userInfoDTO.setUserName(account.getAccountName());
    userInfoDTO.setAccountCode(String.valueOf(account.getAccountCode()));
    userInfoDTO.setMerCode(account.getMerCode());
    userInfoDTO.setMerName(merchant.getMerName());
    if (StringUtils.isNoneBlank(account.getPhone()) && !account.getPhone().startsWith(NhcService.DEFAULT_PHONE_PREFIX)) {
      userInfoDTO.setPhone(account.getPhone());
    }
    if (Objects.nonNull(group)) {
      userInfoDTO.setMallPoint(group.getBalance());
    } else {
      userInfoDTO.setMallPoint(accountAmountType != null ? accountAmountType.getAccountBalance() : BigDecimal.ZERO);
    }
    return userInfoDTO;
  }

  public static List<NhcUserInfoDTO> of(AccountAmountTypeGroup group , Map<Long, Account> accountMap, Map<Long, AccountAmountType> accountAmountTypes, Merchant merchant) {
    List<NhcUserInfoDTO> list = new ArrayList<>();
    accountMap.forEach((accountCode, account) -> {
      NhcUserInfoDTO userInfoDTO = new NhcUserInfoDTO();
      userInfoDTO.setUserName(account.getAccountName());
      userInfoDTO.setAccountCode(String.valueOf(account.getAccountCode()));
      userInfoDTO.setMerCode(account.getMerCode());
      userInfoDTO.setMerName(merchant.getMerName());
      if (StringUtils.isNoneBlank(account.getPhone()) && !account.getPhone().startsWith(NhcService.DEFAULT_PHONE_PREFIX)) {
        userInfoDTO.setPhone(account.getPhone());
      }
      AccountAmountType accountAmountType = accountAmountTypes.get(accountCode);
      if (Objects.nonNull(group)) {
        userInfoDTO.setMallPoint(group.getBalance());
      } else {
        userInfoDTO.setMallPoint(accountAmountType != null ? accountAmountType.getAccountBalance() : BigDecimal.ZERO);
      }
      list.add(userInfoDTO);
    });
    return list;
  }


}
