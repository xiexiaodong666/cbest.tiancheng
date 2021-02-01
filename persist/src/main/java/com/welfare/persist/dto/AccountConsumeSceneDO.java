package com.welfare.persist.dto;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountConsumeSceneStoreRelation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/1/2021
 */
@Data
public class AccountConsumeSceneDO implements Serializable {
    @ApiModelProperty("账户信息")
    private Account account;
    @ApiModelProperty("账户-门店消费场景关联信息")
    private List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelations;
    @ApiModelProperty("查询条件")
    private String queryInfo;
    @ApiModelProperty("查询条件类型")
    private String queryInfoType;
}
