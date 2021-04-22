ALTER TABLE payment_channel_config modify deleted BIGINT(20);

UPDATE payment_channel_config set deleted = id  where deleted = 1

alter table payment_channel_config add unique index uk_mercode_storecode_consumetype_payment_channelcode_deleted(mer_code,store_code,consume_type,payment_channel_code,deleted);