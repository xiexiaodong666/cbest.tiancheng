# DDL
create table payment_channel_config(
                                       id bigint(20) primary key comment 'pk',
                                       mer_code varchar(20) comment '商户编码',
                                       store_code varchar(20) comment '门店编码',
                                       consume_type varchar(20) comment '消费场景',
                                       payment_channel_code varchar(20) comment '支付渠道',
                                       payment_channel_name varchar(20) comment '支付渠道名称',
                                       `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `account_balance` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
                                       `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                       `version` int(11) DEFAULT NULL COMMENT '版本',
                                       UNIQUE key uk_mer_store_consume_type_payment_channel (mer_code,store_code,consume_type,payment_channel_code) using BTREE
);

alter table sub_account add column password_free_signature varchar(100) comment '免密支付签名' after balance;



# DML
update third_party_payment_request t set t.payment_request_type = 'wo_life';

