package com.welfare.servicesettlement.controller;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailParam;
import com.welfare.persist.dto.settlement.wholesale.param.PlatformWholesaleSettleDetailSummaryDTO;
import com.welfare.persist.entity.WholesaleReceivableSettle;
import com.welfare.service.dto.MerchantWithCreditAndTreeDTO;
import com.welfare.service.dto.MonthSettlePageReq;
import com.welfare.service.dto.MonthSettleResp;
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
    public R<PageInfo<PlatformWholesaleSettleGroupDTO>> pageQueryReceivableSummary(
            @RequestParam @ApiParam("商户编码") String merCode,
            @RequestParam @ApiParam("商户编码") String supplierCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间起始 yyyy-MM-dd HH:mm:ss") Date transTimeStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间截至 yyyy-MM-dd HH:mm:ss") Date transTimeEnd,
            @RequestParam @ApiParam("页码") int pageIndex,
            @RequestParam @ApiParam("单页大小") int pageSize) {
        PageInfo<PlatformWholesaleSettleGroupDTO> pageInfo = wholesaleSettlementService.pageQueryReceivable(
                merCode,
                supplierCode,
                transTimeStart,
                transTimeEnd,
                pageIndex,
                pageSize
        );
        return success(pageInfo);
    }

    @PostMapping("/export-receivable-summary")
    @ApiOperation("导出平台应收账单分组汇总")
    public R<String> exportReceivableSummary(
            @RequestParam @ApiParam("商户编码") String merCode,
            @RequestParam @ApiParam("商户编码") String supplierCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间起始 yyyy-MM-dd HH:mm:ss") Date transTimeStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间截至 yyyy-MM-dd HH:mm:ss") Date transTimeEnd,
            @RequestParam @ApiParam("页码") int pageIndex,
            @RequestParam @ApiParam("单页大小") int pageSize) throws IOException {
        List<PlatformWholesaleSettleGroupDTO> resultList = wholesaleSettlementService.queryReceivable(merCode, supplierCode, transTimeStart, transTimeEnd);
        String filePath = fileUploadService.uploadExcelFile(resultList, PlatformWholesaleSettleGroupDTO.class, "批发应收结算分组汇总");
        return R.success(fileUploadService.getFileServerUrl(filePath));
    }

    @GetMapping("/receivable-summary")
    @ApiOperation("查询平台应收账单汇总")
    public R<PlatformWholesaleSettleGroupDTO> queryReceivableSummary(
            @RequestParam @ApiParam("商户编码") String merCode,
            @RequestParam @ApiParam("商户编码") String supplierCode,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间起始 yyyy-MM-dd HH:mm:ss") Date transTimeStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @ApiParam("交易时间截至 yyyy-MM-dd HH:mm:ss") Date transTimeEnd) {
        PlatformWholesaleSettleGroupDTO pageInfo = wholesaleSettlementService.queryReceivableSummary(
                merCode,
                supplierCode,
                transTimeStart,
                transTimeEnd
        );
        return success(pageInfo);
    }

    @PostMapping("/page-receivable-details")
    @ApiOperation("查询平台应收账单明细")
    public R<PageInfo<PlatformWholesaleSettleDetailDTO>> queryReceivableDetails(@RequestBody PlatformWholesaleSettleDetailParam param){
        PageInfo<PlatformWholesaleSettleDetailDTO> pageInfo = wholesaleSettlementService.pageQueryReceivableDetails(param);
        return success(pageInfo);
    }

    @PostMapping("/receivable-details-summary")
    @ApiOperation("查询平台应收帐单明细汇总")
    public R<PlatformWholesaleSettleDetailSummaryDTO> queryReceivableDetailsSummary(PlatformWholesaleSettleDetailParam param){
        PlatformWholesaleSettleDetailSummaryDTO platformWholesaleSettleDetailSummaryDTO = wholesaleSettlementService.queryReceivableDetailsSummary(param);
        return success(platformWholesaleSettleDetailSummaryDTO);
    }

    @PostMapping("/export-receivable-details")
    @ApiOperation("导出平台应收账单明细")
    public R<String> exportReceivableDetails(@RequestBody PlatformWholesaleSettleDetailParam param) throws IOException {
        List<PlatformWholesaleSettleDetailDTO> resultList = wholesaleSettlementService.queryReceivableDetails(param);
        String filePath = fileUploadService.uploadExcelFile(resultList, PlatformWholesaleSettleDetailDTO.class, "批发应收结算明细");
        return R.success(fileUploadService.getFileServerUrl(filePath));
    }

    @PostMapping("/receivable")
    @ApiOperation("生成应收结算单")
    public R<WholesaleReceivableSettle> generateReceivableSettle(@RequestBody PlatformWholesaleSettleDetailParam param){
        WholesaleReceivableSettle settle = wholesaleSettlementService.generateReceivableSettle(param);
        return success(settle);
    }

    @PatchMapping("/receivable/status")
    @ApiOperation("更新应收结算单状态")
    public R<WholesaleReceivableSettle> updateReceivableStatus(@RequestBody WholesaleSettleStatusDTO wholesaleSettleStatusDTO){
        Long settleId = wholesaleSettleStatusDTO.getId();
        WholesaleReceivableSettle wholesaleReceivableSettle= wholesaleSettlementService.
                updateReceivableStatus(settleId,wholesaleSettleStatusDTO.getSendStatus(),wholesaleSettleStatusDTO.getSettleStatus());
        return success(wholesaleReceivableSettle);
    }

//    @GetMapping("/settled/page-receivable-summary")
//    @ApiOperation("分页查询应收结算单分组列表")
//    public R<BasePageVo<MonthSettleResp>> pageQuery(MonthSettlePageReq monthSettleReqDto){
//
//        //商户用户只能查询本商户数据
//        MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
//        if(merchantUser!=null && !StringUtils.isEmpty(merchantUser.getMerchantCode())){
//            monthSettleReqDto.setMerCode(merchantUser.getMerchantCode());
//        }
//
//        BasePageVo<MonthSettleResp> monthSettleRespDtoPage =  monthSettleService.pageQuery(monthSettleReqDto);
//        return success(monthSettleRespDtoPage);
//    }

}
