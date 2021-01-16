package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.MerchantService;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.dto.MerchantReq;
import com.welfare.service.dto.SupplierStoreImportDTO;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hao.yin
 * @version 0.0.1
 * @date 2021/1/11 11:10
 */
@Slf4j
@RequiredArgsConstructor
public class SupplierStoreListener extends AnalysisEventListener<SupplierStoreImportDTO> {

  private List<SupplierStore> list = new LinkedList();
  private List<String> merCodeList = new LinkedList();
  private List<String> storeCodeList = new LinkedList();
  private final static  List<String> excelAllType = Arrays.asList(new String[]{ConsumeTypeEnum.O2O.getCode(),ConsumeTypeEnum.ONLINE_MALL.getCode(),ConsumeTypeEnum.SHOP_SHOPPING.getCode()});


  private final MerchantService merchantService;

  private final SupplierStoreService storeService;
  private  final String defaultConsumType;


  private static StringBuilder uploadInfo = new StringBuilder();


  @Override
  public void invoke(SupplierStoreImportDTO storeImportDTO, AnalysisContext analysisContext) {
    SupplierStore store = new SupplierStore();
    BeanUtils.copyProperties(storeImportDTO, store);
    store.setStatus(0);
    store.setStorePath(store.getMerCode()+"-"+store.getStoreCode());
    Integer row=analysisContext.readRowHolder().getRowIndex();
    if(EmptyChecker.isEmpty(storeImportDTO.getStoreCode())){
      uploadInfo.append("第").append(row.toString()).append("行").append("门店编码不能为空");
    }
    if(EmptyChecker.isEmpty(storeImportDTO.getMerCode())){
      uploadInfo.append("第").append(row.toString()).append("行").append("商户编码不能为空");
    }
    if(EmptyChecker.isEmpty(storeImportDTO.getStoreName())){
      uploadInfo.append("第").append(row.toString()).append("行").append("门店名称不能为空");
    }
    if(EmptyChecker.isEmpty(storeImportDTO.getConsumType())){
      uploadInfo.append("第").append(row.toString()).append("行").append("消费类型不能为空").append(";");
    }
    List<String> consumTypes=Arrays.asList(storeImportDTO.getConsumType().split(","));
    if(excelAllType.containsAll(consumTypes)){
      uploadInfo.append("第").append(row.toString()).append("行").append("未输入正确的消费类型").append(";");
    }
    if((consumTypes.contains(ConsumeTypeEnum.O2O.getExcelType())
            ||consumTypes.contains(ConsumeTypeEnum.ONLINE_MALL.getExcelType()))){
      if(EmptyChecker.isEmpty(storeImportDTO.getCasherNo())){
        uploadInfo.append("第").append(row.toString()).append("行").append("线上商城或者O2O必须输入虚拟收银机号").append(";");
      }
    }else{
      if(EmptyChecker.notEmpty(storeImportDTO.getCasherNo())){
        uploadInfo.append("第").append(row.toString()).append("行").append("只有线上商城或者O2O允许输入虚拟收银机号").append(";");
      }
    }
    store.setCashierNo(store.getCashierNo());
    store.setConsumType(JSON.toJSONString(ConsumeTypesUtils.transferWithExcel(consumTypes)));
    store.setStoreParent(store.getMerCode());
    list.add(store);
    merCodeList.add(store.getMerCode());
    storeCodeList.add(store.getStoreCode());
  }


  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if (!CollectionUtils.isEmpty(list)) {
      boolean flag = check();
      if(flag&&EmptyChecker.notEmpty(uploadInfo)){
        Boolean result = storeService.batchAdd(list);
        if (result == false) {
          uploadInfo.append("入库失败");
        }
        if( StringUtils.isEmpty(uploadInfo.toString())) {
          uploadInfo.append("导入成功");
        }
      }

    }
  }

  private boolean check() {
    QueryWrapper<SupplierStore> storeQueryWrapper=new QueryWrapper<>();
    storeQueryWrapper.in(SupplierStore.STORE_CODE,storeCodeList);
    List<SupplierStore> stores=storeService.list(storeQueryWrapper);
    MerchantReq req=new MerchantReq() ;
    req.setMerCodeList(merCodeList);
    List<Merchant> merchants=merchantService.list(req);
    merCodeList.removeAll(merchants.stream().map(item->item.getMerCode()).collect(Collectors.toList())) ;
    boolean flag=true;
    if(EmptyChecker.notEmpty(stores)){
      String storeStr=stores.stream().map(item->item.getStoreCode()).collect(Collectors.joining(","));
      uploadInfo.append("部门编码重复:").append(storeStr).append(";");
      flag=false;
    }
    if(EmptyChecker.notEmpty(merCodeList)){
      uploadInfo.append("商户不存在:").append(StringUtil.join(merCodeList,",")).append(";");
      flag=false;
    }
    return flag;
  }

  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
