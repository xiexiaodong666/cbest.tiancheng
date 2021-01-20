package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantCreditApplyInfoDTO;
import com.welfare.persist.dto.query.MerchantCreditApplyQueryReq;
import com.welfare.persist.entity.MerchantCreditApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商户金额申请(merchant_credit_apply)数据Mapper
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MerchantCreditApplyMapper extends BaseMapper<MerchantCreditApply> {

   /**
    * 分页查询商户额度申请主数据
    * @param page
    * @param req
    * @return
    */
   Page<MerchantCreditApplyInfoDTO> queryByPage(Page page, @Param("query") MerchantCreditApplyQueryReq req);

   /**
    * 查询商户额度申请主数据
    * @param req
    * @return
    */
   List<MerchantCreditApplyInfoDTO> queryByPage(@Param("query") MerchantCreditApplyQueryReq req);

}
