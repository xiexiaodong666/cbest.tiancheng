package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.AccountType;
import com.welfare.service.AccountTypeService;
import com.welfare.servicemerchant.converter.AccountTypeConverter;
import com.welfare.servicemerchant.dto.AccountTypeReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.apache.logging.log4j.util.Strings;
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
 * @date 2021/1/7 19:46
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountType")
@Api(tags = "员工类型管理")
public class AccountTypeController implements IController {

  @Autowired
  private AccountTypeService accountTypeService;
  @Autowired
  private AccountTypeConverter accountTypeConverter;

  @GetMapping("/page")
  @ApiOperation("分页员工类型列表")
  public R<Page<AccountType>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = false) @ApiParam("商户代码") String merCode,
      @RequestParam(required = false) @ApiParam("类型编码") String typeCode,
      @RequestParam(required = false) @ApiParam("类型名称") Integer typeName,
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd){
    Page<AccountType> page = new Page(currentPage,pageSize);

    QueryWrapper<AccountType> queryWrapper = new QueryWrapper<>();
    if(Strings.isNotEmpty(merCode)){
      queryWrapper.eq(AccountType.MER_CODE,merCode);
    }
    if(Strings.isNotEmpty(typeCode)){
      queryWrapper.eq(AccountType.TYPE_CODE,typeCode);
    }
    if( null != typeName){
      queryWrapper.eq(AccountType.TYPE_NAME,typeName);
    }
    if( null !=  createTimeStart){
      queryWrapper.gt(AccountType.CREATE_TIME,createTimeStart);
    }
    if( null !=  createTimeEnd){
      queryWrapper.lt(AccountType.CREATE_TIME,createTimeStart);
    }
    queryWrapper.ne(AccountType.DELETED,0);

    Page<AccountType> accountTypePage = accountTypeService.pageQuery(page, queryWrapper);
    return success(accountTypePage);
  }

  @GetMapping("/{id}")
  @ApiOperation("员工类型详情")
  public R<AccountType> detail(@PathVariable Long id){
    return success(accountTypeService.getAccountType(id));
  }

  @PostMapping("/save")
  @ApiOperation("新增员工类型")
  public R<Boolean> save(@RequestBody AccountTypeReq accountTypeReq){
    AccountType accountType = accountTypeConverter.toEntity(accountTypeReq);
    return success(accountTypeService.save(accountType));
  }

  @PostMapping("/update")
  @ApiOperation("修改员工类型")
  public R<Boolean> update(@RequestBody AccountTypeReq accountTypeReq){
    AccountType accountType = accountTypeConverter.toEntity(accountTypeReq);
    return success(accountTypeService.update(accountType));
  }

  @PostMapping("/delete/{id}")
  @ApiOperation("删除员工类型")
  public R<Boolean> delete(@PathVariable Long id){
    return success(accountTypeService.delete(id));
  }

}
