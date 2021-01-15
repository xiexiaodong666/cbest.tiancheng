package com.welfare.servicesettlement.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.ExcelUtil;
import com.welfare.service.MonthSettleService;
import com.welfare.service.dto.*;
import com.welfare.servicesettlement.task.SettlementBillBuildTask;
import com.welfare.servicesettlement.task.SettlementDetailDealTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private SettlementBillBuildTask settlementBillBuildTask;
    @Autowired
    private SettlementDetailDealTask settlementDetailDealTask;


    @GetMapping("/page")
    @ApiOperation("分页查询结算账单列表")
    public R<BasePageVo<MonthSettleResp>> pageQuery(MonthSettlePageReq monthSettleReqDto){
        BasePageVo<MonthSettleResp> monthSettleRespDtoPage =  monthSettleService.pageQuery(monthSettleReqDto);
        return success(monthSettleRespDtoPage);
    }


    @GetMapping("/{id}")
    @ApiOperation("分页查询结算账单明细列表")
    public R<Page<MonthSettleDetailResp>> pageQueryMonthSettleDetail(@PathVariable("id")String id, MonthSettleDetailPageReq monthSettleDetailReq){
        Page<MonthSettleDetailResp>  monthSettleDetailRespDtoPage=  monthSettleService.pageQueryMonthSettleDetail(id, monthSettleDetailReq);
        return success(monthSettleDetailRespDtoPage);
    }

    @GetMapping("/{id}/detailList")
    @ApiOperation("查询结算账单明细列表")
    public R<List<MonthSettleDetailResp>> queryMonthSettleDetail(@PathVariable("id")String id, MonthSettleDetailReq monthSettleDetailReq){
        List<MonthSettleDetailResp> monthSettleDetailResps =  monthSettleService.queryMonthSettleDetailLimit(id, monthSettleDetailReq);
        return success(monthSettleDetailResps);
    }


    @GetMapping("/{id}/export")
    @ApiOperation("结算账单明细导出")
    public void exportMonthSettleDetail(@PathVariable("id")String id, MonthSettleDetailReq monthSettleDetailReq, HttpServletResponse response){
        List<MonthSettleDetailResp> monthSettleDetailResps = new ArrayList<>();
        List<MonthSettleDetailResp> monthSettleDetailRespsTemp;
        do {
            monthSettleDetailRespsTemp = monthSettleService.queryMonthSettleDetailLimit(id, monthSettleDetailReq);
            if(!monthSettleDetailRespsTemp.isEmpty()){
                monthSettleDetailResps.addAll(monthSettleDetailRespsTemp);
                monthSettleDetailReq.setMinId(monthSettleDetailRespsTemp.get(monthSettleDetailRespsTemp.size()-1).getId()+1);
            }else{
                break;
            }
        }while(true);

        try {
            ExcelUtil.export(response, "结算账单明细", "结算账单明细", monthSettleDetailResps, MonthSettleDetailResp.class );
        } catch (IOException e) {
            throw new BusiException(null, "文件导出异常", null);
        }
    }



    @PutMapping("/{id}/send")
    @ApiOperation("平台发送账单")
    public R monthSettleSend(@PathVariable("id")String id){
        Integer count = monthSettleService.monthSettleSend(id);
        return count == 1 ? R.success():R.fail("操作失败");

    }

    @PutMapping("/{id}/confirm")
    @ApiOperation("商户确认账单")
    public R monthSettleConfirm(@PathVariable("id")String id){
        Integer count = monthSettleService.monthSettleConfirm(id);
        return count == 1 ? R.success():R.fail("操作失败");
    }


    @PutMapping("/{id}/finish")
    @ApiOperation("平台确认账单完成")
    public R monthSettleFinish(@PathVariable("id")String id){
        Integer count = monthSettleService.monthSettleFinish(id);
        return count == 1 ? R.success():R.fail("操作失败");
    }

    @GetMapping("/settlementDetailDealTask")
    @ApiOperation("员工账户交易数据手工拉取")
    public R settlementDetailDealTask(String date){
        Map params = new HashMap<>();
        params.put("date", date);
        settlementDetailDealTask.execute(JSON.toJSONString(date));
        return R.success();
    }

    @GetMapping("/settlementBillBuildTask")
    @ApiOperation("账单数据手动生成")
    public R settlementBillBuildTask(String date){
        Map params = new HashMap<>();
        params.put("date", date);
        settlementBillBuildTask.execute(JSON.toJSONString(date));
        return R.success();
    }
}
