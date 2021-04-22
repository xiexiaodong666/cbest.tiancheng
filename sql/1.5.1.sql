
ALTER TABLE `account_bill_detail` ADD INDEX idx_abe_trans_amount ( `trans_amount` );

ALTER TABLE `merchant`
ADD COLUMN `bill_detail_show_store_name` varchar(20) NULL COMMENT '员工卡消费明细门店显示' AFTER `remark`;

UPDATE merchant SET bill_detail_show_store_name='1';

INSERT INTO `dict`(`dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('Merchant.billDetailShowStoreName', '1', '显示', NULL, 0, 1);
INSERT INTO `dict`(`dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES ('Merchant.billDetailShowStoreName', '0', '不显示', NULL, 0, 2);

ALTER TABLE account modify deleted BIGINT(20);

# account 新增 商户-手机号-删除标志的联合唯一索引
alter table account add unique index uk_mer_code_phone_deleted(mer_code,phone,deleted);