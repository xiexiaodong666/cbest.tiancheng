package com.welfare.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.*;
import com.welfare.persist.dto.OrderInfoDTO;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderBasicExtResponse;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderDetailRequest;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderDetailResponse;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderTotalRequest;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderTotalResponse;
import com.welfare.persist.dto.query.OrderPageQuery;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountMapper;
import com.welfare.persist.mapper.OrderInfoMapper;
import com.welfare.persist.mapper.SettleDetailMapper;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.MerchantStoreRelationService;
import com.welfare.service.OrderService;
import com.welfare.service.SettleDetailService;
import com.welfare.service.dto.ConsumeTypeJson;
import com.welfare.service.dto.DictReq;
import com.welfare.service.dto.OrderReqDto;
import com.welfare.service.dto.SynOrderDto;
import com.welfare.service.dto.order.ITEM2;
import com.welfare.service.dto.order.ITEM8;
import com.welfare.service.dto.order.MessageData;
import com.welfare.service.helper.QueryHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.service.impl
 * @ClassName: OrderServiceImpl
 * @Author: jian.zhou
 * @Description: 订单服务
 * @Date: 2021/1/9 17:19
 * @Version: 1.0
 */
@Slf4j
//@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderInfoDao orderDao;
    @Autowired
    private OrderInfoMapper orderMapper;
    @Autowired
    private MerchantStoreRelationService merchantStoreRelationService;
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private SupplierStoreDao supplierStoreDao;
    @Autowired
    private SettleDetailDao settleDetailDao;
    @Autowired
    private SettleDetailMapper settleDetailMapper;
    @Autowired
    private ProductInfoDao productInfoDao;
    @Autowired
    private DictDao dictDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private AccountMapper accountMapper;
    @Value("${cbest.pay.card.code:5065}")
    private String cardPayCode;
    @Value("${spring.kafka.servers:192.1.30.236:6667,192.1.30.237:6667,192.1.30.238:6667}")
    private String servers;
    @Value("${spring.kafka.group-id:welfare}")
    private String groupId;
    @Value("${spring.kafka.offset:earliest}")
    private String offsetRest;
    @Value("${spring.kafka.topic:order-info}")
    private String topic;
    @Autowired
    private SettleDetailService settleDetailService;
    @Autowired
    private MerchantBillDetailDao merchantBillDetailDao;
    @Autowired
    private MerchantCreditService merchantCreditService;
    @Autowired
    private MerchantCreditDao merchantCreditDao;

    @Override
    public Page<OrderInfoDTO> selectPage(Page page, OrderReqDto orderReqDto) {
        //根据当前用户查询所在组织的配置门店情况
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.MER_CODE, orderReqDto.getMerchantCode());
        //查询供应商下所有门店
        List<SupplierStore> supplierStoreList = supplierStoreDao.list(queryWrapper);
        List<String> supplierStoreCodeList = new ArrayList<>();
        if (supplierStoreList == null || supplierStoreList.size() < 1){
            return null;
        }else {
            supplierStoreCodeList = supplierStoreList.stream().map(item->{
                return item.getStoreCode();
            }).collect(Collectors.toList());
        }
        QueryWrapper<MerchantStoreRelation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.in( supplierStoreCodeList.size() > 0, MerchantStoreRelation.STORE_CODE , supplierStoreCodeList );

        List<MerchantStoreRelation> merchantStoreRelationList1 = merchantStoreRelationService.getMerchantStoreRelationListByMerCode(relationQueryWrapper);
        // 判断用户传入的门店集合是否在上述商户关联门店中
        List<MerchantStoreRelation> merchantStoreRelationList = new ArrayList<>();
        List<String> storeCodeList = orderReqDto.getStoreIds();
        if (storeCodeList == null) {
            //平台端调用
            merchantStoreRelationList.addAll(merchantStoreRelationList1);
        } else {
            //商户端调用
            merchantStoreRelationList1.forEach(item -> {
                String storeCode = item.getStoreCode();
                if (orderReqDto.getStoreIds() != null && orderReqDto.getStoreIds().contains(storeCode)) {
                    merchantStoreRelationList.add(item);
                }
            });
        }
        //当没有检测到有配置门店，此时不能查询到订单数据
        if (merchantStoreRelationList == null || merchantStoreRelationList.size() < 1) {
            Page<OrderInfoDTO> orderInfoPage = new Page<>();
            return orderInfoPage;
        }
        //没有配置返利门店
        List<String> noRebateStoreList = new ArrayList<>();
        //配置返利门店
        List<String> cardRebateStoreList = new ArrayList<>();
        List<String> otherRebateStoreList = new ArrayList<>();
        List<String> allRebateStoreList = new ArrayList<>();
        /**
         * 1 没有配置返利的门店  --收集到noRebateStoreList
         * 2 配置返利门店
         * 2.1 只配置了员工卡支付方式返利类型 --收集到cardRebateStoreList
         * 2.2 只配置了其他方式支付方式返利类型  --收集到otherRebateStoreList
         * 2.3 配置了员工卡支付类型和其他方式支付类型 -- 收集到allRebateStoreList
         */
        merchantStoreRelationList.forEach(item -> {
            Integer rebate = item.getIsRebate();
            if (rebate != null && rebate.intValue() == 0) {
                //不返利
                noRebateStoreList.add(item.getStoreCode());
            } else {
                String rebateType = item.getRebateType();
                if ("EMPLOYEE_CARD_NUMBER,OTHER_PAY".equals(rebateType)
                        || "OTHER_PAY,EMPLOYEE_CARD_NUMBER".equals(rebateType)) {
                    //配置了返利，此时检测是否配置类线下门店消费
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        allRebateStoreList.add(item.getStoreCode());
                    }
                } else if ("OTHER_PAY".equals(rebateType)) {
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        otherRebateStoreList.add(item.getStoreCode());
                    }
                } else if ("EMPLOYEE_CARD_NUMBER".equals(rebateType)) {
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        cardRebateStoreList.add(item.getStoreCode());
                    }
                } else {
                    noRebateStoreList.add(item.getStoreCode());
                }
            }
        });
        //构建查询条件
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        orderPageQuery.setOrderId((orderReqDto.getOrderId()));
        orderPageQuery.setAccountName((orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null : orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null : orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime((orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime((orderReqDto.getEndDateTime()));
        orderPageQuery.setAllRebateStoreList(allRebateStoreList);
        orderPageQuery.setCardRebateStoreList(cardRebateStoreList);
        orderPageQuery.setOtherRebateStoreList(otherRebateStoreList);
        orderPageQuery.setNoRebateStoreList(noRebateStoreList);
        orderPageQuery.setMerchantCode(orderReqDto.getMerchantCode());

//        Page<OrderInfoDTO> orderInfoPage = orderMapper.searchOrder(page, orderPageQuery);
//        return orderInfoPage;
        return null;
    }


    public Page<OrderInfoDTO> selectPage1(Page page, OrderReqDto orderReqDto){
        //判断是哪一端查询
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        if ("SUPPLIER".equals(orderReqDto.getType())){
            orderPageQuery.setSupplierMerCode(orderReqDto.getSupplierMerchantCode());
        }else if ("MERCHANT".equals(orderReqDto.getType())){
            //客户-判断该客户下配置的消费门店以及返利配置
            orderPageQuery.setMerchantCode(orderReqDto.getMerchantCode());
        }else{
            //平台端
            // 传入了 客户编码  供应商门店编码， 客户编码查询配置的消费门店何返利配置
            orderPageQuery.setMerchantCode(orderReqDto.getMerchantCode());
            orderPageQuery.setSupplierMerCode(orderReqDto.getSupplierMerchantCode());
        }
        // 判断用户传入的门店集合是否在上述商户关联门店中
        //构建查询条件

        orderPageQuery.setOrderId((orderReqDto.getOrderId()));
        orderPageQuery.setStoreList(orderReqDto.getStoreIds());
        orderPageQuery.setAccountName((orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null : orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null : orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime((orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime((orderReqDto.getEndDateTime()));
        orderPageQuery.setOrganizationCode(orderReqDto.getOrganizationCode());
        orderPageQuery.setAccountType(orderReqDto.getAccountType());
        orderPageQuery.setTimeInterval(orderReqDto.getTimeInterval());
        orderPageQuery.setPaymentChannel(orderReqDto.getPaymentChannel());

        Page<OrderInfoDTO> orderInfoPage = orderMapper.searchOrder(page, orderPageQuery);
        return orderInfoPage;

    }

    public OrderSummary selectSummary1(OrderReqDto orderReqDto){
        //判断是哪一端查询
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        if ("SUPPLIER".equals(orderReqDto.getType())){
            orderPageQuery.setSupplierMerCode(orderReqDto.getSupplierMerchantCode());
        }else if ("MERCHANT".equals(orderReqDto.getType())){
            //客户-判断该客户下配置的消费门店以及返利配置
            orderPageQuery.setMerchantCode(orderReqDto.getMerchantCode());
        }else{
            //平台端
            // 传入了 客户编码  供应商门店编码， 客户编码查询配置的消费门店何返利配置
            orderPageQuery.setMerchantCode(orderReqDto.getMerchantCode());
            orderPageQuery.setSupplierMerCode(orderReqDto.getSupplierMerchantCode());
        }
        // 判断用户传入的门店集合是否在上述商户关联门店中
        //构建查询条件

        orderPageQuery.setOrderId((orderReqDto.getOrderId()));
        orderPageQuery.setStoreList(orderReqDto.getStoreIds());
        orderPageQuery.setAccountName((orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null : orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null : orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime((orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime((orderReqDto.getEndDateTime()));
        orderPageQuery.setOrganizationCode(orderReqDto.getOrganizationCode());
        orderPageQuery.setAccountType(orderReqDto.getAccountType());
        orderPageQuery.setTimeInterval(orderReqDto.getTimeInterval());
        orderPageQuery.setPaymentChannel(orderReqDto.getPaymentChannel());

        OrderSummary orderSummary = orderMapper.searchOrderSum( orderPageQuery);
        return orderSummary;

    }



    @Override
    public List<OrderInfoDTO> selectList(OrderReqDto orderReqDto) {
        //根据当前用户查询所在组织的配置门店情况
        //根据当前用户查询所在组织的配置门店情况
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.MER_CODE, orderReqDto.getMerchantCode()).eq(MerchantStoreRelation.DELETED, 0);
        //查询供应商下所有门店
        List<SupplierStore> supplierStoreList = supplierStoreDao.list(queryWrapper);
        List<String> supplierStoreCodeList = new ArrayList<>();
        if (supplierStoreList == null || supplierStoreList.size() < 1){
            return null;
        }else {
            supplierStoreCodeList = supplierStoreList.stream().map(item->{
                return item.getStoreCode();
            }).collect(Collectors.toList());
        }
        QueryWrapper<MerchantStoreRelation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.in( supplierStoreCodeList.size() > 0, MerchantStoreRelation.STORE_CODE , supplierStoreCodeList );

        List<MerchantStoreRelation> merchantStoreRelationList1 = merchantStoreRelationService.getMerchantStoreRelationListByMerCode(relationQueryWrapper);
        // 判断用户传入的门店集合是否在上述商户关联门店中
        List<MerchantStoreRelation> merchantStoreRelationList = new ArrayList<>();
        List<String> storeCodeList = orderReqDto.getStoreIds();
        if (storeCodeList == null) {
            //平台端调用
            merchantStoreRelationList.addAll(merchantStoreRelationList1);
        } else {
            //商户端调用
            merchantStoreRelationList1.forEach(item -> {
                String storeCode = item.getStoreCode();
                if (orderReqDto.getStoreIds() != null && orderReqDto.getStoreIds().contains(storeCode)) {
                    merchantStoreRelationList.add(item);
                }
            });
        }
        //该商户没有配置消费场景，直接返回数据为空
        if (merchantStoreRelationList == null || merchantStoreRelationList.size() < 1) {
            return null;
        }//没有配置返利门店
        List<String> noRebateStoreList = new ArrayList<>();
        //配置返利门店
        List<String> cardRebateStoreList = new ArrayList<>();
        List<String> otherRebateStoreList = new ArrayList<>();
        List<String> allRebateStoreList = new ArrayList<>();
        /**
         * 1 没有配置返利的门店  --收集到noRebateStoreList
         * 2 配置返利门店
         * 2.1 只配置了员工卡支付方式返利类型 --收集到cardRebateStoreList
         * 2.2 只配置了其他方式支付方式返利类型  --收集到otherRebateStoreList
         * 2.3 配置了员工卡支付类型和其他方式支付类型 -- 收集到allRebateStoreList
         */
        merchantStoreRelationList.forEach(item -> {
            Integer rebate = item.getIsRebate();
            if (rebate != null && rebate.intValue() == 0) {
                //不返利
                noRebateStoreList.add(item.getStoreCode());
            } else {
                String rebateType = item.getRebateType();
                if ("EMPLOYEE_CARD_NUMBER,OTHER_PAY".equals(rebateType)
                        || "OTHER_PAY,EMPLOYEE_CARD_NUMBER".equals(rebateType)) {
                    //配置了返利，此时检测是否配置类线下门店消费
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        allRebateStoreList.add(item.getStoreCode());
                    }
                } else if ("OTHER_PAY".equals(rebateType)) {
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        otherRebateStoreList.add(item.getStoreCode());
                    }
                } else if ("EMPLOYEE_CARD_NUMBER".equals(rebateType)) {
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        cardRebateStoreList.add(item.getStoreCode());
                    }
                } else {
                    noRebateStoreList.add(item.getStoreCode());
                }
            }
        });
        //构建查询条件
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        orderPageQuery.setAccountName((orderReqDto.getOrderId()));
        orderPageQuery.setAccountName((orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null : orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null : orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime((orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime((orderReqDto.getEndDateTime()));
        orderPageQuery.setAllRebateStoreList(allRebateStoreList);
        orderPageQuery.setCardRebateStoreList(cardRebateStoreList);
        orderPageQuery.setOtherRebateStoreList(otherRebateStoreList);
        orderPageQuery.setNoRebateStoreList(noRebateStoreList);
        orderPageQuery.setMerchantCode(orderReqDto.getMerchantCode());
        orderPageQuery.setPageNo(orderReqDto.getCurrent());
        orderPageQuery.setPageSize(orderReqDto.getSize());
        orderPageQuery.setPaymentChannel(orderReqDto.getPaymentChannel());

        List<OrderInfoDTO> orderInfoList = orderMapper.searchOrder(orderPageQuery);
        return orderInfoList;
    }

    @Override
    public OrderSummary selectSummary(OrderReqDto orderReqDto) {
        //根据当前用户查询所在组织的配置门店情况
        //根据当前用户查询所在组织的配置门店情况
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SupplierStore.MER_CODE, orderReqDto.getMerchantCode()).eq(MerchantStoreRelation.DELETED, 0);
        //查询供应商下所有门店
        List<SupplierStore> supplierStoreList = supplierStoreDao.list(queryWrapper);
        List<String> supplierStoreCodeList = new ArrayList<>();
        if (supplierStoreList == null || supplierStoreList.size() < 1){
            return null;
        }else {
            supplierStoreCodeList = supplierStoreList.stream().map(item->{
                return item.getStoreCode();
            }).collect(Collectors.toList());
        }
        QueryWrapper<MerchantStoreRelation> relationQueryWrapper = new QueryWrapper<>();
        relationQueryWrapper.in( supplierStoreCodeList.size() > 0, MerchantStoreRelation.STORE_CODE , supplierStoreCodeList );

        List<MerchantStoreRelation> merchantStoreRelationList1 = merchantStoreRelationService.getMerchantStoreRelationListByMerCode(relationQueryWrapper);
        //没有配置返利门店
        // 判断用户传入的门店集合是否在上述商户关联门店中
        List<MerchantStoreRelation> merchantStoreRelationList = new ArrayList<>();
        List<String> storeCodeList = orderReqDto.getStoreIds();
        if (storeCodeList == null) {
            //平台端调用
            merchantStoreRelationList.addAll(merchantStoreRelationList1);
        } else {
            //商户端调用
            merchantStoreRelationList1.forEach(item -> {
                String storeCode = item.getStoreCode();
                if (orderReqDto.getStoreIds() != null && orderReqDto.getStoreIds().contains(storeCode)) {
                    merchantStoreRelationList.add(item);
                }
            });
        }
        //当没有检测到有配置门店，此时不能查询到订单数据
        if (merchantStoreRelationList == null || merchantStoreRelationList.size() < 1) {
            return null;
        }
        //没有配置返利门店
        List<String> noRebateStoreList = new ArrayList<>();
        //配置返利门店
        List<String> cardRebateStoreList = new ArrayList<>();
        List<String> otherRebateStoreList = new ArrayList<>();
        List<String> allRebateStoreList = new ArrayList<>();
        /**
         * 1 没有配置返利的门店  --收集到noRebateStoreList
         * 2 配置返利门店
         * 2.1 只配置了员工卡支付方式返利类型 --收集到cardRebateStoreList
         * 2.2 只配置了其他方式支付方式返利类型  --收集到otherRebateStoreList
         * 2.3 配置了员工卡支付类型和其他方式支付类型 -- 收集到allRebateStoreList
         */
        merchantStoreRelationList.forEach(item -> {
            Integer rebate = item.getIsRebate();
            if (rebate != null && rebate.intValue() == 0) {
                //不返利
                noRebateStoreList.add(item.getStoreCode());
            } else {
                String rebateType = item.getRebateType();
                if ("EMPLOYEE_CARD_NUMBER,OTHER_PAY".equals(rebateType)
                        || "OTHER_PAY,EMPLOYEE_CARD_NUMBER".equals(rebateType)) {
                    //配置了返利，此时检测是否配置类线下门店消费
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        allRebateStoreList.add(item.getStoreCode());
                    }
                } else if ("OTHER_PAY".equals(rebateType)) {
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        otherRebateStoreList.add(item.getStoreCode());
                    }
                } else if ("EMPLOYEE_CARD_NUMBER".equals(rebateType)) {
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        cardRebateStoreList.add(item.getStoreCode());
                    }
                } else {
                    noRebateStoreList.add(item.getStoreCode());
                }
            }
        });
        //构建查询条件
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        orderPageQuery.setOrderId((orderReqDto.getOrderId()));
        orderPageQuery.setAccountName((orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null : orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null : orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime((orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime((orderReqDto.getEndDateTime()));
        orderPageQuery.setAllRebateStoreList(allRebateStoreList);
        orderPageQuery.setCardRebateStoreList(cardRebateStoreList);
        orderPageQuery.setOtherRebateStoreList(otherRebateStoreList);
        orderPageQuery.setNoRebateStoreList(noRebateStoreList);
        orderPageQuery.setMerchantCode(orderReqDto.getMerchantCode());


        OrderSummary orderInfoList = orderMapper.searchOrderSum(orderPageQuery);
        return orderInfoList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncOrderData() {
        //获取所有商户数据
        List<Merchant> merchantList = merchantDao.list();
        //获取供应商下所有门店
        List<SupplierStore> supplierStores = supplierStoreDao.list();
        //获取商户消费配置场景
        QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
        List<MerchantStoreRelation> merchantStoreRelationList = merchantStoreRelationService.getMerchantStoreRelationListByMerCode(queryWrapper);
        //组装<门店编码，门店名称> 、 <门店编码 , 商户信息>
        Map<String, String> storeAndNameMap = new HashMap<>();
        Map<String, String> storeAndMerchantMap = new HashMap<>();
        supplierStores.forEach(item -> {
            storeAndNameMap.put(item.getStoreCode(), item.getStoreName());
            String merchantCode = item.getMerCode();
            merchantList.forEach(merchant -> {
                if (merchant.getMerCode().equals(merchantCode)) {
                    storeAndMerchantMap.put(item.getStoreCode(), merchantCode + "-" + merchant.getMerName());
                }
            });
        });
        //配置返利门店并且是配置了其他支付方式或者全部
        List<String> rebateStoreList = new ArrayList<>();
        List<String> noRebateStoreList = new ArrayList<>();
        merchantStoreRelationList.forEach(item -> {
            Integer rebate = item.getIsRebate();
            if (rebate != null && rebate.intValue() == 0) {
                //不返利
                noRebateStoreList.add(item.getStoreCode());
            } else {
                String rebateType = item.getRebateType();
                if ("EMPLOYEE_CARD_NUMBER,OTHER_PAY".equals(rebateType)
                        || "OTHER_PAY,EMPLOYEE_CARD_NUMBER".equals(rebateType)) {
                    //配置了返利，此时检测是否配置类线下门店消费
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        rebateStoreList.add(item.getStoreCode());
                    }
                } else if ("OTHER_PAY".equals(rebateType)) {
                    ConsumeTypeJson consumeTypeJson = JSONObject.parseObject(item.getConsumType(), ConsumeTypeJson.class);
                    if (consumeTypeJson.getSHOP_CONSUMPTION() != null && consumeTypeJson.getSHOP_CONSUMPTION().booleanValue()) {
                        //配置了线下消费场景
                        rebateStoreList.add(item.getStoreCode());
                    }
                } else {
                    noRebateStoreList.add(item.getStoreCode());
                }
            }
        });

        //支付列表
        DictReq req = new DictReq();
        req.setDictType("Pay.code");
        List<Dict> payList = dictDao.list(QueryHelper.getWrapper(req));
        Map<String, String> payMap = new HashMap<>();
        payList.forEach(item -> {
            payMap.put(item.getDictCode(), item.getDictName());
        });
        //将kafka数据写入DB
        getKafkaOrderToDB(storeAndNameMap, storeAndMerchantMap, payMap,
                rebateStoreList, noRebateStoreList);
    }

    @Override
    public int saveOrUpdateBacth(List<SynOrderDto> orderDtoList) {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        orderDtoList.forEach(item -> {
            log.info("线下订单:{}" , JSONObject.toJSONString(item));
            //根据查询账户详情
            QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(Account.ACCOUNT_CODE, item.getAccountCode());
            Account account = accountMapper.selectOne(queryWrapper);
            //根据门店查询门店名称
            QueryWrapper<SupplierStore> supplierStoreQueryWrapper = new QueryWrapper<>();
            supplierStoreQueryWrapper.eq(SupplierStore.STORE_CODE, item.getStoreCode());
            SupplierStore supplierStore = supplierStoreDao.getOne(supplierStoreQueryWrapper);
            //查询商户数据
            QueryWrapper<Merchant> merchantQueryWrapper = new QueryWrapper<>();
            merchantQueryWrapper.eq(Merchant.MER_CODE, supplierStore != null ? supplierStore.getMerCode() : null);
            Merchant merchant = merchantDao.getOne(merchantQueryWrapper);

            //构建OrdenInfo对象
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(item.getOrderId());
            orderInfo.setPayCode(cardPayCode);
            orderInfo.setGoods(item.getGoods());
            orderInfo.setPayName("员工卡");
            orderInfo.setTransType(item.getTransType());
            orderInfo.setTransTypeName(WelfareConstant.TransType.valueOf(item.getTransType().toUpperCase()).desc());
            if(WelfareConstant.TransType.REFUND.code().equals(item.getTransType())){
                //退款处理（退款订单-- trans_no为订单退单交易号，return_trans_no为退单订单流水号）
                orderInfo.setTransNo(item.getReturnTransNo());
                orderInfo.setReturnTransNo(item.getTransNo());
            }else {
                orderInfo.setTransNo(item.getTransNo());
                orderInfo.setReturnTransNo(item.getReturnTransNo());

            }
            orderInfo.setCreateTime(new Date());
            orderInfo.setOrderTime(item.getTransTime());
            orderInfo.setAccountCode(item.getAccountCode());
            orderInfo.setAccountName(account != null ? account.getAccountName() : null);
            orderInfo.setStoreCode(item.getStoreCode());
            orderInfo.setStoreName(supplierStore != null ? supplierStore.getStoreName() : null);
            orderInfo.setMerchantCode(merchant != null ? merchant.getMerCode() : null);
            orderInfo.setMerchantName(merchant != null ? merchant.getMerName() : null);
            orderInfo.setOrderAmount(item.getTransAmount());
            orderInfo.setTimeInterval(item.getTimeInterval());
            orderInfoList.add(orderInfo);
        });
        int count = orderMapper.saveOrUpdate(orderInfoList);
        return count;

    }

    @Override
    public Page<CommodityOfflineOrderTotalResponse> getCommodityOfflineOrderTotal(Page page,
        CommodityOfflineOrderTotalRequest request) {

        return orderMapper.getCommodityOfflineOrderTotal(page, request);
    }

    @Override
    public List<CommodityOfflineOrderTotalResponse> exportCommodityOfflineOrderTotal(
        CommodityOfflineOrderTotalRequest request) {
        return orderMapper.exportCommodityOfflineOrderTotal(request);
    }

    @Override
    public List<CommodityOfflineOrderDetailResponse> exportCommodityOfflineOrderDetail(
        CommodityOfflineOrderDetailRequest request) {
        return orderMapper.exportCommodityOfflineOrderDetail(request);
    }

    @Override
    public CommodityOfflineOrderBasicExtResponse getCommodityOfflineOrderTotalExt(
        CommodityOfflineOrderTotalRequest request) {

        return orderMapper.getCommodityOfflineOrderTotalExt(request);
    }

    @Override
    public Page<CommodityOfflineOrderDetailResponse> getCommodityOfflineOrderDetail(Page page,
        CommodityOfflineOrderDetailRequest request) {
        return orderMapper.getCommodityOfflineOrderDetail(page, request);
    }

    @Override
    public CommodityOfflineOrderBasicExtResponse getCommodityOfflineOrderDetailExt(
        CommodityOfflineOrderDetailRequest request) {
        return orderMapper.getCommodityOfflineOrderDetailExt(request);
    }

    /**
     * 将kafka数据写入DB
     *
     * @Return
     * @Exception
     */
    private void getKafkaOrderToDB(Map<String, String> storeAndNameMap,
                                   Map<String, String> storeAndMerchantMap,
                                   Map<String, String> payMap,
                                   List<String> rebateStoreList,
                                   List<String> noRebateStoreList) {
        //根据上面的配置，新增消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getPreperties());
        // 订阅topic-user topic
        consumer.subscribe(Collections.singletonList(topic));
        Long startTime = System.currentTimeMillis();
        List<OrderInfo> orderInfoList = new ArrayList<>();
        List<SettleDetail> settleDetailList = new ArrayList<>();
        try {
            while (true) {
                //  从服务器开始拉取数据
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                log.info("拉到kafka数据{}条", records.count());
                for (ConsumerRecord<String, String> record : records) {
                    String value = record.value();
                    if (StringUtils.isBlank(value))
                        continue;
                    MessageData messageData = JSONObject.toJavaObject(JSON.parseObject(value), MessageData.class);
                    Date orderTime = strToDate(messageData.getHeader().getBEGINTIMESTAMP() + "");
                    //线上数据不处理
                    if (isOnline(messageData.getHeader().getOPERATORID())) {
                        continue;
                    }
                    //获取订单消费明细
                    String storeCode = messageData.getHeader().getRETAILSTOREID();
                    //判断门店是否再返利配置表中，再判断该门店配置了哪类返利类型(可能同一个门店在不同的商户上面配置的返利类型不一样)
                    //从最大的返利配置开始匹配
                    //1 先配置所有方式返利门店
                    //2 在配置其他方式返利门店
                    //最后配置没有返利门店
                    if (rebateStoreList.contains(storeCode)) {
                        //该门店配置了返利门店，并且返利类型是其他支付方式和全部方式返利，此时处理所有订单(员工卡、非员工卡)
                        //处理成订单  &  支付数据
                        OrderInfo orderInfo = getOrderInfo(messageData, storeAndNameMap, storeAndMerchantMap, false);
                        if (orderInfo != null) {
//                            List<OrderInfo> list1 = new ArrayList<>();
                            orderInfoList.add(orderInfo);
//                            int i = orderMapper.saveOrUpdate(list1);
                            //明细结算
                            List<SettleDetail> settleDetails = getSettleDetail(messageData, storeAndNameMap,
                                    storeAndMerchantMap, payMap);
                            if (settleDetails != null && settleDetails.size() > 0)
                                settleDetailList.addAll(settleDetails);
//                            boolean b = settleDetailDao.saveBatch(settleDetailList);
                        }
                    } else if (noRebateStoreList.contains(storeCode)) {
                        //该门店没有配置返利，只处理员工卡消费小票
                        OrderInfo orderInfo = getOrderInfo(messageData, storeAndNameMap, storeAndMerchantMap, true);
                        if (orderInfo != null)
                            orderInfoList.add(orderInfo);
//                        {
//                            List<OrderInfo> list1 = new ArrayList<>();
//                            list1.add(orderInfo);
//                            int i = orderMapper.saveOrUpdate(list1);
//                        }
                    } else {
                        //该门店没有和任何商户配置消费场景
                    }
                }
                /**
                 * 结束本次数据同步，有下面三种情况:
                 * 1 当本次同步的订单数据量 大于 1000
                 * 2 当本次同步的结算明细数据量 大于 2000
                 * 3 当循环时间到达 5分钟时
                 */
                if (orderInfoList.size() > 1000
                        || settleDetailList.size() > 2000
                        || (System.currentTimeMillis() - startTime) > 5 * 60 * 1000) {
                    //保存订单数据
                    if (orderInfoList.size() > 0) {
                        int count = orderMapper.saveOrUpdate(orderInfoList);
                        log.info("kafka订单数据保存到数据库{}条", count);
                    } else {
                        log.info("kafka订单中没有满足条件的数据");
                    }
                    //保存结算明细数据
                    if (settleDetailList.size() > 0) {
                        Map<String, List<SettleDetail>> detailsGroupByMer = settleDetailList.stream()
                                .collect(Collectors.groupingBy(SettleDetail::getMerCode));
                        detailsGroupByMer.forEach((merCode, settleDetails) -> {
                            MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merCode);
                            List<MerchantBillDetail> merchantBillDetails =
                                    settleDetailService.rebateAndOrderNoCalculate(merchantCredit, settleDetails);
                            List<String> rebateTransNos = merchantBillDetails.stream().map(MerchantBillDetail::getTransNo).collect(Collectors.toList());
                            if(!CollectionUtils.isEmpty(merchantBillDetails)){
                                //返利需要幂等，先删除相关记录，再重新保存。
                                merchantBillDetailDao.deleteByTransNoAndBalanceType(rebateTransNos, WelfareConstant.MerCreditType.REBATE_LIMIT.code());
                                merchantBillDetailDao.saveBatch(merchantBillDetails);
                            }
                            merchantCreditDao.updateById(merchantCredit);

                            boolean flag = settleDetailDao.saveOrUpdateBatch(settleDetailList);
                            log.info("kafka明细结算数据保存到数据库{}条{}", settleDetailList.size(), flag ? "成功" : "失败");
                        });

                    } else {
                        log.info("kafka明细结算数据中没有满足条件的数据");
                    }
                    consumer.commitAsync();
                    break;
                }
            }
        } catch (Exception ex) {
            log.error("解析小票异常:", ex);
            throw ex;
        } finally {
            consumer.close();
        }
    }

    /**
     * @param messageData
     * @param storeAndNameMap
     * @param storeAndMerchantMap
     * @param payMap
     * @Return
     * @Exception
     */
    private List<SettleDetail> getSettleDetail(MessageData messageData, Map<String, String> storeAndNameMap,
                                               Map<String, String> storeAndMerchantMap, Map<String, String> payMap) {
        List<ITEM8> item8List = messageData.getItem8List();

        List<SettleDetail> resultList = new ArrayList<>();
        item8List.forEach(item8 -> {
            //排除员工卡支付交易
            if (!cardPayCode.equals(item8.getTENDERTYPECODE())) {
                SettleDetail settleDetail = new SettleDetail();
                //这个订单金额
                BigDecimal amount = getOrderAmount(messageData.getItem8List());
                String storeCode = messageData.getHeader().getRETAILSTOREID();
                Long orderTime = messageData.getHeader().getBEGINTIMESTAMP();
                String orderId = messageData.getHeader().getTRANSNUMBER().toString();
                settleDetail.setOrderId(orderId);
//                settleDetail.setAccountCode(null);
//                settleDetail.setAccountName(null);
//                settleDetail.setCardId(null);
                settleDetail.setTransAmount(amount);
                settleDetail.setStoreCode(storeCode);
                settleDetail.setStoreName(storeAndNameMap.get(storeCode));
                settleDetail.setMerCode(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[0]);
                settleDetail.setMerName(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[1]);
                settleDetail.setTransTime(strToDate(messageData.getHeader().getBEGINTIMESTAMP() + ""));
                //订单中是否包含员工卡支付
                settleDetail.setPayCode(item8.getTENDERTYPECODE());
                settleDetail.setPayName(payMap.get(item8.getTENDERTYPECODE()));
                ItemKV consumTypeItem = getConsumType(messageData.getHeader().getTRANSTYPECODE());
                settleDetail.setTransType(consumTypeItem.getCode());
                settleDetail.setTransTypeName(consumTypeItem.getName());
                resultList.add(settleDetail);
            }
        });

        return resultList;
    }

    /**
     * @param messageData
     * @param storeAndNameMap
     * @param storeAndMerchantMap
     * @param isCardPay           是否只处理员工卡支付订单 true-只处理员工卡  false-其他
     * @Return
     * @Exception
     */
    private OrderInfo getOrderInfo(MessageData messageData, Map<String, String> storeAndNameMap,
                                   Map<String, String> storeAndMerchantMap, boolean isCardPay) {
        OrderInfo orderInfo = null;
        if (isCardPay) {
            //只处理员工卡数据
            if (isCardPay(messageData.getItem8List())) {
                orderInfo = new OrderInfo();
                //包含员工卡支付，处理该订单
                //这个订单金额
                BigDecimal amount = getOrderAmount(messageData.getItem8List());
                List<String> productCodeList = getOrderProduct(messageData.getItem2List());
                //查询商品编码
                String product = productInfoDao.select(productCodeList);
                String storeCode = messageData.getHeader().getRETAILSTOREID();
                String orderId = messageData.getHeader().getTRANSNUMBER().toString();
                orderInfo.setOrderId(orderId);
//                orderInfo.setAccountCode(null);
//                orderInfo.setAccountName(null);
//                orderInfo.setCardId(null);
                orderInfo.setOrderAmount(amount);
                orderInfo.setGoods(product);
                orderInfo.setStoreCode(storeCode);
                orderInfo.setStoreName(storeAndNameMap.get(storeCode));
                orderInfo.setMerchantCode(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[0]);
                orderInfo.setMerchantName(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[1]);
                orderInfo.setOrderTime(strToDate(messageData.getHeader().getBEGINTIMESTAMP() + ""));
                //订单中是否包含员工卡支付
                ItemKV payTypeItem = getPayType(messageData.getItem8List());
                if (Objects.nonNull(payTypeItem)) {
                    orderInfo.setPayCode(payTypeItem.getCode());
                    orderInfo.setPayName(payTypeItem.getName());
                }
                ItemKV consumTypeItem = getConsumType(messageData.getHeader().getTRANSTYPECODE());
                orderInfo.setTransType(consumTypeItem.getCode());
                orderInfo.setTransTypeName(consumTypeItem.getName());
            }
        } else {
            orderInfo = new OrderInfo();
            //false - 处理所有数据（员工卡和非员工卡订单）
            //这个订单金额
            BigDecimal amount = getOrderAmount(messageData.getItem8List());
            List<String> productCodeList = getOrderProduct(messageData.getItem2List());
            //查询商品编码
            String product = productInfoDao.select(productCodeList);
            String storeCode = messageData.getHeader().getRETAILSTOREID();
            Long orderTime = messageData.getHeader().getBEGINTIMESTAMP();
            String orderId = messageData.getHeader().getTRANSNUMBER().toString();
            orderInfo.setOrderId(orderId);
//            orderInfo.setAccountCode(null);
//            orderInfo.setAccountName(null);
//            orderInfo.setCardId(null);
            orderInfo.setOrderAmount(amount);
            orderInfo.setGoods(product);
            orderInfo.setStoreCode(storeCode);
            orderInfo.setStoreName(storeAndNameMap.get(storeCode));
            orderInfo.setMerchantCode(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[0]);
            orderInfo.setMerchantName(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[1]);
            orderInfo.setOrderTime(strToDate(messageData.getHeader().getBEGINTIMESTAMP() + ""));
//            orderInfo.setCreateTime(new Date());
            //订单中是否包含员工卡支付
            ItemKV payTypeItem = getPayType(messageData.getItem8List());
            orderInfo.setPayCode(payTypeItem.getCode());
            orderInfo.setPayName(payTypeItem.getName());
            ItemKV consumTypeItem = getConsumType(messageData.getHeader().getTRANSTYPECODE());
            orderInfo.setTransType(consumTypeItem.getCode());
            orderInfo.setTransTypeName(consumTypeItem.getName());
        }
        return orderInfo;
    }

    /**
     * 获取支付方式(员工卡和非员工卡支付)
     *
     * @Return
     * @Exception
     */
    private ItemKV getPayType(List<ITEM8> item8List) {
        ItemKV itemKV = null;
        Set<String> payCodeSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(item8List)) {
            item8List.forEach(item8 -> {
                payCodeSet.add(item8.getTENDERTYPECODE());
            });
            if (payCodeSet.contains(cardPayCode)) {
                itemKV = new ItemKV(cardPayCode, "员工卡");

            } else {
                itemKV = new ItemKV("thrid", "其他支付方式");
            }
        }
        return itemKV;
    }

    /**
     * 判断是否包含卡支付
     *
     * @Return true-包含卡支付 false-不包含卡支付
     * @Exception
     */
    private boolean isCardPay(List<ITEM8> item8List) {
        Set<String> payCodeSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(item8List)) {
            item8List.forEach(item8 -> {
                payCodeSet.add(item8.getTENDERTYPECODE());
            });
        }
        return payCodeSet.contains(cardPayCode);
    }

    /**
     * 获取到订单商品ID
     *
     * @Return
     * @Exception
     */
    private List<String> getOrderProduct(List<ITEM2> item2List) {
        List<String> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(item2List)) {
            item2List.forEach(item2 -> {
                resultList.add(item2.getITEMID());
            });
        }
        return resultList;
    }

    /**
     * 计算订单金额
     *
     * @Return
     * @Exception
     */
    private BigDecimal getOrderAmount(List<ITEM8> item8List) {
        if (!CollectionUtils.isEmpty(item8List)) {
            return item8List.stream()
                    .map(ITEM8::getTENDERAMOUNT)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return null;
    }

    /**
     * 判断线上线下订单
     * 订单11开头为线下订单
     *
     * @Return true-线上 false-线下
     * @Exception
     */
    private boolean isOnline(String chanel) {
        if (StringUtils.isNotBlank(chanel) && chanel.startsWith("11-")) {
            return false;
        }
        return true;
    }

    /**
     * 获取消费方式
     * 富基订单 1-消费 N-团购消费  4-退款 Q-团购退款
     *
     * @Return
     * @Exception
     */
    private ItemKV getConsumType(String value) {
        ItemKV itemKV = null;
        if ("1".equals(value) || "N".equals(value)) {
            itemKV = new ItemKV(WelfareConstant.TransType.CONSUME.code(), WelfareConstant.TransType.CONSUME.desc());
        } else if ("4".equals(value) || "Q".equals(value)) {
            itemKV = new ItemKV(WelfareConstant.TransType.REFUND.code(), WelfareConstant.TransType.REFUND.desc());
        } else {
            itemKV = new ItemKV(value, "未知");
        }
        return itemKV;
    }

    private Properties getPreperties() {
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", servers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", groupId);

        // 可选设置属性

        //提交方式配置
        // 自动提交offset,每1s提交一次（提交后的消息不再消费，避免重复消费问题）
        props.put("enable.auto.commit", "false");//自动提交offset:true【PS：只有当消息提交后，此消息才不会被再次接受到】
        props.put("auto.commit.interval.ms", "1000");//自动提交的间隔

        //消费方式配置
        /**
         * earliest： 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
         * latest： 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
         * none： topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
         */
        props.put("auto.offset.reset", offsetRest);//earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费

        //拉取消息设置
        props.put("max.poll.records", "100");//每次poll操作最多拉取多少条消息（一般不主动设置，取默认的就好）
        return props;
    }

    private Date strToDate(String datetime) {
        Date date = DateTime.of(datetime, "yyyyMMddHHmmss");
        return date;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class ItemKV {
        private String code;
        private String name;
    }
}
