package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.exception.BizException;
import com.welfare.persist.dto.WelfareSettleSumDTO;
import com.welfare.persist.dto.WelfareSettleSummaryDTO;
import com.welfare.persist.dto.query.WelfareSettleQuery;
import com.welfare.service.SettleDetailService;
import com.welfare.service.dto.*;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
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
@RequestMapping("/settlement/welfareSettle")
@Api(tags = "平台结算账单管理")
public class WelfareSettleController implements IController {

    @Autowired
    private SettleDetailService settleDetailService;

    @Autowired
    private FileUploadServiceUtil fileUploadService;



    @GetMapping("/page")
    @ApiOperation("分页查询商户账单信息列表")
    public R<BasePageVo<WelfareSettleResp>> pageQuery(WelfareSettlePageReq welfareSettlePageReq){
        BasePageVo<WelfareSettleResp> welfareSettleRespBasePageVo =  settleDetailService.queryWelfareSettlePage(welfareSettlePageReq);
        return success(welfareSettleRespBasePageVo);

    }
    @GetMapping("/summary")
    @ApiOperation("查询商户账单信息summary")
    public R<WelfareSettleSumDTO> summary(WelfareSettleQuery welfareSettleQuery){
        WelfareSettleSumDTO welfareSettleSumDTO = settleDetailService.queryWelfareSettleSum(welfareSettleQuery);
        return success(welfareSettleSumDTO);

    }


    @PostMapping("/detail")
    @ApiOperation("分页查询商户未结算账单明细列表")
    public R<Page<WelfareSettleDetailResp>> pageQueryMonthSettleDetail(@RequestBody WelfareSettleDetailPageReq welfareSettleDetailPageReq){

        BasePageVo<WelfareSettleDetailResp> welfareSettleDetailRespBasePageVo =  settleDetailService.queryWelfareSettleDetailPage(welfareSettleDetailPageReq);

        return success(welfareSettleDetailRespBasePageVo);
    }

    @PostMapping("/detail/summary")
    @ApiOperation("查询商户未结算账单明细summary")
    public R<WelfareSettleSummaryDTO> pageQueryMonthSettleDetail(@RequestBody WelfareSettleDetailReq welfareSettleDetailReq){
        WelfareSettleSummaryDTO welfareSettleSummaryDTO =  settleDetailService.queryWelfareSettleDetailSummary(welfareSettleDetailReq);
        return success(welfareSettleSummaryDTO);
    }

    @PostMapping("/detail/export")
    @ApiOperation("未结算账单明细导出")
    public Object exportMonthSettleDetail(@RequestBody WelfareSettleDetailReq welfareSettleDetailReq, HttpServletResponse response){


        List<WelfareSettleDetailResp> welfareSettleDetailRespList = new ArrayList<>();
        List<WelfareSettleDetailResp> welfareSettleDetailRespListTemp;
        welfareSettleDetailReq.setMinId(0l);
        do {
            welfareSettleDetailRespListTemp = settleDetailService.queryWelfareSettleDetail(welfareSettleDetailReq);
            if(!welfareSettleDetailRespListTemp.isEmpty()){
                welfareSettleDetailRespList.addAll(welfareSettleDetailRespListTemp);
                welfareSettleDetailReq.setMinId(welfareSettleDetailRespListTemp.get(welfareSettleDetailRespListTemp.size()-1).getId()+1);
            }else{
                break;
            }
        }while(true);

        String path = null;
        try {
            path = fileUploadService.uploadExcelFile(
                    welfareSettleDetailRespList, WelfareSettleDetailResp.class, "应付消费明细");
        } catch (IOException e) {
            throw new BizException(null, "文件导出异常", null);
        }
        return success(fileUploadService.getFileServerUrl(path));
    }

    @PostMapping("/buildSettle")
    @ApiOperation("按条件生成结算单")
    public R<Page<WelfareSettleDetailResp>> buildSettle(@RequestBody WelfareSettleDetailReq welfareSettleDetailReq){

        settleDetailService.buildSettle(welfareSettleDetailReq);

        return success(null);
    }

    @PostMapping("/detail/exportGroupByMer")
    @ApiOperation("结算页面导出")
    public Object exportMonthSettleDetail2(@RequestBody WelfareSettleDetail2Req welfareSettleDetailReq2){


        List<WelfareSettleDetailResp> welfareSettleDetailRespList = new ArrayList<>();
        List<WelfareSettleDetailResp> welfareSettleDetailRespListTemp;
        WelfareSettleDetailReq req = new WelfareSettleDetailReq();
        req.setMinId(0L);
        req.setMerName(welfareSettleDetailReq2.getMerName());
        req.setSupplierCode(welfareSettleDetailReq2.getSupplierCode());
        req.setStartTime(welfareSettleDetailReq2.getStartTime());
        req.setEndTime(welfareSettleDetailReq2.getEndTime());
        req.setMerCooperationMode(welfareSettleDetailReq2.getMerCooperationMode());
        do {
            welfareSettleDetailRespListTemp = settleDetailService.queryWelfareSettleDetail(req);
            if(!welfareSettleDetailRespListTemp.isEmpty()){
                welfareSettleDetailRespList.addAll(welfareSettleDetailRespListTemp);
                req.setMinId(welfareSettleDetailRespListTemp.get(welfareSettleDetailRespListTemp.size()-1).getId()+1);
            }else{
                break;
            }
        }while(true);

        String path = null;
        try {
            path = fileUploadService.uploadExcelFile(
                    WelfareSettleDetailExcelDTO.of(welfareSettleDetailRespList), WelfareSettleDetailExcelDTO.class, "应收消费明细");
        } catch (IOException e) {
            throw new BizException(null, "文件导出异常", null);
        }
        return success(fileUploadService.getFileServerUrl(path));
    }
}
