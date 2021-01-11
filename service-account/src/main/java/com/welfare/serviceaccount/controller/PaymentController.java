package com.welfare.serviceaccount.controller;

import com.welfare.common.util.BarcodeUtil;
import com.welfare.persist.entity.BarcodeSalt;
import com.welfare.service.BarcodeSaltService;
import com.welfare.service.dto.BarcodeSaltDO;
import com.welfare.service.dto.payment.BarcodePaymentRequest;
import com.welfare.service.dto.payment.CardPaymentRequest;
import com.welfare.service.dto.payment.PaymentBarcode;
import com.welfare.service.dto.payment.OnlinePaymentRequest;
import com.welfare.service.dto.RefundRequest;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import net.dreamlu.mica.core.utils.BeanUtil;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @PostMapping("/online")
    @ApiOperation("线上支付")
    public R<OnlinePaymentRequest> newOnlinePaymentRequest(@RequestBody OnlinePaymentRequest paymentRequest) {
        Map<String, Object> map = BeanUtil.toMap(paymentRequest);
        return success(paymentRequest);
    }

    @PostMapping("/barcode")
    @ApiOperation("条码支付")
    public R<BarcodePaymentRequest> newBarcodePaymentRequest(@RequestBody BarcodePaymentRequest paymentRequest) {
        Map<String, Object> map = BeanUtil.toMap(paymentRequest);
        return success(paymentRequest);
    }

    @PostMapping("/card")
    @ApiOperation("刷卡支付")
    public R<CardPaymentRequest> newCardPaymentRequest(@RequestBody CardPaymentRequest paymentRequest) {
        Map<String, Object> map = BeanUtil.toMap(paymentRequest);
        return success(paymentRequest);
    }

    @GetMapping
    @ApiOperation("查询支付结果")
    public R<OnlinePaymentRequest> getPaymentRequest(@RequestParam @ApiParam("重百付支付流水号") String transNo) {
        return success(null);
    }


    @PostMapping("/refund")
    @ApiOperation("退款")
    public R<RefundRequest> newPaymentRequest(@RequestBody RefundRequest refundRequest) {
        Map<String, Object> map = BeanUtil.toMap(refundRequest);
        return success(refundRequest);
    }

    @GetMapping("/refund")
    @ApiOperation("查询退款结果")
    public R<RefundRequest> getRefundRequest(@RequestParam @ApiParam("重百付支付流水号") String transNo) {
        return success(null);
    }

}
