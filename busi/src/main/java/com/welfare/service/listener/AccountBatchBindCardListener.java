package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.CardApplyDao;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.dto.AccountBindCardDTO;
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
    cardInfoList.add(cardInfo);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if(!CollectionUtils.isEmpty(cardInfoList)){
      Boolean result = cardInfoDao.saveBatch(cardInfoList);
      if( result == true && StringUtils.isEmpty(uploadInfo.toString()) ){
        uploadInfo.append("导入成功");
      }
    }
  }

  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
