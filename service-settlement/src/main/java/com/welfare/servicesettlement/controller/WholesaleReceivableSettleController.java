package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.welfare.persist.dto.WholesaleReceivableSettleDetailResp;
import com.welfare.persist.dto.WholesaleReceivableSettleResp;
import com.welfare.persist.dto.WholesaleReceiveSettleSummaryResp;
import com.welfare.persist.dto.query.WholesaleReceivableSettleBillQuery;
import com.welfare.persist.dto.query.WholesaleReceiveSettleDetailPageQuery;
import com.welfare.persist.dto.query.WholesaleReceiveSettleDetailQuery;
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
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/26/2021
 */
@RestController
@RequestMapping("/settlement/wholesale")
@RequiredArgsConstructor
@Api(tags = "批发应收结算")
public class WholesaleReceivableSettleController implements IController {
    private final WholesaleSettlementService wholesaleSettlementService;

    private final FileUploadServiceUtil fileUploadService;
    @GetMapping("/page-receivable-summary")
    @ApiOperation("分页查询平台应收账单分组汇总")
    public R<Page<PlatformWholesaleSettleGroupDTO>> pageQueryReceivableSummary(
            @RequestParam(required = false) @ApiParam("商户编码") String merCode,
            @RequestParam(required = false) @ApiParam("商户名称") String merName,
            @RequestParam(required = false) @ApiParam("商户编码") String supplierCode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间起始 yyyy-MM-dd HH:mm:ss") Date transTimeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间截至 yyyy-MM-dd HH:mm:ss") Date transTimeEnd,
            @RequestParam @ApiParam("页码") int current,
            @RequestParam @ApiParam("单页大小") int size) {
        Page<PlatformWholesaleSettleGroupDTO> pageInfo = wholesaleSettlementService.pageQueryReceivable(
                merCode,
                merName,
                supplierCode,
                transTimeStart,
                transTimeEnd,
                current,
                size
        );
        return success(pageInfo);
    }

    @PostMapping("/export-receivable-summary")
    @ApiOperation("导出平台应收账单分组汇总")
    public R<String> exportReceivableSummary(
            @RequestParam(required = false) @ApiParam("商户编码") String merCode,
            @RequestParam(required = false) @ApiParam("商户名称") String merName,
            @RequestParam(required = false) @ApiParam("商户编码") String supplierCode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间起始 yyyy-MM-dd HH:mm:ss") Date transTimeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间截至 yyyy-MM-dd HH:mm:ss") Date transTimeEnd) throws IOException {
      PlatformWholesaleSettleDetailParam param = new PlatformWholesaleSettleDetailParam();
      param.setMerCode(merCode);
      param.setSupplierCode(supplierCode);
      param.setTransTimeStart(transTimeStart);
      param.setTransTimeEnd(transTimeEnd);
      param.setMerName(merName);
        List<PlatformWholesaleSettleDetailDTO> resultList = wholesaleSettlementService.pageQueryReceivableDetails(param);

        String filePath = fileUploadService.uploadExcelFile(resultList, PlatformWholesaleSettleDetailDTO.class, "批发应收结算分组汇总");
        return R.success(fileUploadService.getFileServerUrl(filePath));
    }

    @GetMapping("/receivable-summary")
    @ApiOperation("查询平台应收账单汇总")
    public R<PlatformWholesaleSettleGroupDTO> queryReceivableSummary(
            @RequestParam(required = false) @ApiParam("商户编码") String merCode,
            @RequestParam(required = false) @ApiParam("商户名称") String merName,
            @RequestParam(required = false) @ApiParam("商户编码") String supplierCode,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间起始 yyyy-MM-dd HH:mm:ss") Date transTimeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间截至 yyyy-MM-dd HH:mm:ss") Date transTimeEnd) {
        PlatformWholesaleSettleGroupDTO pageInfo = wholesaleSettlementService.queryReceivableSummary(
                merCode,
                merName,
                supplierCode,
                transTimeStart,
                transTimeEnd
        );
        return success(pageInfo);
    }

    @PostMapping("/page-receivable-details")
    @ApiOperation("查询平台应收账单明细")
    public R<Page<PlatformWholesaleSettleDetailDTO>> queryReceivableDetails(@RequestBody PlatformWholesaleSettleDetailParam param){


       return success(wholesaleSettlementService.queryReceivableDetails(param));
    }

    @PostMapping("/receivable-details-summary")
    @ApiOperation("查询平台应收帐单明细汇总")
    public R<PlatformWholesaleSettleDetailSummaryDTO> queryReceivableDetailsSummary(@RequestBody PlatformWholesaleSettleDetailParam param){
        PlatformWholesaleSettleDetailSummaryDTO platformWholesaleSettleDetailSummaryDTO = wholesaleSettlementService.queryReceivableDetailsSummary(param);
        return success(platformWholesaleSettleDetailSummaryDTO);
    }

    @PostMapping("/export-receivable-details")
    @ApiOperation("导出平台应收账单明细")
    public R<String> exportReceivableDetails(@RequestBody PlatformWholesaleSettleDetailParam param) throws IOException {
      List<PlatformWholesaleSettleDetailDTO> resultList = wholesaleSettlementService.pageQueryReceivableDetails(param);

      String filePath = fileUploadService.uploadExcelFile(resultList, PlatformWholesaleSettleDetailDTO.class, "批发应收结算分组汇总");
      return R.success(fileUploadService.getFileServerUrl(filePath));
    }

    @PostMapping("/receivable")
    @ApiOperation("生成应收结算单")
    public R<WholesaleReceivableSettle> generateReceivableSettle(@RequestBody PlatformWholesaleSettleDetailParam param){
        WholesaleReceivableSettle settle = wholesaleSettlementService.generateReceivableSettle(param);
        return success(settle);
    }

    @PostMapping("/receivable/status")
    @ApiOperation("更新应收结算单状态")
    public R<WholesaleReceivableSettle> updateReceivableStatus(@RequestBody WholesaleSettleStatusDTO wholesaleSettleStatusDTO){
        Long settleId = wholesaleSettleStatusDTO.getId();
        WholesaleReceivableSettle wholesaleReceivableSettle= wholesaleSettlementService.
                updateReceivableStatus(settleId,wholesaleSettleStatusDTO.getSendStatus(),wholesaleSettleStatusDTO.getSettleStatus(), wholesaleSettleStatusDTO.getRecStatus());
        return success(wholesaleReceivableSettle);
    }


    @GetMapping("/receivable/bill/page")
    @ApiOperation("分页查询应收结算单分组列表")
    public R<Page<WholesaleReceivableSettleResp>> receivableBillPage(WholesaleReceivableSettleBillQuery query)
        throws JsonProcessingException {

        Page<WholesaleReceivableSettleResp> wholesaleReceivableSettleRespPageInfo = wholesaleSettlementService.receivableBillPage(query);

        return success(wholesaleReceivableSettleRespPageInfo);
    }

    @GetMapping("/receivable/bill/{id}")
    @ApiOperation("应收明细结算单详情")
    public R<WholesaleReceivableSettleResp> receivableBillDetail(@PathVariable("id") Long id)
        throws JsonProcessingException {

        return success(wholesaleSettlementService.receivableBillDetail(id));
    }


    @GetMapping("/receivable/bill/{id}/page")
    @ApiOperation("分页查询某个应收结算单明细列表")
    public R<Page<WholesaleReceivableSettleDetailResp>> receivableBillDetailPage(@PathVariable("id") Long id, WholesaleReceiveSettleDetailPageQuery query){

        return success(wholesaleSettlementService.receivableBillDetailPage(id, query));
    }

    @GetMapping("/receivable/bill/{id}/summary")
    @ApiOperation("查询应收结算单明细数据汇总")
    public R<WholesaleReceiveSettleSummaryResp> receivableBillDetailSummary(@PathVariable("id") Long id, WholesaleReceiveSettleDetailQuery query){
        return success(wholesaleSettlementService.receivableBillDetailSummary(id, query));
    }

    @GetMapping("/receivable/bill/{id}/export")
    @ApiOperation("应收结算明细数据单导出")
    public R<String> receivableBillDetailExport(@PathVariable("id") Long id, WholesaleReceiveSettleDetailPageQuery query)
        throws IOException {
      List<WholesaleReceivableSettleDetailResp> resultList = wholesaleSettlementService.receivableBillDetail(id, query);

      String filePath = fileUploadService.uploadExcelFile(resultList, WholesaleReceivableSettleDetailResp.class, "批发应收结算分组汇总");
      return R.success(fileUploadService.getFileServerUrl(filePath));
    }
}
