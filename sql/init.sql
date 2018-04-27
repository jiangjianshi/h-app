CREATE DATABASE `db_huizhaofang_platform` DEFAULT CHARACTER SET utf8;
USE db_huizhaofang_platform;

-- 平台用户表
CREATE TABLE `t_platform_customer` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_app_id` VARCHAR(255) NOT NULL COMMENT '用户appId，平台唯一标识',
  `f_source` VARCHAR(255) NOT NULL COMMENT '平台用户标识',
  `f_secret_key` VARCHAR(255) NOT NULL COMMENT '平台用户用于创建签名和加密数据的秘钥',
  `f_permission_ip` VARCHAR(255) NOT NULL COMMENT '授权ip或者授权ip段',
  `f_permission_begin_date` DATE NOT NULL COMMENT '授权开始时间',
  `f_permission_end_date` DATE NOT NULL COMMENT '授权结束时间',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '创建时间',
  `f_last_change_date` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`f_app_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '平台用户表';

-- 平台用户可访问的api列表
CREATE TABLE `t_permission_list` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_app_id` VARCHAR(255) NOT NULL COMMENT '用户appId，平台唯一标识',
  `f_api_id` INT(11) NOT NULL COMMENT 't_api_list中的id字段',
  `f_times` INT(11) NOT NULL COMMENT '每天可以访问api的次数',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '创建时间',
  `f_last_change_date` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '平台用户可访问的api列表';

-- 平台API列表
CREATE TABLE `t_api_list` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_uri` VARCHAR(255) NOT NULL COMMENT 'api的uri地址',
  `f_versions` VARCHAR(255) NOT NULL COMMENT '该api在平台的版本及以后版本有效',
  `f_last_versions` VARCHAR(255) NOT NULL COMMENT '该api最后支持的平台版本号',	
  `f_comments` VARCHAR(1024) NOT NULL COMMENT 'api说明',	
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '创建时间',
  `f_last_change_date` DATE NOT NULL DEFAULT '0000-00-00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '平台API列表';

-- 房源基础信息表
CREATE TABLE `t_house_base` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_house_sell_id` VARCHAR(255) NOT NULL COMMENT '房源销售编号',
  `f_is_sale` INT(11) NOT NULL DEFAULT '0' COMMENT '0:未上架；1：上架',
  `f_status` INT(11) NOT NULL DEFAULT '0' COMMENT '房源状态',
  `f_ext400` VARCHAR(32) COMMENT '400分机号',	
  `f_house_comment` VARCHAR(1024) NOT NULL COMMENT '房源描述',
  `f_can_checkin_date` DATETIME NOT NULL COMMENT '可入住时间',
  `f_rent_price_month` INT(11) NOT NULL COMMENT '月租金',
  `f_rent_price_day` INT(11) NOT NULL COMMENT '日租金',
  `f_service_fee` INT(11) NOT NULL COMMENT '服务费或中介费',
  `f_deposit_fee` INT(11) NOT NULL COMMENT '押金',
  `f_deposit_month` INT(11) NOT NULL COMMENT '押金押几个月',
  `f_period_month` INT(11) NOT NULL COMMENT '每次付几个月的租金',
  `f_company_id` int(11) NOT NULL DEFAULT '0' COMMENT '公司id',
  `f_company_name` varchar(30) DEFAULT NULL COMMENT '公司名称',
  `f_agency_id` INT(11) NOT NULL DEFAULT '0' COMMENT '经纪人id',
  `f_agency_phone` VARCHAR(16) DEFAULT NULL COMMENT '经纪人电话',
  `f_agency_name` varchar(50) DEFAULT NULL COMMENT '经纪人姓名',
  `f_agency_introduce` varchar(255) DEFAULT NULL COMMENT '经纪人介绍',
  `f_agency_gender` tinyint(2) NOT NULL DEFAULT '0' COMMENT '经纪人性别',
  `f_agency_avatar` varchar(255) DEFAULT NULL COMMENT '经纪人头像',
  `f_approved_id` INT(11) NOT NULL DEFAULT '0' COMMENT '审核房源的人员id',
  `f_first_pub_date` DATETIME COMMENT '第一次上架时间',
  `f_pub_date` DATETIME COMMENT '上架时间',
  `f_has_key` INT(11) NOT NULL COMMENT '是否有钥匙；1:有；0:无',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATETIME NOT NULL COMMENT '创建时间',
  `f_last_change_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`f_house_sell_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '房源基础信息表';


-- 房源详细信息表
CREATE TABLE `t_house_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_house_sell_id` VARCHAR(255) NOT NULL COMMENT '房源销售编号',
  `f_building_no` VARCHAR(255)  COMMENT '楼栋编号',
  `f_unit_no` VARCHAR(255)  COMMENT '单元号',
  `f_flow_no` VARCHAR(255)  COMMENT '所在楼层',
  `f_flow_total` VARCHAR(255)  COMMENT '总楼层',
  `f_house_no` VARCHAR(255)  COMMENT '门牌号',
  `f_area` FLOAT(6,2)  COMMENT '建筑面积',
  `f_orientations` INT(11)  COMMENT '朝向',
  `f_bedroom_nums` INT(11)  COMMENT '卧室数量',
  `f_livingroom_nums` INT(11)  COMMENT '起居室数量',
  `f_kitchen_nums` INT(11)  COMMENT '厨房数量',
  `f_toilet_nums` INT(11)  COMMENT '卫生间数量',
  `f_province` VARCHAR(255)  COMMENT '省份',
  `f_city` VARCHAR(255)  COMMENT '市',
  `f_house_function` INT(11)  COMMENT '房源用途',
  `f_district` VARCHAR(255)  COMMENT '行政区',
  `f_bizname` VARCHAR(255)  COMMENT '商圈',
  `f_address` VARCHAR(255)  COMMENT '详细地址',
  `f_subway` VARCHAR(255)  COMMENT '最近地铁站',
  `f_subway_distance` VARCHAR(255)  COMMENT '到最近地铁站距离',
  `f_bus_stations` VARCHAR(255)  COMMENT '附近公交站',
  `f_surround` VARCHAR(255)  COMMENT '周边',
  `f_city_id` int(11) DEFAULT NULL COMMENT '城市id',
  `f_district_id` int(11) DEFAULT NULL COMMENT '行政区id',
  `f_biz_id` int(11) DEFAULT NULL COMMENT '商圈id',
  `f_subway_line_id` varchar(255) DEFAULT NULL COMMENT '地铁线路id',
  `f_subway_station_id` int(11) DEFAULT NULL COMMENT '地铁站id',
  `f_community_name` VARCHAR(255)  COMMENT '小区名称',
  `f_building_name` VARCHAR(255)  COMMENT '楼栋名称',
  `f_balcony_nums` INT(11)  COMMENT '阳台数量',
  `f_baidu_lo` VARCHAR(30)  COMMENT '百度坐标，经度',
  `f_baidu_la` VARCHAR(30)  COMMENT '百度坐标，纬度',
  `f_building_type` INT(11)  COMMENT '建筑类型',
  `f_building_year` INT(4)  COMMENT '建筑时间',
  `f_toilet` INT(11) COMMENT '独立卫生间',
  `f_balcony` INT(11) COMMENT '独立阳台',
  `f_insurance` INT(11) COMMENT '家财险',
  `f_decoration` INT(11)  COMMENT '装修档次',
  `f_entire_rent` INT(11)  COMMENT '租住类型',
  `f_comment` VARCHAR(1024)  COMMENT '房源描述',
  `f_house_tag` VARCHAR(1024)  COMMENT '房屋标签，客户自定义标签',
  `f_source` VARCHAR(255)  COMMENT '来源',
  `f_run` INT(11)  COMMENT '0:表示数据未经采集；1：表示数据已经采集过',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATETIME NOT NULL COMMENT '创建时间',
  `f_last_change_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '房源详细信息表';

-- 房源配置信息表
CREATE TABLE `t_house_setting` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_house_sell_id` VARCHAR(255) NOT NULL COMMENT '房源销售编号',
  `f_room_id` INT(11) NOT NULL COMMENT '房间id',
  `f_setting_code` INT(11) NOT NULL COMMENT '配置物品id',
  `f_setting_nums` INT(11) NOT NULL COMMENT '配置物品数量',
  `f_setting_position` INT(11) NOT NULL COMMENT '配置物品位置，1：卧室；2公共区域',
  `f_category_type` INT(11) NOT NULL COMMENT '配置类型',
  `f_setting_name` VARCHAR(255) NOT NULL COMMENT '配置名称',
  `f_setting_price` INT(11) NOT NULL COMMENT '配置价格',
  `f_setting_cost` INT(11) NOT NULL COMMENT '配置成本',
  `f_is_completed` INT(11) NOT NULL COMMENT '是否已经配置完成',
  `f_completed_date` DATETIME COMMENT '配置完成时间',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATETIME NOT NULL COMMENT '创建时间',
  `f_last_change_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '房源配置信息表';

-- 房源图片表
CREATE TABLE `t_house_pics` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_house_sell_id` VARCHAR(255) NOT NULL COMMENT '房源销售编号',
  `f_room_id` INT(11) NOT NULL COMMENT '房间id',
  `f_pic_root_path` VARCHAR(255) NOT NULL COMMENT '图片服务器位置',
  `f_pic_web_path` VARCHAR(255) NOT NULL COMMENT '图片网络路径',
  `f_pic_type` INT(11) NOT NULL COMMENT '图片类型',
  `f_is_default` INT(11) NOT NULL COMMENT '是否为首图，1：首图；0：非首图',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATETIME NOT NULL COMMENT '创建时间',
  `f_last_change_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '房源图片表';

-- 房间基础信息表
CREATE TABLE `t_room_base` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_house_sell_id` VARCHAR(255) NOT NULL COMMENT '房源销售编号',
  `f_status` INT(11) NOT NULL COMMENT '房间状态',
  `f_area` FLOAT(6,2) NOT NULL COMMENT '面积',
  `f_room_comment` VARCHAR(1024) COMMENT '房间描述',
  `f_room_type` INT(11) COMMENT '房间类型',
  `f_room_use` INT(11) COMMENT '房间用途类型',
  `f_orientations` INT(11) COMMENT '朝向',
  `f_can_checkin_date` DATETIME NOT NULL COMMENT '可入住时间',
  `f_rent_price_month` INT(11) NOT NULL COMMENT '月租金',
  `f_rent_price_day` INT(11) NOT NULL COMMENT '日租金',
  `f_service_fee` INT(11) NOT NULL COMMENT '服务费或中介费',
  `f_deposit_fee` INT(11) NOT NULL COMMENT '押金',
  `f_deposit_month` INT(11) NOT NULL COMMENT '押金押几个月',
  `f_period_month` INT(11) NOT NULL COMMENT '每次付几个月的租金',
  `f_approved_id` INT(11) COMMENT '审核房源的人员id',
  `f_decoration` INT(11)  COMMENT '装修档次',
  `f_toilet` INT(11) COMMENT '独立卫生间',
  `f_balcony` INT(11) COMMENT '独立阳台',
  `f_insurance` INT(11) COMMENT '家财险',
  `f_comment` VARCHAR(1024)  COMMENT '房间描述',
  `f_room_code` VARCHAR(255)  COMMENT '房间代号',
  `f_room_tag` VARCHAR(255)  COMMENT '房间标签',
  `f_production_name` VARCHAR(255)  COMMENT '产品名称',
  `f_room_name` VARCHAR(255)  COMMENT '房间名称',
  `f_first_pub_date` DATETIME COMMENT '第一次上架时间',
  `f_pub_date` DATETIME COMMENT '上架时间',
  `f_has_key` INT(11) NOT NULL COMMENT '是否有钥匙；1:有；0:无',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATETIME NOT NULL COMMENT '创建时间',
  `f_last_change_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '房间基础信息表';


-- 房屋或房间的投诉信息表
CREATE TABLE `t_house_complaint` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_house_sell_id` VARCHAR(255) NOT NULL COMMENT '房源销售编号',
  `f_room_id` INT(11) NOT NULL COMMENT '房间id',
  `f_uid` VARCHAR(255) NOT NULL COMMENT '用户id',
  `f_complaint` INT(11) NOT NULL COMMENT '投诉原因',
  `f_comment` VARCHAR(1024) COMMENT '投诉详细内容',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATETIME NOT NULL COMMENT '创建时间',
  `f_last_change_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '房屋或房间的投诉信息表';

-- 公寓表
CREATE TABLE `t_apartment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_city_id` int(11) DEFAULT NULL COMMENT '城市id',
  `f_apartment_name` VARCHAR(255) NOT NULL COMMENT '公寓名称',
  `f_pic_root_path` VARCHAR(255) NOT NULL COMMENT '图片服务器位置',
  `f_pic_web_path` VARCHAR(255) NOT NULL COMMENT '图片网络路径',
  `f_status` int(11) NOT NULL COMMENT '公寓状态',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATETIME NOT NULL COMMENT '创建时间',
  `f_last_change_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '公寓表';

-- 推荐房源表
CREATE TABLE `t_house_recommend` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_city_id` int(11) NOT NULL COMMENT '城市id',
  `f_house_sell_id` VARCHAR(255) NOT NULL COMMENT '房源销售编号',
  `f_room_id` INT(11) NOT NULL COMMENT '房间id',
  `f_pic_root_path` VARCHAR(255) NOT NULL COMMENT '图片服务器位置',
  `f_pic_web_path` VARCHAR(255) NOT NULL COMMENT '图片网络路径',
  `f_is_delete` INT(11) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` DATETIME NOT NULL COMMENT '创建时间',
  `f_last_change_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT '推荐房源表';

-- 省表
CREATE TABLE `provinces` (
  `provinces_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '省ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名字',
  PRIMARY KEY (`provinces_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='省';

-- 城市表
CREATE TABLE `city` (
  `city_id` int(11) NOT NULL AUTO_INCREMENT,
  `provinces_id` int(11) DEFAULT NULL COMMENT '省ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名字',
  `sort` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`city_id`),
  KEY `FK_Relationship_1` (`provinces_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='城市';

-- 行政区表
CREATE TABLE `district` (
  `district_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `city_id` int(11) DEFAULT NULL COMMENT '城市ID',
  `provinces_id` int(11) NOT NULL COMMENT '省ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名字',
  `sort` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`district_id`,`provinces_id`),
  KEY `FK_Relationship_2` (`city_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='行政区';


-- 商圈表
CREATE TABLE `area` (
  `area_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `district_id` int(10) unsigned DEFAULT NULL COMMENT '行政区ID',
  `name` varchar(60) NOT NULL,
  `sort` int(10) unsigned DEFAULT NULL,
  `status` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`area_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='商圈';

-- 地铁表
CREATE TABLE `subway` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `city_id` int(11) NOT NULL COMMENT '城市ID',
  `subway_station_id` tinyint(3) unsigned NOT NULL COMMENT '车站ID(用于每条线路排序)',
  `station` varchar(50) NOT NULL COMMENT '车站',
  `subway_line` varchar(20) NOT NULL COMMENT '线路',
  `subway_line_id` tinyint(3) unsigned NOT NULL COMMENT '线路ID(用于线路排序)',
  `open_status` tinyint(2) NOT NULL COMMENT '是否开通',
  `latitude` varchar(30) DEFAULT NULL COMMENT '纬度',
  `longitude` varchar(30) DEFAULT NULL COMMENT '经度',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`subway_station_id`,`subway_line_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='地铁';

-- 位置版本表
CREATE TABLE `t_city_info_version` (
  `f_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `f_city_id` bigint(11) NOT NULL COMMENT '城市ID',
  `f_info_type` tinyint(4) NOT NULL COMMENT '1:地铁信息;2:商圈信息',
  `f_version` bigint(11) NOT NULL COMMENT '版本号',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='位置版本表';

-- 浏览房源足迹表
CREATE TABLE `t_footmark_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `f_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `f_sell_id` varchar(20) NOT NULL DEFAULT '' COMMENT '房源销售编号',
  `f_room_id` int(11) NOT NULL DEFAULT '0' COMMENT '房间ID',
  `f_create_time` datetime NOT NULL COMMENT '创建时间',
  `f_update_time` datetime NOT NULL COMMENT '更新时间',
  `f_is_rent` int(1) NOT NULL DEFAULT '0' COMMENT '是否出租标识：0：默认未出租；1：已出租',
  `f_state` int(1) NOT NULL DEFAULT '1' COMMENT '有效标识：1：默认有效；0：失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='浏览房源足迹表';

-- 用户订制数据表
CREATE TABLE `t_order_custom` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `f_user_id` int(11) NOT NULL COMMENT '用户ID',
  `f_location` varchar(50) NOT NULL COMMENT '地理位置',
  `f_min_price` bigint(20) NOT NULL COMMENT '房间价格(最小区间)',
  `f_max_price` bigint(20) NOT NULL COMMENT '房间价格(最大区间)',
  `f_check_in_time` date NOT NULL COMMENT '入住时间',
  `f_phone` varchar(11) NOT NULL COMMENT '手机号',
  `f_create_time` datetime NOT NULL COMMENT '创建时间',
  `f_update_time` datetime NOT NULL COMMENT '更新时间',
  `f_state` int(1) NOT NULL DEFAULT '1' COMMENT '有效标识：1：默认有效；0：失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户订制数据表';

--用户收藏数据表
CREATE TABLE `t_house_collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `f_user_id` int(11) NOT NULL COMMENT '用户ID',
  `f_sell_id` varchar(20) NOT NULL COMMENT '房源销售编号',
  `f_room_id` int(11) NOT NULL COMMENT '房间ID',
  `f_is_rent` int(1) DEFAULT '0' COMMENT '是否已出租0：默认未出租；1：已出租',
  `f_create_time` datetime NOT NULL COMMENT '创建时间',
  `f_update_time` datetime NOT NULL COMMENT '更新时间',
  `f_state` int(1) NOT NULL DEFAULT '0' COMMENT '有效标识：0：默认有效；1：失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户收藏数据表';

--轮播图维护表
CREATE TABLE `t_slide_show_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `f_image_url` varchar(255) NOT NULL COMMENT '轮播图图片URL',
  `f_destination_url` varchar(255) NOT NULL COMMENT '跳转链接',
  `f_image_type` int(1) NOT NULL DEFAULT '0' COMMENT '图片类型（0：默认未分类；1：会找房轮播图；2：会分期轮播图）',
  `f_create_time` datetime NOT NULL COMMENT '创建时间',
  `f_update_time` datetime NOT NULL COMMENT '更新时间',
  `f_state` int(1) NOT NULL DEFAULT '1' COMMENT '有效标识：1：默认有效；0：失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='轮播图维护表';

--品牌公寓活动表
CREATE TABLE `t_agency_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `f_agency_id` varchar(50) NOT NULL COMMENT '中介公司ID',
  `f_page_name` varchar(50) NOT NULL COMMENT '活动页面名称',
  `f_activity_name` varchar(50) NOT NULL COMMENT '活动名称',
  `f_city_id` int(11) NOT NULL COMMENT '活动城市ID',
  `f_agency_name` varchar(50) NOT NULL COMMENT '中介公司名称',
  `f_agency_desc` varchar(255) NOT NULL COMMENT '中介公司介绍',
  `f_activity_time` datetime NOT NULL COMMENT '活动时间',
  `f_img_url1` varchar(255) DEFAULT NULL COMMENT '活动图片的URL',
  `f_img_url2` varchar(255) DEFAULT NULL COMMENT '图片的url',
  `f_img_url3` varchar(255) DEFAULT NULL COMMENT '图片的url',
  `f_img_url4` varchar(255) DEFAULT NULL COMMENT '图片的url',
  `f_img_url5` varchar(255) DEFAULT NULL COMMENT '图片的url',
  `f_create_time` datetime NOT NULL COMMENT '创建时间',
  `f_update_time` datetime NOT NULL COMMENT '更新时间',
  `f_state` int(1) NOT NULL DEFAULT '1' COMMENT '有效标识：1：默认有效；0：失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='品牌公寓活动表';

--小区坐标库
 CREATE TABLE `t_community_base` (                                                                                                                                 
                    `f_id` int(32) NOT NULL AUTO_INCREMENT,                                                                                                                         
                    `f_city_id` int(10) DEFAULT NULL COMMENT '城市ID',                                                                                                            
                    `f_city_name` varchar(20) DEFAULT NULL COMMENT '城市名称',                                                                                                  
                    `f_district_id` int(10) DEFAULT NULL COMMENT '行政区ID',                                                                                                     
                    `f_district_name` varchar(20) DEFAULT NULL COMMENT '行政区名称',                                                                                           
                    `f_biz_id` int(10) DEFAULT NULL COMMENT '商圈ID',                                                                                                             
                    `f_biz_name` varchar(20) DEFAULT NULL COMMENT '商圈名称',                                                                                                   
                    `f_address` varchar(50) DEFAULT NULL,                                                                                                                           
                    `f_community_name` varchar(50) DEFAULT NULL COMMENT '小区名称',                                                                                             
                    `f_center` varchar(100) DEFAULT NULL COMMENT '坐标中心点',                                                                                                 
                    `f_precise` int(4) DEFAULT '0' COMMENT '位置的附加信息，是否精确查找。1为精确查找，即准确打点；0为不精确，即模糊打点。',  
                    `f_confidence` int(4) DEFAULT '0' COMMENT '可信度，描述打点准确度',                                                                                  
                    `f_level` varchar(100) DEFAULT NULL COMMENT '地址类型',                                                                                                     
                    `f_flag` int(4) DEFAULT '0' COMMENT '收集状态 0待收集 1已收集',                                                                                       
                    `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',                                                
                    `f_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',                                                                        
                    PRIMARY KEY (`f_id`)                                                                                                                                            
                  ) ENGINE=InnoDB AUTO_INCREMENT=767 DEFAULT CHARSET=utf8
 
 --小区库初始化脚本
insert into t_community_base  (f_community_name,f_address,f_city_name,f_city_id,f_district_name,f_district_id,f_biz_name,f_biz_id)  
select f_community_name,f_address,f_city,f_city_id,f_district,f_district_id,f_bizname,f_biz_id  from t_house_detail where f_city_id=1101 and f_community_name not in (
select f_community_name from t_community_base where f_city_id=1101
) group by f_community_name


create table f_statistic_report
(
	f_id bigint auto_increment
		primary key,
	f_city_id int(10) unsigned not null comment '城市id',
	f_ftol int(10) null comment '页面访问数量',
	f_fpv int(10) unsigned null comment 'pv访问数量',
	f_fss int(10) unsigned null comment '搜索框点击量',
	f_fbdj int(10) unsigned null comment 'banner点击量',
	f_fjg int(10) unsigned null comment '金刚点击量',
	f_fppgy int(10) unsigned null comment '品牌公寓点击量',
	f_fhz int(10) unsigned null comment '合租点击量',
	f_fzz int(10) unsigned null comment '整租点击量',
	f_ffq int(10) unsigned null comment '分期点击量',
	f_time varchar(10) not null comment '日期字符串',
	f_state int null comment '状态点击量',
	f_create_time datetime null comment '创建时间',
	f_update_time datetime null comment '修改时间'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='监控日报统计';


整租合租数据合并视图创建
create view hzfhouses as
(SELECT
     concat_ws('_', `t`.`id`, `t`.`f_house_sell_id`, 1)                   AS `ID`,
     `t`.`id`                                                             AS `f_ID`,
     '0'                                                                  AS `f_room_id`,
     `t`.`f_house_sell_id`                                                AS `f_house_sell_id`,
     `t`.`f_status`                                                       AS `f_status`,
     ifnull(`hd`.`f_area`, 0.0)                                           AS `f_area`,
     ifnull(`hd`.`f_area`, 0.0)                                           AS `f_house_area`,
     ''                                                                   AS `f_room_type`,
     ''                                                                   AS `f_rt_name`,
     ''                                                                   AS `f_room_use`,
     ''                                                                   AS `f_ru_name`,
     `hd`.`f_orientations`                                                AS `f_orientations`,
     (CASE `hd`.`f_orientations`
      WHEN '10001'
        THEN '东'
      WHEN '10002'
        THEN '西'
      WHEN '10003'
        THEN '南'
      WHEN '10004'
        THEN '北'
      WHEN '10023'
        THEN '西南'
      WHEN '10024'
        THEN '西北'
      WHEN '10014'
        THEN '东北'
      WHEN '10013'
        THEN '东南'
      WHEN '10034'
        THEN '南北'
      WHEN '10012'
        THEN '东西'
      ELSE '其他' END)                                                      AS `ori_name`,
     `t`.`f_can_checkin_date`                                             AS `f_can_checkin_date`,
     ifnull(`t`.`f_rent_price_month`, 0.0)                                AS `f_rent_price_month`,
     (CASE `t`.`f_rent_price_month`
      WHEN 0.0
        THEN 0
      ELSE 1 END)                                                         AS `f_rent_price_zero`,
     0                                                                    AS `f_rent_price_day`,
     ifnull(`t`.`f_service_fee`, 0.0)                                     AS `f_service_fee`,
     ifnull(`t`.`f_deposit_fee`, 0.0)                                     AS `f_deposit_fee`,
     ifnull(`t`.`f_deposit_month`, 0)                                     AS `f_deposit_month`,
     ifnull(`t`.`f_period_month`, 0)                                      AS `f_period_month`,
     ifnull(`hd`.`f_decoration`, 0)                                       AS `f_decoration`,
     (CASE `hd`.`f_decoration`
      WHEN 1
        THEN '精装'
      WHEN 2
        THEN '简装'
      WHEN 3
        THEN '毛坯'
      WHEN 4
        THEN '老旧'
      WHEN 5
        THEN '豪装'
      WHEN 6
        THEN '中装'
      WHEN 7
        THEN '普装'
      ELSE '其他' END)                                                      AS `dec_name`,
     ifnull(`hd`.`f_toilet_nums`, 0)                                      AS `f_toilet_nums`,
     ifnull(`hd`.`f_balcony_nums`, 0)                                     AS `f_balcony_nums`,
     ifnull(`hd`.`f_insurance`, 0)                                        AS `f_insurance`,
     `hd`.`f_comment`                                                     AS `f_comment`,
     ''                                                                   AS `f_room_code`,
     `hd`.`f_house_tag`                                                   AS `f_house_tag`,
     ''                                                                   AS `f_production_name`,
     ''                                                                   AS `f_room_name`,
     `t`.`f_first_pub_date`                                               AS `f_first_pub_date`,
     ifnull(`t`.`f_has_key`, 0)                                           AS `f_has_key`,
     `t`.`f_creation_date`                                                AS `f_creation_date`,
     `hd`.`f_community_name`                                              AS `f_community_name`,
     `hd`.`f_address`                                                     AS `f_address`,
     ifnull(`hd`.`f_bedroom_nums`, 0)                                     AS `f_bedroom_nums`,
     ifnull(`hd`.`f_livingroom_nums`, 0)                                  AS `f_livingroom_nums`,
     ifnull(`hd`.`f_flow_no`, 0)                                          AS `f_flow_no`,
     ifnull(`hd`.`f_flow_total`, 0)                                       AS `f_flow_total`,
     `hd`.`f_entire_rent`                                                 AS `f_entire_rent`,
     (CASE `hd`.`f_entire_rent`
      WHEN 0
        THEN '合租'
      WHEN 1
        THEN '整租'
      WHEN 2
        THEN '整分皆可'
      ELSE '其他' END)                                                      AS `rent_name`,
     `hd`.`f_subway`                                                      AS `f_subway`,
     `hd`.`f_subway_distance`                                             AS `f_subway_distance`,
     `hd`.`f_bus_stations`                                                AS `f_bus_stations`,
     `hd`.`f_surround`                                                    AS `f_surround`,
     `hd`.`f_source`                                                      AS `f_source`,
     `hd`.`f_city_id`                                                     AS `f_city_id`,
     `hd`.`f_biz_id`                                                      AS `f_biz_id`,
     `hd`.`f_is_top`                                                      AS `f_is_top`,
     `hd`.`f_approve_status`                                              AS `f_approve_status`,
     `hd`.`f_district_id`                                                 AS `f_district_id`,
     `hd`.`f_subway_line_id`                                              AS `f_subway_line_id`,
     `hd`.`f_subway_station_id`                                           AS `f_subway_station_id`,
     `hd`.`f_baidu_lo`                                                    AS `f_baidu_lo`,
     `hd`.`f_baidu_la`                                                    AS `f_baidu_la`,
     concat_ws(',', cast(ifnull(`hd`.`f_baidu_la`, 0.0) AS CHAR CHARSET utf8),
               cast(ifnull(`hd`.`f_baidu_lo`, 0.0) AS CHAR CHARSET utf8)) AS `Google`,
     `hd`.`f_bizname`                                                     AS `f_bizname`,
     `t`.`f_agency_phone`                                                 AS `f_agency_phone`,
     `t`.`f_company_id`                                                   AS `f_company_id`,
     `t`.`f_company_name`                                                 AS `f_company_name`,
     `hd`.`f_is_pay_month`                                                AS `f_is_pay_month`,
     `hd`.`f_pub_type`                                                    AS `f_pub_type`,
     `t`.`f_pub_date`                                                     AS `f_pub_date`,
     `t`.`f_pub_date`                                                     AS `f_pub_date_str`,
     `t`.`f_last_change_date`                                             AS `f_last_change_date`,
     `hd`.`f_last_change_date`                                            AS `f_last_change_date_hd`,
     `t`.`f_last_change_date`                                             AS `f_lc_date`,
     `calScore`(`t`.`f_house_sell_id`)                                    AS `rank_num`,
     `t`.`f_is_delete`                                                    AS `f_is_delete`
   FROM (`db_huizhaofang_platform`.`t_house_base` `t` LEFT JOIN `db_huizhaofang_platform`.`t_house_detail` `hd`
       ON ((`hd`.`f_house_sell_id` = `t`.`f_house_sell_id`)))
   WHERE (`hd`.`f_entire_rent` <> 0))
  UNION ALL (SELECT
               concat_ws('_', `r`.`id`, `r`.`f_house_sell_id`, 0)                   AS `ID`,
               `r`.`id`                                                             AS `f_ID`,
               `r`.`id`                                                             AS `f_room_id`,
               `r`.`f_house_sell_id`                                                AS `f_house_sell_id`,
               `r`.`f_status`                                                       AS `f_status`,
               `r`.`f_area`                                                         AS `f_area`,
               `hd`.`f_area`                                                        AS `f_house_area`,
               `r`.`f_room_type`                                                    AS `f_room_type`,
               (CASE `r`.`f_room_type`
                WHEN 1
                  THEN '主卧'
                WHEN 10
                  THEN '次卧'
                WHEN 20
                  THEN '优化间'
                WHEN 30
                  THEN '隔间'
                ELSE '其他' END)                                                      AS `rtName`,
               `r`.`f_room_use`                                                     AS `f_room_use`,
               (CASE `r`.`f_room_use`
                WHEN 1
                  THEN '居住'
                WHEN 2
                  THEN '写字楼'
                WHEN 3
                  THEN '商铺'
                ELSE '其他' END)                                                      AS `ruName`,
               `r`.`f_orientations`                                                 AS `f_orientations`,
               (CASE `r`.`f_orientations`
                WHEN '10001'
                  THEN '东'
                WHEN '10002'
                  THEN '西'
                WHEN '10003'
                  THEN '南'
                WHEN '10004'
                  THEN '北'
                WHEN '10023'
                  THEN '西南'
                WHEN '10024'
                  THEN '西北'
                WHEN '10014'
                  THEN '东北'
                WHEN '10013'
                  THEN '东南'
                WHEN '10034'
                  THEN '南北'
                WHEN '10012'
                  THEN '东西'
                ELSE '其他' END)                                                      AS `r_ori_name`,
               `r`.`f_can_checkin_date`                                             AS `f_can_checkin_date`,
               ifnull(`r`.`f_rent_price_month`, 0)                                  AS `f_rent_price_month`,
               (CASE `r`.`f_rent_price_month`
                WHEN 0.0
                  THEN 0
                ELSE 1 END)                                                         AS `f_rent_price_zero`,
               ifnull(`r`.`f_rent_price_day`, 0)                                    AS `f_rent_price_day`,
               ifnull(`r`.`f_service_fee`, 0)                                       AS `f_service_fee`,
               ifnull(`r`.`f_deposit_fee`, 0)                                       AS `f_deposit_fee`,
               ifnull(`r`.`f_deposit_month`, 0)                                     AS `f_deposit_month`,
               ifnull(`r`.`f_period_month`, 0)                                      AS `f_period_month`,
               ifnull(`r`.`f_decoration`, 0)                                        AS `f_decoration`,
               (CASE `r`.`f_decoration`
                WHEN 1
                  THEN '精装'
                WHEN 2
                  THEN '简装'
                WHEN 3
                  THEN '毛坯'
                WHEN 4
                  THEN '老旧'
                WHEN 5
                  THEN '豪装'
                WHEN 6
                  THEN '中装'
                WHEN 7
                  THEN '普装'
                ELSE '其他' END)                                                      AS `r_dec_name`,
               ifnull(`r`.`f_toilet`, 0)                                            AS `f_toilet`,
               ifnull(`r`.`f_balcony`, 0)                                           AS `f_balcony`,
               ifnull(`r`.`f_insurance`, 0)                                         AS `f_insurance`,
               `r`.`f_comment`                                                      AS `f_comment`,
               `r`.`f_room_code`                                                    AS `f_room_code`,
               `r`.`f_room_tag`                                                     AS `f_room_tag`,
               `r`.`f_production_name`                                              AS `f_production_name`,
               `r`.`f_room_name`                                                    AS `f_room_name`,
               `r`.`f_first_pub_date`                                               AS `f_first_pub_date`,
               ifnull(`r`.`f_has_key`, 0)                                           AS `f_has_key`,
               `r`.`f_creation_date`                                                AS `f_creation_date`,
               `hd`.`f_community_name`                                              AS `f_community_name`,
               `hd`.`f_address`                                                     AS `f_address`,
               ifnull(`hd`.`f_bedroom_nums`, 0)                                     AS `f_bedroom_nums`,
               ifnull(`hd`.`f_livingroom_nums`, 0)                                  AS `f_livingroom_nums`,
               ifnull(`hd`.`f_flow_no`, 0)                                          AS `f_flow_no`,
               ifnull(`hd`.`f_flow_total`, 0)                                       AS `f_flow_total`,
               `hd`.`f_entire_rent`                                                 AS `f_entire_rent`,
               (CASE `hd`.`f_entire_rent`
                WHEN 0
                  THEN '合租'
                WHEN 1
                  THEN '整租'
                WHEN 2
                  THEN '整分皆可'
                ELSE '其他' END)                                                      AS `rent_name`,
               `hd`.`f_subway`                                                      AS `f_subway`,
               `hd`.`f_subway_distance`                                             AS `f_subway_distance`,
               `hd`.`f_bus_stations`                                                AS `f_bus_stations`,
               `hd`.`f_surround`                                                    AS `f_surround`,
               `hd`.`f_source`                                                      AS `f_source`,
               `hd`.`f_city_id`                                                     AS `f_city_id`,
               `hd`.`f_biz_id`                                                      AS `f_biz_id`,
               `r`.`f_is_top`                                                       AS `f_is_top`,
               `r`.`f_approve_status`                                               AS `f_approve_status`,
               `hd`.`f_district_id`                                                 AS `f_district_id`,
               `hd`.`f_subway_line_id`                                              AS `f_subway_line_id`,
               `hd`.`f_subway_station_id`                                           AS `f_subway_station_id`,
               `hd`.`f_baidu_lo`                                                    AS `f_baidu_lo`,
               `hd`.`f_baidu_la`                                                    AS `f_baidu_la`,
               concat_ws(',', cast(ifnull(`hd`.`f_baidu_la`, 0.0) AS CHAR CHARSET utf8),
                         cast(ifnull(`hd`.`f_baidu_lo`, 0.0) AS CHAR CHARSET utf8)) AS `Google`,
               `hd`.`f_bizname`                                                     AS `f_bizname`,
               `r`.`f_agency_phone`                                                 AS `f_agency_phone`,
               `hb`.`f_company_id`                                                  AS `f_company_id`,
               `hb`.`f_company_name`                                                AS `f_company_name`,
               `r`.`f_is_pay_month`                                                 AS `f_is_pay_month`,
               `r`.`f_pub_type`                                                     AS `f_pub_type`,
               `r`.`f_pub_date`                                                     AS `f_pub_date`,
               `r`.`f_pub_date`                                                     AS `pubDateStr`,
               `r`.`f_last_change_date`                                             AS `rupdateTimeStr`,
               `hd`.`f_last_change_date`                                            AS `f_last_change_date_hd`,
               `r`.`f_last_change_date`                                             AS `rCanCheckinDateStr`,
               `CALRSCORE`(`r`.`id`)                                                AS `rank_num`,
               `r`.`f_is_delete`                                                    AS `f_is_delete`
             FROM (
                 (`db_huizhaofang_platform`.`t_room_base` `r` LEFT JOIN `db_huizhaofang_platform`.`t_house_detail` `hd`
                     ON ((`hd`.`f_house_sell_id` = `r`.`f_house_sell_id`))) LEFT JOIN
                 `db_huizhaofang_platform`.`t_house_base` `hb` ON ((`hb`.`f_house_sell_id` = `r`.`f_house_sell_id`))));



create table f_statistic_report
(
	f_id bigint auto_increment
		primary key,
	f_city_id int(10) unsigned not null comment '城市id',
	f_ftol int(10) null comment '页面访问数量',
	f_fpv int(10) unsigned null comment 'pv访问数量',
	f_fss int(10) unsigned null comment '搜索框点击量',
	f_fbdj int(10) unsigned null comment 'banner点击量',
	f_fjg int(10) unsigned null comment '金刚点击量',
	f_fppgy int(10) unsigned null comment '品牌公寓点击量',
	f_fhz int(10) unsigned null comment '合租点击量',
	f_fzz int(10) unsigned null comment '整租点击量',
	f_ffq int(10) unsigned null comment '分期点击量',
	f_time varchar(10) not null comment '日期字符串',
	f_state int null comment '状态点击量',
	f_create_time datetime null comment '创建时间',
	f_update_time datetime null comment '修改时间'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='监控日报统计';


 --下架公司配置表
CREATE TABLE `t_company_off_config` (                                                                               
                        `f_id` int(36) NOT NULL AUTO_INCREMENT COMMENT '主键ID',                                                        
                        `f_city_id` int(10) DEFAULT NULL COMMENT '城市ID',                                                              
                        `f_city_name` varchar(20) DEFAULT NULL COMMENT '城市名称',                                                    
                        `f_company_id` varchar(36) DEFAULT NULL COMMENT '公司ID',                                                       
                        `f_company_name` varchar(100) DEFAULT NULL COMMENT '公司名称',                                                
                        `f_off_desc` varchar(500) DEFAULT NULL COMMENT '下架原因',                                                    
                        `f_status` int(2) DEFAULT NULL COMMENT '是否下架 1是 0否',                                                  
                        `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',  
                        `f_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',                          
                        PRIMARY KEY (`f_id`)                                                                                              
                      ) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 
                      
                      CREATE TABLE `t_third_sys_file` (                                                            
                    `f_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',                                   
                    `f_channel` int(11) DEFAULT NULL COMMENT '同步渠道 1:闲鱼 2:百度地图',           
                    `f_pic_root_path` varchar(200) DEFAULT NULL COMMENT '找房图片地址',                  
                    `f_file_id` varchar(50) DEFAULT NULL COMMENT '闲鱼返回文件ID',                       
                    `f_user_id` varchar(50) DEFAULT NULL COMMENT '淘宝账号',                               
                    `f_user_session` varchar(50) DEFAULT NULL COMMENT '账号授权session',                   
                    `f_error_code` varchar(50) DEFAULT NULL COMMENT '返回编码',                            
                    `f_error_msg` varchar(50) DEFAULT NULL COMMENT '返回错误信息描述',                 
                    `f_status` int(1) DEFAULT NULL COMMENT '上传状态 1成功 0初始化',                  
                    `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  
                    `f_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',                          
                    PRIMARY KEY (`f_id`)                                                                       
                  ) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8

--房源同步操作表
CREATE TABLE `t_third_sys_record` (                                                                      
                      `f_id` int(11) NOT NULL AUTO_INCREMENT,                                                                
                      `f_opt_type` int(10) NOT NULL COMMENT '操作类型 1新增 2更新 3下架 5上架',                  
                      `f_opt_target_name` varchar(10) NOT NULL COMMENT '操作目标',                                       
                      `f_opt_target_code` int(10) DEFAULT NULL COMMENT '操作目标编号 1闲鱼 2百度 -1所有',        
                      `f_house_source_name` varchar(10) NOT NULL COMMENT '房源来源名称',                               
                      `f_house_source_appid` varchar(20) NOT NULL COMMENT '房源来源编码',                              
                      `f_house_entire_rent` int(10) NOT NULL COMMENT '出租类型 0分租 1整租 2整分皆可',           
                      `f_house_sell_id` varchar(32) NOT NULL COMMENT '房源ID',                                             
                      `f_room_id` int(32) NOT NULL COMMENT '房间ID',                                                       
                      `f_opt_status` varchar(2) NOT NULL DEFAULT '0' COMMENT '操作状态 0:初始化 1:成功  2:失败',  
                      `f_error_code` varchar(200) DEFAULT NULL COMMENT '错误编码',                                       
                      `f_error_msg` varchar(200) DEFAULT NULL COMMENT '错误描述',                                        
                      `f_opt_userid` varchar(32) DEFAULT NULL COMMENT '操作用户ID',                                      
                      `f_success_url` varchar(100) DEFAULT NULL COMMENT '成功url',                                         
                      `f_opt_creatime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建操作时间',                
                      `f_opt_updatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作执行时间',          
                      PRIMARY KEY (`f_id`)                                                                                   
                    ) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8
--房源同步账号表（闲鱼）
CREATE TABLE `t_third_sys_user` (                                              
                    `f_id` int(10) NOT NULL AUTO_INCREMENT,                                      
                    `f_user_id` varchar(30) DEFAULT NULL COMMENT '淘宝账号',                 
                    `f_channel_name` varchar(20) DEFAULT NULL COMMENT '渠道名称',            
                    `f_channel_id` varchar(20) DEFAULT NULL COMMENT '渠道ID',                  
                    `f_company_name` varchar(100) DEFAULT NULL COMMENT '公司名',              
                    `f_company_id` varchar(50) DEFAULT NULL COMMENT '公司ID',                  
                    `f_is_delete` int(1) DEFAULT '0' COMMENT '是否删除 0否 1是',           
                    `f_use_count` int(10) DEFAULT '0' COMMENT '房源发布次数',              
                    `f_status` int(1) DEFAULT '0' COMMENT '同步api状态 1成功 0初始化',  
                    `f_version` int(10) DEFAULT '0' COMMENT '乐观锁-版本号',               
                    `f_is_use` int(1) DEFAULT '0' COMMENT '是否可用 0否 1是',              
                    `f_error_code` varchar(100) DEFAULT NULL COMMENT '返回编码',             
                    `f_error_msg` varchar(100) DEFAULT NULL COMMENT '返回错误信息',        
                    `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,                
                    `f_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',            
                    PRIMARY KEY (`f_id`)                                                         
                  ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8
--房源同步账号明细表（闲鱼）
CREATE TABLE `t_third_sys_user_record` (                                                                            
                           `f_id` int(10) NOT NULL AUTO_INCREMENT,                                                                           
                           `f_user_id` varchar(45) DEFAULT NULL COMMENT '闲鱼账户',                                                      
                           `f_company_id` varchar(100) DEFAULT NULL COMMENT '公司ID',                                                      
                           `f_item_id` varchar(45) DEFAULT NULL COMMENT '闲鱼房源ID',                                                    
                           `f_house_sell_id` varchar(45) DEFAULT NULL COMMENT '房源id',                                                    
                           `f_room_id` int(10) DEFAULT NULL COMMENT '房间id',                                                              
                           `f_house_entire` int(1) DEFAULT NULL COMMENT '出租方式 1整租 0分租',                                      
                           `f_house_status` int(1) DEFAULT NULL COMMENT '房态 0待出租 1已出租',                                      
                           `f_status` int(1) DEFAULT NULL COMMENT '同步状态',                                                            
                           `f_error_code` varchar(50) DEFAULT NULL COMMENT '返回错误编码',                                             
                           `f_error_msg` varchar(50) DEFAULT NULL COMMENT '返回错误描述',                                              
                           `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',  
                           `f_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',                          
                           PRIMARY KEY (`f_id`)                                                                                              
                         ) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8
--房源同步文件表（闲鱼）                         
CREATE TABLE `t_third_sys_file` (                                                            
                    `f_id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',                                   
                    `f_channel` int(11) DEFAULT NULL COMMENT '同步渠道 1:闲鱼 2:百度地图',           
                    `f_pic_root_path` varchar(200) DEFAULT NULL COMMENT '找房图片地址',                  
                    `f_file_id` varchar(50) DEFAULT NULL COMMENT '闲鱼返回文件ID',                       
                    `f_user_id` varchar(50) DEFAULT NULL COMMENT '淘宝账号',                               
                    `f_user_session` varchar(50) DEFAULT NULL COMMENT '账号授权session',                   
                    `f_error_code` varchar(50) DEFAULT NULL COMMENT '返回编码',                            
                    `f_error_msg` varchar(50) DEFAULT NULL COMMENT '返回错误信息描述',                 
                    `f_status` int(1) DEFAULT NULL COMMENT '上传状态 1成功 0初始化',                  
                    `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,  
                    `f_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',                          
                    PRIMARY KEY (`f_id`)                                                                       
                  ) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8
                  
 --房源同步中介白名单（闲鱼）                 
                  CREATE TABLE `t_third_sys_company` (                                                                                
                       `f_id` int(45) NOT NULL AUTO_INCREMENT COMMENT '主键',                                                          
                       `f_source` varchar(50) DEFAULT NULL COMMENT '来源',                                                             
                       `f_app_id` varchar(10) DEFAULT NULL COMMENT '来源标识',                                                       
                       `f_company_name` varchar(50) DEFAULT NULL COMMENT '公司名称',                                                 
                       `f_company_id` varchar(45) DEFAULT NULL COMMENT '公司ID',                                                       
                       `f_desc` varchar(100) DEFAULT NULL COMMENT '描述',                                                              
                       `f_status` int(2) DEFAULT '0' COMMENT '状态',                                                                   
                       `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',  
                       `f_update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '更新时间',                          
                       PRIMARY KEY (`f_id`)                                                                                              
                     ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8
--渠道白名单                     
                     CREATE TABLE `t_company_white_config` (                                                 
                          `f_id` int(36) NOT NULL AUTO_INCREMENT COMMENT '主键ID',                            
                          `f_source` varchar(20) DEFAULT NULL COMMENT '渠道',                                 
                          `f_app_id` varchar(20) DEFAULT NULL COMMENT '渠道ID',                               
                          `f_company_id` varchar(36) DEFAULT NULL COMMENT '公司ID',                           
                          `f_company_name` varchar(100) DEFAULT NULL COMMENT '公司名称',                    
                          `f_white_desc` varchar(500) DEFAULT NULL COMMENT '下架原因',                      
                          `f_status` int(2) DEFAULT NULL COMMENT '是否下架 1是 0否',                      
                          `f_white_count` int(10) DEFAULT NULL COMMENT '下架房源数',                       
                          `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',  
                          `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',  
                          PRIMARY KEY (`f_id`),                                                                 
                          UNIQUE KEY `index_appid_companyid` (`f_app_id`,`f_company_id`)                        
                        ) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8        