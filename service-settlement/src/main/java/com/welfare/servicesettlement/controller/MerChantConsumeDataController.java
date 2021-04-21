package com.welfare.servicesettlement.controller;

import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.service.dto.merchantconsume.ExportMerChantConsumeData;
import com.welfare.service.dto.merchantconsume.MerChantConsumeDataDetailApiResponse;
import com.welfare.service.dto.merchantconsume.MerChantConsumeDataRowsApiResponse;
import com.welfare.service.dto.merchantconsume.WelfareMerChantConsumeDataApiRequest;
import com.welfare.service.dto.merchantconsume.WelfareMerChantConsumeDataApiResponse;
import com.welfare.service.dto.merchantconsume.WelfareMerChantConsumeDataApiResponse.TableExt;
import com.welfare.service.dto.merchantconsume.WelfareMerChantConsumeDataApiResponse.TopExt;
import com.welfare.service.remote.ServiceMirrorFeignClient;
import com.welfare.service.remote.entity.request.WelfareMerChantConsumeDataRequest;
import com.welfare.service.remote.entity.response.WelfareMerChantConsumeDataBaiscResponse;
import com.welfare.service.remote.entity.response.WelfareMerChantConsumeDataResponse;
import com.welfare.servicesettlement.util.FileUploadServiceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 10:43 AM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchant/consume/data")
@Api(tags = "平台端商户消费报表")
public class MerChantConsumeDataController {

  /**
   * attribute 类型标注，1明细 3总的汇总数据 4 最顶部的数据
   */
  @Autowired(required = false)
  private ServiceMirrorFeignClient serviceMirrorFeignClient;

  @Autowired
  private FileUploadServiceUtil fileUploadServiceUtil;

  @PostMapping("/get")
  @ApiOperation("查询商户消费统计")
  public R<WelfareMerChantConsumeDataApiResponse> getWelfareMerChantConsumeData(
      @RequestBody WelfareMerChantConsumeDataApiRequest request) {

    WelfareMerChantConsumeDataResponse response = serviceMirrorFeignClient
        .getWelfareMerChantConsumeData(WelfareMerChantConsumeDataRequest.of(request));

    if (response == null || !"200".equals(response.getCode()) || CollectionUtils.isEmpty(
        response.getDatas())) {
      return R.success();
    }

    Long id = 0l;
    WelfareMerChantConsumeDataApiResponse apiResponse = getApiResponse(response);
    List<MerChantConsumeDataRowsApiResponse> rowsApiResponseList = apiResponse.getRowsData();
    if (CollectionUtils.isNotEmpty(rowsApiResponseList)) {
      for (MerChantConsumeDataRowsApiResponse merChantConsumeDataRowsApiResponse:
      rowsApiResponseList) {
        id++;
        merChantConsumeDataRowsApiResponse.setId(id);
        List<MerChantConsumeDataDetailApiResponse> dataDetailApiResponseList = merChantConsumeDataRowsApiResponse.getConsumeTypeDetailList();
        if(CollectionUtils.isNotEmpty(dataDetailApiResponseList)) {
          for (MerChantConsumeDataDetailApiResponse merChantConsumeDataDetailApiResponse:
          dataDetailApiResponseList) {
            id++;
            merChantConsumeDataDetailApiResponse.setId(id);
          }
        }
      }
    }
    List<TableExt> tableExtList = apiResponse.getTableExt();
    if(CollectionUtils.isNotEmpty(tableExtList)) {
      for (TableExt tableExt:
      tableExtList) {
        id++;
        tableExt.setId(id);
        List<MerChantConsumeDataDetailApiResponse> dataDetailApiResponseList = tableExt.getConsumeTypeDetailList();
        if(CollectionUtils.isNotEmpty(dataDetailApiResponseList)) {
          for (MerChantConsumeDataDetailApiResponse merChantConsumeDataDetailApiResponse:
              dataDetailApiResponseList) {
            id++;
            merChantConsumeDataDetailApiResponse.setId(id);
          }
        }
      }
    }

    return R.success(apiResponse);
  }

  @PostMapping("/export")
  @ApiOperation("导出商户消费统计")
  public R<String> exportWelfareMerChantConsumeData(
      @RequestBody WelfareMerChantConsumeDataApiRequest request) throws IOException {

    WelfareMerChantConsumeDataResponse response = serviceMirrorFeignClient
        .getWelfareMerChantConsumeData(WelfareMerChantConsumeDataRequest.of(request));

    if (response == null || !"200".equals(response.getCode())) {
      return R.success();
    }

    WelfareMerChantConsumeDataApiResponse apiResponse = getApiResponse(response);

    List<ExportMerChantConsumeData> exportMerChantConsumeDataList = new ArrayList<>();

    List<MerChantConsumeDataRowsApiResponse> merChantConsumeDataRowsApiResponseList = apiResponse
        .getRowsData();

    if (CollectionUtils.isNotEmpty(merChantConsumeDataRowsApiResponseList)) {
      for (MerChantConsumeDataRowsApiResponse merChantConsumeDataRowsApiResponse :
          merChantConsumeDataRowsApiResponseList) {
        exportMerChantConsumeDataList.add(
            ExportMerChantConsumeData.rowsOf(merChantConsumeDataRowsApiResponse));
        for (MerChantConsumeDataDetailApiResponse merChantConsumeDataDetailApiResponse :
            merChantConsumeDataRowsApiResponse.getConsumeTypeDetailList()) {
          exportMerChantConsumeDataList.add(
              ExportMerChantConsumeData.detailOf(merChantConsumeDataDetailApiResponse));
        }
      }
    }

    List<TableExt> tableExtList = apiResponse.getTableExt();
    if (CollectionUtils.isNotEmpty(tableExtList)) {

      for (TableExt tableExt :
          tableExtList) {
        exportMerChantConsumeDataList.add(ExportMerChantConsumeData.extOf(tableExt));

        for (MerChantConsumeDataDetailApiResponse merChantConsumeDataDetailApiResponse :
            tableExt.getConsumeTypeDetailList()) {
          exportMerChantConsumeDataList.add(
              ExportMerChantConsumeData.detailOf(merChantConsumeDataDetailApiResponse));
        }
      }
    }

    String path = fileUploadServiceUtil.uploadExcelFile(
        exportMerChantConsumeDataList, ExportMerChantConsumeData.class, "客户消费汇总表");

    return R.success(fileUploadServiceUtil.getFileServerUrl(path));
  }

  private WelfareMerChantConsumeDataApiResponse getApiResponse(
      WelfareMerChantConsumeDataResponse response) {

    WelfareMerChantConsumeDataApiResponse apiResponse = new WelfareMerChantConsumeDataApiResponse();

    List<MerChantConsumeDataRowsApiResponse> rowsData = new ArrayList<>();
    apiResponse.setRowsData(rowsData);

    List<TableExt> tableExtList = new ArrayList<>();
    apiResponse.setTableExt(tableExtList);

    List<WelfareMerChantConsumeDataBaiscResponse> dataBasicResponseList = response.getDatas();

    Map<String, List<WelfareMerChantConsumeDataBaiscResponse>> listMap = dataBasicResponseList
        .stream().collect(Collectors.groupingBy(w -> w.getAttribute()));

    // 处理最顶部的数据
    List<WelfareMerChantConsumeDataBaiscResponse> topList = listMap.get("4");
    WelfareMerChantConsumeDataBaiscResponse topData = topList.get(0);
    TopExt topExt = new TopExt();
    BeanUtils.copyProperties(topData, topExt);
    apiResponse.setTopExt(topExt);

    // 处理表格行数据
    List<WelfareMerChantConsumeDataBaiscResponse> rowsList = listMap.get("1");
    // 根据商户分组排序
    if(CollectionUtils.isEmpty(rowsList)) {

      return apiResponse;
    }

    Map<String, List<WelfareMerChantConsumeDataBaiscResponse>> merMap = rowsList
        .stream().collect(Collectors.groupingBy(WelfareMerChantConsumeDataBaiscResponse::getMerCode,
                                                LinkedHashMap::new, Collectors.toList()
        ));

    for (Map.Entry<String, List<WelfareMerChantConsumeDataBaiscResponse>> entry : merMap
        .entrySet()) {

      List<WelfareMerChantConsumeDataBaiscResponse> merDataList = entry.getValue();
      Map<String, List<WelfareMerChantConsumeDataBaiscResponse>> businessMap = merDataList
          .stream().collect(Collectors.groupingBy(
              WelfareMerChantConsumeDataBaiscResponse::getBusinessType, LinkedHashMap::new,
              Collectors.toList()
          ));

      List<WelfareMerChantConsumeDataBaiscResponse> selfDataList = businessMap.get("self");
      List<WelfareMerChantConsumeDataBaiscResponse> thirdDataList = businessMap.get("third");
      boolean isFillMerchantAttributes = true;
      if(CollectionUtils.isNotEmpty(selfDataList)) {
        // 该商户具有自营属性, 非自营不用填充表格前面的商户共同属性数据
        isFillMerchantAttributes = false;
        WelfareMerChantConsumeDataBaiscResponse selfResponse = selfDataList.get(0);
        MerChantConsumeDataRowsApiResponse selfOf = MerChantConsumeDataRowsApiResponse.selfOf(
            selfResponse);
        List<MerChantConsumeDataDetailApiResponse> selfMerChantConsumeDataDetailApiResponseList = new ArrayList<>();
        selfDataList.forEach(s->{
          MerChantConsumeDataDetailApiResponse merChantConsumeDataDetailApiResponse = new MerChantConsumeDataDetailApiResponse();
          s.setConsumeType(ConsumeTypeEnum.getByType(s.getConsumeType()).getDesc());
          BeanUtils.copyProperties(s, merChantConsumeDataDetailApiResponse);
          selfMerChantConsumeDataDetailApiResponseList.add(merChantConsumeDataDetailApiResponse);
        });

        selfOf.setConsumeTypeDetailList(selfMerChantConsumeDataDetailApiResponseList);

        rowsData.add(selfOf);
      }

      if(CollectionUtils.isNotEmpty(thirdDataList)) {
        WelfareMerChantConsumeDataBaiscResponse thirdResponse = thirdDataList.get(0);

        MerChantConsumeDataRowsApiResponse thirdOf = MerChantConsumeDataRowsApiResponse.thirdOf(
            thirdResponse, isFillMerchantAttributes);

        List<MerChantConsumeDataDetailApiResponse> thirdMerChantConsumeDataDetailApiResponseList = new ArrayList<>();

        thirdDataList.forEach(t->{
          MerChantConsumeDataDetailApiResponse merChantConsumeDataDetailApiResponse = new MerChantConsumeDataDetailApiResponse();
          t.setConsumeType(ConsumeTypeEnum.getByType(t.getConsumeType()).getDesc());

          BeanUtils.copyProperties(t, merChantConsumeDataDetailApiResponse);
          thirdMerChantConsumeDataDetailApiResponseList.add(merChantConsumeDataDetailApiResponse);
        });

        thirdOf.setConsumeTypeDetailList(thirdMerChantConsumeDataDetailApiResponseList);

        rowsData.add(thirdOf);
      }

    }

    // 处理表格总的汇总数据
    List<WelfareMerChantConsumeDataBaiscResponse> totalList = listMap.get("3");

    Map<String, List<WelfareMerChantConsumeDataBaiscResponse>> businessMap = totalList
        .stream().collect(Collectors
                              .groupingBy(WelfareMerChantConsumeDataBaiscResponse::getBusinessType,
                                          LinkedHashMap::new, Collectors.toList()
                              ));
    List<WelfareMerChantConsumeDataBaiscResponse> selfDataList = businessMap.get("self");
    List<WelfareMerChantConsumeDataBaiscResponse> thirdDataList = businessMap.get("third");

    boolean isFillMerchantAttributes = true;

    if(CollectionUtils.isNotEmpty(selfDataList)) {
      isFillMerchantAttributes = false;
      WelfareMerChantConsumeDataBaiscResponse selfResponse = selfDataList.get(0);
      TableExt selfOf = TableExt.selfOf(selfResponse);
      List<MerChantConsumeDataDetailApiResponse> selfMerChantConsumeDataDetailApiResponseList = new ArrayList<>();
      selfDataList.forEach(s->{
        MerChantConsumeDataDetailApiResponse merChantConsumeDataDetailApiResponse = new MerChantConsumeDataDetailApiResponse();
        s.setConsumeType(ConsumeTypeEnum.getByType(s.getConsumeType()).getDesc());

        BeanUtils.copyProperties(s, merChantConsumeDataDetailApiResponse);
        selfMerChantConsumeDataDetailApiResponseList.add(merChantConsumeDataDetailApiResponse);
      });

      selfOf.setConsumeTypeDetailList(selfMerChantConsumeDataDetailApiResponseList);

      tableExtList.add(selfOf);
    }

    if(CollectionUtils.isNotEmpty(thirdDataList)) {
      WelfareMerChantConsumeDataBaiscResponse thirdResponse = thirdDataList.get(0);

      TableExt thirdOf = TableExt.thirdOf(thirdResponse, isFillMerchantAttributes);

      List<MerChantConsumeDataDetailApiResponse> thirdMerChantConsumeDataDetailApiResponseList = new ArrayList<>();

      thirdDataList.forEach(t->{
        MerChantConsumeDataDetailApiResponse merChantConsumeDataDetailApiResponse = new MerChantConsumeDataDetailApiResponse();

        t.setConsumeType(ConsumeTypeEnum.getByType(t.getConsumeType()).getDesc());
        BeanUtils.copyProperties(t, merChantConsumeDataDetailApiResponse);
        thirdMerChantConsumeDataDetailApiResponseList.add(merChantConsumeDataDetailApiResponse);
      });

      thirdOf.setConsumeTypeDetailList(thirdMerChantConsumeDataDetailApiResponseList);

      tableExtList.add(thirdOf);
    }

    return apiResponse;
  }
}
