package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.ApiUser;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.persist.dto.AdminMerchantStore;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.dto.MerchantStoreRelationDetailDTO;
import com.welfare.persist.dto.query.MerchantStoreRelationAddReq;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.service.MerchantService;
import com.welfare.service.MerchantStoreRelationService;
import com.welfare.servicemerchant.dto.AdminMerchantStoreRelationDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/api/list")
  @ApiOperation("api分页查询消费门店配置列表")
  public R<Page<MerchantStoreRelation>> apiPageQuery(
      @RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = true) @ApiParam("商户代码") String merCode) {

    Page<MerchantStoreRelation> page = new Page(currentPage, pageSize);

    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();

    queryWrapper.eq(MerchantStoreRelation.MER_CODE, merCode);
    queryWrapper.ne(MerchantStoreRelation.DELETED, 1);
    queryWrapper.ne(MerchantStoreRelation.STATUS, 1);

    Page<MerchantStoreRelation> merchantStoreRelationPage = merchantStoreRelationService.pageQuery(
        page, queryWrapper);

    return success(merchantStoreRelationPage);
  }

  @GetMapping("/admin/list")
  @ApiOperation("后台分页查询消费门店配置列表")
  public R<Page<AdminMerchantStoreRelationDTO>> adminPageQuery(
      @RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = false) @ApiParam("商户名称") String merName,
      @RequestParam(required = false) @ApiParam("使用状态") String status
  ) {

    Page<MerchantStoreRelation> page = new Page(currentPage, pageSize);
    Page mapPage = merchantStoreRelationService
        .searchMerchantStoreRelations(page, merName, status);

    List<MerchantStoreRelationDTO> merchantStoreRelationDTOList = mapPage.getRecords();
    mapPage.setRecords(convertAdminMerchantStoreRelationDTOs(merchantStoreRelationDTOList));

    return success(mapPage);
  }

  @GetMapping("/detail")
  @ApiOperation("后台查询消费门店详情")
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

    MerchantStoreRelationDetailDTO merchantStoreRelationDetailDTO = convertMerchantStoreRelationDetailDTO(merchantStoreRelationList);
    merchantStoreRelationDetailDTO.setMerName(merchant.getMerName());
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
  public R<Boolean> updateMerchantStore() {

    return null;
  }

  @PostMapping("/export")
  @ApiOperation("导出消费门店配置(返回文件下载地址)")
  @MerchantUser
  public R<String> export(
      @RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = false) @ApiParam("商户名称") String merName,
      @RequestParam(required = false) @ApiParam("使用状态") String status) {
    return null;
  }


  private MerchantStoreRelationDetailDTO convertMerchantStoreRelationDetailDTO(List<MerchantStoreRelation> merchantStoreRelationList) {
    MerchantStoreRelationDetailDTO merchantStoreRelationDetailDTO = new MerchantStoreRelationDetailDTO();
    if(CollectionUtils.isEmpty(merchantStoreRelationList)) {
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
      adminMerchantStoreRelationDTO.setCreateTime(merchantStoreRelationDTO.getCreateTime());
      adminMerchantStoreRelationDTO.setMerCode(merchantStoreRelationDTO.getMerCode());
      adminMerchantStoreRelationDTO.setMerName(merchantStoreRelationDTO.getMerName());
      adminMerchantStoreRelationDTO.setRamark(merchantStoreRelationDTO.getRamark());

      String storeCodes = merchantStoreRelationDTO.getStoreCode();
      String storeAlias = merchantStoreRelationDTO.getStoreAlias();

      if (Strings.isNotEmpty(storeCodes) && Strings.isNotEmpty(storeAlias)
          && storeAlias.length() == storeCodes.length()) {
        List<AdminMerchantStore> merchantStores = new ArrayList<>();

        String[] storeCodeArray = storeCodes.split(",");
        String[] storeAliaArray = storeAlias.split(",");

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