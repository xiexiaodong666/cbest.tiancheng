package com.welfare.service.settlement.impl;

import com.amazonaws.util.StringInputStream;
import com.welfare.common.util.FtpUtil;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.settlement.DailyCheckBillService;
import com.welfare.service.settlement.domain.AccountDeductionDetailExcelModel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.RowSet;
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
 * @date 2/8/2021
 */
@RequiredArgsConstructor
@Component
public class DailyCheckBillServiceImpl implements DailyCheckBillService {

    public static final String COMMA = ",";
    private final SupplierStoreDao supplierStoreDao;
    private final AccountDeductionDetailDao accountDeductionDetailDao;
    private final FtpUtil ftpUtil;

    @Value("${ftp.path:/test}")
    private String targetFtpPath;


    @Override
    public void generateDailyCheckBill() {
        Calendar from = Calendar.getInstance();
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 24);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        List<SupplierStore> supplierStores = supplierStoreDao.selectAllCbest(SupplierStore.STORE_CODE, SupplierStore.MER_CODE);
        Map<String, String> storeMerCodeMap = supplierStores.stream().collect(Collectors.toMap(SupplierStore::getStoreCode, SupplierStore::getMerCode));
        List<AccountDeductionDetail> accountDeductionDetails = accountDeductionDetailDao.queryByStoreNosAndDate(storeMerCodeMap.keySet(), from.getTime(), end.getTime());

        String csvContent = this.generateExcel(accountDeductionDetails, storeMerCodeMap);

        this.writeToFtp(csvContent, from.getTime());


    }

    private String generateExcel(List<AccountDeductionDetail> accountDeductionDetails, Map<String, String> storeMerCodeMap) {
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder csvBuilder = new StringBuilder("门店编号,商户号,账户号,重百付流水号,交易时间,交易类型,交易金额(元),服务费(元),");
        csvBuilder.append(lineSeparator);
        accountDeductionDetails.stream()
                .map(accountBillDetail -> AccountDeductionDetailExcelModel.of(accountBillDetail, storeMerCodeMap))
                .forEach(model -> {
                    csvBuilder.append(model.getStoreCode()).append(COMMA);
                    csvBuilder.append(model.getMerCode()).append(COMMA);
                    csvBuilder.append(model.getAccountCode()).append(COMMA);
                    csvBuilder.append(model.getTransNo()).append(COMMA);
                    csvBuilder.append(model.getTransDate()).append(COMMA);
                    csvBuilder.append(model.getTransType()).append(COMMA);
                    csvBuilder.append(model.getTransAmount()).append(COMMA);
                    //服务费固定为0，此项不计服务费
                    csvBuilder.append(0).append(COMMA).append(lineSeparator);

                });

        return csvBuilder.toString();
    }

    private XSSFWorkbook generateExcel(RowSet rowSet) {
        return null;
    }

    @SneakyThrows
    private void writeToFtp(String cvsContent, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(date);
        StringInputStream inputStream = new StringInputStream(cvsContent);
        ftpUtil.upload(dateStr + ".csv",inputStream);
    }
}
