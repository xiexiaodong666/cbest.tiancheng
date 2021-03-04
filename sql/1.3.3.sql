INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('76', 'MerDeductionType', 'balance', '余额', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('77', 'MerDeductionType', 'credit', '授信余额', '1', '0', '2');

INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('78', 'SettleFlag', 'unsettle', '待结算', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('79', 'SettleFlag', 'settling', '结算中', '1', '0', '2');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('80', 'SettleFlag', 'settled', '已结算', '1', '0', '3');

#两张表添加order_channel
alter table account_deduction_detail add column order_channel varchar(20) comment '订单渠道' after chanel;
alter table settle_detail add column order_channel varchar(20) comment '订单渠道' after settle_flag;
#上面两个新加字段的历史数据补充
update account_deduction_detail d set d.order_channel = (select order_channel from account_bill_detail abd where abd.trans_no = d.trans_no limit 1);
update settle_detail d set d.order_channel = (select order_channel from account_bill_detail abd where abd.trans_no = d.trans_no limit 1);

#新增字典类型 批发收货地址 批发商城
INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (81, 'MerchantAddress.addressType', 'WHOLESALE', '批发收货地址', 1, 0, 3);
INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (82, 'SupplierStore.consumType', 'WHOLESALE', '批发商城', 1, 0, 4);
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('83', 'SettleConsumeType', 'online', '线上消费', '1', '0', '1');
INSERT INTO `dict` (`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('84', 'SettleConsumeType', 'offline', '线下消费', '1', '0', '2');

#填充商户结算明细的订单号
update settle_detail d set d.order_id = (select order_id from order_info oi where oi.trans_no = d.trans_no limit 1);