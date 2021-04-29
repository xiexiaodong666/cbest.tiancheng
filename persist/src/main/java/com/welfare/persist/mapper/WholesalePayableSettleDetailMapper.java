package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.PlatformPayableSettleGroupDTO;
import com.welfare.persist.dto.query.PlatformWholesalePayablePageQuery;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.entity.WholesalePayableSettleDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (wholesale_payable_settle_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-04-13 09:55:39
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface WholesalePayableSettleDetailMapper extends BaseMapper<WholesalePayableSettleDetail> {

    Page<PlatformPayableSettleGroupDTO> pageUnsettleDataGroupBySupplierMerCode(Page<PlatformPayableSettleGroupDTO> page,
                                                                               @Param("query") PlatformWholesalePayablePageQuery query);

}
