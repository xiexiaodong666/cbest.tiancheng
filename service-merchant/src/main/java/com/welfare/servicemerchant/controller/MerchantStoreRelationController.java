package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.ApiUser;
import com.welfare.persist.dto.AdminMerchantStore;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.dto.MerchantStoreRelationDetailDTO;
import com.welfare.persist.dto.query.MerchantStoreRelationAddReq;
import com.welfare.persist.dto.query.MerchantStoreRelationUpdateReq;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.service.MerchantService;
import com.welfare.service.MerchantStoreRelationService;
import com.welfare.servicemerchant.dto.AdminMerchantStoreRelationDTO;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商户消费场景配置服务控制器
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchantStoreRelation")
@Api(tags = "商户消费场景配置接口")
public class MerchantStoreRelationController implements IController {

  private final MerchantStoreRelationService merchantStoreRelationService;
  private final MerchantService merchantService;
  private final FileUploadService fileUploadService;


  @GetMapping("/api/list")
  @ApiOperation("api分页查询消费门店配置列表")
  @ApiUser
  public R<Page<MerchantStoreRelation>> apiPageQuery(
      @RequestParam @ApiParam("当前页") Integer current,
      @RequestParam @ApiParam("单页大小") Integer size,
      @RequestParam(required = true) @ApiParam("商户代码") String merCode) {

    Page<MerchantStoreRelation> page = new Page(current, size);

    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();

    queryWrapper.eq(MerchantStoreRelation.MER_CODE, merCode);
    queryWrapper.ne(MerchantStoreRelation.DELETED, 1);
    queryWrapper.ne(MerchantStoreRelation.STATUS, 0);

    Page<MerchantStoreRelation> merchantStoreRelationPage = merchantStoreRelationService.pageQuery(
        page, queryWrapper);

    return success(merchantStoreRelationPage);
  }

  @GetMapping("/admin/list")
  @ApiOperation("后台分页查询消费门店配置列表")
  @ApiUser
  public R<Page<AdminMerchantStoreRelationDTO>> adminPageQuery(
      @RequestParam @ApiParam("当前页") Integer current,
      @RequestParam @ApiParam("单页大小") Integer size,
      @RequestParam(required = false) @ApiParam("商户名称") String merName,
      @RequestParam(required = false) @ApiParam("使用状态") String status,
      @RequestParam(required = false) @ApiParam("起始时间") Date startTime,
      @RequestParam(required = false) @ApiParam("结束时间") Date endTime
  ) {

    Page<MerchantStoreRelation> page = new Page(current, size);
    Page mapPage = merchantStoreRelationService
        .searchMerchantStoreRelations(page, merName, status, startTime, endTime);

    List<MerchantStoreRelationDTO> merchantStoreRelationDTOList = mapPage.getRecords();
    mapPage.setRecords(convertAdminMerchantStoreRelationDTOs(merchantStoreRelationDTOList));

    return success(mapPage);
  }

  @GetMapping("/detail")
  @ApiOperation("后台查询消费门店详情")
  @ApiUser
  public R<MerchantStoreRelationDetailDTO> detail(
      @RequestParam(required = true) @ApiParam("消费场景门店id") Long id) {

    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.ID, id);

    MerchantStoreRelation merchantStoreRelation = merchantStoreRelationService
        .getMerchantStoreRelationById(queryWrapper);

    QueryWrapper<Merchant> merchantQueryWrapper = new QueryWrapper<>();
    merchantQueryWrapper.eq(Merchant.MER_CODE, merchantStoreRelation.getMerCode());
    Merchant merchant = merchantService.getMerchantByMerCode(merchantQueryWrapper);

    queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(MerchantStoreRelation.MER_CODE, merchantStoreRelation.getMerCode());
    List<MerchantStoreRelation> merchantStoreRelationList = merchantStoreRelationService
        .getMerchantStoreRelationListByMerCode(queryWrapper);

    MerchantStoreRelationDetailDTO merchantStoreRelationDetailDTO = convertMerchantStoreRelationDetailDTO(
        merchantStoreRelationList);
    if(merchant != null) {
      merchantStoreRelationDetailDTO.setMerName(merchant.getMerName());
    }
    merchantStoreRelationDetailDTO.setMerCode(merchantStoreRelation.getMerCode());
    merchantStoreRelationDetailDTO.setRamark(merchantStoreRelation.getRamark());

    return success(merchantStoreRelationDetailDTO);
  }

  @PostMapping
  @ApiOperation("新增消费门店配置")
  @ApiUser
  public R<Boolean> addMerchantStore(@RequestBody MerchantStoreRelationAddReq relationAddReq) {

    return success(merchantStoreRelationService.add(relationAddReq));
  }

  @PutMapping
  @ApiOperation("修改消费门店配置")
  @ApiUser
  public R<Boolean> updateMerchantStore(
      @RequestBody MerchantStoreRelationUpdateReq relationUpadateReq) {

    return success(merchantStoreRelationService.update(relationUpadateReq));
  }

  @PutMapping("/status")
  @ApiOperation("删除/禁用/启动门店配置")
  @ApiUser
  public R<Boolean> updateMerchantStoreStatus(
      @RequestParam(required = true) @ApiParam("消费场景门店id") Long id,
      @RequestParam(required = false) @ApiParam("1删除") Integer delete,
      @RequestParam(required = false) @ApiParam("状态 1 启用  0禁用") Integer status) {

    return success(merchantStoreRelationService.updateStatus(id, delete, status));
  }

  @PostMapping("/export")
  @ApiOperation("导出消费门店配置(返回文件下载地址)")
  @ApiUser
  public R<String> export(
      @RequestParam(required = false) @ApiParam("商户名称") String merName,
      @RequestParam(required = false) @ApiParam("使用状态") String status,
      @RequestParam(required = false) @ApiParam("起始时间") Date startTime,
      @RequestParam(required = false) @ApiParam("结束时间") Date endTime) throws IOException {
    List<MerchantStoreRelationDTO> exportList = merchantStoreRelationService
        .exportMerchantStoreRelations(merName, status, startTime, endTime);
    String path = fileUploadService.uploadExcelFile(
        exportList, MerchantStoreRelationDTO.class, "消费门店配置列表");

    exportList.clear();
    return success(fileUploadService.getFileServerUrl(path));
  }


  private MerchantStoreRelationDetailDTO convertMerchantStoreRelationDetailDTO(
      List<MerchantStoreRelation> merchantStoreRelationList) {
    MerchantStoreRelationDetailDTO merchantStoreRelationDetailDTO = new MerchantStoreRelationDetailDTO();
    if (CollectionUtils.isEmpty(merchantStoreRelationList)) {
      return merchantStoreRelationDetailDTO;
    }
    List<AdminMerchantStore> adminMerchantStoreList = new ArrayList<>();
    merchantStoreRelationDetailDTO.setAdminMerchantStoreList(adminMerchantStoreList);

    for (MerchantStoreRelation m :
        merchantStoreRelationList) {

      AdminMerchantStore adminMerchantStore = new AdminMerchantStore();
      adminMerchantStore.setStoreCode(m.getStoreCode());
      adminMerchantStore.setStoreAlias(m.getStoreAlias());
      adminMerchantStore.setConsumType(m.getConsumType());
      adminMerchantStore.setIsRebate(m.getIsRebate());
      adminMerchantStore.setRebateType(m.getRebateType());
      adminMerchantStore.setRebateRatio(m.getRebateRatio());
      adminMerchantStore.setMerchantStoreId(String.valueOf(m.getId()));
      adminMerchantStoreList.add(adminMerchantStore);
    }
    return merchantStoreRelationDetailDTO;
  }

  /**
   * convert MerchantStoreRelationDTO to AdminMerchantStoreRelationDTO
   */
  private List<AdminMerchantStoreRelationDTO> convertAdminMerchantStoreRelationDTOs(
      List<MerchantStoreRelationDTO> merchantStoreRelationDTOList) {

    List<AdminMerchantStoreRelationDTO> adminMerchantStoreRelationDTOS = new ArrayList<>();

    for (MerchantStoreRelationDTO merchantStoreRelationDTO :
        merchantStoreRelationDTOList) {
      AdminMerchantStoreRelationDTO adminMerchantStoreRelationDTO = new AdminMerchantStoreRelationDTO();
      adminMerchantStoreRelationDTO.setId(String.valueOf(merchantStoreRelationDTO.getId()));
      adminMerchantStoreRelationDTO.setCreateTime(merchantStoreRelationDTO.getCreateTime());
      adminMerchantStoreRelationDTO.setMerCode(merchantStoreRelationDTO.getMerCode());
      adminMerchantStoreRelationDTO.setMerName(merchantStoreRelationDTO.getMerName());
      adminMerchantStoreRelationDTO.setRamark(merchantStoreRelationDTO.getRamark());
      adminMerchantStoreRelationDTO.setStatus(merchantStoreRelationDTO.getStatus());

      String storeCodes = merchantStoreRelationDTO.getStoreCode();
      String storeAlias = merchantStoreRelationDTO.getStoreAlias();
      String[] storeCodeArray = storeCodes.split(",");
      String[] storeAliaArray = storeAlias.split(",");

      if (Strings.isNotEmpty(storeCodes) && Strings.isNotEmpty(storeAlias)
          && storeCodeArray.length == storeAliaArray.length) {
        List<AdminMerchantStore> merchantStores = new ArrayList<>();

        for (int i = 0; i < storeCodeArray.length; i++) {
          AdminMerchantStore adminMerchantStore = new AdminMerchantStore();
          adminMerchantStore.setStoreAlias(storeAliaArray[i]);
          adminMerchantStore.setStoreCode(storeCodeArray[i]);
          merchantStores.add(adminMerchantStore);
        }
        adminMerchantStoreRelationDTO.setMerchantStores(merchantStores);

      }

      adminMerchantStoreRelationDTOS.add(adminMerchantStoreRelationDTO);
    }

    return adminMerchantStoreRelationDTOS;
  }
}