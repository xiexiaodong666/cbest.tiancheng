# 条码加盐表添加索引
create index idx_bs_valid_period on barcode_salt(valid_period);
create index idx_bs_valid_period_numeric on barcode_salt(valid_period_numeric);

# 流水中记录条码信息或者磁卡信息
alter table account_bill_detail add column payment_type varchar(20) comment '支付方式:条码或者线上' after order_channel;
alter table account_bill_detail add column payment_type_info varchar(64) comment '支付方式信息:条码或磁卡信息' after payment_type;