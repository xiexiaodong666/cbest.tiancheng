package com.welfare.serviceaccount.controller;

import com.welfare.serviceaccount.domain.PaymentBarcode;
import com.welfare.serviceaccount.domain.PaymentRequest;
import com.welfare.serviceaccount.domain.RefundRequest;
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
    @PostMapping
    @ApiOperation("支付")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "Signature",
                    value = "验签算法为md5(hmac1(originalStr)),规则为按照SignatureFields所示字段取值拼接"
            ),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "SignatureFields",
                    value = "用来生成签名的属性，从paymentRequest中取值." +
                            "requestId,transNo,amount表明按照requestId,transNo,amount的顺序进行拼接作为验签源",
                    example = "requestId,transNo,amount"
            )
    })
    public R<PaymentRequest> newPaymentRequest(@RequestHeader("Signature") String signature,
                                               @RequestBody PaymentRequest paymentRequest) {
        Map<String, Object> map = BeanUtil.toMap(paymentRequest);
        return success(paymentRequest);
    }

    @GetMapping
    @ApiOperation("查询支付结果")
    public R<PaymentRequest> getPaymentRequest(@RequestParam @ApiParam("重百付支付流水号") String transNo) {
        return success(null);
    }


    @PostMapping("/refund")
    @ApiOperation("支付")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "Signature",
                    value = "验签算法为md5(hmac1(originalStr)),规则为按照SignatureFields所示字段取值拼接"
            ),
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "SignatureFields",
                    value = "用来生成签名的属性，从refundRequest中取值." +
                            "requestId,transNo,amount表明按照requestId,transNo,amount的顺序进行拼接作为验签源",
                    example = "requestId,transNo,amount"
            )
    })
    public R<RefundRequest> newPaymentRequest(@RequestHeader("Signature") String signature,
                                               @RequestBody RefundRequest refundRequest) {
        Map<String, Object> map = BeanUtil.toMap(refundRequest);
        return success(refundRequest);
    }

    @GetMapping("/refund")
    @ApiOperation("查询退款结果")
    public R<RefundRequest> getRefundRequest(@RequestParam @ApiParam("重百付支付流水号") String transNo) {
        return success(null);
    }

    @GetMapping("/barcode")
    @ApiOperation("获取支付条码")
    public R<PaymentBarcode> getPaymentBarcode(@RequestParam String accountCode,@RequestParam String cardNo){
        PaymentBarcode paymentBarcode = PaymentBarcode.of(accountCode,"null",cardNo);
        return success(paymentBarcode);
    }

}
