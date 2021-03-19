package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.MerIdentityEnum;
import com.welfare.common.enums.MerchantAccountTypeShowStatusEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.enums.SupplierStoreSourceEnum;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.dao.MerchantAccountTypeDao;
import com.welfare.persist.dao.MerchantDao;
import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.dto.MerchantWithCreditDTO;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.MerchantExMapper;
import com.welfare.service.*;
import com.welfare.service.converter.MerchantAddConverter;
import com.welfare.service.converter.MerchantDetailConverter;
import com.welfare.service.converter.MerchantSyncConverter;
import com.welfare.service.converter.MerchantWithCreditConverter;
import com.welfare.service.dto.*;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.sync.event.MerchantEvt;
import com.welfare.service.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private  MerchantCreditService merchantCreditService;
    private final MerchantDetailConverter merchantDetailConverter;
    private final MerchantSyncConverter merchantSyncConverter;

    private final MerchantAddConverter merchantAddConverter;
    private final SequenceService sequenceService;
    private final ApplicationContext applicationContext;
    private final MerchantStoreRelationDao merchantStoreRelationDao;
    private final MerchantWithCreditConverter merchantWithCreditConverter;
    private final MerchantAccountTypeDao merchantAccountTypeDao;
    @Autowired
    MerchantAccountTypeService merchantAccountTypeService;


    @Override
    public List<Merchant> list(MerchantReq req) {
        QueryWrapper<Merchant> q=QueryHelper.getWrapper(req);
        q.orderByDesc(Merchant.CREATE_TIME);
        List<Merchant> list = merchantDao.list(q);
        if(req.isMerAccountTypeFlag()){
            QueryWrapper<MerchantAccountType> groupWapper=new QueryWrapper<>();
            groupWapper.eq(MerchantAccountType.SHOW_STATUS, MerchantAccountTypeShowStatusEnum.SHOW.getCode());
            List<MerchantAccountType> types=merchantAccountTypeDao.list(groupWapper.groupBy(MerchantAccountType.MER_CODE));
            if(EmptyChecker.notEmpty(types)){
                List<String> merCodes=types.stream().map(item->item.getMerCode()).collect(Collectors.toList());
                list=list.stream().filter(item->!merCodes.contains(item.getMerCode())).collect(Collectors.toList());
            }
        }
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
                queryWrapperMerchant.orderByDesc(Merchant.CREATE_TIME);
                list = merchantDao.list(queryWrapperMerchant);
            }
        }
        if(EmptyChecker.notEmpty(req.getMerIdentity())){
            List<String> merIddentityList= Arrays.asList(req.getMerIdentity().split(","));
            return list.stream().filter(item->{
                //商户已有的身份属性
                List<String> merIddentityList2= Arrays.asList(item.getMerIdentity().split(","));
                for(String merIddentity:merIddentityList2){
                    if(merIddentityList.contains(merIddentity)){
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public MerchantDetailDTO detail(Long id) {
        //商户详情
        MerchantDetailDTO merchantDetailDTO = merchantDetailConverter.toD(merchantDao.getById(id));
        if(EmptyChecker.isEmpty(merchantDetailDTO)){
            throw new BizException("商户不存在");
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
            item.setCode(item.getSelfCode());
            item.setParentCode(item.getDepartmentParent());
        });
        dictService.trans(MerchantWithCreditAndTreeDTO.class, Merchant.class.getSimpleName(), true, treeDTOList.toArray());
        TreeUtil treeUtil=new TreeUtil(treeDTOList,"0");
        return treeUtil.getTree();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(MerchantAddDTO merchant) {
        if(EmptyChecker.notEmpty(merchant.getAddressList())
                &&merchant.getAddressList().size()>10){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "收货地址不能超过十个", null);
        }
        String merCode=sequenceService.nextFullNo(WelfareConstant.SequenceType.MER_CODE.code());
        merchant.setMerCode(merCode);
        Merchant save =merchantAddConverter.toE(merchant);
        boolean flag=merchantDao.save(save);
        boolean flag2=merchantAddressService.saveOrUpdateBatch(merchant.getAddressList(),Merchant.class.getSimpleName(),save.getId());
        merchantCreditService.init(merchant.getMerCode());
        boolean flag3=merchantAccountTypeService.init(merCode);
        //同步商城中台
        if(!(flag&&flag2&flag3)){
            throw new BizException("新增商户失败");
        }
        MerchantSyncDTO detailDTO=merchantSyncConverter.toD(save);
        detailDTO.setAddressList(merchant.getAddressList());
        detailDTO.setId(save.getId());
        List<MerchantSyncDTO> syncList=new ArrayList<>();
        syncList.add(detailDTO);
        applicationContext.publishEvent( MerchantEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD).merchantDetailDTOList(syncList).timestamp(new Date()).build());
        return flag&&flag2&flag3;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(MerchantUpdateDTO merchant) {
        if(EmptyChecker.notEmpty(merchant.getAddressList())
                &&merchant.getAddressList().size()>10){
            throw new BizException(ExceptionCode.ILLEGALITY_ARGURMENTS, "收货地址不能超过十个", null);
        }
        Merchant update=buildEntity(merchant);
        boolean flag= 1==merchantDao.updateAllColumnById(update);
        boolean flag2=merchantAddressService.saveOrUpdateBatch(merchant.getAddressList(),Merchant.class.getSimpleName(),update.getId());
        //同步商城中台
        if(!(flag&&flag2)){
            throw new BizException("更新商户失败");
        }
        List<MerchantSyncDTO> syncList=new ArrayList<>();
        MerchantSyncDTO detailDTO=merchantSyncConverter.toD(update);
        detailDTO.setAddressList(merchant.getAddressList());
        syncList.add(detailDTO);
        applicationContext.publishEvent( MerchantEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).merchantDetailDTOList(syncList).timestamp(new Date()).build());
        return flag&&flag2;
    }
    private Merchant buildEntity(MerchantUpdateDTO merchant){
        Merchant entity=merchantDao.getById(merchant.getId());
        if(EmptyChecker.isEmpty(entity)){
            throw new BizException("id不存在");
        }
        if (StringUtils.isNoneBlank(merchant.getMerIdentity())) {
            List<String> merIdentityList = Lists.newArrayList(merchant.getMerIdentity().split(","));
            if (!merIdentityList.contains(MerIdentityEnum.customer.getCode())) {
                merchant.setMerCooperationMode(null);
                merchant.setSelfRecharge("0");
            }
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
        TreeUtil treeUtil=new TreeUtil(list,"0");

        return treeUtil.spread();
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
            throw new BizException("商户不存在");
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

    @Override
    public Boolean queryIsRabteByMerCOde(String merCode) {
        QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantStoreRelation.MER_CODE, merCode);
        List<MerchantStoreRelation> relations = merchantStoreRelationDao.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(relations)) {
            long count = relations.stream()
                    .filter(merchantStoreRelation -> merchantStoreRelation.getIsRebate() != null && merchantStoreRelation.getIsRebate() == 1)
                    .count();
            if (count > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Merchant> supplierByMer(String merCode) {
        return merchantDao.getBaseMapper().supplierByMer(merCode);
    }
}