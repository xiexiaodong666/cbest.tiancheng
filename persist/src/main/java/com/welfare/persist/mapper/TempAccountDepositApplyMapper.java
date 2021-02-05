package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountApplyTotalDTO;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.TempAccountDepositApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (temp_account_deposit_apply)数据Mapper
 *
 * @author kancy
 * @since 2021-01-09 13:01:22
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface TempAccountDepositApplyMapper extends BaseMapper<TempAccountDepositApply> {

  /**
   * 通过fileId分页查询批量员工申请上传文件内容
   * @param page
   * @param fileId
   * @return
   */
  Page<TempAccountDepositApplyDTO> pageByFileIdByExistAccount(@Param("page") Page<TempAccountDepositApply> page,
                                                              @Param("fileId") String fileId,
                                                              @Param("merCode") String merCode);

  /**
   * 通过fileId查询批量员工申请上传文件内容
   * @param fileId
   * @return
   */
  List<TempAccountDepositApplyDTO> pageByFileIdByExistAccount(@Param("fileId") String fileId, @Param("merCode") String merCode);

  /**
   * 通过fileId分页查询批量员工申请上传文件里的总人数和总金额
   * @param fileId
   * @return
   */
  AccountApplyTotalDTO getUserCountAndTotalAmount(@Param("fileId") String fileId, @Param("merCode") String merCode);
}
