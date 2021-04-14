package com.welfare.service.dto.account;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.welfare.persist.dto.AccountSimpleDTO;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountTypeGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/14/2021
 */
@Data
public class AccountAmountTypeGroupDO {
    @ApiModelProperty("组内账户列表")
    private List<AccountSimpleDTO> accounts;
    /**
     * 主键
     */
    @ApiModelProperty("主键")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private Long id;
    /**
     * 账户组编码
     */
    @ApiModelProperty("账户组编码")
    private String groupCode;
    /**
     * 福利类型
     */
    @ApiModelProperty("福利类型")
    private String merAccountTypeCode;
    /**
     * 余额
     */
    @ApiModelProperty("余额")
    private BigDecimal balance;
}
