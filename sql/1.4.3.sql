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
                                          `store_type` varchar(20) NOT NULL COMMENT '门店类型(自营:self,第三方:third)',
                                          `account_deduction_amount_id` bigint(20) NOT NULL COMMENT '用户交易流水明细表主键',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

create index idx_esd_trans_no on employee_settle_detail(trans_no);
create index idx_esd_trans_time on employee_settle_detail(trans_time);
create index idx_esd_settle_no on employee_settle_detail(settle_no);
create unique index uk_account_deduction_amount_id on employee_settle_detail(account_deduction_amount_id);


CREATE TABLE `employee_settle` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                   `settle_no` varchar(50) DEFAULT NULL COMMENT '账单编号',
                                   `settle_period` varchar(50) DEFAULT NULL COMMENT '账单周期',
                                   `mer_code` varchar(20) DEFAULT NULL COMMENT '商户代码',
                                   `account_code` bigint(20) COMMENT '账户号',
                                   `trans_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
                                   `settle_amount` decimal(10,2) DEFAULT NULL COMMENT '结算金额',
                                   `order_num` int(11) DEFAULT NULL COMMENT '交易笔数',
                                   `settle_status` varchar(20) DEFAULT NULL COMMENT '结算状态（结算中-settling；已结算-settled）',
                                   `send_time` datetime DEFAULT NULL COMMENT '发送时间',
                                   `confirm_time` datetime DEFAULT NULL COMMENT '确定时间',
                                   `settle_start_time` datetime DEFAULT NULL COMMENT '账单开始时间',
                                   `settle_end_time` datetime DEFAULT NULL COMMENT '账单结束时间',
                                   `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `uppdate_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                   `build_time` datetime DEFAULT NULL COMMENT '生成时间',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=255 DEFAULT CHARSET=utf8mb4 COMMENT='商户员工结算账单';

create index idx_es_settle_no on employee_settle(settle_no);
create index idx_build_time on employee_settle(build_time);

alter table account_amount_type modify mer_account_type_code varchar(32) null comment '商家账户类型';
alter table account_deduction_detail modify mer_account_type varchar(32) null comment '子账户类型(例如餐费、交通费等)';
alter table account_deposit_apply modify mer_account_type_code varchar(32) null comment '商家账户类型';
alter table merchant_account_type modify mer_account_type_code varchar(32) not null comment '商户账户类型编码';
alter table settle_detail modify mer_account_type varchar(32) null comment '福利类型(餐费、交通费等)';
alter table employee_settle_detail modify mer_account_type varchar(32) null comment '福利类型(个人授信，个人授信溢缴款)';
alter table account_bill_detail add column surplus_quota_overpay decimal(10,2) comment '个人授信溢缴款' after surplus_quota;
alter table account add column surplus_quota_overpay decimal(10,2) DEFAULT 0.00 comment '个人授信溢缴款' after surplus_quota;
alter table supplier_store add mobile varchar(32) null comment '门店手机号';

### DML

insert into account_amount_type (id, account_code, mer_account_type_code, account_balance, deleted, create_user, create_time, update_user, update_time, version)
select floor( 10000000 + rand() * (99999999 - 10000000)), aat.account_code, 'surplus_quota_overpay', 0, 0, 'anonymous', now(), null, null, 0
from account_amount_type aat where aat.mer_account_type_code = 'surplus_quota';

insert into merchant_account_type (id, mer_code, mer_account_type_code, mer_account_type_name, deduction_order, deleted, remark, create_user, create_time, update_user, update_time, version, show_status)
select floor( 10000000 + rand() * (99999999 - 10000000)), mat.mer_code, 'surplus_quota_overpay', '员工授信额度溢缴额', 9000, 0, null, 'anonymous', now(), null, null, 0, 0
from merchant_account_type mat where mat.mer_account_type_code = 'surplus_quota';

#新增字段赋初始值
update account a set a.surplus_quota_overpay = 0;

#新增字段赋初始值
update account_bill_detail a set a.surplus_quota_overpay = 0;