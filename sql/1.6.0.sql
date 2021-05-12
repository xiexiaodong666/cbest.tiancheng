INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (110, 'Merchant.merCreditType', 'wholesaleCreditLimit', '设置批发采购信用额度', 1, 0, 5);

INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (111, 'AccountDepositApply.applyType', 'wholesaleCreditLimitApply', '批发采购充值', 1, 0, 1);

INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (112, 'AccountDepositApply.applyType', 'welfareApply', '福利充值', 1, 0, 2);

INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (113, 'WholesaleCooperationMode', 'joint_venture', '联营', 1, 0, 1);

INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (114, 'WholesaleCooperationMode', 'distribution', '经销', 1, 0, 2);


ALTER TABLE account_deposit_apply ADD COLUMN apply_type VARCHAR(20) DEFAULT NULL;

UPDATE account_deposit_apply set apply_type = 'welfareApply';

alter table month_settle change  uppdate_user  update_user varchar(20) comment '更新人';


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wholesale_payable_settle
-- ----------------------------
DROP TABLE IF EXISTS `wholesale_payable_settle`;
CREATE TABLE `wholesale_payable_settle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `settle_no` varchar(50) DEFAULT NULL COMMENT '账单编号',
  `settle_month` varchar(20) DEFAULT NULL COMMENT '账单月',
  `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
  `trans_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `settle_amount` decimal(10,2) DEFAULT NULL COMMENT '结算金额',
  `settle_self_amount` decimal(10,2) DEFAULT NULL COMMENT '结算的自费额度',
  `rebate_amount` decimal(10,2) DEFAULT NULL COMMENT '返利金额',
  `order_num` int(11) DEFAULT NULL COMMENT '交易笔数',
  `rec_status` varchar(20) DEFAULT NULL COMMENT '对账状态（待确认-unconfirmed；已确认-confirmed）',
  `settle_status` varchar(20) DEFAULT NULL COMMENT '结算状态（待结算-unsettled；已结算-settled）',
  `send_status` varchar(20) DEFAULT NULL COMMENT '发送状态（待发送-unsended；已发送-sended）',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '确定时间',
  `settle_start_time` date DEFAULT NULL COMMENT '账单开始时间',
  `settle_end_time` datetime DEFAULT NULL COMMENT '账单结束时间',
  `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
  `settle_statistics_info` text COMMENT '账单账户类型统计信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=383 DEFAULT CHARSET=utf8mb4 COMMENT='批发应付结算账单';

SET FOREIGN_KEY_CHECKS = 1;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wholesale_payable_settle_detail
-- ----------------------------
DROP TABLE IF EXISTS `wholesale_payable_settle_detail`;
CREATE TABLE `wholesale_payable_settle_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `settle_no` varchar(50) DEFAULT NULL COMMENT '账单编号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单编码',
  `trans_no` varchar(50) DEFAULT NULL COMMENT '交易流水号',
  `account_code` int(10) DEFAULT NULL COMMENT '账户',
  `account_name` varchar(20) DEFAULT NULL COMMENT '账户名称',
  `card_id` int(11) DEFAULT NULL COMMENT '卡号',
  `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
  `mer_name` varchar(50) DEFAULT NULL COMMENT '商户名称',
  `store_code` varchar(20) DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(50) DEFAULT NULL COMMENT '门店名称',
  `trans_time` datetime DEFAULT NULL COMMENT '交易时间',
  `pos` varchar(20) DEFAULT NULL COMMENT 'pos机器编码',
  `pay_code` varchar(20) DEFAULT NULL COMMENT '支付编码',
  `pay_name` varchar(50) DEFAULT NULL COMMENT '支付名称',
  `trans_type` varchar(20) DEFAULT NULL COMMENT '交易类型',
  `trans_type_name` varchar(20) DEFAULT NULL COMMENT '交易类型名',
  `trans_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `mer_account_type` varchar(32) DEFAULT NULL COMMENT '福利类型(餐费、交通费等)',
  `mer_account_type_name` varchar(20) DEFAULT NULL COMMENT '福利类型(餐费、交通费等)',
  `account_amount` decimal(10,2) DEFAULT NULL COMMENT '子账户扣款金额',
  `account_balance` decimal(10,2) DEFAULT NULL COMMENT '子账户余额',
  `mer_deduction_amount` decimal(20,2) DEFAULT NULL COMMENT '商户余额扣款金额',
  `mer_credit_deduction_amount` decimal(10,2) DEFAULT NULL COMMENT '商户信用扣款金额',
  `self_deduction_amount` decimal(10,2) DEFAULT NULL COMMENT '自费扣款金额',
  `mer_wholesale_credit_deduction_amount` decimal(10,2) DEFAULT NULL COMMENT '商户批发信用扣款金额',
  `data_type` varchar(20) DEFAULT NULL COMMENT '数据支付类型 welfare-员工卡支付 third-其它三方支付',
  `settle_flag` varchar(20) DEFAULT NULL COMMENT '结算标志 settled已结算 unsettled未结算',
  `order_channel` varchar(20) DEFAULT NULL COMMENT '订单渠道',
  `payment_channel` varchar(20) DEFAULT NULL COMMENT '支付渠道',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
  `rebate_amount` decimal(15,4) DEFAULT NULL COMMENT '返点标志 unrebated未返点 rebated 已返点',
  `mer_credit` decimal(10,2) DEFAULT NULL COMMENT '商户授信额度',
  `mer_balance` decimal(10,2) DEFAULT NULL COMMENT '商户余额',
  `mer_wholesale_credit` decimal(10,2) DEFAULT NULL COMMENT '商户剩余批发采购额度',
  `order_wholesale_amount` decimal(10,2) DEFAULT NULL COMMENT '结算金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22223 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wholesale_receivable_settle
-- ----------------------------
DROP TABLE IF EXISTS `wholesale_receivable_settle`;
CREATE TABLE `wholesale_receivable_settle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `settle_no` varchar(50) DEFAULT NULL COMMENT '账单编号',
  `settle_month` varchar(20) DEFAULT NULL COMMENT '账单月',
  `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
  `trans_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `settle_amount` decimal(10,2) DEFAULT NULL COMMENT '结算金额',
  `settle_self_amount` decimal(10,2) DEFAULT NULL COMMENT '结算的自费额度',
  `rebate_amount` decimal(10,2) DEFAULT NULL COMMENT '返利金额',
  `order_num` int(11) DEFAULT NULL COMMENT '交易笔数',
  `rec_status` varchar(20) DEFAULT NULL COMMENT '对账状态（待确认-unconfirmed；已确认-confirmed）',
  `settle_status` varchar(20) DEFAULT NULL COMMENT '结算状态（待结算-unsettled；已结算-settled）',
  `send_status` varchar(20) DEFAULT NULL COMMENT '发送状态（待发送-unsended；已发送-sended）',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '确定时间',
  `settle_start_time` date DEFAULT NULL COMMENT '账单开始时间',
  `settle_end_time` datetime DEFAULT NULL COMMENT '账单结束时间',
  `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
  `settle_statistics_info` text COMMENT '账单账户类型统计信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=383 DEFAULT CHARSET=utf8mb4 COMMENT='批发应收结算账单';

SET FOREIGN_KEY_CHECKS = 1;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wholesale_receivable_settle_detail
-- ----------------------------
DROP TABLE IF EXISTS `wholesale_receivable_settle_detail`;
CREATE TABLE `wholesale_receivable_settle_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `settle_no` varchar(50) DEFAULT NULL COMMENT '账单编号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单编码',
  `trans_no` varchar(50) DEFAULT NULL COMMENT '交易流水号',
  `account_code` int(10) DEFAULT NULL COMMENT '账户',
  `account_name` varchar(20) DEFAULT NULL COMMENT '账户名称',
  `card_id` int(11) DEFAULT NULL COMMENT '卡号',
  `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
  `mer_name` varchar(50) DEFAULT NULL COMMENT '商户名称',
  `store_code` varchar(20) DEFAULT NULL COMMENT '门店编码',
  `store_name` varchar(50) DEFAULT NULL COMMENT '门店名称',
  `trans_time` datetime DEFAULT NULL COMMENT '交易时间',
  `pos` varchar(20) DEFAULT NULL COMMENT 'pos机器编码',
  `pay_code` varchar(20) DEFAULT NULL COMMENT '支付编码',
  `pay_name` varchar(50) DEFAULT NULL COMMENT '支付名称',
  `trans_type` varchar(20) DEFAULT NULL COMMENT '交易类型',
  `trans_type_name` varchar(20) DEFAULT NULL COMMENT '交易类型名',
  `trans_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `mer_account_type` varchar(32) DEFAULT NULL COMMENT '福利类型(餐费、交通费等)',
  `mer_account_type_name` varchar(20) DEFAULT NULL COMMENT '福利类型(餐费、交通费等)',
  `account_amount` decimal(10,2) DEFAULT NULL COMMENT '子账户扣款金额',
  `account_balance` decimal(10,2) DEFAULT NULL COMMENT '子账户余额',
  `mer_deduction_amount` decimal(20,2) DEFAULT NULL COMMENT '商户余额扣款金额',
  `mer_credit_deduction_amount` decimal(10,2) DEFAULT NULL COMMENT '商户信用扣款金额',
  `self_deduction_amount` decimal(10,2) DEFAULT NULL COMMENT '自费扣款金额',
  `data_type` varchar(20) DEFAULT NULL COMMENT '数据支付类型 welfare-员工卡支付 third-其它三方支付',
  `settle_flag` varchar(20) DEFAULT NULL COMMENT '结算标志 settled已结算 unsettled未结算',
  `order_channel` varchar(20) DEFAULT NULL COMMENT '订单渠道',
  `payment_channel` varchar(20) DEFAULT NULL COMMENT '支付渠道',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `version` int(11) DEFAULT NULL COMMENT '版本',
  `rebate_amount` decimal(15,4) DEFAULT NULL COMMENT '返点标志 unrebated未返点 rebated 已返点',
  `mer_credit` decimal(10,2) DEFAULT NULL COMMENT '商户授信额度',
  `mer_balance` decimal(10,2) DEFAULT NULL COMMENT '商户余额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22223 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
CREATE TABLE `order_info_detail` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'pk',
                                     `order_id` varchar(20) DEFAULT NULL COMMENT '订单id',
                                     `product_id` varchar(50) DEFAULT NULL COMMENT '商品id',
                                     `uuid` varchar(36) DEFAULT NULL COMMENT '行uuid',
                                     `sku_id` varchar(50) DEFAULT NULL COMMENT '商品skuid',
                                     `sku_no` varchar(50) DEFAULT NULL COMMENT '商品skuNo',
                                     `sku_name` varchar(50) DEFAULT NULL COMMENT '商品sku名称',
                                     `count` int(8) DEFAULT NULL COMMENT '商品数量',
                                     `refund_count` int(8) DEFAULT NULL COMMENT '售后数量',
                                     `wholesale_price` decimal(10,2) DEFAULT NULL COMMENT '商品结算单价',
                                     `wholesale_amount` decimal(15,2) DEFAULT NULL COMMENT '商品结算总金额',
                                     `wholesale_tax_rate` decimal(8,4) DEFAULT NULL COMMENT '税率',
                                     `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                     `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志',
                                     `version` bigint(20) DEFAULT '0' COMMENT '版本',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table account_bill_detail add column order_no varchar(50) comment '订单号' after trans_no;
alter table account_deduction_detail add column order_no varchar(50) comment '订单号' after trans_no;
alter table account_deduction_detail add column mer_deduction_wholesale_credit_amount decimal(10,2) comment '商户批发额度扣减额度' after self_deduction_amount;
alter table merchant_bill_detail add column wholesale_limit decimal(10,2) comment  '批发最高额度' after remaining_limit;
alter table merchant_bill_detail add column wholesale_remaining_limit decimal(10,2) comment '批发剩余额度' after remaining_limit;
alter table order_info add column order_wholesale_amount decimal(10,2) comment '订单批发结算金额' after order_amount;
