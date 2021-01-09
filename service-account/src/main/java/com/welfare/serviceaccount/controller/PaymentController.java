package com.welfare.serviceaccount.controller;

import com.welfare.common.util.BarcodeUtil;
import com.welfare.persist.entity.BarcodeSalt;
import com.welfare.service.BarcodeSaltService;
import com.welfare.service.dto.BarcodeSaltDO;
import com.welfare.service.dto.PaymentBarcode;
import com.welfare.service.dto.PaymentRequest;
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
    private final BarcodeSaltService barcodeSaltService;
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
    @ApiOperation("退款")
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
    @ApiOperation("用户获取支付条码")
    public R<PaymentBarcode> getPaymentBarcode(@RequestParam @ApiParam("账户编号") Long accountCode){
        BarcodeSalt barcodeSalt =  barcodeSaltService.queryCurrentPeriodSaltValue();
        PaymentBarcode paymentBarcode = PaymentBarcode.of(accountCode, barcodeSalt.getSaltValue());
        return success(paymentBarcode);
    }

    @GetMapping("/test-barcode-parse")
    public R<String> testBarcodeParse(@RequestParam String barcode){
        Long saltValue = barcodeSaltService.queryCurrentPeriodSaltValue().getSaltValue();
        return success(BarcodeUtil.calculateAccount("699048259340405130242", saltValue).toString());
    }

    @GetMapping("/barcode-salts")
    @ApiOperation("支付条码加盐参数列表获取")
    public R<List<BarcodeSaltDO>> getBarcodeSalts(){
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dayStr = dateFormat.format(currentDate);
        List<BarcodeSalt> barcodeSalts = barcodeSaltService.query(Long.valueOf(dayStr));
        return success(
                barcodeSalts.stream()
                        .map(BarcodeSaltDO::of)
                        .collect(Collectors.toList())
        );
    }

}
