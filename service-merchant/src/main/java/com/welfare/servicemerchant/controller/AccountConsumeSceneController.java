package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.exception.BusiException;
import com.welfare.persist.dao.AccountChangeEventRecordDao;
import com.welfare.persist.dto.AccountConsumeScenePageDTO;
import com.welfare.persist.dto.query.AccountConsumePageQuery;
import com.welfare.persist.entity.Account;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.AccountConsumeSceneService;
import com.welfare.service.AccountConsumeSceneStoreRelationService;
import com.welfare.service.dto.AccountConsumeSceneAddReq;
import com.welfare.service.dto.AccountConsumeSceneDTO;
import com.welfare.service.dto.AccountConsumeSceneReq;
import com.welfare.servicemerchant.converter.AccountConsumeSceneConverter;
import com.welfare.servicemerchant.dto.AccountConsumePageReq;
import com.welfare.servicemerchant.dto.UpdateStatusReq;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

  @GetMapping("/test")
  public void test(@RequestParam("list")List<Long> list){
   /* List<AccountChangeEventRecord> list = new LinkedList<AccountChangeEventRecord>();
    AccountChangeEventRecord a = AccountUtils.assemableChangeEvent(AccountChangeType.ACCOUNT_TYPE_DELETE, 1l,"aaa");
    AccountChangeEventRecord b = AccountUtils.assemableChangeEvent(AccountChangeType.ACCOUNT_TYPE_DELETE, 2l,"aaa");
    list.add(a);
    list.add(b);
    accountChangeEventRecordService.batchSave(list,AccountChangeType.ACCOUNT_TYPE_DELETE);*/
    List<Account>  accountList = accountCustomizeMapper.queryByConsumeSceneIdList(list);
    System.out.println(accountList);
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
  public R<Boolean> save(@RequestBody AccountConsumeSceneAddReq accountConsumeSceneAddReq) {
    try {
      return success(accountConsumeSceneService.save(accountConsumeSceneAddReq));
    } catch (BusiException be) {
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/update")
  @ApiOperation("修改员工消费配置")
  public R<Boolean> update(@RequestBody AccountConsumeSceneReq accountConsumeSceneReq) {
    try {
      return success(accountConsumeSceneService.update(accountConsumeSceneReq));
    } catch (BusiException be) {
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/updateStatus")
  @ApiOperation("激活")
  public R<Boolean> updateStatus(@RequestBody UpdateStatusReq updateStatusReq) {
    try {
      return success(accountConsumeSceneService.updateStatus(Long.parseLong(updateStatusReq.getId()),
          updateStatusReq.getConsumeSceneStatus()));
    } catch (BusiException be) {
      return R.fail(be.getMessage());
    }
  }

  @PostMapping("/delete")
  @ApiOperation("删除员工消费配置")
  public R<Boolean> delete(@RequestBody UpdateStatusReq updateStatusReq) {
    try {
      return success(accountConsumeSceneService.delete(Long.parseLong(updateStatusReq.getId())));
    }catch (BusiException be){
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
}
