package com.welfare.service.converter;

import com.welfare.persist.entity.MerchantCreditApply;
import com.welfare.service.dto.merchantapply.MerchantCreditApplyExcelInfo;
import com.welfare.service.dto.merchantapply.MerchantCreditApplyInfo;
import com.welfare.service.dto.merchantapply.MerchantCreditApplyRequest;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/10  2:37 PM
 */
@Mapper(componentModel = "spring")
public interface MerchantCreditApplyConverter {

  MerchantCreditApplyInfo toInfo(MerchantCreditApply apply);

  List<MerchantCreditApplyInfo> toInfoList(List<MerchantCreditApply> applyList);

  MerchantCreditApply toApply(MerchantCreditApplyInfo info);

  List<MerchantCreditApply> toApplyList(List<MerchantCreditApplyInfo> infos);

  MerchantCreditApply toApply(MerchantCreditApplyRequest request);

  List<MerchantCreditApplyExcelInfo> toExcelInfoList(List<MerchantCreditApply> applyList);

  MerchantCreditApplyExcelInfo toExcelInfo(MerchantCreditApply apply);
}