package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.servicesettlement.dto.OrderReqDto;
import com.welfare.servicesettlement.dto.OrderRespDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @MerchantUser
    @ApiOperation("分页查看线下订单")
    @GetMapping("page")
    public R<Page<OrderRespDto>> selectByPage(OrderReqDto orderReqDto){
        Page<OrderRespDto> oderPage = new Page<>();
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
        oderPage.setSize(1);
        oderPage.setCurrent(1);
        oderPage.setRecords(list);

        MerchantUserInfo  merchantUserInfo = MerchantUserHolder.getDeptIds();
        //TODO 查询商户的配置门店表
        //TODO 拼装条件查询
        return success(oderPage);
    }
}
