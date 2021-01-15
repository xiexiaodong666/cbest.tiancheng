package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.enums.SupplierStoreSourceEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.dao.MerchantDao;
import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dto.MerchantWithCreditDTO;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.persist.entity.MerchantAddress;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.mapper.MerchantExMapper;
import com.welfare.service.DictService;
import com.welfare.service.MerchantAddressService;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.SequenceService;
import com.welfare.service.converter.MerchantDetailConverter;
import com.welfare.service.converter.MerchantWithCreditConverter;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantAddressReq;
import com.welfare.service.dto.MerchantDetailDTO;
import com.welfare.service.dto.MerchantReq;
import com.welfare.service.dto.MerchantUpdateDTO;
import com.welfare.service.dto.MerchantWithCreditAndTreeDTO;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.sync.event.MerchantEvt;
import com.welfare.service.utils.TreeUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private final SequenceService sequenceService;
    private final ApplicationContext applicationContext;
    private final MerchantStoreRelationDao merchantStoreRelationDao;
    private final MerchantWithCreditConverter merchantWithCreditConverter;



    @Override
    public List<Merchant> list(MerchantReq req) {
        List<Merchant> list = merchantDao.list(QueryHelper.getWrapper(req));
        if (SupplierStoreSourceEnum.MERCHANT_STORE_RELATION.getCode().equals(req.getSource())) {

            QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
            queryWrapper.groupBy(MerchantStoreRelation.MER_CODE);

            List<MerchantStoreRelation> merchantStoreRelations = merchantStoreRelationDao.list(
                queryWrapper);
            if (CollectionUtils.isNotEmpty(merchantStoreRelations)) {
                List<String> merCodeList = merchantStoreRelations.stream().map(
                    MerchantStoreRelation::getMerCode).collect(Collectors.toList());
                Set<String> merCodeSet = new HashSet<>(merCodeList);
                QueryWrapper<Merchant> queryWrapperMerchant = new QueryWrapper<>();
                queryWrapperMerchant.notIn(Merchant.MER_CODE, merCodeSet);
                queryWrapperMerchant.eq(Merchant.STATUS, 1);
                list = merchantDao.list(queryWrapperMerchant);
            }
        }
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
    public List<MerchantWithCreditAndTreeDTO> tree( MerchantPageReq merchantPageReq) {
        List<MerchantWithCreditDTO> list = merchantExMapper.listWithCredit( merchantPageReq);


        if(EmptyChecker.isEmpty(list)){
            return new ArrayList<>();
        }
        List<MerchantWithCreditAndTreeDTO> treeDTOList=merchantWithCreditConverter.toD(list);
        treeDTOList.forEach(item->{
            item.setCode(item.getMerCode());
            item.setParentCode(item.getDepartmentParent());
        });
        dictService.trans(MerchantWithCreditAndTreeDTO.class, Merchant.class.getSimpleName(), true, treeDTOList.toArray());
        TreeUtil treeUtil=new TreeUtil(treeDTOList,"0");
        return treeUtil.getTree();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(MerchantDetailDTO merchant) {
        merchant.setMerCode(sequenceService.nextFullNo(WelfareConstant.SequenceType.MER_CODE.code()));
        Merchant save =merchantDetailConverter.toE(merchant);
        boolean flag=merchantDao.save(save);
        boolean flag2=merchantAddressService.saveOrUpdateBatch(merchant.getAddressList(),Merchant.class.getSimpleName(),save.getId());
        //同步商城中台
        merchant.setId(save.getId());
        List<MerchantDetailDTO> syncList=new ArrayList<>();
        syncList.add(merchant);
        applicationContext.publishEvent( MerchantEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD).merchantDetailDTOList(syncList).build());
        return flag&&flag2;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(MerchantUpdateDTO merchant) {
        Merchant update=buildEntity(merchant);
        boolean flag= merchantDao.updateById(update);
        boolean flag2=merchantAddressService.saveOrUpdateBatch(merchant.getAddressList(),Merchant.class.getSimpleName(),update.getId());
        //同步商城中台
        List<MerchantDetailDTO> syncList=new ArrayList<>();
        MerchantDetailDTO detailDTO=merchantDetailConverter.toD(update);
        detailDTO.setAddressList(merchant.getAddressList());
        syncList.add(detailDTO);
        applicationContext.publishEvent( MerchantEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).merchantDetailDTOList(syncList).build());
        return flag&&flag2;
    }
    private Merchant buildEntity(MerchantUpdateDTO merchant){
        Merchant entity=merchantDao.getById(merchant.getId());
        if(EmptyChecker.isEmpty(entity)){
            throw new BusiException("id不存在");
        }
        entity.setSelfRecharge(merchant.getSelfRecharge());
        entity.setMerName(merchant.getMerName());
        entity.setMerType(merchant.getMerType());
        entity.setMerIdentity(merchant.getMerIdentity());
        entity.setMerCooperationMode(merchant.getMerCooperationMode());
        entity.setUpdateUser(merchant.getUpdateUser());
        entity.setRemark(merchant.getRemark());
        return entity;
    }

    @Override
    public List<MerchantWithCreditAndTreeDTO> exportList(MerchantPageReq merchantPageReq) {
        List<MerchantWithCreditAndTreeDTO> list = this.tree( merchantPageReq);
        return list;
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

    @Override
    public List<Merchant> queryMerchantByCodeList(List<String> merCodeList) {
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(Merchant.MER_CODE,merCodeList);
        return merchantDao.list(queryWrapper);
    }
}