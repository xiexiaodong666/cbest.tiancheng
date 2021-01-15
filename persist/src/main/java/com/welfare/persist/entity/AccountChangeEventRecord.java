package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 17:31
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_change_event_record")
@ApiModel("账户变更记录表")
public class AccountChangeEventRecord {
  private Long id;
  private Long accountCode;
  private String changeType;
  private String changeValue;
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String createUser;
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private Date createTime;
}
