package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.MerIdentityEnum;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.MerchantService;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantReq;
import com.welfare.service.dto.SupplierStoreAddDTO;
import com.welfare.service.dto.SupplierStoreImportDTO;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

  private List<SupplierStoreAddDTO> list = new ArrayList<>();

  private List<String> merCodeList = new LinkedList();
  private List<String> casherNoList = new LinkedList();
  private List<String> storeCodeList = new LinkedList();
  private final static  List<String> excelAllType = Arrays.asList(new String[]{ConsumeTypeEnum.O2O.getExcelType(),ConsumeTypeEnum.ONLINE_MALL.getExcelType(),ConsumeTypeEnum.SHOP_SHOPPING.getExcelType()});

  public static  final String success="导入成功";
  public static  final String fail="入库失败";
  private final MerchantService merchantService;

  private final SupplierStoreService storeService;


  private static StringBuilder uploadInfo = new StringBuilder();


  @Override
  public void invoke(SupplierStoreImportDTO storeImportDTO, AnalysisContext analysisContext) {
    SupplierStoreAddDTO store = new SupplierStoreAddDTO();
    BeanUtils.copyProperties(storeImportDTO, store);

    Integer row=analysisContext.readRowHolder().getRowIndex()+1;
    if(EmptyChecker.isEmpty(storeImportDTO.getStoreCode())){
      uploadInfo.append("第").append(row.toString()).append("行").append("门店编码不能为空").append(";");
    }else{
      if(storeImportDTO.getStoreCode().length()!=4){
        uploadInfo.append("第").append(row.toString()).append("行").append("门店编码只能为4位").append(";");
      }
      String regex = "^[0-9A-Z]+$";
      if(!storeImportDTO.getStoreCode().matches(regex)){
        uploadInfo.append("第").append(row.toString()).append("行").append("门店编码格式错误").append(";");
      }
    }
    if(EmptyChecker.isEmpty(storeImportDTO.getMerCode())){
      uploadInfo.append("第").append(row.toString()).append("行").append("商户编码不能为空").append(";");
    }
    if(EmptyChecker.isEmpty(storeImportDTO.getStoreName())){
      uploadInfo.append("第").append(row.toString()).append("行").append("门店名称不能为空").append(";");
    }else{
      if(storeImportDTO.getStoreName().length()>50){
        uploadInfo.append("第").append(row.toString()).append("行").append("门店名称不能大于50").append(";");
      }
    }
    if(EmptyChecker.isEmpty(storeImportDTO.getConsumType())){
      uploadInfo.append("第").append(row.toString()).append("行").append("消费类型不能为空").append(";");
    }
    List<String> consumTypes=Arrays.asList(storeImportDTO.getConsumType().split(","));
    if(!excelAllType.containsAll(consumTypes)){
      uploadInfo.append("第").append(row.toString()).append("行").append("未输入正确的消费类型").append(";");
    }
    if((consumTypes.contains(ConsumeTypeEnum.O2O.getExcelType())
            &&consumTypes.contains(ConsumeTypeEnum.ONLINE_MALL.getExcelType()))){
      uploadInfo.append("第").append(row.toString()).append("行").append("线上商城和O2O不能同时选择").append(";");
    }
    if((consumTypes.contains(ConsumeTypeEnum.O2O.getExcelType())
            ||consumTypes.contains(ConsumeTypeEnum.ONLINE_MALL.getExcelType()))){
      if(EmptyChecker.isEmpty(storeImportDTO.getCashierNo())){
        uploadInfo.append("第").append(row.toString()).append("行").append("线上商城或者O2O必须输入虚拟收银机号").append(";");
      }else{
        if(storeImportDTO.getCashierNo().length()>255){
          uploadInfo.append("第").append(row.toString()).append("行").append("虚拟收银机号长度不能大于255").append(";");
        }else{
          casherNoList.add(storeImportDTO.getCashierNo());
        }

      }
    }else{
      if(EmptyChecker.notEmpty(storeImportDTO.getCashierNo())){
        uploadInfo.append("第").append(row.toString()).append("行").append("只有线上商城或者O2O允许输入虚拟收银机号").append(";");
      }
    }

    if((consumTypes.contains(ConsumeTypeEnum.O2O.getExcelType()))){
      if(EmptyChecker.isEmpty(storeImportDTO.getAddressName())
              ||EmptyChecker.isEmpty(storeImportDTO.getAddress())){
        uploadInfo.append("第").append(row.toString()).append("行").append("O2O门店必填自提点").append(";");
      }else {
        if(storeImportDTO.getAddressName().length()>100){
          uploadInfo.append("第").append(row.toString()).append("行").append("自提点名称长度不能大于100").append(";");
        }
        if(storeImportDTO.getAddress().length()>255) {
          uploadInfo.append("第").append(row.toString()).append("行").append("详细地址长度不能大于255").append(";");
        }
        List<MerchantAddressDTO> addressList = new ArrayList();
        MerchantAddressDTO merchantAddressDTO=new MerchantAddressDTO();
        merchantAddressDTO.setRelatedType(SupplierStore.class.getSimpleName());
        merchantAddressDTO.setAddress(storeImportDTO.getAddress());
        merchantAddressDTO.setAddressName(storeImportDTO.getAddressName());
        addressList.add(merchantAddressDTO);
        store.setAddressList(addressList);
      }
    }else{
      if(!(EmptyChecker.isEmpty(storeImportDTO.getAddressName())
              &&EmptyChecker.isEmpty(storeImportDTO.getAddress()))) {
        uploadInfo.append("第").append(row.toString()).append("行").append("只有O2O门店可以填写自提点").append(";");
      }
      }
    store.setCashierNo(store.getCashierNo());
    store.setConsumType(JSON.toJSONString(ConsumeTypesUtils.transferWithExcel(consumTypes)));
    list.add(store);
    merCodeList.add(store.getMerCode());
    storeCodeList.add(store.getStoreCode());
  }


  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if (!CollectionUtils.isEmpty(list)) {
      boolean flag = check();
      if(flag&&EmptyChecker.isEmpty(uploadInfo.toString())){
        Boolean result = storeService.batchAdd(list);
        if (result == false) {
          uploadInfo.append(fail);
        }
        if( StringUtils.isEmpty(uploadInfo.toString())) {
          uploadInfo.append(success);
        }
      }

    }
  }

  private boolean check() {
    boolean flag=true;
    if(EmptyChecker.notEmpty(casherNoList)){
      Map<String,List<String>> casherNoGroupMap= casherNoList.stream().collect(Collectors.groupingBy(String::toString));
      for(Map.Entry<String,List<String>> entry:casherNoGroupMap.entrySet()){
        if(entry.getValue().size()>1){
          uploadInfo.append("excel文件中存在重复的虚拟收银机号:").append(entry.getKey()).append(";");
          flag=false;
        }
      }
      QueryWrapper<SupplierStore> storeCashQuery=new QueryWrapper<>();
      storeCashQuery.in(SupplierStore.CASHIER_NO,casherNoList);
      List<SupplierStore> storesCash=storeService.list(storeCashQuery);
      if(EmptyChecker.notEmpty(storesCash)){
        String cashStr=storesCash.stream().map(item->item.getCashierNo()).collect(Collectors.joining(","));
        uploadInfo.append("虚拟收银机号重复:").append(cashStr).append(";");
        flag=false;
      }
    }
    if(EmptyChecker.notEmpty(storeCodeList)){
      Map<String,List<String>> groupMap= storeCodeList.stream().collect(Collectors.groupingBy(String::toString));
      for(Map.Entry<String,List<String>> entry:groupMap.entrySet()){
        if(entry.getValue().size()>1){
          uploadInfo.append("excel文件中存在重复的门店代码:").append(entry.getKey()).append(";");
          flag=false;
        }
      }
      QueryWrapper<SupplierStore> storeQueryWrapper=new QueryWrapper<>();
      storeQueryWrapper.in(SupplierStore.STORE_CODE,storeCodeList);
      List<SupplierStore> stores=storeService.list(storeQueryWrapper);
      if(EmptyChecker.notEmpty(stores)){
        String storeStr=stores.stream().map(item->item.getStoreCode()).collect(Collectors.joining(","));
        uploadInfo.append("门店编码重复:").append(storeStr).append(";");
        flag=false;
      }
    }
    if(EmptyChecker.notEmpty(merCodeList)){
      MerchantReq req=new MerchantReq() ;
      req.setMerCodeList(merCodeList);
      req.setMerIdentity(MerIdentityEnum.supplier.getCode());
      List<Merchant> merchants=merchantService.list(req);
      merCodeList.removeAll(merchants.stream().map(item->item.getMerCode()).collect(Collectors.toList())) ;
      if(EmptyChecker.notEmpty(merCodeList)){
        uploadInfo.append("商户不存在:").append(StringUtil.join(merCodeList,",")).append(";");
        flag=false;
      }
    }

    return flag;
  }

  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
