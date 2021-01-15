package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.OrderTransRelationDao;
import com.welfare.persist.entity.OrderTransRelation;
import com.welfare.service.OrderTransRelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/15  12:11 AM
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderTransRelationServiceImpl implements OrderTransRelationService{

  private final OrderTransRelationDao orderTransRelationDao;

  @Override
  public void saveNewTransRelation(String orderId, String transNo, WelfareConstant.TransType transType) {
    OrderTransRelation orderTransRelation = new OrderTransRelation();
    orderTransRelation.setOrderId(orderId);
    orderTransRelation.setTransNo(transNo);
    orderTransRelation.setType(transType.code());
    orderTransRelationDao.save(orderTransRelation);
  }
}