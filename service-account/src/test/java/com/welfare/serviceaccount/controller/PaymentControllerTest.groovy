package com.welfare.serviceaccount.controller

import com.welfare.persist.dao.AccountDao
import com.welfare.persist.dao.AccountTypeDao
import com.welfare.persist.entity.Account
import com.welfare.persist.entity.AccountType
import com.welfare.service.dto.payment.OnlinePaymentRequest
import com.welfare.service.dto.payment.PaymentRequest
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.event.annotation.AfterTestClass
import org.springframework.test.context.event.annotation.AfterTestExecution
import org.springframework.test.context.event.annotation.BeforeTestClass
import org.springframework.test.context.event.annotation.BeforeTestExecution

/**
 *  Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/14/2021
 */

@SpringBootTest
class PaymentControllerTest {
    PaymentRequest paymentRequest;
    Long testEntityId = 8999999999L;
    @Autowired
    PaymentController paymentController;
    @Autowired
    AccountDao accountDao;
    @Autowired
    AccountTypeDao accountTypeDao;
    @BeforeEach
    void setUp() {
        paymentRequest = new OnlinePaymentRequest();
        paymentRequest.setAccountCode(1000000001L);
        paymentRequest.setAmount(new BigDecimal(1000));
        paymentRequest.setMachineNo("machineNo");
        paymentRequest.setMerCode("testMerCode");
        paymentRequest.setOffline(false);
        paymentRequest.setRequestId(UUID.randomUUID().toString());
        paymentRequest.setPaymentDate(Calendar.getInstance().getTime());
        paymentRequest.setStoreNo("store001");
        paymentRequest.setTransNo("transNo001");

        Account account = new Account();
        account.setMerCode("testMerCode");
        account.setId(testEntityId);
        account.setAccountCode(8999999999L);
        account.setAccountBalance(new BigDecimal(1000));
        account.setAccountTypeCode("testAccountType");
        account.setMaxQuota(BigDecimal.ZERO)
        account.setAccountName("测试账户");
        accountDao.saveOrUpdate(account);

        AccountType accountType = new AccountType();
        accountType.setId(testEntityId);
        accountType.setMerCode("testMerCode");
        accountType.setTypeName("测试类型");
        accountType.setTypeCode("testAccountType");
        accountTypeDao.saveOrUpdate(accountType);
    }

    @AfterEach
    void tearDown() {
        accountDao.removeById(testEntityId);
        accountTypeDao.removeById(testEntityId);
    }

    @Test
    void testNewOnlinePaymentRequest() {
        paymentController.newOnlinePaymentRequest(paymentRequest);
    }
}
