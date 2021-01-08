package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.dto.MerchantPageReq;
import com.welfare.service.dto.MerchantReq;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * 商户信息服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MerchantService {
    /**
     * 查询商户列表（不分页）
     * @return
     */
    List<Merchant>  list(MerchantReq req);

    /**
     * 查询商户详情
     * @param id
     * @return
     */
    Merchant detail(Long id);

    /**
     * 查询商户列表（分页））
     * @param merchantPageReq
     * @return
     */
    Page<Merchant> page(Page<Merchant> page,MerchantPageReq merchantPageReq);

    /**
     * 新增商户
     * @param merchant
     * @return
     */
    boolean add(Merchant merchant);

    /**
     * 编辑商户
     * @param merchant
     * @return
     */
    boolean update(Merchant merchant);

    /**
     * 导出商户列表
     * @param merchantPageReq
     * @return
     */
    String exportList(MerchantPageReq merchantPageReq);
}
