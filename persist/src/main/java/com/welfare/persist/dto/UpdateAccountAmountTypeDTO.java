package com.welfare.persist.dto;

import com.welfare.persist.entity.AccountAmountType;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/22 5:56 下午
 */
@Data
public class UpdateAccountAmountTypeDTO {

    private Long id;

    private Long accountAmountTypeGroupId;

    private Integer joinedGroup;

    private BigDecimal accountBalance;

    public static List<UpdateAccountAmountTypeDTO> of(List<AccountAmountType> accountAmountTypes) {
        List<UpdateAccountAmountTypeDTO> updateAccountAmountTypeDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accountAmountTypes)) {
            accountAmountTypes.forEach(accountAmountType -> {
                UpdateAccountAmountTypeDTO dto = new UpdateAccountAmountTypeDTO();
                dto.setId(accountAmountType.getId());
                dto.setAccountBalance(accountAmountType.getAccountBalance());
                dto.setAccountAmountTypeGroupId(accountAmountType.getAccountAmountTypeGroupId());
                if (accountAmountType.getJoinedGroup() != null && accountAmountType.getJoinedGroup()) {
                    dto.setJoinedGroup(1);
                } else {
                    dto.setJoinedGroup(0);
                }
                updateAccountAmountTypeDTOS.add(dto);
            });
        }
        return updateAccountAmountTypeDTOS;
    }
}
