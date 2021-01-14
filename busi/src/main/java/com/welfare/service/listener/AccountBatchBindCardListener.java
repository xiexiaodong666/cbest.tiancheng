package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.welfare.common.constants.WelfareConstant.CardStatus;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.CardApplyDao;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.dto.AccountBindCardDTO;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/11 10:44
 */
@Slf4j
public class AccountBatchBindCardListener extends AnalysisEventListener<AccountBindCardDTO> {
  List<CardInfo> cardInfoList = new LinkedList<CardInfo>();
  List<Long> accountIdList = new LinkedList<Long>();

  private CardInfoDao cardInfoDao;
  private AccountDao accountDao;
  private CardApplyDao cardApplyDao;
  private static StringBuilder  uploadInfo = new StringBuilder();

  public AccountBatchBindCardListener(CardInfoDao cardInfoDao,
      AccountDao accountDao, CardApplyDao cardApplyDao) {
    this.cardInfoDao = cardInfoDao;
    this.accountDao = accountDao;
    this.cardApplyDao = cardApplyDao;
  }

  @Override
  public void invoke(AccountBindCardDTO accountBindCardDTO, AnalysisContext analysisContext) {
    QueryWrapper<CardApply> cardApplyQueryWrapper = new  QueryWrapper<CardApply>();
    cardApplyQueryWrapper.eq(CardApply.APPLY_CODE,accountBindCardDTO.getApplyCode());
    CardApply cardApply = cardApplyDao.getOne(cardApplyQueryWrapper);
    if(  null == cardApply){
      uploadInfo.append("申请编码:").append(accountBindCardDTO.getApplyCode()).append("不存在;");
      return;
    }
    QueryWrapper<Account> accountQueryWrapper = new  QueryWrapper<Account>();
    accountQueryWrapper.eq(Account.ACCOUNT_CODE,accountBindCardDTO.getAccountCode());
    Account account = accountDao.getOne(accountQueryWrapper);
    if( null == account ){
      uploadInfo.append("员工账号:").append(accountBindCardDTO.getAccountCode()).append("不存在;");
      return;
    }
    CardInfo cardInfo = new CardInfo();
    BeanUtils.copyProperties(accountBindCardDTO,cardInfo);
    cardInfo.setBindTime(new Date());
    cardInfo.setCardStatus(CardStatus.BIND.code());
    cardInfoList.add(cardInfo);
    accountIdList.add(account.getId());
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if(!CollectionUtils.isEmpty(cardInfoList)){
      Boolean result = cardInfoDao.saveBatch(cardInfoList);
      updateAccountBindStatus();
      if( result == true && StringUtils.isEmpty(uploadInfo.toString()) ){
        uploadInfo.append("导入成功");
      }
    }
  }

  private void updateAccountBindStatus() {
    UpdateWrapper<Account> accountUpdateWrapper = new UpdateWrapper();
    accountUpdateWrapper.in(Account.ID,accountIdList);
    Account updateAccount = new Account();
    updateAccount.setBinding(1);
    accountDao.update(updateAccount,accountUpdateWrapper);
  }

  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
