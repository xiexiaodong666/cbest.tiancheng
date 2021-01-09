package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.TempAccountDepositApplyDTO;
import com.welfare.persist.entity.TempAccountDepositApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * (temp_account_deposit_apply)数据Mapper
 *
 * @author kancy
 * @since 2021-01-09 13:01:22
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface TempAccountDepositApplyMapper extends BaseMapper<TempAccountDepositApply> {

  Page<TempAccountDepositApplyDTO> pageByFileId(@Param("page") Page<TempAccountDepositApply> page, @Param("fileId") String fileId);
}
