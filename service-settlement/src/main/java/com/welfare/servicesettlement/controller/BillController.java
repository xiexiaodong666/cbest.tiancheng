package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;
import com.welfare.servicesettlement.dto.BillDetailReqDto;
import com.welfare.servicesettlement.dto.BillDetailRespDto;
import com.welfare.servicesettlement.dto.BillReqDto;
import com.welfare.servicesettlement.dto.BillRespDto;

import java.io.File;
import java.util.List;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:37 下午
 * @desc 结算账单管理接口
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/settlement/bill")
@Api(tags = "结算账单管理")
public class BillController implements IController {

    @GetMapping("/page")
    @ApiOperation("分页查询结算账单列表")
    public R<Page<BillRespDto>> pageQuery(BillReqDto billReqDto){


        Page<BillRespDto> accountPage = null;
        return success(accountPage);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询结算账单明细")
    public R<List<BillDetailRespDto>> queryBillDetail(BillDetailReqDto billDetailReqDto){

        return success(null);
    }


    @GetMapping("/{id}/export")
    @ApiOperation("结算账单明细导出")
    public void exportBillDetail(@PathVariable("id")String id){
        download(new File(""), "账单明细");
    }

    @GetMapping("/{id}/send")
    @ApiOperation("平台发送账单")
    public Object billSend(BillReqDto billReqDto){
        return success(1);
    }

    @GetMapping("/{id}/confirm")
    @ApiOperation("商户确认账单")
    public Object billConfirm(@PathVariable("id")String id){
        return success();
    }


    @GetMapping("/{id}/finish")
    @ApiOperation("平台确认账单完成")
    public Object billFinish(@PathVariable("id")String id){
        return success();
    }
}
