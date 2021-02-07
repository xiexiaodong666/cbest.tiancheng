# 条码加盐表添加索引
create index idx_bs_valid_period on barcode_salt(valid_period);
create index idx_bs_valid_period_numeric on barcode_salt(valid_period_numeric);

# 流水中记录条码信息或者磁卡信息
alter table account_bill_detail add column payment_type varchar(20) comment '支付方式:条码或者线上' after order_channel;
alter table account_bill_detail add column payment_type_info varchar(64) comment '支付方式信息:条码或磁卡信息' after payment_type;


alter table order_info add column time_interval INT(4) comment '就餐时段: 1-早餐,2-午餐,3-晚餐,4-宵夜';

# 删除订单表中order_id唯一约束
alter table order_info drop index uk_order_id;