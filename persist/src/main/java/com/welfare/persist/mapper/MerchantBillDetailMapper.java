package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.dto.MerTransDetailDTO;
import com.welfare.persist.dto.query.MerTransDetailQuery;
import com.welfare.persist.entity.MerchantBillDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (merchant_bill_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MerchantBillDetailMapper extends BaseMapper<MerchantBillDetail> {
    List<MerTransDetailDTO> getMerTransDetail(MerTransDetailQuery merTransDetailQuery);
}
