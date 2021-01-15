package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.entity.OrderInfo;
import com.welfare.persist.entity.OrderSummary;
import com.welfare.service.OrderService;
import com.welfare.service.dto.OrderReqDto;
import com.welfare.servicesettlement.dto.OrderRespDto;
import com.welfare.servicesettlement.dto.PageVo;
import com.welfare.service.dto.SynOrderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private OrderService orderService;

    @MerchantUser
    @ApiOperation("分页查看线下订单")
    @GetMapping("page")
    public R<PageVo<OrderRespDto>> selectByPage(OrderReqDto orderReqDto){
        PageVo<OrderRespDto> resultPage = new PageVo<>();
        MerchantUserInfo merchantUserInfo = MerchantUserHolder.getMerchantUser();
        if (merchantUserInfo != null && StringUtils.isNotBlank(merchantUserInfo.getMerchantCode())){
            orderReqDto.setMerchantCode(merchantUserInfo.getMerchantCode());
        }
        Page page = new Page();
        page.setCurrent(orderReqDto.getCurrent());
        page.setSize(orderReqDto.getSize());
        Page<OrderInfo> orderPage = orderService.selectPage(page , orderReqDto);
        if (Objects.nonNull(orderPage) && orderPage.getRecords().size() > 0){
            List<OrderRespDto> respDtoList = new ArrayList<>();
            orderPage.getRecords().forEach(item->{
                OrderRespDto respDto = new OrderRespDto();
                beanCopy(item , respDto);
                respDtoList.add(respDto);
            });
            resultPage.setRecords(respDtoList);
            resultPage.setCurrent(orderPage.getCurrent());
            resultPage.setSize(orderPage.getSize());
            resultPage.setTotal(orderPage.getTotal());
            //订单汇总数据
            OrderSummary orderSummary = orderService.selectSummary(orderReqDto);
            PageVo.Ext ext1 = new PageVo.Ext();
            if (orderSummary != null){
                ext1.setAmount(orderSummary.getOrderAmount());
                ext1.setOrderNum(orderSummary.getOrderNum());
                resultPage.setExt(ext1);
            }
        }
        return success(resultPage);
    }
    @ApiOperation("同步线下订单")
    @PostMapping("sync")
    public R<String> synOrder(@RequestBody List<SynOrderDto> orderList){
        /**
         * 订单id 流水号 退单流水号 商品 账户 卡号 消费类型 消费门店 消费金额 消费时间
         */
        orderService.saveOrUpdateBacth(orderList);
        return success();
    }


    @MerchantUser
    @ApiOperation("查询所有线下订单")
    @GetMapping("select/list")
    public R<List<OrderRespDto>> selectList(OrderReqDto orderReqDto){
        MerchantUserInfo merchantUserInfo = MerchantUserHolder.getMerchantUser();
        if (merchantUserInfo != null && StringUtils.isNotBlank(merchantUserInfo.getMerchantCode())){
            orderReqDto.setMerchantCode(merchantUserInfo.getMerchantCode());
        }
        List<OrderInfo> orderInfoPage = orderService.selectList(orderReqDto);
        List<OrderRespDto> respDtoList = new ArrayList<>();

        if (Objects.nonNull(orderInfoPage)){
            orderInfoPage.forEach(item->{
                OrderRespDto respDto = new OrderRespDto();
                beanCopy(item , respDto);
                respDtoList.add(respDto);
            });

        }

        return success(respDtoList);
    }

    private void beanCopy(OrderInfo orderInfo , OrderRespDto orderRespDto){
        BeanUtils.copyProperties(orderInfo , orderRespDto);
        orderRespDto.setOrderAmount(orderInfo.getOrderAmount().toPlainString());
    }

}
