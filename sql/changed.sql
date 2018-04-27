--2017-06-05 09:47:15
CREATE TABLE `t_sys_code` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_code_name` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '码值名称',
  `f_code_value` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '码值',
  `f_type_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '类型编码',
  `f_type_name` varchar(30) COLLATE utf8_bin DEFAULT '' COMMENT '类型名称',
  PRIMARY KEY (`id`)
)

CREATE TABLE `t_sys_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_account` varchar(100) DEFAULT NULL COMMENT '用户名',
  `f_password` varchar(100) DEFAULT NULL COMMENT '密码',
  `f_real_name` varchar(40) DEFAULT NULL COMMENT '真实姓名',
  `f_salt` varchar(100) DEFAULT NULL COMMENT '加密盐值',
  `f_token` varchar(100) DEFAULT NULL COMMENT '登录token',
  `f_status` int(2) DEFAULT '1' COMMENT '账号状态  1 可用，2 不可用',
  `f_login_ip` varchar(40) DEFAULT NULL COMMENT '登录IP',
  `f_last_login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `f_create_time` datetime NOT NULL COMMENT '创建时间',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
)

ALTER TABLE `db_huizhaofang_platform`.`t_house_setting` CHANGE COLUMN `f_is_delete` `f_is_delete` int(1) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效';
ALTER TABLE `db_huizhaofang_platform`.`t_house_detail_extend` ADD COLUMN `f_transfer_flag`  int(1) NOT NULL DEFAULT '0' COMMENT '0 :失败，1：成功';


--saas房源对接，t_house_detail表新增是否置顶、房源类型，集中式房间编号
ALTER TABLE `db_huizhaofang_platform`.`t_house_detail` ADD COLUMN `f_is_top` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否置顶 0 否，1 是；' AFTER `f_source`;
ALTER TABLE `t_house_detail` ADD COLUMN `f_focus_code`  varchar(30)  NULL DEFAULT NULL COMMENT '集中式公寓房间编号' AFTER `f_is_top`;
ALTER TABLE `t_house_detail` ADD COLUMN `f_house_type` tinyint(2)  NOT NULL DEFAULT '0' COMMENT '房源类型  0: 分散式（普通）， 1: 集中式' AFTER `f_focus_code`;
--saas房源对接，t_room_base表新增是否置顶
ALTER TABLE `t_room_base` ADD COLUMN `f_is_top` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否置顶    0:否    1:是' AFTER `f_has_key`; 
--roomBase 增加经纪人电话字段
ALTER TABLE `db_huizhaofang_platform`.`t_room_base` ADD COLUMN `f_agency_phone` varchar(16) NULL DEFAULT NULL COMMENT '经纪人电话' AFTER `f_production_name`;

--错误数据处理
ALTER TABLE `db_huizhaofang_platform`.`t_house_detail` ADD COLUMN `f_is_approve` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否已审核： 0 未审核，1 已程序审核， 2 已人工审核' AFTER `f_house_type`;
ALTER TABLE `db_huizhaofang_platform`.`t_room_base` ADD COLUMN `f_is_approve` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否已审核： 0 未审核，1 已程序审核， 2 已人工审核' AFTER `f_is_top`;
--审核记录表
CREATE TABLE `t_house_approve_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_house_sell_id` varchar(20) NOT NULL COMMENT '房源ID',
  `f_room_id` varchar(11) NOT NULL DEFAULT '0' COMMENT '房间ID',
  `f_operator` varchar(40) DEFAULT NULL COMMENT '审核操作者',
  `f_error_reason` varchar(255) DEFAULT NULL,
  `f_approve_desc` varchar(255) DEFAULT NULL,
  `f_approve_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
  `f_creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_last_change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6568 DEFAULT CHARSET=utf8;


--创建房源扩展表-全房通
CREATE TABLE `t_house_detail_extend_qft` (                                                                                                                              
                         `f_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',                                                                                                          
                         `f_out_house_id` varchar(36) NOT NULL COMMENT '合作公寓的房屋ID',                                                                                          
                         `f_house_sell_id` varchar(26) DEFAULT NULL,                                                                                                                       
                         `f_pic_url_list` varchar(3000) DEFAULT NULL COMMENT '合作公寓的房屋图片url列表，图片大小小于5M，房源图片数量不超过20张。',          
                         `f_rent_type` int(4) DEFAULT NULL COMMENT '出租方式，枚举值1：整租 2：单间出租',                                                                  
                         `f_bed_room_num` int(4) DEFAULT NULL COMMENT '房屋户型-室 ',                                                                                                 
                         `f_living_room_num` int(4) DEFAULT NULL COMMENT '房屋户型-厅',                                                                                               
                         `f_toilet_num` int(4) DEFAULT NULL COMMENT '房屋户型-卫',                                                                                                    
                         `f_rent_room_area` float(6,2) DEFAULT NULL COMMENT '房屋面积，最多支持两位小数',                                                                     
                         `f_bed_room_type` int(4) DEFAULT NULL COMMENT '出租类型，枚举值31:主卧 32:次卧 33:不区分主次',                                                   
                         `f_room_name` varchar(10) DEFAULT NULL COMMENT '房间名称',                                                                                                    
                         `f_room_code` varchar(10) DEFAULT NULL COMMENT '房间编码',                                                                                                    
                         `f_face_to_type` varchar(10) DEFAULT NULL COMMENT '朝向，枚举值：60-70{东，南，西，北，东南，西南，东北，西北，东西，南北，未知}',  
                         `f_total_floor` int(4) DEFAULT NULL COMMENT '楼层总数',                                                                                                       
                         `f_house_floor` int(4) DEFAULT NULL COMMENT '房间所在楼层',                                                                                                 
                         `f_feature_tag` varchar(300) DEFAULT NULL COMMENT '房间标签，枚举值，可多选，以逗号分隔',                                                        
                         `f_detail_point` varchar(300) DEFAULT NULL COMMENT '房屋配置，枚举值，可多选，以逗号分隔',                                                       
                         `f_service_point` varchar(300) DEFAULT NULL COMMENT '公寓配套服务，枚举值，可多选，以逗号分隔',                                                
                         `f_month_rent` int(4) DEFAULT NULL COMMENT '月租金',                                                                                                           
                         `f_rent_start_date` datetime NOT NULL COMMENT '起租时间，格式yyyy-MM-dd',                                                                                  
                         `f_short_rent` int(4) DEFAULT NULL COMMENT '是否支持短租，枚举值0不支持  1支持',                                                                   
                         `f_provice` varchar(30) NOT NULL COMMENT '房屋所在省',                                                                                                       
                         `f_city_name` varchar(30) DEFAULT NULL COMMENT '房屋所在城市',                                                                                              
                         `f_county_name` varchar(30) DEFAULT NULL COMMENT '房屋所在区县',                                                                                            
                         `f_area_name` varchar(30) DEFAULT NULL COMMENT '房屋所在商圈',                                                                                              
                         `f_district_name` varchar(30) DEFAULT NULL COMMENT '房屋所在小区名称 蓝天家园',                                                                       
                         `f_street` varchar(300) NOT NULL COMMENT '房屋所在小区详细地址 北京市昌平区蓝天家园',                                                          
                         `f_address` varchar(300) NOT NULL COMMENT '出租房屋门牌地址 1号楼3单元208室',                                                                         
                         `f_subway_line` varchar(30) DEFAULT NULL COMMENT '房屋附近地铁站所在线路名称 1号线',                                                               
                         `f_subway_station` varchar(30) DEFAULT NULL COMMENT '房屋附近地铁站名 北京南站',                                                                      
                         `f_house_desc` varchar(500) DEFAULT NULL COMMENT '房间描述',                                                                                                  
                         `f_x_code` varchar(30) DEFAULT NULL COMMENT '房间位置坐标-经度',                                                                                          
                         `f_y_code` varchar(30) DEFAULT NULL COMMENT '房间位置坐标-纬度',                                                                                          
                         `f_agent_phone` varchar(20) DEFAULT NULL COMMENT '房管员手机号',                                                                                            
                         `f_order_phone` varchar(20) DEFAULT NULL COMMENT '预约短信接收到的手机号',                                                                             
                         `f_agent_name` varchar(100) DEFAULT NULL COMMENT '房管员姓名',                                                                                                
                         `f_video_url` varchar(200) DEFAULT NULL COMMENT '合作公寓系统房屋视频url',                                                                              
                         `f_build_year` int(4) DEFAULT NULL COMMENT '房源建筑年代',                                                                                                  
                         `f_supply_heating` int(4) DEFAULT NULL COMMENT '小区供暖方式，1集中供暖 2自供暖 3无供暖',                                                        
                         `f_green_ratio` int(4) DEFAULT NULL COMMENT '小区绿化率，不带百分号',                                                                                  
                         `f_build_type` int(4) DEFAULT NULL COMMENT '小区建筑类型，71塔楼 72板楼 73平板',                                                                     
                         `f_state` varchar(10) DEFAULT '4000' COMMENT '房源状态  4000:上架 5000:下架',                                                                             
                         `f_memo` varchar(500) DEFAULT NULL COMMENT '改动原因',
						 `f_live_bed_totile` varchar(30) DEFAULT NULL COMMENT '户型',
						 `f_company_id` varchar(36) DEFAULT NULL COMMENT '公司ID',
						 `f_company_name` varchar(50) DEFAULT NULL COMMENT '公司名称',
						 `f_fitment_state` varchar(30) DEFAULT NULL COMMENT '装修状态',                                                         
                         `f_app_id` varchar(36) NOT NULL COMMENT '分配给合作公寓的接入ID  与f_out_house_id确定唯一房源',                                                  
                         `f_transfer_flag` int(2) NOT NULL DEFAULT '0' COMMENT '0 :失败，1：成功',                                                                                   
                         `f_create_time` datetime NOT NULL COMMENT '创建时间',                                                                                                         
                         `f_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',                                                                   
                         PRIMARY KEY (`f_id`)                                                                                                                                              
                       ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='百度接口房源基础信息表-全房通'  
                       
--房源扩展表增加字段 公司ID、公司名称、装修状态
ALTER TABLE `db_huizhaofang_platform`.`t_house_detail_extend` ADD COLUMN `f_company_id` varchar(36) NULL DEFAULT NULL COMMENT '公司ID' AFTER `f_memo`;
ALTER TABLE `db_huizhaofang_platform`.`t_house_detail_extend` ADD COLUMN `f_company_name` varchar(36) NULL DEFAULT NULL COMMENT '公司名称' AFTER `f_memo`;
alter table t_house_detail_extend_qft add constraint uk_f_out_house_id unique (f_out_house_id);
alter table t_house_detail_extend add constraint uk_f_out_house_id unique (f_out_house_id);

--h5二期 ，中介管理表
CREATE TABLE `t_agency_manage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_agency_id` varchar(15) NOT NULL COMMENT '中介公司ID',
  `f_agency_name` varchar(255) NOT NULL COMMENT '中介公司名称',
  `f_agency_desc` varchar(500) NOT NULL COMMENT '中介公司描述',
  `f_pic_root_path` varchar(255) NOT NULL COMMENT '图片服务器位置',
  `f_pic_web_path` varchar(255) NOT NULL COMMENT '图片网络路径',
  `f_is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` datetime NOT NULL COMMENT '创建时间',
  `f_last_change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_angency_id` (`f_agency_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='公寓中介公司管理表';

--户型对应关系表
CREATE TABLE `t_house_type_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `f_live_bed_totile` varchar(40) DEFAULT NULL COMMENT '户型描述',
  `f_bedroom_nums` int(2) DEFAULT '0' COMMENT '卧室数量',
  `f_livingroom_nums` int(2) DEFAULT '0' COMMENT '客厅数量',
  `f_toilet_nums` int(2) DEFAULT '0' COMMENT '卫生间数量',
  `f_status` tinyint(2) DEFAULT '1' COMMENT '当前状态： 1 可用，0 不可用',
  `f_creation_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_last_change_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


--审核状态和房源状态分离
ALTER TABLE `db_huizhaofang_platform`.`t_house_detail` CHANGE COLUMN `f_is_approve` `f_approve_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否已审核： 0 未审核，1 已程序审核， 2 已人工审核';
ALTER TABLE `db_huizhaofang_platform`.`t_room_base` CHANGE COLUMN `f_is_approve` `f_approve_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否已审核： 0 未审核，1 已程序审核， 2 已人工审核';


--增加房间类型字典表
CREATE TABLE `tb_house_dicitem` (
  `f_id` int(32) NOT NULL AUTO_INCREMENT COMMENT '主建',
  `f_dic_id` varchar(36) DEFAULT NULL COMMENT '本级ID',
  `f_company_id` varchar(36) NOT NULL COMMENT '公司ID',
  `f_company_name` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `f_dic_type` varchar(50) DEFAULT NULL COMMENT '字典类型',
  `f_dic_code` varchar(50) DEFAULT NULL COMMENT '字典code',
  `f_dic_name` varchar(50) DEFAULT NULL COMMENT '字典名称',
  `f_dic_value` varchar(50) DEFAULT NULL COMMENT '字典vlaue',
  `f_is_delete` int(2) DEFAULT NULL COMMENT '是否删除 0否  1是',
  `f_sort` int(11) DEFAULT NULL COMMENT '排序值',
  `f_create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `f_parent_id` varchar(100) DEFAULT NULL COMMENT '父级ID',
  `f_dic_rank` varchar(100) DEFAULT NULL COMMENT '字典等级',
  `f_create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `f_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

--修改房间类型字段类型
alter table t_house_detail_extend_qft modify column f_bed_room_type varchar(50);

ALTER TABLE `db_huizhaofang_platform`.`t_agency_manage` ADD COLUMN `f_pic_sx_path` VARCHAR(255) NULL COMMENT '筛选页面图片路径' AFTER `f_pic_web_path`;

insert into `t_agency_manage` (`id`, `f_agency_id`, `f_agency_name`, `f_agency_desc`, `f_pic_root_path`, `f_pic_web_path`, `f_pic_sx_path`, `f_is_delete`, `f_creation_date`, `f_last_change_date`) values('3','-1','更多','更多','http://hzf-image-test.oss-cn-beijing.aliyuncs.com/company_image/gengduo.png','http://hzf-image-test.oss-cn-beijing.aliyuncs.com/company_image/gengduo.png',NULL,'0','0000-00-00 00:00:00','2017-08-29 21:21:04');


--增加月付公司表
CREATE TABLE `t_company_pay_month` (                                                    
                       `f_id` int(16) NOT NULL AUTO_INCREMENT,                                               
                       `f_company_name_hzf` varchar(200) NOT NULL COMMENT '公司名称-找房',             
                       `f_company_id_hzf` varchar(36) NOT NULL COMMENT '公司ID-找房',                    
                       `f_company_name_saas` varchar(200) NOT NULL COMMENT '公司名称-saas',              
                       `f_company_id_saas` varchar(36) NOT NULL COMMENT '公司ID-saas',                     
                       `f_pay_status` int(4) NOT NULL COMMENT '是否支持分期 0否 1是',                
                       `f_run` int(4) NOT NULL COMMENT '是否收集标签 0否 1是',                       
                       `f_is_delete` int(4) NOT NULL COMMENT '是否删除 0未删除 1已删除',           
                       `f_create_time` datetime NOT NULL COMMENT '创建时间',                             
                       `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',  
                       PRIMARY KEY (`f_id`)                                                                  
                     ) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 
--增加月付字段标识
ALTER TABLE `db_huizhaofang_platform`.`t_house_detail` ADD COLUMN  `f_is_pay_month` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否支付月付： 0 不支持，1 支持' AFTER `f_approve_status`;
ALTER TABLE `db_huizhaofang_platform`.`t_room_base` ADD COLUMN  `f_is_pay_month` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否支付月付： 0 不支持，1 支持' AFTER `f_approve_status`;
                     
--房源数据分离
CREATE TABLE `t_room_original` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_house_sell_id` varchar(20) NOT NULL COMMENT '房源销售编号',
  `f_room_id` int(11) NOT NULL,
  `f_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '房间状态',
  `f_area` float(6,2) NOT NULL DEFAULT '0.00' COMMENT '建筑面积',
  `f_room_comment` varchar(1024) DEFAULT NULL COMMENT '房间描述',
  `f_room_type` int(6) NOT NULL DEFAULT '0' COMMENT '房间类型  1：主卧，10：次卧，20：优化间',
  `f_room_use` tinyint(2) NOT NULL DEFAULT '0' COMMENT '房间用途类型',
  `f_orientations` int(6) NOT NULL DEFAULT '0' COMMENT '朝向',
  `f_can_checkin_date` datetime NOT NULL COMMENT '可入住时间',
  `f_rent_price_month` int(11) NOT NULL DEFAULT '0' COMMENT '月租金',
  `f_rent_price_day` int(11) NOT NULL DEFAULT '0' COMMENT '日租金',
  `f_service_fee` int(11) NOT NULL DEFAULT '0' COMMENT '服务费或中介费',
  `f_deposit_fee` int(11) NOT NULL DEFAULT '0' COMMENT '押金',
  `f_deposit_month` tinyint(2) NOT NULL DEFAULT '0' COMMENT '押金押几个月',
  `f_period_month` tinyint(2) NOT NULL DEFAULT '0' COMMENT '每次付几个月的租金',
  `f_decoration` int(11) NOT NULL DEFAULT '0' COMMENT '装修档次',
  `f_toilet` tinyint(2) NOT NULL DEFAULT '0' COMMENT '卫生间独立',
  `f_balcony` tinyint(2) NOT NULL DEFAULT '0' COMMENT '独立阳台',
  `f_insurance` tinyint(2) NOT NULL DEFAULT '0' COMMENT '家财险',
  `f_agency_phone` varchar(16) DEFAULT NULL COMMENT '经纪人电话',
  `f_room_name` varchar(30) DEFAULT NULL COMMENT '房间名称',
  `f_room_settings` varchar(1000) DEFAULT NULL,
  `f_room_pics` varchar(1000) DEFAULT NULL,
  `f_has_key` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否有钥匙；1:有；0:无',
  `f_is_top` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否置顶    0:否    1:是',
  `f_is_delete` tinyint(2) NOT NULL DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` datetime NOT NULL COMMENT '创建时间',
  `f_last_change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_hsid` (`f_house_sell_id`),
  KEY `index_is_delete` (`f_is_delete`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='房间基础信息表';


CREATE TABLE `t_house_original` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID（自增）',
  `f_house_sell_id` varchar(20) NOT NULL COMMENT '房源销售编号',
  `f_status` int(2) NOT NULL DEFAULT '0' COMMENT '房源状态',
  `f_rent_price_month` int(11) NOT NULL DEFAULT '0' COMMENT '月租金',
  `f_rent_price_day` int(11) NOT NULL DEFAULT '0' COMMENT '日租金',
  `f_service_fee` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '服务费或中介费',
  `f_deposit_fee` int(11) NOT NULL DEFAULT '0' COMMENT '押金',
  `f_deposit_month` tinyint(2) NOT NULL DEFAULT '0' COMMENT '押金押几个月',
  `f_period_month` tinyint(2) NOT NULL DEFAULT '0' COMMENT '每次付几个月的租金',
  `f_company_id` varchar(36) DEFAULT '0' COMMENT '公司id',
  `f_company_name` varchar(50) DEFAULT NULL COMMENT '公司名称',
  `f_agency_id` int(11) NOT NULL DEFAULT '0' COMMENT '经纪人id',
  `f_agency_phone` varchar(16) DEFAULT NULL COMMENT '经纪人电话',
  `f_agency_name` varchar(30) DEFAULT NULL COMMENT '经纪人姓名',
  `f_agency_introduce` varchar(255) DEFAULT NULL COMMENT '经纪人介绍',
  `f_agency_gender` tinyint(2) NOT NULL DEFAULT '0' COMMENT '经纪人性别',
  `f_agency_avatar` varchar(255) DEFAULT NULL COMMENT '经纪人头像',
  `f_has_key` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否有钥匙；1:有；0:无',
  `f_can_checkin_date` datetime NOT NULL COMMENT '可入住时间',
  `f_building_no` varchar(50) DEFAULT NULL COMMENT '楼栋编号',
  `f_unit_no` varchar(50) DEFAULT NULL COMMENT '单元号',
  `f_flow_no` varchar(30) DEFAULT NULL COMMENT '所在楼层',
  `f_flow_total` varchar(30) DEFAULT NULL COMMENT '总楼层',
  `f_house_no` varchar(20) DEFAULT NULL COMMENT '门牌号',
  `f_area` float(6,2) NOT NULL DEFAULT '0.00' COMMENT '建筑面积',
  `f_orientations` int(6) NOT NULL DEFAULT '0' COMMENT '朝向',
  `f_bedroom_nums` int(2) NOT NULL DEFAULT '0' COMMENT '卧室数量',
  `f_livingroom_nums` int(2) NOT NULL DEFAULT '0' COMMENT '起居室数量',
  `f_kitchen_nums` int(2) NOT NULL DEFAULT '0' COMMENT '出房数量',
  `f_toilet_nums` int(2) NOT NULL DEFAULT '0' COMMENT '卫生间数量',
  `f_balcony_nums` tinyint(2) NOT NULL DEFAULT '0' COMMENT '阳台数量',
  `f_province` varchar(20) DEFAULT NULL COMMENT '省份',
  `f_city` varchar(20) DEFAULT NULL COMMENT '市',
  `f_district` varchar(20) DEFAULT NULL COMMENT '行政区',
  `f_bizname` varchar(20) DEFAULT NULL COMMENT '商圈',
  `f_address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `f_community_name` varchar(50) DEFAULT NULL COMMENT '小区名称',
  `f_building_name` varchar(30) DEFAULT NULL COMMENT '楼栋名称',
  `f_baidu_lo` varchar(30) DEFAULT NULL COMMENT '百度坐标，经度',
  `f_baidu_la` varchar(30) DEFAULT NULL COMMENT '百度坐标，纬度',
  `f_building_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '建筑类型',
  `f_building_year` int(4) NOT NULL DEFAULT '0' COMMENT '建筑时间',
  `f_toilet` tinyint(2) NOT NULL DEFAULT '0' COMMENT '卫生间独立',
  `f_balcony` tinyint(2) NOT NULL DEFAULT '0' COMMENT '独立阳台',
  `f_insurance` tinyint(2) NOT NULL DEFAULT '0' COMMENT '家财险',
  `f_house_function` tinyint(2) NOT NULL DEFAULT '0' COMMENT '房源用途',
  `f_decoration` tinyint(2) NOT NULL DEFAULT '0' COMMENT '装修档次',
  `f_entire_rent` tinyint(2) NOT NULL DEFAULT '0' COMMENT '租住类型 0 分租 1整租 2 整分皆可',
  `f_house_pics` varchar(1000) DEFAULT NULL,
  `f_house_setting` varchar(1000) DEFAULT NULL,
  `f_comment` varchar(1024) DEFAULT NULL COMMENT '房源描述',
  `f_source` varchar(30) DEFAULT NULL COMMENT '来源',
  `f_is_top` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否置顶 0 否，1 是；',
  `f_focus_code` varchar(30) DEFAULT NULL COMMENT '集中式公寓房间编号',
  `f_house_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '房源类型  0: 分散式（普通）， 1: 集中式',
  `f_is_delete` tinyint(2) DEFAULT '0' COMMENT '该记录是否已被删除，1代表删除；0代表有效',
  `f_creation_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_last_change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  
  
  
  ---图片审核----
  ALTER TABLE `db_huizhaofang_platform`.`t_house_approve_record` CHANGE COLUMN `f_image_
` float(4,2) DEFAULT '0' COMMENT '图片评分';
  KEY `ix_hd_hs_id` (`f_house_sell_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='房源原始信息表';

--审核记录表修改
ALTER TABLE `db_huizhaofang_platform`.`t_house_approve_record` ADD COLUMN `f_image_score` int(4) DEFAULT '0' COMMENT '图片评分' AFTER `f_image_status`;
ALTER TABLE `db_huizhaofang_platform`.`t_house_approve_record` add COLUMN `f_status` int(2) DEFAULT '0' COMMENT '状态： 0 无效，1 有效';

--爱上租房源扩展表
CREATE TABLE `t_house_detail_extend_asz` (                                                                                                        
                             `f_id` int(32) NOT NULL AUTO_INCREMENT,                                                                                                         
                             `f_house_sell_id` varchar(32) DEFAULT NULL COMMENT '找房房源编号',                                                                        
                             `f_apartment_id` varchar(35) DEFAULT NULL COMMENT '公寓房源编号ID',                                                                       
                             `f_apartment_code` varchar(35) DEFAULT NULL COMMENT '房源编号',                                                                             
                             `f_house_id` varchar(35) DEFAULT NULL COMMENT '房源所属房屋',                                                                             
                             `f_rent_type` varchar(10) DEFAULT NULL COMMENT '出租方式 ENTIRE 整租 SHARE 合租',                                                       
                             `f_room_no` varchar(10) DEFAULT NULL COMMENT '房间号 出租方式为合租存在',                                                           
                             `f_rent_status` varchar(32) DEFAULT NULL COMMENT '出租状态 WAITING_RENT:待出租；RENTED:已出租；BOOKED:已下架INVALID:已失效',  
                             `f_rent_price` int(6) DEFAULT NULL COMMENT '出租月租金',                                                                                   
                             `f_customer_person` varchar(500) DEFAULT NULL COMMENT '租客信息',                                                                           
                             `f_residential_name` varchar(256) DEFAULT NULL COMMENT '楼盘名称',                                                                          
                             `f_city_code` varchar(12) DEFAULT NULL COMMENT '城市ID',                                                                                      
                             `f_city_name` varchar(32) DEFAULT NULL COMMENT '城市名称',                                                                                  
                             `f_area_code` varchar(12) DEFAULT NULL COMMENT '区域ID',                                                                                      
                             `f_area_name` varchar(32) DEFAULT NULL COMMENT '区域名称',                                                                                  
                             `f_address` varchar(256) DEFAULT NULL COMMENT '街道地址',                                                                                   
                             `f_business_circle_multi` varchar(100) DEFAULT NULL COMMENT '商圈',                                                                           
                             `f_lng` varchar(32) DEFAULT NULL COMMENT '经度',                                                                                              
                             `f_lat` varchar(32) DEFAULT NULL COMMENT '纬度',                                                                                              
                             `f_property_use` varchar(32) DEFAULT NULL COMMENT '物业用途',                                                                               
                             `f_floor` int(4) DEFAULT NULL COMMENT '所属楼层',                                                                                           
                             `f_ground_floors` int(4) DEFAULT NULL COMMENT '总楼层',                                                                                      
                             `f_rooms` int(4) DEFAULT NULL COMMENT '室',                                                                                                    
                             `f_livings` int(4) DEFAULT NULL COMMENT '厅',                                                                                                  
                             `f_bathrooms` int(4) DEFAULT NULL COMMENT '卫',                                                                                                
                             `f_build_area` float(10,2) DEFAULT NULL COMMENT '房源面积',                                                                                 
                             `f_total_area` float(10,2) DEFAULT NULL COMMENT '总面积',                                                                                    
                             `f_orientation` varchar(10) DEFAULT NULL COMMENT '朝向朝向：北，东北，东，东南，南，西南，',                                
                             `f_fitment_type` varchar(16) DEFAULT NULL COMMENT '装修情况 FITMENT_ROUGH 毛坯',                                                          
                             `f_house_room_feature` varchar(500) DEFAULT NULL COMMENT '房源特色',                                                                        
                             `f_house_configu_tation` varchar(500) DEFAULT NULL COMMENT '房屋配置',                                                                      
                             `f_remark` varchar(1024) DEFAULT NULL COMMENT '房屋描述',                                                                                   
                             `f_img_list` varchar(10240) DEFAULT NULL COMMENT '图片url',                                                                                   
                             `f_agent_uname` varchar(40) DEFAULT NULL COMMENT '经纪人姓名',                                                                             
                             `f_agent_uphone` varchar(32) DEFAULT NULL COMMENT '经纪人手机号',                                                                         
                             `f_agent_post` varchar(32) DEFAULT NULL COMMENT '所属岗位',                                                                                 
                             `f_agent_department` varchar(32) DEFAULT NULL COMMENT '所属部门',                                                                           
                             `f_create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',                                                                             
                             `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',                                                            
                             `f_online_status` varchar(10) DEFAULT NULL COMMENT '上架状态 ONLINE：上架OFFLINE:下架',                                                
                             `f_transfer_flag` int(4) DEFAULT NULL COMMENT '同步状态 0未同步 1已同步',                                                             
                             PRIMARY KEY (`f_id`)                                                                                                                            
                           ) ENGINE=InnoDB AUTO_INCREMENT=298236 DEFAULT CHARSET=utf8  

--t_agency_manage中介公司管理表新增城市ID字段
ALTER TABLE t_agency_manage ADD f_city_id int(11) NOT NULL COMMENT '城市ID';

--t_agency_manage中介公司管理表新增跳转链接字段
ALTER TABLE t_agency_manage ADD f_destination_url varchar(255) COMMENT '跳转链接';

--行政区增加坐标中心
ALTER TABLE `db_huizhaofang_platform`.`t_district` ADD COLUMN `center` varchar(50) NULL DEFAULT NULL COMMENT '中心坐 标' AFTER `name`;

--租房宝典新增两张表
CREATE TABLE `t_custom_city_price` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `f_city_id` int(11) NOT NULL COMMENT '城市ID',
  `f_limit_price` int(6) NOT NULL COMMENT '限定价格',
  `f_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1 有效，0 无效',
  `f_creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_last_change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

CREATE TABLE `t_custom_renting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `f_title` varchar(100) DEFAULT '' COMMENT '标题',
  `f_banner_url` varchar(256) DEFAULT '' COMMENT '图片链接',
  `f_inner_banner_url` varchar(256) DEFAULT '' COMMENT '内部banner链接',
  `f_desc` varchar(1000) DEFAULT '' COMMENT '情景描述',
  `f_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态',
  `f_creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_last_change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

--t_city中介公司管理表新增中心城市坐标点字段
ALTER TABLE t_city ADD center varchar(100) COMMENT '中心城市坐标点';

--t_agency_manage中介公司管理表新增某城市的公寓下的房间数量统计字段
ALTER TABLE t_agency_manage ADD f_room_count int(20) DEFAULT '0' COMMENT '某城市的公寓下的房间数量统计';
--t_agency_manage中介公司管理表新增品牌公寓名称字段
ALTER TABLE t_agency_manage ADD f_page_name varchar(50) DEFAULT NULL COMMENT '品牌公寓活动名称';
--t_slide_show_url轮播图表新增活动页面名称字段
ALTER TABLE t_slide_show_url ADD f_page_name varchar(50) DEFAULT NULL COMMENT '活动页面名称';

---中介公司对应城市过滤
CREATE TABLE `t_company_city_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_company_name` varchar(256) DEFAULT '' COMMENT '中介公司名称',
  `f_city_name` varchar(100) DEFAULT '' COMMENT '城市名称',
  `f_ciyt_id` int(11) DEFAULT NULL COMMENT '城市ID',
  `f_status` tinyint(1) DEFAULT '1' COMMENT '是否有效 ： 1 有效，0 无效',
  `f_creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_last_change_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

--渠道审核 2017年10月28日10:47:44
ALTER TABLE `db_huizhaofang_platform`.`t_platform_customer` ADD COLUMN `f_permission_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '权限状态： 0 默认两者都允许，1 禁止solr查， 2 程序不审核，3 两者都不允许' AFTER `f_permission_end_date`;
--修改用户优惠券表
CREATE TABLE `t_coupon` (
  `f_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `f_user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `f_title` varchar(765) DEFAULT NULL COMMENT '标题',
  `f_coupon_type` int(1) DEFAULT NULL COMMENT '优惠券类型',
  `f_code` varchar(90) DEFAULT NULL COMMENT '兑换码',
  `f_desc` varchar(765) DEFAULT NULL COMMENT '描述',
  `f_ext_desc` varchar(765) DEFAULT NULL COMMENT '扩展描述',
  `f_start_time` date DEFAULT NULL COMMENT '开始日期',
  `f_expire_time` date DEFAULT NULL COMMENT '过期日期',
  `f_comment` varchar(765) DEFAULT NULL COMMENT '备注',
  `f_state` int(1) NOT NULL DEFAULT '1' COMMENT '状态（1：默认有效；0：失效）',
  `f_create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `f_update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `f_coupon_link` varchar(255) DEFAULT NULL COMMENT '优惠券原链接',
  `f_index_link` varchar(100) DEFAULT NULL COMMENT '优惠券短链接',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8

--重复图片链接表
CREATE TABLE `t_repeat_img` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `f_repeat_img_url` varchar(200) DEFAULT '',
  `f_type` tinyint(2) DEFAULT '1' COMMENT '类型： 1 默认图片，2 装修图片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

--分项分记录 2017-11-09 16:58:50
--渠道默认分
CREATE TABLE `t_agency_default_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_source` varchar(40) DEFAULT NULL COMMENT '渠道来源',
  `f_score` float(2,1) DEFAULT NULL COMMENT '整租默认分',
  `f_rscore` float(2,1) DEFAULT NULL COMMENT '分租默认分',
  `f_status` tinyint(1) DEFAULT '1' COMMENT '状态：1，有效 0 无效',
  `f_creation_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_last_change_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
---分项分记录表--
CREATE TABLE `t_house_rank_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_house_sell_id` varchar(64) NOT NULL COMMENT '房源ID',
  `f_room_id` int(11) NOT NULL COMMENT '房间ID',
  `f_source_score` float(4,2) DEFAULT '0.00' COMMENT '来源得分',
  `f_pic_score` float(4,2) DEFAULT '0.00' COMMENT '图片得分',
  `f_base_info_score` float(4,2) DEFAULT '0.00' COMMENT '基本信息得分',
  `f_subway_score` float(4,2) DEFAULT '0.00' COMMENT '地铁得分',
  `f_img_deco_score` float(4,2) DEFAULT '0.00' COMMENT '装修度',
  `f_img_repeat_score` float(4,2) DEFAULT '0.00' COMMENT '重复度',
  `f_img_shooting_score` float(4,2) DEFAULT '0.00' COMMENT '拍摄度',
  `f_img_cover_score` float(4,2) DEFAULT '0.00' COMMENT '覆盖度',
  `f_img_total_score` float(4,2) DEFAULT '0.00' COMMENT '图片总分',
  `f_creation_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_last_change_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `index_sellId_and_roomId` (`f_house_sell_id`,`f_room_id`) USING BTREE,
  KEY `index_room_id` (`f_room_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

--增加随机分数字段-
ALTER TABLE `t_house_rank_score` add COLUMN `f_random_score` float(4,3) DEFAULT '0.000' COMMENT '随机分数' after `f_subway_score`;

--v1和v6合并 增加saas标识
ALTER TABLE `db_huizhaofang_platform`.`t_platform_customer` ADD COLUMN `f_is_saas` int(1) NULL DEFAULT 0 COMMENT '是否saas 0否  1是' AFTER `f_permission_status`;
ALTER TABLE `db_huizhaofang_platform`.`t_house_detail_extend` ADD COLUMN `f_is_saas` int(1) NULL DEFAULT 0 COMMENT '公司名称' AFTER `f_transfer_flag`;

--添加dhash字段
ALTER TABLE `t_house_pics` add COLUMN `f_pic_dhash` varchar(20) NOT NULL DEFAULT '' COMMENT '差异值哈希' after `f_pic_type`;

--理想生活圈模块可配置
ALTER TABLE `t_custom_city_price` add COLUMN `f_exclude_model_id` varchar(30)  DEFAULT '' COMMENT '不包含的模块ID（理想生活圈不展示哪个模块，多模块以逗号分隔' after `f_limit_price`;

--城市表增加是否有地铁字段
ALTER TABLE `t_city` ADD COLUMN `has_subway` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否有地铁，1 有， 0无' AFTER `sort`;

--通话记录表
CREATE TABLE `db_huizhaofang_platform`.`t_call_record` (
  `f_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_out_id` varchar(50) NOT NULL DEFAULT '' COMMENT '唯一业务编号',
  `f_call_id` varchar(200) DEFAULT '' COMMENT '打电话的唯一ID',
  `f_custom_phone` varchar(20) DEFAULT '' COMMENT '客户电话',
  `f_call_time` varchar(255) DEFAULT '' COMMENT '返回的电话话时间',
  `f_release_dir` tinyint(1) DEFAULT NULL COMMENT '释放方向 1 主叫挂机， 2 被叫挂机（房屋经纪人）',
  `f_call_duration` int(10) DEFAULT '0' COMMENT '通话时长 单位 秒',
  `f_voice_record_url` varchar(255) DEFAULT '' COMMENT '录音地址',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--绑定记录
CREATE TABLE `db_huizhaofang_platform`.`t_bind_record` (
  `f_id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_house_sell_id` varchar(20) NOT NULL DEFAULT '' COMMENT '房源编号',
  `f_room_id` int(11) NOT NULL COMMENT '房间ID',
  `f_agency_phone` varchar(20) NOT NULL DEFAULT '' COMMENT '中介电话',
  `f_secret_no` varchar(20) DEFAULT '' COMMENT '分配的虚拟号',
  `f_sub_id` varchar(10) DEFAULT '' COMMENT '阿里返回的绑定关系ID',
  `f_out_id` varchar(50) NOT NULL DEFAULT '' COMMENT '唯一业务编号',
  `f_assign_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '虚拟号分配状态： 0：已分配， 1 号码不支持转接， 2 没有可用的虚拟号，3 虚拟号绑定失败',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--虚拟号表
CREATE TABLE `t_secret_phone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `f_secret_phone_no` varchar(15) NOT NULL DEFAULT '' COMMENT '虚拟号码',
  `f_bind_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '绑定状态：0 未绑定，1 已绑定',
  `f_status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '号码状态：1 可用，0 不可用',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

--商圈地铁版本信息报
CREATE TABLE `t_city_info_version` (
  `f_id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `f_type_desc` varchar(20) NOT NULL DEFAULT '' COMMENT '类别描述',
  `f_info_type` tinyint(4) NOT NULL COMMENT '1:地铁信息;2:商圈信息',
  `f_version` int(5) NOT NULL COMMENT '版本号',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

--iphoneX 图片链接
ALTER TABLE `db_huizhaofang_platform`.`t_slide_show_url` ADD COLUMN `f_px_image_url` varchar(255) NOT NULL DEFAULT '' COMMENT 'iphoneX 图片链接' AFTER `f_image_url`;
ALTER TABLE `db_huizhaofang_platform`.`t_agency_manage` ADD COLUMN `f_px_pic_path` varchar(255) DEFAULT '' COMMENT 'iphoneX 图片链接' AFTER `f_destination_url`;

--房源操作记录表
CREATE TABLE `db_huizhaofang_platform`.`t_house_operate_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `f_house_sell_id` varchar(30) NOT NULL COMMENT '房源编号',
  `f_room_id` int(11) NOT NULL COMMENT '房间ID',
  `f_company_id` varchar(50) DEFAULT '' COMMENT '中介公司ID',
  `f_company_name` varchar(255) DEFAULT '' COMMENT '中介公司名称',
  `f_entire_rent` tinyint(2) DEFAULT NULL COMMENT '租住类型（0：分租；1：整租；2：整分皆可）',
  `f_opt_type` tinyint(2) DEFAULT NULL COMMENT '操作类型（1：新增房源；2：更新房源；3：删除房源；4：新增房间；5：更新房间：6：删除房间）',
  `f_house_status` tinyint(2) DEFAULT NULL COMMENT '房态 1待租 5已租 0未上架',
  `f_approve_status` tinyint(2) DEFAULT NULL,
  `f_baidu_lo` varchar(30) DEFAULT '' COMMENT '百度坐标，经度',
  `f_baidu_la` varchar(255) DEFAULT '' COMMENT '百度坐标，纬度',
  `f_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `f_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;