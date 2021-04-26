INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (110, 'Merchant.merCreditType', 'wholesaleCreditLimit', '设置批发采购信用额度', 1, 0, 5);

INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (111, 'AccountDepositApply.applyType', 'wholesaleCreditLimitApply', '批发采购充值', 1, 0, 1);

INSERT INTO `dict`(`id`, `dict_type`, `dict_code`, `dict_name`, `status`, `deleted`, `sort`) VALUES (112, 'AccountDepositApply.applyType', 'welfareApply', '福利充值', 1, 0, 2);

ALTER TABLE account_deposit_apply ADD COLUMN apply_type VARCHAR(20) DEFAULT NULL;

UPDATE account_deposit_apply set apply_type = 'welfareApply'

alter table month_settle change  uppdate_user  update_user varchar(20) comment '更新人';