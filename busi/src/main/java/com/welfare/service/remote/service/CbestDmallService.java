package com.welfare.service.remote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.remote.entity.pos.*;


/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 3:25 下午
 */
public interface CbestDmallService {

  /**
   * 分页查询价格模板
   * @param req
   * @return
   */
  Page<PriceTemplateBrief> listPriceTemplate(PosPriceTemplateReq req);

  /**
   * 查询价格模板
   * @param id
   * @return
   */
  PosPriceTemplate queryPriceTemplate(Long id);

  /**
   * 创建价格模板
   * @param req
   * @return
   */
  PosPriceTemplate createPriceTemplate(PosPriceTemplateSaveReq req);

  /**
   * 修改价格模板
   * @param req
   * @return
   */
  PosPriceTemplate modifyPriceTemplate(PosPriceTemplate req);

  /**
   * 分页查询收银机价格模板
   * @param req
   * @return
   */
  Page<PosTerminalPriceTemplateResp> listTerminalPriceTemplate(TerminalPriceTemplateReq req);

  /**
   * 修改收银机价格模板
   * @param req
   * @return
   */
  PosTerminalPriceTemplateResp modifyTerminalPriceTemplate(TerminalPriceTemplateUpdateReq req);
}
