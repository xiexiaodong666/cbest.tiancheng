package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.MerchantAccountTypeConstant;
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
import com.welfare.service.converter.MerchantAccountTypeDetailConverter;
import com.welfare.service.dto.MerchantAccountTypeDetailDTO;
import com.welfare.service.dto.MerchantAccountTypeReq;
import com.welfare.service.dto.MerchantAccountTypeSortReq;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


    @Override
    public List<MerchantAccountType> list(MerchantAccountTypeReq req) {
        return merchantAccountTypeDao.list(QueryHelper.getWrapper(req));
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
    public boolean add(MerchantAccountTypeDetailDTO merchantAccountType) {
        if(EmptyChecker.isEmpty(merchantAccountType.getTypeList())){
            throw new BusiException("福利类型扣款顺序不能为空");
        }
        List<MerchantAccountType> accountTypeList=new ArrayList<>();
        for(MerchantAccountTypeDetailDTO.TypeItem typeItem:merchantAccountType.getTypeList()){
            MerchantAccountType type=merchantAccountTypeDetailConverter.toE(merchantAccountType);
            type.setDeductionOrder(typeItem.getDeductionOrder());
            type.setMerAccountTypeCode(typeItem.getMerAccountTypeCode());
            type.setMerAccountTypeName(typeItem.getMerAccountTypeName());
            //TODO
//            type.setMerAccountTypeCode(getMaxCode(merchantAccountType.get))
            accountTypeList.add(type);
        }
        return merchantAccountTypeDao.saveBatch(accountTypeList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(MerchantAccountTypeDetailDTO merchantAccountType) {
        //merCode不允许修改
        merchantAccountType.setMerCode(null);
        List<MerchantAccountType> accountTypeList=new ArrayList<>();
        for(MerchantAccountTypeDetailDTO.TypeItem typeItem:merchantAccountType.getTypeList()){
            if(EmptyChecker.isEmpty(typeItem.getId())){
                throw new BusiException("id不能为空");
            }
            MerchantAccountType type=merchantAccountTypeDetailConverter.toE(merchantAccountType);
            type.setDeductionOrder(typeItem.getDeductionOrder());
            type.setId(typeItem.getId());
            accountTypeList.add(type);
        }
        return merchantAccountTypeDao.saveOrUpdateBatch(accountTypeList);
    }

    @Override
    public String exportList(MerchantAccountTypePageReq pageReq) {
        return null;
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