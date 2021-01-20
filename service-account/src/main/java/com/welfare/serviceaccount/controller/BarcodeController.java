package com.welfare.serviceaccount.controller;

import com.welfare.common.util.BarcodeUtil;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.entity.BarcodeSalt;
import com.welfare.service.BarcodeService;
import com.welfare.service.dto.BarcodeSaltDO;
import com.welfare.service.dto.payment.PaymentBarcode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "条码接口")
@RequestMapping("/barcode")
public class BarcodeController implements IController {
    private final BarcodeService barcodeService;
    private final SupplierStoreDao supplierStoreDao;
    @GetMapping
    @ApiOperation("用户获取支付条码")
    public R<PaymentBarcode> getPaymentBarcode(@RequestParam @ApiParam("账户编号") Long accountCode){
        BarcodeSalt barcodeSalt =  barcodeService.queryCurrentPeriodSaltValue();
        PaymentBarcode paymentBarcode = PaymentBarcode.of(accountCode, barcodeSalt.getSaltValue());
        return success(paymentBarcode);
    }

    @GetMapping("/test-barcode-parse")
    public R<String> testBarcodeParse(@RequestParam String barcode){
        Long saltValue = barcodeService.queryCurrentPeriodSaltValue().getSaltValue();
        return success(BarcodeUtil.calculateAccount("699048259340405130242", saltValue).toString());
    }

    @GetMapping("/salts")
    @ApiOperation("支付条码加盐参数列表获取")
    public R<List<BarcodeSaltDO>> getBarcodeSalts(){
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dayStr = dateFormat.format(currentDate);
        List<BarcodeSalt> barcodeSalts = barcodeService.querySalt(Long.valueOf(dayStr));
        return success(
                barcodeSalts.stream()
                        .map(BarcodeSaltDO::of)
                        .collect(Collectors.toList())
        );
    }
}
