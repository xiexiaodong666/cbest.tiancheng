package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.AccountUtil;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.TempAccountDepositApply;
import com.welfare.service.TempAccountDepositApplyService;
import com.welfare.service.dto.AccountDepositRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

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

  private static final BigDecimal MAX_AMOUNT = BigDecimal.valueOf(99999999.99);

  private static final Integer MAX_COUNT = 1000;

  private List<TempAccountDepositApply> applyList = new ArrayList<>();

  private TempAccountDepositApplyService depositApplyService;

  private AccountDao accountDao;

  private String requestId;

  private String fileId;

  private String merCode;

  private Set<String> phoneSet = new HashSet<>(1000);

  private List<AccountDepositRequest> requestList = new ArrayList<>(1000);

  public DepositApplyUploadListener(TempAccountDepositApplyService depositApplyService,
                                    String requestId, String fileId,AccountDao accountDao,
                                    String merCode) {
    this.depositApplyService = depositApplyService;
    this.requestId = requestId;
    this.fileId = fileId;
    this.accountDao = accountDao;
    this.merCode = merCode;
  }

  @Override
  public void invoke(AccountDepositRequest request, AnalysisContext context) {
    log.info("员工账号存储申请：解析到一条数据:{}, requestId:{}, fileId;{}", JSON.toJSONString(request), requestId, fileId);
    if (StringUtils.isBlank(request.getPhone())) {
      throw new BusiException("账号不能为空");
    }
//    if (!AccountUtil.validPhone(request.getPhone())) {
//      throw new BusiException(String.format("[%s]手机号不合法！", request.getPhone()));
//    }
    if (request.getRechargeAmount() == null || request.getRechargeAmount().compareTo(BigDecimal.ZERO) < 0) {
      throw new BusiException(String.format("[%s]金额不能小于0！", request.getPhone()));
    }
    if (request.getRechargeAmount().compareTo(MAX_AMOUNT) > 0) {
      throw new BusiException(String.format("[%s]金额超过限制[%s]！", request.getPhone(), MAX_AMOUNT));
    }
    if (phoneSet.contains(request.getPhone().trim())) {
      throw new BusiException(String.format("[%s]账号(手机号)不能重复！", request.getPhone()));
    }
    request.setPhone(request.getPhone().trim());
    phoneSet.add(request.getPhone().trim());
    if (phoneSet.size() > MAX_COUNT) {
      throw new BusiException("超过单次充值上限[1000]");
    }
    requestList.add(request);
  }

  /**
   * 所有数据解析完成了 都会来调用
   *
   * @param context
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void doAfterAllAnalysed(AnalysisContext context) {
    // 校验员工是否都存在
    Map<String, Account> accountMap = validateAccountIsExist();
    // 封装数据
    assembly(accountMap);
    // 保存数据
    saveData();
    log.info("员工账号存储申请：所有数据解析完成！requestId:{}, fileId;{}", requestId, fileId);
  }

  private Map<String, Account> validateAccountIsExist() {
    Map<String, Account> accountMap = accountDao.mapByMerCodeAndPhones(merCode, phoneSet);
    if (org.springframework.util.CollectionUtils.isEmpty(accountMap)) {
      throw new BusiException("商户下没有当前导入的员工");
    }
    Set<String> phones = accountMap.keySet();
    if (accountMap.size() != phoneSet.size() || !phoneSet.containsAll(phones)) {
      requestList.forEach(request -> {
        if (!phones.contains(request.getPhone())) {
          throw new BusiException(String.format("商户下没有[%s]员工！", request.getPhone()));
        }
      });
    }
    return accountMap;
  }

  private void assembly(Map<String, Account> accountMap) {
     requestList.forEach(request -> {
       Account account = accountMap.get(request.getPhone());
       if (Objects.isNull(account)) {
         throw new BusiException(String.format("商户下没有[%s]员工！", request.getPhone()));
       }
       TempAccountDepositApply apply = getTempAccountDepositApply(request, account);
       applyList.add(apply);
     });
  }

  /**
   * 加上存储数据库
   */
  private void saveData() {
    if (CollectionUtils.isEmpty(applyList)) {
      throw new BusiException("请至少上传一个员工");
    }
    log.info("员工账号存储申请：{}条数据，开始存储数据库！requestId:{}, fileId:{}", applyList.size(), requestId, fileId);
    depositApplyService.saveAll(applyList);
    log.info("员工账号存储申请: 存储数据库成功！{}条数据 requestId:{}, fileId:{}", applyList.size(), requestId, fileId);
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
}