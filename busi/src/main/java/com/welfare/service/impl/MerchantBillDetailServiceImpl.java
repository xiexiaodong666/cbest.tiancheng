package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dao.MerchantBillDetailDao;
import com.welfare.persist.entity.MerchantBillDetail;
import com.welfare.service.MerchantBillDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantBillDetailServiceImpl implements MerchantBillDetailService {

  private final MerchantBillDetailDao merchantBillDetailDao;
  @Override
  public List<MerchantBillDetail> findByTransNoAndTransType(String transNO, String tranTsype) {
    QueryWrapper<MerchantBillDetail> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantBillDetail.TRANS_NO, transNO);
    queryWrapper.eq(MerchantBillDetail.TRANS_TYPE, tranTsype);
    return merchantBillDetailDao.list(queryWrapper);
  }
}
