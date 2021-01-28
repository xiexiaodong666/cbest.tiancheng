package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.TempAccountDepositApply;
import com.welfare.service.AccountService;
import com.welfare.service.TempAccountDepositApplyService;
import com.welfare.service.dto.AccountDepositRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.DecimalMax;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  4:05 PM
 */
@Slf4j
public class DepositApplyUploadListener extends AnalysisEventListener<AccountDepositRequest> {
  /**
   * 每隔5条存储数据库，然后清理list ，方便内存回收
   */
  private static final int BATCH_COUNT = 100;

  private static final int MAX_COUNT = 2000;

  private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(99999999.99);

  private List<TempAccountDepositApply> list = new ArrayList<>();

  /**
   * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
   */
  private TempAccountDepositApplyService depositApplyService;

  private AccountService accountService;

  private String requestId;

  private String fileId;

  private String merCode;

  private Set<String> accountCodeSet = new HashSet<>(1000);

  public DepositApplyUploadListener() {}

  public DepositApplyUploadListener(TempAccountDepositApplyService depositApplyService,
                                    String requestId, String fileId,AccountService accountService,
                                    String merCode) {
    this.depositApplyService = depositApplyService;
    this.requestId = requestId;
    this.fileId = fileId;
    this.accountService = accountService;
    this.merCode = merCode;
  }

  @Override
  public void invoke(AccountDepositRequest request, AnalysisContext context) {
    log.info("员工账号存储申请：解析到一条数据:{}, requestId:{}, fileId;{}", JSON.toJSONString(request), requestId, fileId);
    if (Objects.isNull(request.getPhone())) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "账号不能为空！", null);
    }
    if (request.getRechargeAmount() == null || request.getRechargeAmount().compareTo(BigDecimal.ZERO) < 0) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("[%s]金额不能小于0！", request.getPhone()), null);
    }
    if (request.getRechargeAmount().compareTo(MAX_AMOUNT) > 0) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("[%s]金额超过限制[%s]！", request.getPhone(), MAX_AMOUNT), null);
    }
    if (accountCodeSet.contains(request.getPhone())) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("[%s]账号(手机号)不能重复！", request.getPhone()), null);
    }
    Account account = accountService.findByPhoneAndMerCode(request.getPhone(), merCode);
    if (account == null) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("商户下没有[%s]员工！", request.getPhone()), null);
    }
    accountCodeSet.add(request.getPhone());
    list.add(getTempAccountDepositApply(request, account));
//    if (list.size() > MAX_COUNT) {
//      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("超过最大上传数量[%s]！", MAX_COUNT), null);
//    }
  }

  /**
   * 加上存储数据库
   */
//  private void saveData() {
//    log.info("员工账号存储申请：{}条数据，开始存储数据库！requestId:{}, fileId:{}", list.size(), requestId, fileId);
//    if (CollectionUtils.isNotEmpty(list)) {
//      List<TempAccountDepositApply> batchSave = new ArrayList<>();
//      final CountDownLatch countDownLatch = new CountDownLatch(page(list.size()));
//      for (int i = 0; i < list.size(); i++) {
//        TempAccountDepositApply apply = getTempAccountDepositApply(list.get(i));
//        batchSave.add(apply);
//        if ((batchSave.size() == BATCH_COUNT) || (i == list.size() - 1)) {
//          List<TempAccountDepositApply> finalBatchSave = batchSave;
//          executor.execute(() -> {
//            depositApplyService.saveAll(finalBatchSave);
//            countDownLatch.countDown();
//          });
//          batchSave = new ArrayList<>();
//        }
//      }
//      try {
//        countDownLatch.await();
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    }
//    log.info("员工账号存储申请: 存储数据库成功！{}条数据 requestId:{}, fileId:{}", list.size(), requestId, fileId);
//  }

  /**
   * 加上存储数据库
   */
  private void saveData() {
    if (CollectionUtils.isEmpty(list)) {
      throw new BusiException("请至少上传一个员工");
    }
    log.info("员工账号存储申请：{}条数据，开始存储数据库！requestId:{}, fileId:{}", list.size(), requestId, fileId);
    depositApplyService.saveAll(list);
    log.info("员工账号存储申请: 存储数据库成功！{}条数据 requestId:{}, fileId:{}", list.size(), requestId, fileId);
  }

  public int page(int size) {
    if (size % BATCH_COUNT == 0) {
      return size / BATCH_COUNT;
    } else {
      return size / BATCH_COUNT + 1;
    }
  }

  private TempAccountDepositApply getTempAccountDepositApply(AccountDepositRequest request, Account account){
    TempAccountDepositApply apply = new TempAccountDepositApply();
    apply.setPhone(request.getPhone());
    apply.setAccountCode(account.getAccountCode());
    apply.setRechargeAmount(request.getRechargeAmount());
    apply.setFileId(fileId);
    apply.setRequestId(requestId);
    return apply;
  }

  /**
   * 所有数据解析完成了 都会来调用
   *
   * @param context
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void doAfterAllAnalysed(AnalysisContext context) {
    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    saveData();
    log.info("员工账号存储申请：所有数据解析完成！requestId:{}, fileId;{}", requestId, fileId);
  }

}