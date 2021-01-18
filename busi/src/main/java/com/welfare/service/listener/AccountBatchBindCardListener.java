package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.welfare.common.constants.AccountBindStatus;
import com.welfare.common.constants.WelfareConstant.CardStatus;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.CardApplyDao;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.AccountService;
import com.welfare.service.CardInfoService;
import com.welfare.service.dto.AccountBindCardDTO;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.BindStatus;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/11 10:44
 */
@Slf4j
public class AccountBatchBindCardListener extends AnalysisEventListener<AccountBindCardDTO> {
  List<CardInfo> cardInfoList = new LinkedList<CardInfo>();
  List<Account> accountList = new LinkedList<Account>();

  private AccountDao accountDao;
  private CardInfoService cardInfoService;
  private AccountService accountService;
  private static StringBuilder  uploadInfo = new StringBuilder();

  public AccountBatchBindCardListener(AccountDao accountDao,CardInfoService cardInfoService,AccountService accountService) {
    this.accountDao = accountDao;
    this.cardInfoService = cardInfoService;
    this.accountService = accountService;
  }

  @Override
  public void invoke(AccountBindCardDTO accountBindCardDTO, AnalysisContext analysisContext) {
    QueryWrapper<Account> accountQueryWrapper = new  QueryWrapper<Account>();
    accountQueryWrapper.eq(Account.ACCOUNT_CODE,Long.parseLong(accountBindCardDTO.getAccountCode()));
    Account account = accountDao.getOne(accountQueryWrapper);
    if( null == account ){
      uploadInfo.append("员工账号:").append(accountBindCardDTO.getAccountCode()).append("不存在;");
      return;
    }
    CardInfo cardInfo = cardInfoService.getByCardNo(accountBindCardDTO.getCardId());
    if( null ==  cardInfo){
      uploadInfo.append("卡号:").append(accountBindCardDTO.getCardId()).append("不存在;");
      return;
    }
    if( cardInfoService.cardIsBind(accountBindCardDTO.getCardId()) ){
      uploadInfo.append("卡号:").append(accountBindCardDTO.getCardId()).append("已经绑定其他账号;");
      return;
    }
    cardInfo.setAccountCode(account.getAccountCode());
    cardInfo.setBindTime(new Date());
    cardInfo.setCardStatus(CardStatus.BIND.code());
    cardInfoList.add(cardInfo);
    account.setBinding(AccountBindStatus.BIND.getCode());
    accountList.add(account);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if(!CollectionUtils.isEmpty(cardInfoList)){
      accountService.batchBindCard(cardInfoList,accountList);
      if(StringUtils.isEmpty(uploadInfo.toString()) ){
        uploadInfo.append("导入成功");
      }
    }
  }


  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
