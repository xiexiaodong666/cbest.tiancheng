package com.welfare.service.payment;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.AccountBillDetailDao;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.AccountDeductionDetailDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.entity.AccountDeductionDetail;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.dto.RefundRequest;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
public interface IRefundOperator {
    /**
     * 退款
     * @param refundRequest 退款请求
     * @param refundDeductionInDbs 数据库中中退款流水
     * @param paidDeductionDetails 付款流水
     * @param accountCode 账户编码
     */
    void refund(RefundRequest refundRequest,
                List<AccountDeductionDetail> refundDeductionInDbs,
                List<AccountDeductionDetail> paidDeductionDetails,
                Long accountCode);

    /**
     * 执行退款
     * @param refundRequest 退款请求
     * @param paidDeductionDetails 付款流水
     * @param accountCode 账户编码
     */
    default void doRefund(RefundRequest refundRequest, List<AccountDeductionDetail> paidDeductionDetails, Long accountCode) {

        //沃生活，微信，支付宝只会有一条
        AccountDeductionDetail thePaidDeductionDetail = paidDeductionDetails.get(0);
        AccountBillDetail thePaidBilDetail = getAccountBillDetailDao().getOneByTransNoAndTransType(
                refundRequest.getOriginalTransNo(),
                WelfareConstant.TransType.CONSUME.code()
        );
        BigDecimal paidAmount = paidDeductionDetails.stream()
                .map(AccountDeductionDetail::getTransAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal reversedAmount = thePaidDeductionDetail.getReversedAmount() == null?BigDecimal.ZERO:thePaidDeductionDetail.getReversedAmount();
        boolean isMoreThanPaid = paidAmount.subtract(reversedAmount).compareTo(BigDecimal.ZERO) < 0;
        BizAssert.isTrue(isMoreThanPaid, ExceptionCode.REFUND_MORE_THAN_PAID);
        Account account = getAccountDao().queryByAccountCode(accountCode);
        refundRequest.setPhone(account.getPhone());
        AccountDeductionDetail refundDeductionDetail = toRefundDeductionDetail(thePaidDeductionDetail, refundRequest);
        AccountBillDetail refundBillDetail = toRefundBillDetail(thePaidBilDetail, refundRequest);
        SupplierStore supplierStore = getSupplierDao().getOneByCode(refundBillDetail.getStoreCode());
        if(!Objects.equals(account.getMerCode(),supplierStore.getMerCode())){
            //非自营才有退回商户操作,和扣款时保持一致
            operateMerchantRefund(refundRequest, account);
        }

        getAccountBillDetailDao().saveOrUpdateBatch(Arrays.asList(refundBillDetail, thePaidBilDetail));
        getAccountDeductionDetailDao().saveOrUpdateBatch(Arrays.asList(refundDeductionDetail, thePaidDeductionDetail));
        refundRequest.setRefundStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
    }

    /**
     * 是否已经退款
     * @param refundRequest 退款请求
     * @param refundDeductionDetailsInDb 数据库中的退款流水
     * @return 是否已经退款，退款金额大于已付款金额会抛出异常
     */
    default boolean hasRefunded(RefundRequest refundRequest, List<AccountDeductionDetail> refundDeductionDetailsInDb){
        //数据库已经有退款记录则表示已经退款了
        return !CollectionUtils.isEmpty(refundDeductionDetailsInDb);
    }


    /**
     * 操作商家账户退款
     * @param refundRequest 退款请求
     * @param account 账户
     */
    default void operateMerchantRefund(RefundRequest refundRequest, Account account){
        RLock merAccountLock = DistributedLockUtil.lockFairly(MER_ACCOUNT_TYPE_OPERATE + ":" + account.getMerCode());
        try {
            getMerchantCreditService().increaseAccountType(
                    account.getMerCode(),
                    WelfareConstant.MerCreditType.REMAINING_LIMIT,
                    refundRequest.getAmount(),
                    refundRequest.getTransNo(),
                    WelfareConstant.TransType.REFUND.code());
        } finally {
            DistributedLockUtil.unlock(merAccountLock);
        }
    }

    /**
     * 根据付款记录转为退款记录
     * @param thePayDeductionDetail
     * @param refundRequest
     * @return
     */
    default AccountDeductionDetail toRefundDeductionDetail(AccountDeductionDetail thePayDeductionDetail, RefundRequest refundRequest) {
        AccountDeductionDetail refundDeductionDetail = new AccountDeductionDetail();
        BeanUtils.copyProperties(thePayDeductionDetail, refundDeductionDetail);
        refundDeductionDetail.setTransNo(refundRequest.getTransNo());
        refundDeductionDetail.setRelatedTransNo(refundRequest.getOriginalTransNo());
        refundDeductionDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundDeductionDetail.setTransTime(refundRequest.getRefundDate());
        refundDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        refundDeductionDetail.setId(null);
        refundDeductionDetail.setVersion(0);
        thePayDeductionDetail.setReversedAmount(refundRequest.getAmount());
        return refundDeductionDetail;
    }

    /**
     * 根据付款记录转为退款记录
     * @param thePayBillDetail
     * @param refundRequest
     * @return
     */
    default AccountBillDetail toRefundBillDetail(AccountBillDetail thePayBillDetail, RefundRequest refundRequest) {
        AccountBillDetail refundBillDetail = new AccountBillDetail();
        BeanUtils.copyProperties(thePayBillDetail, refundBillDetail);
        refundBillDetail.setTransNo(refundRequest.getTransNo());
        refundBillDetail.setTransType(WelfareConstant.TransType.REFUND.code());
        refundBillDetail.setTransTime(refundRequest.getRefundDate());
        refundBillDetail.setTransAmount(refundRequest.getAmount());
        refundBillDetail.setId(null);
        refundBillDetail.setVersion(0);
        return refundBillDetail;
    }

    /**
     * 获取accountBillDetailDao
     * @return
     */
    default AccountBillDetailDao getAccountBillDetailDao(){
        return SpringBeanUtils.getBean(AccountBillDetailDao.class);
    }

    /**
     * 获取accountDao
     * @return
     */
    default AccountDao getAccountDao(){
        return SpringBeanUtils.getBean(AccountDao.class);
    }

    /**
     * 获取supplierDao
     * @return
     */
    default SupplierStoreDao getSupplierDao(){
        return SpringBeanUtils.getBean(SupplierStoreDao.class);
    }

    /**
     * 获取merchantCreditService
     * @return
     */
    default MerchantCreditService getMerchantCreditService(){
        return SpringBeanUtils.getBean(MerchantCreditService.class);
    }

    /**
     * 获取accountDeductionDetailDao
     * @return
     */
    default AccountDeductionDetailDao getAccountDeductionDetailDao(){
        return SpringBeanUtils.getBean(AccountDeductionDetailDao.class);
    }



}
