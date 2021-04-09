
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
alter table order_info modify card_id varchar(32) comment '卡id';

# DML
update third_party_payment_request t set t.payment_request_type = 'wo_life';

# 初始化甜橙卡支付渠道
INSERT INTO `payment_channel_config`(`id`, `mer_code`, `store_code`, `consume_type`, `payment_channel_code`, `payment_channel_name`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`)
SELECT msr.id - 1000000000000000000,msr.mer_code,msr.store_code,'O2O','welfare', '甜橙卡', 'anonymous', now(), NULL, NULL, 0 ,0 from merchant_store_relation msr where consum_type->'$.O2O' = true and deleted = 0;

INSERT INTO `payment_channel_config`(`id`, `mer_code`, `store_code`, `consume_type`, `payment_channel_code`, `payment_channel_name`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`)
SELECT msr.id - 100000000000000000,msr.mer_code,msr.store_code,'ONLINE_MALL','welfare', '甜橙卡', 'anonymous', now(), NULL, NULL, 0 ,0 from merchant_store_relation msr where consum_type->'$.ONLINE_MALL' = true and deleted = 0;

INSERT INTO `payment_channel_config`(`id`, `mer_code`, `store_code`, `consume_type`, `payment_channel_code`, `payment_channel_name`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`)
SELECT msr.id - 10000000000000000,msr.mer_code,msr.store_code,'SHOP_CONSUMPTION','welfare', '甜橙卡', 'anonymous', now(), NULL, NULL, 0 ,0 from merchant_store_relation msr where consum_type->'$.SHOP_CONSUMPTION' = true and deleted = 0;

INSERT INTO `payment_channel_config`(`id`, `mer_code`, `store_code`, `consume_type`, `payment_channel_code`, `payment_channel_name`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`)
SELECT msr.id - 1000000000000000,msr.mer_code,msr.store_code,'WHOLESALE','welfare', '甜橙卡', 'anonymous', now(), NULL, NULL, 0 ,0 from merchant_store_relation msr where consum_type->'$.WHOLESALE' = true and deleted = 0;

INSERT INTO `payment_channel`(`id`, `code`, `name`, `merchant_code`, `show_order`, `deleted`, `create_user`, `create_time`, `update_user`, `update_time`, `version`) VALUES (263711297, 'wechat', '微信支付', 'default', 2, 0, 'anonymous', '2021-03-18 14:59:29', NULL, NULL, 5);
INSERT INTO `payment_channel`(`id`, `code`, `name`, `merchant_code`, `show_order`, `deleted`, `create_user`, `create_time`, `update_user`, `update_time`, `version`) VALUES (263711277, 'alipay', '支付宝', 'default', 3, 0, 'anonymous', '2021-03-18 14:59:29', NULL, NULL, 5);


INSERT INTO `sub_account`(`id`, `account_code`, `sub_account_type`, `balance`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`)
select a.id - 100000000000000000,a.account_code,'alipay',0,'system',now(),NULL,NULL,0,0 from account a where a.deleted = 0;

INSERT INTO `sub_account`(`id`, `account_code`, `sub_account_type`, `balance`, `create_user`, `create_time`, `update_user`, `update_time`, `deleted`, `version`)
select a.id - 10000000000000000,a.account_code,'wechat',0,'system',now(),NULL,NULL,0,0 from account a where a.deleted = 0;




