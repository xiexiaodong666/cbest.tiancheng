package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.MerchantAccountTypeShowStatusEnum;
import com.welfare.common.enums.MoveDirectionEnum;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.dao.MerchantAccountTypeDao;
import com.welfare.persist.dto.MerchantAccountTypeWithMerchantDTO;
import com.welfare.persist.dto.query.MerchantAccountTypePageReq;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.mapper.MerchantAccountTypeExMapper;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.MerchantService;
import com.welfare.service.SequenceService;
import com.welfare.service.converter.MerchantAccountTypeDetailConverter;
import com.welfare.service.dto.*;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商户信息服务接口实现
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantAccountTypeServiceImpl implements MerchantAccountTypeService {
    private final MerchantAccountTypeDao merchantAccountTypeDao;
    private final MerchantAccountTypeExMapper merchantAccountTypeExMapper;
    @Autowired
    private MerchantService merchantService;
    private final MerchantAccountTypeDetailConverter merchantAccountTypeDetailConverter;
    private final SequenceService sequenceService;
    private final static Long startId = 10000L;
    @Override
    public List<MerchantAccountType> list(MerchantAccountTypeReq req) {
        QueryWrapper q=QueryHelper.getWrapper(req);
        q.eq(MerchantAccountType.SHOW_STATUS,MerchantAccountTypeShowStatusEnum.SHOW.getCode());
        q.orderByDesc(MerchantAccountType.CREATE_TIME);
        return merchantAccountTypeDao.list(q);
    }

    @Override
    public MerchantAccountTypeDetailDTO detail(Long id) {
        MerchantAccountTypeDetailDTO detailDTO=merchantAccountTypeDetailConverter.toD(merchantAccountTypeDao.getById(id));
        if(EmptyChecker.isEmpty(detailDTO)){
            throw new BizException("福利类型不存在");
        }
        detailDTO.setMerName(merchantService.getMerchantByMerCode(detailDTO.getMerCode()).getMerName());
        List<MerchantAccountType> list=this.queryShowedByMerCode(detailDTO.getMerCode());
        if(EmptyChecker.notEmpty(list)){
            List<MerchantAccountTypeDetailDTO.TypeItem> itemList=list.stream().map(item->{
                MerchantAccountTypeDetailDTO.TypeItem typeItem=new MerchantAccountTypeDetailDTO.TypeItem();
                typeItem.setDeductionOrder(item.getDeductionOrder());
                typeItem.setMerAccountTypeName(item.getMerAccountTypeName());
                typeItem.setId(item.getId());
                typeItem.setMerAccountTypeCode(item.getMerAccountTypeCode());
                return typeItem;
            }).collect(Collectors.toList());
            detailDTO.setTypeList(itemList);
        }
        return detailDTO;
    }

    @Override
    public Page<MerchantAccountTypeWithMerchantDTO> page(Page page, MerchantAccountTypePageReq req) {
        return merchantAccountTypeExMapper.listWithMerchant(page, req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(MerchantAccountTypeAddDTO merchantAccountType) {
        if(EmptyChecker.isEmpty(merchantAccountType.getTypeList())){
            throw new BizException("福利类型扣款顺序不能为空");
        }
        List<MerchantAccountType> accountTypeList=new ArrayList<>();
        Date date=new Date();
        for(MerchantAccountTypeAddDTO.TypeItem typeItem:merchantAccountType.getTypeList()){
            MerchantAccountType type=new MerchantAccountType();
            type.setDeductionOrder(typeItem.getDeductionOrder());
            type.setMerCode(merchantAccountType.getMerCode());
            type.setMerAccountTypeName(typeItem.getMerAccountTypeName());
            type.setMerAccountTypeCode(sequenceService.nextNo(
                    WelfareConstant.SequenceType.MER_ACCOUNT_TYPE_CODE.code(), merchantAccountType.getMerCode(), startId,
                    1).toString());
            type.setRemark(merchantAccountType.getRemark());
            type.setCreateTime(date);
            type.setShowStatus(MerchantAccountTypeShowStatusEnum.SHOW.getCode());
            accountTypeList.add(type);
        }
        return merchantAccountTypeDao.saveBatch(accountTypeList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(MerchantAccountTypeUpdateDTO merchantAccountType) {
        if(EmptyChecker.isEmpty(merchantAccountType.getTypeList())){
            throw new BizException("福利类型扣款顺序不能为空");
        }
        MerchantAccountTypeReq req=new MerchantAccountTypeReq();
        req.setMerCode(merchantAccountType.getMerCode());
        //数据库已有的福利类型id
        List<Long> list=list(req).stream().map(item->item.getId()).collect(Collectors.toList());
        //此次更新包含的福利类型id
        List<Long> updateList=merchantAccountType.getTypeList().stream().map(item->item.getId()).collect(Collectors.toList());
        //此次更新需要删除的福利类型
        list.removeAll(updateList);
        merchantAccountTypeDao.removeByIds(list);
        Date date=new Date();
        List<MerchantAccountType> addList=new ArrayList<>();
        for(MerchantAccountTypeUpdateDTO.TypeItem typeItem:merchantAccountType.getTypeList()){
            if(EmptyChecker.isEmpty(typeItem.getId())){
                MerchantAccountType type=new MerchantAccountType();
                type.setDeductionOrder(typeItem.getDeductionOrder());
                type.setMerCode(merchantAccountType.getMerCode());
                type.setMerAccountTypeName(typeItem.getMerAccountTypeName());
                type.setMerAccountTypeCode(sequenceService.nextNo(
                        WelfareConstant.SequenceType.MER_ACCOUNT_TYPE_CODE.code(), merchantAccountType.getMerCode(), startId,
                        1).toString());
                type.setRemark(merchantAccountType.getRemark());
                type.setShowStatus(MerchantAccountTypeShowStatusEnum.SHOW.getCode());
                type.setCreateTime(date);
                addList.add(type);
            }else{
                MerchantAccountType type=merchantAccountTypeDao.getById(typeItem.getId());
                type.setMerCode(merchantAccountType.getMerCode());
                type.setDeductionOrder(typeItem.getDeductionOrder());
                type.setRemark(merchantAccountType.getRemark());
                type.setCreateTime(date);
                type.setUpdateTime(date);
                merchantAccountTypeDao.updateAllColumnById(type);
            }
        }
        merchantAccountTypeDao.saveBatch(addList);
        //无法判断更新是否成功，因为这里有三种更新方式， 没报错默认更新成功
        return Boolean.TRUE;
    }

    @Override
    public List<MerchantAccountTypeWithMerchantDTO> exportList(MerchantAccountTypePageReq pageReq) {
        List<MerchantAccountTypeWithMerchantDTO> list=this.page(new Page(0,Integer.MAX_VALUE),pageReq).getRecords();
        return list;
    }

    @Override
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public boolean moveDeductionsOrder(MerchantAccountTypeSortReq merchantAccountTypeSortReq) {
        MerchantAccountType merchantAccountType=merchantAccountTypeDao.getById(merchantAccountTypeSortReq.getId());
        MerchantAccountTypeReq req=new MerchantAccountTypeReq();
        req.setMerCode(merchantAccountType.getMerCode());
        if(MoveDirectionEnum.UP.getCode().equals(merchantAccountTypeSortReq.getDirection())){
            req.setDeductionOrder(merchantAccountType.getDeductionOrder()+1);
        }
        if(MoveDirectionEnum.DOWN.getCode().equals(merchantAccountTypeSortReq.getDirection())){
            req.setDeductionOrder(merchantAccountType.getDeductionOrder()-1);
        }
        MerchantAccountType type= merchantAccountTypeDao.getOne(QueryHelper.getWrapper(req));
        boolean flag=true;
        if(EmptyChecker.notEmpty(type)){
            type.setDeductionOrder(merchantAccountType.getDeductionOrder());
            flag=merchantAccountTypeDao.updateById(type);
        }
        merchantAccountType.setDeductionOrder(req.getDeductionOrder());
        merchantAccountTypeDao.updateById(merchantAccountType);
        return merchantAccountTypeDao.updateById(merchantAccountType)&&flag;
    }

    @Override
    public MerchantAccountType queryOneByCode(String merCode, String merchantAccountTypeCode) {
        QueryWrapper<MerchantAccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantAccountType.MER_ACCOUNT_TYPE_CODE,merchantAccountTypeCode)
                .eq(MerchantAccountType.MER_CODE,merCode);
        return merchantAccountTypeDao.getOne(queryWrapper);
    }

    @Override
    public List<MerchantAccountType> queryShowedByMerCode(String merCode) {
        QueryWrapper<MerchantAccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantAccountType.MER_CODE,merCode);
        queryWrapper.eq(MerchantAccountType.SHOW_STATUS,MerchantAccountTypeShowStatusEnum.SHOW.getCode());
        return merchantAccountTypeDao.list(queryWrapper);
    }

    @Override
    public List<MerchantAccountType> queryAllByMerCode(String merCode) {
        QueryWrapper<MerchantAccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantAccountType.MER_CODE,merCode);
        return merchantAccountTypeDao.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean init(String merCode, MerchantExtendDTO extend) {
        List<MerchantAccountType> initList=new ArrayList<>();
        MerchantAccountType merchantAccountType1=new MerchantAccountType();
        merchantAccountType1.setMerAccountTypeName("员工授信额度");
        merchantAccountType1.setMerCode(merCode);
        merchantAccountType1.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA.code());
        merchantAccountType1.setDeductionOrder(9999);
        merchantAccountType1.setShowStatus(MerchantAccountTypeShowStatusEnum.UNSHOW.getCode());
        initList.add(merchantAccountType1);
        MerchantAccountType merchantAccountType2=new MerchantAccountType();
        merchantAccountType2.setMerAccountTypeName("自助充值");
        merchantAccountType2.setMerCode(merCode);
        merchantAccountType2.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.SELF.code());
        merchantAccountType2.setDeductionOrder(0);
        merchantAccountType2.setShowStatus(MerchantAccountTypeShowStatusEnum.UNSHOW.getCode());
        initList.add(merchantAccountType2);
        MerchantAccountType merchantAccountType3=new MerchantAccountType();
        merchantAccountType3.setMerAccountTypeName("员工授信额度溢缴款");
        merchantAccountType3.setMerCode(merCode);
        merchantAccountType3.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY.code());
        merchantAccountType3.setDeductionOrder(9000);
        merchantAccountType3.setShowStatus(MerchantAccountTypeShowStatusEnum.UNSHOW.getCode());
        initList.add(merchantAccountType3);

        if (Objects.nonNull(extend) && BooleanUtils.toBooleanDefaultIfNull(extend.getPointMall(), false)) {
            MerchantAccountType merchantAccountType4 = new MerchantAccountType();
            merchantAccountType4.setMerAccountTypeName(WelfareConstant.MerAccountTypeCode.MALL_POINT.desc());
            merchantAccountType4.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
            merchantAccountType4.setMerCode(merCode);
            merchantAccountType4.setShowStatus(MerchantAccountTypeShowStatusEnum.SHOW.getCode());
            merchantAccountType4.setDeductionOrder(888);
            initList.add(merchantAccountType4);
        }
        return merchantAccountTypeDao.saveBatch(initList);
    }

    @Override
    public void saveIfExist(MerchantAccountType merchantAccountType) {
        MerchantAccountType old = merchantAccountTypeDao.queryAllByMerCodeAndType(merchantAccountType.getMerCode(), merchantAccountType.getMerAccountTypeCode());
        if (Objects.isNull(old)) {
            merchantAccountTypeDao.save(merchantAccountType);
        }
    }
}