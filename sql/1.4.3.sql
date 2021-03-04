CREATE TABLE `employee_settle_detail` (
                                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
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
                                          `mer_account_type` varchar(20) DEFAULT NULL COMMENT '福利类型(个人授信，个人授信溢缴款)',
                                          `mer_account_type_name` varchar(20) DEFAULT NULL COMMENT '福利类型(个人授信，个人授信溢缴款)',
                                          `account_amount` decimal(10,2) DEFAULT NULL COMMENT '子账户扣款金额',
                                          `account_balance` decimal(10,2) DEFAULT NULL COMMENT '子账户余额',
                                          `mer_deduction_amount` decimal(20,2) DEFAULT NULL COMMENT '商户余额扣款金额',
                                          `mer_credit_deduction_amount` decimal(10,2) DEFAULT NULL COMMENT '商户信用扣款金额',
                                          `settle_flag` varchar(20) DEFAULT NULL COMMENT '结算标志 settled已结算 settling结算中 unsettled未结算',
                                          `order_channel` varchar(20) DEFAULT NULL COMMENT '订单渠道',
                                          `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `version` int(11) DEFAULT NULL COMMENT '版本',
                                          `mer_credit` decimal(10,2) DEFAULT NULL COMMENT '商户授信额度',
                                          `mer_balance` decimal(10,2) DEFAULT NULL COMMENT '商户余额',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create index idx_esd_trans_no on employee_settle_detail(trans_no);
create index idx_esd_trans_time on employee_settle_detail(trans_time);
create index idx_esd_settle_no on employee_settle_detail(settle_no);

CREATE TABLE `employee_settle` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                   `settle_no` varchar(50) DEFAULT NULL COMMENT '账单编号',
                                   `settle_period` varchar(20) DEFAULT NULL COMMENT '账单周期',
                                   `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                   `account_code` bigint(20) COMMENT '账户号',
                                   `trans_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
                                   `settle_amount` decimal(10,2) DEFAULT NULL COMMENT '结算金额',
                                   `order_num` int(11) DEFAULT NULL COMMENT '交易笔数',
                                   `settle_status` varchar(20) DEFAULT NULL COMMENT '结算状态（待结算-unsettled；已结算-settled）',
                                   `send_time` datetime DEFAULT NULL COMMENT '发送时间',
                                   `confirm_time` datetime DEFAULT NULL COMMENT '确定时间',
                                   `settle_start_time` date DEFAULT NULL COMMENT '账单开始时间',
                                   `settle_end_time` datetime DEFAULT NULL COMMENT '账单结束时间',
                                   `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `uppdate_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=255 DEFAULT CHARSET=utf8mb4 COMMENT='商户员工结算账单';

create index idx_es_settle_no on employee_settle(settle_no);