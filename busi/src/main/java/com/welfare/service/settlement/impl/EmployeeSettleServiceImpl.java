package com.welfare.service.settlement.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.DateUtil;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.EmployeeSettleDao;
import com.welfare.persist.dao.EmployeeSettleDetailDao;
import com.welfare.persist.dto.*;
import com.welfare.persist.dto.query.EmployeeSettleBillQuery;
import com.welfare.persist.dto.query.EmployeeSettleBuildQuery;
import com.welfare.persist.entity.EmployeeSettle;
import com.welfare.persist.entity.EmployeeSettleDetail;
import com.welfare.persist.mapper.EmployeeSettleDetailMapper;
import com.welfare.persist.mapper.EmployeeSettleMapper;
import com.welfare.service.AccountService;
import com.welfare.service.SequenceService;
import com.welfare.service.dto.*;
import com.welfare.service.remote.AccountCreditFeign;
import com.welfare.service.remote.entity.WelfareResp;
import com.welfare.service.settlement.EmployeeSettleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.*;

/**
 * 商户员工结算账单服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeSettleServiceImpl implements EmployeeSettleService {
    private final EmployeeSettleDao employeeSettleDao;
    private final EmployeeSettleMapper employeeSettleMapper;
    private final EmployeeSettleDetailMapper employeeSettleDetailMapper;
    private final EmployeeSettleDetailDao employeeSettleDetailDao;
    private final RedissonClient redissonClient;
    private final SequenceService sequenceService;
    @Autowired(required = false)
    private AccountCreditFeign accountCreditFeign;
    @Autowired
    private AccountService accountService;
    @Override
    public Page<EmployeeSettleBillResp> pageQueryBill(EmployeeSettleBillPageReq billPageReq) {

        EmployeeSettleBillQuery billQuery = new EmployeeSettleBillQuery();
        BeanUtils.copyProperties(billPageReq, billQuery);

        //传入的日期，修订未当日最大和当日最小值
        if(billQuery.getStartTime()!=null){
            billQuery.setStartTime(DateUtil.getDayMin(billQuery.getStartTime(), 0));
        }
        if(billQuery.getEndTime()!=null){
            billQuery.setEndTime(DateUtil.getDayMax(billQuery.getEndTime(), 0));
        }

        PageInfo<EmployeeSettleBillDTO> dtoPage = PageHelper.startPage(billPageReq.getCurrent(), billPageReq.getSize()).doSelectPageInfo(
            () -> employeeSettleMapper.querySettleBills(billQuery));

        Page<EmployeeSettleBillResp> respPage = new Page<>(billPageReq.getCurrent(), billPageReq.getSize(),dtoPage.getTotal());

        if(dtoPage.getList().isEmpty()){
            return respPage;
        }

        respPage.setRecords(dtoPage.getList().stream().map(dto -> {
            EmployeeSettleBillResp resp = new EmployeeSettleBillResp();
            BeanUtils.copyProperties(dto, resp);
            return resp;
        }).collect(Collectors.toList()));

        return respPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> buildEmployeeSettle(EmployeeSettleBuildReq settleBuildReq) {
        if (CollectionUtils.isEmpty(settleBuildReq.getSelectedAccountCodes())) {
            throw new BusiException("至少勾选一个员工！");
        }
        List<RLock> locks = new ArrayList<>();
        RLock multiLock;
        settleBuildReq.getSelectedAccountCodes().forEach(accountCode -> {
            RLock lock = redissonClient.getFairLock(BUILD_EMPLOYEE_SETTLE + accountCode);
            locks.add(lock);
        });
        multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[]{}));
        multiLock.lock(-1, TimeUnit.SECONDS);
        try {
            EmployeeSettleBuildQuery query = new EmployeeSettleBuildQuery();
            BeanUtils.copyProperties(settleBuildReq, query);
            query.setMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
            List<EmployeeSettleDetail> settleDetails = employeeSettleDetailMapper.getBuildEmployeeSettleDetail(query);
            if (CollectionUtils.isEmpty(settleDetails)) {
                throw new BusiException("没有符合条件结算数据！");
            }
            List<Long> accountCodes = settleDetails.stream().map(EmployeeSettleDetail::getAccountCode).collect(Collectors.toList());
            settleBuildReq.getSelectedAccountCodes().forEach(accountCode -> {
                if (!accountCodes.contains(Long.parseLong(accountCode))) {
                    throw new BusiException(String.format("员工[%s]没有可结算的数据！", accountCode));
                }
            });
            List<EmployeeSettle> employeeSettles = assemblyEmployeeSettles(settleDetails, settleBuildReq);
            boolean flag1 = employeeSettleDao.saveBatch(employeeSettles);
            boolean flag2 = employeeSettleDetailDao.updateBatchById(settleDetails);
            if (!flag1 || !flag2) {
                throw new BusiException("操作繁忙，请稍后再试！");
            }
            return employeeSettles.stream().map(EmployeeSettle::getSettleNo).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("生成员工授信结算单失败,请求:{}", JSON.toJSONString(settleBuildReq), e);
            throw e;
        } finally {
            DistributedLockUtil.unlock(multiLock);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean finishEmployeeSettle(EmployeeSettleFinishReq employeeSettleFinishReq) {
        List<String> settleNos = employeeSettleFinishReq.getSettleNos();
        if (CollectionUtils.isEmpty(settleNos)) {
            throw new BusiException("至少勾选一个员工！");
        }
        List<RLock> locks = new ArrayList<>();
        RLock multiLock;
        settleNos.forEach(settleNo -> {
            RLock lock = redissonClient.getFairLock(FINISH_EMPLOYEE_SETTLE + settleNo);
            locks.add(lock);
        });
        multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[]{}));
        multiLock.lock(-1, TimeUnit.SECONDS);
        try {
            List<EmployeeSettle> employeeSettles = employeeSettleDao.listBySettleNos(settleNos);
            List<String> getSettleNos = employeeSettles.stream().map(EmployeeSettle::getSettleNo).collect(Collectors.toList());
            settleNos.forEach(settleNo -> {
                if (!getSettleNos.contains(settleNo)) {
                    throw new BusiException(String.format("结算单[%s]不存在！", settleNo));
                }
            });
            employeeSettles.forEach(employeeSettle -> {
                if (WelfareSettleConstant.SettleStatusEnum.SETTLED.code().equals(employeeSettle.getSettleStatus())) {
                    throw new BusiException(String.format("结算单[%s]不能重复结算！", employeeSettle.getSettleNo()));
                }
                employeeSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.SETTLED.code());
                employeeSettle.setUppdateUser(MerchantUserHolder.getMerchantUser().getUserCode());
            });
            // 修改结算状态
            boolean flag1 = employeeSettleDao.updateBatchById(employeeSettles);
            boolean flag2 = employeeSettleDetailDao.batchUpdateStatusBySettleNo(
                    WelfareSettleConstant.SettleStatusEnum.SETTLED.code(),
                    MerchantUserHolder.getMerchantUser().getUserCode(),
                    settleNos);
            if (!flag1 || !flag2) {
                throw new BusiException("完成结算失败，请重新操作！");
            }
            // 恢复员工授信额度或溢缴款账户额度
            AccountRestoreCreditLimitReq req = new AccountRestoreCreditLimitReq();
            req.setCreditLimitDtos(AccountRestoreCreditLimitReq.RestoreCreditLimitDTO.of(employeeSettles));
            WelfareResp resp = accountCreditFeign.remainingLimit(req, "settlement-api");
            if (resp == null || resp.getCode() != 1) {
                throw new BusiException("完成结算失败，请重新操作！");
            }
            return true;
        } finally {
            DistributedLockUtil.unlock(multiLock);
        }
    }

    private List<EmployeeSettle> assemblyEmployeeSettles(List<EmployeeSettleDetail> settleDetails, EmployeeSettleBuildReq settleBuildReq) {
        List<EmployeeSettle> employeeSettles = new ArrayList<>();
        Date now = new Date();
        if (CollectionUtils.isNotEmpty(settleDetails)) {
            Map<Long, List<EmployeeSettleDetail>> settleDetailsMap = settleDetails.stream().collect(Collectors.groupingBy(EmployeeSettleDetail::getAccountCode));
            BatchSequence batchSequence = sequenceService.batchGenerate(WelfareConstant.SequenceType.EMPLOYEE_SETTLE_NO.code(), settleDetailsMap.size());
            AtomicInteger sequenceIndex = new AtomicInteger();
            settleDetailsMap.forEach((accountCode, details) -> {
                String settleNo = accountCode + ""
                        + batchSequence.getSequences().get(sequenceIndex.getAndIncrement()).getSequenceNo();
                BigDecimal totalSettleAmount = BigDecimal.ZERO;
                BigDecimal totalConsumerAmount = BigDecimal.ZERO;
                for (EmployeeSettleDetail detail : details) {
                    if (detail.getMerAccountType().equals(WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY.code())
                            && WelfareConstant.TransType.REFUND.code().equals(detail.getTransType())) {
                        totalSettleAmount = totalSettleAmount.add(BigDecimal.ZERO);
                        totalConsumerAmount = totalConsumerAmount.add(detail.getTransAmount().abs());
                    } else if (detail.getMerAccountType().equals(WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA.code())
                            && WelfareConstant.TransType.REFUND.code().equals(detail.getTransType())) {
                        totalSettleAmount = totalSettleAmount.add(detail.getTransAmount().abs().negate());
                        totalConsumerAmount = totalConsumerAmount.add(detail.getTransAmount().abs().negate());
                    } else {
                        totalSettleAmount = totalSettleAmount.add(detail.getTransAmount().abs());
                        totalConsumerAmount = totalConsumerAmount.add(detail.getTransAmount().abs());
                    }
                    detail.setSettleNo(settleNo);
                    detail.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.SETTLING.code());
                }
                EmployeeSettle employeeSettle = new EmployeeSettle();
                employeeSettle.setMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
                employeeSettle.setAccountCode(accountCode);
                employeeSettle.setTransAmount(totalConsumerAmount);
                employeeSettle.setSettleAmount(totalSettleAmount);
                employeeSettle.setOrderNum(details.size());
                employeeSettle.setSettleStatus(WelfareSettleConstant.SettleStatusEnum.SETTLING.code());
                employeeSettle.setBuildTime(now);
                employeeSettle.setCreateUser(MerchantUserHolder.getMerchantUser().getUserCode());
                employeeSettle.setSettleNo(settleNo);
                details = details.stream().sorted(Comparator.comparing(EmployeeSettleDetail::getTransTime)).collect(Collectors.toList());
                if (totalSettleAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusiException(String.format("员工[%s]结算金额[%s]必须大于零！", totalSettleAmount, accountCode));
                }
                if (settleBuildReq.getTransTimeStart() == null) {
                    employeeSettle.setSettleStartTime(details.get(0).getTransTime());
                } else {
                    employeeSettle.setSettleStartTime(settleBuildReq.getTransTimeStart());
                }
                if (settleBuildReq.getTransTimeEnd() == null) {
                    employeeSettle.setSettleEndTime(details.get(details.size() - 1).getTransTime());
                } else {
                    employeeSettle.setSettleEndTime(settleBuildReq.getTransTimeEnd());
                }
                String dateStartStr = DateUtil.date2Str(settleBuildReq.getTransTimeStart(), "yyyy-MM-dd");
                String dateStartEnd = DateUtil.date2Str(settleBuildReq.getTransTimeEnd(), "yyyy-MM-dd");
                employeeSettle.setSettlePeriod(dateStartStr + "至" + dateStartEnd);
                employeeSettles.add(employeeSettle);
            });
        };
        return employeeSettles;
    }
}