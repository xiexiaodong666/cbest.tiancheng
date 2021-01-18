package com.welfare.service;

import com.welfare.service.dto.BarcodePaymentNotifyReq;
import com.welfare.service.dto.BarcodePaymentResultDTO;
import com.welfare.service.dto.BarcodePaymentResultReq;

public interface AccountPaymentResultService {

    void barcodePaymentNotify(BarcodePaymentNotifyReq req);

    BarcodePaymentResultDTO queryBarcodePaymentResult(BarcodePaymentResultReq req);
}
