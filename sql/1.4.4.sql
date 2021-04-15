CREATE TABLE `message_push_config` (
                                       `id` bigint(20) NOT NULL,
                                       `config_code` varchar(20) NOT NULL COMMENT '配置编码',
                                       `config_name` varchar(50) NOT NULL COMMENT '配置名称',
                                       `mer_code` varchar(20) NOT NULL COMMENT '商户名称',
                                       `target_type` varchar(20) NOT NULL COMMENT '消息发送类型',
                                       `template_type` varchar(20) NOT NULL COMMENT '模板类型',
                                       `template_content` varchar(512) NOT NULL COMMENT '模板内容',
                                       `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                       `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `version` int(11) DEFAULT NULL COMMENT '版本',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `message_push_config_contact` (
                                               `id` bigint(20) NOT NULL,
                                               `message_push_config_id` bigint(20) NOT NULL COMMENT '关联配置id',
                                               `config_code` varchar(20) NOT NULL COMMENT '配置编码',
                                               `config_name` varchar(50) NOT NULL COMMENT '配置名称',
                                               `mer_code` varchar(20) NOT NULL COMMENT '所属商户',
                                               `contact_person` varchar(20) NOT NULL COMMENT '联系人姓名',
                                               `contact` varchar(100) NOT NULL COMMENT '联系方式（手机号）',
                                               `push_time` varchar(100) NOT NULL COMMENT '推送时间(例:12:10)',
                                               `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
                                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                               `update_user` varchar(20) DEFAULT NULL COMMENT '更新人',
                                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                               `deleted` tinyint(1) DEFAULT NULL COMMENT '删除标志',
                                               `version` int(11) DEFAULT NULL COMMENT '版本',
                                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table account add column offline_lock int(2) DEFAULT 1 comment '离线是否可用';

# 初始化离线短信模板
INSERT INTO `message_push_config`(`id`, `config_code`, `config_name`, `mer_code`, `target_type`, `template_type`, `template_content`, `deleted`, `create_user`, `create_time`, `update_user`, `update_time`, `version`)
SELECT m.id -1000000000000000000 ,CONCAT('TEMPLATE_', m.mer_code), '默认模板', m.mer_code, 'sms', 'string_replacer', '今日挂起订单#todayOrderCount#笔，交易额#todayOrderAmount#元，累计挂起订单#totalOrderCount#笔，交易额#totalOrderAmount#元，请登录甜橙生活查看并处理，以免影响正常交易', 0, 'duanhy', now(), NULL, NULL, 0 from merchant m where m.deleted = 0 and m.mer_identity LIKE '%PARTER%';


