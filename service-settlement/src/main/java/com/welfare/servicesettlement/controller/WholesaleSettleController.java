package com.welfare.servicesettlement.controller;

import com.github.pagehelper.PageInfo;
import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleGroupDTO;
import com.welfare.service.settlement.WholesaleSettlementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/26/2021
 */
@RestController
@RequestMapping("/settlement/wholesale/")
@RequiredArgsConstructor
@Api(tags = "批发结算")
public class WholesaleSettleController implements IController {
    private final WholesaleSettlementService wholesaleSettlementService;

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
}
