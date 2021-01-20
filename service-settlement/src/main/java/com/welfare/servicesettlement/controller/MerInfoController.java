package com.welfare.servicesettlement.controller;

import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.base.BasePageVo;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.service.SettleDetailService;
import com.welfare.service.dto.SettleMerInfoResp;
import com.welfare.service.dto.SettleMerTransDetailPageReq;
import com.welfare.service.dto.SettleMerTransDetailReq;
import com.welfare.service.dto.SettleMerTransDetailResp;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
public class MerInfoController  implements IController {

    @Autowired
    private SettleDetailService settleDetailService;

    @Autowired
    private FileUploadServiceUtil fileUploadService;

    @GetMapping("")
    @ApiOperation("查询商户基本额度信息")
    @MerchantUser
    public R<SettleMerInfoResp> getAccountInfo(){
        MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
        if(merchantUser == null){
            throw new BusiException(ExceptionCode.BUSI_ERROR_NO_PERMISSION,"未登录的商户",null);
        }
        SettleMerInfoResp settleAccountInfoResp= settleDetailService.getMerAccountInfo(merchantUser.getMerchantCode());
        return R.success(settleAccountInfoResp);
    }


    @GetMapping("/page")
    @ApiOperation("分页商户账户明细")
    @MerchantUser
    public R<BasePageVo<SettleMerTransDetailResp>> getAccountTransPageDetail(SettleMerTransDetailPageReq settleMerTransDetailPageReq){
        MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
        if(merchantUser == null){
            throw new BusiException(ExceptionCode.BUSI_ERROR_NO_PERMISSION,"未登录的商户",null);
        }
        BasePageVo<SettleMerTransDetailResp> settleAccountTransDetailRespPage = settleDetailService.getMerAccountTransPageDetail(merchantUser.getMerchantCode(), settleMerTransDetailPageReq);
        return R.success(settleAccountTransDetailRespPage);
    }

    @ApiOperation("导出商户账户明细")
    @GetMapping("/excelExport")
    @MerchantUser
    public R<String> getAccountTransDetail(SettleMerTransDetailReq settleMerTransDetailReq){
        MerchantUserInfo merchantUser = MerchantUserHolder.getMerchantUser();
        if(merchantUser == null){
            throw new BusiException(ExceptionCode.BUSI_ERROR_NO_PERMISSION,"未登录的商户",null);
        }
        List<SettleMerTransDetailResp> settleAccountTransDetailRespPage = settleDetailService
                .getMerAccountTransDetail(merchantUser.getMerchantCode(), settleMerTransDetailReq)
                .stream().map(settleMerTransDetailResp -> {
                    settleMerTransDetailResp.setInOrOutType(settleMerTransDetailResp.getInOrOutType().equals("in") ? "入账":"出账");
                    settleMerTransDetailResp.setTransType(WelfareConstant.MerCreditType.findByCode(settleMerTransDetailResp.getTransType()).desc());
                    return settleMerTransDetailResp;
                }).collect(Collectors.toList());

        String path = null;
        try {
            path = fileUploadService.uploadExcelFile(
                    settleAccountTransDetailRespPage, SettleMerTransDetailResp.class, "商户账户明细");
        } catch (IOException e) {
            throw new BusiException(null, "文件导出异常", null);
        }
        return success(fileUploadService.getFileServerUrl(path));
    }
}
