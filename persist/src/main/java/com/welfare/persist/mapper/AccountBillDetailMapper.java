package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.welfare.persist.dto.AccountBillDetailSimpleDTO;
import com.welfare.persist.dto.query.AccountBillDetailSimpleReq;
import com.welfare.persist.entity.AccountBillDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户流水明细(account_bill_detail)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface AccountBillDetailMapper extends BaseMapper<AccountBillDetail> {
    List<AccountBillDetailSimpleDTO> selectAccountBillDetailSimpleList(AccountBillDetailSimpleReq accountBillDetailSimpleReq);
}
