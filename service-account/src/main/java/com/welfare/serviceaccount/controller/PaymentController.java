package com.welfare.serviceaccount.controller;

import com.welfare.service.AccountPaymentResultService;
import com.welfare.service.PaymentService;
import com.welfare.service.RefundService;
import com.welfare.service.dto.RefundRequest;
import com.welfare.service.dto.ThirdPartyBarcodePaymentDTO;
import com.welfare.service.dto.payment.*;
import com.welfare.service.remote.entity.CbestPayBaseResp;
import com.welfare.serviceaccount.controller.dto.PaymentNotification;
import com.welfare.serviceaccount.controller.dto.PaymentNotificationContent;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.BeanUtils;
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
    private final AccountPaymentResultService accountPaymentResultService;

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

    @PostMapping("/thirdPartyBarcodePaymentSceneCheck")
    @ApiOperation("第三方条码支付场景检验")
    public R<ThirdPartyBarcodePaymentDTO> thirdPartyBarcodePaymentSceneCheck(@RequestBody BarcodePaymentRequest paymentRequest) {
        ThirdPartyBarcodePaymentDTO thirdPartyBarcodePaymentDTO = paymentService
            .thirdPartyBarcodePaymentSceneCheck(paymentRequest);
        return success(thirdPartyBarcodePaymentDTO);
    }

    @PostMapping("/password-free/notification")
    @ApiOperation("免密支付成功通知接口")
    @ApiModelProperty("SUCCESS or FAILED")
    public String paymentNotification(@RequestBody PaymentNotification paymentNotification){
        try{
            //缓存支付通知结果，员工卡H5端会轮训查询支付结果
            CbestPayBaseResp cbestPayBaseResp = new CbestPayBaseResp();
            BeanUtils.copyProperties(paymentNotification, cbestPayBaseResp);
            accountPaymentResultService.thirdPartyPaymentResultNotify(cbestPayBaseResp);
            PaymentNotificationContent paymentNotificationContent = paymentNotification.parseContent();
            PaymentRequest paymentRequest = paymentNotificationContent.toPaymentRequest();
            paymentService.paymentRequest(paymentRequest);
            return PaymentNotification.SUCCESS;
        }catch (Exception e){
            log.error("免密支付通知异常:",e);
            return PaymentNotification.FAILED;
        }

    }

}
