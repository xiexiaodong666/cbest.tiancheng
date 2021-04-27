package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.*;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailParam;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailSummaryDTO;
import com.welfare.persist.entity.WholesalePayableSettle;
import com.welfare.persist.entity.WholesaleReceivableSettle;
import com.welfare.service.settlement.WholesaleSettlementService;
import com.welfare.servicesettlement.dto.wholesale.WholesaleSettleStatusDTO;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 2:13 下午
 */
@RestController
@RequestMapping("/settlement/wholesale")
@RequiredArgsConstructor
@Api(tags = "批发应付结算")
public class WholesalePayableSettleController implements IController {

    private final WholesaleSettlementService wholesaleSettlementService;
    private final FileUploadServiceUtil fileUploadService;

    @GetMapping("/page-payable-summary")
    @ApiOperation("分页查询平台应付未结算分组汇总")
    public R<Page<PlatformWholesaleSettleGroupDTO>> pageQueryPayableSummary(PlatformWholesalePayablePageQuery query) {
        return success(null);
    }

    @PostMapping("/export-payable-summary")
    @ApiOperation("导出平台应付未结算账单分组汇总")
    public R<String> exportPayableSummary(PlatformWholesalePayableQuery query) throws IOException {
        return R.success(fileUploadService.getFileServerUrl(null));
    }

    @GetMapping("/payable-summary")
    @ApiOperation("查询平台应付未结算账单汇总")
    public R<PlatformWholesalePayableGroupDTO> queryPayableSummary(PlatformWholesalePayableQuery query) {

        return success(null);
    }

    @PostMapping("/page-payable-details")
    @ApiOperation("查询平台应付未结算账单明细")
    public R<Page<PlatformWholesaleSettleDetailDTO>> queryReceivableDetails(@RequestBody PlatformWholesalePayableDetailPageQuery query){
     //   PageInfo<PlatformWholesaleSettleDetailDTO> pageInfo = wholesaleSettlementService.pageQueryReceivableDetails(param);
        return success(null);
    }

    @PostMapping("/payable-details-summary")
    @ApiOperation("查询平台应付未结算帐单明细汇总")
    public R<PlatformWholesaleSettleDetailSummaryDTO> queryReceivableDetailsSummary(PlatformWholesalePayableDetailQuery query){
       // PlatformWholesaleSettleDetailSummaryDTO platformWholesaleSettleDetailSummaryDTO = wholesaleSettlementService.queryReceivableDetailsSummary(param);
        return success(null);
    }

    @PostMapping("/export-payable-details")
    @ApiOperation("导出平台应付未结算账单明细")
    public R<String> exportReceivableDetails(@RequestBody PlatformWholesalePayableDetailQuery query) throws IOException {
     //   List<PlatformWholesaleSettleDetailDTO> resultList = wholesaleSettlementService.queryReceivableDetails(param);
       // String filePath = fileUploadService.uploadExcelFile(resultList, PlatformWholesaleSettleDetailDTO.class, "批发应收结算明细");
        return R.success(fileUploadService.getFileServerUrl(null));
    }

    @PostMapping("/payable")
    @ApiOperation("生成应付结算单")
    public R<WholesalePayableSettle> generateReceivableSettle(@RequestBody PlatformWholesalePayableDetailPageQuery query){
        return success(null);
    }

    @PatchMapping("/payable/status")
    @ApiOperation("更新应付结算单状态")
    public R<WholesalePayableSettle> updateReceivableStatus(@RequestBody WholesaleSettleStatusDTO wholesaleSettleStatusDTO){
             return success(null);
    }


    @GetMapping("/payable/bill/page")
    @ApiOperation("分页查询应付结算单分组列表")
    public R<Page<WholesalePayableSettleResp>> receivableBillPage(WholesalePayableSettleBillQuery query){
        return success(null);
    }

    @GetMapping("/payable/bill/{id}/page")
    @ApiOperation("分页查询某个应付结算单明细列表")
    public R<Page<WholesaleReceivableSettleDetailResp>> receivableBillDetailPage(@PathVariable("id") Long id, WholesaleReceiveSettleDetailPageQuery query){
        return success(null);
    }

    @GetMapping("/payable/bill/{id}/summary")
    @ApiOperation("查询应付结算单明细数据汇总")
    public R<PlatformWholesalePayableGroupDTO> receivableBillDetailSummary(@PathVariable("id") Long id, WholesaleReceiveSettleDetailQuery query){
        return success(null);
    }

    @GetMapping("/payable/bill/{id}/export")
    @ApiOperation("应付结算明细数据单导出")
    public R<String> receivableBillDetailExport(@PathVariable("id") Long id, WholesaleReceiveSettleDetailQuery query){
        return success(null);
    }
}
