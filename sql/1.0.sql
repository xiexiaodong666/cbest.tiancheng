/*
Navicat MySQL Data Transfer

Source Server         : prd-cbest
Source Server Version : 80016
Source Host           : db.haproxy.cbest.gam:36601
Source Database       : e_welfare

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2021-02-02 15:49:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
                           `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
                           `account_name` varchar(50) DEFAULT NULL COMMENT '员工名称',
                           `account_code` int(10) DEFAULT NULL COMMENT '员工账号',
                           `account_type_code` varchar(20) DEFAULT NULL COMMENT '员工类型编码',
                           `mer_code` varchar(20) DEFAULT NULL COMMENT '所属商户',
                           `store_code` varchar(20) DEFAULT NULL COMMENT '所属部门',
                           `account_status` int(11) DEFAULT NULL COMMENT '账号状态(1正常2禁用)',
                           `staff_status` varchar(20) DEFAULT NULL COMMENT '员工状态',
                           `binding` int(11) DEFAULT '0' COMMENT '是否绑卡(1绑定0未绑定)',
                           `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `account_balance` decimal(15,2) DEFAULT '0.00' COMMENT '账户余额',
                           `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                           `version` int(11) DEFAULT NULL COMMENT '版本',
                           `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
                           `max_quota` decimal(15,2) DEFAULT '0.00' COMMENT '最大授权额度',
                           `surplus_quota` decimal(15,2) DEFAULT '0.00' COMMENT '剩余授权额度',
                           `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                           `change_event_id` bigint(20) DEFAULT '0' COMMENT '员工账号变更记录ID',
                           `credit` tinyint(1) DEFAULT NULL COMMENT '是否授信',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_account_code` (`account_code`) USING BTREE,
                           KEY `idx_account_name` (`account_name`) USING BTREE,
                           KEY `idx_change_event_id` (`change_event_id`) USING BTREE,
                           KEY `idx_mer_code` (`mer_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户信息';

-- ----------------------------
-- Table structure for account_amount_type
-- ----------------------------
DROP TABLE IF EXISTS `account_amount_type`;
CREATE TABLE `account_amount_type` (
                                       `id` bigint(20) NOT NULL COMMENT '自增id',
                                       `account_code` int(10) DEFAULT NULL COMMENT '账户编码',
                                       `mer_account_type_code` varchar(20) DEFAULT NULL COMMENT '商家账户类型',
                                       `account_balance` decimal(11,2) DEFAULT NULL COMMENT '余额',
                                       `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                       `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `version` int(11) DEFAULT NULL COMMENT '版本',
                                       PRIMARY KEY (`id`),
                                       UNIQUE KEY `UK_account_code_mer_account_type_code` (`account_code`,`mer_account_type_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for account_bill_detail
-- ----------------------------
DROP TABLE IF EXISTS `account_bill_detail`;
CREATE TABLE `account_bill_detail` (
                                       `id` bigint(20) NOT NULL COMMENT 'id',
                                       `account_code` int(10) DEFAULT NULL COMMENT '员工账号',
                                       `card_id` varchar(20) DEFAULT NULL COMMENT '卡号',
                                       `trans_no` varchar(64) DEFAULT NULL COMMENT '交易流水号',
                                       `store_code` varchar(20) DEFAULT NULL COMMENT '消费门店',
                                       `trans_type` varchar(20) DEFAULT NULL COMMENT '交易类型(消费、退款、充值等)',
                                       `pos` varchar(20) DEFAULT NULL COMMENT 'pos标识',
                                       `channel` varchar(20) DEFAULT NULL COMMENT '充值渠道(第三方充值需要体现:支付宝或者微信)',
                                       `trans_amount` decimal(15,2) DEFAULT NULL COMMENT '交易总金额',
                                       `trans_time` datetime DEFAULT NULL COMMENT '交易时间',
                                       `account_balance` decimal(15,2) DEFAULT NULL COMMENT '账户余额',
                                       `surplus_quota` decimal(15,2) DEFAULT NULL COMMENT '授信余额',
                                       `order_channel` varchar(20) DEFAULT NULL COMMENT '订单渠道',
                                       `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                       `version` int(11) DEFAULT NULL COMMENT '版本',
                                       PRIMARY KEY (`id`),
                                       KEY `idx_abe_trans_no_trans_type` (`trans_no`,`trans_type`),
                                       KEY `idx_abe_account_code` (`account_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户交易流水明细表';

-- ----------------------------
-- Table structure for account_change_event_record
-- ----------------------------
DROP TABLE IF EXISTS `account_change_event_record`;
CREATE TABLE `account_change_event_record` (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                                               `account_code` bigint(20) DEFAULT NULL COMMENT '员工账号Code\r\n',
                                               `change_type` varchar(255) DEFAULT NULL COMMENT '变更类型',
                                               `change_value` varchar(255) DEFAULT NULL COMMENT '变更类型名称',
                                               `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7222 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for account_consume_scene
-- ----------------------------
DROP TABLE IF EXISTS `account_consume_scene`;
CREATE TABLE `account_consume_scene` (
                                         `id` bigint(20) unsigned NOT NULL COMMENT 'id',
                                         `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                         `account_type_code` varchar(20) DEFAULT NULL COMMENT '员工类型编码',
                                         `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                         `status` int(11) unsigned DEFAULT '1' COMMENT '状态(1正常 2禁用)',
                                         `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                         `version` int(11) DEFAULT NULL COMMENT '版本',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工消费场景配置';

-- ----------------------------
-- Table structure for account_consume_scene_store_relation
-- ----------------------------
DROP TABLE IF EXISTS `account_consume_scene_store_relation`;
CREATE TABLE `account_consume_scene_store_relation` (
                                                        `id` bigint(20) NOT NULL COMMENT 'id',
                                                        `account_consume_scene_id` bigint(20) DEFAULT NULL,
                                                        `store_code` varchar(20) DEFAULT NULL COMMENT '门店编码',
                                                        `scene_consum_type` varchar(40) DEFAULT NULL COMMENT '消费方式',
                                                        `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                        `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                        `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                                        `version` int(11) DEFAULT NULL COMMENT '版本',
                                                        PRIMARY KEY (`id`),
                                                        KEY `idx_account_consume_scene_id` (`account_consume_scene_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工消费场景关联门店';

-- ----------------------------
-- Table structure for account_deduction_detail
-- ----------------------------
DROP TABLE IF EXISTS `account_deduction_detail`;
CREATE TABLE `account_deduction_detail` (
                                            `id` bigint(20) NOT NULL COMMENT 'id',
                                            `account_code` int(10) DEFAULT NULL COMMENT '员工账号',
                                            `card_id` varchar(20) DEFAULT NULL COMMENT '卡号',
                                            `trans_no` varchar(64) DEFAULT NULL COMMENT '交易流水号',
                                            `related_trans_no` varchar(50) DEFAULT NULL COMMENT '关联交易单号（退款时使用）',
                                            `store_code` varchar(20) DEFAULT NULL COMMENT '消费门店',
                                            `trans_type` varchar(20) DEFAULT NULL COMMENT '交易类型(消费、退款、充值等)',
                                            `pos` varchar(20) DEFAULT NULL COMMENT 'pos标识',
                                            `chanel` varchar(20) DEFAULT NULL COMMENT '渠道(自主充值需要显示来源:支付宝、微信)',
                                            `trans_amount` decimal(18,5) DEFAULT NULL COMMENT '交易总金额',
                                            `reversed_amount` decimal(18,5) DEFAULT NULL COMMENT '已逆向金额',
                                            `trans_time` datetime DEFAULT NULL COMMENT '交易时间',
                                            `pay_code` varchar(20) DEFAULT NULL COMMENT '支付编码',
                                            `mer_account_type` varchar(20) DEFAULT NULL COMMENT '子账户类型(例如餐费、交通费等)',
                                            `account_deduction_amount` decimal(15,2) DEFAULT NULL COMMENT '子账户扣款金额',
                                            `account_amount_type_balance` decimal(15,2) DEFAULT NULL COMMENT '子账户剩余金额',
                                            `mer_deduction_amount` decimal(15,2) DEFAULT NULL COMMENT '商户余额扣款金额',
                                            `mer_deduction_credit_amount` decimal(15,2) DEFAULT NULL COMMENT '商户额度扣款金额',
                                            `self_deduction_amount` decimal(15,2) DEFAULT NULL COMMENT '自费扣款金额',
                                            `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                            `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                            `version` int(11) DEFAULT NULL COMMENT '版本',
                                            PRIMARY KEY (`id`),
                                            KEY `idx_add_store_code` (`store_code`),
                                            KEY `idx_add_trans_no` (`trans_no`),
                                            KEY `idx_add_account_code` (`account_code`),
                                            KEY `idx_add_related_transNo_transType` (`related_trans_no`,`trans_type`),
                                            KEY `idx_add_transNo_transType` (`trans_no`,`trans_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户交易流水明细表';

-- ----------------------------
-- Table structure for account_deposit_apply
-- ----------------------------
DROP TABLE IF EXISTS `account_deposit_apply`;
CREATE TABLE `account_deposit_apply` (
                                         `id` bigint(20) NOT NULL COMMENT 'id',
                                         `apply_code` varchar(32) DEFAULT NULL COMMENT '申请编码',
                                         `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                         `recharge_num` int(11) DEFAULT NULL COMMENT '充值账户个数',
                                         `recharge_amount` decimal(15,2) DEFAULT NULL COMMENT '申请充值总额',
                                         `recharge_status` varchar(20) DEFAULT NULL COMMENT '充值状态',
                                         `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                                         `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新日期',
                                         `version` int(11) DEFAULT NULL COMMENT '版本',
                                         `approval_status` varchar(20) DEFAULT NULL COMMENT '审批状态',
                                         `approval_user` varchar(50) DEFAULT NULL COMMENT '审批人',
                                         `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
                                         `approval_remark` varchar(255) DEFAULT NULL COMMENT '审批备注',
                                         `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                         `apply_remark` varchar(255) DEFAULT NULL COMMENT '申请备注',
                                         `apply_user` varchar(50) DEFAULT NULL COMMENT '申请人',
                                         `approval_opinion` varchar(255) DEFAULT NULL COMMENT '审批意见',
                                         `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
                                         `approval_type` varchar(20) DEFAULT NULL COMMENT '请求类型',
                                         `request_id` varchar(64) DEFAULT NULL COMMENT '请求id',
                                         `mer_account_type_code` varchar(20) DEFAULT NULL COMMENT '商家账户类型',
                                         `channel` varchar(20) DEFAULT NULL COMMENT '渠道',
                                         `mer_account_type_name` varchar(20) DEFAULT NULL COMMENT '商家账户类型名称',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `UK_apply_code` (`apply_code`) USING BTREE,
                                         UNIQUE KEY `UK_request_id` (`request_id`) USING BTREE,
                                         KEY `IDX_approval_time` (`approval_time`) USING BTREE,
                                         KEY `IDX_apply_time` (`apply_time`) USING BTREE,
                                         KEY `IDX_apply_user` (`apply_user`) USING BTREE,
                                         KEY `IDX_approval_user` (`approval_user`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户充值申请';

-- ----------------------------
-- Table structure for account_deposit_apply_detail
-- ----------------------------
DROP TABLE IF EXISTS `account_deposit_apply_detail`;
CREATE TABLE `account_deposit_apply_detail` (
                                                `id` bigint(20) NOT NULL COMMENT 'id',
                                                `apply_code` varchar(32) DEFAULT NULL COMMENT '申请编码',
                                                `account_code` int(10) DEFAULT NULL COMMENT '员工账户',
                                                `recharge_amount` decimal(15,2) DEFAULT NULL COMMENT '充值金额',
                                                `recharge_status` varchar(20) DEFAULT NULL COMMENT '充值状态',
                                                `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                                `update_time` datetime DEFAULT NULL COMMENT '更新日期',
                                                `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志',
                                                `version` int(11) DEFAULT NULL COMMENT '版本',
                                                `trans_no` varchar(32) DEFAULT NULL COMMENT '流水号',
                                                PRIMARY KEY (`id`),
                                                UNIQUE KEY `UK_Apply_code_Account_code` (`apply_code`,`account_code`) USING BTREE,
                                                UNIQUE KEY `UK_trans_no` (`trans_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='充值申请明细';

-- ----------------------------
-- Table structure for account_deposit_record
-- ----------------------------
DROP TABLE IF EXISTS `account_deposit_record`;
CREATE TABLE `account_deposit_record` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                          `account_code` int(10) DEFAULT NULL COMMENT '员工账号',
                                          `mer_code` varchar(20) NOT NULL COMMENT '商户代码',
                                          `pay_type` varchar(32) NOT NULL COMMENT '支付方式',
                                          `pay_trade_no` varchar(50) NOT NULL COMMENT '支付交易号',
                                          `pay_gateway_trade_no` varchar(50) DEFAULT NULL COMMENT '支付重百付交易号',
                                          `pay_channel_trade_no` varchar(50) DEFAULT NULL COMMENT '支付渠道交易号',
                                          `deposit_trade_no` varchar(50) DEFAULT NULL COMMENT '充值交易号',
                                          `deposit_gateway_trade_no` varchar(50) DEFAULT NULL COMMENT '充值重百付交易号',
                                          `deposit_amount` decimal(14,4) NOT NULL COMMENT '充值金额',
                                          `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
                                          `deposit_time` datetime DEFAULT NULL COMMENT '充值时间',
                                          `pay_status` int(11) NOT NULL COMMENT '支付状态',
                                          `recharge_status` int(11) NOT NULL COMMENT '充值状态',
                                          `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `version` int(11) DEFAULT NULL COMMENT '版本',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          KEY `idx_account_code` (`account_code`) USING BTREE,
                                          KEY `idx_pay_trade_no` (`pay_trade_no`) USING BTREE,
                                          KEY `idx_deposit_trade_no` (`deposit_trade_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1356237869223895042 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账号充值记录表';

-- ----------------------------
-- Table structure for account_type
-- ----------------------------
DROP TABLE IF EXISTS `account_type`;
CREATE TABLE `account_type` (
                                `id` bigint(20) NOT NULL COMMENT 'id',
                                `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                `type_code` varchar(20) DEFAULT NULL COMMENT '类型编码',
                                `type_name` varchar(50) DEFAULT NULL COMMENT '类型名称',
                                `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                `version` int(11) DEFAULT NULL COMMENT '版本',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工类型';

-- ----------------------------
-- Table structure for barcode_salt
-- ----------------------------
DROP TABLE IF EXISTS `barcode_salt`;
CREATE TABLE `barcode_salt` (
                                `id` bigint(20) NOT NULL COMMENT 'pk',
                                `valid_period` varchar(50) DEFAULT NULL COMMENT '有效期',
                                `valid_period_numeric` bigint(20) DEFAULT NULL COMMENT '有效期数字表示',
                                `salt_value` bigint(8) DEFAULT NULL COMMENT '加盐值',
                                `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                `version` int(11) DEFAULT NULL COMMENT '版本',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='条码加盐信息';

-- ----------------------------
-- Table structure for bus_events
-- ----------------------------
DROP TABLE IF EXISTS `bus_events`;
CREATE TABLE `bus_events` (
                              `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                              `class_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                              `event_json` mediumtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                              `user_token` varchar(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                              `created_date` datetime NOT NULL,
                              `creating_owner` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                              `processing_owner` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                              `processing_available_date` datetime DEFAULT NULL,
                              `processing_state` varchar(14) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'AVAILABLE',
                              `error_count` int(10) unsigned DEFAULT '0',
                              `search_key1` bigint(20) unsigned DEFAULT NULL,
                              `search_key2` bigint(20) unsigned DEFAULT NULL,
                              PRIMARY KEY (`record_id`),
                              UNIQUE KEY `record_id` (`record_id`),
                              KEY `idx_bus_where` (`processing_state`,`processing_owner`,`processing_available_date`),
                              KEY `bus_events_tenant_account_record_id` (`search_key2`,`search_key1`)
) ENGINE=InnoDB AUTO_INCREMENT=2634 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for bus_events_history
-- ----------------------------
DROP TABLE IF EXISTS `bus_events_history`;
CREATE TABLE `bus_events_history` (
                                      `record_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                                      `class_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                                      `event_json` mediumtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                                      `user_token` varchar(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                                      `created_date` datetime NOT NULL,
                                      `creating_owner` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                                      `processing_owner` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                                      `processing_available_date` datetime DEFAULT NULL,
                                      `processing_state` varchar(14) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT 'AVAILABLE',
                                      `error_count` int(10) unsigned DEFAULT '0',
                                      `search_key1` bigint(20) unsigned DEFAULT NULL,
                                      `search_key2` bigint(20) unsigned DEFAULT NULL,
                                      PRIMARY KEY (`record_id`),
                                      UNIQUE KEY `record_id` (`record_id`),
                                      KEY `bus_events_history_tenant_account_record_id` (`search_key2`,`search_key1`)
) ENGINE=InnoDB AUTO_INCREMENT=2633 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for card_apply
-- ----------------------------
DROP TABLE IF EXISTS `card_apply`;
CREATE TABLE `card_apply` (
                              `id` bigint(20) NOT NULL COMMENT 'id',
                              `apply_code` varchar(20) DEFAULT NULL COMMENT '制卡申请号',
                              `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                              `card_name` varchar(100) DEFAULT NULL COMMENT '卡片名称',
                              `card_type` varchar(50) DEFAULT NULL COMMENT '卡片类型',
                              `card_medium` varchar(50) DEFAULT NULL COMMENT '卡片介质',
                              `card_num` int(11) DEFAULT NULL COMMENT '卡片数量',
                              `identification_code` varchar(20) DEFAULT NULL COMMENT '识别码方法',
                              `identification_length` int(11) DEFAULT NULL COMMENT '识别码长度',
                              `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                              `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                              `status` int(11) DEFAULT NULL COMMENT '状态: 锁定、激活',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='制卡信息';

-- ----------------------------
-- Table structure for card_info
-- ----------------------------
DROP TABLE IF EXISTS `card_info`;
CREATE TABLE `card_info` (
                             `id` bigint(20) NOT NULL COMMENT 'id',
                             `apply_code` varchar(20) DEFAULT NULL COMMENT '申请编码',
                             `card_id` varchar(20) NOT NULL COMMENT '卡号',
                             `card_status` int(5) DEFAULT NULL COMMENT '卡状态',
                             `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                             `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `account_code` int(10) DEFAULT NULL COMMENT '员工账号',
                             `version` int(11) DEFAULT NULL COMMENT '版本',
                             `magnetic_stripe` varchar(64) NOT NULL COMMENT '磁条号',
                             `written_time` datetime DEFAULT NULL COMMENT '入库时间',
                             `bind_time` datetime DEFAULT NULL COMMENT '绑定时间',
                             `enabled` int(2) DEFAULT '1' COMMENT '是否启用',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uni_ci_magnetic_stripe` (`magnetic_stripe`),
                             UNIQUE KEY `uni_ci_card_id` (`card_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='卡信息';

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
                              `id` bigint(20) NOT NULL COMMENT 'id',
                              `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                              `department_name` varchar(50) DEFAULT NULL COMMENT '部门名称',
                              `department_code` varchar(20) DEFAULT NULL COMMENT '部门编码',
                              `department_parent` varchar(20) DEFAULT NULL COMMENT '部门父级',
                              `department_level` int(11) DEFAULT NULL COMMENT '部门层级',
                              `department_path` varchar(100) DEFAULT NULL COMMENT '部门路径',
                              `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                              `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `version` int(11) DEFAULT NULL COMMENT '版本',
                              `external_code` varchar(10) DEFAULT NULL COMMENT '外部编码',
                              `department_type` varchar(20) DEFAULT NULL COMMENT '部门类型',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_department_code` (`department_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户部门';

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
                        `id` bigint(20) NOT NULL COMMENT 'id',
                        `dict_type` varchar(64) DEFAULT NULL COMMENT '码表类型',
                        `dict_code` varchar(50) DEFAULT NULL COMMENT '编码',
                        `dict_name` varchar(100) DEFAULT NULL COMMENT '名称',
                        `status` int(11) DEFAULT NULL COMMENT '状态',
                        `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志',
                        `sort` int(11) DEFAULT NULL COMMENT '顺序',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_dict_type_dict_code` (`dict_type`,`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典';

-- ----------------------------
-- Table structure for file_template
-- ----------------------------
DROP TABLE IF EXISTS `file_template`;
CREATE TABLE `file_template` (
                                 `id` bigint(20) NOT NULL COMMENT 'id',
                                 `file_type` varchar(64) DEFAULT NULL COMMENT '文件类型',
                                 `url` varchar(255) DEFAULT NULL COMMENT '文件下载地址',
                                 `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志',
                                 `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `version` int(11) DEFAULT '0' COMMENT '版本',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件模板';

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                            `mer_name` varchar(100) DEFAULT NULL COMMENT '商户名称',
                            `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                            `mer_type` varchar(20) DEFAULT NULL COMMENT '商户类型',
                            `mer_identity` varchar(255) DEFAULT NULL COMMENT '身份属性',
                            `mer_cooperation_mode` varchar(255) DEFAULT NULL COMMENT '合作方式',
                            `self_recharge` varchar(255) DEFAULT NULL COMMENT '员工自主充值',
                            `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                            `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                            `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                            `update_time` datetime DEFAULT NULL COMMENT '更新日期',
                            `status` int(11) DEFAULT NULL COMMENT '状态',
                            `deleted` tinyint(1) DEFAULT NULL,
                            `version` int(11) DEFAULT NULL COMMENT '版本',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_mer_code` (`mer_code`),
                            KEY `idx_mer_name` (`mer_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1356442707773845506 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户信息';

-- ----------------------------
-- Table structure for merchant_account_type
-- ----------------------------
DROP TABLE IF EXISTS `merchant_account_type`;
CREATE TABLE `merchant_account_type` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
                                         `mer_code` varchar(20) NOT NULL COMMENT '商户代码',
                                         `mer_account_type_code` varchar(20) NOT NULL COMMENT '商户账户类型编码',
                                         `mer_account_type_name` varchar(20) NOT NULL COMMENT '商户账户类型名称',
                                         `deduction_order` int(11) DEFAULT NULL COMMENT '扣款序号',
                                         `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标识',
                                         `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                         `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         `version` int(11) DEFAULT NULL COMMENT '版本',
                                         `show_status` int(11) DEFAULT NULL COMMENT '显示状态（默认福利类型（授信额度，自主充值）不需要展示）1展示；0不展示',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `uk_mer_code_mer_account_type_code` (`mer_code`,`mer_account_type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1356442789915095042 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户福利类型';

-- ----------------------------
-- Table structure for merchant_address
-- ----------------------------
DROP TABLE IF EXISTS `merchant_address`;
CREATE TABLE `merchant_address` (
                                    `id` bigint(20) NOT NULL COMMENT 'id',
                                    `address_name` varchar(100) DEFAULT NULL COMMENT '地址名称',
                                    `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
                                    `address_type` varchar(50) DEFAULT NULL COMMENT '地址类型',
                                    `status` int(11) DEFAULT NULL COMMENT '状态',
                                    `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                                    `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新日期',
                                    `version` int(11) DEFAULT NULL COMMENT '版本',
                                    `related_type` varchar(20) DEFAULT NULL COMMENT '关联类型',
                                    `related_id` bigint(20) DEFAULT NULL COMMENT '关联id',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='地址信息';

-- ----------------------------
-- Table structure for merchant_bill_detail
-- ----------------------------
DROP TABLE IF EXISTS `merchant_bill_detail`;
CREATE TABLE `merchant_bill_detail` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                        `trans_no` varchar(64) DEFAULT NULL COMMENT '交易流水号',
                                        `trans_type` varchar(20) DEFAULT NULL COMMENT '交易类型(消费、退款、添加余额、添加额度等)',
                                        `balance_type` varchar(20) DEFAULT NULL COMMENT '余额类型(余额、可用信用额度、最大额度、充值额度)',
                                        `trans_amount` decimal(15,2) DEFAULT NULL COMMENT '交易金额',
                                        `recharge_limit` decimal(15,2) DEFAULT NULL COMMENT '充值额度',
                                        `current_balance` decimal(15,2) DEFAULT NULL COMMENT '当前余额',
                                        `credit_limit` decimal(15,2) DEFAULT NULL COMMENT '最高信用额度',
                                        `remaining_limit` decimal(15,2) DEFAULT NULL COMMENT '剩余信用额度',
                                        `rebate_limit` decimal(15,2) DEFAULT NULL COMMENT '返利额度',
                                        `self_deposit_balance` decimal(15,2) DEFAULT NULL,
                                        `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        `version` int(11) DEFAULT NULL COMMENT '版本',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_trans_no` (`trans_no`) USING BTREE,
                                        KEY `idx_mer_code` (`mer_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1356501329979498498 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for merchant_credit
-- ----------------------------
DROP TABLE IF EXISTS `merchant_credit`;
CREATE TABLE `merchant_credit` (
                                   `id` bigint(20) NOT NULL COMMENT 'id',
                                   `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                   `recharge_limit` decimal(15,2) DEFAULT NULL COMMENT '充值额度',
                                   `current_balance` decimal(15,2) DEFAULT NULL COMMENT '目前余额',
                                   `credit_limit` decimal(15,2) DEFAULT NULL COMMENT '信用额度',
                                   `remaining_limit` decimal(15,2) DEFAULT NULL COMMENT '剩余信用额度',
                                   `rebate_limit` decimal(15,2) DEFAULT NULL COMMENT '返利余额',
                                   `self_deposit_balance` decimal(15,2) DEFAULT NULL COMMENT '员工自主充值',
                                   `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                   `version` int(11) DEFAULT NULL COMMENT '版本',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户额度信';

-- ----------------------------
-- Table structure for merchant_credit_apply
-- ----------------------------
DROP TABLE IF EXISTS `merchant_credit_apply`;
CREATE TABLE `merchant_credit_apply` (
                                         `id` bigint(20) NOT NULL COMMENT 'id',
                                         `apply_code` varchar(64) DEFAULT NULL COMMENT '申请编码',
                                         `mer_code` varchar(20) DEFAULT NULL COMMENT '商户编码',
                                         `apply_type` varchar(20) DEFAULT NULL COMMENT '申请类型',
                                         `balance` decimal(15,2) DEFAULT NULL COMMENT '金额',
                                         `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                         `enclosure` varchar(255) DEFAULT NULL COMMENT '附件',
                                         `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                         `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                         `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                         `approval_status` varchar(20) DEFAULT NULL COMMENT '审批状态',
                                         `approval_user` varchar(20) DEFAULT NULL COMMENT '审批人',
                                         `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
                                         `approval_remark` varchar(255) DEFAULT NULL COMMENT '审批备注',
                                         `version` int(11) DEFAULT NULL COMMENT '版本',
                                         `apply_user` varchar(50) DEFAULT NULL COMMENT '申请人',
                                         `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
                                         `request_id` varchar(64) DEFAULT NULL COMMENT '请求id',
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `uk_apply_code` (`apply_code`) USING BTREE,
                                         KEY `idx_approval_time` (`approval_time`) USING BTREE,
                                         KEY `idx_apply_time` (`apply_time`) USING BTREE,
                                         KEY `idx_apply_user` (`apply_user`) USING BTREE,
                                         KEY `idx_approval_user` (`approval_user`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户金额申请';

-- ----------------------------
-- Table structure for merchant_credit_apply_detail
-- ----------------------------
DROP TABLE IF EXISTS `merchant_credit_apply_detail`;
CREATE TABLE `merchant_credit_apply_detail` (
                                                `id` bigint(20) NOT NULL COMMENT 'id',
                                                `apply_code` varchar(20) DEFAULT NULL COMMENT '申请编码',
                                                `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                                `type` varchar(20) DEFAULT NULL COMMENT '变动类型',
                                                `amount` decimal(10,0) DEFAULT NULL COMMENT '金额',
                                                `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                                `version` int(11) DEFAULT NULL COMMENT '版本',
                                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户明细记录';

-- ----------------------------
-- Table structure for merchant_rebate_record
-- ----------------------------
DROP TABLE IF EXISTS `merchant_rebate_record`;
CREATE TABLE `merchant_rebate_record` (
                                          `id` int(11) NOT NULL AUTO_INCREMENT,
                                          `rebate_no` int(11) DEFAULT NULL COMMENT '返点记录编号',
                                          `mer_code` varchar(50) DEFAULT NULL COMMENT '商户编号',
                                          `rebate_date` date DEFAULT NULL COMMENT '返点日期',
                                          `rebate_amount` decimal(15,2) DEFAULT NULL COMMENT '返点金额',
                                          `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `version` int(11) DEFAULT NULL COMMENT '版本',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for merchant_store_relation
-- ----------------------------
DROP TABLE IF EXISTS `merchant_store_relation`;
CREATE TABLE `merchant_store_relation` (
                                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                           `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                           `store_code` varchar(20) DEFAULT NULL COMMENT '门店编码',
                                           `consum_type` varchar(100) DEFAULT NULL COMMENT '消费方式',
                                           `store_alias` varchar(100) DEFAULT NULL COMMENT '门店别名',
                                           `is_rebate` int(11) DEFAULT NULL COMMENT '是否返利',
                                           `rebate_type` varchar(100) DEFAULT NULL COMMENT '返利类型',
                                           `rebate_ratio` decimal(5,2) DEFAULT NULL COMMENT '返利比率',
                                           `ramark` varchar(255) DEFAULT NULL COMMENT '备注',
                                           `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                           `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                           `version` int(11) DEFAULT NULL COMMENT '版本',
                                           `status` int(11) DEFAULT NULL COMMENT '状态',
                                           `sync_status` int(11) DEFAULT NULL COMMENT '同步状态',
                                           PRIMARY KEY (`id`),
                                           KEY `idx_msr_mer_code_store_code` (`mer_code`,`store_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1356442996849471507 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商户消费场景配置';

-- ----------------------------
-- Table structure for mer_deposit_apply_file
-- ----------------------------
DROP TABLE IF EXISTS `mer_deposit_apply_file`;
CREATE TABLE `mer_deposit_apply_file` (
                                          `id` bigint(20) NOT NULL,
                                          `mer_deposit_apply_code` varchar(64) NOT NULL COMMENT '申请编码',
                                          `file_url` varchar(255) DEFAULT NULL,
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for month_settle
-- ----------------------------
DROP TABLE IF EXISTS `month_settle`;
CREATE TABLE `month_settle` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `settle_no` varchar(50) DEFAULT NULL COMMENT '账单编号',
                                `settle_month` varchar(20) DEFAULT NULL COMMENT '账单月',
                                `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                `trans_amount` decimal(15,2) DEFAULT NULL COMMENT '交易金额',
                                `settle_amount` decimal(15,2) DEFAULT NULL COMMENT '结算金额',
                                `settle_self_amount` decimal(15,2) DEFAULT NULL COMMENT '结算的自费额度',
                                `rebate_amount` decimal(15,2) DEFAULT NULL COMMENT '返利金额',
                                `order_num` int(11) DEFAULT NULL COMMENT '交易笔数',
                                `rec_status` varchar(20) DEFAULT NULL COMMENT '对账状态（待确认-unconfirmed；已确认-confirmed）',
                                `settle_status` varchar(20) DEFAULT NULL COMMENT '结算状态（待结算-unsettled；已结算-settled）',
                                `send_status` varchar(20) DEFAULT NULL COMMENT '发送状态（待发送-unsended；已发送-sended）',
                                `send_time` datetime DEFAULT NULL COMMENT '发送时间',
                                `confirm_time` datetime DEFAULT NULL COMMENT '确定时间',
                                `settle_start_time` date DEFAULT NULL COMMENT '账单开始时间',
                                `settle_end_time` datetime DEFAULT NULL COMMENT '账单结束时间',
                                `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                `settle_statistics_info` text COMMENT '账单账户类型统计信息',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='月度结算账单';

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `order_id` varchar(20) DEFAULT NULL COMMENT '订单号',
                              `trans_no` varchar(50) DEFAULT NULL COMMENT '交易流水号',
                              `return_trans_no` varchar(50) DEFAULT NULL COMMENT '退款流水号',
                              `goods` longtext COMMENT '商品',
                              `merchant_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                              `merchant_name` varchar(50) DEFAULT NULL COMMENT '商户名称',
                              `store_code` varchar(20) DEFAULT NULL COMMENT '门店编码',
                              `store_name` varchar(50) DEFAULT NULL COMMENT '门店名称',
                              `account_code` int(10) DEFAULT NULL COMMENT '账户',
                              `account_name` varchar(50) DEFAULT NULL COMMENT '账户名称',
                              `account_mer_code` varchar(20) DEFAULT NULL COMMENT '账户所属商户编码',
                              `trans_type` varchar(20) DEFAULT NULL COMMENT '交易类型',
                              `trans_type_name` varchar(20) DEFAULT NULL COMMENT '交易类型名称',
                              `card_id` int(11) DEFAULT NULL COMMENT '卡号',
                              `order_amount` decimal(12,2) DEFAULT NULL COMMENT '订单金额',
                              `order_time` datetime DEFAULT NULL COMMENT '订单时间',
                              `pay_code` varchar(50) DEFAULT NULL COMMENT '支付编码',
                              `pay_name` varchar(50) DEFAULT NULL COMMENT '支付名称',
                              `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_order_id` (`order_id`) COMMENT '订单id唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=162622 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for order_trans_relation
-- ----------------------------
DROP TABLE IF EXISTS `order_trans_relation`;
CREATE TABLE `order_trans_relation` (
                                        `id` bigint(20) NOT NULL,
                                        `order_id` varchar(20) DEFAULT NULL COMMENT '订单号',
                                        `trans_no` varchar(20) DEFAULT NULL COMMENT '交易号',
                                        `type` varchar(20) DEFAULT NULL COMMENT '类型(充值订单还是消费订单)',
                                        `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        `version` int(11) DEFAULT NULL COMMENT '版本',
                                        `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标记',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for product_info
-- ----------------------------
DROP TABLE IF EXISTS `product_info`;
CREATE TABLE `product_info` (
                                `product_code` varchar(20) NOT NULL COMMENT '商品编码',
                                `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
                                `update_time` datetime DEFAULT NULL,
                                PRIMARY KEY (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for pull_account_detail_record
-- ----------------------------
DROP TABLE IF EXISTS `pull_account_detail_record`;
CREATE TABLE `pull_account_detail_record` (
                                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                              `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                              `del_date` varchar(20) DEFAULT NULL COMMENT '处理日期',
                                              `del_status` varchar(20) DEFAULT NULL COMMENT '处理状态 success-成功 fail-失败',
                                              `try_count` int(10) DEFAULT NULL COMMENT '重试次数',
                                              `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                              `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=816 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户交易明细拉取记录表';

-- ----------------------------
-- Table structure for sequence
-- ----------------------------
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence` (
                            `id` bigint(20) NOT NULL COMMENT 'pk',
                            `sequence_type` varchar(30) DEFAULT NULL COMMENT '序列类型',
                            `prefix` varchar(20) DEFAULT NULL,
                            `sequence_no` bigint(20) DEFAULT NULL COMMENT '序列号',
                            `min_sequence` bigint(20) DEFAULT NULL,
                            `max_sequence` bigint(20) DEFAULT NULL,
                            `handler_for_max` varchar(100) DEFAULT NULL,
                            `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                            `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                            `update_time` datetime DEFAULT NULL COMMENT '更新日期',
                            `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标记',
                            `version` int(11) DEFAULT NULL COMMENT '版本',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `UK_Sequence_type_Prefix` (`sequence_type`,`prefix`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for settle_detail
-- ----------------------------
DROP TABLE IF EXISTS `settle_detail`;
CREATE TABLE `settle_detail` (
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
                                 `trans_amount` decimal(15,2) DEFAULT NULL COMMENT '交易金额',
                                 `mer_account_type` varchar(20) DEFAULT NULL COMMENT '福利类型(餐费、交通费等)',
                                 `mer_account_type_name` varchar(20) DEFAULT NULL COMMENT '福利类型(餐费、交通费等)',
                                 `account_amount` decimal(15,2) DEFAULT NULL COMMENT '子账户扣款金额',
                                 `account_balance` decimal(15,2) DEFAULT NULL COMMENT '子账户余额',
                                 `mer_deduction_amount` decimal(20,2) DEFAULT NULL COMMENT '商户余额扣款金额',
                                 `mer_credit_deduction_amount` decimal(15,2) DEFAULT NULL COMMENT '商户信用扣款金额',
                                 `self_deduction_amount` decimal(15,2) DEFAULT NULL COMMENT '自费扣款金额',
                                 `data_type` varchar(20) DEFAULT NULL COMMENT '数据支付类型 welfare-员工卡支付 third-其它三方支付',
                                 `settle_flag` varchar(20) DEFAULT NULL COMMENT '结算标志 settled已结算 unsettled未结算',
                                 `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `version` int(11) DEFAULT NULL COMMENT '版本',
                                 `rebate_amount` decimal(15,4) DEFAULT NULL COMMENT '返点标志 unrebated未返点 rebated 已返点',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_trans_time` (`trans_time`) USING BTREE,
                                 KEY `idx_mer_code` (`mer_code`),
                                 KEY `idx_trans_no` (`trans_no`) USING BTREE,
                                 KEY `idx_settle_flag` (`settle_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=16707 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for supplier_store
-- ----------------------------
DROP TABLE IF EXISTS `supplier_store`;
CREATE TABLE `supplier_store` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                  `store_code` varchar(20) DEFAULT NULL COMMENT '门店代码',
                                  `store_name` varchar(100) DEFAULT NULL COMMENT '门店名称',
                                  `store_level` int(11) DEFAULT NULL COMMENT '门店层级',
                                  `store_parent` varchar(20) DEFAULT NULL COMMENT '父级门店',
                                  `store_path` varchar(50) DEFAULT NULL COMMENT '门店路径',
                                  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                                  `consum_type` varchar(100) DEFAULT NULL COMMENT '消费方式',
                                  `status` int(11) DEFAULT NULL COMMENT '状态',
                                  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志',
                                  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
                                  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
                                  `external_code` varchar(20) DEFAULT NULL COMMENT '外部编码',
                                  `version` int(11) DEFAULT NULL COMMENT '版本',
                                  `cashier_no` varchar(255) DEFAULT NULL COMMENT '虚拟收银机号',
                                  `sync_status` int(11) DEFAULT NULL COMMENT '门店同步到商城状态',
                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uk_store_code` (`store_code`) USING BTREE,
                                  UNIQUE KEY `uni_ss_store_code_cashier_no` (`store_code`,`cashier_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1356251253482819587 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应商门店';

-- ----------------------------
-- Table structure for temp_account_deposit_apply
-- ----------------------------
DROP TABLE IF EXISTS `temp_account_deposit_apply`;
CREATE TABLE `temp_account_deposit_apply` (
                                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                              `file_id` varchar(64) NOT NULL,
                                              `phone` varchar(32) NOT NULL,
                                              `recharge_amount` decimal(15,2) DEFAULT NULL,
                                              `request_id` varchar(64) NOT NULL,
                                              `account_code` varchar(20) NOT NULL,
                                              PRIMARY KEY (`id`),
                                              KEY `idx_file_id` (`file_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1355830298071756808 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- data
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('1', 'ApprovalStatus', 'AUDIT_SUCCESS', '通过', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('2', 'ApprovalStatus', 'AUDIT_FAILED', '不通过', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('3', 'ApprovalStatus', 'AUDITING', '待审核', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('4', 'Merchant.merCreditType', 'rechargeLimit', '添加充值额度', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('5', 'Merchant.merCreditType', 'currentBalance', '添加余额', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('6', 'Merchant.merCreditType', 'creditLimit', '设置信用额度', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('7', 'Merchant.merCreditType', 'rebateLimit', '消耗返点', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('8', 'Pos.TradeMode', '0', '自定价格', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('9', 'Pos.TradeMode', '1', '固定价格', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('10', 'Pos.TradeMode', '2', '动态价格', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('11', 'Pos.OnlineState', '0', '离线', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('12', 'Pos.OnlineState', '1', '在线', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('13', 'Pos.OnlineState', '2', '活跃', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('14', 'Pos.status', '0', '初始', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('15', 'Pos.status', '1', '可用', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('16', 'Pos.status', '-1', '禁用', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('17', 'Pos.updateRange', '0', '全部更新', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('18', 'Pos.updateRange', '1', '门店更新', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('19', 'Pos.updateRange', '2', '终端更新', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('20', 'Pos.forced', '1', '强制', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('21', 'Pos.forced', '0', '非强制', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('22', 'Pos.available', '1', '有效', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('23', 'Pos.available', '0', '无效', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('24', 'Pos.type', '1', '海信手持pos机', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('25', 'Pos.type', '2', '德卡双屏收银机', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('26', 'Pos.type', '3', '海信自助收银机\n', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('27', 'MerchantStoreRelation.status', '1', '启用', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('28', 'MerchantStoreRelation.status', '0', '禁用', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('29', 'CardApply.type', 'STORED_VALUE_CARD', '储值卡', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('34', 'CardApply.medium', 'IC_CARD', 'ic卡', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('39', 'CardApply.status', '1', '启用', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('40', 'CardApply.status', '0', '禁用', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('41', 'CardInfo.status', '0', '新增', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('42', 'CardInfo.status', '1', '已写入', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('43', 'CardInfo.status', '2', '已绑定', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('44', 'MerchantStoreRelation.rebate.type', 'EMPLOYEE_CARD_NUMBER', '员工卡号支付', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('45', 'MerchantStoreRelation.rebate.type', 'OTHER_PAY', '其他支付方式', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('46', 'shoppingPlatformUser.status', '0', '锁定', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('47', 'shoppingPlatformUser.status', '1', '正常', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('48', 'shoppingPlatformUser.status', '2', '删除', '1', '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('49', 'Merchant.merIdentity', 'PARTER', '客户', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('50', 'Merchant.merIdentity', 'SUPPLIER', '供应商', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('51', 'Merchant.merCooperationMode', 'PAY_FIRST', '先付费', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('52', 'Merchant.merCooperationMode', 'PAYED', '后付费', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('53', 'Merchant.merType', 'HEADQUARTERS', '总部', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('54', 'Merchant.merType', 'BRANCH_HEADQUARTERS', '分区总部', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('55', 'Merchant.selfRecharge', '1', '是', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('56', 'Merchant.selfRecharge', '0', '否', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('57', 'Department.departmentType', 'DISTRIBUTION_CENTER', '配送中心', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('58', 'Department.departmentType', 'DEPARTMENT', '部门', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('59', 'Department.departmentType', 'GROUP', '小组', NULL, '0', '3');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('60', 'SupplierStore.consumType', 'O2O', 'O2O', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('61', 'SupplierStore.consumType', 'ONLINE_MALL', '线上商城', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('62', 'SupplierStore.consumType', 'SHOP_CONSUMPTION', '到店消费', NULL, '0', '3');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('63', 'SupplierStore.status', '1', '激活', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('64', 'SupplierStore.status', '0', '未激活', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('65', 'MerchantAddress.addressType', 'NORMAL', '普通', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('66', 'MerchantAddress.addressType', 'MAIL_BOX', '快递柜', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('67', 'CardApply.type', 'DISCOUNT_STORED_VALUE_CARD', '折扣储值卡', NULL, '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('68', 'Pos.printable', '0', '不打印', NULL, '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('69', 'Pos.printable', '1', '打印', NULL, '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('73', 'CardInfo.enabled', '1', '启用', NULL, '0', NULL);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('74', 'CardInfo.enabled', '0', '禁用', NULL, '0', NULL);



INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1', 'merchant_credit_apply', 'MCP', '10', '1', '99999999999999', 'com.welfare.service.sequence.CommonMaxHandler', NULL, '2021-01-20 21:54:08', 'admin', '2021-01-26 10:06:04', '0', '46');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('2', 'deposit', NULL, '10', '1', '9999999', NULL, NULL, '2021-01-20 21:54:08', '15111989630', '2021-01-26 10:24:14', '0', '74');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('4', 'account_type_code', NULL, '10', '1', '9999999', NULL, NULL, '2021-01-20 21:54:08', '15111989650', '2021-01-26 15:19:48', '0', '32');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('5', 'account_code', NULL, '1000000000', '1000000000', '99999999999999', NULL, NULL, '2021-01-20 21:54:08', 'chenyx', '2021-01-26 14:45:26', '0', '3728');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('7', 'account_deposit_apply', 'ADP', '10', '1', '99999999999999', 'com.welfare.service.sequence.CommonMaxHandler', NULL, '2021-01-20 21:54:08', '15111989630', '2021-01-26 10:24:14', '0', '37');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('8', 'mer_code', 'M', '100', '100', '999', 'com.welfare.service.sequence.SinglePrefixAddHandler', NULL, '2021-01-21 09:09:10', 'anonymous', '2021-01-26 14:03:19', '0', '38');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('9', 'department_code', NULL, '10000000', '10000000', '99999999', 'com.welfare.service.sequence.CommonMaxHandler', NULL, '2021-01-21 09:09:10', 'anonymous', '2021-01-26 14:43:34', '0', '43');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('10', 'mer_account_type', NULL, '1000', '1000', '999999', 'com.welfare.service.sequence.CommonMaxHandler', NULL, '2021-01-21 09:09:10', 'anonymous', '2021-01-22 13:41:32', '0', '26');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352072534357741570', 'CARDID', 'M103', '100000000', NULL, NULL, NULL, 'admin', '2021-01-21 09:56:27', 'admin', '2021-01-21 15:50:00', '0', '1');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352077971132026882', 'CARDID', 'M110', '100000000', NULL, NULL, NULL, 'admin', '2021-01-21 10:18:03', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352083443503403010', 'CARDID', 'M108', '100000000', NULL, NULL, NULL, 'admin', '2021-01-21 10:39:48', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352083498482339841', 'CARDID', 'M109', '100000000', NULL, NULL, NULL, 'admin', '2021-01-21 10:40:01', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352151464133582849', 'CARDID', 'M106', '100000000', NULL, NULL, NULL, 'admin', '2021-01-21 15:10:05', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352161284890832898', 'CARDID', 'M112', '100000000', NULL, NULL, NULL, 'admin', '2021-01-21 15:49:07', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352444082721714178', 'CARDID', 'M115', '100000000', NULL, NULL, NULL, 'admin', '2021-01-22 10:32:51', 'admin', '2021-01-26 15:08:06', '0', '7');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352456294546862081', 'CARDID', 'M114', '100000000', NULL, NULL, NULL, 'admin', '2021-01-22 11:21:23', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352519065401573378', 'mer_account_type', 'M107', '10000', NULL, NULL, NULL, 'min.wu', '2021-01-22 15:30:48', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352520100908122113', 'mer_account_type', 'M118', '10000', NULL, NULL, NULL, 'anonymous', '2021-01-22 15:34:55', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352523051470962689', 'mer_account_type', 'M116', '10001', NULL, NULL, NULL, 'anonymous', '2021-01-22 15:46:39', 'anonymous', '2021-01-22 15:47:02', '0', '1');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352523959420338178', 'mer_account_type', 'M119', '10001', NULL, NULL, NULL, 'anonymous', '2021-01-22 15:50:15', 'anonymous', '2021-01-22 15:50:38', '0', '1');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352529493162737666', 'mer_account_type', 'M101', '10001', NULL, NULL, NULL, 'anonymous', '2021-01-22 16:12:15', 'anonymous', '2021-01-22 16:12:26', '0', '2');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1352876582698770434', 'mer_account_type', 'M126', '10001', NULL, NULL, NULL, 'anonymous', '2021-01-23 15:11:27', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1353537223080083458', 'CARDID', 'M124', '100000001', NULL, NULL, NULL, 'admin', '2021-01-25 10:56:36', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1353578535120601089', 'mer_account_type', 'M134', '10001', NULL, NULL, NULL, 'anonymous', '2021-01-25 13:40:46', 'anonymous', '2021-01-25 13:40:46', '0', '2');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1353583386579763202', 'mer_account_type', 'M133', '10002', NULL, NULL, NULL, 'anonymous', '2021-01-25 14:00:02', 'anonymous', '2021-01-25 14:00:16', '0', '1');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1353586815540723714', 'CARDID', 'M132', '100000002', NULL, NULL, NULL, 'admin', '2021-01-25 14:13:40', NULL, NULL, '0', '0');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1353594203186462721', 'CARDID', 'M134', '100000004', NULL, NULL, NULL, 'admin', '2021-01-25 14:43:01', 'admin', '2021-01-25 14:47:27', '0', '1');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1353608754095259650', 'mer_account_type', 'M132', '10002', NULL, NULL, NULL, 'anonymous', '2021-01-25 15:40:50', 'anonymous', '2021-01-25 15:40:51', '0', '1');
INSERT INTO `sequence` (`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES ('1353907634263293954', 'CARDID', 'M136', '100000002', NULL, NULL, NULL, 'admin', '2021-01-26 11:28:29', NULL, NULL, '0', '0');
