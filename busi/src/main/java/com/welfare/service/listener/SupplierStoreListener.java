package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.util.LinkedList;
import java.util.List;
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
    store.setConsumType(defaultConsumType);
    list.add(store);
    merCodeList.add(store.getMerCode());
    storeCodeList.add(store.getStoreCode());
  }


  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if (!CollectionUtils.isEmpty(list)) {
      boolean flag = check();
      if(flag){
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
