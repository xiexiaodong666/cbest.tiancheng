package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.exception.BusiException;
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


    @PostMapping("/detail")
    @ApiOperation("分页查询商户未结算账单明细列表")
    public R<Page<WelfareSettleDetailResp>> pageQueryMonthSettleDetail(@RequestBody WelfareSettleDetailPageReq welfareSettleDetailPageReq){

        BasePageVo<WelfareSettleDetailResp> welfareSettleDetailRespBasePageVo =  settleDetailService.queryWelfareSettleDetailPage(welfareSettleDetailPageReq);

        return success(welfareSettleDetailRespBasePageVo);
    }

    @PostMapping("/detail/export")
    @ApiOperation("未结算账单明细导出")
    public Object exportMonthSettleDetail(@RequestBody WelfareSettleDetailReq welfareSettleDetailReq, HttpServletResponse response){


        List<WelfareSettleDetailResp> welfareSettleDetailRespList = new ArrayList<>();
        List<WelfareSettleDetailResp> welfareSettleDetailRespListTemp;
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
                    welfareSettleDetailRespList, WelfareSettleDetailReq.class, "未结算账单明细");
        } catch (IOException e) {
            throw new BusiException(null, "文件导出异常", null);
        }
        return success(fileUploadService.getFileServerUrl(path));
    }

    @PostMapping("/buildSettle")
    @ApiOperation("按条件生成结算单")
    public R<Page<WelfareSettleDetailResp>> buildSettle(@RequestBody WelfareSettleDetailReq welfareSettleDetailReq){

        settleDetailService.buildSettle(welfareSettleDetailReq);

        return success(null);
    }
}
