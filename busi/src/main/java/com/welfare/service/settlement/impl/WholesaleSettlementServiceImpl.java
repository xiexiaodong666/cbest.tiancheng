package com.welfare.service.settlement.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.persist.dao.WholesaleReceivableSettleDao;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.mapper.WholesaleReceivableSettleMapper;
import com.welfare.service.remote.entity.PlatformUser;
import com.welfare.service.settlement.WholesaleSettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/26/2021
 */
@Service
@RequiredArgsConstructor
public class WholesaleSettlementServiceImpl implements WholesaleSettlementService {
    private final WholesaleReceivableSettleMapper wholesaleReceivableSettleMapper;

    @Override
    public PageInfo<PlatformWholesaleSettleGroupDTO> pageQueryReceivable(String merCode,
                                                                         String supplierCode,
                                                                         Date transTimeStart,
                                                                         Date transTimeEnd,
                                                                         int pageIndex,
                                                                         int pageSize){
        return PageHelper.startPage(pageIndex, pageSize).doSelectPageInfo(() -> {
            wholesaleReceivableSettleMapper.queryReceivable(merCode, supplierCode, transTimeStart, transTimeEnd);
        });
    }

    @Override
    public PlatformWholesaleSettleGroupDTO queryReceivableSummary(String merCode,
                                                                  String supplierCode,
                                                                  Date transTimeStart,
                                                                  Date transTimeEnd){
        return wholesaleReceivableSettleMapper.queryReceivableSummary(merCode, supplierCode, transTimeStart, transTimeEnd);
    }
}
