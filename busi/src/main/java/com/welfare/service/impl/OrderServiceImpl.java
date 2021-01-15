package com.welfare.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.annotation.ApiUser;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.persist.dao.*;
import com.welfare.persist.dto.query.OrderPageQuery;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountMapper;
import com.welfare.persist.mapper.OrderInfoMapper;
import com.welfare.service.MerchantStoreRelationService;
import com.welfare.service.OrderService;
import com.welfare.service.dto.DictReq;
import com.welfare.service.dto.OrderReqDto;
import com.welfare.service.dto.SynOrderDto;
import com.welfare.service.dto.order.ITEM2;
import com.welfare.service.dto.order.ITEM8;
import com.welfare.service.dto.order.MessageData;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

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
    private ProductInfoDao productInfoDao;
    @Autowired
    private DictDao dictDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public Page<OrderInfo> selectPage(Page page ,OrderReqDto orderReqDto) {
        //根据当前用户查询所在组织的配置门店情况
        QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantStoreRelation.MER_CODE, orderReqDto.getMerchantCode()).eq(MerchantStoreRelation.DELETED , 0);
        List<MerchantStoreRelation> merchantStoreRelationList1 = merchantStoreRelationService.getMerchantStoreRelationListByMerCode(queryWrapper);
        //TODO 判断用户传入的门店集合是否在上述商户关联门店中
        List<MerchantStoreRelation> merchantStoreRelationList = new ArrayList<>();
        List<Integer> storeCodeList = orderReqDto.getStoreIds();
        if (storeCodeList == null){
            //平台端调用
            merchantStoreRelationList.addAll(merchantStoreRelationList1);
        }else {
            //商户端调用
            merchantStoreRelationList1.forEach(item->{
                Integer storeCode = Integer.valueOf(item.getStoreCode());
                if (orderReqDto.getStoreIds() != null && orderReqDto.getStoreIds().contains(storeCode)){
                    merchantStoreRelationList.add(item);
                }
            });
        }
        if (merchantStoreRelationList == null || merchantStoreRelationList.size() < 1){
            Page<OrderInfo> orderInfoPage = new Page<>();
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
        merchantStoreRelationList.forEach(item->{
           Integer rebate = item.getIsRebate();
           if (rebate != null && rebate.intValue() == 0){
               //不返利
                noRebateStoreList.add(item.getStoreCode());
           }else {
                String rebateType = item.getRebateType();
                if ("ALL".equals(rebateType)){
                    allRebateStoreList.add((item.getStoreCode()));
                }else if("card_pay".equals(rebateType)){
                    cardRebateStoreList.add((item.getStoreCode()));
                }else if ("OTHER".equals(rebateType)){
                    otherRebateStoreList.add((item.getStoreCode()));
                }
           }
        });
        //构建查询条件
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        orderPageQuery.setAccountName((orderReqDto.getOrderId()));
        orderPageQuery.setAccountName((orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null: orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null: orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime((orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime((orderReqDto.getEndDateTime()));
        orderPageQuery.setAllRebateStoreList(allRebateStoreList);
        orderPageQuery.setCardRebateStoreList(cardRebateStoreList);
        orderPageQuery.setOtherRebateStoreList(otherRebateStoreList);
        orderPageQuery.setNoRebateStoreList(noRebateStoreList);
        orderPageQuery.setPageNo(orderReqDto.getCurrent());
        orderPageQuery.setPageSize(orderReqDto.getSize());

        Page<OrderInfo> orderInfoPage =  orderMapper.searchOrder(page , orderPageQuery);
        return orderInfoPage;
    }

    @Override
    public List<OrderInfo> selectList(OrderReqDto orderReqDto ) {
        //根据当前用户查询所在组织的配置门店情况
        QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantStoreRelation.MER_CODE, orderReqDto.getMerchantCode()).eq(MerchantStoreRelation.DELETED , 0);
        List<MerchantStoreRelation> merchantStoreRelationList1 = merchantStoreRelationService.getMerchantStoreRelationListByMerCode(queryWrapper);
        //TODO 判断用户传入的门店集合是否在上述商户关联门店中
        List<MerchantStoreRelation> merchantStoreRelationList = new ArrayList<>();
        List<Integer> storeCodeList = orderReqDto.getStoreIds();
        if (storeCodeList == null){
            //平台端调用
            merchantStoreRelationList.addAll(merchantStoreRelationList1);
        }else {
            //商户端调用
            merchantStoreRelationList1.forEach(item->{
                Integer storeCode = Integer.valueOf(item.getStoreCode());
                if (orderReqDto.getStoreIds() != null && orderReqDto.getStoreIds().contains(storeCode)){
                    merchantStoreRelationList.add(item);
                }
            });
        }
        if (merchantStoreRelationList == null || merchantStoreRelationList.size() < 1){
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
        merchantStoreRelationList.forEach(item->{
            Integer rebate = item.getIsRebate();
            if (rebate != null && rebate.intValue() == 0){
                //不返利
                noRebateStoreList.add(item.getStoreCode());
            }else {
                String rebateType = item.getRebateType();
                if ("ALL".equals(rebateType)){
                    allRebateStoreList.add((item.getStoreCode()));
                }else if("card_pay".equals(rebateType)){
                    cardRebateStoreList.add((item.getStoreCode()));
                }else if ("OTHER".equals(rebateType)){
                    otherRebateStoreList.add((item.getStoreCode()));
                }
            }
        });
        //构建查询条件
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        orderPageQuery.setAccountName((orderReqDto.getOrderId()));
        orderPageQuery.setAccountName((orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null: orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null: orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime((orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime((orderReqDto.getEndDateTime()));
        orderPageQuery.setAllRebateStoreList(allRebateStoreList);
        orderPageQuery.setCardRebateStoreList(cardRebateStoreList);
        orderPageQuery.setOtherRebateStoreList(otherRebateStoreList);
        orderPageQuery.setNoRebateStoreList(noRebateStoreList);
        orderPageQuery.setPageNo(orderReqDto.getCurrent());
        orderPageQuery.setPageSize(orderReqDto.getSize());

        List<OrderInfo> orderInfoList =  orderMapper.searchOrder(orderPageQuery);
        return orderInfoList;
    }

    @Override
    public OrderSummary selectSummary(OrderReqDto orderReqDto) {
        //根据当前用户查询所在组织的配置门店情况
        QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantStoreRelation.MER_CODE, orderReqDto.getMerchantCode()).eq(MerchantStoreRelation.DELETED , 0);
        List<MerchantStoreRelation> merchantStoreRelationList = merchantStoreRelationService.getMerchantStoreRelationListByMerCode(queryWrapper);
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
        merchantStoreRelationList.forEach(item->{
            Integer rebate = item.getIsRebate();
            if (rebate != null && rebate.intValue() == 0){
                //不返利
                noRebateStoreList.add(item.getStoreCode());
            }else {
                String rebateType = item.getRebateType();
                if ("ALL".equals(rebateType)){
                    allRebateStoreList.add((item.getStoreCode()));
                }else if("card_pay".equals(rebateType)){
                    cardRebateStoreList.add((item.getStoreCode()));
                }else if ("OTHER".equals(rebateType)){
                    otherRebateStoreList.add((item.getStoreCode()));
                }
            }
        });
        //构建查询条件
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        orderPageQuery.setAccountName((orderReqDto.getOrderId()));
        orderPageQuery.setAccountName((orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null: orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null: orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime((orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime((orderReqDto.getEndDateTime()));
        orderPageQuery.setAllRebateStoreList(allRebateStoreList);
        orderPageQuery.setCardRebateStoreList(cardRebateStoreList);
        orderPageQuery.setOtherRebateStoreList(otherRebateStoreList);
        orderPageQuery.setNoRebateStoreList(noRebateStoreList);


        OrderSummary orderInfoList =  orderMapper.searchOrderSum(orderPageQuery);
        return orderInfoList;
    }

    @Override
    public void syncOrderData() {
        //获取所有商户数据
        List<Merchant> merchantList = merchantDao.list();
        //获取供应商下所有门店
        QueryWrapper<SupplierStore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("deleted" , 0);
        List<SupplierStore> supplierStores = supplierStoreDao.list(queryWrapper);
        //获取商户消费配置场景
        QueryWrapper<MerchantStoreRelation> queryWrapper1 = new QueryWrapper<>();
        List<MerchantStoreRelation> merchantStoreRelationList = merchantStoreRelationService.getMerchantStoreRelationListByMerCode(queryWrapper1);
        //组装<门店编码，门店名称> 、 <门店编码 , 商户信息>
        Map<String , String> storeAndNameMap = new HashMap<>();
        Map<String, String> storeAndMerchantMap = new HashMap<>();
        supplierStores.forEach(item->{
            storeAndNameMap.put(item.getStoreCode() , item.getStoreName());
            String merchantCode = item.getMerCode();
            merchantList.forEach(merchant->{
                if (merchant.getMerCode().equals(merchantCode)){
                    storeAndMerchantMap.put(item.getStoreCode() , merchantCode + "-" +merchant.getMerName());
                }
            });
        });
        //配置返利门店并且是配置了其他支付方式或者全部
        List<String> rebateStoreList = new ArrayList<>();
        List<String> noRebateStoreList = new ArrayList<>();
        merchantStoreRelationList.forEach(item->{
            Integer rebate = item.getIsRebate();
            if (rebate != null && rebate.intValue() == 0){
                //不返利
                noRebateStoreList.add(item.getStoreCode());
            }else {
                String rebateType = item.getRebateType();
                if ("ALL".equals(rebateType)){
                    rebateStoreList.add(item.getStoreCode());
                }else if ("OTHER".equals(rebateType)){
                    rebateStoreList.add(item.getStoreCode());
                }else {
                    noRebateStoreList.add(item.getStoreCode());
                }
            }
        });

       //支付列表
        DictReq req = new DictReq();
        req.setDictType("Pay.code");
        List<Dict> payList = dictDao.list(QueryHelper.getWrapper(req));
        Map<String ,String> payMap = new HashMap<>();
        payList.forEach(item->{
            payMap.put(item.getDictCode() , item.getDictName());
        });

        getKafkaOrder(storeAndNameMap , storeAndMerchantMap , payMap , rebateStoreList , noRebateStoreList);
    }

    @Override
    public void saveOrUpdateBacth(List<SynOrderDto> orderDtoList) {
        List<OrderInfo> orderInfoList = new ArrayList<>();
        orderDtoList.forEach(item->{
            //TODO 根据查询账户详情
            QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(Account.ACCOUNT_CODE , item.getAccountCode());
            Account account = accountMapper.selectOne(queryWrapper);
            //根据门店查询门店名称
            QueryWrapper<SupplierStore> supplierStoreQueryWrapper = new QueryWrapper<>();
            supplierStoreQueryWrapper.eq(SupplierStore.STORE_CODE , item.getStoreCode());
            SupplierStore supplierStore = supplierStoreDao.getOne(supplierStoreQueryWrapper);
            //查询商户数据
            QueryWrapper<Merchant> merchantQueryWrapper = new QueryWrapper<>();
            merchantQueryWrapper.eq(Merchant.MER_CODE , supplierStore != null ? supplierStore.getMerCode():null);
            Merchant merchant = merchantDao.getOne(merchantQueryWrapper);

            //构建OrdenInfo对象
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderId(item.getOrderId());
            orderInfo.setPayCode("5065");
            orderInfo.setPayName("员工卡支付");
            orderInfo.setTransType("1");
            orderInfo.setTransTypeName("消费");
            orderInfo.setCreateUser("system");
            orderInfo.setCreateTime(new Date());
//            orderInfo.setOrderTime(item.getTransTime());
//            orderInfo.setAccountCode(Integer.valueOf(item.getAccountCode()));
            orderInfo.setAccountName(account != null ? account.getAccountName():null);
            orderInfo.setStoreCode(item.getStoreCode());
            orderInfo.setStoreName(supplierStore != null ? supplierStore.getStoreName() : null);
            orderInfo.setMerchantCode(merchant != null ? merchant.getMerCode():null);
            orderInfo.setMerchantName(merchant != null ? merchant.getMerName():null);
            orderInfo.setOrderAmount(item.getTransAmount());
            orderInfoList.add(orderInfo);
        });
        int count = orderMapper.saveOrUpdate(orderInfoList);
        System.out.println(count);

    }


    private List<MessageData> getKafkaOrder(Map<String , String > storeAndNameMap ,
                                            Map<String , String> storeAndMerchantMap,
                                            Map<String , String> payMap ,
                                            List<String> rebateStoreList,
                                            List<String> noRebateStoreList) {
        List<MessageData> list = new ArrayList<>();
        //根据上面的配置，新增消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(getPreperties());
        // 订阅topic-user topic
        consumer.subscribe(Collections.singletonList("order_info"));
        while (true) {
            //  从服务器开始拉取数据
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records){
                String value = record.value();
                MessageData messageData = JSONObject.toJavaObject(JSON.parseObject(value) , MessageData.class);
                list.add(messageData);
                System.out.printf("成功消费消息：topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());
                //线上数据不处理
                if (isOnline(messageData.getHeader().getOPERATORID())){
                    continue;
                }
                //获取订单消费明细
                String storeCode = messageData.getHeader().getRETAILSTOREID();
                //判断门店是否再返利配置表中，再判断该门店配置了哪类返利类型(可能同一个门店在不同的商户上面配置的返利类型不一样)
                //从最大的返利配置开始匹配
                //1 先配置所有方式返利门店
                //2 在配置其他方式返利门店
                //最后配置没有返利门店
                if (rebateStoreList.contains(storeCode)){
                    //处理成订单  &  支付数据
                    OrderInfo orderInfo = getOrderInfo(messageData , storeAndNameMap , storeAndMerchantMap , false);
                    if (orderInfo != null){
                        List<OrderInfo> list1 = new ArrayList<>();
                        list1.add(orderInfo);
                        int i = orderMapper.saveOrUpdate(list1);
                        System.out.println(i);
                        List<SettleDetail> settleDetailList = getSettleDetail(messageData , storeAndNameMap ,
                                storeAndMerchantMap , payMap);
                        boolean b = settleDetailDao.saveBatch(settleDetailList);
                        System.out.println(b);
                    }
                }else if (noRebateStoreList.contains(storeCode)){
                    //该门店没有配置返利，只处理员工卡消费小票
                    OrderInfo orderInfo = getOrderInfo(messageData , storeAndNameMap , storeAndMerchantMap , true);
                    if (orderInfo != null){
                        List<OrderInfo> list1 = new ArrayList<>();
                        list1.add(orderInfo);
                        int i = orderMapper.saveOrUpdate(list1);
                        System.out.println(i);
                    }
                }else {
                    //该门店没有和任何商户配置消费场景
                }
            }
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
                                         Map<String, String> storeAndMerchantMap , Map<String , String> payMap) {
        List<ITEM8> item8List = messageData.getItem8List();

        List<SettleDetail> resultList = new ArrayList<>();
        item8List.forEach(item8 -> {
            //排除员工卡支付交易
            if (!"5065".equals(item8.getTENDERTYPECODE())){
                SettleDetail settleDetail = new SettleDetail();
                //这个订单金额
                BigDecimal amount = getOrderAmount(messageData.getItem8List());
                String storeCode = messageData.getHeader().getRETAILSTOREID();
                Long orderTime = messageData.getHeader().getBEGINTIMESTAMP();
                String orderId = messageData.getHeader().getTRANSNUMBER().toString();
                settleDetail.setOrderId(orderId);
                settleDetail.setAccountCode(null);
                settleDetail.setAccountName(null);
                settleDetail.setCardId(null);
                settleDetail.setTransAmount(amount);
                settleDetail.setStoreCode(storeCode);
                settleDetail.setStoreName(storeAndNameMap.get(storeCode));
                settleDetail.setMerCode(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[0]);
                settleDetail.setMerName(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[1]);
                DateTime time = new DateTime();
                time.setTime(orderTime);
                settleDetail.setTransTime(time);
                settleDetail.setCreateUser("system");
                settleDetail.setCreateTime(new Date());
                //订单中是否包含员工卡支付
                settleDetail.setPayCode(item8.getTENDERTYPECODE());
                settleDetail.setPayName(payMap.get(item8.getTENDERTYPECODE()));
                Map<String , String> consumTyppe = getConsumType(messageData.getHeader().getTRANSTYPECODE());
                settleDetail.setTransType(consumTyppe.get("code"));
                settleDetail.setTransTypeName(consumTyppe.get("name"));
                resultList.add(settleDetail);
            }
        });

        return resultList;
    }

    /**
     * @param messageData
     * @param storeAndNameMap
     * @param storeAndMerchantMap
     * @param isCardPay 是否只处理员工卡支付订单 true-只处理员工卡  false-其他
     * @Return
     * @Exception
     */
    private OrderInfo getOrderInfo(MessageData messageData , Map<String , String > storeAndNameMap ,
                                   Map<String , String> storeAndMerchantMap , boolean isCardPay) {
        OrderInfo orderInfo = null;
        if (isCardPay){
            //只处理员工卡数据
            if (isCardPay(messageData.getItem8List())){
                orderInfo = new OrderInfo();
                //包含员工卡支付，处理该订单
                //这个订单金额
                BigDecimal amount = getOrderAmount(messageData.getItem8List());
                List<String> productCodeList = getOrderProduct(messageData.getItem2List());
                //查询商品编码
                String product = productInfoDao.select(productCodeList);
                String storeCode = messageData.getHeader().getRETAILSTOREID();
                Long orderTime = messageData.getHeader().getBEGINTIMESTAMP();
                String orderId = messageData.getHeader().getTRANSNUMBER().toString();
                orderInfo.setOrderId(orderId);
                orderInfo.setAccountCode(null);
                orderInfo.setAccountName(null);
                orderInfo.setCardId(null);
                orderInfo.setOrderAmount(amount);
                orderInfo.setGoods(product);
                orderInfo.setStoreCode(storeCode);
                orderInfo.setStoreName(storeAndNameMap.get(storeCode));
                orderInfo.setMerchantCode(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[0]);
                orderInfo.setMerchantName(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[1]);
                DateTime time = new DateTime();
                time.setTime(orderTime);
//                 orderInfo.setOrderTime("");
                orderInfo.setCreateUser("system");
                orderInfo.setCreateTime(new Date());
                //订单中是否包含员工卡支付
                Map<String , String> payType = getPayType(messageData.getItem8List());
                orderInfo.setPayCode(payType.get("code"));
                orderInfo.setPayName(payType.get("name"));
                Map<String , String> consumType = getConsumType(messageData.getHeader().getTRANSTYPECODE());
                orderInfo.setTransType(consumType.get("code"));
                orderInfo.setTransTypeName(consumType.get("name"));
            }
        }else {
            //false - 处理所有数据（员工卡和非员工卡订单）
            orderInfo =new OrderInfo();
            //这个订单金额
            BigDecimal amount = getOrderAmount(messageData.getItem8List());
            List<String> productCodeList = getOrderProduct(messageData.getItem2List());
            //查询商品编码
            String product = productInfoDao.select(productCodeList);
            String storeCode = messageData.getHeader().getRETAILSTOREID();
            Long orderTime = messageData.getHeader().getBEGINTIMESTAMP();
            String orderId = messageData.getHeader().getTRANSNUMBER().toString();
            orderInfo.setOrderId(orderId);
            orderInfo.setAccountCode(null);
            orderInfo.setAccountName(null);
            orderInfo.setCardId(null);
            orderInfo.setOrderAmount(amount);
            orderInfo.setGoods(product);
            orderInfo.setStoreCode(storeCode);
            orderInfo.setStoreName(storeAndNameMap.get(storeCode));
            orderInfo.setMerchantCode(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[0]);
            orderInfo.setMerchantName(storeAndMerchantMap.get(storeCode) == null ? null : storeAndMerchantMap.get(storeCode).split("-")[1]);
            DateTime time = new DateTime();
            time.setTime(orderTime);
//            orderInfo.setOrderTime("");
            orderInfo.setCreateUser("system");
            orderInfo.setCreateTime(new Date());
            //订单中是否包含员工卡支付
            Map<String , String> payType = getPayType(messageData.getItem8List());
            orderInfo.setPayCode(payType.get("code"));
            orderInfo.setPayName(payType.get("name"));
            Map<String , String> consumType = getConsumType(messageData.getHeader().getTRANSTYPECODE());
            orderInfo.setTransType(consumType.get("code"));
            orderInfo.setTransTypeName(consumType.get("name"));
        }
        return orderInfo;
    }

    private Map<String, String> getPayType(List<ITEM8> item8List) {
        Map<String , String > payType = new HashMap<>();
        if (item8List != null && item8List.size()>0){
            item8List.forEach(item8 -> {
                if ("5065".equals(item8.getTENDERTYPECODE())){
                    payType.put("code", "5065");
                    payType.put("name", "员工卡");
                }
            });
            if (payType.size() < 3){
                payType.put("code","other");
                payType.put("name","其他支付方式");
            }
        }
        return payType;
    }
    /**
     * 判断是否包含卡支付
     * @Return true-包含卡支付 false-不包含卡支付
     * @Exception 
     */
    private boolean isCardPay(List<ITEM8> item8List) {
        Set<String> payCodeSet = new HashSet<>();
        if (item8List != null && item8List.size()>0){
            item8List.forEach(item8 -> {
                payCodeSet.add(item8.getTENDERTYPECODE());
            });
        }
        return payCodeSet.contains("5065");
    }

    /**
     * 获取到订单商品
     * @Return
     * @Exception 
     */
    private List<String> getOrderProduct(List<ITEM2> item2List) {
        List<String> resultList = new ArrayList<>();
        if (item2List != null && item2List.size() > 0) {
            item2List.forEach(item2 -> {
                resultList.add(item2.getITEMID());
            });
        }
        return resultList;
    }

    /**
     * 计算订单金额
     * @Return
     * @Exception 
     */
    private BigDecimal getOrderAmount(List<ITEM8> item8List) {
        if (item8List != null && item8List.size() > 0){
            double amount = item8List.stream().mapToDouble(item8 -> item8.getTENDERAMOUNT().doubleValue()).sum();
            return new BigDecimal(amount);
        }
        return null;
    }

    /**
     * 判断线上线下订单
     * @Return
     * @Exception 
     */
    private boolean isOnline(String chanel){
        if (StringUtils.isNotBlank(chanel) && chanel.startsWith("11-")){
            return false;
        }
        return true;
    }

    private Map<String , String> getConsumType(String value){
        Map<String , String> resultMap = new HashMap<>();
        if ("1".equals(value) || "N".equals(value)){
            resultMap.put("code" , value);
            resultMap.put("name" , "消费");
        }else if("4".equals(value) || "Q".equals(value)){
            resultMap.put("code" , value);
            resultMap.put("name" , "退货");
        }else {
            resultMap.put("code" , value);
            resultMap.put("name" , "未知");
        }
        return resultMap;
    }

    /**
     * 添加presto查询条件前后缀
     * @Return
     * @Exception 
     */
    private String addPrefixAndSuffix(String value){
        if (StringUtils.isNotBlank(value)){
            return "\'" + value + "\'";
        }else {
            return null;
        }
    }

    private Properties getPreperties(){
        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", "192.1.30.236:6667,192.1.30.237:6667,192.1.30.238:6667");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "welfare");

        // 可选设置属性

        //提交方式配置
        // 自动提交offset,每1s提交一次（提交后的消息不再消费，避免重复消费问题）
        props.put("enable.auto.commit", "true");//自动提交offset:true【PS：只有当消息提交后，此消息才不会被再次接受到】
        props.put("auto.commit.interval.ms", "1000");//自动提交的间隔

        //消费方式配置
        /**
         * earliest： 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
         * latest： 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
         * none： topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
         */
        props.put("auto.offset.reset", "earliest ");//earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费

        //拉取消息设置
        props.put("max.poll.records", "100 ");//每次poll操作最多拉取多少条消息（一般不主动设置，取默认的就好）
        return props;
    }

}
