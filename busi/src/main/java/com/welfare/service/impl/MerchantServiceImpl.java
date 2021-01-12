package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.MerchantConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.StringUtil;
import com.welfare.persist.dao.MerchantDao;
import com.welfare.persist.dto.MerchantWithCreditDTO;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.persist.entity.MerchantAddress;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.mapper.MerchantExMapper;
import com.welfare.service.DictService;
import com.welfare.service.MerchantAddressService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.converter.MerchantDetailConverter;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantAddressReq;
import com.welfare.service.dto.MerchantDetailDTO;
import com.welfare.service.dto.MerchantReq;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户信息服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantServiceImpl implements MerchantService {
    private final MerchantDao merchantDao;
    private final MerchantExMapper merchantExMapper;
    private final DictService dictService;
    private final MerchantAddressService merchantAddressService;
    private final MerchantCreditService merchantCreditService;
    private final MerchantDetailConverter merchantDetailConverter;


    @Override
    public List<Merchant> list(MerchantReq req) {
        List<Merchant> list = merchantDao.list(QueryHelper.getWrapper(req));
        return list;
    }

    @Override
    public MerchantDetailDTO detail(Long id) {
        //商户详情
        MerchantDetailDTO merchantDetailDTO = merchantDetailConverter.toD(merchantDao.getById(id));
        if(EmptyChecker.isEmpty(merchantDetailDTO)){
            throw new BusiException("商户不存在");
        }
        MerchantAddressReq merchantAddressReq = new MerchantAddressReq();
        merchantAddressReq.setRelatedType(Merchant.class.getSimpleName());
        merchantAddressReq.setRelatedId(merchantDetailDTO.getId());
        //收获地址
        List<MerchantAddressDTO> addressDTOLis = merchantAddressService.list(merchantAddressReq);
        dictService.trans(MerchantAddressDTO.class, MerchantAddress.class.getSimpleName(), true, addressDTOLis.toArray());
        merchantDetailDTO.setAddressList(addressDTOLis);

        MerchantCredit merchantCredit = merchantCreditService.getByMerCode(merchantDetailDTO.getMerCode());
        if (EmptyChecker.notEmpty(merchantCredit)) {
            merchantDetailDTO.setRechargeLimit(merchantCredit.getRechargeLimit());
            merchantDetailDTO.setCurrentBalance(merchantCredit.getCurrentBalance());
            merchantDetailDTO.setRebateLimit(merchantCredit.getRebateLimit());
            merchantDetailDTO.setCreditLimit(merchantCredit.getCreditLimit());
            merchantDetailDTO.setRemainingLimit(merchantCredit.getRemainingLimit());
        }
        dictService.trans(MerchantDetailDTO.class, Merchant.class.getSimpleName(), true, merchantDetailDTO);
        return merchantDetailDTO;
    }

    @Override
    public Page<MerchantWithCreditDTO> page(Page<Merchant> page, MerchantPageReq merchantPageReq) {
        Page<MerchantWithCreditDTO> pageResult = merchantExMapper.listWithCredit(page, merchantPageReq);
        dictService.trans(MerchantWithCreditDTO.class, Merchant.class.getSimpleName(), true, pageResult.getRecords().toArray());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(MerchantDetailDTO merchant) {
        merchant.setMerCode(nextMaxCode());
        boolean flag=merchantDao.save(merchantDetailConverter.toE(merchant));
        return flag&merchantAddressService.saveOrUpdateBatch(merchant.getAddressList());
    }
    private String nextMaxCode(){
        String maxCode=merchantExMapper.getMaxMerCode();
        if(MerchantConstant.MAX_MER_CODE.equals(maxCode)){
            throw new BusiException("已达最大商户编码，请联系管理员");
        }
        if(EmptyChecker.isEmpty(maxCode)){
            return MerchantConstant.INIT_MER_CODE;
        }
        String first=String.valueOf(maxCode.charAt(0));
        Integer second=Integer.parseInt(maxCode.substring(1));
        if(MerchantConstant.MAX_MER_CODE_SECOND.equals(second)){
            second=MerchantConstant.INIT_MER_CODE_SECOND;
            first= StringUtil.getNextUpEn(first);

        }else{
            second=second+1;
        }
        return first+second;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean update(MerchantDetailDTO merchant) {
        boolean flag= merchantDao.updateById(merchantDetailConverter.toE(merchant));
        return flag&&merchantAddressService.saveOrUpdateBatch(merchant.getAddressList());

    }

    @Override
    public String exportList(MerchantPageReq merchantPageReq) {
        return null;
    }

    @Override
    public Merchant getMerchantByMerCode(QueryWrapper<Merchant> queryWrapper) {
        return merchantDao.getOne(queryWrapper);
    }

    @Override
    public Merchant getMerchantByMerCode(String merCode) {
        QueryWrapper<Merchant> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(Merchant.MER_CODE,merCode);
        Merchant merchant=this.getMerchantByMerCode(queryWrapper);
        if(EmptyChecker.isEmpty(merchant)){
            throw new BusiException("商户不存在");
        }
        return merchant;
    }

    @Override
    public Merchant detailByMerCode(String merCode) {
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Merchant.MER_CODE, merCode);
        return merchantDao.getOne(queryWrapper);
    }

    @Override
    public Merchant queryByCode(String merCode) {
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Merchant.MER_CODE,merCode);
        return merchantDao.getOne(queryWrapper);
    }


}