
ALTER TABLE `order_info` ADD INDEX idx_abe_trans_no ( `trans_no` );

ALTER TABLE `account_bill_detail` ADD INDEX idx_abe_trans_amount ( `trans_amount` );
