package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.AccountStatus;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.EnableEnum;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.dto.CardInfoApiDTO;
import com.welfare.persist.dto.CardInfoDTO;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.mapper.CardInfoMapper;
import com.welfare.service.CardInfoService;

import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 卡信息服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CardInfoServiceImpl implements CardInfoService {

    private final CardInfoDao cardInfoDao;
    private final CardInfoMapper cardInfoMapper;
    private final AccountDao accountDao;

    @Override
    public CardInfo getByCardNo(String cardNo) {
        QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CardInfo.CARD_ID, cardNo);
        return cardInfoDao.getOne(queryWrapper);
    }

    @Override
    public CardInfo getByMagneticStripe(String magneticStripe) {
        QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CardInfo.MAGNETIC_STRIPE, magneticStripe);
        return cardInfoDao.getOne(queryWrapper);
    }

    @Override
    public List<CardInfoApiDTO> listByApplyCode(String applyCode, Integer status) {

        return cardInfoMapper.listByApplyCode(applyCode, status);
    }

    @Override
    public CardInfo updateWritten(CardInfo cardInfo) {

        cardInfo = cardInfoDao.getById(cardInfo.getId());
        if (cardInfo == null) {
            throw new BizException(ExceptionCode.DATA_BASE_ERROR, "更新错误", null);
        }
        if (WelfareConstant.CardStatus.NEW.code().equals(cardInfo.getCardStatus())) {
            cardInfo.setCardStatus(WelfareConstant.CardStatus.WRITTEN.code());
            cardInfo.setWrittenTime(new Date());

            if (cardInfoDao.saveOrUpdate(cardInfo)) {
                return cardInfo;
            } else {
                throw new BizException(ExceptionCode.DATA_BASE_ERROR, "更新错误", null);
            }
        }

        return cardInfo;
    }

    @Override
    public boolean disableCard(Set<String> cardIdSet, Integer enabled) {

        QueryWrapper<CardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(CardInfo.CARD_ID, cardIdSet);
        List<CardInfo> cardInfoList = cardInfoDao.list(queryWrapper);
        if (CollectionUtils.isEmpty(cardInfoList)) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "未找到对应卡号", null);
        }

        for (CardInfo cardInfo :
                cardInfoList) {
            cardInfo.setEnabled(enabled);
        }

        return cardInfoDao.saveOrUpdateBatch(cardInfoList);
    }

    @Override
    public Page<CardInfoDTO> list(Integer currentPage, Integer pageSize, String cardId,
                                  String applyCode, String cardName,
                                  String merCode,
                                  String cardType, String cardMedium, Integer cardStatus, Date writtenStartTime,
                                  Date writtenEndTime, Date startTime, Date endTime, Date bindStartTime,
                                  Date bindEndTime) {
        Page<CardInfo> page = new Page<>(currentPage, pageSize);

        return cardInfoMapper.list(page, cardId, applyCode, cardName,
                merCode, cardType, cardMedium,
                cardStatus, writtenStartTime,
                writtenEndTime, startTime,
                endTime, bindStartTime, bindEndTime
        );
    }

    @Override
    public List<CardInfoDTO> exportCardInfo(String cardId, String cardName, String merCode,
                                            String cardType,
                                            String cardMedium, Integer cardStatus, Date writtenStartTime, Date writtenEndTime,
                                            Date startTime, Date endTime, Date bindStartTime, Date bindEndTime) {
        return cardInfoMapper.exportCardInfo(cardId, cardName,
                merCode, cardType, cardMedium,
                cardStatus, writtenStartTime,
                writtenEndTime, startTime,
                endTime, bindStartTime, bindEndTime
        );
    }

    @Override
    public boolean cardIsBind(String cardId) {
        QueryWrapper<CardInfo> cardInfoQueryWrapper = new QueryWrapper<>();
        cardInfoQueryWrapper.eq(CardInfo.CARD_ID, cardId);
        cardInfoQueryWrapper.isNotNull(CardInfo.ACCOUNT_CODE);
        CardInfo queryCardInfo = cardInfoDao.getOne(cardInfoQueryWrapper);
        return null != queryCardInfo;
    }

    @Override
    public CardInfo createAndBind(CardInfo cardInfo) {

        Account account = accountDao.queryByAccountCode(cardInfo.getAccountCode());
        BizAssert.notNull(account,ExceptionCode.ACCOUNT_NOT_EXIST);
        BizAssert.isTrue(
                AccountStatus.ENABLE.getCode().equals(account.getAccountStatus()),
                ExceptionCode.ACCOUNT_DISABLED
        );
        CardInfo oneByAccountCode = cardInfoDao.getOneByAccountCode(account.getAccountCode());
        BizAssert.isNull(oneByAccountCode,ExceptionCode.ACCOUNT_ALREADY_BIND);
        CardInfo cardInfoInDb = cardInfoDao.getOneByCardId(cardInfo.getCardId());
        if(Objects.nonNull(cardInfoInDb)){
            return onCardExisted(cardInfo, cardInfoInDb);
        }else{
            cardInfo.setCardStatus(WelfareConstant.CardStatus.BIND.code());
            Date now = Calendar.getInstance().getTime();
            cardInfo.setBindTime(now);
            cardInfo.setWrittenTime(now);
            cardInfo.setEnabled(EnableEnum.ENABLE.getCode());
            cardInfo.setApplyCode("");
            cardInfoDao.save(cardInfo);
        }

        return cardInfo;
    }

    private CardInfo onCardExisted(CardInfo cardInfo, CardInfo cardInfoInDb) {
        if(Objects.nonNull(cardInfoInDb.getAccountCode())
                && !Objects.equals(cardInfo.getAccountCode(), cardInfoInDb.getAccountCode())){
            throw new BizException(ExceptionCode.CARD_ALREADY_BIND);
        } else if (Objects.equals(cardInfo.getAccountCode(), cardInfoInDb.getAccountCode())){
            log.info("already bind, return");
            return cardInfoInDb;
        } else {
            log.info("already existed but not bind, directly bind");
            cardInfoInDb.setAccountCode(cardInfo.getAccountCode());
            cardInfoInDb.setEnabled(EnableEnum.ENABLE.getCode());
            cardInfoInDb.setCardStatus(WelfareConstant.CardStatus.BIND.code());
            cardInfoDao.updateById(cardInfoInDb);
            return cardInfoInDb;
        }
    }

    @Override
    public CardInfo unbind(String cardNo) {
        CardInfo cardInfo = cardInfoDao.getOneByCardId(cardNo);
        cardInfoDao.update(Wrappers.<CardInfo>lambdaUpdate()
                .eq(CardInfo::getId,cardInfo.getId())
                .set(CardInfo::getAccountCode,null)
                .set(CardInfo::getEnabled,EnableEnum.DISABLE.getCode())
                .set(CardInfo::getCardStatus, WelfareConstant.CardStatus.NEW));
        cardInfo.setAccountCode(null);
        cardInfo.setEnabled(EnableEnum.DISABLE.getCode());
        return cardInfo;
    }
}