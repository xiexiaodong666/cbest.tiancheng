package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.MoveDirectionEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.dao.MerchantAccountTypeDao;
import com.welfare.persist.dto.MerchantAccountTypeWithMerchantDTO;
import com.welfare.persist.dto.query.MerchantAccountTypePageReq;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.mapper.MerchantAccountTypeExMapper;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.MerchantService;
import com.welfare.service.SequenceService;
import com.welfare.service.converter.MerchantAccountTypeDetailConverter;
import com.welfare.service.dto.MerchantAccountTypeAddDTO;
import com.welfare.service.dto.MerchantAccountTypeDetailDTO;
import com.welfare.service.dto.MerchantAccountTypeReq;
import com.welfare.service.dto.MerchantAccountTypeSortReq;
import com.welfare.service.dto.MerchantAccountTypeUpdateDTO;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
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
    private final MerchantService merchantService;
    private final MerchantAccountTypeDetailConverter merchantAccountTypeDetailConverter;
    private final SequenceService sequenceService;

    @Override
    public List<MerchantAccountType> list(MerchantAccountTypeReq req) {
        QueryWrapper q=QueryHelper.getWrapper(req);
        q.orderByDesc(MerchantAccountType.CREATE_TIME);
        return merchantAccountTypeDao.list(q);
    }

    @Override
    public MerchantAccountTypeDetailDTO detail(Long id) {
        MerchantAccountTypeDetailDTO detailDTO=merchantAccountTypeDetailConverter.toD(merchantAccountTypeDao.getById(id));
        if(EmptyChecker.isEmpty(detailDTO)){
            throw new BusiException("福利类型不存在");
        }
        detailDTO.setMerName(merchantService.getMerchantByMerCode(detailDTO.getMerCode()).getMerName());
        List<MerchantAccountType> list=this.queryByMerCode(detailDTO.getMerCode());
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
            throw new BusiException("福利类型扣款顺序不能为空");
        }
        List<MerchantAccountType> accountTypeList=new ArrayList<>();
        Date date=new Date();
        for(MerchantAccountTypeAddDTO.TypeItem typeItem:merchantAccountType.getTypeList()){
            MerchantAccountType type=new MerchantAccountType();
            type.setDeductionOrder(typeItem.getDeductionOrder());
            type.setMerCode(merchantAccountType.getMerCode());
            type.setMerAccountTypeName(typeItem.getMerAccountTypeName());
            type.setMerAccountTypeCode(sequenceService.nextNo(WelfareConstant.SequenceType.MER_ACCOUNT_TYPE_CODE.code()).toString());
            type.setRemark(merchantAccountType.getRemark());
            type.setCreateTime(date);
            accountTypeList.add(type);
        }
        return merchantAccountTypeDao.saveBatch(accountTypeList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(MerchantAccountTypeUpdateDTO merchantAccountType) {
        if(EmptyChecker.isEmpty(merchantAccountType.getTypeList())){
            throw new BusiException("福利类型扣款顺序不能为空");
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
                type.setMerAccountTypeCode(sequenceService.nextNo(WelfareConstant.SequenceType.MER_ACCOUNT_TYPE_CODE.code()).toString());
                type.setRemark(merchantAccountType.getRemark());
                type.setCreateTime(date);
                addList.add(type);
            }else{
                MerchantAccountType type=new MerchantAccountType();
                type.setId(typeItem.getId());
                type.setMerCode(merchantAccountType.getMerCode());
                type.setMerAccountTypeName(typeItem.getMerAccountTypeName());
                type.setMerAccountTypeCode(typeItem.getMerAccountTypeCode());
                type.setDeductionOrder(typeItem.getDeductionOrder());
                type.setRemark(merchantAccountType.getRemark());
                type.setCreateTime(date);
                type.setUpdateTime(date);
                type.setUpdateUser(merchantAccountType.getUpdateUser());
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
    public List<MerchantAccountType> queryByMerCode(String merCode) {
        QueryWrapper<MerchantAccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantAccountType.MER_CODE,merCode);
        return merchantAccountTypeDao.list(queryWrapper);
    }
}