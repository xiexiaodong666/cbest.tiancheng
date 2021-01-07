package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.servicemerchant.dto.AccountConsumeSceneDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
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
public class AccountConsumeSceneController {
  @GetMapping("/page")
  @ApiOperation("分页查询员工消费配置列表")
  public R<Page<AccountConsumeSceneDTO>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = false) @ApiParam("商户代码") String merCode,
      @RequestParam(required = false) @ApiParam("员工类型编码") String accountTypeId,
      @RequestParam(required = false) @ApiParam("使用状态") Integer status,
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd){
    return null;
  }

  @GetMapping("/{id}")
  @ApiOperation("员工消费配置详情")
  public R<AccountConsumeSceneDTO> detail(@PathVariable Long id){
    return null;
  }

  @PostMapping("/save")
  @ApiOperation("新增员工消费配置")
  public R<AccountConsumeScene> save(@RequestBody List<AccountConsumeScene> accountConsumeSceneList){
    return null;
  }

  @PostMapping("/update")
  @ApiOperation("修改员工消费配置")
  public R<AccountConsumeScene> update(@RequestBody List<AccountConsumeScene> accountConsumeSceneList){
    return null;
  }

  @PostMapping("/updateStatus/{id}")
  @ApiOperation("激活")
  public R<AccountConsumeScene> updateStatus(@PathVariable Long id,@RequestParam(required = false) @ApiParam("使用状态") Integer status){
    return null;
  }

  @PostMapping("/delete/{id}")
  @ApiOperation("删除员工消费配置")
  public R<Boolean> delete(@PathVariable Long id){
    return null;
  }


  @ApiOperation("员工消费配置导出")
  @GetMapping(value="/exportAccountConsumeScene")
  public R<String> exportAccountConsumeScene(HttpServletResponse response,
      @RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = false) @ApiParam("商户代码") String merCode,
      @RequestParam(required = false) @ApiParam("员工类型编码") String accountTypeId,
      @RequestParam(required = false) @ApiParam("使用状态") Integer status,
      @RequestParam(required = false) @ApiParam("创建时间_start") Date createTimeStart,
      @RequestParam(required = false) @ApiParam("创建时间_end") Date createTimeEnd){
    return null;
  }
}
