package com.welfare.serviceaccount.controller;

import com.welfare.service.PaymentService;
import com.welfare.service.RefundService;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/6/2021
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/payment")
@Api(tags = "支付接口")
public class PaymentController implements IController {
    private final PaymentService paymentService;
    private final RefundService refundService;

    @PostMapping("/online")
    @ApiOperation("线上支付")
    public R<OnlinePaymentRequest> newOnlinePaymentRequest(@RequestBody OnlinePaymentRequest paymentRequest) {
        paymentService.paymentRequest(paymentRequest);
        return success(paymentRequest);
    }

    @PostMapping("/barcode")
    @ApiOperation("条码支付")
    public R<BarcodePaymentRequest> newBarcodePaymentRequest(@RequestBody BarcodePaymentRequest paymentRequest) {
        paymentService.paymentRequest(paymentRequest);
        return success(paymentRequest);
    }

    @PostMapping("/card")
    @ApiOperation("刷卡支付")
    public R<CardPaymentRequest> newCardPaymentRequest(@RequestBody CardPaymentRequest paymentRequest) {
        paymentService.paymentRequest(paymentRequest);
        return success(paymentRequest);
    }

    @PostMapping("/wholesale")
    @ApiOperation("批发支付")
    public R<WholesalePaymentRequest> newCardPaymentRequest(@RequestBody WholesalePaymentRequest paymentRequest) {
        paymentService.paymentRequest(paymentRequest);
        return success(paymentRequest);
    }

    @PostMapping("/door-access")
    @ApiOperation("门禁系统支付（交投）")
    public R<DoorAccessPaymentRequest> newDoorAccessPaymentRequest(@RequestBody DoorAccessPaymentRequest doorAccessPaymentRequest){
        return success(doorAccessPaymentRequest);
    }

    @GetMapping
    @ApiOperation("查询支付结果")
    public R<PaymentRequest> getPaymentRequest(@RequestParam @ApiParam("重百付支付流水号") String transNo) {
        //cardPaymentRequest包含全量信息，所以以CardPaymentRequest查询
        PaymentRequest paymentRequest = paymentService.queryResult(transNo,CardPaymentRequest.class);
        return success(paymentRequest);
    }


    @PostMapping("/refund")
    @ApiOperation("退款")
    public R<RefundRequest> newPaymentRequest(@RequestBody RefundRequest refundRequest) {
        refundService.handleRefundRequest(refundRequest);
        return success(refundRequest);
    }

    @GetMapping("/refund")
    @ApiOperation("查询退款结果")
    public R<RefundRequest> getRefundRequest(@RequestParam @ApiParam("重百付支付流水号") String transNo) {
        RefundRequest refundRequest = refundService.queryResult(transNo);
        return success(refundRequest);
    }

}
