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
                                   `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                   `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `version` int(11) DEFAULT NULL COMMENT '版本',
                                   PRIMARY KEY (`id`),
                                   KEY `idx_code` (`code`) USING BTREE,
                                   KEY `idx_merchant_code` (`merchant_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='子账户信息';

# 增加支付渠道字段
alter table account_deduction_detail add column payment_channel varchar(20) comment '支付渠道' after order_channel;
alter table account_bill_detail add column payment_channel varchar(20) comment '支付渠道' after order_channel;
alter table settle_detail add column payment_channel varchar(20) comment '支付渠道' after order_channel;