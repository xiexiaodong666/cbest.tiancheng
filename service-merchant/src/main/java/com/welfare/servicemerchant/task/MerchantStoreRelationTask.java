package com.welfare.servicemerchant.task;

import com.welfare.persist.dao.MerchantStoreRelationDao;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.persist.mapper.MerchantStoreRelationMapper;
import com.welfare.service.MerchantStoreRelationService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/14 1:47 PM
 */
@JobHandler(value = "merchantStoreRelationTask")
@Slf4j
public class MerchantStoreRelationTask extends IJobHandler {


  private MerchantStoreRelationService merchantStoreRelationService;
  private MerchantStoreRelationDao merchantStoreRelationDao;

  private MerchantStoreRelationMapper merchantStoreRelationMapper;

  @Override
  public ReturnT<String> execute(String pramas) throws Exception {

    log.info("============" + this.getClass().getName() + "任务执行开始===================");

    //处理数据
    log.info("请求处理参数：{}", pramas);

    // log.debug("准备执行任务数据：{}", JSON.toJSONString(dealList));
    try {
      List<MerchantStoreRelation> merchantStoreRelationList = merchantStoreRelationMapper
          .getMerchantStoreRelationListBySyncStatus(0);
      if (CollectionUtils.isNotEmpty(merchantStoreRelationList)) {
        Map<String, List<MerchantStoreRelation>> groupByMerCode = merchantStoreRelationList.stream()
            .collect(Collectors.groupingBy(MerchantStoreRelation::getMerCode));

        for (Map.Entry<String, List<MerchantStoreRelation>> entry : groupByMerCode.entrySet()) {
          String merCode = entry.getKey();
          List<MerchantStoreRelation> merchantStoreRelationPushList = entry.getValue();

          boolean isDelete = merchantStoreRelationPushList.stream().anyMatch(
              m -> !m.getVersion().equals(0));


          boolean isUpdate = merchantStoreRelationPushList.stream().anyMatch(
              m -> !m.getVersion().equals(0));
          // update
          if (isUpdate) {

          }
          // add
          else {

          }

        }

      }


    } catch (Exception e) {
      log.info("============" + this.getClass().getName() + "任务执行【异常】===================");
      log.info("任务执行异常,异常信息：{}", e);
      return ReturnT.FAIL;
    }

    log.info("============" + this.getClass().getName() + "任务执行【完成】===================");
    return ReturnT.SUCCESS;
  }
}
