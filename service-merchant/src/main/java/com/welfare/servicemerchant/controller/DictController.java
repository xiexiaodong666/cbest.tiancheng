package com.welfare.servicemerchant.controller;

import com.welfare.service.DictService;
import com.welfare.service.dto.DictDTO;
import com.welfare.service.dto.DictReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 字典管理
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  2:01 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/dict")
@Api(tags = "字典管理")
public class DictController implements IController {
  private final DictService dictService;


  @ApiOperation("查询字典")
  @GetMapping("/all")
  public R<Map<String, Map<String,String>>> getAll() {

    return null;
  }

  @ApiOperation("根据类型查询字典")
  @GetMapping("/type")
  public R<List<DictDTO>> getByType(@Valid DictReq req) {
    return R.success(dictService.getByType(req));
  }


}