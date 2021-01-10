package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.util.StringUtil;
import com.welfare.persist.dao.OrderDao;
import com.welfare.persist.dto.query.OrderPageQuery;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.persist.mapper.OrderMapper;
import com.welfare.service.MerchantStoreRelationService;
import com.welfare.service.OrderService;
import com.welfare.service.dto.OrderReqDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    private final OrderMapper orderMapper;
    private final MerchantStoreRelationService merchantStoreRelationService;

    @Override
    public List<OrderInfo> selectList(OrderReqDto orderReqDto , MerchantUserInfo merchantUserInfo) {
        //根据当前用户查询所在组织的配置门店情况
        QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantStoreRelation.MER_CODE, merchantUserInfo.getMerchantCode()).eq(MerchantStoreRelation.DELETED , 0);
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
                if ("all".equals(rebateType)){
                    allRebateStoreList.add(addPrefixAndSuffix(item.getStoreCode()));
                }else if("card_pay".equals(rebateType)){
                    cardRebateStoreList.add(addPrefixAndSuffix(item.getStoreCode()));
                }else if ("other_pay".equals(rebateType)){
                    otherRebateStoreList.add(addPrefixAndSuffix(item.getStoreCode()));
                }
           }
        });
        //构建查询条件
        OrderPageQuery orderPageQuery = new OrderPageQuery();
        orderPageQuery.setAccountName(addPrefixAndSuffix(orderReqDto.getOrderId()));
        orderPageQuery.setAccountName(addPrefixAndSuffix(orderReqDto.getConsumerName()));
        orderPageQuery.setLowPrice(orderReqDto.getLowPrice() == null ? null: orderReqDto.getLowPrice().toPlainString());
        orderPageQuery.setHighPrice(orderReqDto.getHightPrice() == null ? null: orderReqDto.getHightPrice().toPlainString());
        orderPageQuery.setStartDateTime(addPrefixAndSuffix(orderReqDto.getStartDateTime()));
        orderPageQuery.setEndDateTime(addPrefixAndSuffix(orderReqDto.getEndDateTime()));
        orderPageQuery.setAllRebateStoreList(allRebateStoreList);
        orderPageQuery.setCardRebateStoreList(cardRebateStoreList);
        orderPageQuery.setOtherRebateStoreList(otherRebateStoreList);
        orderPageQuery.setNoRebateStoreList(noRebateStoreList);
        orderPageQuery.setPageNo(orderReqDto.getPageNo());
        orderPageQuery.setPageSize(orderReqDto.getPageSize());

        List<OrderInfo> orderInfoList = orderMapper.searchOrder(orderPageQuery);
        return orderInfoList;
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
}
