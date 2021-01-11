package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.MonthSettleService;
import com.welfare.service.dto.MonthSettleDetailReq;
import com.welfare.service.dto.MonthSettleDetailResp;
import com.welfare.service.dto.MonthSettleReq;
import com.welfare.service.dto.MonthSettleResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:37 下午
 * @desc 结算账单管理接口
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/settlement/monthSettle")
@Api(tags = "结算账单管理")
public class MonthSettleController implements IController {

    @Autowired
    private MonthSettleService monthSettleService;


    @GetMapping("/page")
    @ApiOperation("分页查询结算账单列表")
    public R<Page<MonthSettleResp>> pageQuery(MonthSettleReq monthSettleReqDto){
        Page<MonthSettleResp> monthSettleRespDtoPage =  monthSettleService.pageQuery(monthSettleReqDto);
        return success(monthSettleRespDtoPage);
    }

    @GetMapping("/{id}")
    @ApiOperation("分页查询结算账单明细列表")
    public R<Page<MonthSettleDetailResp>> pageQueryMonthSettleDetail(MonthSettleDetailReq monthSettleDetailReqDto){
        Page<MonthSettleDetailResp>  monthSettleDetailRespDtoPage=  monthSettleService.pageQueryMonthSettleDetail(monthSettleDetailReqDto);
        return success(monthSettleDetailRespDtoPage);
    }


    @GetMapping("/{id}/export")
    @ApiOperation("结算账单明细导出")
    public void exportMonthSettleDetail(@PathVariable("id")String id){
        monthSettleService.exportMonthSettleDetail(id);
    }

    @GetMapping("/{id}/send")
    @ApiOperation("平台发送账单")
    public R monthSettleSend(@PathVariable("id")String id){
        monthSettleService.monthSettleSend(id);
        return R.success();
    }

    @GetMapping("/{id}/confirm")
    @ApiOperation("商户确认账单")
    public R monthSettleConfirm(@PathVariable("id")String id){
        monthSettleService.monthSettleConfirm(id);
        return R.success();
    }


    @GetMapping("/{id}/finish")
    @ApiOperation("平台确认账单完成")
    public Object monthSettleFinish(@PathVariable("id")String id){
        monthSettleService.monthSettleFinish(id);
        return R.success();
    }
}
