package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.TempAccountDepositApply;
import com.welfare.service.AccountService;
import com.welfare.service.TempAccountDepositApplyService;
import com.welfare.service.dto.AccountDepositRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

  private List<AccountDepositRequest> list = new ArrayList<>();

  private ThreadPoolExecutor executor;

  /**
   * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
   */
  private TempAccountDepositApplyService depositApplyService;

  private AccountService accountService;

  private String requestId;

  private String fileId;

  public DepositApplyUploadListener() {}

  public DepositApplyUploadListener(TempAccountDepositApplyService depositApplyService,
                                    String requestId, String fileId,AccountService accountService,
                                    ThreadPoolExecutor executor) {
    this.depositApplyService = depositApplyService;
    this.requestId = requestId;
    this.fileId = fileId;
    this.accountService = accountService;
    this.executor = executor;
  }

  @Override
  public void invoke(AccountDepositRequest request, AnalysisContext context) {
    log.info("员工账号存储申请：解析到一条数据:{}, requestId:{}, fileId;{}", JSON.toJSONString(request), requestId, fileId);
    if (Objects.isNull(request.getAccountCode())) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "账号不能为空！", null);
    }
    if (request.getRechargeAmount().compareTo(BigDecimal.ZERO) < 0) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("[%s]金额不能小于0！", request.getAccountCode()), null);
    }
    list.add(request);
    if (list.size() > MAX_COUNT) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, String.format("超过最大上传数量[%s]！", MAX_COUNT), null);
    }
  }

  /**
   * 加上存储数据库
   */
  private void saveData() {
    log.info("员工账号存储申请：{}条数据，开始存储数据库！requestId:{}, fileId:{}", list.size(), requestId, fileId);
    if (CollectionUtils.isNotEmpty(list)) {
      List<TempAccountDepositApply> batchSave = new ArrayList<>();
      final CountDownLatch countDownLatch = new CountDownLatch(page(list.size()));
      for (int i = 0; i < list.size(); i++) {
        TempAccountDepositApply apply = getTempAccountDepositApply(list.get(i));
        batchSave.add(apply);
        if ((batchSave.size() == BATCH_COUNT) || (i == list.size() - 1)) {
          List<TempAccountDepositApply> finalBatchSave = batchSave;
          executor.execute(() -> {
            depositApplyService.saveAll(finalBatchSave);
            countDownLatch.countDown();
          });
          batchSave = new ArrayList<>();
        }
      }
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    log.info("员工账号存储申请: 存储数据库成功！{}条数据 requestId:{}, fileId:{}", list.size(), requestId, fileId);
  }

  public int page(int size) {
    if (size % BATCH_COUNT == 0) {
      return size / BATCH_COUNT;
    } else {
      return size / BATCH_COUNT + 1;
    }
  }

  private TempAccountDepositApply getTempAccountDepositApply(AccountDepositRequest request){
    TempAccountDepositApply apply = new TempAccountDepositApply();
    apply.setAccountCode(request.getAccountCode());
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
  public void doAfterAllAnalysed(AnalysisContext context) {
    // 这里也要保存数据，确保最后遗留的数据也存储到数据库
    saveData();
    log.info("员工账号存储申请：所有数据解析完成！requestId:{}, fileId;{}", requestId, fileId);
  }

}