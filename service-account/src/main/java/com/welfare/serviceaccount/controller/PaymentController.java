package com.welfare.serviceaccount.controller;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.service.PaymentService;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.CardPaymentRequest;
import com.welfare.service.dto.payment.OnlinePaymentRequest;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.utils.BeanUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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


    @PostMapping("/online")
    @ApiOperation("线上支付")
    public R<OnlinePaymentRequest> newOnlinePaymentRequest(@RequestBody OnlinePaymentRequest paymentRequest) {
        paymentService.handlePayRequest(paymentRequest);
        return success(paymentRequest);
    }

    @PostMapping("/barcode")
    @ApiOperation("条码支付")
    public R<BarcodePaymentRequest> newBarcodePaymentRequest(@RequestBody BarcodePaymentRequest paymentRequest) {
        paymentService.handlePayRequest(paymentRequest);
        return success(paymentRequest);
    }

    @PostMapping("/card")
    @ApiOperation("刷卡支付")
    public R<CardPaymentRequest> newCardPaymentRequest(@RequestBody CardPaymentRequest paymentRequest) {
        paymentService.handlePayRequest(paymentRequest);
        return success(paymentRequest);
    }

    @GetMapping
    @ApiOperation("查询支付结果")
    public R<PaymentRequest> getPaymentRequest(@RequestParam @ApiParam("重百付支付流水号") String transNo) {
        PaymentRequest paymentRequest = paymentService.queryResult(transNo);
        return success(paymentRequest);
    }


    @PostMapping("/refund")
    @ApiOperation("退款")
    public R<RefundRequest> newPaymentRequest(@RequestBody RefundRequest refundRequest) {

        refundRequest.setRefundStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
        return success(refundRequest);
    }

    @GetMapping("/refund")
    @ApiOperation("查询退款结果")
    public R<RefundRequest> getRefundRequest(@RequestParam @ApiParam("重百付支付流水号") String transNo) {
        return success(null);
    }

}
