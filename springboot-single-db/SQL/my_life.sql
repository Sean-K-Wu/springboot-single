CREATE TABLE `sys_user` (
`id` bigint NOT NULL AUTO_INCREMENT,
`type` varchar(32) NULL,
`openid` varchar(255) NULL,
`unionid` varchar(255) NULL,
`nickname` varchar(255) NULL,
`username` varchar(100) NULL,
`password` varchar(255) NULL,
`gender` varchar(32) NULL,
`birthday` datetime NULL ON UPDATE CURRENT_TIMESTAMP,
`head_img_url` varchar(255) NULL,
`last_login_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP,
`last_password_reset_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP,
`create_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP,
`update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`) 
);
CREATE TABLE `bse_dic` (
`id` bigint NOT NULL AUTO_INCREMENT,
`name` varchar(32) NULL,
`code` varchar(255) NULL,
`status` bit NULL,
`weight` int NULL,
`descrip` varchar(255) NULL,
`parent_id` bigint NULL,
PRIMARY KEY (`id`) 
);
