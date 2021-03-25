# DDL
CREATE TABLE `payment_channel_config` (
                                          `id` bigint(20) NOT NULL COMMENT 'pk',
                                          `mer_code` varchar(20) NOT NULL COMMENT '商户编码',
                                          `store_code` varchar(20) NOT NULL COMMENT '门店编码',
                                          `consume_type` varchar(20) NOT NULL COMMENT '消费场景',
                                          `payment_channel_code` varchar(20) NOT NULL COMMENT '支付渠道',
                                          `payment_channel_name` varchar(20) DEFAULT NULL COMMENT '支付渠道名称',
                                          `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                          `version` int(11) DEFAULT NULL COMMENT '版本',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table sub_account add column password_free_signature varchar(100) comment '免密支付签名' after balance;


# DML
update third_party_payment_request t set t.payment_request_type = 'wo_life';

