package com.welfare.serviceaccount.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.AccountConsumeScene;
import com.welfare.persist.entity.AccountType;
import com.welfare.service.AccountConsumeSceneService;
import com.welfare.serviceaccount.dto.AccountConsumeSceneDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工消费场景配置服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 11:08:59
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountConsumeScene")
@Api(tags = "员工消费配置管理")
public class AccountConsumeSceneController implements IController {
    private final AccountConsumeSceneService accountConsumeSceneService;

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
    public R<AccountConsumeScene> save(@RequestBody AccountConsumeScene accountType){
        return null;
    }

    @PostMapping("/update")
    @ApiOperation("修改员工消费配置")
    public R<AccountConsumeScene> update(@RequestBody AccountConsumeScene accountType){
        return null;
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除员工消费配置")
    public R<Boolean> delete(@PathVariable Integer id){
        return null;
    }


}