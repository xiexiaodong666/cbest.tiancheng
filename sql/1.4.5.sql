# 创建子账户表
create table sub_account (
                             id bigint(20) primary key,
                             `account_code` bigint(10) DEFAULT NULL COMMENT '员工账号',
                             sub_account_type varchar(20) comment '子账户类型',
                             balance decimal(15,2) comment '余额',
                             `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                             `version` int(11) DEFAULT NULL COMMENT '版本',
                             KEY `idx_sa_account_code` (`account_code`) USING BTREE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='子账户信息';

CREATE TABLE `payment_channel` (
                                   `id` bigint(20) NOT NULL,
                                   `code` varchar(20) NOT NULL COMMENT '支出渠道编码',
                                   `name` varchar(20) NOT NULL COMMENT '支付渠道名称',
                                   `merchant_code` varchar(20) NOT NULL COMMENT '商户编码',
                                   `show_order` int(11) NOT NULL COMMENT '展示顺序',
                                   `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                   `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `version` int(11) DEFAULT NULL COMMENT '版本',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_code_mer_code` (`code`,`merchant_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付渠道商户关联表';

create table third_party_payment_request(
                                            id bigint(20) primary key comment 'pk',
                                            trans_no varchar(60) comment '交易流水号',
                                            trans_amount decimal(10,2) comment '交易金额',
                                            account_code bigint(12) comment '账号',
                                            payment_channel varchar(20) comment '支付渠道',
                                            payment_type varchar(20) comment '支付方式',
                                            payment_type_info varchar(50) comment '支付方式内容（条码或者磁卡信息）',
                                            payment_request_type varchar(20) comment '支付请求类型',
                                            payment_request text comment '支付请求',
                                            `trans_status` int(2) comment '状态',
                                            `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                            `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方支付请求';
alter table third_party_payment_request add column trans_type varchar(20) comment '账号'  after account_code;
alter table third_party_payment_request add column response text comment '返回'  after payment_request;

create index idx_tppr_trans_no on third_party_payment_request(trans_no);
create index idx_tppr_payment_type_info on third_party_payment_request(payment_type_info);


# 增加支付渠道字段
alter table account_deduction_detail add column payment_channel varchar(20) comment '支付渠道' after order_channel;
alter table account_bill_detail add column payment_channel varchar(20) comment '支付渠道' after order_channel;
alter table settle_detail add column payment_channel varchar(20) comment '支付渠道' after order_channel;


INSERT INTO `payment_channel`(`id`, `code`, `name`, `merchant_code`, `show_order`, `deleted`, `create_user`, `create_time`, `update_user`, `update_time`, `version`) VALUES (666666, 'welfare', '甜橙卡', 'default', 1, 0, 'anonymous', '2021-03-18 14:59:29', NULL, NULL, 0);

UPDATE account_bill_detail set payment_channel = 'welfare', update_time = NOW() ,version = version + 1;
UPDATE account_deduction_detail set payment_channel = 'welfare' ,update_time = NOW() ,version = version + 1;
UPDATE settle_detail set payment_channel = 'welfare' ,update_time = NOW(), version = version + 1;

INSERT INTO `sub_account`(`id`, `account_code`, `sub_account_type`, `balance`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`)
select a.id - 1000000000000000000,a.account_code,'welfare',0,'system',now(),NULL,NULL,0,0 from account a where a.deleted = 0;

ALTER TABLE account CHANGE store_code department VARCHAR(20);


