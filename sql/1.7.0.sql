# ---- DDL
CREATE TABLE `account_amount_type_group` (
                                     `id` bigint(20) NOT NULL COMMENT '主键',
                                     `group_code` varchar(20) NOT NULL COMMENT '账户组编码',
                                     `mer_account_type_code` varchar(20) NOT NULL COMMENT '福利类型',
                                     `balance` decimal(15,2) DEFAULT '0.00' COMMENT '余额',
                                     `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                     `deleted` bigint(20) DEFAULT NULL COMMENT '删除标志',
                                     `version` int(11) DEFAULT NULL COMMENT '版本',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户组';

CREATE TABLE `merchant_extend` (
                                   `id` bigint(20) NOT NULL COMMENT '主键',
                                   `mer_code` varchar(20) NOT NULL COMMENT '商户编码',
                                   `industry_tag` varchar(255)  DEFAULT NULL COMMENT '行业标签',
                                   `point_mall` tinyint(1) DEFAULT NULL COMMENT '积分商城是否开启',
                                   `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                   `version` int(11) DEFAULT NULL COMMENT '版本',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商户扩展表';

CREATE TABLE `mer_account_type_consume_scene_config` (
                                                         `id` bigint(20) NOT NULL COMMENT 'id',
                                                         `mer_code` bigint(20) NOT NULL COMMENT '商户编码',
                                                         `mer_account_type_code` varchar(20) NOT NULL COMMENT '福利类型',
                                                         `store_code` varchar(20) NOT NULL COMMENT '门店编码',
                                                         `scene_consume_type` varchar(255) NOT NULL COMMENT '消费方式',
                                                         `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
                                                         `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                         `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
                                                         `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                         `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志  1-删除、0-未删除',
                                                         `version` int(11) DEFAULT NULL COMMENT '版本',
                                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='福利类型消费门店场景配置';

alter table account_bill_detail add column account_amount_type_group_id bigint(20) comment '福利类型组id' after payment_channel;
alter table account_deduction_detail add column account_amount_type_group_id bigint(20) comment '福利类型组id' after payment_channel;

alter table account_amount_type add column joined_group tinyint(1) comment '是否加入组' after account_balance;
alter table account_amount_type add column account_amount_type_group_id bigint(20) comment '家庭id' after account_balance;


# DML ---

INSERT INTO `sequence`(`id`, `sequence_type`, `prefix`, `sequence_no`, `min_sequence`, `max_sequence`, `handler_for_max`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`) VALUES (20, 'account_amount_type_group_code', NULL, 1000000000, 1000000000, 9999999999, 'com.welfare.service.sequence.CommonMaxHandler', 'anonymous', '2021-03-08 16:59:23', 'zxadmin', '2021-03-16 10:44:35', 0, 0);