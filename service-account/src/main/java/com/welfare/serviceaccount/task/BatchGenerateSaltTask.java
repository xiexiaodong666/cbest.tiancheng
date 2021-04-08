package com.welfare.serviceaccount.task;

import com.welfare.service.BarcodeService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * 每天 批量生成缺失的period
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/31 2:27 下午
 */
@Slf4j
@RequiredArgsConstructor
@Component
@JobHandler("batchGenerateSaltTask")
public class BatchGenerateSaltTask extends IJobHandler {

  private final BarcodeService barcodeService;

  @Override
  public ReturnT<String> execute(String s) {
    try {
      log.info("============{}批量生成缺失的period任务执行开始===================", this.getClass().getName());
      barcodeService.batchGenerateSalt();
      log.info("============{}批量生成缺失的period任务执行结束===================", this.getClass().getName());
    } catch (Exception e) {
      log.error("{}批量生成缺失的period任务执行异常", this.getClass().getName(), e);
      return ReturnT.FAIL;
    }
    return ReturnT.SUCCESS;
  }
}
