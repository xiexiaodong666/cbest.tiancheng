package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.AccountTypeDao;
import com.welfare.persist.dto.AccountTypeMapperDTO;
import com.welfare.persist.dto.MerSupplierStoreDTO;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.mapper.AccountTypeCustomizeMapper;
import com.welfare.persist.mapper.SupplierStoreExMapper;
import com.welfare.service.AccountTypeService;
import com.welfare.service.MerchantService;
import com.welfare.service.SequenceService;
import com.welfare.service.converter.AccountTypeConverter;
import com.welfare.service.dto.AccountTypeDTO;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工类型服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountTypeServiceImpl implements AccountTypeService {
    private final AccountTypeDao accountTypeDao;
    private final MerchantService merchantService;
    private final AccountTypeCustomizeMapper accountTypeCustomizeMapper;
    private final AccountTypeConverter accountTypeConverter;
    private final SequenceService sequenceService;
    private final SupplierStoreExMapper supplierStoreExMapper;

    @Override
    public Page<AccountType> pageQuery(Page<AccountType> page,
        QueryWrapper<AccountType> queryWrapper) {
        return accountTypeDao.page(page,queryWrapper);
    }

    @Override
    public Page<AccountTypeDTO> getPageDTO(Page<AccountTypeMapperDTO> page, String merCode,
        String typeCode,
        String typeName, Date startDate, Date endDate) {
        IPage<AccountTypeMapperDTO> accountTypeDTOIPage = accountTypeCustomizeMapper.queryAccountType(page,merCode,typeCode,typeName,startDate,endDate);
        return accountTypeConverter.toDTOPage(accountTypeDTOIPage);
    }

    @Override
    public List<AccountTypeDTO> queryAccountTypeDTO(String merCode, String typeCode,
        String typeName,
        Date startDate, Date endDate) {
        return accountTypeConverter.toDTOList(accountTypeCustomizeMapper.queryAccountType(merCode,typeCode,typeName,startDate,endDate));
    }

    @Override
    public List<MerSupplierStoreDTO> queryMerSupplierStoreDTList(String merCode) {
        return supplierStoreExMapper.queryMerSupplierStoreDTList(merCode);
    }

    @Override
    public AccountType getAccountType(Long id) {
        return accountTypeDao.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean save(AccountType accountType){
        accountType.setTypeCode(sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_TYPE_CODE.code()).toString());
        validationAccountType(accountType,true);
        return accountTypeDao.save(accountType);
    }

    private void validationAccountType(AccountType accountType,boolean isNew){
        Merchant merchant = merchantService.detailByMerCode(accountType.getMerCode());
        if( null == merchant ) {
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"商户不存在",null);
        }
        if( isNew ){
            AccountType queryAccountType = this.queryByTypeCode(accountType.getMerCode(),accountType.getTypeCode());
            if( null != queryAccountType ){
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工类型code已经存在",null);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(AccountType accountType) {
        validationAccountType(accountType,false);
        return accountTypeDao.updateById(accountType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        return accountTypeDao.removeById(id);
    }

    @Override
    public AccountType queryByTypeCode(String merCode,String typeCode) {
        QueryWrapper<AccountType> wrapper = new QueryWrapper<AccountType>();
        wrapper.eq(AccountType.MER_CODE,merCode);
        wrapper.eq(AccountType.TYPE_CODE,typeCode);
        AccountType accountType = accountTypeDao.getOne(wrapper);
        return accountType;
    }
}