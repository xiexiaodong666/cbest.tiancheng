package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.service.OrderService;
import com.welfare.service.dto.OrderReqDto;
import com.welfare.servicesettlement.dto.OrderRespDto;
import com.welfare.servicesettlement.dto.PageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import jodd.util.ArraysUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ProjectName: e-welfare
 * @Package: welfare.servicesettlement.controller
 * @ClassName: OrderController
 * @Author: jian.zhou
 * @Description: 订单控制器
 * @Date: 2021/1/7 20:24
 * @Version: 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/settlement/order")
@Api(tags = "线下订单管理")
public class OrderController implements IController {

    private OrderService orderService;

    @MerchantUser
    @ApiOperation("分页查看线下订单")
    @GetMapping("page")
    public R<PageVo<OrderRespDto>> selectByPage(OrderReqDto orderReqDto){
        PageVo<OrderRespDto> oderPage = new PageVo<>();
        OrderRespDto orderRespDto = new OrderRespDto();
        orderRespDto.setAccountCardId("C10001");

        orderRespDto.setAccountCode("A10001");
        orderRespDto.setAccountName("张三");
        orderRespDto.setGoods("矿泉水、可乐");
        orderRespDto.setMerchantCode("cbest");
        orderRespDto.setMerchantName("重百");
        orderRespDto.setOrderAmount("3.2");
        orderRespDto.setOrderDateTime("2020-12-21 12:32:22");
        orderRespDto.setOrderId("2020102125");
        orderRespDto.setStoreCode("1001");
        orderRespDto.setStoreName("重百大楼");
        List<OrderRespDto> list = new ArrayList<>();
        list.add(orderRespDto);
        oderPage.setTotal(1);
        oderPage.setSize(orderReqDto.getPageSize());
        oderPage.setCurrent(orderReqDto.getPageNo());
        oderPage.setRecords(list);
        PageVo.Ext ext = new PageVo.Ext();
        ext.setAmount("1000.21");
        ext.setOrderNum(100);
        oderPage.setExt(ext);

        /*MerchantUserInfo  merchantUserInfo = MerchantUserHolder.getDeptIds();
        List<OrderInfo> orderInfoList = orderService.selectList(orderReqDto , merchantUserInfo);
        PageVo.Ext ext1 = new PageVo.Ext();
        if (Objects.nonNull(orderInfoList)){
            List<OrderRespDto> respDtoList = new ArrayList<>();
            orderInfoList.forEach(item->{
                OrderRespDto respDto = new OrderRespDto();
                beanCopy(item , respDto);
                respDtoList.add(respDto);
                ext1.setAmount(item.getAmount());
                ext1.setOrderNum(item.getOrderNum());
                oderPage.setSize(item.getOrderNum());
            });
            oderPage.setRecords(respDtoList);
            oderPage.setExt(ext1);

        }*/

        return success(oderPage);
    }

    private void beanCopy(OrderInfo orderInfo , OrderRespDto orderRespDto){
        BeanUtils.copyProperties(orderInfo , orderRespDto);
    }

}
