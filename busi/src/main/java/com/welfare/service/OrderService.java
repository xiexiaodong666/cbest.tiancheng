package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.OrderInfoDTO;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderBasicExtResponse;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderDetailRequest;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderDetailResponse;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderTotalRequest;
import com.welfare.persist.dto.commodityOfflineOrder.CommodityOfflineOrderTotalResponse;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.persist.entity.OrderSummary;
import com.welfare.service.dto.OrderReqDto;
import com.welfare.service.dto.SynOrderDto;

import java.util.List;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.service
 * @ClassName: OrderService
 * @Author: jian.zhou
 * @Description: 订单
 * @Date: 2021/1/9 17:15
 * @Version: 1.0
 */
public interface OrderService {

    Page<OrderInfoDTO> selectPage(Page page,OrderReqDto orderReqDto);

    List<OrderInfoDTO> selectList(OrderReqDto orderReqDto);

    OrderSummary selectSummary(OrderReqDto orderReqDto);

    /**
     * 同步重百小票数据
     * @Return
     * @Exception
     */
    void syncOrderData();
    /**
     * 保存订单
     * @Return
     * @Exception 
     */
    int saveOrUpdateBacth(List<SynOrderDto> orderDtoList);

    /**
     * 获取商品订单汇总
     * @param page
     * @param request
     * @return
     */
    Page<CommodityOfflineOrderTotalResponse> getCommodityOfflineOrderTotal(
        Page page, CommodityOfflineOrderTotalRequest request);

    /**
     * 导出商品订单汇总
     * @param request
     * @param request
     * @return
     */
    List<CommodityOfflineOrderTotalResponse> exportCommodityOfflineOrderTotal(CommodityOfflineOrderTotalRequest request);

    /**
     * 导出商品订单汇总
     * @param request
     * @param request
     * @return
     */
    List<CommodityOfflineOrderDetailResponse> exportCommodityOfflineOrderDetail(CommodityOfflineOrderDetailRequest request);


    /**
     * 获取商品订单汇总 Ext
     * @param request
     * @return
     */
    CommodityOfflineOrderBasicExtResponse getCommodityOfflineOrderTotalExt(
        CommodityOfflineOrderTotalRequest request);

    /**
     * 获取线下订单商品销售明细
     * @param page
     * @param request
     * @return
     */
    Page<CommodityOfflineOrderDetailResponse> getCommodityOfflineOrderDetail(
        Page page, CommodityOfflineOrderDetailRequest request);

    /**
     * 获取线下订单商品销售明细 Ext
     * @param request
     * @return
     */
    CommodityOfflineOrderBasicExtResponse getCommodityOfflineOrderDetailExt(
        CommodityOfflineOrderDetailRequest request);
}
