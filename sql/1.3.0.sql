# 条码加盐表添加索引
create index idx_bs_valid_period on barcode_salt(valid_period);
create index idx_bs_valid_period_numeric on barcode_salt(valid_period_numeric);

# 流水中记录条码信息或者磁卡信息
alter table account_bill_detail add column payment_type varchar(20) comment '支付方式:条码或者线上' after order_channel;
alter table account_bill_detail add column payment_type_info varchar(64) comment '支付方式信息:条码或磁卡信息' after payment_type;


alter table order_info add column time_interval INT(4) comment '就餐时段: 1-早餐,2-午餐,3-晚餐,4-宵夜';

# 删除订单表中order_id唯一约束
alter table order_info drop index uk_order_id;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
# 创建文件通用存储表
-- ----------------------------
-- Table structure for file_universal_storage
-- ----------------------------
DROP TABLE IF EXISTS `file_universal_storage`;
CREATE TABLE `file_universal_storage` (
  `id` bigint(20) NOT NULL COMMENT '自增id',
  `type` varchar(100) DEFAULT NULL COMMENT '文件存储类型',
  `img_key` varchar(255) DEFAULT NULL COMMENT 's3存储key',
  `url` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

# 创建门店消费类型虚拟收银机号关联表
-- ----------------------------
-- Table structure for store_consume_type
-- ----------------------------
DROP TABLE IF EXISTS `store_consume_type`;
CREATE TABLE `store_consume_type` (
  `id` bigint(20) NOT NULL COMMENT '自增id',
  `store_code` varchar(20) DEFAULT NULL COMMENT '门店代码',
  `cashier_no` varchar(255) DEFAULT NULL COMMENT '虚拟收银机号',
  `consum_type` varchar(100) DEFAULT NULL COMMENT '消费方式(目前只有o2o和线上商城)',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
  `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_user` datetime DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;


# 删除商户门店表 虚拟收银机号
ALTER TABLE supplier_store  DROP cashier_no;
# 增加账户关联 文件通用存储id
ALTER TABLE account ADD file_universal_storage_id bigint(20)  COMMENT '关联文件通用存储id';
