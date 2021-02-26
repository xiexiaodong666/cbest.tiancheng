package com.welfare.persist.mapper;

import com.welfare.persist.dto.CheckBillDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/23/2021
 */
@Mapper
public interface CheckBillMapper {
    List<CheckBillDetail> queryCheckBill(@Param("storeCodes") List<String> storeCodes,@Param("startTime") Date startDate,@Param("endTime") Date endTime);
}
