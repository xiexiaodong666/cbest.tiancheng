package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.welfare.persist.dto.PlatformWholePayableSettleDTO;
import com.welfare.persist.dto.query.WholesalePayableSummaryQueryDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailParam;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailSummaryDTO;
import com.welfare.persist.entity.WholesaleReceivableSettle;
import com.welfare.service.settlement.WholesaleSettlementService;
import com.welfare.servicesettlement.dto.wholesale.WholesaleSettleStatusDTO;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 2:13 下午
 */
@RestController
@RequestMapping("/settlement/wholesale/")
@RequiredArgsConstructor
@Api(tags = "批发应付结算")
public class WholesalePayableSettleController implements IController {

    private final WholesaleSettlementService wholesaleSettlementService;
    private final FileUploadServiceUtil fileUploadService;
//
//    @PostMapping("/page-payable-summary")
//    @ApiOperation("分页查询平台应付账单分组汇总")
//    public R<Page<PlatformWholePayableSettleDTO>> pageQueryPayableSummary(@RequestBody WholesalePayableSummaryQueryDTO queryDTO) {
//
//        return success(null);
//    }
//
//    @PostMapping("/export-payable-summary")
//    @ApiOperation("导出平台应付账单分组汇总")
//    public R<String> exportPayableSummary(@RequestBody WholesalePayableSummaryQueryDTO queryDTO) throws IOException {
//        return R.success(fileUploadService.getFileServerUrl(null));
//    }
//
//    @GetMapping("/payable-summary")
//    @ApiOperation("查询平台应付账单汇总")
//    public R<PlatformWholePayableSettleGroupDTO> queryPayableSummary(@RequestBody WholesalePayableSummaryQueryDTO queryDTO) {
//
//        return success(null);
//    }
//
//    @PostMapping("/page-payable-details")
//    @ApiOperation("查询平台应付账单明细")
//    public R<PageInfo<PlatformWholesaleSettleDetailDTO>> queryReceivableDetails(@RequestBody PlatformWholesaleSettleDetailParam param){
//        PageInfo<PlatformWholesaleSettleDetailDTO> pageInfo = wholesaleSettlementService.pageQueryReceivableDetails(param);
//        return success(pageInfo);
//    }
//
//    @PostMapping("/payable-details-summary")
//    @ApiOperation("查询平台应付帐单明细汇总")
//    public R<PlatformWholesaleSettleDetailSummaryDTO> queryReceivableDetailsSummary(PlatformWholesaleSettleDetailParam param){
//        PlatformWholesaleSettleDetailSummaryDTO platformWholesaleSettleDetailSummaryDTO = wholesaleSettlementService.queryReceivableDetailsSummary(param);
//        return success(platformWholesaleSettleDetailSummaryDTO);
//    }
//
//    @PostMapping("/export-payable-details")
//    @ApiOperation("导出平台应付账单明细")
//    public R<String> exportReceivableDetails(@RequestBody PlatformWholesaleSettleDetailParam param) throws IOException {
//        List<PlatformWholesaleSettleDetailDTO> resultList = wholesaleSettlementService.queryReceivableDetails(param);
//        String filePath = fileUploadService.uploadExcelFile(resultList, PlatformWholesaleSettleDetailDTO.class, "批发应收结算明细");
//        return R.success(fileUploadService.getFileServerUrl(filePath));
//    }
//
//
//    @PostMapping("/payable")
//    @ApiOperation("生成应付结算单")
//    public R<WholesaleReceivableSettle> generateReceivableSettle(@RequestBody PlatformWholesaleSettleDetailParam param){
//        WholesaleReceivableSettle settle =  wholesaleSettlementService.generateReceivableSettle(param);
//        return success(settle);
//    }
//
//    @PatchMapping("/payable/status")
//    @ApiOperation("更新应付结算单状态")
//    public R<WholesaleReceivableSettle> updateReceivableStatus(@RequestBody WholesaleSettleStatusDTO wholesaleSettleStatusDTO){
//        Long settleId = wholesaleSettleStatusDTO.getId();
//        WholesaleReceivableSettle wholesaleReceivableSettle= wholesaleSettlementService.
//                updateReceivableStatus(settleId,wholesaleSettleStatusDTO.getSendStatus(),wholesaleSettleStatusDTO.getSettleStatus());
//        return success(wholesaleReceivableSettle);
//    }
}
