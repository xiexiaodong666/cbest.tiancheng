package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.ApiUser;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.*;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.entity.WholesalePayableSettle;
import com.welfare.service.WholesalePayableSettletService;
import com.welfare.service.dto.StoreCodeNameDTO;
import com.welfare.service.dto.WholesalePaySettleDetailReq;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    private final WholesalePayableSettletService payableSettletService;
    private final FileUploadServiceUtil fileUploadService;

    @GetMapping("/page-payable-summary")
    @ApiOperation("(平台)分页查询平台应付未结算分组汇总")
    public R<Page<PlatformPayableSettleGroupDTO>> pageQueryPayableSummary(PlatformWholesalePayablePageQuery query) {
        return success(payableSettletService.pageQueryPayableSummary(query));
    }

    @PostMapping("/export-payable-summary")
    @ApiOperation("(平台)导出平台应付未结算账单分组汇总")
    public R<String> exportPayableSummary(PlatformWholesalePayableQuery query) throws IOException {
        PlatformWholesalePayableDetailQuery detailQuery = new PlatformWholesalePayableDetailQuery();
        BeanUtils.copyProperties(query, detailQuery);
        detailQuery.setMerCode(query.getSupplierMerName());
        List<PlatformWholesaleSettleDetailDTO> details = payableSettletService.queryPayableDetails(detailQuery);
        String path = fileUploadService.uploadExcelFile(details, PlatformWholesaleSettleDetailDTO.class, "应付结算明细");
        return R.success(fileUploadService.getFileServerUrl(path));
    }

    @GetMapping("/payable-summary")
    @ApiOperation("(平台)查询平台应付未结算账单汇总")
    public R<PlatformWholesalePayableGroupDTO> queryPayableSummary(PlatformWholesalePayableQuery query) {
        return success(payableSettletService.queryPayableSummary(query));
    }

    @PostMapping("/page-payable-details")
    @ApiOperation("(平台)查询平台应付未结算账单明细")
    public R<Page<PlatformWholesaleSettleDetailDTO>> queryPayableDetails(@RequestBody PlatformWholesalePayableDetailPageQuery query){
        return success(payableSettletService.queryPagePayableDetails(query));
    }

    @PostMapping("/payable-details-summary")
    @ApiOperation("(平台)查询平台应付未结算帐单明细汇总")
    public R<PlatformWholesalePayableDetailSummaryDTO> queryPayableDetailsSummary(PlatformWholesalePayableDetailQuery query){
        return success(payableSettletService.queryPayableDetailsSummary(query));
    }

    @PostMapping("/export-payable-details")
    @ApiOperation("(平台)导出平台应付未结算账单明细")
    public R<String> exportPayableDetails(@RequestBody PlatformWholesalePayableDetailQuery query) throws IOException {
        List<PlatformWholesaleSettleDetailDTO> details = payableSettletService.queryPayableDetails(query);
        String path = fileUploadService.uploadExcelFile(details, PlatformWholesaleSettleDetailDTO.class, "应付结算明细");
        return R.success(fileUploadService.getFileServerUrl(path));
    }

    @PostMapping("/payable")
    @ApiOperation("(平台)生成应付结算单")
    public R<WholesalePayableSettle> generatePayableSettle(@RequestBody PlatformWholesalePayableDetailPageQuery query){
        PlatformWholesalePayableDetailQuery detailQuery = new PlatformWholesalePayableDetailQuery();
        BeanUtils.copyProperties(query, detailQuery);
        return success(payableSettletService.generatePayableSettle(detailQuery));
    }

    @PutMapping("/payable/bill/{id}/send")
    @ApiOperation("(平台)平台发送账单")
    @ApiUser
    public R monthSettleSend(@PathVariable("id")Long id){
        return success(payableSettletService.settleSend(id));
    }

    @PutMapping("/payable/bill/{id}/finish")
    @ApiOperation("(平台)平台确认账单完成")
    @ApiUser
    public R monthSettleFinish(@PathVariable("id")Long id) {
        return success(payableSettletService.settleFinish(id));
    }

    @PutMapping("/payable/bill/{id}/confirm")
    @ApiOperation("(商户应收结算单)商户确认账单")
    @MerchantUser
    public R monthSettleConfirm(@PathVariable("id")Long id){
        return success(payableSettletService.settleConfirm(id));
    }

    @GetMapping("/payable/bill/page")
    @ApiOperation("(商户应收结算单)分页查询应付结算单分组列表")
    public R<Page<WholesalePayableSettleResp>> payableBillPage(WholesalePayableSettleBillQuery query){
        return success(payableSettletService.payableBillPage(query));
    }

    @GetMapping("/payable/bill/{id}/page")
    @ApiOperation("(商户应收结算单)分页查询某个应付结算单明细列表")
    public R<Page<WholesalePayableSettleDetailResp>> payableBillDetailPage(@PathVariable("id") Long id, WholesalePaySettleDetailPageQuery query){
        return success(payableSettletService.payableBillDetailPage(id, query));
    }

    @GetMapping("/payable/bill/{id}/summary")
    @ApiOperation("(商户应收结算单)查询应付结算单明细数据汇总")
    public R<WholesalePayableBillGroupDTO> payableBillDetailSummary(@PathVariable("id") Long id, WholesalePaySettleDetailReq query){
        return success(payableSettletService.payableBillDetailSummary(id, query));
    }

    @GetMapping("/payable/bill/{id}/detail")
    @ApiOperation("(商户应收结算单)根据id查询结算单详情")
    public R<WholesalePayableSettleResp> payableBillById(@PathVariable("id") Long id){
        return success(payableSettletService.payableBillById(id));
    }

    @GetMapping("/payable/bill/{id}/export")
    @ApiOperation("(商户应收结算单)应付结算明细数据单导出")
    public R<String> payableBillDetailExport(@PathVariable("id") Long id, WholesalePaySettleDetailReq query) throws IOException {
        List<WholesalePayableSettleDetailResp> details = payableSettletService.queryPayableBillDetail(id, query);
        String path = fileUploadService.uploadExcelFile(details, WholesalePayableSettleDetailResp.class, "应付结算单明细");
        return R.success(fileUploadService.getFileServerUrl(path));
    }

    @GetMapping("/payable/bill/{id}/stores")
    @ApiOperation("(商户应收结算单)查询结算单下所有的消费门店")
    public R<List<StoreCodeAndNameDTO>> storesBySettleId(@PathVariable("id") Long id) {
        return success(payableSettletService.storesBySettleId(id));
    }

    @GetMapping("/payable/bill/{id}/customerMers")
    @ApiOperation("(商户应收结算单)查询结算单下所有的消费客户")
    public R<List<MerCodeAndNameDTO>> customerMersBySettleId(@PathVariable("id") Long id) {
        return success(payableSettletService.customerMersBySettleId(id));
    }
}
