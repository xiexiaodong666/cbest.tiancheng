package com.welfare.service.settlement.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareSettleConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DateUtil;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.EmployeeSettleDetailDao;
import com.welfare.persist.dto.EmployeeSettleConsumeDTO;
import com.welfare.persist.dto.EmployeeSettleDetailDTO;
import com.welfare.persist.dto.EmployeeSettleStoreDTO;
import com.welfare.persist.dto.EmployeeSettleSumDTO;
import com.welfare.persist.dto.query.EmployeeSettleConsumeQuery;
import com.welfare.persist.dto.query.EmployeeSettleDetailQuery;
import com.welfare.persist.entity.EmployeeSettleDetail;
import com.welfare.persist.entity.MerchantBillDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.EmployeeSettleDetailMapper;
import com.welfare.service.dto.EmployeeSettleConsumePageReq;
import com.welfare.service.dto.EmployeeSettleDetailPageReq;
import com.welfare.service.dto.EmployeeSettleDetailReq;
import com.welfare.service.dto.EmployeeSettleDetailResp;
import com.welfare.service.dto.StoreCodeNameDTO;
import com.welfare.service.settlement.EmployeeSettleDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;

/**
 * 服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeSettleDetailServiceImpl implements EmployeeSettleDetailService {
    private final EmployeeSettleDetailDao employeeSettleDetailDao;

    private final EmployeeSettleDetailMapper employeeSettleDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pullAccountDetailByDate(Date date) {
        try {
            EmployeeSettleDetailMapper employeeSettleDetailMapper = employeeSettleDetailDao.getBaseMapper();
            Map<String, Object> params = new HashMap<>();
            if (date != null) {
                Date dateStart = DateUtil.getDayMin(date, -1);
                Date dateEnd = DateUtil.getDayMax(date, -1);
                params.put("startTime", dateStart);
                params.put("endTime", dateEnd);
            }
            params.put("minId", 0);
            params.put("limit", 2000);
            do {
                log.info("employeeSettleDetailMapper循环拉取账户授信详细交易数据环拉取账户授信详细交易数据，请求参数：{}", JSONObject.toJSONString(params));
                List<EmployeeSettleDetail> settleDetails = employeeSettleDetailMapper.getFromAccountDetail(params);
                if (!settleDetails.isEmpty()) {
                    params.put("minId", settleDetails.get(settleDetails.size() - 1).getAccountDeductionAmountId() + 1);
                    employeeSettleDetailDao.saveBatch(settleDetails, settleDetails.size());
                } else {
                    break;
                }
            } while (true);
        } catch (Exception e) {
            String dateStr = "";
            if (date != null) {
                dateStr = DateUtil.date2Str(DateUtil.getDayMin(date, -1), DateUtil.DEFAULT_DATE_FORMAT);
            }
            log.error("同步{}的员工授信流水数据失败", dateStr, e);
            throw new BusiException(String.format("同步%s的员工授信流水数据失败", dateStr));
        }
    }

    @Override
    public BasePageVo<EmployeeSettleConsumeDTO> pageQuery(EmployeeSettleConsumePageReq employeeSettleConsumePageReq) {

        EmployeeSettleConsumeQuery employeeSettleConsumeQuery = new EmployeeSettleConsumeQuery();
        BeanUtils.copyProperties(employeeSettleConsumePageReq, employeeSettleConsumeQuery);
        employeeSettleConsumeQuery.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code());
        PageInfo<EmployeeSettleConsumeDTO> employeeSettleDTOPageInfo = PageHelper.startPage(employeeSettleConsumePageReq.getCurrent(), employeeSettleConsumePageReq.getSize())
                .doSelectPageInfo(() -> employeeSettleDetailMapper.getEmployeeSettleConsumeList(employeeSettleConsumeQuery));

        BasePageVo<EmployeeSettleConsumeDTO> employeeSettleRespBasePageVo = new BasePageVo<>(employeeSettleConsumePageReq.getCurrent(),
                employeeSettleConsumePageReq.getSize(), employeeSettleDTOPageInfo.getTotal(), employeeSettleDTOPageInfo.getList());

        return employeeSettleRespBasePageVo;
    }

    @Override
    public EmployeeSettleSumDTO summary(EmployeeSettleConsumeQuery employeeSettleConsumeQuery) {
        employeeSettleConsumeQuery.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code());
        EmployeeSettleSumDTO result = employeeSettleDetailMapper.getEmployeeSettleConsumeSum(employeeSettleConsumeQuery);
        return result == null ?  new EmployeeSettleSumDTO() : result;
    }

    @Override
    public EmployeeSettleSumDTO detailSummary(String accountCode, EmployeeSettleDetailReq employeeSettleDetailReq) {
        EmployeeSettleDetailQuery employeeSettleDetailQuery = new EmployeeSettleDetailQuery();
        BeanUtils.copyProperties(employeeSettleDetailReq, employeeSettleDetailQuery);
        employeeSettleDetailQuery.setAccountCode(accountCode);
        employeeSettleDetailQuery.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code());
        EmployeeSettleSumDTO result = employeeSettleDetailMapper.getEmployeeSettleDetailSum(employeeSettleDetailQuery);
        return result == null ?  new EmployeeSettleSumDTO() : result;
    }

    @Override
    public BasePageVo<EmployeeSettleDetailResp> pageQueryDetail(String accountCode, EmployeeSettleDetailPageReq employeeSettleDetailPageReq) {
        EmployeeSettleDetailQuery employeeSettleDetailQuery = new EmployeeSettleDetailQuery();
        BeanUtils.copyProperties(employeeSettleDetailPageReq, employeeSettleDetailQuery);
        employeeSettleDetailQuery.setAccountCode(accountCode);
        employeeSettleDetailQuery.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code());
        PageInfo<EmployeeSettleDetailResp> employeeSettleDetailResp = PageHelper.startPage(employeeSettleDetailPageReq.getCurrent(),
                employeeSettleDetailPageReq.getSize()).doSelectPageInfo(() -> {
                    employeeSettleDetailMapper.querySettleDetail(employeeSettleDetailQuery).stream().map(employeeSettleDetailDTO -> {
                        EmployeeSettleDetailResp resp= new EmployeeSettleDetailResp();
                        BeanUtils.copyProperties(employeeSettleDetailDTO, resp);
                        return resp;
                    });
                });
        return new BasePageVo<>(employeeSettleDetailPageReq.getCurrent(), employeeSettleDetailPageReq.getSize(),
                employeeSettleDetailResp.getTotal(), employeeSettleDetailResp.getList());
    }

    @Override
    public List<EmployeeSettleDetailResp> detailExport(String accountCode, EmployeeSettleDetailReq employeeSettleDetailReq) {
        EmployeeSettleDetailQuery employeeSettleDetailQuery = new EmployeeSettleDetailQuery();
        BeanUtils.copyProperties(employeeSettleDetailReq, employeeSettleDetailQuery);
        employeeSettleDetailQuery.setAccountCode(accountCode);
        employeeSettleDetailQuery.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code());
        employeeSettleDetailQuery.setLimit(WelfareSettleConstant.LIMIT);
        List<EmployeeSettleDetailResp> employeeSettleDetailList =
            employeeSettleDetailMapper.querySettleDetail(employeeSettleDetailQuery).stream().map(employeeSettleDetailDTO -> {
                EmployeeSettleDetailResp resp= new EmployeeSettleDetailResp();
                BeanUtils.copyProperties(employeeSettleDetailDTO, resp);
                return resp;
            }).collect(Collectors.toList());
        return employeeSettleDetailList;
    }

    @Override
    public Page<EmployeeSettleDetailResp> pageQueryEmployeeSettleDetail(String settleNo, EmployeeSettleDetailPageReq req) {
        EmployeeSettleDetailQuery query = new EmployeeSettleDetailQuery();
        BeanUtils.copyProperties(req, query);
        query.setSettleNo(settleNo);
        PageInfo<EmployeeSettleDetailDTO> pageInfo = PageHelper.startPage(req.getCurrent(), req.getSize()).doSelectPageInfo(()->employeeSettleDetailMapper.querySettleDetail(query));
        Page<EmployeeSettleDetailResp> respPage = new Page<>(req.getCurrent(), req.getSize(),pageInfo.getTotal());

        if(pageInfo.getList().isEmpty()){
            return respPage;
        }

        respPage.setRecords(pageInfo.getList().stream().map(dto -> {
            EmployeeSettleDetailResp resp = new EmployeeSettleDetailResp();
            BeanUtils.copyProperties(dto, resp);
            return resp;
        }).collect(Collectors.toList()));

        return respPage;
    }

    @Override
    public EmployeeSettleSumDTO detailSummaryWithSettleNo(String settleNo, EmployeeSettleDetailReq employeeSettleDetailReq) {
        EmployeeSettleDetailQuery employeeSettleDetailQuery = new EmployeeSettleDetailQuery();
        BeanUtils.copyProperties(employeeSettleDetailReq, employeeSettleDetailQuery);
        employeeSettleDetailQuery.setSettleNo(settleNo);
        EmployeeSettleSumDTO result = employeeSettleDetailMapper.getEmployeeSettleDetailSum(employeeSettleDetailQuery);
        return result == null ? new EmployeeSettleSumDTO() : result;
    }

    @Override
    public List<EmployeeSettleDetailResp> detailExportWithSettleNo(String settleNo, EmployeeSettleDetailReq employeeSettleDetailReq) {
        EmployeeSettleDetailQuery employeeSettleDetailQuery = new EmployeeSettleDetailQuery();
        BeanUtils.copyProperties(employeeSettleDetailReq, employeeSettleDetailQuery);
        employeeSettleDetailQuery.setSettleNo(settleNo);
        employeeSettleDetailQuery.setSettleFlag(WelfareSettleConstant.SettleStatusEnum.UNSETTLED.code());
        employeeSettleDetailQuery.setLimit(WelfareSettleConstant.LIMIT);
        return employeeSettleDetailMapper.querySettleDetail(employeeSettleDetailQuery).stream().map(employeeSettleDetailDTO -> {
                EmployeeSettleDetailResp resp= new EmployeeSettleDetailResp();
                BeanUtils.copyProperties(employeeSettleDetailDTO, resp);
                return resp;
            }).collect(Collectors.toList());
    }

    @Override
    public List<StoreCodeNameDTO> allStoresInMonthSettle(String settleNo, String accountCode) {
        List<EmployeeSettleStoreDTO> dtos = employeeSettleDetailMapper.allStoresInMonthSettle(settleNo, accountCode);
        List<StoreCodeNameDTO> list = new ArrayList<>();
        dtos.forEach(dto -> {
            StoreCodeNameDTO storeCodeNameDTO = new StoreCodeNameDTO();
            BeanUtils.copyProperties(dto, storeCodeNameDTO);
            list.add(storeCodeNameDTO);
        });
        return list;
    }
}