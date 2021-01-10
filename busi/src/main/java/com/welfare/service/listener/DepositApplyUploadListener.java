package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.welfare.persist.entity.TempAccountDepositApply;
import com.welfare.service.TempAccountDepositApplyService;
import com.welfare.service.dto.AccountDepositRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

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
  private static final int BATCH_COUNT = 200;

  private List<AccountDepositRequest> list = new ArrayList<>();

  /**
   * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
   */
  private TempAccountDepositApplyService depositApplyService;

  private String requestId;

  private String fileId;

  public DepositApplyUploadListener() {}

  public DepositApplyUploadListener(TempAccountDepositApplyService depositApplyService,
                                    String requestId, String fileId) {
    this.depositApplyService = depositApplyService;
    this.requestId = requestId;
    this.fileId = fileId;
  }

  @Override
  public void invoke(AccountDepositRequest request, AnalysisContext context) {
    log.info("员工账号存储申请：解析到一条数据:{}, requestId:{}, fileId;{}", JSON.toJSONString(request), requestId, fileId);
    list.add(request);
    // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
    if (list.size() >= BATCH_COUNT) {
      saveData();
      // 存储完成清理 list
      list.clear();
    }
  }

  /**
   * 加上存储数据库
   */
  private void saveData() {
    log.info("员工账号存储申请：{}条数据，开始存储数据库！requestId:{}, fileId:{}", list.size(), requestId, fileId);
    List<TempAccountDepositApply> applies = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(list)) {
      list.forEach(request -> {
        TempAccountDepositApply apply = new TempAccountDepositApply();
        apply.setAccountCode(request.getAccountCode());
        apply.setRechargeAmount(request.getRechargeAmount());
        apply.setFileId(fileId);
        apply.setRequestId(requestId);
        applies.add(apply);
      });
    }
    depositApplyService.saveAll(applies);
    log.info("员工账号存储申请: 存储数据库成功！{}条数据 requestId:{}, fileId:{}", list.size(), requestId, fileId);
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