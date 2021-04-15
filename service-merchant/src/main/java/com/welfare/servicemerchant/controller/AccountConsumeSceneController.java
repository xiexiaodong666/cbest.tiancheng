package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.annotation.RepeatRequestVerification;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.AccountChangeEventRecordDao;
import com.welfare.persist.dao.AccountConsumeSceneStoreRelationDao;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.AccountConsumeSceneService;
import com.welfare.service.AccountConsumeSceneStoreRelationService;
import com.welfare.service.dto.*;
import com.welfare.servicemerchant.converter.AccountConsumeSceneConverter;
import com.welfare.servicemerchant.dto.AccountConsumePageReq;
import com.welfare.servicemerchant.dto.UpdateStatusReq;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/7 19:43
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountConsumeScene")
@Api(tags = "员工消费配置管理")
public class AccountConsumeSceneController implements IController {

  @Autowired
  private AccountConsumeSceneService accountConsumeSceneService;
  @Autowired
  private AccountConsumeSceneConverter accountConsumeSceneConverter;
  @Autowired
  private FileUploadService fileUploadService;
  @Autowired
  private AccountConsumeSceneStoreRelationService accountConsumeSceneStoreRelationService;

  @Autowired
  private AccountChangeEventRecordService accountChangeEventRecordService;
  @Autowired
  private AccountChangeEventRecordDao accountChangeEventRecordDao;
  @Autowired
  private AccountCustomizeMapper accountCustomizeMapper;
  @Autowired
  private AccountConsumeSceneStoreRelationDao accountConsumeSceneStoreRelationDao;

  @GetMapping("/test")
  public void test( @RequestParam(value = "merCode")  String merCode,
      @RequestParam(value = "storeCode")  String storeCode,
      @RequestParam(value = "consumeType")  String consumeType){
    accountConsumeSceneStoreRelationService.deleteConsumeScene(merCode,Arrays.asList(storeCode));
   /* StoreConsumeRelationDTO storeConsumeRelationDTO = new StoreConsumeRelationDTO();
    storeConsumeRelationDTO.setStoreCode(storeCode);
    storeConsumeRelationDTO.setConsumeType(consumeType);
    accountConsumeSceneStoreRelationService.updateStoreConsumeTypeByDTOList(merCode, Arrays.asList(storeConsumeRelationDTO));*/
  }

  @GetMapping("/page")
  @ApiOperation("分页查询员工消费配置列表")
  public R<Page<AccountConsumeScenePageDTO>> pageQuery(
      @RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      AccountConsumePageReq accountConsumePageReq) {
    Page<AccountConsumeScenePageDTO> page = new Page(currentPage, pageSize);
    AccountConsumePageQuery accountConsumePageQuery = new AccountConsumePageQuery();
    BeanUtils.copyProperties(accountConsumePageReq, accountConsumePageQuery);
    IPage<AccountConsumeScenePageDTO> result = accountConsumeSceneService
        .getPageDTO(page, accountConsumePageQuery);

    return success(accountConsumeSceneConverter.toD(result));
  }

  @GetMapping("/{id}")
  @ApiOperation("员工消费配置详情")
  public R<AccountConsumeSceneDTO> detail(@PathVariable String id) {
    AccountConsumeSceneDTO accountConsumeSceneDTO = accountConsumeSceneService
            .findAccountConsumeSceneDTOById(Long.parseLong(id));
    return success(accountConsumeSceneDTO);
  }

  @PostMapping("/save")
  @ApiOperation("新增员工消费配置")
  @MerchantUser
  @RepeatRequestVerification(prefixKey= "e-welfare-repeat-request:account_consume_scene_save")
  public R<Boolean> save(@RequestBody AccountConsumeSceneAddReq accountConsumeSceneAddReq) {
    try {
      accountConsumeSceneAddReq.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
      return success(accountConsumeSceneService.save(accountConsumeSceneAddReq));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/update")
  @ApiOperation("修改员工消费配置")
  @MerchantUser
  public R<Boolean> update(@RequestBody AccountConsumeSceneReq accountConsumeSceneReq) {
    try {
      accountConsumeSceneReq.setUpdateUser(MerchantUserHolder.getMerchantUser().getUsername());
      return success(accountConsumeSceneService.update(accountConsumeSceneReq));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/updateStatus")
  @ApiOperation("激活")
  public R<Boolean> updateStatus(@RequestBody UpdateStatusReq updateStatusReq) {
    try {
      return success(accountConsumeSceneService.updateStatus(Long.parseLong(updateStatusReq.getId()),
          updateStatusReq.getConsumeSceneStatus()));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/delete")
  @ApiOperation("删除员工消费配置")
  public R<Boolean> delete(@RequestBody UpdateStatusReq updateStatusReq) {
    try {
      return success(accountConsumeSceneService.delete(Long.parseLong(updateStatusReq.getId())));
    }catch (BizException be){
      return R.fail(be.getMessage());
    }
  }


  @ApiOperation("员工消费配置导出")
  @GetMapping(value = "/exportAccountConsumeScene")
  public R<String> exportAccountConsumeScene(AccountConsumePageReq accountConsumePageReq)
      throws IOException {
    AccountConsumePageQuery accountConsumePageQuery = new AccountConsumePageQuery();
    BeanUtils.copyProperties(accountConsumePageReq, accountConsumePageQuery);
    List<AccountConsumeScenePageDTO> list = accountConsumeSceneService
        .export(accountConsumePageQuery);
    String path = fileUploadService
        .uploadExcelFile(list, AccountConsumeScenePageDTO.class, "员工消费配置");
    return success(fileUploadService.getFileServerUrl(path));
  }

  @PostMapping("/edit")
  @ApiOperation("编辑员工消费配置")
  @MerchantUser
  public R<Boolean> edit(@RequestBody List<AccountConsumeSceneEditReq> consumeSceneEditReqs) {
    try {
      return success(accountConsumeSceneService.edit(consumeSceneEditReqs));
    } catch (BizException be) {
      return R.fail(be.getMessage());
    }
  }

  @GetMapping("/details")
  @ApiOperation("查询商户下所有员工消费配置详情")
  @MerchantUser
  public R<List<AccountConsumeSceneResp>> detail() {
    List<AccountConsumeSceneResp> accountConsumeSceneResps = accountConsumeSceneService
            .findAllAccountConsumeSceneDTO(MerchantUserHolder.getMerchantUser().getMerchantCode());
    return success(accountConsumeSceneResps);
  }
}
