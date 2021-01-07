package com.welfare.servicemerchant.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



  @ApiOperation("查询字典")
  @GetMapping("/all")
  public R<Map<String, Map<String,String>>> getAll() {

    return null;
  }

}