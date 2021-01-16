package com.welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.SettleDetailService;
import com.welfare.service.dto.SettleMerInfoResp;
import com.welfare.service.dto.SettleMerTransDetailResp;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/16 4:03 下午
 * @desc 账户信息查询
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/settlement/merInfo")
@Api(tags = "账户信息管理")
public class MerInfoController {

    @Autowired
    private SettleDetailService settleDetailService;

    @GetMapping("/")
    public R<SettleMerInfoResp> getAccountInfo(){
        SettleMerInfoResp settleAccountInfoResp= settleDetailService.getAccountInfo(1l);
        return R.success(settleAccountInfoResp);
    }


    @GetMapping("/page")
    public R<Page<SettleMerTransDetailResp>> getAccountTransDetail(SettleMerInfoResp settleMerInfoResp){
        Page<SettleMerTransDetailResp> settleAccountTransDetailRespPage = settleDetailService.getAccountTransDetail(1l);
        return R.success(settleAccountTransDetailRespPage);
    }
}
