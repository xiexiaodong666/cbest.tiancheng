package com.welfare.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.TempAccountDepositApplyDao;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.TempAccountDepositApply;
import com.welfare.persist.mapper.TempAccountDepositApplyMapper;
import com.welfare.service.TempAccountDepositApplyService;
import com.welfare.service.dto.AccountDepositRequest;
import com.welfare.service.listener.DepositApplyUploadListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  1:05 PM
 */
@Service
@Slf4j
public class TempAccountDepositApplyServiceImpl implements TempAccountDepositApplyService {

  @Autowired
  private TempAccountDepositApplyDao tempAccountDepositApplyDao;

  @Autowired
  private TempAccountDepositApplyMapper tempAccountDepositApplyMapper;

  @Autowired
  private RedissonClient redissonClient;

  @Override
  public Boolean saveAll(List<TempAccountDepositApply> applys) {
    return tempAccountDepositApplyDao.saveBatch(applys);
  }

  @Override
  public Boolean delByFileId(String fileId) {
    QueryWrapper<TempAccountDepositApply> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(TempAccountDepositApply.FILE_ID, fileId);
    return tempAccountDepositApplyDao.remove(queryWrapper);
  }

  @Override
  public Page<TempAccountDepositApplyDTO> pageByFileId(int current, int size, String fileId) {
    Page<TempAccountDepositApply> page = new Page<>();
    page.setCurrent(current);
    page.setSize(size);
    return tempAccountDepositApplyMapper.pageByFileId(page, fileId);
  }

  @Override
  public List<TempAccountDepositApply> getAllByFileId(String fileId) {
    QueryWrapper<TempAccountDepositApply> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(TempAccountDepositApply.FILE_ID, fileId);
    return tempAccountDepositApplyDao.getBaseMapper().selectList(queryWrapper);
  }

  @Override
  public String upload(MultipartFile multipartFile, String requestId) {
    String fileId = getFileIdByRequestId(requestId);
    if (StringUtils.isNotBlank(fileId)) {
      return fileId;
    }
    String lockKey = RedisKeyConstant.buidKey(RedisKeyConstant.TEMP_ACCOUNT_DEPOSIT_APPLY_SAVE, requestId);
    RLock lock = redissonClient.getLock(lockKey);
    try {
      boolean locked = lock.tryLock(2, TimeUnit.SECONDS);
      if (locked) {
        fileId = getFileIdByRequestId(requestId);
        if (StringUtils.isNotBlank(fileId)) {
          return fileId;
        }
        fileId = UUID.randomUUID().toString();
        DepositApplyUploadListener listener = new DepositApplyUploadListener(this, requestId , fileId);
        EasyExcel.read(multipartFile.getInputStream(), AccountDepositRequest.class, listener).sheet().doRead();
        log.info("批量导入员工账号存储申请完成. requestId:{} fileId:{}", requestId, fileId);
        return fileId;
      } else {
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "操作频繁稍后再试！", null);
      }
    } catch (Exception e) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "上传账号存款申请临时文件错误", e);
    } finally {
      if (lock.isHeldByCurrentThread()) {
        lock.unlock();
      }
    }
  }

  @Override
  public String getFileIdByRequestId(String requestId) {
    QueryWrapper<TempAccountDepositApply> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(TempAccountDepositApply.REQUEST_ID, requestId);
    List<TempAccountDepositApply> tempAccountDepositApplies = tempAccountDepositApplyDao.getBaseMapper().selectList(queryWrapper);
    if (CollectionUtils.isNotEmpty(tempAccountDepositApplies)) {
      return tempAccountDepositApplies.get(0).getFileId();
    } else {
      return null;
    }
  }
}