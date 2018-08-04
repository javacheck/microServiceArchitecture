DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account` (
  `id` bigint(20) NOT NULL,
  `mobile` varchar(11) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sex` int(11) DEFAULT '0' COMMENT '0男，1女',
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `idCard` varchar(255) DEFAULT NULL COMMENT '身份证',
  `createdTime` datetime DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL COMMENT '创建者id，null表示管理员',
  `path` varchar(255) DEFAULT NULL COMMENT 'id连接',
  `storeId` bigint(20) DEFAULT NULL,
  `agentId` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_account_log`;
CREATE TABLE `t_account_log` (
  `accountId` bigint(20) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `parameter` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`accountId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_account_role`;
CREATE TABLE `t_account_role` (
  `accountId` bigint(20) NOT NULL,
  `roleId` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity` (
  `id` bigint(20) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL COMMENT '开始时间',
  `endDate` datetime DEFAULT NULL COMMENT '结束时间',
  `imageId` varchar(64) DEFAULT NULL COMMENT '图片id',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `createdTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1 正常，0无效',
  `areaId` bigint(20) DEFAULT NULL COMMENT 't_aread表id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_activity_detail`;
CREATE TABLE `t_activity_detail` (
  `id` bigint(20) NOT NULL,
  `activityId` bigint(20) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `areaId` bigint(20) DEFAULT NULL,
  `placeName` varchar(20) DEFAULT NULL COMMENT '小区名称',
  `imageId` varchar(64) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1有效，0无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_activity_detail_location`;
CREATE TABLE `t_activity_detail_location` (
  `id` bigint(20) NOT NULL,
  `activityDetailId` bigint(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `shopName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_activity_partake`;
CREATE TABLE `t_activity_partake` (
  `id` bigint(20) NOT NULL,
  `activityDetailId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_activity_result`;
CREATE TABLE `t_activity_result` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `locationId` bigint(20) DEFAULT NULL COMMENT '活动详情藏宝点id',
  `userId` bigint(20) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '状态 0 表示审核中 , 1表示审核通过，2表示审核不通过',
  `accountId` bigint(20) DEFAULT NULL,
  `imageId` varchar(64) DEFAULT NULL COMMENT '上传的图片id',
  `partakeId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_activity_statistics`;
CREATE TABLE `t_activity_statistics` (
  `id` bigint(20) DEFAULT NULL COMMENT '表记录ID',
  `userId` bigint(20) DEFAULT NULL COMMENT '会员ID',
  `mobile` varchar(13) DEFAULT NULL COMMENT '会员手机号',
  `allTreasurePoint` int(11) DEFAULT NULL COMMENT '寻宝点',
  `time` varchar(20) DEFAULT NULL COMMENT '用时',
  `ranklist` int(11) DEFAULT NULL COMMENT '排行'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_agent`;
CREATE TABLE `t_agent` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `createdId` bigint(20) DEFAULT NULL,
  `updatedTime` datetime DEFAULT NULL,
  `updatedId` bigint(20) DEFAULT NULL,
  `type` int(1) DEFAULT NULL COMMENT '代理商类型(1总代理商，2运营商，3子公司。默认是1)',
  `mobile` varchar(255) DEFAULT NULL,
  `contactName` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `areaId` bigint(20) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL COMMENT '// 上级代理，总代理没有上级代理',
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_apk`;
CREATE TABLE `t_apk` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `version` varchar(11) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `fileId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_area`;
CREATE TABLE `t_area` (
  `id` bigint(20) NOT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `depth` int(11) DEFAULT NULL COMMENT '2表示城市',
  `spelling` varchar(50) DEFAULT NULL COMMENT '拼音',
  `fullName` varchar(255) DEFAULT NULL,
  `shortSpelling` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全国共有34个省市： \r\n省(23个) 辽宁省（辽） 吉林省（吉） 黑龙江省（黑）河北省（冀） 山西省（晋） 江苏省（苏） 浙江省（浙） 安徽省（皖) 福建省（闽） 江西省（赣） 山东省（鲁） 河南省（豫） 湖北省（鄂） 湖南省（湘） 广东省（粤） 海南省（琼） 四川省（川、蜀） 贵州省（黔、贵） 云南省（滇、云） 陕西省（陕、秦） 甘肃省（甘、陇） 青海省（青） 台湾省（台）  \r\n直辖市(4个)北京市（京）天津市（津）上海市（沪）重庆市（渝）  \r\n自治区(5个)广西壮族自治区（桂）内蒙古自治区（蒙）西藏自治区（藏）宁夏回族自治区（宁）新疆维吾尔自治区（疆）  \r\n特别行政区(2个) 香港特别行政区（港）澳门特别行政区（澳）\r\n\r\n\r\n------现在数据表中，暂没有台湾 - 香港 - 澳门';

DROP TABLE IF EXISTS `t_bank`;
CREATE TABLE `t_bank` (
  `id` int(11) NOT NULL,
  `code` varchar(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_booking`;
CREATE TABLE `t_booking` (
  `id` bigint(32) NOT NULL COMMENT 'ID',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `publishId` bigint(32) DEFAULT NULL COMMENT '发布ID',
  `userId` bigint(32) DEFAULT NULL COMMENT '会员ID',
  `publishTimeId` bigint(32) DEFAULT NULL COMMENT '预约时间ID',
  `confirmTime` datetime DEFAULT NULL COMMENT '确认时间',
  `status` int(11) DEFAULT NULL COMMENT '0表示确认中(这个时候publish的可预约数-1)，1表示已经确认，2表示取消(这个时候publish的可预约数+1)，3表示交易完成',
  `payStatus` int(11) DEFAULT NULL COMMENT '线上支付状态',
  `memo` varchar(255) DEFAULT NULL,
  `reply` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `serviceCompletionTime` datetime DEFAULT NULL,
  `orderCompleteTime` datetime DEFAULT NULL,
  `cancelTime` datetime DEFAULT NULL,
  `cancelUserId` int(255) DEFAULT NULL,
  `cancelReason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_booking_message`;
CREATE TABLE `t_booking_message` (
  `id` bigint(20) NOT NULL,
  `bookingId` bigint(20) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `message` varchar(200) DEFAULT NULL,
  `type` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_business_bank`;
CREATE TABLE `t_business_bank` (
  `id` bigint(20) NOT NULL,
  `businessId` bigint(20) DEFAULT NULL COMMENT '商户id，代理商id',
  `type` int(11) DEFAULT NULL COMMENT '0表示商家，1表示代理商，2用户',
  `bankName` varchar(50) DEFAULT NULL COMMENT '银行名称',
  `accountNumber` varchar(50) DEFAULT NULL COMMENT '帐号名称',
  `accountName` varchar(50) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `isDefault` int(11) DEFAULT NULL COMMENT '1是默认帐号，0表示不是，只能有一个默认',
  `createdTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` bigint(11) NOT NULL,
  `deviceSn` varchar(255) NOT NULL,
  `deviceName` varchar(255) DEFAULT NULL COMMENT 'sn号',
  `storeId` bigint(11) DEFAULT NULL,
  `model` varchar(255) DEFAULT NULL COMMENT '型号',
  `status` int(11) DEFAULT NULL COMMENT '1有效，0无效',
  `factory` varchar(255) DEFAULT NULL COMMENT '厂家',
  `serialId` varchar(30) DEFAULT NULL COMMENT '终端系统的唯一标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_id`;
CREATE TABLE `t_id` (
  `value` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_identifyType`;
CREATE TABLE `t_identifyType` (
  `identifyTypeId` bigint(20) NOT NULL,
  `identifyTypeName` varchar(255) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_lbs_address`;
CREATE TABLE `t_lbs_address` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `userId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `createdTime` datetime DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  `requestUrl` varchar(255) DEFAULT NULL,
  `parameters` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_login`;
CREATE TABLE `t_login` (
  `loginName` varchar(255) DEFAULT NULL,
  `loginTime` datetime DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_login_record`;
CREATE TABLE `t_login_record` (
  `token` varchar(64) NOT NULL COMMENT '主键',
  `type` int(11) DEFAULT NULL COMMENT '0表示商家，1表示用户',
  `ownerId` bigint(20) DEFAULT NULL COMMENT '商家accountId 或者用户id',
  `createTime` datetime DEFAULT NULL COMMENT '保存时间',
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='api那个项目，用户登录或者商家登录的时候，成功了，就把这些信息记录到这个表';

DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `messageId` bigint(20) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `userId` bigint(20) NOT NULL,
  `readed` varchar(1) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`messageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` bigint(20) NOT NULL COMMENT '序号',
  `accountId` bigint(20) DEFAULT NULL COMMENT '账户ID',
  `storeId` bigint(20) DEFAULT NULL COMMENT '商店ID',
  `price` decimal(20,4) DEFAULT NULL COMMENT '价格',
  `createdTime` datetime DEFAULT NULL COMMENT '订单时间',
  `userId` bigint(20) DEFAULT NULL COMMENT '会员ID',
  `status` int(20) DEFAULT '0' COMMENT '订单状态 0待付款 1已支付 2商家取消 3已完成,4待签收,5待发货 ,6商家已确认,7退换货，8待评价，9缺货,10用户取消 99交易关闭',
  `paymentMode` int(10) DEFAULT NULL COMMENT '支付方式 0 支付宝 1 微信支付 2 刷卡 3  现金，4银联在线,7货到付款',
  `discount` decimal(10,2) DEFAULT '1.00' COMMENT '折扣',
  `actualPrice` decimal(20,4) DEFAULT NULL COMMENT '实际支付价格',
  `source` int(20) DEFAULT NULL COMMENT '订单来源',
  `addressId` bigint(20) DEFAULT NULL COMMENT '收货地址id',
  `message` varchar(255) DEFAULT NULL COMMENT '订单留言',
  `settlement` int(11) DEFAULT '0' COMMENT '是否结算 0 未，1结算',
  `evaluate` varchar(255) DEFAULT NULL COMMENT '评价',
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_order_handle`;
CREATE TABLE `t_order_handle` (
  `orderId` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `status` int(11) DEFAULT NULL COMMENT '订单状态',
  `handleTime` datetime DEFAULT NULL COMMENT '创建时间',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item` (
  `id` bigint(20) DEFAULT NULL,
  `orderId` bigint(20) DEFAULT NULL,
  `stockId` bigint(20) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `categoryId` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `discount` decimal(10,2) DEFAULT '1.00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_pay_account`;
CREATE TABLE `t_pay_account` (
  `id` bigint(20) NOT NULL,
  `ownerId` bigint(20) DEFAULT NULL COMMENT '商户id，或者代理商id，用户id',
  `type` int(11) DEFAULT NULL COMMENT '0商户，1代理商,2用户',
  `password` varchar(64) DEFAULT NULL COMMENT '支付密码',
  `createdTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0未激活，1正常，2冻结，3销户，4挂失，5锁定',
  `balance` double DEFAULT '0' COMMENT '可提余额',
  `frozenAmount` double DEFAULT '0' COMMENT '冻结金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_pay_record`;
CREATE TABLE `t_pay_record` (
  `orderId` bigint(20) NOT NULL,
  `queryId` varchar(255) DEFAULT NULL COMMENT '由银联返回，用于在后续类交易中唯一标识一笔交易',
  `respCode` varchar(255) DEFAULT NULL COMMENT '响应代码 银联1000010,1000011表示成功',
  `respMsg` varchar(255) DEFAULT NULL COMMENT '响应信息',
  `settleDate` varchar(255) DEFAULT NULL COMMENT '清算日期',
  `traceNo` varchar(255) DEFAULT NULL COMMENT '系统跟踪号',
  `traceTime` varchar(255) DEFAULT NULL COMMENT '交易传输时间',
  `txnAmt` int(11) DEFAULT NULL COMMENT '交易金额 单位为分',
  `txnTime` varchar(255) DEFAULT NULL COMMENT '订单发送时间',
  `channel` int(11) DEFAULT NULL COMMENT '1表示银联，2微信',
  `status` int(11) DEFAULT NULL COMMENT '1成功，2失败',
  PRIMARY KEY (`orderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_paymentMode_info`;
CREATE TABLE `t_paymentMode_info` (
  `id` bigint(20) NOT NULL,
  `associationId` bigint(20) DEFAULT NULL,
  `paymentMode` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  `price` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` bigint(20) NOT NULL,
  `operator` varchar(255) DEFAULT NULL COMMENT '操作',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `url` varchar(255) DEFAULT NULL,
  `categoryName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `categoryId` bigint(20) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  `storeId` bigint(20) DEFAULT NULL COMMENT '商店ID',
  `type` int(2) DEFAULT NULL COMMENT '是否有属性',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_product_attribute`;
CREATE TABLE `t_product_attribute` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `categoryId` bigint(20) DEFAULT NULL COMMENT '分类ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_product_attribute_value`;
CREATE TABLE `t_product_attribute_value` (
  `id` bigint(20) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `attributeId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_product_category`;
CREATE TABLE `t_product_category` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pId` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `storeId` bigint(20) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL COMMENT '父节点，子节点id连接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_product_image`;
CREATE TABLE `t_product_image` (
  `id` varchar(50) DEFAULT NULL COMMENT '图片ID',
  `productStockId` bigint(20) DEFAULT NULL COMMENT '库存id',
  `suffix` varchar(255) DEFAULT NULL COMMENT '图片后缀',
  `sort` int(255) DEFAULT NULL COMMENT '图片排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_product_stock`;
CREATE TABLE `t_product_stock` (
  `id` bigint(20) NOT NULL,
  `stock` int(255) DEFAULT NULL,
  `alarmValue` int(255) DEFAULT NULL,
  `productId` bigint(20) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  `storeId` bigint(20) DEFAULT NULL,
  `attributeCode` varchar(255) DEFAULT NULL,
  `price` double(20,2) DEFAULT NULL,
  `barCode` varchar(255) DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `standard` varchar(255) DEFAULT NULL,
  `sales` bigint(20) DEFAULT '0' COMMENT '销售量',
  `hits` bigint(20) DEFAULT NULL COMMENT '点击量',
  `createTime` datetime DEFAULT NULL,
  `createId` bigint(20) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `updateId` bigint(20) DEFAULT NULL,
  `recommended` int(11) DEFAULT NULL COMMENT '推荐情况： 0不推荐，1推荐',
  `shelves` int(11) DEFAULT NULL COMMENT '上架情况： 0上架 ，1 下架',
  `type` int(1) DEFAULT '0' COMMENT '0表示有属性 1表示无属性',
  `marketPrice` double(20,0) DEFAULT NULL COMMENT '市场价格',
  `costPrice` double(20,0) DEFAULT NULL COMMENT '成本价格',
  `details` text COMMENT '详情说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_publish`;
CREATE TABLE `t_publish` (
  `id` bigint(64) NOT NULL,
  `createdtime` datetime DEFAULT NULL,
  `keywords` varchar(200) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `price` double(20,2) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `payType` int(11) DEFAULT NULL,
  `paystatus` int(11) DEFAULT NULL,
  `viewnumber` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '// 0 待审，1 已审 ，2 取消',
  `userId` bigint(64) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `range` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `categoryId` bigint(32) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `bookingType` int(11) DEFAULT NULL,
  `bookedNumber` int(11) DEFAULT NULL,
  `longitude` double DEFAULT NULL COMMENT '经度',
  `latitude` double DEFAULT NULL COMMENT '纬度',
  `userName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_publish_category`;
CREATE TABLE `t_publish_category` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(100) DEFAULT NULL COMMENT '发布分类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_publish_image`;
CREATE TABLE `t_publish_image` (
  `publishId` bigint(32) DEFAULT NULL COMMENT '发布ID',
  `imageId` varchar(64) DEFAULT NULL COMMENT '图片ID',
  `suffix` varchar(32) DEFAULT NULL COMMENT '后缀',
  `orderSort` int(11) DEFAULT NULL COMMENT '排序',
  `type` int(11) DEFAULT NULL COMMENT '图片类型1为描述图片2为证书图片'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_publish_time`;
CREATE TABLE `t_publish_time` (
  `id` bigint(32) DEFAULT NULL COMMENT 'ID',
  `startDate` datetime DEFAULT NULL COMMENT '开始时间',
  `endDate` datetime DEFAULT NULL COMMENT '结束时间',
  `publishId` bigint(32) DEFAULT NULL COMMENT '发布ID',
  `bookingNumber` int(11) DEFAULT NULL COMMENT '可以预约次数',
  `bookedNumber` int(11) DEFAULT NULL COMMENT '已经预约次数',
  `orderSort` int(11) DEFAULT NULL COMMENT '排序'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_report`;
CREATE TABLE `t_report` (
  `id` bigint(20) NOT NULL,
  `typeId` int(11) DEFAULT NULL COMMENT '举报类型id',
  `content` varchar(255) DEFAULT NULL COMMENT '举报内容',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系方式，手机，邮箱，qq等',
  `userId` bigint(20) DEFAULT NULL COMMENT '用户登录的情况，就取userid,不登录为空',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_report_type`;
CREATE TABLE `t_report_type` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` bigint(11) DEFAULT NULL COMMENT '-1自定义，其他表示默认，值越大级别越大',
  `createdId` bigint(20) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `roleId` bigint(20) NOT NULL,
  `permissionId` bigint(20) NOT NULL,
  PRIMARY KEY (`roleId`,`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_settlements_record`;
CREATE TABLE `t_settlements_record` (
  `id` bigint(20) NOT NULL,
  `orderId` bigint(20) DEFAULT NULL,
  `ownerId` bigint(20) DEFAULT NULL COMMENT '商户id，或者代理商id',
  `type` int(11) DEFAULT NULL COMMENT '0商户，1代理商',
  `amount` double DEFAULT NULL COMMENT '金额',
  `createdTime` datetime DEFAULT NULL,
  `balance` double DEFAULT NULL COMMENT '余额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_shift_work_log`;
CREATE TABLE `t_shift_work_log` (
  `id` bigint(20) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL,
  `beginTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_shop_type`;
CREATE TABLE `t_shop_type` (
  `id` bigint(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `parentId` bigint(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_store`;
CREATE TABLE `t_store` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '商家名称',
  `companyName` varchar(255) DEFAULT NULL COMMENT '公司名称',
  `createdTime` datetime DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `accountId` bigint(20) DEFAULT NULL COMMENT '创建者',
  `shopTypeId` bigint(11) DEFAULT NULL COMMENT '商店类型Id',
  `agentId` bigint(11) DEFAULT NULL COMMENT '代理商id',
  `areaId` int(11) DEFAULT NULL COMMENT '地区id',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `updatedTime` datetime DEFAULT NULL,
  `updatedAccountId` bigint(20) DEFAULT NULL COMMENT '更新帐号id',
  `logo` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '简介',
  `longitude` double DEFAULT NULL COMMENT '经度',
  `latitude` double DEFAULT NULL COMMENT '纬度',
  `payType` int(11) DEFAULT NULL COMMENT '0在线支付，1货到付款，2两种方式',
  `merchantName` varchar(255) DEFAULT NULL COMMENT 'post商户名称',
  `merchantNo` varchar(255) DEFAULT NULL COMMENT 'post商户号',
  `mcc` double DEFAULT NULL COMMENT '标准签约费率',
  `mccSign` double DEFAULT NULL COMMENT '签约费率',
  `mccCost` double DEFAULT NULL COMMENT '收单成本费率',
  `status` int(11) DEFAULT NULL COMMENT '营业状态1营业，0休息中',
  `posBankDeposit` varchar(255) DEFAULT NULL COMMENT '收单开户行',
  `posAccountName` varchar(255) DEFAULT NULL COMMENT '收单帐号名',
  `posBankLine` varchar(255) DEFAULT NULL COMMENT '收单银行行号',
  `posBankAccount` varchar(255) DEFAULT NULL COMMENT '收单银行账户',
  `contact` varchar(255) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `businessTime` varchar(255) DEFAULT NULL COMMENT '营业时间',
  `minAmount` double DEFAULT NULL COMMENT '起送金额',
  `shipRange` varchar(255) DEFAULT NULL COMMENT '配送范围',
  `shipTime` varchar(255) DEFAULT NULL COMMENT '送达时间',
  `shipAmount` double DEFAULT NULL COMMENT '送货费用，默认为0元',
  `service` varchar(255) DEFAULT NULL COMMENT '服务(免费取件及送货上门)',
  `shipType` varchar(11) DEFAULT NULL COMMENT '送货方式：0：店铺送货上门(默认)，1：到店自提，2：快递，3：第三方配送',
  `ratingStar` double(11,0) DEFAULT NULL,
  `signedType` bigint(20) DEFAULT NULL COMMENT '签约类型(暂规定为0,餐饮娱乐，1,一般类，2,民生类)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_sys_config`;
CREATE TABLE `t_sys_config` (
  `name` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL,
  `mobile` varchar(50) DEFAULT NULL COMMENT '电话',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `sex` int(10) DEFAULT NULL COMMENT '性别',
  `discount` double DEFAULT NULL COMMENT '折扣',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `createdId` bigint(20) DEFAULT NULL COMMENT '创建者',
  `storeId` bigint(20) DEFAULT NULL COMMENT '商店id',
  `password` varchar(64) DEFAULT NULL,
  `identifyTypeId` int(2) DEFAULT NULL COMMENT '身份证明类型代码',
  `identity` varchar(18) DEFAULT NULL COMMENT '证件号码',
  `nickName` varchar(255) DEFAULT NULL COMMENT '昵称',
  `iconUrl` varchar(255) DEFAULT NULL COMMENT '头像id',
  `phoneNumber` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  `realName` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `areaId` bigint(20) DEFAULT NULL COMMENT '地址URLID',
  `idAudit` int(11) DEFAULT NULL COMMENT '身份审核状态0审核中，1审核通过，2审核失败',
  `cid` varchar(255) DEFAULT NULL COMMENT '推送对应的id',
  `recommended` varchar(255) DEFAULT NULL COMMENT '推荐人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_user_address`;
CREATE TABLE `t_user_address` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `isDefault` int(11) DEFAULT NULL COMMENT '1默认，0不是',
  `userId` bigint(20) DEFAULT NULL,
  `areaId` bigint(20) DEFAULT NULL COMMENT '省市区ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_user_bank`;
CREATE TABLE `t_user_bank` (
  `bankId` bigint(20) NOT NULL COMMENT '银行卡ID',
  `cardNumber` varchar(50) DEFAULT NULL COMMENT '持卡号',
  `cardHolder` varchar(50) DEFAULT NULL COMMENT '持卡人',
  `userId` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `inUse` int(2) DEFAULT NULL COMMENT '正在使用中0表示不是当前使用中1表示是当前使用中',
  PRIMARY KEY (`bankId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_user_cer`;
CREATE TABLE `t_user_cer` (
  `cerId` varchar(255) NOT NULL COMMENT '证书ID',
  `cerName` varchar(255) DEFAULT NULL COMMENT '证书名称',
  `userId` bigint(20) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`cerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_user_publish_count`;
CREATE TABLE `t_user_publish_count` (
  `userId` bigint(20) NOT NULL,
  `serviceTimes` int(11) DEFAULT NULL,
  `publishTimes` int(11) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `t_withdraw`;
CREATE TABLE `t_withdraw` (
  `id` bigint(20) NOT NULL,
  `createdTime` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '0商户，1代理商,2用户',
  `ownerId` bigint(20) DEFAULT NULL COMMENT '商户id，或者代理商id，用户id',
  `amount` double DEFAULT NULL COMMENT '金额',
  `accountId` bigint(20) DEFAULT NULL COMMENT '代理商或者商家的accountId，如果是用户null值',
  `bankName` varchar(255) DEFAULT NULL COMMENT '银行名称',
  `bankAccountNumber` varchar(255) DEFAULT NULL COMMENT '银行帐号',
  `bankAccountName` varchar(255) DEFAULT NULL COMMENT '帐号名称',
  `status` int(11) DEFAULT NULL COMMENT '状态 0 处理中 1 成功 2 失败 3 审核通过 4 审核失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_work_record`;
CREATE TABLE `t_work_record` (
  `id` bigint(11) NOT NULL,
  `accountId` bigint(11) DEFAULT NULL,
  `storeId` bigint(11) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `sales` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



INSERT INTO `t_account` VALUES ('1', 'admin', '3XJ23+BBhVLK3SNJmWnVZl5VdSyPYU/MODjnO8SD/CeOhP6TmiF3R0T+ZuceW8Oi', '管理员', '1', 'lxl@qq.com', '广州市天河区', '440981198204182235', '2015-05-06 16:08:35', null, '1', null, null, '1');

INSERT INTO `t_account_role` VALUES ('1', '1');


INSERT INTO `t_area` VALUES ('2', null, '北京市', '2', '1', 'Bei Jing Shi', '北京市', 'BJ');
INSERT INTO `t_area` VALUES ('3', null, '天津市', '3', '1', 'Tian Jin Shi', '天津市', 'TJ');
INSERT INTO `t_area` VALUES ('4', null, '河北省', '4', '1', 'He Bei Sheng', '河北省', 'HE');
INSERT INTO `t_area` VALUES ('5', null, '山西省', '5', '1', 'Shan Xi Sheng ', '山西省', 'SX');
INSERT INTO `t_area` VALUES ('6', null, '内蒙古自治区', '6', '1', 'Nei Meng Gu Zi Zhi Qu', '内蒙古自治区', 'NM');
INSERT INTO `t_area` VALUES ('7', null, '辽宁省', '7', '1', 'Liao Ning Sheng', '辽宁省', 'LN');
INSERT INTO `t_area` VALUES ('8', null, '吉林省', '8', '1', 'Ji Lin Sheng', '吉林省', 'JL');
INSERT INTO `t_area` VALUES ('9', null, '黑龙江省', '9', '1', 'Hei Long Jiang Sheng', '黑龙江省', 'HL');
INSERT INTO `t_area` VALUES ('10', null, '上海市', '10', '1', 'Shang Hai Shi', '上海市', 'SH');
INSERT INTO `t_area` VALUES ('11', null, '江苏省', '11', '1', 'Jiang Su Sheng', '江苏省', 'JS');
INSERT INTO `t_area` VALUES ('12', null, '浙江省', '12', '1', 'Zhe Jiang Sheng', '浙江省', 'ZJ');
INSERT INTO `t_area` VALUES ('13', null, '安徽省', '13', '1', 'An Hui Sheng', '安徽省', 'AH');
INSERT INTO `t_area` VALUES ('14', null, '福建省', '14', '1', 'Fu Jian Sheng ', '福建省', 'FJ');
INSERT INTO `t_area` VALUES ('15', null, '江西省', '15', '1', 'Jiang Xi Sheng', '江西省', 'JX');
INSERT INTO `t_area` VALUES ('16', null, '山东省', '16', '1', 'Shan Dong Sheng ', '山东省', 'SD');
INSERT INTO `t_area` VALUES ('17', null, '河南省', '17', '1', 'He Nan Sheng', '河南省', 'HA');
INSERT INTO `t_area` VALUES ('18', null, '湖北省', '18', '1', 'Hu Bei Sheng', '湖北省', 'HB');
INSERT INTO `t_area` VALUES ('19', null, '湖南省', '19', '1', 'Hu Nan Sheng', '湖南省', 'HN');
INSERT INTO `t_area` VALUES ('20', null, '广东省', '20', '1', 'Guang Dong Sheng', '广东省', 'GD');
INSERT INTO `t_area` VALUES ('21', null, '广西壮族自治区', '21', '1', 'Guang Xi Zhuang Zu Zi Zhi Qu', '广西壮族自治区', 'GX');
INSERT INTO `t_area` VALUES ('22', null, '海南省', '22', '1', 'Hai Nan Sheng', '海南省', 'HI');
INSERT INTO `t_area` VALUES ('23', null, '重庆市', '23', '1', 'Chong Qing Shi', '重庆市', 'CQ');
INSERT INTO `t_area` VALUES ('24', null, '四川省', '24', '1', 'Si Chuan Sheng', '四川省', 'SC');
INSERT INTO `t_area` VALUES ('25', null, '贵州省', '25', '1', 'Gui Zhou Sheng', '贵州省', 'GZ');
INSERT INTO `t_area` VALUES ('26', null, '云南省', '26', '1', 'Yun Nan Sheng', '云南省', 'YN');
INSERT INTO `t_area` VALUES ('27', null, '西藏自治区', '27', '1', 'Xi Zang Zi Zhi Qu', '西藏自治区', 'XZ');
INSERT INTO `t_area` VALUES ('28', null, '陕西省', '28', '1', 'Shan Xi Sheng ', '陕西省', 'SN');
INSERT INTO `t_area` VALUES ('29', null, '甘肃省', '29', '1', 'Gan Su Sheng', '甘肃省', 'GS');
INSERT INTO `t_area` VALUES ('30', null, '青海省', '30', '1', 'Qing Hai Sheng', '青海省', 'QH');
INSERT INTO `t_area` VALUES ('31', null, '宁夏回族自治区', '31', '1', 'Ning Xia Hui Zu Zi Zhi Qu', '宁夏回族自治区', 'NX');
INSERT INTO `t_area` VALUES ('32', null, '新疆维吾尔自治区', '32', '1', 'Xin Jiang Wei Wu Er Zi Zhi Qu', '新疆维吾尔自治区', 'XJ');
INSERT INTO `t_area` VALUES ('37', '4', '石家庄市', '4-37', '2', 'Shijiazhuang Shi', '河北省石家庄市', 'SJZ');
INSERT INTO `t_area` VALUES ('38', '4', '唐山市', '4-38', '2', 'Tangshan Shi', '河北省唐山市', 'TGS');
INSERT INTO `t_area` VALUES ('39', '4', '秦皇岛市', '4-39', '2', 'Qinhuangdao Shi', '河北省秦皇岛市', 'SHP');
INSERT INTO `t_area` VALUES ('40', '4', '邯郸市', '4-40', '2', 'Handan Shi', '河北省邯郸市', 'HDS');
INSERT INTO `t_area` VALUES ('41', '4', '邢台市', '4-41', '2', 'Xingtai Shi', '河北省邢台市', 'XTS');
INSERT INTO `t_area` VALUES ('42', '4', '保定市', '4-42', '2', 'Baoding Shi', '河北省保定市', 'BDS');
INSERT INTO `t_area` VALUES ('43', '4', '张家口市', '4-43', '2', 'Zhangjiakou Shi ', '河北省张家口市', 'ZJK');
INSERT INTO `t_area` VALUES ('44', '4', '承德市', '4-44', '2', 'Chengde Shi', '河北省承德市', 'CDS');
INSERT INTO `t_area` VALUES ('45', '4', '沧州市', '4-45', '2', 'Cangzhou Shi', '河北省沧州市', 'CGZ');
INSERT INTO `t_area` VALUES ('46', '4', '廊坊市', '4-46', '2', 'Langfang Shi', '河北省廊坊市', 'LFS');
INSERT INTO `t_area` VALUES ('47', '4', '衡水市', '4-47', '2', 'Hengshui Shi ', '河北省衡水市', 'HGS');
INSERT INTO `t_area` VALUES ('48', '5', '太原市', '5-48', '2', 'Taiyuan Shi', '山西省太原市', 'TYN');
INSERT INTO `t_area` VALUES ('49', '5', '大同市', '5-49', '2', 'Datong Shi ', '山西省大同市', 'DTG');
INSERT INTO `t_area` VALUES ('50', '5', '阳泉市', '5-50', '2', 'Yangquan Shi', '山西省阳泉市', 'YQS');
INSERT INTO `t_area` VALUES ('51', '5', '长治市', '5-51', '2', 'Changzhi Shi', '山西省长治市', 'CZS');
INSERT INTO `t_area` VALUES ('52', '5', '晋城市', '5-52', '2', 'Jincheng Shi ', '山西省晋城市', 'JCG');
INSERT INTO `t_area` VALUES ('53', '5', '朔州市', '5-53', '2', 'Shuozhou Shi ', '山西省朔州市', 'SZJ');
INSERT INTO `t_area` VALUES ('54', '5', '晋中市', '5-54', '2', 'Jinzhong Shi', '山西省晋中市', '2');
INSERT INTO `t_area` VALUES ('55', '5', '运城市', '5-55', '2', 'Yuncheng Shi', '山西省运城市', '2');
INSERT INTO `t_area` VALUES ('56', '5', '忻州市', '5-56', '2', 'Xinzhou Shi', '山西省忻州市', '2');
INSERT INTO `t_area` VALUES ('57', '5', '临汾市', '5-57', '2', 'Linfen Shi', '山西省临汾市', '2');
INSERT INTO `t_area` VALUES ('58', '5', '吕梁市', '5-58', '2', 'Lvliang Shi', '山西省吕梁市', '2');
INSERT INTO `t_area` VALUES ('59', '6', '呼和浩特市', '6-59', '2', 'Hohhot Shi', '内蒙古自治区呼和浩特市', 'Hhht');
INSERT INTO `t_area` VALUES ('60', '6', '包头市', '6-60', '2', 'Baotou Shi ', '内蒙古自治区包头市', 'BTS');
INSERT INTO `t_area` VALUES ('61', '6', '乌海市', '6-61', '2', 'Wuhai Shi', '内蒙古自治区乌海市', 'WHM');
INSERT INTO `t_area` VALUES ('62', '6', '赤峰市', '6-62', '2', 'Chifeng (Ulanhad)Shi', '内蒙古自治区赤峰市', 'CFS');
INSERT INTO `t_area` VALUES ('63', '6', '通辽市', '6-63', '2', 'Tongliao Shi', '内蒙古自治区通辽市', '2');
INSERT INTO `t_area` VALUES ('64', '6', '鄂尔多斯市', '6-64', '2', 'Eerduosi Shi', '内蒙古自治区鄂尔多斯市', '2');
INSERT INTO `t_area` VALUES ('65', '6', '呼伦贝尔市', '6-65', '2', 'Hulunbeier Shi ', '内蒙古自治区呼伦贝尔市', '2');
INSERT INTO `t_area` VALUES ('66', '6', '巴彦淖尔市', '6-66', '2', 'Bayannaoer Shi', '内蒙古自治区巴彦淖尔市', '2');
INSERT INTO `t_area` VALUES ('67', '6', '乌兰察布市', '6-67', '2', 'Wulanchabu Shi', '内蒙古自治区乌兰察布市', '2');
INSERT INTO `t_area` VALUES ('68', '6', '兴安盟', '6-68', '2', 'Hinggan Meng', '内蒙古自治区兴安盟', 'HIN');
INSERT INTO `t_area` VALUES ('69', '6', '锡林郭勒盟', '6-69', '2', 'Xilin Gol Meng', '内蒙古自治区锡林郭勒盟', 'XGO');
INSERT INTO `t_area` VALUES ('70', '6', '阿拉善盟', '6-70', '2', 'Alxa Meng', '内蒙古自治区阿拉善盟', 'ALM');
INSERT INTO `t_area` VALUES ('71', '7', '沈阳市', '7-71', '2', 'Shenyang Shi', '辽宁省沈阳市', 'SHE');
INSERT INTO `t_area` VALUES ('72', '7', '大连市', '7-72', '2', 'Dalian Shi', '辽宁省大连市', 'DLC');
INSERT INTO `t_area` VALUES ('73', '7', '鞍山市', '7-73', '2', 'AnShan Shi', '辽宁省鞍山市', 'ASN');
INSERT INTO `t_area` VALUES ('74', '7', '抚顺市', '7-74', '2', 'Fushun Shi', '辽宁省抚顺市', 'FSN');
INSERT INTO `t_area` VALUES ('75', '7', '本溪市', '7-75', '2', 'Benxi Shi', '辽宁省本溪市', 'BXS');
INSERT INTO `t_area` VALUES ('76', '7', '丹东市', '7-76', '2', 'Dandong Shi', '辽宁省丹东市', 'DDG');
INSERT INTO `t_area` VALUES ('77', '7', '锦州市', '7-77', '2', 'Jinzhou Shi', '辽宁省锦州市', 'JNZ');
INSERT INTO `t_area` VALUES ('78', '7', '营口市', '7-78', '2', 'Yingkou Shi', '辽宁省营口市', 'YIK');
INSERT INTO `t_area` VALUES ('79', '7', '阜新市', '7-79', '2', 'Fuxin Shi', '辽宁省阜新市', 'FXS');
INSERT INTO `t_area` VALUES ('80', '7', '辽阳市', '7-80', '2', 'Liaoyang Shi', '辽宁省辽阳市', 'LYL');
INSERT INTO `t_area` VALUES ('81', '7', '盘锦市', '7-81', '2', 'Panjin Shi', '辽宁省盘锦市', 'PJS');
INSERT INTO `t_area` VALUES ('82', '7', '铁岭市', '7-82', '2', 'Tieling Shi', '辽宁省铁岭市', 'TLS');
INSERT INTO `t_area` VALUES ('83', '7', '朝阳市', '7-83', '2', 'Chaoyang Shi', '辽宁省朝阳市', 'CYS');
INSERT INTO `t_area` VALUES ('84', '7', '葫芦岛市', '7-84', '2', 'Huludao Shi', '辽宁省葫芦岛市', 'HLD');
INSERT INTO `t_area` VALUES ('85', '8', '长春市', '8-85', '2', 'Changchun Shi ', '吉林省长春市', 'CGQ');
INSERT INTO `t_area` VALUES ('86', '8', '吉林市', '8-86', '2', 'Jilin Shi ', '吉林省吉林市', 'JLS');
INSERT INTO `t_area` VALUES ('87', '8', '四平市', '8-87', '2', 'Siping Shi', '吉林省四平市', 'SPS');
INSERT INTO `t_area` VALUES ('88', '8', '辽源市', '8-88', '2', 'Liaoyuan Shi', '吉林省辽源市', 'LYH');
INSERT INTO `t_area` VALUES ('89', '8', '通化市', '8-89', '2', 'Tonghua Shi', '吉林省通化市', 'THS');
INSERT INTO `t_area` VALUES ('90', '8', '白山市', '8-90', '2', 'Baishan Shi', '吉林省白山市', 'BSN');
INSERT INTO `t_area` VALUES ('91', '8', '松原市', '8-91', '2', 'Songyuan Shi', '吉林省松原市', 'SYU');
INSERT INTO `t_area` VALUES ('92', '8', '白城市', '8-92', '2', 'Baicheng Shi', '吉林省白城市', 'BCS');
INSERT INTO `t_area` VALUES ('93', '8', '延边朝鲜族自治州', '8-93', '2', 'Yanbian Chosenzu Zizhizhou', '吉林省延边朝鲜族自治州', 'YBZ');
INSERT INTO `t_area` VALUES ('94', '9', '哈尔滨市', '9-94', '2', 'Harbin Shi', '黑龙江省哈尔滨市', 'HRB');
INSERT INTO `t_area` VALUES ('95', '9', '齐齐哈尔市', '9-95', '2', 'Qiqihar Shi', '黑龙江省齐齐哈尔市', 'NDG');
INSERT INTO `t_area` VALUES ('96', '9', '鸡西市', '9-96', '2', 'Jixi Shi', '黑龙江省鸡西市', 'JXI');
INSERT INTO `t_area` VALUES ('97', '9', '鹤岗市', '9-97', '2', 'Hegang Shi', '黑龙江省鹤岗市', 'HEG');
INSERT INTO `t_area` VALUES ('98', '9', '双鸭山市', '9-98', '2', 'Shuangyashan Shi', '黑龙江省双鸭山市', 'SYS');
INSERT INTO `t_area` VALUES ('99', '9', '大庆市', '9-99', '2', 'Daqing Shi', '黑龙江省大庆市', 'DQG');
INSERT INTO `t_area` VALUES ('100', '9', '伊春市', '9-100', '2', 'Yichun Shi', '黑龙江省伊春市', 'YCH');
INSERT INTO `t_area` VALUES ('101', '9', '佳木斯市', '9-101', '2', 'Jiamusi Shi', '黑龙江省佳木斯市', 'JMU');
INSERT INTO `t_area` VALUES ('102', '9', '七台河市', '9-102', '2', 'Qitaihe Shi', '黑龙江省七台河市', 'QTH');
INSERT INTO `t_area` VALUES ('103', '9', '牡丹江市', '9-103', '2', 'Mudanjiang Shi', '黑龙江省牡丹江市', 'MDG');
INSERT INTO `t_area` VALUES ('104', '9', '黑河市', '9-104', '2', 'Heihe Shi', '黑龙江省黑河市', 'HEK');
INSERT INTO `t_area` VALUES ('105', '9', '绥化市', '9-105', '2', 'Suihua Shi', '黑龙江省绥化市', '2');
INSERT INTO `t_area` VALUES ('106', '9', '大兴安岭地区', '9-106', '2', 'Da Hinggan Ling Diqu', '黑龙江省大兴安岭地区', 'DHL');
INSERT INTO `t_area` VALUES ('109', '11', '南京市', '11-109', '2', 'Nanjing Shi', '江苏省南京市', 'NKG');
INSERT INTO `t_area` VALUES ('110', '11', '无锡市', '11-110', '2', 'Wuxi Shi', '江苏省无锡市', 'WUX');
INSERT INTO `t_area` VALUES ('111', '11', '徐州市', '11-111', '2', 'Xuzhou Shi', '江苏省徐州市', 'XUZ');
INSERT INTO `t_area` VALUES ('112', '11', '常州市', '11-112', '2', 'Changzhou Shi', '江苏省常州市', 'CZX');
INSERT INTO `t_area` VALUES ('113', '11', '苏州市', '11-113', '2', 'Suzhou Shi', '江苏省苏州市', 'SZH');
INSERT INTO `t_area` VALUES ('114', '11', '南通市', '11-114', '2', 'Nantong Shi', '江苏省南通市', 'NTG');
INSERT INTO `t_area` VALUES ('115', '11', '连云港市', '11-115', '2', 'Lianyungang Shi', '江苏省连云港市', 'LYG');
INSERT INTO `t_area` VALUES ('116', '11', '淮安市', '11-116', '2', 'Huai,an Xian', '江苏省淮安市', '2');
INSERT INTO `t_area` VALUES ('117', '11', '盐城市', '11-117', '2', 'Yancheng Shi', '江苏省盐城市', 'YCK');
INSERT INTO `t_area` VALUES ('118', '11', '扬州市', '11-118', '2', 'Yangzhou Shi', '江苏省扬州市', 'YZH');
INSERT INTO `t_area` VALUES ('119', '11', '镇江市', '11-119', '2', 'Zhenjiang Shi', '江苏省镇江市', 'ZHE');
INSERT INTO `t_area` VALUES ('120', '11', '泰州市', '11-120', '2', 'Taizhou Shi', '江苏省泰州市', 'TZS');
INSERT INTO `t_area` VALUES ('121', '11', '宿迁市', '11-121', '2', 'Suqian Shi', '江苏省宿迁市', 'SUQ');
INSERT INTO `t_area` VALUES ('122', '12', '杭州市', '12-122', '2', 'Hangzhou Shi', '浙江省杭州市', 'HGH');
INSERT INTO `t_area` VALUES ('123', '12', '宁波市', '12-123', '2', 'Ningbo Shi', '浙江省宁波市', 'NGB');
INSERT INTO `t_area` VALUES ('124', '12', '温州市', '12-124', '2', 'Wenzhou Shi', '浙江省温州市', 'WNZ');
INSERT INTO `t_area` VALUES ('125', '12', '嘉兴市', '12-125', '2', 'Jiaxing Shi', '浙江省嘉兴市', 'JIX');
INSERT INTO `t_area` VALUES ('126', '12', '湖州市', '12-126', '2', 'Huzhou Shi ', '浙江省湖州市', 'HZH');
INSERT INTO `t_area` VALUES ('127', '12', '绍兴市', '12-127', '2', 'Shaoxing Shi', '浙江省绍兴市', 'SXG');
INSERT INTO `t_area` VALUES ('128', '12', '金华市', '12-128', '2', 'Jinhua Shi', '浙江省金华市', 'JHA');
INSERT INTO `t_area` VALUES ('129', '12', '衢州市', '12-129', '2', 'Quzhou Shi', '浙江省衢州市', 'QUZ');
INSERT INTO `t_area` VALUES ('130', '12', '舟山市', '12-130', '2', 'Zhoushan Shi', '浙江省舟山市', 'ZOS');
INSERT INTO `t_area` VALUES ('131', '12', '台州市', '12-131', '2', 'Taizhou Shi', '浙江省台州市', 'TZZ');
INSERT INTO `t_area` VALUES ('132', '12', '丽水市', '12-132', '2', 'Lishui Shi', '浙江省丽水市', '2');
INSERT INTO `t_area` VALUES ('133', '13', '合肥市', '13-133', '2', 'Hefei Shi', '安徽省合肥市', 'HFE');
INSERT INTO `t_area` VALUES ('134', '13', '芜湖市', '13-134', '2', 'Wuhu Shi', '安徽省芜湖市', 'WHI');
INSERT INTO `t_area` VALUES ('135', '13', '蚌埠市', '13-135', '2', 'Bengbu Shi', '安徽省蚌埠市', 'BBU');
INSERT INTO `t_area` VALUES ('136', '13', '淮南市', '13-136', '2', 'Huainan Shi', '安徽省淮南市', 'HNS');
INSERT INTO `t_area` VALUES ('137', '13', '马鞍山市', '13-137', '2', 'Ma,anshan Shi', '安徽省马鞍山市', 'MAA');
INSERT INTO `t_area` VALUES ('138', '13', '淮北市', '13-138', '2', 'Huaibei Shi', '安徽省淮北市', 'HBE');
INSERT INTO `t_area` VALUES ('139', '13', '铜陵市', '13-139', '2', 'Tongling Shi', '安徽省铜陵市', 'TOL');
INSERT INTO `t_area` VALUES ('140', '13', '安庆市', '13-140', '2', 'Anqing Shi', '安徽省安庆市', 'AQG');
INSERT INTO `t_area` VALUES ('141', '13', '黄山市', '13-141', '2', 'Huangshan Shi', '安徽省黄山市', 'HSN');
INSERT INTO `t_area` VALUES ('142', '13', '滁州市', '13-142', '2', 'Chuzhou Shi', '安徽省滁州市', 'CUZ');
INSERT INTO `t_area` VALUES ('143', '13', '阜阳市', '13-143', '2', 'Fuyang Shi', '安徽省阜阳市', 'FYS');
INSERT INTO `t_area` VALUES ('144', '13', '宿州市', '13-144', '2', 'Suzhou Shi', '安徽省宿州市', 'SUZ');
INSERT INTO `t_area` VALUES ('145', '13', '巢湖市', '13-145', '2', 'Chaohu Shi', '安徽省巢湖市', '2');
INSERT INTO `t_area` VALUES ('146', '13', '六安市', '13-146', '2', 'Liu,an Shi', '安徽省六安市', '2');
INSERT INTO `t_area` VALUES ('147', '13', '亳州市', '13-147', '2', 'Bozhou Shi', '安徽省亳州市', '2');
INSERT INTO `t_area` VALUES ('148', '13', '池州市', '13-148', '2', 'Chizhou Shi', '安徽省池州市', '2');
INSERT INTO `t_area` VALUES ('149', '13', '宣城市', '13-149', '2', 'Xuancheng Shi', '安徽省宣城市', '2');
INSERT INTO `t_area` VALUES ('150', '14', '福州市', '14-150', '2', 'Fuzhou Shi', '福建省福州市', 'FOC');
INSERT INTO `t_area` VALUES ('151', '14', '厦门市', '14-151', '2', 'Xiamen Shi', '福建省厦门市', 'XMN');
INSERT INTO `t_area` VALUES ('152', '14', '莆田市', '14-152', '2', 'Putian Shi', '福建省莆田市', 'PUT');
INSERT INTO `t_area` VALUES ('153', '14', '三明市', '14-153', '2', 'Sanming Shi', '福建省三明市', 'SMS');
INSERT INTO `t_area` VALUES ('154', '14', '泉州市', '14-154', '2', 'Quanzhou Shi', '福建省泉州市', 'QZJ');
INSERT INTO `t_area` VALUES ('155', '14', '漳州市', '14-155', '2', 'Zhangzhou Shi', '福建省漳州市', 'ZZU');
INSERT INTO `t_area` VALUES ('156', '14', '南平市', '14-156', '2', 'Nanping Shi', '福建省南平市', 'NPS');
INSERT INTO `t_area` VALUES ('157', '14', '龙岩市', '14-157', '2', 'Longyan Shi', '福建省龙岩市', 'LYF');
INSERT INTO `t_area` VALUES ('158', '14', '宁德市', '14-158', '2', 'Ningde Shi', '福建省宁德市', '2');
INSERT INTO `t_area` VALUES ('159', '15', '南昌市', '15-159', '2', 'Nanchang Shi', '江西省南昌市', 'KHN');
INSERT INTO `t_area` VALUES ('160', '15', '景德镇市', '15-160', '2', 'Jingdezhen Shi', '江西省景德镇市', 'JDZ');
INSERT INTO `t_area` VALUES ('161', '15', '萍乡市', '15-161', '2', 'Pingxiang Shi', '江西省萍乡市', 'PXS');
INSERT INTO `t_area` VALUES ('162', '15', '九江市', '15-162', '2', 'Jiujiang Shi', '江西省九江市', 'JIU');
INSERT INTO `t_area` VALUES ('163', '15', '新余市', '15-163', '2', 'Xinyu Shi', '江西省新余市', 'XYU');
INSERT INTO `t_area` VALUES ('164', '15', '鹰潭市', '15-164', '2', 'Yingtan Shi', '江西省鹰潭市', '2');
INSERT INTO `t_area` VALUES ('165', '15', '赣州市', '15-165', '2', 'Ganzhou Shi', '江西省赣州市', 'GZH');
INSERT INTO `t_area` VALUES ('166', '15', '吉安市', '15-166', '2', 'Ji,an Shi', '江西省吉安市', '2');
INSERT INTO `t_area` VALUES ('167', '15', '宜春市', '15-167', '2', 'Yichun Shi', '江西省宜春市', '2');
INSERT INTO `t_area` VALUES ('168', '15', '抚州市', '15-168', '2', 'Wuzhou Shi', '江西省抚州市', '2');
INSERT INTO `t_area` VALUES ('169', '15', '上饶市', '15-169', '2', 'Shangrao Shi', '江西省上饶市', '2');
INSERT INTO `t_area` VALUES ('170', '16', '济南市', '16-170', '2', 'Jinan Shi', '山东省济南市', 'TNA');
INSERT INTO `t_area` VALUES ('171', '16', '青岛市', '16-171', '2', 'Qingdao Shi', '山东省青岛市', 'TAO');
INSERT INTO `t_area` VALUES ('172', '16', '淄博市', '16-172', '2', 'Zibo Shi', '山东省淄博市', 'ZBO');
INSERT INTO `t_area` VALUES ('173', '16', '枣庄市', '16-173', '2', 'Zaozhuang Shi', '山东省枣庄市', 'ZZG');
INSERT INTO `t_area` VALUES ('174', '16', '东营市', '16-174', '2', 'Dongying Shi', '山东省东营市', 'DYG');
INSERT INTO `t_area` VALUES ('175', '16', '烟台市', '16-175', '2', 'Yantai Shi', '山东省烟台市', 'YNT');
INSERT INTO `t_area` VALUES ('176', '16', '潍坊市', '16-176', '2', 'Weifang Shi', '山东省潍坊市', 'WEF');
INSERT INTO `t_area` VALUES ('177', '16', '济宁市', '16-177', '2', 'Jining Shi', '山东省济宁市', 'JNG');
INSERT INTO `t_area` VALUES ('178', '16', '泰安市', '16-178', '2', 'Tai,an Shi', '山东省泰安市', 'TAI');
INSERT INTO `t_area` VALUES ('179', '16', '威海市', '16-179', '2', 'Weihai Shi', '山东省威海市', 'WEH');
INSERT INTO `t_area` VALUES ('180', '16', '日照市', '16-180', '2', 'Rizhao Shi', '山东省日照市', 'RZH');
INSERT INTO `t_area` VALUES ('181', '16', '莱芜市', '16-181', '2', 'Laiwu Shi', '山东省莱芜市', 'LWS');
INSERT INTO `t_area` VALUES ('182', '16', '临沂市', '16-182', '2', 'Linyi Shi', '山东省临沂市', 'LYI');
INSERT INTO `t_area` VALUES ('183', '16', '德州市', '16-183', '2', 'Dezhou Shi', '山东省德州市', 'DZS');
INSERT INTO `t_area` VALUES ('184', '16', '聊城市', '16-184', '2', 'Liaocheng Shi', '山东省聊城市', 'LCH');
INSERT INTO `t_area` VALUES ('185', '16', '滨州市', '16-185', '2', 'Binzhou Shi', '山东省滨州市', '2');
INSERT INTO `t_area` VALUES ('186', '16', '菏泽市', '16-186', '2', 'Heze Shi', '山东省菏泽市', 'HZ');
INSERT INTO `t_area` VALUES ('187', '17', '郑州市', '17-187', '2', 'Zhengzhou Shi', '河南省郑州市', 'CGO');
INSERT INTO `t_area` VALUES ('188', '17', '开封市', '17-188', '2', 'Kaifeng Shi', '河南省开封市', 'KFS');
INSERT INTO `t_area` VALUES ('189', '17', '洛阳市', '17-189', '2', 'Luoyang Shi', '河南省洛阳市', 'LYA');
INSERT INTO `t_area` VALUES ('190', '17', '平顶山市', '17-190', '2', 'Pingdingshan Shi', '河南省平顶山市', 'PDS');
INSERT INTO `t_area` VALUES ('191', '17', '安阳市', '17-191', '2', 'Anyang Shi', '河南省安阳市', 'AYS');
INSERT INTO `t_area` VALUES ('192', '17', '鹤壁市', '17-192', '2', 'Hebi Shi', '河南省鹤壁市', 'HBS');
INSERT INTO `t_area` VALUES ('193', '17', '新乡市', '17-193', '2', 'Xinxiang Shi', '河南省新乡市', 'XXS');
INSERT INTO `t_area` VALUES ('194', '17', '焦作市', '17-194', '2', 'Jiaozuo Shi', '河南省焦作市', 'JZY');
INSERT INTO `t_area` VALUES ('195', '17', '濮阳市', '17-195', '2', 'Puyang Shi', '河南省濮阳市', 'PYS');
INSERT INTO `t_area` VALUES ('196', '17', '许昌市', '17-196', '2', 'Xuchang Shi', '河南省许昌市', 'XCS');
INSERT INTO `t_area` VALUES ('197', '17', '漯河市', '17-197', '2', 'Luohe Shi', '河南省漯河市', 'LHS');
INSERT INTO `t_area` VALUES ('198', '17', '三门峡市', '17-198', '2', 'Sanmenxia Shi', '河南省三门峡市', 'SMX');
INSERT INTO `t_area` VALUES ('199', '17', '南阳市', '17-199', '2', 'Nanyang Shi', '河南省南阳市', 'NYS');
INSERT INTO `t_area` VALUES ('200', '17', '商丘市', '17-200', '2', 'Shangqiu Shi', '河南省商丘市', 'SQS');
INSERT INTO `t_area` VALUES ('201', '17', '信阳市', '17-201', '2', 'Xinyang Shi', '河南省信阳市', 'XYG');
INSERT INTO `t_area` VALUES ('202', '17', '周口市', '17-202', '2', 'Zhoukou Shi', '河南省周口市', '2');
INSERT INTO `t_area` VALUES ('203', '17', '驻马店市', '17-203', '2', 'Zhumadian Shi', '河南省驻马店市', '2');
INSERT INTO `t_area` VALUES ('204', '18', '武汉市', '18-204', '2', 'Wuhan Shi', '湖北省武汉市', 'WUH');
INSERT INTO `t_area` VALUES ('205', '18', '黄石市', '18-205', '2', 'Huangshi Shi', '湖北省黄石市', 'HIS');
INSERT INTO `t_area` VALUES ('206', '18', '十堰市', '18-206', '2', 'Shiyan Shi', '湖北省十堰市', 'SYE');
INSERT INTO `t_area` VALUES ('207', '18', '宜昌市', '18-207', '2', 'Yichang Shi', '湖北省宜昌市', 'YCO');
INSERT INTO `t_area` VALUES ('208', '18', '襄樊市', '18-208', '2', 'Xiangfan Shi', '湖北省襄樊市', 'XFN');
INSERT INTO `t_area` VALUES ('209', '18', '鄂州市', '18-209', '2', 'Ezhou Shi', '湖北省鄂州市', 'EZS');
INSERT INTO `t_area` VALUES ('210', '18', '荆门市', '18-210', '2', 'Jingmen Shi', '湖北省荆门市', 'JMS');
INSERT INTO `t_area` VALUES ('211', '18', '孝感市', '18-211', '2', 'Xiaogan Shi', '湖北省孝感市', 'XGE');
INSERT INTO `t_area` VALUES ('212', '18', '荆州市', '18-212', '2', 'Jingzhou Shi', '湖北省荆州市', 'JGZ');
INSERT INTO `t_area` VALUES ('213', '18', '黄冈市', '18-213', '2', 'Huanggang Shi', '湖北省黄冈市', 'HE');
INSERT INTO `t_area` VALUES ('214', '18', '咸宁市', '18-214', '2', 'Xianning Xian', '湖北省咸宁市', 'XNS');
INSERT INTO `t_area` VALUES ('215', '18', '随州市', '18-215', '2', 'Suizhou Shi', '湖北省随州市', '2');
INSERT INTO `t_area` VALUES ('216', '18', '恩施土家族苗族自治州', '18-216', '2', 'Enshi Tujiazu Miaozu Zizhizhou', '湖北省恩施土家族苗族自治州', 'ESH');
INSERT INTO `t_area` VALUES ('218', '19', '长沙市', '19-218', '2', 'Changsha Shi', '湖南省长沙市', 'CSX');
INSERT INTO `t_area` VALUES ('219', '19', '株洲市', '19-219', '2', 'Zhuzhou Shi', '湖南省株洲市', 'ZZS');
INSERT INTO `t_area` VALUES ('220', '19', '湘潭市', '19-220', '2', 'Xiangtan Shi', '湖南省湘潭市', 'XGT');
INSERT INTO `t_area` VALUES ('221', '19', '衡阳市', '19-221', '2', 'Hengyang Shi', '湖南省衡阳市', 'HNY');
INSERT INTO `t_area` VALUES ('222', '19', '邵阳市', '19-222', '2', 'Shaoyang Shi', '湖南省邵阳市', 'SYR');
INSERT INTO `t_area` VALUES ('223', '19', '岳阳市', '19-223', '2', 'Yueyang Shi', '湖南省岳阳市', 'YYG');
INSERT INTO `t_area` VALUES ('224', '19', '常德市', '19-224', '2', 'Changde Shi', '湖南省常德市', 'CDE');
INSERT INTO `t_area` VALUES ('225', '19', '张家界市', '19-225', '2', 'Zhangjiajie Shi', '湖南省张家界市', 'ZJJ');
INSERT INTO `t_area` VALUES ('226', '19', '益阳市', '19-226', '2', 'Yiyang Shi', '湖南省益阳市', 'YYS');
INSERT INTO `t_area` VALUES ('227', '19', '郴州市', '19-227', '2', 'Chenzhou Shi', '湖南省郴州市', 'CNZ');
INSERT INTO `t_area` VALUES ('228', '19', '永州市', '19-228', '2', 'Yongzhou Shi', '湖南省永州市', 'YZS');
INSERT INTO `t_area` VALUES ('229', '19', '怀化市', '19-229', '2', 'Huaihua Shi', '湖南省怀化市', 'HHS');
INSERT INTO `t_area` VALUES ('230', '19', '娄底市', '19-230', '2', 'Loudi Shi', '湖南省娄底市', '2');
INSERT INTO `t_area` VALUES ('231', '19', '湘西土家族苗族自治州', '19-231', '2', 'Xiangxi Tujiazu Miaozu Zizhizhou ', '湖南省湘西土家族苗族自治州', 'XXZ');
INSERT INTO `t_area` VALUES ('232', '20', '广州市', '20-232', '2', 'Guangzhou Shi', '广东省广州市', 'CAN');
INSERT INTO `t_area` VALUES ('233', '20', '韶关市', '20-233', '2', 'Shaoguan Shi', '广东省韶关市', 'HSC');
INSERT INTO `t_area` VALUES ('234', '20', '深圳市', '20-234', '2', 'Shenzhen Shi', '广东省深圳市', 'SZX');
INSERT INTO `t_area` VALUES ('235', '20', '珠海市', '20-235', '2', 'Zhuhai Shi', '广东省珠海市', 'ZUH');
INSERT INTO `t_area` VALUES ('236', '20', '汕头市', '20-236', '2', 'Shantou Shi', '广东省汕头市', 'SWA');
INSERT INTO `t_area` VALUES ('237', '20', '佛山市', '20-237', '2', 'Foshan Shi', '广东省佛山市', 'FOS');
INSERT INTO `t_area` VALUES ('238', '20', '江门市', '20-238', '2', 'Jiangmen Shi', '广东省江门市', 'JMN');
INSERT INTO `t_area` VALUES ('239', '20', '湛江市', '20-239', '2', 'Zhanjiang Shi', '广东省湛江市', 'ZHA');
INSERT INTO `t_area` VALUES ('240', '20', '茂名市', '20-240', '2', 'Maoming Shi', '广东省茂名市', 'MMI');
INSERT INTO `t_area` VALUES ('241', '20', '肇庆市', '20-241', '2', 'Zhaoqing Shi', '广东省肇庆市', 'ZQG');
INSERT INTO `t_area` VALUES ('242', '20', '惠州市', '20-242', '2', 'Huizhou Shi', '广东省惠州市', 'HUI');
INSERT INTO `t_area` VALUES ('243', '20', '梅州市', '20-243', '2', 'Meizhou Shi', '广东省梅州市', 'MXZ');
INSERT INTO `t_area` VALUES ('244', '20', '汕尾市', '20-244', '2', 'Shanwei Shi', '广东省汕尾市', 'SWE');
INSERT INTO `t_area` VALUES ('245', '20', '河源市', '20-245', '2', 'Heyuan Shi', '广东省河源市', 'HEY');
INSERT INTO `t_area` VALUES ('246', '20', '阳江市', '20-246', '2', 'Yangjiang Shi', '广东省阳江市', 'YJI');
INSERT INTO `t_area` VALUES ('247', '20', '清远市', '20-247', '2', 'Qingyuan Shi', '广东省清远市', 'QYN');
INSERT INTO `t_area` VALUES ('248', '20', '东莞市', '20-248', '2', 'Dongguan Shi', '广东省东莞市', 'DGG');
INSERT INTO `t_area` VALUES ('249', '20', '中山市', '20-249', '2', 'Zhongshan Shi', '广东省中山市', 'ZSN');
INSERT INTO `t_area` VALUES ('250', '20', '潮州市', '20-250', '2', 'Chaozhou Shi', '广东省潮州市', 'CZY');
INSERT INTO `t_area` VALUES ('251', '20', '揭阳市', '20-251', '2', 'Jieyang Shi', '广东省揭阳市', 'JIY');
INSERT INTO `t_area` VALUES ('252', '20', '云浮市', '20-252', '2', 'Yunfu Shi', '广东省云浮市', 'YFS');
INSERT INTO `t_area` VALUES ('253', '21', '南宁市', '21-253', '2', 'Nanning Shi', '广西壮族自治区南宁市', 'NNG');
INSERT INTO `t_area` VALUES ('254', '21', '柳州市', '21-254', '2', 'Liuzhou Shi', '广西壮族自治区柳州市', 'LZH');
INSERT INTO `t_area` VALUES ('255', '21', '桂林市', '21-255', '2', 'Guilin Shi', '广西壮族自治区桂林市', 'KWL');
INSERT INTO `t_area` VALUES ('256', '21', '梧州市', '21-256', '2', 'Wuzhou Shi', '广西壮族自治区梧州市', 'WUZ');
INSERT INTO `t_area` VALUES ('257', '21', '北海市', '21-257', '2', 'Beihai Shi', '广西壮族自治区北海市', 'BHY');
INSERT INTO `t_area` VALUES ('258', '21', '防城港市', '21-258', '2', 'Fangchenggang Shi', '广西壮族自治区防城港市', 'FAN');
INSERT INTO `t_area` VALUES ('259', '21', '钦州市', '21-259', '2', 'Qinzhou Shi', '广西壮族自治区钦州市', 'QZH');
INSERT INTO `t_area` VALUES ('260', '21', '贵港市', '21-260', '2', 'Guigang Shi', '广西壮族自治区贵港市', 'GUG');
INSERT INTO `t_area` VALUES ('261', '21', '玉林市', '21-261', '2', 'Yulin Shi', '广西壮族自治区玉林市', 'YUL');
INSERT INTO `t_area` VALUES ('262', '21', '百色市', '21-262', '2', 'Baise Shi', '广西壮族自治区百色市', '2');
INSERT INTO `t_area` VALUES ('263', '21', '贺州市', '21-263', '2', 'Hezhou Shi', '广西壮族自治区贺州市', '2');
INSERT INTO `t_area` VALUES ('264', '21', '河池市', '21-264', '2', 'Hechi Shi', '广西壮族自治区河池市', '2');
INSERT INTO `t_area` VALUES ('265', '21', '来宾市', '21-265', '2', 'Laibin Shi', '广西壮族自治区来宾市', '2');
INSERT INTO `t_area` VALUES ('266', '21', '崇左市', '21-266', '2', 'Chongzuo Shi', '广西壮族自治区崇左市', '2');
INSERT INTO `t_area` VALUES ('267', '22', '海口市', '22-267', '2', 'Haikou Shi', '海南省海口市', 'HAK');
INSERT INTO `t_area` VALUES ('268', '22', '三亚市', '22-268', '2', 'Sanya Shi', '海南省三亚市', 'SYX');
INSERT INTO `t_area` VALUES ('273', '24', '成都市', '24-273', '2', 'Chengdu Shi', '四川省成都市', 'CTU');
INSERT INTO `t_area` VALUES ('274', '24', '自贡市', '24-274', '2', 'Zigong Shi', '四川省自贡市', 'ZGS');
INSERT INTO `t_area` VALUES ('275', '24', '攀枝花市', '24-275', '2', 'Panzhihua Shi', '四川省攀枝花市', 'PZH');
INSERT INTO `t_area` VALUES ('276', '24', '泸州市', '24-276', '2', 'Luzhou Shi', '四川省泸州市', 'LUZ');
INSERT INTO `t_area` VALUES ('277', '24', '德阳市', '24-277', '2', 'Deyang Shi', '四川省德阳市', 'DEY');
INSERT INTO `t_area` VALUES ('278', '24', '绵阳市', '24-278', '2', 'Mianyang Shi', '四川省绵阳市', 'MYG');
INSERT INTO `t_area` VALUES ('279', '24', '广元市', '24-279', '2', 'Guangyuan Shi', '四川省广元市', 'GYC');
INSERT INTO `t_area` VALUES ('280', '24', '遂宁市', '24-280', '2', 'Suining Shi', '四川省遂宁市', 'SNS');
INSERT INTO `t_area` VALUES ('281', '24', '内江市', '24-281', '2', 'Neijiang Shi', '四川省内江市', 'NJS');
INSERT INTO `t_area` VALUES ('282', '24', '乐山市', '24-282', '2', 'Leshan Shi', '四川省乐山市', 'LES');
INSERT INTO `t_area` VALUES ('283', '24', '南充市', '24-283', '2', 'Nanchong Shi', '四川省南充市', 'NCO');
INSERT INTO `t_area` VALUES ('284', '24', '眉山市', '24-284', '2', 'Meishan Shi', '四川省眉山市', '2');
INSERT INTO `t_area` VALUES ('285', '24', '宜宾市', '24-285', '2', 'Yibin Shi', '四川省宜宾市', 'YBS');
INSERT INTO `t_area` VALUES ('286', '24', '广安市', '24-286', '2', 'Guang,an Shi', '四川省广安市', 'GAC');
INSERT INTO `t_area` VALUES ('287', '24', '达州市', '24-287', '2', 'Dazhou Shi', '四川省达州市', '2');
INSERT INTO `t_area` VALUES ('288', '24', '雅安市', '24-288', '2', 'Ya,an Shi', '四川省雅安市', '2');
INSERT INTO `t_area` VALUES ('289', '24', '巴中市', '24-289', '2', 'Bazhong Shi', '四川省巴中市', '2');
INSERT INTO `t_area` VALUES ('290', '24', '资阳市', '24-290', '2', 'Ziyang Shi', '四川省资阳市', '2');
INSERT INTO `t_area` VALUES ('291', '24', '阿坝藏族羌族自治州', '24-291', '2', 'Aba(Ngawa) Zangzu Qiangzu Zizhizhou', '四川省阿坝藏族羌族自治州', 'ABA');
INSERT INTO `t_area` VALUES ('292', '24', '甘孜藏族自治州', '24-292', '2', 'Garze Zangzu Zizhizhou', '四川省甘孜藏族自治州', 'GAZ');
INSERT INTO `t_area` VALUES ('293', '24', '凉山彝族自治州', '24-293', '2', 'Liangshan Yizu Zizhizhou', '四川省凉山彝族自治州', 'LSY');
INSERT INTO `t_area` VALUES ('294', '25', '贵阳市', '25-294', '2', 'Guiyang Shi', '贵州省贵阳市', 'KWE');
INSERT INTO `t_area` VALUES ('295', '25', '六盘水市', '25-295', '2', 'Liupanshui Shi', '贵州省六盘水市', 'LPS');
INSERT INTO `t_area` VALUES ('296', '25', '遵义市', '25-296', '2', 'Zunyi Shi', '贵州省遵义市', 'ZNY');
INSERT INTO `t_area` VALUES ('297', '25', '安顺市', '25-297', '2', 'Anshun Xian', '贵州省安顺市', '2');
INSERT INTO `t_area` VALUES ('298', '25', '铜仁地区', '25-298', '2', 'Tongren Diqu', '贵州省铜仁地区', 'TRD');
INSERT INTO `t_area` VALUES ('299', '25', '黔西南布依族苗族自治州', '25-299', '2', 'Qianxinan Buyeizu Zizhizhou', '贵州省黔西南布依族苗族自治州', 'QXZ');
INSERT INTO `t_area` VALUES ('300', '25', '毕节地区', '25-300', '2', 'Bijie Diqu', '贵州省毕节地区', 'BJD');
INSERT INTO `t_area` VALUES ('301', '25', '黔东南苗族侗族自治州', '25-301', '2', 'Qiandongnan Miaozu Dongzu Zizhizhou', '贵州省黔东南苗族侗族自治州', 'QND');
INSERT INTO `t_area` VALUES ('302', '25', '黔南布依族苗族自治州', '25-302', '2', 'Qiannan Buyeizu Miaozu Zizhizhou', '贵州省黔南布依族苗族自治州', 'QNZ');
INSERT INTO `t_area` VALUES ('303', '26', '昆明市', '26-303', '2', 'Kunming Shi', '云南省昆明市', 'KMG');
INSERT INTO `t_area` VALUES ('304', '26', '曲靖市', '26-304', '2', 'Qujing Shi', '云南省曲靖市', 'QJS');
INSERT INTO `t_area` VALUES ('305', '26', '玉溪市', '26-305', '2', 'Yuxi Shi', '云南省玉溪市', 'YXS');
INSERT INTO `t_area` VALUES ('306', '26', '保山市', '26-306', '2', 'Baoshan Shi', '云南省保山市', '2');
INSERT INTO `t_area` VALUES ('307', '26', '昭通市', '26-307', '2', 'Zhaotong Shi', '云南省昭通市', '2');
INSERT INTO `t_area` VALUES ('308', '26', '丽江市', '26-308', '2', 'Lijiang Shi', '云南省丽江市', '2');
INSERT INTO `t_area` VALUES ('309', '26', '普洱市', '26-309', '2', 'Simao Shi', '云南省普洱市', '2');
INSERT INTO `t_area` VALUES ('310', '26', '临沧市', '26-310', '2', 'Lincang Shi', '云南省临沧市', '2');
INSERT INTO `t_area` VALUES ('311', '26', '楚雄彝族自治州', '26-311', '2', 'Chuxiong Yizu Zizhizhou', '云南省楚雄彝族自治州', 'CXD');
INSERT INTO `t_area` VALUES ('312', '26', '红河哈尼族彝族自治州', '26-312', '2', 'Honghe Hanizu Yizu Zizhizhou', '云南省红河哈尼族彝族自治州', 'HHZ');
INSERT INTO `t_area` VALUES ('313', '26', '文山壮族苗族自治州', '26-313', '2', 'Wenshan Zhuangzu Miaozu Zizhizhou', '云南省文山壮族苗族自治州', 'WSZ');
INSERT INTO `t_area` VALUES ('314', '26', '西双版纳傣族自治州', '26-314', '2', 'Xishuangbanna Daizu Zizhizhou', '云南省西双版纳傣族自治州', 'XSB');
INSERT INTO `t_area` VALUES ('315', '26', '大理白族自治州', '26-315', '2', 'Dali Baizu Zizhizhou', '云南省大理白族自治州', 'DLZ');
INSERT INTO `t_area` VALUES ('316', '26', '德宏傣族景颇族自治州', '26-316', '2', 'Dehong Daizu Jingpozu Zizhizhou', '云南省德宏傣族景颇族自治州', 'DHG');
INSERT INTO `t_area` VALUES ('317', '26', '怒江傈僳族自治州', '26-317', '2', 'Nujiang Lisuzu Zizhizhou', '云南省怒江傈僳族自治州', 'NUJ');
INSERT INTO `t_area` VALUES ('318', '26', '迪庆藏族自治州', '26-318', '2', 'Deqen Zangzu Zizhizhou', '云南省迪庆藏族自治州', 'DEZ');
INSERT INTO `t_area` VALUES ('319', '27', '拉萨市', '27-319', '2', 'Lhasa Shi', '西藏自治区拉萨市', 'LXA');
INSERT INTO `t_area` VALUES ('320', '27', '昌都地区', '27-320', '2', 'Qamdo Diqu', '西藏自治区昌都地区', 'QAD');
INSERT INTO `t_area` VALUES ('321', '27', '山南地区', '27-321', '2', 'Shannan Diqu', '西藏自治区山南地区', 'SND');
INSERT INTO `t_area` VALUES ('322', '27', '日喀则地区', '27-322', '2', 'Xigaze Diqu', '西藏自治区日喀则地区', 'XID');
INSERT INTO `t_area` VALUES ('323', '27', '那曲地区', '27-323', '2', 'Nagqu Diqu', '西藏自治区那曲地区', 'NAD');
INSERT INTO `t_area` VALUES ('324', '27', '阿里地区', '27-324', '2', 'Ngari Diqu', '西藏自治区阿里地区', 'NGD');
INSERT INTO `t_area` VALUES ('325', '27', '林芝地区', '27-325', '2', 'Nyingchi Diqu', '西藏自治区林芝地区', 'NYD');
INSERT INTO `t_area` VALUES ('326', '28', '西安市', '28-326', '2', 'Xi,an Shi', '陕西省西安市', 'SIA');
INSERT INTO `t_area` VALUES ('327', '28', '铜川市', '28-327', '2', 'Tongchuan Shi', '陕西省铜川市', 'TCN');
INSERT INTO `t_area` VALUES ('328', '28', '宝鸡市', '28-328', '2', 'Baoji Shi', '陕西省宝鸡市', 'BJI');
INSERT INTO `t_area` VALUES ('329', '28', '咸阳市', '28-329', '2', 'Xianyang Shi', '陕西省咸阳市', 'XYS');
INSERT INTO `t_area` VALUES ('330', '28', '渭南市', '28-330', '2', 'Weinan Shi', '陕西省渭南市', 'WNA');
INSERT INTO `t_area` VALUES ('331', '28', '延安市', '28-331', '2', 'Yan,an Shi', '陕西省延安市', 'YNA');
INSERT INTO `t_area` VALUES ('332', '28', '汉中市', '28-332', '2', 'Hanzhong Shi', '陕西省汉中市', 'HZJ');
INSERT INTO `t_area` VALUES ('333', '28', '榆林市', '28-333', '2', 'Yulin Shi', '陕西省榆林市', '2');
INSERT INTO `t_area` VALUES ('334', '28', '安康市', '28-334', '2', 'Ankang Shi', '陕西省安康市', '2');
INSERT INTO `t_area` VALUES ('335', '28', '商洛市', '28-335', '2', 'Shangluo Shi', '陕西省商洛市', '2');
INSERT INTO `t_area` VALUES ('336', '29', '兰州市', '29-336', '2', 'Lanzhou Shi', '甘肃省兰州市', 'LHW');
INSERT INTO `t_area` VALUES ('337', '29', '嘉峪关市', '29-337', '2', 'Jiayuguan Shi', '甘肃省嘉峪关市', 'JYG');
INSERT INTO `t_area` VALUES ('338', '29', '金昌市', '29-338', '2', 'Jinchang Shi', '甘肃省金昌市', 'JCS');
INSERT INTO `t_area` VALUES ('339', '29', '白银市', '29-339', '2', 'Baiyin Shi', '甘肃省白银市', 'BYS');
INSERT INTO `t_area` VALUES ('340', '29', '天水市', '29-340', '2', 'Tianshui Shi', '甘肃省天水市', 'TSU');
INSERT INTO `t_area` VALUES ('341', '29', '武威市', '29-341', '2', 'Wuwei Shi', '甘肃省武威市', '2');
INSERT INTO `t_area` VALUES ('342', '29', '张掖市', '29-342', '2', 'Zhangye Shi', '甘肃省张掖市', '2');
INSERT INTO `t_area` VALUES ('343', '29', '平凉市', '29-343', '2', 'Pingliang Shi', '甘肃省平凉市', '2');
INSERT INTO `t_area` VALUES ('344', '29', '酒泉市', '29-344', '2', 'Jiuquan Shi', '甘肃省酒泉市', '2');
INSERT INTO `t_area` VALUES ('345', '29', '庆阳市', '29-345', '2', 'Qingyang Shi', '甘肃省庆阳市', '2');
INSERT INTO `t_area` VALUES ('346', '29', '定西市', '29-346', '2', 'Dingxi Shi', '甘肃省定西市', '2');
INSERT INTO `t_area` VALUES ('347', '29', '陇南市', '29-347', '2', 'Longnan Shi', '甘肃省陇南市', '2');
INSERT INTO `t_area` VALUES ('348', '29', '临夏回族自治州', '29-348', '2', 'Linxia Huizu Zizhizhou ', '甘肃省临夏回族自治州', 'LXH');
INSERT INTO `t_area` VALUES ('349', '29', '甘南藏族自治州', '29-349', '2', 'Gannan Zangzu Zizhizhou', '甘肃省甘南藏族自治州', 'GNZ');
INSERT INTO `t_area` VALUES ('350', '30', '西宁市', '30-350', '2', 'Xining Shi', '青海省西宁市', 'XNN');
INSERT INTO `t_area` VALUES ('351', '30', '海东地区', '30-351', '2', 'Haidong Diqu', '青海省海东地区', 'HDD');
INSERT INTO `t_area` VALUES ('352', '30', '海北藏族自治州', '30-352', '2', 'Haibei Zangzu Zizhizhou', '青海省海北藏族自治州', 'HBZ');
INSERT INTO `t_area` VALUES ('353', '30', '黄南藏族自治州', '30-353', '2', 'Huangnan Zangzu Zizhizhou', '青海省黄南藏族自治州', 'HNZ');
INSERT INTO `t_area` VALUES ('354', '30', '海南藏族自治州', '30-354', '2', 'Hainan Zangzu Zizhizhou', '青海省海南藏族自治州', 'HNN');
INSERT INTO `t_area` VALUES ('355', '30', '果洛藏族自治州', '30-355', '2', 'Golog Zangzu Zizhizhou', '青海省果洛藏族自治州', 'GOL');
INSERT INTO `t_area` VALUES ('356', '30', '玉树藏族自治州', '30-356', '2', 'Yushu Zangzu Zizhizhou', '青海省玉树藏族自治州', 'YSZ');
INSERT INTO `t_area` VALUES ('357', '30', '海西蒙古族藏族自治州', '30-357', '2', 'Haixi Mongolzu Zangzu Zizhizhou', '青海省海西蒙古族藏族自治州', 'HXZ');
INSERT INTO `t_area` VALUES ('358', '31', '银川市', '31-358', '2', 'Yinchuan Shi', '宁夏回族自治区银川市', 'INC');
INSERT INTO `t_area` VALUES ('359', '31', '石嘴山市', '31-359', '2', 'Shizuishan Shi', '宁夏回族自治区石嘴山市', 'SZS');
INSERT INTO `t_area` VALUES ('360', '31', '吴忠市', '31-360', '2', 'Wuzhong Shi', '宁夏回族自治区吴忠市', 'WZS');
INSERT INTO `t_area` VALUES ('361', '31', '固原市', '31-361', '2', 'Guyuan Shi', '宁夏回族自治区固原市', '2');
INSERT INTO `t_area` VALUES ('362', '31', '中卫市', '31-362', '2', 'Zhongwei Shi', '宁夏回族自治区中卫市', '2');
INSERT INTO `t_area` VALUES ('363', '32', '乌鲁木齐市', '32-363', '2', 'Urumqi Shi', '新疆维吾尔自治区乌鲁木齐市', 'URC');
INSERT INTO `t_area` VALUES ('364', '32', '克拉玛依市', '32-364', '2', 'Karamay Shi', '新疆维吾尔自治区克拉玛依市', 'KAR');
INSERT INTO `t_area` VALUES ('365', '32', '吐鲁番地区', '32-365', '2', 'Turpan Diqu', '新疆维吾尔自治区吐鲁番地区', 'TUD');
INSERT INTO `t_area` VALUES ('366', '32', '哈密地区', '32-366', '2', 'Hami(kumul) Diqu', '新疆维吾尔自治区哈密地区', 'HMD');
INSERT INTO `t_area` VALUES ('367', '32', '昌吉回族自治州', '32-367', '2', 'Changji Huizu Zizhizhou', '新疆维吾尔自治区昌吉回族自治州', 'CJZ');
INSERT INTO `t_area` VALUES ('368', '32', '博尔塔拉蒙古自治州', '32-368', '2', 'Bortala Monglo Zizhizhou', '新疆维吾尔自治区博尔塔拉蒙古自治州', 'BOR');
INSERT INTO `t_area` VALUES ('369', '32', '巴音郭楞蒙古自治州', '32-369', '2', 'bayinguolengmengguzizhizhou', '新疆维吾尔自治区巴音郭楞蒙古自治州', '2');
INSERT INTO `t_area` VALUES ('370', '32', '阿克苏地区', '32-370', '2', 'Aksu Diqu', '新疆维吾尔自治区阿克苏地区', 'AKD');
INSERT INTO `t_area` VALUES ('371', '32', '克孜勒苏柯尔克孜自治州', '32-371', '2', 'Kizilsu Kirgiz Zizhizhou', '新疆维吾尔自治区克孜勒苏柯尔克孜自治州', 'KIZ');
INSERT INTO `t_area` VALUES ('372', '32', '喀什地区', '32-372', '2', 'Kashi(Kaxgar) Diqu', '新疆维吾尔自治区喀什地区', 'KSI');
INSERT INTO `t_area` VALUES ('373', '32', '和田地区', '32-373', '2', 'Hotan Diqu', '新疆维吾尔自治区和田地区', 'HOD');
INSERT INTO `t_area` VALUES ('374', '32', '伊犁哈萨克自治州', '32-374', '2', 'Ili Kazak Zizhizhou', '新疆维吾尔自治区伊犁哈萨克自治州', 'ILD');
INSERT INTO `t_area` VALUES ('375', '32', '塔城地区', '32-375', '2', 'Tacheng(Qoqek) Diqu', '新疆维吾尔自治区塔城地区', 'TCD');
INSERT INTO `t_area` VALUES ('376', '32', '阿勒泰地区', '32-376', '2', 'Altay Diqu', '新疆维吾尔自治区阿勒泰地区', 'ALD');
INSERT INTO `t_area` VALUES ('378', '5001', '东城区', '2-5001-378', '3', 'Dongcheng Qu', '北京市北京市东城区', 'DCQ');
INSERT INTO `t_area` VALUES ('379', '5001', '西城区', '2-5001-379', '3', 'Xicheng Qu', '北京市北京市西城区', 'XCQ');
INSERT INTO `t_area` VALUES ('382', '5001', '朝阳区', '2-5001-382', '3', 'Chaoyang Qu', '北京市北京市朝阳区', 'CYQ');
INSERT INTO `t_area` VALUES ('383', '5001', '丰台区', '2-5001-383', '3', 'Fengtai Qu', '北京市北京市丰台区', 'FTQ');
INSERT INTO `t_area` VALUES ('384', '5001', '石景山区', '2-5001-384', '3', 'Shijingshan Qu', '北京市北京市石景山区', 'SJS');
INSERT INTO `t_area` VALUES ('385', '5001', '海淀区', '2-5001-385', '3', 'Haidian Qu', '北京市北京市海淀区', 'HDN');
INSERT INTO `t_area` VALUES ('386', '5001', '门头沟区', '2-5001-386', '3', 'Mentougou Qu', '北京市北京市门头沟区', 'MTG');
INSERT INTO `t_area` VALUES ('387', '5001', '房山区', '2-5001-387', '3', 'Fangshan Qu', '北京市北京市房山区', 'FSQ');
INSERT INTO `t_area` VALUES ('388', '5001', '通州区', '2-5001-388', '3', 'Tongzhou Qu', '北京市北京市通州区', 'TZQ');
INSERT INTO `t_area` VALUES ('389', '5001', '顺义区', '2-5001-389', '3', 'Shunyi Qu', '北京市北京市顺义区', 'SYI');
INSERT INTO `t_area` VALUES ('390', '5001', '昌平区', '2-5001-390', '3', 'Changping Qu', '北京市北京市昌平区', 'CP Q');
INSERT INTO `t_area` VALUES ('391', '5001', '大兴区', '2-5001-391', '3', 'Daxing Qu', '北京市北京市大兴区', 'DX Q');
INSERT INTO `t_area` VALUES ('392', '5001', '怀柔区', '2-5001-392', '3', 'Huairou Qu', '北京市北京市怀柔区', 'HR Q');
INSERT INTO `t_area` VALUES ('393', '5001', '平谷区', '2-5001-393', '3', 'Pinggu Qu', '北京市北京市平谷区', 'PG Q');
INSERT INTO `t_area` VALUES ('394', '5001', '密云县', '2-5001-394', '3', 'Miyun Xian ', '北京市北京市密云县', 'MYN');
INSERT INTO `t_area` VALUES ('395', '5001', '延庆县', '2-5001-395', '3', 'Yanqing Xian', '北京市北京市延庆县', 'YQX');
INSERT INTO `t_area` VALUES ('396', '5002', '和平区', '3-5002-396', '3', 'Heping Qu', '天津市天津市和平区', 'HPG');
INSERT INTO `t_area` VALUES ('397', '5002', '河东区', '3-5002-397', '3', 'Hedong Qu', '天津市天津市河东区', 'HDQ');
INSERT INTO `t_area` VALUES ('398', '5002', '河西区', '3-5002-398', '3', 'Hexi Qu', '天津市天津市河西区', 'HXQ');
INSERT INTO `t_area` VALUES ('399', '5002', '南开区', '3-5002-399', '3', 'Nankai Qu', '天津市天津市南开区', 'NKQ');
INSERT INTO `t_area` VALUES ('400', '5002', '河北区', '3-5002-400', '3', 'Hebei Qu', '天津市天津市河北区', 'HBQ');
INSERT INTO `t_area` VALUES ('401', '5002', '红桥区', '3-5002-401', '3', 'Hongqiao Qu', '天津市天津市红桥区', 'HQO');
INSERT INTO `t_area` VALUES ('404', '5002', '滨海新区', '3-5002-404', '3', 'Dagang Qu', '天津市天津市滨海新区', '2');
INSERT INTO `t_area` VALUES ('405', '5002', '东丽区', '3-5002-405', '3', 'Dongli Qu', '天津市天津市东丽区', 'DLI');
INSERT INTO `t_area` VALUES ('406', '5002', '西青区', '3-5002-406', '3', 'Xiqing Qu', '天津市天津市西青区', 'XQG');
INSERT INTO `t_area` VALUES ('407', '5002', '津南区', '3-5002-407', '3', 'Jinnan Qu', '天津市天津市津南区', 'JNQ');
INSERT INTO `t_area` VALUES ('408', '5002', '北辰区', '3-5002-408', '3', 'Beichen Qu', '天津市天津市北辰区', 'BCQ');
INSERT INTO `t_area` VALUES ('409', '5002', '武清区', '3-5002-409', '3', 'Wuqing Qu', '天津市天津市武清区', 'WQ Q');
INSERT INTO `t_area` VALUES ('410', '5002', '宝坻区', '3-5002-410', '3', 'Baodi Qu', '天津市天津市宝坻区', 'BDI');
INSERT INTO `t_area` VALUES ('411', '5002', '宁河县', '3-5002-411', '3', 'Ninghe Xian', '天津市天津市宁河县', 'NHE');
INSERT INTO `t_area` VALUES ('412', '5002', '静海县', '3-5002-412', '3', 'Jinghai Xian', '天津市天津市静海县', 'JHT');
INSERT INTO `t_area` VALUES ('413', '5002', '蓟县', '3-5002-413', '3', 'Ji Xian', '天津市天津市蓟县', 'JIT');
INSERT INTO `t_area` VALUES ('415', '37', '长安区', '4-37-415', '3', 'Chang,an Qu', '河北省石家庄市长安区', 'CAQ');
INSERT INTO `t_area` VALUES ('416', '37', '桥东区', '4-37-416', '3', 'Qiaodong Qu', '河北省石家庄市桥东区', 'QDQ');
INSERT INTO `t_area` VALUES ('417', '37', '桥西区', '4-37-417', '3', 'Qiaoxi Qu', '河北省石家庄市桥西区', 'QXQ');
INSERT INTO `t_area` VALUES ('418', '37', '新华区', '4-37-418', '3', 'Xinhua Qu', '河北省石家庄市新华区', 'XHK');
INSERT INTO `t_area` VALUES ('419', '37', '井陉矿区', '4-37-419', '3', 'Jingxing Kuangqu', '河北省石家庄市井陉矿区', 'JXK');
INSERT INTO `t_area` VALUES ('420', '37', '裕华区', '4-37-420', '3', 'Yuhua Qu', '河北省石家庄市裕华区', '2');
INSERT INTO `t_area` VALUES ('421', '37', '井陉县', '4-37-421', '3', 'Jingxing Xian', '河北省石家庄市井陉县', 'JXJ');
INSERT INTO `t_area` VALUES ('422', '37', '正定县', '4-37-422', '3', 'Zhengding Xian', '河北省石家庄市正定县', 'ZDJ');
INSERT INTO `t_area` VALUES ('423', '37', '栾城县', '4-37-423', '3', 'Luancheng Xian', '河北省石家庄市栾城县', 'LCG');
INSERT INTO `t_area` VALUES ('424', '37', '行唐县', '4-37-424', '3', 'Xingtang Xian', '河北省石家庄市行唐县', 'XTG');
INSERT INTO `t_area` VALUES ('425', '37', '灵寿县', '4-37-425', '3', 'Lingshou Xian ', '河北省石家庄市灵寿县', 'LSO');
INSERT INTO `t_area` VALUES ('426', '37', '高邑县', '4-37-426', '3', 'Gaoyi Xian', '河北省石家庄市高邑县', 'GYJ');
INSERT INTO `t_area` VALUES ('427', '37', '深泽县', '4-37-427', '3', 'Shenze Xian', '河北省石家庄市深泽县', '2');
INSERT INTO `t_area` VALUES ('428', '37', '赞皇县', '4-37-428', '3', 'Zanhuang Xian', '河北省石家庄市赞皇县', 'ZHG');
INSERT INTO `t_area` VALUES ('429', '37', '无极县', '4-37-429', '3', 'Wuji Xian', '河北省石家庄市无极县', 'WJI');
INSERT INTO `t_area` VALUES ('430', '37', '平山县', '4-37-430', '3', 'Pingshan Xian', '河北省石家庄市平山县', 'PSH');
INSERT INTO `t_area` VALUES ('431', '37', '元氏县', '4-37-431', '3', 'Yuanshi Xian', '河北省石家庄市元氏县', 'YSI');
INSERT INTO `t_area` VALUES ('432', '37', '赵县', '4-37-432', '3', 'Zhao Xian', '河北省石家庄市赵县', 'ZAO');
INSERT INTO `t_area` VALUES ('433', '37', '辛集市', '4-37-433', '3', 'Xinji Shi', '河北省石家庄市辛集市', 'XJS');
INSERT INTO `t_area` VALUES ('434', '37', '藁城市', '4-37-434', '3', 'Gaocheng Shi', '河北省石家庄市藁城市', 'GCS');
INSERT INTO `t_area` VALUES ('435', '37', '晋州市', '4-37-435', '3', 'Jinzhou Shi', '河北省石家庄市晋州市', 'JZJ');
INSERT INTO `t_area` VALUES ('436', '37', '新乐市', '4-37-436', '3', 'Xinle Shi', '河北省石家庄市新乐市', 'XLE');
INSERT INTO `t_area` VALUES ('437', '37', '鹿泉市', '4-37-437', '3', 'Luquan Shi', '河北省石家庄市鹿泉市', 'LUQ');
INSERT INTO `t_area` VALUES ('439', '38', '路南区', '4-38-439', '3', 'Lunan Qu', '河北省唐山市路南区', 'LNB');
INSERT INTO `t_area` VALUES ('440', '38', '路北区', '4-38-440', '3', 'Lubei Qu', '河北省唐山市路北区', 'LBQ');
INSERT INTO `t_area` VALUES ('441', '38', '古冶区', '4-38-441', '3', 'Guye Qu', '河北省唐山市古冶区', 'GYE');
INSERT INTO `t_area` VALUES ('442', '38', '开平区', '4-38-442', '3', 'Kaiping Qu', '河北省唐山市开平区', 'KPQ');
INSERT INTO `t_area` VALUES ('443', '38', '丰南区', '4-38-443', '3', 'Fengnan Qu', '河北省唐山市丰南区', '2');
INSERT INTO `t_area` VALUES ('444', '38', '丰润区', '4-38-444', '3', 'Fengrun Qu', '河北省唐山市丰润区', '2');
INSERT INTO `t_area` VALUES ('445', '38', '滦县', '4-38-445', '3', 'Luan Xian', '河北省唐山市滦县', 'LUA');
INSERT INTO `t_area` VALUES ('446', '38', '滦南县', '4-38-446', '3', 'Luannan Xian', '河北省唐山市滦南县', 'LNJ');
INSERT INTO `t_area` VALUES ('447', '38', '乐亭县', '4-38-447', '3', 'Leting Xian', '河北省唐山市乐亭县', 'LTJ');
INSERT INTO `t_area` VALUES ('448', '38', '迁西县', '4-38-448', '3', 'Qianxi Xian', '河北省唐山市迁西县', 'QXX');
INSERT INTO `t_area` VALUES ('449', '38', '玉田县', '4-38-449', '3', 'Yutian Xian', '河北省唐山市玉田县', 'YTJ');
INSERT INTO `t_area` VALUES ('450', '38', '唐海县', '4-38-450', '3', 'Tanghai Xian ', '河北省唐山市唐海县', 'THA');
INSERT INTO `t_area` VALUES ('451', '38', '遵化市', '4-38-451', '3', 'Zunhua Shi', '河北省唐山市遵化市', 'ZNH');
INSERT INTO `t_area` VALUES ('452', '38', '迁安市', '4-38-452', '3', 'Qian,an Shi', '河北省唐山市迁安市', 'QAS');
INSERT INTO `t_area` VALUES ('454', '39', '海港区', '4-39-454', '3', 'Haigang Qu', '河北省秦皇岛市海港区', 'HGG');
INSERT INTO `t_area` VALUES ('455', '39', '山海关区', '4-39-455', '3', 'Shanhaiguan Qu', '河北省秦皇岛市山海关区', 'SHG');
INSERT INTO `t_area` VALUES ('456', '39', '北戴河区', '4-39-456', '3', 'Beidaihe Qu', '河北省秦皇岛市北戴河区', 'BDH');
INSERT INTO `t_area` VALUES ('457', '39', '青龙满族自治县', '4-39-457', '3', 'Qinglong Manzu Zizhixian', '河北省秦皇岛市青龙满族自治县', 'QLM');
INSERT INTO `t_area` VALUES ('458', '39', '昌黎县', '4-39-458', '3', 'Changli Xian', '河北省秦皇岛市昌黎县', 'CGL');
INSERT INTO `t_area` VALUES ('459', '39', '抚宁县', '4-39-459', '3', 'Funing Xian ', '河北省秦皇岛市抚宁县', 'FUN');
INSERT INTO `t_area` VALUES ('460', '39', '卢龙县', '4-39-460', '3', 'Lulong Xian', '河北省秦皇岛市卢龙县', 'LLG');
INSERT INTO `t_area` VALUES ('462', '40', '邯山区', '4-40-462', '3', 'Hanshan Qu', '河北省邯郸市邯山区', 'HHD');
INSERT INTO `t_area` VALUES ('463', '40', '丛台区', '4-40-463', '3', 'Congtai Qu', '河北省邯郸市丛台区', 'CTQ');
INSERT INTO `t_area` VALUES ('464', '40', '复兴区', '4-40-464', '3', 'Fuxing Qu', '河北省邯郸市复兴区', 'FXQ');
INSERT INTO `t_area` VALUES ('465', '40', '峰峰矿区', '4-40-465', '3', 'Fengfeng Kuangqu', '河北省邯郸市峰峰矿区', 'FFK');
INSERT INTO `t_area` VALUES ('466', '40', '邯郸县', '4-40-466', '3', 'Handan Xian ', '河北省邯郸市邯郸县', 'HDX');
INSERT INTO `t_area` VALUES ('467', '40', '临漳县', '4-40-467', '3', 'Linzhang Xian ', '河北省邯郸市临漳县', 'LNZ');
INSERT INTO `t_area` VALUES ('468', '40', '成安县', '4-40-468', '3', 'Cheng,an Xian', '河北省邯郸市成安县', 'CAJ');
INSERT INTO `t_area` VALUES ('469', '40', '大名县', '4-40-469', '3', 'Daming Xian', '河北省邯郸市大名县', 'DMX');
INSERT INTO `t_area` VALUES ('470', '40', '涉县', '4-40-470', '3', 'She Xian', '河北省邯郸市涉县', 'SEJ');
INSERT INTO `t_area` VALUES ('471', '40', '磁县', '4-40-471', '3', 'Ci Xian', '河北省邯郸市磁县', 'CIX');
INSERT INTO `t_area` VALUES ('472', '40', '肥乡县', '4-40-472', '3', 'Feixiang Xian', '河北省邯郸市肥乡县', 'FXJ');
INSERT INTO `t_area` VALUES ('473', '40', '永年县', '4-40-473', '3', 'Yongnian Xian', '河北省邯郸市永年县', 'YON');
INSERT INTO `t_area` VALUES ('474', '40', '邱县', '4-40-474', '3', 'Qiu Xian', '河北省邯郸市邱县', 'QIU');
INSERT INTO `t_area` VALUES ('475', '40', '鸡泽县', '4-40-475', '3', 'Jize Xian', '河北省邯郸市鸡泽县', 'JZE');
INSERT INTO `t_area` VALUES ('476', '40', '广平县', '4-40-476', '3', 'Guangping Xian ', '河北省邯郸市广平县', 'GPX');
INSERT INTO `t_area` VALUES ('477', '40', '馆陶县', '4-40-477', '3', 'Guantao Xian', '河北省邯郸市馆陶县', 'GTO');
INSERT INTO `t_area` VALUES ('478', '40', '魏县', '4-40-478', '3', 'Wei Xian ', '河北省邯郸市魏县', 'WEI');
INSERT INTO `t_area` VALUES ('479', '40', '曲周县', '4-40-479', '3', 'Quzhou Xian ', '河北省邯郸市曲周县', 'QZX');
INSERT INTO `t_area` VALUES ('480', '40', '武安市', '4-40-480', '3', 'Wu,an Shi', '河北省邯郸市武安市', 'WUA');
INSERT INTO `t_area` VALUES ('482', '41', '桥东区', '4-41-482', '3', 'Qiaodong Qu', '河北省邢台市桥东区', 'QDG');
INSERT INTO `t_area` VALUES ('483', '41', '桥西区', '4-41-483', '3', 'Qiaoxi Qu', '河北省邢台市桥西区', 'QXT');
INSERT INTO `t_area` VALUES ('484', '41', '邢台县', '4-41-484', '3', 'Xingtai Xian', '河北省邢台市邢台县', 'XTJ');
INSERT INTO `t_area` VALUES ('485', '41', '临城县', '4-41-485', '3', 'Lincheng Xian ', '河北省邢台市临城县', 'LNC');
INSERT INTO `t_area` VALUES ('486', '41', '内丘县', '4-41-486', '3', 'Neiqiu Xian ', '河北省邢台市内丘县', 'NQU');
INSERT INTO `t_area` VALUES ('487', '41', '柏乡县', '4-41-487', '3', 'Baixiang Xian', '河北省邢台市柏乡县', 'BXG');
INSERT INTO `t_area` VALUES ('488', '41', '隆尧县', '4-41-488', '3', 'Longyao Xian', '河北省邢台市隆尧县', 'LYO');
INSERT INTO `t_area` VALUES ('489', '41', '任县', '4-41-489', '3', 'Ren Xian', '河北省邢台市任县', 'REN');
INSERT INTO `t_area` VALUES ('490', '41', '南和县', '4-41-490', '3', 'Nanhe Xian', '河北省邢台市南和县', 'NHX');
INSERT INTO `t_area` VALUES ('491', '41', '宁晋县', '4-41-491', '3', 'Ningjin Xian', '河北省邢台市宁晋县', 'NJN');
INSERT INTO `t_area` VALUES ('492', '41', '巨鹿县', '4-41-492', '3', 'Julu Xian', '河北省邢台市巨鹿县', 'JLU');
INSERT INTO `t_area` VALUES ('493', '41', '新河县', '4-41-493', '3', 'Xinhe Xian ', '河北省邢台市新河县', 'XHJ');
INSERT INTO `t_area` VALUES ('494', '41', '广宗县', '4-41-494', '3', 'Guangzong Xian ', '河北省邢台市广宗县', 'GZJ');
INSERT INTO `t_area` VALUES ('495', '41', '平乡县', '4-41-495', '3', 'Pingxiang Xian', '河北省邢台市平乡县', 'PXX');
INSERT INTO `t_area` VALUES ('496', '41', '威县', '4-41-496', '3', 'Wei Xian ', '河北省邢台市威县', 'WEX');
INSERT INTO `t_area` VALUES ('497', '41', '清河县', '4-41-497', '3', 'Qinghe Xian', '河北省邢台市清河县', 'QHE');
INSERT INTO `t_area` VALUES ('498', '41', '临西县', '4-41-498', '3', 'Linxi Xian', '河北省邢台市临西县', 'LXI');
INSERT INTO `t_area` VALUES ('499', '41', '南宫市', '4-41-499', '3', 'Nangong Shi', '河北省邢台市南宫市', 'NGO');
INSERT INTO `t_area` VALUES ('500', '41', '沙河市', '4-41-500', '3', 'Shahe Shi', '河北省邢台市沙河市', 'SHS');
INSERT INTO `t_area` VALUES ('502', '42', '新市区', '4-42-502', '3', 'Xinshi Qu', '河北省保定市新市区', '2');
INSERT INTO `t_area` VALUES ('503', '42', '北市区', '4-42-503', '3', 'Beishi Qu', '河北省保定市北市区', 'BSI');
INSERT INTO `t_area` VALUES ('504', '42', '南市区', '4-42-504', '3', 'Nanshi Qu', '河北省保定市南市区', 'NSB');
INSERT INTO `t_area` VALUES ('505', '42', '满城县', '4-42-505', '3', 'Mancheng Xian ', '河北省保定市满城县', 'MCE');
INSERT INTO `t_area` VALUES ('506', '42', '清苑县', '4-42-506', '3', 'Qingyuan Xian', '河北省保定市清苑县', 'QYJ');
INSERT INTO `t_area` VALUES ('507', '42', '涞水县', '4-42-507', '3', 'Laishui Xian', '河北省保定市涞水县', 'LSM');
INSERT INTO `t_area` VALUES ('508', '42', '阜平县', '4-42-508', '3', 'Fuping Xian ', '河北省保定市阜平县', 'FUP');
INSERT INTO `t_area` VALUES ('509', '42', '徐水县', '4-42-509', '3', 'Xushui Xian ', '河北省保定市徐水县', 'XSJ');
INSERT INTO `t_area` VALUES ('510', '42', '定兴县', '4-42-510', '3', 'Dingxing Xian ', '河北省保定市定兴县', 'DXG');
INSERT INTO `t_area` VALUES ('511', '42', '唐县', '4-42-511', '3', 'Tang Xian ', '河北省保定市唐县', 'TAG');
INSERT INTO `t_area` VALUES ('512', '42', '高阳县', '4-42-512', '3', 'Gaoyang Xian ', '河北省保定市高阳县', 'GAY');
INSERT INTO `t_area` VALUES ('513', '42', '容城县', '4-42-513', '3', 'Rongcheng Xian ', '河北省保定市容城县', 'RCX');
INSERT INTO `t_area` VALUES ('514', '42', '涞源县', '4-42-514', '3', 'Laiyuan Xian ', '河北省保定市涞源县', 'LIY');
INSERT INTO `t_area` VALUES ('515', '42', '望都县', '4-42-515', '3', 'Wangdu Xian ', '河北省保定市望都县', 'WDU');
INSERT INTO `t_area` VALUES ('516', '42', '安新县', '4-42-516', '3', 'Anxin Xian ', '河北省保定市安新县', 'AXX');
INSERT INTO `t_area` VALUES ('517', '42', '易县', '4-42-517', '3', 'Yi Xian', '河北省保定市易县', 'YII');
INSERT INTO `t_area` VALUES ('518', '42', '曲阳县', '4-42-518', '3', 'Quyang Xian ', '河北省保定市曲阳县', 'QUY');
INSERT INTO `t_area` VALUES ('519', '42', '蠡县', '4-42-519', '3', 'Li Xian', '河北省保定市蠡县', 'LXJ');
INSERT INTO `t_area` VALUES ('520', '42', '顺平县', '4-42-520', '3', 'Shunping Xian ', '河北省保定市顺平县', 'SPI');
INSERT INTO `t_area` VALUES ('521', '42', '博野县', '4-42-521', '3', 'Boye Xian ', '河北省保定市博野县', 'BYE');
INSERT INTO `t_area` VALUES ('522', '42', '雄县', '4-42-522', '3', 'Xiong Xian', '河北省保定市雄县', 'XOX');
INSERT INTO `t_area` VALUES ('523', '42', '涿州市', '4-42-523', '3', 'Zhuozhou Shi', '河北省保定市涿州市', 'ZZO');
INSERT INTO `t_area` VALUES ('524', '42', '定州市', '4-42-524', '3', 'Dingzhou Shi ', '河北省保定市定州市', 'DZO');
INSERT INTO `t_area` VALUES ('525', '42', '安国市', '4-42-525', '3', 'Anguo Shi ', '河北省保定市安国市', 'AGO');
INSERT INTO `t_area` VALUES ('526', '42', '高碑店市', '4-42-526', '3', 'Gaobeidian Shi', '河北省保定市高碑店市', 'GBD');
INSERT INTO `t_area` VALUES ('528', '43', '桥东区', '4-43-528', '3', 'Qiaodong Qu', '河北省张家口市桥东区', 'QDZ');
INSERT INTO `t_area` VALUES ('529', '43', '桥西区', '4-43-529', '3', 'Qiaoxi Qu', '河北省张家口市桥西区', 'QXI');
INSERT INTO `t_area` VALUES ('530', '43', '宣化区', '4-43-530', '3', 'Xuanhua Qu', '河北省张家口市宣化区', 'XHZ');
INSERT INTO `t_area` VALUES ('531', '43', '下花园区', '4-43-531', '3', 'Xiahuayuan Qu ', '河北省张家口市下花园区', 'XHY');
INSERT INTO `t_area` VALUES ('532', '43', '宣化县', '4-43-532', '3', 'Xuanhua Xian ', '河北省张家口市宣化县', 'XHX');
INSERT INTO `t_area` VALUES ('533', '43', '张北县', '4-43-533', '3', 'Zhangbei Xian ', '河北省张家口市张北县', 'ZGB');
INSERT INTO `t_area` VALUES ('534', '43', '康保县', '4-43-534', '3', 'Kangbao Xian', '河北省张家口市康保县', 'KBO');
INSERT INTO `t_area` VALUES ('535', '43', '沽源县', '4-43-535', '3', 'Guyuan Xian', '河北省张家口市沽源县', '2');
INSERT INTO `t_area` VALUES ('536', '43', '尚义县', '4-43-536', '3', 'Shangyi Xian', '河北省张家口市尚义县', 'SYK');
INSERT INTO `t_area` VALUES ('537', '43', '蔚县', '4-43-537', '3', 'Yu Xian', '河北省张家口市蔚县', 'YXJ');
INSERT INTO `t_area` VALUES ('538', '43', '阳原县', '4-43-538', '3', 'Yangyuan Xian', '河北省张家口市阳原县', 'YYN');
INSERT INTO `t_area` VALUES ('539', '43', '怀安县', '4-43-539', '3', 'Huai,an Xian', '河北省张家口市怀安县', 'HAX');
INSERT INTO `t_area` VALUES ('540', '43', '万全县', '4-43-540', '3', 'Wanquan Xian ', '河北省张家口市万全县', 'WQN');
INSERT INTO `t_area` VALUES ('541', '43', '怀来县', '4-43-541', '3', 'Huailai Xian', '河北省张家口市怀来县', 'HLA');
INSERT INTO `t_area` VALUES ('542', '43', '涿鹿县', '4-43-542', '3', 'Zhuolu Xian ', '河北省张家口市涿鹿县', 'ZLU');
INSERT INTO `t_area` VALUES ('543', '43', '赤城县', '4-43-543', '3', 'Chicheng Xian', '河北省张家口市赤城县', 'CCX');
INSERT INTO `t_area` VALUES ('544', '43', '崇礼县', '4-43-544', '3', 'Chongli Xian', '河北省张家口市崇礼县', 'COL');
INSERT INTO `t_area` VALUES ('546', '44', '双桥区', '4-44-546', '3', 'Shuangqiao Qu ', '河北省承德市双桥区', 'SQO');
INSERT INTO `t_area` VALUES ('547', '44', '双滦区', '4-44-547', '3', 'Shuangluan Qu', '河北省承德市双滦区', 'SLQ');
INSERT INTO `t_area` VALUES ('548', '44', '鹰手营子矿区', '4-44-548', '3', 'Yingshouyingzi Kuangqu', '河北省承德市鹰手营子矿区', 'YSY');
INSERT INTO `t_area` VALUES ('549', '44', '承德县', '4-44-549', '3', 'Chengde Xian', '河北省承德市承德县', 'CDX');
INSERT INTO `t_area` VALUES ('550', '44', '兴隆县', '4-44-550', '3', 'Xinglong Xian', '河北省承德市兴隆县', 'XLJ');
INSERT INTO `t_area` VALUES ('551', '44', '平泉县', '4-44-551', '3', 'Pingquan Xian', '河北省承德市平泉县', 'PQN');
INSERT INTO `t_area` VALUES ('552', '44', '滦平县', '4-44-552', '3', 'Luanping Xian ', '河北省承德市滦平县', 'LUP');
INSERT INTO `t_area` VALUES ('553', '44', '隆化县', '4-44-553', '3', 'Longhua Xian', '河北省承德市隆化县', 'LHJ');
INSERT INTO `t_area` VALUES ('554', '44', '丰宁满族自治县', '4-44-554', '3', 'Fengning Manzu Zizhixian', '河北省承德市丰宁满族自治县', 'FNJ');
INSERT INTO `t_area` VALUES ('555', '44', '宽城满族自治县', '4-44-555', '3', 'Kuancheng Manzu Zizhixian', '河北省承德市宽城满族自治县', 'KCX');
INSERT INTO `t_area` VALUES ('556', '44', '围场满族蒙古族自治县', '4-44-556', '3', 'Weichang Manzu Menggolzu Zizhixian', '河北省承德市围场满族蒙古族自治县', 'WCJ');
INSERT INTO `t_area` VALUES ('558', '45', '新华区', '4-45-558', '3', 'Xinhua Qu', '河北省沧州市新华区', 'XHF');
INSERT INTO `t_area` VALUES ('559', '45', '运河区', '4-45-559', '3', 'Yunhe Qu', '河北省沧州市运河区', 'YHC');
INSERT INTO `t_area` VALUES ('560', '45', '沧县', '4-45-560', '3', 'Cang Xian', '河北省沧州市沧县', 'CAG');
INSERT INTO `t_area` VALUES ('561', '45', '青县', '4-45-561', '3', 'Qing Xian', '河北省沧州市青县', 'QIG');
INSERT INTO `t_area` VALUES ('562', '45', '东光县', '4-45-562', '3', 'Dongguang Xian ', '河北省沧州市东光县', 'DGU');
INSERT INTO `t_area` VALUES ('563', '45', '海兴县', '4-45-563', '3', 'Haixing Xian', '河北省沧州市海兴县', 'HXG');
INSERT INTO `t_area` VALUES ('564', '45', '盐山县', '4-45-564', '3', 'Yanshan Xian', '河北省沧州市盐山县', 'YNS');
INSERT INTO `t_area` VALUES ('565', '45', '肃宁县', '4-45-565', '3', 'Suning Xian ', '河北省沧州市肃宁县', 'SNG');
INSERT INTO `t_area` VALUES ('566', '45', '南皮县', '4-45-566', '3', 'Nanpi Xian', '河北省沧州市南皮县', 'NPI');
INSERT INTO `t_area` VALUES ('567', '45', '吴桥县', '4-45-567', '3', 'Wuqiao Xian ', '河北省沧州市吴桥县', 'WUQ');
INSERT INTO `t_area` VALUES ('568', '45', '献县', '4-45-568', '3', 'Xian Xian ', '河北省沧州市献县', 'XXN');
INSERT INTO `t_area` VALUES ('569', '45', '孟村回族自治县', '4-45-569', '3', 'Mengcun Huizu Zizhixian', '河北省沧州市孟村回族自治县', 'MCN');
INSERT INTO `t_area` VALUES ('570', '45', '泊头市', '4-45-570', '3', 'Botou Shi ', '河北省沧州市泊头市', 'BOT');
INSERT INTO `t_area` VALUES ('571', '45', '任丘市', '4-45-571', '3', 'Renqiu Shi', '河北省沧州市任丘市', 'RQS');
INSERT INTO `t_area` VALUES ('572', '45', '黄骅市', '4-45-572', '3', 'Huanghua Shi', '河北省沧州市黄骅市', 'HHJ');
INSERT INTO `t_area` VALUES ('573', '45', '河间市', '4-45-573', '3', 'Hejian Shi', '河北省沧州市河间市', 'HJN');
INSERT INTO `t_area` VALUES ('575', '46', '安次区', '4-46-575', '3', 'Anci Qu', '河北省廊坊市安次区', 'ACI');
INSERT INTO `t_area` VALUES ('576', '46', '广阳区', '4-46-576', '3', 'Guangyang Qu', '河北省廊坊市广阳区', '2');
INSERT INTO `t_area` VALUES ('577', '46', '固安县', '4-46-577', '3', 'Gu,an Xian', '河北省廊坊市固安县', 'GUA');
INSERT INTO `t_area` VALUES ('578', '46', '永清县', '4-46-578', '3', 'Yongqing Xian ', '河北省廊坊市永清县', 'YQG');
INSERT INTO `t_area` VALUES ('579', '46', '香河县', '4-46-579', '3', 'Xianghe Xian', '河北省廊坊市香河县', 'XGH');
INSERT INTO `t_area` VALUES ('580', '46', '大城县', '4-46-580', '3', 'Dacheng Xian', '河北省廊坊市大城县', 'DCJ');
INSERT INTO `t_area` VALUES ('581', '46', '文安县', '4-46-581', '3', 'Wen,an Xian', '河北省廊坊市文安县', 'WEA');
INSERT INTO `t_area` VALUES ('582', '46', '大厂回族自治县', '4-46-582', '3', 'Dachang Huizu Zizhixian', '河北省廊坊市大厂回族自治县', 'DCG');
INSERT INTO `t_area` VALUES ('583', '46', '霸州市', '4-46-583', '3', 'Bazhou Shi', '河北省廊坊市霸州市', 'BZO');
INSERT INTO `t_area` VALUES ('584', '46', '三河市', '4-46-584', '3', 'Sanhe Shi', '河北省廊坊市三河市', 'SNH');
INSERT INTO `t_area` VALUES ('586', '47', '桃城区', '4-47-586', '3', 'Taocheng Qu', '河北省衡水市桃城区', 'TOC');
INSERT INTO `t_area` VALUES ('587', '47', '枣强县', '4-47-587', '3', 'Zaoqiang Xian ', '河北省衡水市枣强县', 'ZQJ');
INSERT INTO `t_area` VALUES ('588', '47', '武邑县', '4-47-588', '3', 'Wuyi Xian', '河北省衡水市武邑县', 'WYI');
INSERT INTO `t_area` VALUES ('589', '47', '武强县', '4-47-589', '3', 'Wuqiang Xian ', '河北省衡水市武强县', 'WQG');
INSERT INTO `t_area` VALUES ('590', '47', '饶阳县', '4-47-590', '3', 'Raoyang Xian', '河北省衡水市饶阳县', 'RYG');
INSERT INTO `t_area` VALUES ('591', '47', '安平县', '4-47-591', '3', 'Anping Xian', '河北省衡水市安平县', 'APG');
INSERT INTO `t_area` VALUES ('592', '47', '故城县', '4-47-592', '3', 'Gucheng Xian', '河北省衡水市故城县', 'GCE');
INSERT INTO `t_area` VALUES ('593', '47', '景县', '4-47-593', '3', 'Jing Xian ', '河北省衡水市景县', 'JIG');
INSERT INTO `t_area` VALUES ('594', '47', '阜城县', '4-47-594', '3', 'Fucheng Xian ', '河北省衡水市阜城县', 'FCE');
INSERT INTO `t_area` VALUES ('595', '47', '冀州市', '4-47-595', '3', 'Jizhou Shi ', '河北省衡水市冀州市', 'JIZ');
INSERT INTO `t_area` VALUES ('596', '47', '深州市', '4-47-596', '3', 'Shenzhou Shi', '河北省衡水市深州市', 'SNZ');
INSERT INTO `t_area` VALUES ('598', '48', '小店区', '5-48-598', '3', 'Xiaodian Qu', '山西省太原市小店区', 'XDQ');
INSERT INTO `t_area` VALUES ('599', '48', '迎泽区', '5-48-599', '3', 'Yingze Qu', '山西省太原市迎泽区', 'YZT');
INSERT INTO `t_area` VALUES ('600', '48', '杏花岭区', '5-48-600', '3', 'Xinghualing Qu', '山西省太原市杏花岭区', 'XHL');
INSERT INTO `t_area` VALUES ('601', '48', '尖草坪区', '5-48-601', '3', 'Jiancaoping Qu', '山西省太原市尖草坪区', 'JCP');
INSERT INTO `t_area` VALUES ('602', '48', '万柏林区', '5-48-602', '3', 'Wanbailin Qu', '山西省太原市万柏林区', 'WBL');
INSERT INTO `t_area` VALUES ('603', '48', '晋源区', '5-48-603', '3', 'Jinyuan Qu', '山西省太原市晋源区', 'JYM');
INSERT INTO `t_area` VALUES ('604', '48', '清徐县', '5-48-604', '3', 'Qingxu Xian ', '山西省太原市清徐县', 'QXU');
INSERT INTO `t_area` VALUES ('605', '48', '阳曲县', '5-48-605', '3', 'Yangqu Xian ', '山西省太原市阳曲县', 'YGQ');
INSERT INTO `t_area` VALUES ('606', '48', '娄烦县', '5-48-606', '3', 'Loufan Xian', '山西省太原市娄烦县', 'LFA');
INSERT INTO `t_area` VALUES ('607', '48', '古交市', '5-48-607', '3', 'Gujiao Shi', '山西省太原市古交市', 'GUJ');
INSERT INTO `t_area` VALUES ('611', '49', '南郊区', '5-49-611', '3', 'Nanjiao Qu', '山西省大同市南郊区', 'NJQ');
INSERT INTO `t_area` VALUES ('612', '49', '新荣区', '5-49-612', '3', 'Xinrong Qu', '山西省大同市新荣区', 'XRQ');
INSERT INTO `t_area` VALUES ('613', '49', '阳高县', '5-49-613', '3', 'Yanggao Xian ', '山西省大同市阳高县', 'YGO');
INSERT INTO `t_area` VALUES ('614', '49', '天镇县', '5-49-614', '3', 'Tianzhen Xian ', '山西省大同市天镇县', 'TZE');
INSERT INTO `t_area` VALUES ('615', '49', '广灵县', '5-49-615', '3', 'Guangling Xian ', '山西省大同市广灵县', 'GLJ');
INSERT INTO `t_area` VALUES ('616', '49', '灵丘县', '5-49-616', '3', 'Lingqiu Xian ', '山西省大同市灵丘县', 'LQX');
INSERT INTO `t_area` VALUES ('617', '49', '浑源县', '5-49-617', '3', 'Hunyuan Xian', '山西省大同市浑源县', 'HYM');
INSERT INTO `t_area` VALUES ('618', '49', '左云县', '5-49-618', '3', 'Zuoyun Xian', '山西省大同市左云县', 'ZUY');
INSERT INTO `t_area` VALUES ('619', '49', '大同县', '5-49-619', '3', 'Datong Xian ', '山西省大同市大同县', 'DTX');
INSERT INTO `t_area` VALUES ('624', '50', '平定县', '5-50-624', '3', 'Pingding Xian', '山西省阳泉市平定县', 'PDG');
INSERT INTO `t_area` VALUES ('625', '50', '盂县', '5-50-625', '3', 'Yu Xian', '山西省阳泉市盂县', 'YUX');
INSERT INTO `t_area` VALUES ('629', '51', '长治县', '5-51-629', '3', 'Changzhi Xian', '山西省长治市长治县', 'CZI');
INSERT INTO `t_area` VALUES ('630', '51', '襄垣县', '5-51-630', '3', 'Xiangyuan Xian', '山西省长治市襄垣县', 'XYJ');
INSERT INTO `t_area` VALUES ('631', '51', '屯留县', '5-51-631', '3', 'Tunliu Xian', '山西省长治市屯留县', 'TNL');
INSERT INTO `t_area` VALUES ('632', '51', '平顺县', '5-51-632', '3', 'Pingshun Xian', '山西省长治市平顺县', 'PSX');
INSERT INTO `t_area` VALUES ('633', '51', '黎城县', '5-51-633', '3', 'Licheng Xian', '山西省长治市黎城县', 'LIC');
INSERT INTO `t_area` VALUES ('634', '51', '壶关县', '5-51-634', '3', 'Huguan Xian', '山西省长治市壶关县', 'HGN');
INSERT INTO `t_area` VALUES ('635', '51', '长子县', '5-51-635', '3', 'Zhangzi Xian ', '山西省长治市长子县', 'ZHZ');
INSERT INTO `t_area` VALUES ('636', '51', '武乡县', '5-51-636', '3', 'Wuxiang Xian', '山西省长治市武乡县', 'WXG');
INSERT INTO `t_area` VALUES ('637', '51', '沁县', '5-51-637', '3', 'Qin Xian', '山西省长治市沁县', 'QIN');
INSERT INTO `t_area` VALUES ('638', '51', '沁源县', '5-51-638', '3', 'Qinyuan Xian ', '山西省长治市沁源县', 'QYU');
INSERT INTO `t_area` VALUES ('639', '51', '潞城市', '5-51-639', '3', 'Lucheng Shi', '山西省长治市潞城市', 'LCS');
INSERT INTO `t_area` VALUES ('642', '52', '沁水县', '5-52-642', '3', 'Qinshui Xian', '山西省晋城市沁水县', 'QSI');
INSERT INTO `t_area` VALUES ('643', '52', '阳城县', '5-52-643', '3', 'Yangcheng Xian ', '山西省晋城市阳城县', 'YGC');
INSERT INTO `t_area` VALUES ('644', '52', '陵川县', '5-52-644', '3', 'Lingchuan Xian', '山西省晋城市陵川县', 'LGC');
INSERT INTO `t_area` VALUES ('645', '52', '泽州县', '5-52-645', '3', 'Zezhou Xian', '山西省晋城市泽州县', 'ZEZ');
INSERT INTO `t_area` VALUES ('646', '52', '高平市', '5-52-646', '3', 'Gaoping Shi ', '山西省晋城市高平市', 'GPG');
INSERT INTO `t_area` VALUES ('648', '53', '朔城区', '5-53-648', '3', 'Shuocheng Qu', '山西省朔州市朔城区', 'SCH');
INSERT INTO `t_area` VALUES ('649', '53', '平鲁区', '5-53-649', '3', 'Pinglu Qu', '山西省朔州市平鲁区', 'PLU');
INSERT INTO `t_area` VALUES ('650', '53', '山阴县', '5-53-650', '3', 'Shanyin Xian', '山西省朔州市山阴县', 'SYP');
INSERT INTO `t_area` VALUES ('651', '53', '应县', '5-53-651', '3', 'Ying Xian', '山西省朔州市应县', 'YIG');
INSERT INTO `t_area` VALUES ('652', '53', '右玉县', '5-53-652', '3', 'Youyu Xian ', '山西省朔州市右玉县', 'YOY');
INSERT INTO `t_area` VALUES ('653', '53', '怀仁县', '5-53-653', '3', 'Huairen Xian', '山西省朔州市怀仁县', 'HRN');
INSERT INTO `t_area` VALUES ('655', '54', '榆次区', '5-54-655', '3', 'Yuci Qu', '山西省晋中市榆次区', '2');
INSERT INTO `t_area` VALUES ('656', '54', '榆社县', '5-54-656', '3', 'Yushe Xian', '山西省晋中市榆社县', '2');
INSERT INTO `t_area` VALUES ('657', '54', '左权县', '5-54-657', '3', 'Zuoquan Xian', '山西省晋中市左权县', '2');
INSERT INTO `t_area` VALUES ('658', '54', '和顺县', '5-54-658', '3', 'Heshun Xian', '山西省晋中市和顺县', '2');
INSERT INTO `t_area` VALUES ('659', '54', '昔阳县', '5-54-659', '3', 'Xiyang Xian', '山西省晋中市昔阳县', '2');
INSERT INTO `t_area` VALUES ('660', '54', '寿阳县', '5-54-660', '3', 'Shouyang Xian', '山西省晋中市寿阳县', '2');
INSERT INTO `t_area` VALUES ('661', '54', '太谷县', '5-54-661', '3', 'Taigu Xian', '山西省晋中市太谷县', '2');
INSERT INTO `t_area` VALUES ('662', '54', '祁县', '5-54-662', '3', 'Qi Xian', '山西省晋中市祁县', '2');
INSERT INTO `t_area` VALUES ('663', '54', '平遥县', '5-54-663', '3', 'Pingyao Xian', '山西省晋中市平遥县', '2');
INSERT INTO `t_area` VALUES ('664', '54', '灵石县', '5-54-664', '3', 'Lingshi Xian', '山西省晋中市灵石县', '2');
INSERT INTO `t_area` VALUES ('665', '54', '介休市', '5-54-665', '3', 'Jiexiu Shi', '山西省晋中市介休市', '2');
INSERT INTO `t_area` VALUES ('667', '55', '盐湖区', '5-55-667', '3', 'Yanhu Qu', '山西省运城市盐湖区', '2');
INSERT INTO `t_area` VALUES ('668', '55', '临猗县', '5-55-668', '3', 'Linyi Xian', '山西省运城市临猗县', '2');
INSERT INTO `t_area` VALUES ('669', '55', '万荣县', '5-55-669', '3', 'Wanrong Xian', '山西省运城市万荣县', '2');
INSERT INTO `t_area` VALUES ('670', '55', '闻喜县', '5-55-670', '3', 'Wenxi Xian', '山西省运城市闻喜县', '2');
INSERT INTO `t_area` VALUES ('671', '55', '稷山县', '5-55-671', '3', 'Jishan Xian', '山西省运城市稷山县', '2');
INSERT INTO `t_area` VALUES ('672', '55', '新绛县', '5-55-672', '3', 'Xinjiang Xian', '山西省运城市新绛县', '2');
INSERT INTO `t_area` VALUES ('673', '55', '绛县', '5-55-673', '3', 'Jiang Xian', '山西省运城市绛县', '2');
INSERT INTO `t_area` VALUES ('674', '55', '垣曲县', '5-55-674', '3', 'Yuanqu Xian', '山西省运城市垣曲县', '2');
INSERT INTO `t_area` VALUES ('675', '55', '夏县', '5-55-675', '3', 'Xia Xian ', '山西省运城市夏县', '2');
INSERT INTO `t_area` VALUES ('676', '55', '平陆县', '5-55-676', '3', 'Pinglu Xian', '山西省运城市平陆县', '2');
INSERT INTO `t_area` VALUES ('677', '55', '芮城县', '5-55-677', '3', 'Ruicheng Xian', '山西省运城市芮城县', '2');
INSERT INTO `t_area` VALUES ('678', '55', '永济市', '5-55-678', '3', 'Yongji Shi ', '山西省运城市永济市', '2');
INSERT INTO `t_area` VALUES ('679', '55', '河津市', '5-55-679', '3', 'Hejin Shi', '山西省运城市河津市', '2');
INSERT INTO `t_area` VALUES ('681', '56', '忻府区', '5-56-681', '3', 'Xinfu Qu', '山西省忻州市忻府区', '2');
INSERT INTO `t_area` VALUES ('682', '56', '定襄县', '5-56-682', '3', 'Dingxiang Xian', '山西省忻州市定襄县', '2');
INSERT INTO `t_area` VALUES ('683', '56', '五台县', '5-56-683', '3', 'Wutai Xian', '山西省忻州市五台县', '2');
INSERT INTO `t_area` VALUES ('684', '56', '代县', '5-56-684', '3', 'Dai Xian', '山西省忻州市代县', '2');
INSERT INTO `t_area` VALUES ('685', '56', '繁峙县', '5-56-685', '3', 'Fanshi Xian', '山西省忻州市繁峙县', '2');
INSERT INTO `t_area` VALUES ('686', '56', '宁武县', '5-56-686', '3', 'Ningwu Xian', '山西省忻州市宁武县', '2');
INSERT INTO `t_area` VALUES ('687', '56', '静乐县', '5-56-687', '3', 'Jingle Xian', '山西省忻州市静乐县', '2');
INSERT INTO `t_area` VALUES ('688', '56', '神池县', '5-56-688', '3', 'Shenchi Xian', '山西省忻州市神池县', '2');
INSERT INTO `t_area` VALUES ('689', '56', '五寨县', '5-56-689', '3', 'Wuzhai Xian', '山西省忻州市五寨县', '2');
INSERT INTO `t_area` VALUES ('690', '56', '岢岚县', '5-56-690', '3', 'Kelan Xian', '山西省忻州市岢岚县', '2');
INSERT INTO `t_area` VALUES ('691', '56', '河曲县', '5-56-691', '3', 'Hequ Xian ', '山西省忻州市河曲县', '2');
INSERT INTO `t_area` VALUES ('692', '56', '保德县', '5-56-692', '3', 'Baode Xian', '山西省忻州市保德县', '2');
INSERT INTO `t_area` VALUES ('693', '56', '偏关县', '5-56-693', '3', 'Pianguan Xian', '山西省忻州市偏关县', '2');
INSERT INTO `t_area` VALUES ('694', '56', '原平市', '5-56-694', '3', 'Yuanping Shi', '山西省忻州市原平市', '2');
INSERT INTO `t_area` VALUES ('696', '57', '尧都区', '5-57-696', '3', 'Yaodu Qu', '山西省临汾市尧都区', '2');
INSERT INTO `t_area` VALUES ('697', '57', '曲沃县', '5-57-697', '3', 'Quwo Xian ', '山西省临汾市曲沃县', '2');
INSERT INTO `t_area` VALUES ('698', '57', '翼城县', '5-57-698', '3', 'Yicheng Xian', '山西省临汾市翼城县', '2');
INSERT INTO `t_area` VALUES ('699', '57', '襄汾县', '5-57-699', '3', 'Xiangfen Xian', '山西省临汾市襄汾县', '2');
INSERT INTO `t_area` VALUES ('700', '57', '洪洞县', '5-57-700', '3', 'Hongtong Xian', '山西省临汾市洪洞县', '2');
INSERT INTO `t_area` VALUES ('701', '57', '古县', '5-57-701', '3', 'Gu Xian', '山西省临汾市古县', '2');
INSERT INTO `t_area` VALUES ('702', '57', '安泽县', '5-57-702', '3', 'Anze Xian', '山西省临汾市安泽县', '2');
INSERT INTO `t_area` VALUES ('703', '57', '浮山县', '5-57-703', '3', 'Fushan Xian ', '山西省临汾市浮山县', '2');
INSERT INTO `t_area` VALUES ('704', '57', '吉县', '5-57-704', '3', 'Ji Xian', '山西省临汾市吉县', '2');
INSERT INTO `t_area` VALUES ('705', '57', '乡宁县', '5-57-705', '3', 'Xiangning Xian', '山西省临汾市乡宁县', '2');
INSERT INTO `t_area` VALUES ('706', '57', '大宁县', '5-57-706', '3', 'Daning Xian', '山西省临汾市大宁县', '2');
INSERT INTO `t_area` VALUES ('707', '57', '隰县', '5-57-707', '3', 'Xi Xian', '山西省临汾市隰县', '2');
INSERT INTO `t_area` VALUES ('708', '57', '永和县', '5-57-708', '3', 'Yonghe Xian', '山西省临汾市永和县', '2');
INSERT INTO `t_area` VALUES ('709', '57', '蒲县', '5-57-709', '3', 'Pu Xian', '山西省临汾市蒲县', '2');
INSERT INTO `t_area` VALUES ('710', '57', '汾西县', '5-57-710', '3', 'Fenxi Xian', '山西省临汾市汾西县', '2');
INSERT INTO `t_area` VALUES ('711', '57', '侯马市', '5-57-711', '3', 'Houma Shi ', '山西省临汾市侯马市', '2');
INSERT INTO `t_area` VALUES ('712', '57', '霍州市', '5-57-712', '3', 'Huozhou Shi ', '山西省临汾市霍州市', '2');
INSERT INTO `t_area` VALUES ('714', '58', '离石区', '5-58-714', '3', 'Lishi Qu', '山西省吕梁市离石区', '2');
INSERT INTO `t_area` VALUES ('715', '58', '文水县', '5-58-715', '3', 'Wenshui Xian', '山西省吕梁市文水县', '2');
INSERT INTO `t_area` VALUES ('716', '58', '交城县', '5-58-716', '3', 'Jiaocheng Xian', '山西省吕梁市交城县', '2');
INSERT INTO `t_area` VALUES ('717', '58', '兴县', '5-58-717', '3', 'Xing Xian', '山西省吕梁市兴县', '2');
INSERT INTO `t_area` VALUES ('718', '58', '临县', '5-58-718', '3', 'Lin Xian ', '山西省吕梁市临县', '2');
INSERT INTO `t_area` VALUES ('719', '58', '柳林县', '5-58-719', '3', 'Liulin Xian', '山西省吕梁市柳林县', '2');
INSERT INTO `t_area` VALUES ('720', '58', '石楼县', '5-58-720', '3', 'Shilou Xian', '山西省吕梁市石楼县', '2');
INSERT INTO `t_area` VALUES ('721', '58', '岚县', '5-58-721', '3', 'Lan Xian', '山西省吕梁市岚县', '2');
INSERT INTO `t_area` VALUES ('722', '58', '方山县', '5-58-722', '3', 'Fangshan Xian', '山西省吕梁市方山县', '2');
INSERT INTO `t_area` VALUES ('723', '58', '中阳县', '5-58-723', '3', 'Zhongyang Xian', '山西省吕梁市中阳县', '2');
INSERT INTO `t_area` VALUES ('724', '58', '交口县', '5-58-724', '3', 'Jiaokou Xian', '山西省吕梁市交口县', '2');
INSERT INTO `t_area` VALUES ('725', '58', '孝义市', '5-58-725', '3', 'Xiaoyi Shi', '山西省吕梁市孝义市', '2');
INSERT INTO `t_area` VALUES ('726', '58', '汾阳市', '5-58-726', '3', 'Fenyang Shi', '山西省吕梁市汾阳市', '2');
INSERT INTO `t_area` VALUES ('728', '59', '新城区', '6-59-728', '3', 'Xincheng Qu', '内蒙古自治区呼和浩特市新城区', 'XCN');
INSERT INTO `t_area` VALUES ('729', '59', '回民区', '6-59-729', '3', 'Huimin Qu', '内蒙古自治区呼和浩特市回民区', 'HMQ');
INSERT INTO `t_area` VALUES ('730', '59', '玉泉区', '6-59-730', '3', 'Yuquan Qu', '内蒙古自治区呼和浩特市玉泉区', 'YQN');
INSERT INTO `t_area` VALUES ('731', '59', '赛罕区', '6-59-731', '3', 'Saihan Qu', '内蒙古自治区呼和浩特市赛罕区', '2');
INSERT INTO `t_area` VALUES ('732', '59', '土默特左旗', '6-59-732', '3', 'Tumd Zuoqi', '内蒙古自治区呼和浩特市土默特左旗', 'TUZ');
INSERT INTO `t_area` VALUES ('733', '59', '托克托县', '6-59-733', '3', 'Togtoh Xian', '内蒙古自治区呼和浩特市托克托县', 'TOG');
INSERT INTO `t_area` VALUES ('734', '59', '和林格尔县', '6-59-734', '3', 'Horinger Xian', '内蒙古自治区呼和浩特市和林格尔县', 'HOR');
INSERT INTO `t_area` VALUES ('735', '59', '清水河县', '6-59-735', '3', 'Qingshuihe Xian', '内蒙古自治区呼和浩特市清水河县', 'QSH');
INSERT INTO `t_area` VALUES ('736', '59', '武川县', '6-59-736', '3', 'Wuchuan Xian', '内蒙古自治区呼和浩特市武川县', 'WCX');
INSERT INTO `t_area` VALUES ('738', '60', '东河区', '6-60-738', '3', 'Donghe Qu', '内蒙古自治区包头市东河区', 'DHE');
INSERT INTO `t_area` VALUES ('739', '60', '昆都仑区', '6-60-739', '3', 'Kundulun Qu', '内蒙古自治区包头市昆都仑区', '2');
INSERT INTO `t_area` VALUES ('740', '60', '青山区', '6-60-740', '3', 'Qingshan Qu', '内蒙古自治区包头市青山区', 'QSB');
INSERT INTO `t_area` VALUES ('741', '60', '石拐区', '6-60-741', '3', 'Shiguai Qu', '内蒙古自治区包头市石拐区', '2');
INSERT INTO `t_area` VALUES ('742', '60', '白云鄂博矿区', '6-60-742', '3', 'Baiyun Kuangqu', '内蒙古自治区包头市白云鄂博矿区', '2');
INSERT INTO `t_area` VALUES ('743', '60', '九原区', '6-60-743', '3', 'Jiuyuan Qu', '内蒙古自治区包头市九原区', '2');
INSERT INTO `t_area` VALUES ('744', '60', '土默特右旗', '6-60-744', '3', 'Tumd Youqi', '内蒙古自治区包头市土默特右旗', 'TUY');
INSERT INTO `t_area` VALUES ('745', '60', '固阳县', '6-60-745', '3', 'Guyang Xian', '内蒙古自治区包头市固阳县', 'GYM');
INSERT INTO `t_area` VALUES ('746', '60', '达尔罕茂明安联合旗', '6-60-746', '3', 'Darhan Muminggan Lianheqi', '内蒙古自治区包头市达尔罕茂明安联合旗', 'DML');
INSERT INTO `t_area` VALUES ('748', '61', '海勃湾区', '6-61-748', '3', 'Haibowan Qu', '内蒙古自治区乌海市海勃湾区', 'HBW');
INSERT INTO `t_area` VALUES ('749', '61', '海南区', '6-61-749', '3', 'Hainan Qu', '内蒙古自治区乌海市海南区', 'HNU');
INSERT INTO `t_area` VALUES ('750', '61', '乌达区', '6-61-750', '3', 'Ud Qu', '内蒙古自治区乌海市乌达区', 'UDQ');
INSERT INTO `t_area` VALUES ('752', '62', '红山区', '6-62-752', '3', 'Hongshan Qu', '内蒙古自治区赤峰市红山区', 'HSZ');
INSERT INTO `t_area` VALUES ('753', '62', '元宝山区', '6-62-753', '3', 'Yuanbaoshan Qu', '内蒙古自治区赤峰市元宝山区', 'YBO');
INSERT INTO `t_area` VALUES ('754', '62', '松山区', '6-62-754', '3', 'Songshan Qu', '内蒙古自治区赤峰市松山区', 'SSQ');
INSERT INTO `t_area` VALUES ('755', '62', '阿鲁科尔沁旗', '6-62-755', '3', 'Ar Horqin Qi', '内蒙古自治区赤峰市阿鲁科尔沁旗', 'AHO');
INSERT INTO `t_area` VALUES ('756', '62', '巴林左旗', '6-62-756', '3', 'Bairin Zuoqi', '内蒙古自治区赤峰市巴林左旗', 'BAZ');
INSERT INTO `t_area` VALUES ('757', '62', '巴林右旗', '6-62-757', '3', 'Bairin Youqi', '内蒙古自治区赤峰市巴林右旗', 'BAY');
INSERT INTO `t_area` VALUES ('758', '62', '林西县', '6-62-758', '3', 'Linxi Xian', '内蒙古自治区赤峰市林西县', 'LXM');
INSERT INTO `t_area` VALUES ('759', '62', '克什克腾旗', '6-62-759', '3', 'Hexigten Qi', '内蒙古自治区赤峰市克什克腾旗', 'HXT');
INSERT INTO `t_area` VALUES ('760', '62', '翁牛特旗', '6-62-760', '3', 'Ongniud Qi', '内蒙古自治区赤峰市翁牛特旗', 'ONG');
INSERT INTO `t_area` VALUES ('761', '62', '喀喇沁旗', '6-62-761', '3', 'Harqin Qi', '内蒙古自治区赤峰市喀喇沁旗', 'HAR');
INSERT INTO `t_area` VALUES ('762', '62', '宁城县', '6-62-762', '3', 'Ningcheng Xian', '内蒙古自治区赤峰市宁城县', 'NCH');
INSERT INTO `t_area` VALUES ('763', '62', '敖汉旗', '6-62-763', '3', 'Aohan Qi', '内蒙古自治区赤峰市敖汉旗', 'AHN');
INSERT INTO `t_area` VALUES ('765', '63', '科尔沁区', '6-63-765', '3', 'Keermi Qu', '内蒙古自治区通辽市科尔沁区', '2');
INSERT INTO `t_area` VALUES ('766', '63', '科尔沁左翼中旗', '6-63-766', '3', 'Horqin Zuoyi Zhongqi', '内蒙古自治区通辽市科尔沁左翼中旗', '2');
INSERT INTO `t_area` VALUES ('767', '63', '科尔沁左翼后旗', '6-63-767', '3', 'Horqin Zuoyi Houqi', '内蒙古自治区通辽市科尔沁左翼后旗', '2');
INSERT INTO `t_area` VALUES ('768', '63', '开鲁县', '6-63-768', '3', 'Kailu Xian', '内蒙古自治区通辽市开鲁县', '2');
INSERT INTO `t_area` VALUES ('769', '63', '库伦旗', '6-63-769', '3', 'Hure Qi', '内蒙古自治区通辽市库伦旗', '2');
INSERT INTO `t_area` VALUES ('770', '63', '奈曼旗', '6-63-770', '3', 'Naiman Qi', '内蒙古自治区通辽市奈曼旗', '2');
INSERT INTO `t_area` VALUES ('771', '63', '扎鲁特旗', '6-63-771', '3', 'Jarud Qi', '内蒙古自治区通辽市扎鲁特旗', '2');
INSERT INTO `t_area` VALUES ('772', '63', '霍林郭勒市', '6-63-772', '3', 'Holingol Shi', '内蒙古自治区通辽市霍林郭勒市', '2');
INSERT INTO `t_area` VALUES ('773', '64', '东胜区', '6-64-773', '3', 'Dongsheng Qu', '内蒙古自治区鄂尔多斯市东胜区', '2');
INSERT INTO `t_area` VALUES ('774', '64', '达拉特旗', '6-64-774', '3', 'Dalad Qi', '内蒙古自治区鄂尔多斯市达拉特旗', '2');
INSERT INTO `t_area` VALUES ('775', '64', '准格尔旗', '6-64-775', '3', 'Jungar Qi', '内蒙古自治区鄂尔多斯市准格尔旗', '2');
INSERT INTO `t_area` VALUES ('776', '64', '鄂托克前旗', '6-64-776', '3', 'Otog Qianqi', '内蒙古自治区鄂尔多斯市鄂托克前旗', '2');
INSERT INTO `t_area` VALUES ('777', '64', '鄂托克旗', '6-64-777', '3', 'Otog Qi', '内蒙古自治区鄂尔多斯市鄂托克旗', '2');
INSERT INTO `t_area` VALUES ('778', '64', '杭锦旗', '6-64-778', '3', 'Hanggin Qi', '内蒙古自治区鄂尔多斯市杭锦旗', '2');
INSERT INTO `t_area` VALUES ('779', '64', '乌审旗', '6-64-779', '3', 'Uxin Qi', '内蒙古自治区鄂尔多斯市乌审旗', '2');
INSERT INTO `t_area` VALUES ('780', '64', '伊金霍洛旗', '6-64-780', '3', 'Ejin Horo Qi', '内蒙古自治区鄂尔多斯市伊金霍洛旗', '2');
INSERT INTO `t_area` VALUES ('782', '65', '海拉尔区', '6-65-782', '3', 'Hailaer Qu', '内蒙古自治区呼伦贝尔市海拉尔区', '2');
INSERT INTO `t_area` VALUES ('783', '65', '阿荣旗', '6-65-783', '3', 'Arun Qi', '内蒙古自治区呼伦贝尔市阿荣旗', '2');
INSERT INTO `t_area` VALUES ('784', '65', '莫力达瓦达斡尔族自治旗', '6-65-784', '3', 'Morin Dawa Daurzu Zizhiqi', '内蒙古自治区呼伦贝尔市莫力达瓦达斡尔族自治旗', '2');
INSERT INTO `t_area` VALUES ('785', '65', '鄂伦春自治旗', '6-65-785', '3', 'Oroqen Zizhiqi', '内蒙古自治区呼伦贝尔市鄂伦春自治旗', '2');
INSERT INTO `t_area` VALUES ('786', '65', '鄂温克族自治旗', '6-65-786', '3', 'Ewenkizu Zizhiqi', '内蒙古自治区呼伦贝尔市鄂温克族自治旗', '2');
INSERT INTO `t_area` VALUES ('787', '65', '陈巴尔虎旗', '6-65-787', '3', 'Chen Barag Qi', '内蒙古自治区呼伦贝尔市陈巴尔虎旗', '2');
INSERT INTO `t_area` VALUES ('788', '65', '新巴尔虎左旗', '6-65-788', '3', 'Xin Barag Zuoqi', '内蒙古自治区呼伦贝尔市新巴尔虎左旗', '2');
INSERT INTO `t_area` VALUES ('789', '65', '新巴尔虎右旗', '6-65-789', '3', 'Xin Barag Youqi', '内蒙古自治区呼伦贝尔市新巴尔虎右旗', '2');
INSERT INTO `t_area` VALUES ('790', '65', '满洲里市', '6-65-790', '3', 'Manzhouli Shi', '内蒙古自治区呼伦贝尔市满洲里市', '2');
INSERT INTO `t_area` VALUES ('791', '65', '牙克石市', '6-65-791', '3', 'Yakeshi Shi', '内蒙古自治区呼伦贝尔市牙克石市', '2');
INSERT INTO `t_area` VALUES ('792', '65', '扎兰屯市', '6-65-792', '3', 'Zalantun Shi', '内蒙古自治区呼伦贝尔市扎兰屯市', '2');
INSERT INTO `t_area` VALUES ('793', '65', '额尔古纳市', '6-65-793', '3', 'Ergun Shi', '内蒙古自治区呼伦贝尔市额尔古纳市', '2');
INSERT INTO `t_area` VALUES ('794', '65', '根河市', '6-65-794', '3', 'Genhe Shi', '内蒙古自治区呼伦贝尔市根河市', '2');
INSERT INTO `t_area` VALUES ('796', '66', '临河区', '6-66-796', '3', 'Linhe Qu', '内蒙古自治区巴彦淖尔市临河区', '2');
INSERT INTO `t_area` VALUES ('797', '66', '五原县', '6-66-797', '3', 'Wuyuan Xian', '内蒙古自治区巴彦淖尔市五原县', '2');
INSERT INTO `t_area` VALUES ('798', '66', '磴口县', '6-66-798', '3', 'Dengkou Xian', '内蒙古自治区巴彦淖尔市磴口县', '2');
INSERT INTO `t_area` VALUES ('799', '66', '乌拉特前旗', '6-66-799', '3', 'Urad Qianqi', '内蒙古自治区巴彦淖尔市乌拉特前旗', '2');
INSERT INTO `t_area` VALUES ('800', '66', '乌拉特中旗', '6-66-800', '3', 'Urad Zhongqi', '内蒙古自治区巴彦淖尔市乌拉特中旗', '2');
INSERT INTO `t_area` VALUES ('801', '66', '乌拉特后旗', '6-66-801', '3', 'Urad Houqi', '内蒙古自治区巴彦淖尔市乌拉特后旗', '2');
INSERT INTO `t_area` VALUES ('802', '66', '杭锦后旗', '6-66-802', '3', 'Hanggin Houqi', '内蒙古自治区巴彦淖尔市杭锦后旗', '2');
INSERT INTO `t_area` VALUES ('804', '67', '集宁区', '6-67-804', '3', 'Jining Qu', '内蒙古自治区乌兰察布市集宁区', '2');
INSERT INTO `t_area` VALUES ('805', '67', '卓资县', '6-67-805', '3', 'Zhuozi Xian', '内蒙古自治区乌兰察布市卓资县', '2');
INSERT INTO `t_area` VALUES ('806', '67', '化德县', '6-67-806', '3', 'Huade Xian', '内蒙古自治区乌兰察布市化德县', '2');
INSERT INTO `t_area` VALUES ('807', '67', '商都县', '6-67-807', '3', 'Shangdu Xian', '内蒙古自治区乌兰察布市商都县', '2');
INSERT INTO `t_area` VALUES ('808', '67', '兴和县', '6-67-808', '3', 'Xinghe Xian', '内蒙古自治区乌兰察布市兴和县', '2');
INSERT INTO `t_area` VALUES ('809', '67', '凉城县', '6-67-809', '3', 'Liangcheng Xian', '内蒙古自治区乌兰察布市凉城县', '2');
INSERT INTO `t_area` VALUES ('810', '67', '察哈尔右翼前旗', '6-67-810', '3', 'Qahar Youyi Qianqi', '内蒙古自治区乌兰察布市察哈尔右翼前旗', '2');
INSERT INTO `t_area` VALUES ('811', '67', '察哈尔右翼中旗', '6-67-811', '3', 'Qahar Youyi Zhongqi', '内蒙古自治区乌兰察布市察哈尔右翼中旗', '2');
INSERT INTO `t_area` VALUES ('812', '67', '察哈尔右翼后旗', '6-67-812', '3', 'Qahar Youyi Houqi', '内蒙古自治区乌兰察布市察哈尔右翼后旗', '2');
INSERT INTO `t_area` VALUES ('813', '67', '四子王旗', '6-67-813', '3', 'Dorbod Qi', '内蒙古自治区乌兰察布市四子王旗', '2');
INSERT INTO `t_area` VALUES ('814', '67', '丰镇市', '6-67-814', '3', 'Fengzhen Shi', '内蒙古自治区乌兰察布市丰镇市', '2');
INSERT INTO `t_area` VALUES ('815', '68', '乌兰浩特市', '6-68-815', '3', 'Ulan Hot Shi', '内蒙古自治区兴安盟乌兰浩特市', 'ULO');
INSERT INTO `t_area` VALUES ('816', '68', '阿尔山市', '6-68-816', '3', 'Arxan Shi', '内蒙古自治区兴安盟阿尔山市', 'ARS');
INSERT INTO `t_area` VALUES ('817', '68', '科尔沁右翼前旗', '6-68-817', '3', 'Horqin Youyi Qianqi', '内蒙古自治区兴安盟科尔沁右翼前旗', 'HYQ');
INSERT INTO `t_area` VALUES ('818', '68', '科尔沁右翼中旗', '6-68-818', '3', 'Horqin Youyi Zhongqi', '内蒙古自治区兴安盟科尔沁右翼中旗', 'HYZ');
INSERT INTO `t_area` VALUES ('819', '68', '扎赉特旗', '6-68-819', '3', 'Jalaid Qi', '内蒙古自治区兴安盟扎赉特旗', 'JAL');
INSERT INTO `t_area` VALUES ('820', '68', '突泉县', '6-68-820', '3', 'Tuquan Xian', '内蒙古自治区兴安盟突泉县', 'TUQ');
INSERT INTO `t_area` VALUES ('821', '69', '二连浩特市', '6-69-821', '3', 'Erenhot Shi', '内蒙古自治区锡林郭勒盟二连浩特市', 'ERC');
INSERT INTO `t_area` VALUES ('822', '69', '锡林浩特市', '6-69-822', '3', 'Xilinhot Shi', '内蒙古自治区锡林郭勒盟锡林浩特市', 'XLI');
INSERT INTO `t_area` VALUES ('823', '69', '阿巴嘎旗', '6-69-823', '3', 'Abag Qi', '内蒙古自治区锡林郭勒盟阿巴嘎旗', 'ABG');
INSERT INTO `t_area` VALUES ('824', '69', '苏尼特左旗', '6-69-824', '3', 'Sonid Zuoqi', '内蒙古自治区锡林郭勒盟苏尼特左旗', 'SOZ');
INSERT INTO `t_area` VALUES ('825', '69', '苏尼特右旗', '6-69-825', '3', 'Sonid Youqi', '内蒙古自治区锡林郭勒盟苏尼特右旗', 'SOY');
INSERT INTO `t_area` VALUES ('826', '69', '东乌珠穆沁旗', '6-69-826', '3', 'Dong Ujimqin Qi', '内蒙古自治区锡林郭勒盟东乌珠穆沁旗', 'DUJ');
INSERT INTO `t_area` VALUES ('827', '69', '西乌珠穆沁旗', '6-69-827', '3', 'Xi Ujimqin Qi', '内蒙古自治区锡林郭勒盟西乌珠穆沁旗', 'XUJ');
INSERT INTO `t_area` VALUES ('828', '69', '太仆寺旗', '6-69-828', '3', 'Taibus Qi', '内蒙古自治区锡林郭勒盟太仆寺旗', 'TAB');
INSERT INTO `t_area` VALUES ('829', '69', '镶黄旗', '6-69-829', '3', 'Xianghuang(Hobot Xar) Qi', '内蒙古自治区锡林郭勒盟镶黄旗', 'XHG');
INSERT INTO `t_area` VALUES ('830', '69', '正镶白旗', '6-69-830', '3', 'Zhengxiangbai(Xulun Hobot Qagan)Qi', '内蒙古自治区锡林郭勒盟正镶白旗', 'ZXB');
INSERT INTO `t_area` VALUES ('831', '69', '正蓝旗', '6-69-831', '3', 'Zhenglan(Xulun Hoh)Qi', '内蒙古自治区锡林郭勒盟正蓝旗', 'ZLM');
INSERT INTO `t_area` VALUES ('832', '69', '多伦县', '6-69-832', '3', 'Duolun (Dolonnur)Xian', '内蒙古自治区锡林郭勒盟多伦县', 'DLM');
INSERT INTO `t_area` VALUES ('833', '70', '阿拉善左旗', '6-70-833', '3', 'Alxa Zuoqi', '内蒙古自治区阿拉善盟阿拉善左旗', 'ALZ');
INSERT INTO `t_area` VALUES ('834', '70', '阿拉善右旗', '6-70-834', '3', 'Alxa Youqi', '内蒙古自治区阿拉善盟阿拉善右旗', 'ALY');
INSERT INTO `t_area` VALUES ('835', '70', '额济纳旗', '6-70-835', '3', 'Ejin Qi', '内蒙古自治区阿拉善盟额济纳旗', 'EJI');
INSERT INTO `t_area` VALUES ('837', '71', '和平区', '7-71-837', '3', 'Heping Qu', '辽宁省沈阳市和平区', 'HEP');
INSERT INTO `t_area` VALUES ('838', '71', '沈河区', '7-71-838', '3', 'Shenhe Qu ', '辽宁省沈阳市沈河区', 'SHQ');
INSERT INTO `t_area` VALUES ('839', '71', '大东区', '7-71-839', '3', 'Dadong Qu ', '辽宁省沈阳市大东区', 'DDQ');
INSERT INTO `t_area` VALUES ('840', '71', '皇姑区', '7-71-840', '3', 'Huanggu Qu', '辽宁省沈阳市皇姑区', 'HGU');
INSERT INTO `t_area` VALUES ('841', '71', '铁西区', '7-71-841', '3', 'Tiexi Qu', '辽宁省沈阳市铁西区', 'TXI');
INSERT INTO `t_area` VALUES ('842', '71', '苏家屯区', '7-71-842', '3', 'Sujiatun Qu', '辽宁省沈阳市苏家屯区', 'SJT');
INSERT INTO `t_area` VALUES ('843', '71', '东陵区', '7-71-843', '3', 'Dongling Qu ', '辽宁省沈阳市东陵区', 'DLQ');
INSERT INTO `t_area` VALUES ('844', '71', '沈北新区', '7-71-844', '3', 'Xinchengzi Qu', '辽宁省沈阳市沈北新区', '2');
INSERT INTO `t_area` VALUES ('845', '71', '于洪区', '7-71-845', '3', 'Yuhong Qu ', '辽宁省沈阳市于洪区', 'YHQ');
INSERT INTO `t_area` VALUES ('846', '71', '辽中县', '7-71-846', '3', 'Liaozhong Xian', '辽宁省沈阳市辽中县', 'LZL');
INSERT INTO `t_area` VALUES ('847', '71', '康平县', '7-71-847', '3', 'Kangping Xian', '辽宁省沈阳市康平县', 'KPG');
INSERT INTO `t_area` VALUES ('848', '71', '法库县', '7-71-848', '3', 'Faku Xian', '辽宁省沈阳市法库县', 'FKU');
INSERT INTO `t_area` VALUES ('849', '71', '新民市', '7-71-849', '3', 'Xinmin Shi', '辽宁省沈阳市新民市', 'XMS');
INSERT INTO `t_area` VALUES ('851', '72', '中山区', '7-72-851', '3', 'Zhongshan Qu', '辽宁省大连市中山区', 'ZSD');
INSERT INTO `t_area` VALUES ('852', '72', '西岗区', '7-72-852', '3', 'Xigang Qu', '辽宁省大连市西岗区', 'XGD');
INSERT INTO `t_area` VALUES ('853', '72', '沙河口区', '7-72-853', '3', 'Shahekou Qu', '辽宁省大连市沙河口区', 'SHK');
INSERT INTO `t_area` VALUES ('854', '72', '甘井子区', '7-72-854', '3', 'Ganjingzi Qu', '辽宁省大连市甘井子区', 'GJZ');
INSERT INTO `t_area` VALUES ('855', '72', '旅顺口区', '7-72-855', '3', 'Lvshunkou Qu ', '辽宁省大连市旅顺口区', 'LSK');
INSERT INTO `t_area` VALUES ('856', '72', '金州区', '7-72-856', '3', 'Jinzhou Qu', '辽宁省大连市金州区', 'JZH');
INSERT INTO `t_area` VALUES ('857', '72', '长海县', '7-72-857', '3', 'Changhai Xian', '辽宁省大连市长海县', 'CHX');
INSERT INTO `t_area` VALUES ('858', '72', '瓦房店市', '7-72-858', '3', 'Wafangdian Shi', '辽宁省大连市瓦房店市', 'WFD');
INSERT INTO `t_area` VALUES ('859', '72', '普兰店市', '7-72-859', '3', 'Pulandian Shi', '辽宁省大连市普兰店市', 'PLD');
INSERT INTO `t_area` VALUES ('860', '72', '庄河市', '7-72-860', '3', 'Zhuanghe Shi', '辽宁省大连市庄河市', 'ZHH');
INSERT INTO `t_area` VALUES ('862', '73', '铁东区', '7-73-862', '3', 'Tiedong Qu ', '辽宁省鞍山市铁东区', 'TED');
INSERT INTO `t_area` VALUES ('863', '73', '铁西区', '7-73-863', '3', 'Tiexi Qu', '辽宁省鞍山市铁西区', 'TXL');
INSERT INTO `t_area` VALUES ('864', '73', '立山区', '7-73-864', '3', 'Lishan Qu', '辽宁省鞍山市立山区', 'LAS');
INSERT INTO `t_area` VALUES ('865', '73', '千山区', '7-73-865', '3', 'Qianshan Qu ', '辽宁省鞍山市千山区', 'QSQ');
INSERT INTO `t_area` VALUES ('866', '73', '台安县', '7-73-866', '3', 'Tai,an Xian', '辽宁省鞍山市台安县', 'TAX');
INSERT INTO `t_area` VALUES ('867', '73', '岫岩满族自治县', '7-73-867', '3', 'Xiuyan Manzu Zizhixian', '辽宁省鞍山市岫岩满族自治县', 'XYL');
INSERT INTO `t_area` VALUES ('868', '73', '海城市', '7-73-868', '3', 'Haicheng Shi', '辽宁省鞍山市海城市', 'HCL');
INSERT INTO `t_area` VALUES ('870', '74', '新抚区', '7-74-870', '3', 'Xinfu Qu', '辽宁省抚顺市新抚区', 'XFU');
INSERT INTO `t_area` VALUES ('871', '74', '东洲区', '7-74-871', '3', 'Dongzhou Qu', '辽宁省抚顺市东洲区', '2');
INSERT INTO `t_area` VALUES ('872', '74', '望花区', '7-74-872', '3', 'Wanghua Qu', '辽宁省抚顺市望花区', 'WHF');
INSERT INTO `t_area` VALUES ('873', '74', '顺城区', '7-74-873', '3', 'Shuncheng Qu', '辽宁省抚顺市顺城区', 'SCF');
INSERT INTO `t_area` VALUES ('874', '74', '抚顺县', '7-74-874', '3', 'Fushun Xian', '辽宁省抚顺市抚顺县', 'FSX');
INSERT INTO `t_area` VALUES ('875', '74', '新宾满族自治县', '7-74-875', '3', 'Xinbinmanzuzizhi Xian', '辽宁省抚顺市新宾满族自治县', '2');
INSERT INTO `t_area` VALUES ('876', '74', '清原满族自治县', '7-74-876', '3', 'Qingyuanmanzuzizhi Xian', '辽宁省抚顺市清原满族自治县', '2');
INSERT INTO `t_area` VALUES ('878', '75', '平山区', '7-75-878', '3', 'Pingshan Qu', '辽宁省本溪市平山区', 'PSN');
INSERT INTO `t_area` VALUES ('879', '75', '溪湖区', '7-75-879', '3', 'Xihu Qu ', '辽宁省本溪市溪湖区', 'XHB');
INSERT INTO `t_area` VALUES ('880', '75', '明山区', '7-75-880', '3', 'Mingshan Qu', '辽宁省本溪市明山区', 'MSB');
INSERT INTO `t_area` VALUES ('881', '75', '南芬区', '7-75-881', '3', 'Nanfen Qu', '辽宁省本溪市南芬区', 'NFQ');
INSERT INTO `t_area` VALUES ('882', '75', '本溪满族自治县', '7-75-882', '3', 'Benxi Manzu Zizhixian', '辽宁省本溪市本溪满族自治县', 'BXX');
INSERT INTO `t_area` VALUES ('883', '75', '桓仁满族自治县', '7-75-883', '3', 'Huanren Manzu Zizhixian', '辽宁省本溪市桓仁满族自治县', 'HRL');
INSERT INTO `t_area` VALUES ('885', '76', '元宝区', '7-76-885', '3', 'Yuanbao Qu', '辽宁省丹东市元宝区', 'YBD');
INSERT INTO `t_area` VALUES ('886', '76', '振兴区', '7-76-886', '3', 'Zhenxing Qu ', '辽宁省丹东市振兴区', 'ZXQ');
INSERT INTO `t_area` VALUES ('887', '76', '振安区', '7-76-887', '3', 'Zhen,an Qu', '辽宁省丹东市振安区', 'ZAQ');
INSERT INTO `t_area` VALUES ('888', '76', '宽甸满族自治县', '7-76-888', '3', 'Kuandian Manzu Zizhixian', '辽宁省丹东市宽甸满族自治县', 'KDN');
INSERT INTO `t_area` VALUES ('889', '76', '东港市', '7-76-889', '3', 'Donggang Shi', '辽宁省丹东市东港市', 'DGS');
INSERT INTO `t_area` VALUES ('890', '76', '凤城市', '7-76-890', '3', 'Fengcheng Shi', '辽宁省丹东市凤城市', 'FCL');
INSERT INTO `t_area` VALUES ('892', '77', '古塔区', '7-77-892', '3', 'Guta Qu', '辽宁省锦州市古塔区', 'GTQ');
INSERT INTO `t_area` VALUES ('893', '77', '凌河区', '7-77-893', '3', 'Linghe Qu', '辽宁省锦州市凌河区', 'LHF');
INSERT INTO `t_area` VALUES ('894', '77', '太和区', '7-77-894', '3', 'Taihe Qu', '辽宁省锦州市太和区', '2');
INSERT INTO `t_area` VALUES ('895', '77', '黑山县', '7-77-895', '3', 'Heishan Xian', '辽宁省锦州市黑山县', 'HSL');
INSERT INTO `t_area` VALUES ('896', '77', '义县', '7-77-896', '3', 'Yi Xian', '辽宁省锦州市义县', 'YXL');
INSERT INTO `t_area` VALUES ('897', '77', '凌海市', '7-77-897', '3', 'Linghai Shi ', '辽宁省锦州市凌海市', 'LHL');
INSERT INTO `t_area` VALUES ('898', '77', '北镇市', '7-77-898', '3', 'Beining Shi', '辽宁省锦州市北镇市', '2');
INSERT INTO `t_area` VALUES ('900', '78', '站前区', '7-78-900', '3', 'Zhanqian Qu', '辽宁省营口市站前区', 'ZQQ');
INSERT INTO `t_area` VALUES ('901', '78', '西市区', '7-78-901', '3', 'Xishi Qu', '辽宁省营口市西市区', 'XII');
INSERT INTO `t_area` VALUES ('902', '78', '鲅鱼圈区', '7-78-902', '3', 'Bayuquan Qu', '辽宁省营口市鲅鱼圈区', 'BYQ');
INSERT INTO `t_area` VALUES ('903', '78', '老边区', '7-78-903', '3', 'Laobian Qu', '辽宁省营口市老边区', 'LOB');
INSERT INTO `t_area` VALUES ('904', '78', '盖州市', '7-78-904', '3', 'Gaizhou Shi', '辽宁省营口市盖州市', 'GZU');
INSERT INTO `t_area` VALUES ('905', '78', '大石桥市', '7-78-905', '3', 'Dashiqiao Shi', '辽宁省营口市大石桥市', 'DSQ');
INSERT INTO `t_area` VALUES ('907', '79', '海州区', '7-79-907', '3', 'Haizhou Qu', '辽宁省阜新市海州区', 'HZF');
INSERT INTO `t_area` VALUES ('908', '79', '新邱区', '7-79-908', '3', 'Xinqiu Qu', '辽宁省阜新市新邱区', 'XQF');
INSERT INTO `t_area` VALUES ('909', '79', '太平区', '7-79-909', '3', 'Taiping Qu', '辽宁省阜新市太平区', 'TPG');
INSERT INTO `t_area` VALUES ('910', '79', '清河门区', '7-79-910', '3', 'Qinghemen Qu', '辽宁省阜新市清河门区', 'QHM');
INSERT INTO `t_area` VALUES ('911', '79', '细河区', '7-79-911', '3', 'Xihe Qu', '辽宁省阜新市细河区', 'XHO');
INSERT INTO `t_area` VALUES ('912', '79', '阜新蒙古族自治县', '7-79-912', '3', 'Fuxin Mongolzu Zizhixian', '辽宁省阜新市阜新蒙古族自治县', 'FXX');
INSERT INTO `t_area` VALUES ('913', '79', '彰武县', '7-79-913', '3', 'Zhangwu Xian', '辽宁省阜新市彰武县', 'ZWU');
INSERT INTO `t_area` VALUES ('915', '80', '白塔区', '7-80-915', '3', 'Baita Qu', '辽宁省辽阳市白塔区', 'BTL');
INSERT INTO `t_area` VALUES ('916', '80', '文圣区', '7-80-916', '3', 'Wensheng Qu', '辽宁省辽阳市文圣区', 'WST');
INSERT INTO `t_area` VALUES ('917', '80', '宏伟区', '7-80-917', '3', 'Hongwei Qu', '辽宁省辽阳市宏伟区', 'HWQ');
INSERT INTO `t_area` VALUES ('918', '80', '弓长岭区', '7-80-918', '3', 'Gongchangling Qu', '辽宁省辽阳市弓长岭区', 'GCL');
INSERT INTO `t_area` VALUES ('919', '80', '太子河区', '7-80-919', '3', 'Taizihe Qu', '辽宁省辽阳市太子河区', 'TZH');
INSERT INTO `t_area` VALUES ('920', '80', '辽阳县', '7-80-920', '3', 'Liaoyang Xian', '辽宁省辽阳市辽阳县', 'LYX');
INSERT INTO `t_area` VALUES ('921', '80', '灯塔市', '7-80-921', '3', 'Dengta Shi', '辽宁省辽阳市灯塔市', 'DTL');
INSERT INTO `t_area` VALUES ('923', '81', '双台子区', '7-81-923', '3', 'Shuangtaizi Qu', '辽宁省盘锦市双台子区', 'STZ');
INSERT INTO `t_area` VALUES ('924', '81', '兴隆台区', '7-81-924', '3', 'Xinglongtai Qu', '辽宁省盘锦市兴隆台区', 'XLT');
INSERT INTO `t_area` VALUES ('925', '81', '大洼县', '7-81-925', '3', 'Dawa Xian', '辽宁省盘锦市大洼县', 'DWA');
INSERT INTO `t_area` VALUES ('926', '81', '盘山县', '7-81-926', '3', 'Panshan Xian', '辽宁省盘锦市盘山县', 'PNS');
INSERT INTO `t_area` VALUES ('928', '82', '银州区', '7-82-928', '3', 'Yinzhou Qu', '辽宁省铁岭市银州区', 'YZU');
INSERT INTO `t_area` VALUES ('929', '82', '清河区', '7-82-929', '3', 'Qinghe Qu', '辽宁省铁岭市清河区', 'QHQ');
INSERT INTO `t_area` VALUES ('930', '82', '铁岭县', '7-82-930', '3', 'Tieling Xian', '辽宁省铁岭市铁岭县', 'TLG');
INSERT INTO `t_area` VALUES ('931', '82', '西丰县', '7-82-931', '3', 'Xifeng Xian', '辽宁省铁岭市西丰县', 'XIF');
INSERT INTO `t_area` VALUES ('932', '82', '昌图县', '7-82-932', '3', 'Changtu Xian', '辽宁省铁岭市昌图县', 'CTX');
INSERT INTO `t_area` VALUES ('933', '82', '调兵山市', '7-82-933', '3', 'Diaobingshan Shi', '辽宁省铁岭市调兵山市', '2');
INSERT INTO `t_area` VALUES ('934', '82', '开原市', '7-82-934', '3', 'Kaiyuan Shi', '辽宁省铁岭市开原市', 'KYS');
INSERT INTO `t_area` VALUES ('936', '83', '双塔区', '7-83-936', '3', 'Shuangta Qu', '辽宁省朝阳市双塔区', 'STQ');
INSERT INTO `t_area` VALUES ('937', '83', '龙城区', '7-83-937', '3', 'Longcheng Qu', '辽宁省朝阳市龙城区', 'LCL');
INSERT INTO `t_area` VALUES ('938', '83', '朝阳县', '7-83-938', '3', 'Chaoyang Xian', '辽宁省朝阳市朝阳县', 'CYG');
INSERT INTO `t_area` VALUES ('939', '83', '建平县', '7-83-939', '3', 'Jianping Xian', '辽宁省朝阳市建平县', 'JPG');
INSERT INTO `t_area` VALUES ('940', '83', '喀喇沁左翼蒙古族自治县', '7-83-940', '3', 'Harqin Zuoyi Mongolzu Zizhixian', '辽宁省朝阳市喀喇沁左翼蒙古族自治县', 'HAZ');
INSERT INTO `t_area` VALUES ('941', '83', '北票市', '7-83-941', '3', 'Beipiao Shi', '辽宁省朝阳市北票市', 'BPO');
INSERT INTO `t_area` VALUES ('942', '83', '凌源市', '7-83-942', '3', 'Lingyuan Shi', '辽宁省朝阳市凌源市', 'LYK');
INSERT INTO `t_area` VALUES ('944', '84', '连山区', '7-84-944', '3', 'Lianshan Qu', '辽宁省葫芦岛市连山区', 'LSQ');
INSERT INTO `t_area` VALUES ('945', '84', '龙港区', '7-84-945', '3', 'Longgang Qu', '辽宁省葫芦岛市龙港区', 'LGD');
INSERT INTO `t_area` VALUES ('946', '84', '南票区', '7-84-946', '3', 'Nanpiao Qu', '辽宁省葫芦岛市南票区', 'NPQ');
INSERT INTO `t_area` VALUES ('947', '84', '绥中县', '7-84-947', '3', 'Suizhong Xian', '辽宁省葫芦岛市绥中县', 'SZL');
INSERT INTO `t_area` VALUES ('948', '84', '建昌县', '7-84-948', '3', 'Jianchang Xian', '辽宁省葫芦岛市建昌县', 'JCL');
INSERT INTO `t_area` VALUES ('949', '84', '兴城市', '7-84-949', '3', 'Xingcheng Shi', '辽宁省葫芦岛市兴城市', 'XCL');
INSERT INTO `t_area` VALUES ('951', '85', '南关区', '8-85-951', '3', 'Nanguan Qu', '吉林省长春市南关区', 'NGN');
INSERT INTO `t_area` VALUES ('952', '85', '宽城区', '8-85-952', '3', 'Kuancheng Qu', '吉林省长春市宽城区', 'KCQ');
INSERT INTO `t_area` VALUES ('953', '85', '朝阳区', '8-85-953', '3', 'Chaoyang Qu ', '吉林省长春市朝阳区', 'CYC');
INSERT INTO `t_area` VALUES ('954', '85', '二道区', '8-85-954', '3', 'Erdao Qu', '吉林省长春市二道区', 'EDQ');
INSERT INTO `t_area` VALUES ('955', '85', '绿园区', '8-85-955', '3', 'Lvyuan Qu', '吉林省长春市绿园区', 'LYQ');
INSERT INTO `t_area` VALUES ('956', '85', '双阳区', '8-85-956', '3', 'Shuangyang Qu', '吉林省长春市双阳区', 'SYQ');
INSERT INTO `t_area` VALUES ('957', '85', '农安县', '8-85-957', '3', 'Nong,an Xian ', '吉林省长春市农安县', 'NAJ');
INSERT INTO `t_area` VALUES ('958', '85', '九台市', '8-85-958', '3', 'Jiutai Shi', '吉林省长春市九台市', '2');
INSERT INTO `t_area` VALUES ('959', '85', '榆树市', '8-85-959', '3', 'Yushu Shi', '吉林省长春市榆树市', 'YSS');
INSERT INTO `t_area` VALUES ('960', '85', '德惠市', '8-85-960', '3', 'Dehui Shi', '吉林省长春市德惠市', 'DEH');
INSERT INTO `t_area` VALUES ('962', '86', '昌邑区', '8-86-962', '3', 'Changyi Qu', '吉林省吉林市昌邑区', 'CYI');
INSERT INTO `t_area` VALUES ('963', '86', '龙潭区', '8-86-963', '3', 'Longtan Qu', '吉林省吉林市龙潭区', 'LTQ');
INSERT INTO `t_area` VALUES ('964', '86', '船营区', '8-86-964', '3', 'Chuanying Qu', '吉林省吉林市船营区', 'CYJ');
INSERT INTO `t_area` VALUES ('965', '86', '丰满区', '8-86-965', '3', 'Fengman Qu', '吉林省吉林市丰满区', 'FMQ');
INSERT INTO `t_area` VALUES ('966', '86', '永吉县', '8-86-966', '3', 'Yongji Xian', '吉林省吉林市永吉县', 'YOJ');
INSERT INTO `t_area` VALUES ('967', '86', '蛟河市', '8-86-967', '3', 'Jiaohe Shi', '吉林省吉林市蛟河市', 'JHJ');
INSERT INTO `t_area` VALUES ('968', '86', '桦甸市', '8-86-968', '3', 'Huadian Shi', '吉林省吉林市桦甸市', 'HDJ');
INSERT INTO `t_area` VALUES ('969', '86', '舒兰市', '8-86-969', '3', 'Shulan Shi', '吉林省吉林市舒兰市', 'SLN');
INSERT INTO `t_area` VALUES ('970', '86', '磐石市', '8-86-970', '3', 'Panshi Shi', '吉林省吉林市磐石市', 'PSI');
INSERT INTO `t_area` VALUES ('972', '87', '铁西区', '8-87-972', '3', 'Tiexi Qu', '吉林省四平市铁西区', 'TXS');
INSERT INTO `t_area` VALUES ('973', '87', '铁东区', '8-87-973', '3', 'Tiedong Qu ', '吉林省四平市铁东区', 'TDQ');
INSERT INTO `t_area` VALUES ('974', '87', '梨树县', '8-87-974', '3', 'Lishu Xian', '吉林省四平市梨树县', 'LSU');
INSERT INTO `t_area` VALUES ('975', '87', '伊通满族自治县', '8-87-975', '3', 'Yitong Manzu Zizhixian', '吉林省四平市伊通满族自治县', 'YTO');
INSERT INTO `t_area` VALUES ('976', '87', '公主岭市', '8-87-976', '3', 'Gongzhuling Shi', '吉林省四平市公主岭市', 'GZL');
INSERT INTO `t_area` VALUES ('977', '87', '双辽市', '8-87-977', '3', 'Shuangliao Shi', '吉林省四平市双辽市', 'SLS');
INSERT INTO `t_area` VALUES ('979', '88', '龙山区', '8-88-979', '3', 'Longshan Qu', '吉林省辽源市龙山区', 'LGS');
INSERT INTO `t_area` VALUES ('980', '88', '西安区', '8-88-980', '3', 'Xi,an Qu', '吉林省辽源市西安区', 'XAA');
INSERT INTO `t_area` VALUES ('981', '88', '东丰县', '8-88-981', '3', 'Dongfeng Xian', '吉林省辽源市东丰县', 'DGF');
INSERT INTO `t_area` VALUES ('982', '88', '东辽县', '8-88-982', '3', 'Dongliao Xian ', '吉林省辽源市东辽县', 'DLX');
INSERT INTO `t_area` VALUES ('984', '89', '东昌区', '8-89-984', '3', 'Dongchang Qu', '吉林省通化市东昌区', 'DCT');
INSERT INTO `t_area` VALUES ('985', '89', '二道江区', '8-89-985', '3', 'Erdaojiang Qu', '吉林省通化市二道江区', 'EDJ');
INSERT INTO `t_area` VALUES ('986', '89', '通化县', '8-89-986', '3', 'Tonghua Xian ', '吉林省通化市通化县', 'THX');
INSERT INTO `t_area` VALUES ('987', '89', '辉南县', '8-89-987', '3', 'Huinan Xian ', '吉林省通化市辉南县', 'HNA');
INSERT INTO `t_area` VALUES ('988', '89', '柳河县', '8-89-988', '3', 'Liuhe Xian ', '吉林省通化市柳河县', 'LHC');
INSERT INTO `t_area` VALUES ('989', '89', '梅河口市', '8-89-989', '3', 'Meihekou Shi', '吉林省通化市梅河口市', 'MHK');
INSERT INTO `t_area` VALUES ('990', '89', '集安市', '8-89-990', '3', 'Ji,an Shi', '吉林省通化市集安市', 'KNC');
INSERT INTO `t_area` VALUES ('992', '90', '八道江区', '8-90-992', '3', 'Badaojiang Qu', '吉林省白山市八道江区', 'BDJ');
INSERT INTO `t_area` VALUES ('993', '90', '抚松县', '8-90-993', '3', 'Fusong Xian', '吉林省白山市抚松县', 'FSG');
INSERT INTO `t_area` VALUES ('994', '90', '靖宇县', '8-90-994', '3', 'Jingyu Xian', '吉林省白山市靖宇县', 'JYJ');
INSERT INTO `t_area` VALUES ('995', '90', '长白朝鲜族自治县', '8-90-995', '3', 'Changbaichaoxianzuzizhi Xian', '吉林省白山市长白朝鲜族自治县', '2');
INSERT INTO `t_area` VALUES ('996', '90', '江源区', '8-90-996', '3', 'Jiangyuan Xian', '吉林省白山市江源区', '2');
INSERT INTO `t_area` VALUES ('997', '90', '临江市', '8-90-997', '3', 'Linjiang Shi', '吉林省白山市临江市', 'LIN');
INSERT INTO `t_area` VALUES ('999', '91', '宁江区', '8-91-999', '3', 'Ningjiang Qu', '吉林省松原市宁江区', 'NJA');
INSERT INTO `t_area` VALUES ('1000', '91', '前郭尔罗斯蒙古族自治县', '8-91-1000', '3', 'Qian Gorlos Mongolzu Zizhixian', '吉林省松原市前郭尔罗斯蒙古族自治县', 'QGO');
INSERT INTO `t_area` VALUES ('1001', '91', '长岭县', '8-91-1001', '3', 'Changling Xian', '吉林省松原市长岭县', 'CLG');
INSERT INTO `t_area` VALUES ('1002', '91', '乾安县', '8-91-1002', '3', 'Qian,an Xian', '吉林省松原市乾安县', 'QAJ');
INSERT INTO `t_area` VALUES ('1003', '91', '扶余县', '8-91-1003', '3', 'Fuyu Xian', '吉林省松原市扶余县', 'FYU');
INSERT INTO `t_area` VALUES ('1005', '92', '洮北区', '8-92-1005', '3', 'Taobei Qu', '吉林省白城市洮北区', 'TBQ');
INSERT INTO `t_area` VALUES ('1006', '92', '镇赉县', '8-92-1006', '3', 'Zhenlai Xian', '吉林省白城市镇赉县', 'ZLA');
INSERT INTO `t_area` VALUES ('1007', '92', '通榆县', '8-92-1007', '3', 'Tongyu Xian', '吉林省白城市通榆县', 'TGY');
INSERT INTO `t_area` VALUES ('1008', '92', '洮南市', '8-92-1008', '3', 'Taonan Shi', '吉林省白城市洮南市', 'TNS');
INSERT INTO `t_area` VALUES ('1009', '92', '大安市', '8-92-1009', '3', 'Da,an Shi', '吉林省白城市大安市', 'DNA');
INSERT INTO `t_area` VALUES ('1010', '93', '延吉市', '8-93-1010', '3', 'Yanji Shi', '吉林省延边朝鲜族自治州延吉市', 'YNJ');
INSERT INTO `t_area` VALUES ('1011', '93', '图们市', '8-93-1011', '3', 'Tumen Shi', '吉林省延边朝鲜族自治州图们市', 'TME');
INSERT INTO `t_area` VALUES ('1012', '93', '敦化市', '8-93-1012', '3', 'Dunhua Shi', '吉林省延边朝鲜族自治州敦化市', 'DHS');
INSERT INTO `t_area` VALUES ('1013', '93', '珲春市', '8-93-1013', '3', 'Hunchun Shi', '吉林省延边朝鲜族自治州珲春市', 'HUC');
INSERT INTO `t_area` VALUES ('1014', '93', '龙井市', '8-93-1014', '3', 'Longjing Shi', '吉林省延边朝鲜族自治州龙井市', 'LJJ');
INSERT INTO `t_area` VALUES ('1015', '93', '和龙市', '8-93-1015', '3', 'Helong Shi', '吉林省延边朝鲜族自治州和龙市', 'HEL');
INSERT INTO `t_area` VALUES ('1016', '93', '汪清县', '8-93-1016', '3', 'Wangqing Xian', '吉林省延边朝鲜族自治州汪清县', 'WGQ');
INSERT INTO `t_area` VALUES ('1017', '93', '安图县', '8-93-1017', '3', 'Antu Xian', '吉林省延边朝鲜族自治州安图县', 'ATU');
INSERT INTO `t_area` VALUES ('1019', '94', '道里区', '9-94-1019', '3', 'Daoli Qu', '黑龙江省哈尔滨市道里区', 'DLH');
INSERT INTO `t_area` VALUES ('1020', '94', '南岗区', '9-94-1020', '3', 'Nangang Qu', '黑龙江省哈尔滨市南岗区', 'NGQ');
INSERT INTO `t_area` VALUES ('1021', '94', '道外区', '9-94-1021', '3', 'Daowai Qu', '黑龙江省哈尔滨市道外区', 'DWQ');
INSERT INTO `t_area` VALUES ('1022', '94', '香坊区', '9-94-1022', '3', 'Xiangfang Qu', '黑龙江省哈尔滨市香坊区', '2');
INSERT INTO `t_area` VALUES ('1024', '94', '平房区', '9-94-1024', '3', 'Pingfang Qu', '黑龙江省哈尔滨市平房区', 'PFQ');
INSERT INTO `t_area` VALUES ('1025', '94', '松北区', '9-94-1025', '3', 'Songbei Qu', '黑龙江省哈尔滨市松北区', '2');
INSERT INTO `t_area` VALUES ('1026', '94', '呼兰区', '9-94-1026', '3', 'Hulan Qu', '黑龙江省哈尔滨市呼兰区', '2');
INSERT INTO `t_area` VALUES ('1027', '94', '依兰县', '9-94-1027', '3', 'Yilan Xian', '黑龙江省哈尔滨市依兰县', 'YLH');
INSERT INTO `t_area` VALUES ('1028', '94', '方正县', '9-94-1028', '3', 'Fangzheng Xian', '黑龙江省哈尔滨市方正县', 'FZH');
INSERT INTO `t_area` VALUES ('1029', '94', '宾县', '9-94-1029', '3', 'Bin Xian', '黑龙江省哈尔滨市宾县', 'BNX');
INSERT INTO `t_area` VALUES ('1030', '94', '巴彦县', '9-94-1030', '3', 'Bayan Xian', '黑龙江省哈尔滨市巴彦县', 'BYH');
INSERT INTO `t_area` VALUES ('1031', '94', '木兰县', '9-94-1031', '3', 'Mulan Xian ', '黑龙江省哈尔滨市木兰县', 'MUL');
INSERT INTO `t_area` VALUES ('1032', '94', '通河县', '9-94-1032', '3', 'Tonghe Xian', '黑龙江省哈尔滨市通河县', 'TOH');
INSERT INTO `t_area` VALUES ('1033', '94', '延寿县', '9-94-1033', '3', 'Yanshou Xian', '黑龙江省哈尔滨市延寿县', 'YSU');
INSERT INTO `t_area` VALUES ('1034', '94', '阿城区', '9-94-1034', '3', 'Acheng Shi', '黑龙江省哈尔滨市阿城区', '2');
INSERT INTO `t_area` VALUES ('1035', '94', '双城市', '9-94-1035', '3', 'Shuangcheng Shi', '黑龙江省哈尔滨市双城市', 'SCS');
INSERT INTO `t_area` VALUES ('1036', '94', '尚志市', '9-94-1036', '3', 'Shangzhi Shi', '黑龙江省哈尔滨市尚志市', 'SZI');
INSERT INTO `t_area` VALUES ('1037', '94', '五常市', '9-94-1037', '3', 'Wuchang Shi', '黑龙江省哈尔滨市五常市', 'WCA');
INSERT INTO `t_area` VALUES ('1039', '95', '龙沙区', '9-95-1039', '3', 'Longsha Qu', '黑龙江省齐齐哈尔市龙沙区', 'LQQ');
INSERT INTO `t_area` VALUES ('1040', '95', '建华区', '9-95-1040', '3', 'Jianhua Qu', '黑龙江省齐齐哈尔市建华区', 'JHQ');
INSERT INTO `t_area` VALUES ('1041', '95', '铁锋区', '9-95-1041', '3', 'Tiefeng Qu', '黑龙江省齐齐哈尔市铁锋区', '2');
INSERT INTO `t_area` VALUES ('1042', '95', '昂昂溪区', '9-95-1042', '3', 'Ang,angxi Qu', '黑龙江省齐齐哈尔市昂昂溪区', 'AAX');
INSERT INTO `t_area` VALUES ('1043', '95', '富拉尔基区', '9-95-1043', '3', 'Hulan Ergi Qu', '黑龙江省齐齐哈尔市富拉尔基区', 'HUE');
INSERT INTO `t_area` VALUES ('1044', '95', '碾子山区', '9-95-1044', '3', 'Nianzishan Qu', '黑龙江省齐齐哈尔市碾子山区', 'NZS');
INSERT INTO `t_area` VALUES ('1045', '95', '梅里斯达斡尔族区', '9-95-1045', '3', 'Meilisidawoerzu Qu', '黑龙江省齐齐哈尔市梅里斯达斡尔族区', '2');
INSERT INTO `t_area` VALUES ('1046', '95', '龙江县', '9-95-1046', '3', 'Longjiang Xian', '黑龙江省齐齐哈尔市龙江县', 'LGJ');
INSERT INTO `t_area` VALUES ('1047', '95', '依安县', '9-95-1047', '3', 'Yi,an Xian', '黑龙江省齐齐哈尔市依安县', 'YAN');
INSERT INTO `t_area` VALUES ('1048', '95', '泰来县', '9-95-1048', '3', 'Tailai Xian', '黑龙江省齐齐哈尔市泰来县', 'TLA');
INSERT INTO `t_area` VALUES ('1049', '95', '甘南县', '9-95-1049', '3', 'Gannan Xian', '黑龙江省齐齐哈尔市甘南县', 'GNX');
INSERT INTO `t_area` VALUES ('1050', '95', '富裕县', '9-95-1050', '3', 'Fuyu Xian', '黑龙江省齐齐哈尔市富裕县', 'FYX');
INSERT INTO `t_area` VALUES ('1051', '95', '克山县', '9-95-1051', '3', 'Keshan Xian', '黑龙江省齐齐哈尔市克山县', 'KSN');
INSERT INTO `t_area` VALUES ('1052', '95', '克东县', '9-95-1052', '3', 'Kedong Xian', '黑龙江省齐齐哈尔市克东县', 'KDO');
INSERT INTO `t_area` VALUES ('1053', '95', '拜泉县', '9-95-1053', '3', 'Baiquan Xian', '黑龙江省齐齐哈尔市拜泉县', 'BQN');
INSERT INTO `t_area` VALUES ('1054', '95', '讷河市', '9-95-1054', '3', 'Nehe Shi', '黑龙江省齐齐哈尔市讷河市', 'NEH');
INSERT INTO `t_area` VALUES ('1056', '96', '鸡冠区', '9-96-1056', '3', 'Jiguan Qu', '黑龙江省鸡西市鸡冠区', 'JGU');
INSERT INTO `t_area` VALUES ('1057', '96', '恒山区', '9-96-1057', '3', 'Hengshan Qu', '黑龙江省鸡西市恒山区', 'HSD');
INSERT INTO `t_area` VALUES ('1058', '96', '滴道区', '9-96-1058', '3', 'Didao Qu', '黑龙江省鸡西市滴道区', 'DDO');
INSERT INTO `t_area` VALUES ('1059', '96', '梨树区', '9-96-1059', '3', 'Lishu Qu', '黑龙江省鸡西市梨树区', 'LJX');
INSERT INTO `t_area` VALUES ('1060', '96', '城子河区', '9-96-1060', '3', 'Chengzihe Qu', '黑龙江省鸡西市城子河区', 'CZH');
INSERT INTO `t_area` VALUES ('1061', '96', '麻山区', '9-96-1061', '3', 'Mashan Qu', '黑龙江省鸡西市麻山区', 'MSN');
INSERT INTO `t_area` VALUES ('1062', '96', '鸡东县', '9-96-1062', '3', 'Jidong Xian', '黑龙江省鸡西市鸡东县', 'JID');
INSERT INTO `t_area` VALUES ('1063', '96', '虎林市', '9-96-1063', '3', 'Hulin Shi', '黑龙江省鸡西市虎林市', 'HUL');
INSERT INTO `t_area` VALUES ('1064', '96', '密山市', '9-96-1064', '3', 'Mishan Shi', '黑龙江省鸡西市密山市', 'MIS');
INSERT INTO `t_area` VALUES ('1066', '97', '向阳区', '9-97-1066', '3', 'Xiangyang  Qu ', '黑龙江省鹤岗市向阳区', 'XYZ');
INSERT INTO `t_area` VALUES ('1067', '97', '工农区', '9-97-1067', '3', 'Gongnong Qu', '黑龙江省鹤岗市工农区', 'GNH');
INSERT INTO `t_area` VALUES ('1068', '97', '南山区', '9-97-1068', '3', 'Nanshan Qu', '黑龙江省鹤岗市南山区', 'NSQ');
INSERT INTO `t_area` VALUES ('1069', '97', '兴安区', '9-97-1069', '3', 'Xing,an Qu', '黑龙江省鹤岗市兴安区', 'XAH');
INSERT INTO `t_area` VALUES ('1070', '97', '东山区', '9-97-1070', '3', 'Dongshan Qu', '黑龙江省鹤岗市东山区', 'DSA');
INSERT INTO `t_area` VALUES ('1071', '97', '兴山区', '9-97-1071', '3', 'Xingshan Qu', '黑龙江省鹤岗市兴山区', 'XSQ');
INSERT INTO `t_area` VALUES ('1072', '97', '萝北县', '9-97-1072', '3', 'Luobei Xian', '黑龙江省鹤岗市萝北县', 'LUB');
INSERT INTO `t_area` VALUES ('1073', '97', '绥滨县', '9-97-1073', '3', 'Suibin Xian', '黑龙江省鹤岗市绥滨县', '2');
INSERT INTO `t_area` VALUES ('1075', '98', '尖山区', '9-98-1075', '3', 'Jianshan Qu', '黑龙江省双鸭山市尖山区', 'JSQ');
INSERT INTO `t_area` VALUES ('1076', '98', '岭东区', '9-98-1076', '3', 'Lingdong Qu', '黑龙江省双鸭山市岭东区', 'LDQ');
INSERT INTO `t_area` VALUES ('1077', '98', '四方台区', '9-98-1077', '3', 'Sifangtai Qu', '黑龙江省双鸭山市四方台区', 'SFT');
INSERT INTO `t_area` VALUES ('1078', '98', '宝山区', '9-98-1078', '3', 'Baoshan Qu', '黑龙江省双鸭山市宝山区', 'BSQ');
INSERT INTO `t_area` VALUES ('1079', '98', '集贤县', '9-98-1079', '3', 'Jixian Xian', '黑龙江省双鸭山市集贤县', 'JXH');
INSERT INTO `t_area` VALUES ('1080', '98', '友谊县', '9-98-1080', '3', 'Youyi Xian', '黑龙江省双鸭山市友谊县', 'YYI');
INSERT INTO `t_area` VALUES ('1081', '98', '宝清县', '9-98-1081', '3', 'Baoqing Xian', '黑龙江省双鸭山市宝清县', 'BQG');
INSERT INTO `t_area` VALUES ('1082', '98', '饶河县', '9-98-1082', '3', 'Raohe Xian ', '黑龙江省双鸭山市饶河县', 'ROH');
INSERT INTO `t_area` VALUES ('1084', '99', '萨尔图区', '9-99-1084', '3', 'Sairt Qu', '黑龙江省大庆市萨尔图区', 'SAI');
INSERT INTO `t_area` VALUES ('1085', '99', '龙凤区', '9-99-1085', '3', 'Longfeng Qu', '黑龙江省大庆市龙凤区', 'LFQ');
INSERT INTO `t_area` VALUES ('1086', '99', '让胡路区', '9-99-1086', '3', 'Ranghulu Qu', '黑龙江省大庆市让胡路区', 'RHL');
INSERT INTO `t_area` VALUES ('1087', '99', '红岗区', '9-99-1087', '3', 'Honggang Qu', '黑龙江省大庆市红岗区', 'HGD');
INSERT INTO `t_area` VALUES ('1088', '99', '大同区', '9-99-1088', '3', 'Datong Qu', '黑龙江省大庆市大同区', 'DHD');
INSERT INTO `t_area` VALUES ('1089', '99', '肇州县', '9-99-1089', '3', 'Zhaozhou Xian', '黑龙江省大庆市肇州县', 'ZAZ');
INSERT INTO `t_area` VALUES ('1090', '99', '肇源县', '9-99-1090', '3', 'Zhaoyuan Xian', '黑龙江省大庆市肇源县', 'ZYH');
INSERT INTO `t_area` VALUES ('1091', '99', '林甸县', '9-99-1091', '3', 'Lindian Xian ', '黑龙江省大庆市林甸县', 'LDN');
INSERT INTO `t_area` VALUES ('1092', '99', '杜尔伯特蒙古族自治县', '9-99-1092', '3', 'Dorbod Mongolzu Zizhixian', '黑龙江省大庆市杜尔伯特蒙古族自治县', 'DOM');
INSERT INTO `t_area` VALUES ('1094', '100', '伊春区', '9-100-1094', '3', 'Yichun Qu', '黑龙江省伊春市伊春区', 'YYC');
INSERT INTO `t_area` VALUES ('1095', '100', '南岔区', '9-100-1095', '3', 'Nancha Qu', '黑龙江省伊春市南岔区', 'NCQ');
INSERT INTO `t_area` VALUES ('1096', '100', '友好区', '9-100-1096', '3', 'Youhao Qu', '黑龙江省伊春市友好区', 'YOH');
INSERT INTO `t_area` VALUES ('1097', '100', '西林区', '9-100-1097', '3', 'Xilin Qu', '黑龙江省伊春市西林区', 'XIL');
INSERT INTO `t_area` VALUES ('1098', '100', '翠峦区', '9-100-1098', '3', 'Cuiluan Qu', '黑龙江省伊春市翠峦区', 'CLN');
INSERT INTO `t_area` VALUES ('1099', '100', '新青区', '9-100-1099', '3', 'Xinqing Qu', '黑龙江省伊春市新青区', 'XQQ');
INSERT INTO `t_area` VALUES ('1100', '100', '美溪区', '9-100-1100', '3', 'Meixi Qu', '黑龙江省伊春市美溪区', 'MXQ');
INSERT INTO `t_area` VALUES ('1101', '100', '金山屯区', '9-100-1101', '3', 'Jinshantun Qu', '黑龙江省伊春市金山屯区', 'JST');
INSERT INTO `t_area` VALUES ('1102', '100', '五营区', '9-100-1102', '3', 'Wuying Qu', '黑龙江省伊春市五营区', 'WYQ');
INSERT INTO `t_area` VALUES ('1103', '100', '乌马河区', '9-100-1103', '3', 'Wumahe Qu', '黑龙江省伊春市乌马河区', 'WMH');
INSERT INTO `t_area` VALUES ('1104', '100', '汤旺河区', '9-100-1104', '3', 'Tangwanghe Qu', '黑龙江省伊春市汤旺河区', 'TWH');
INSERT INTO `t_area` VALUES ('1105', '100', '带岭区', '9-100-1105', '3', 'Dailing Qu', '黑龙江省伊春市带岭区', 'DLY');
INSERT INTO `t_area` VALUES ('1106', '100', '乌伊岭区', '9-100-1106', '3', 'Wuyiling Qu', '黑龙江省伊春市乌伊岭区', 'WYL');
INSERT INTO `t_area` VALUES ('1107', '100', '红星区', '9-100-1107', '3', 'Hongxing Qu', '黑龙江省伊春市红星区', 'HGX');
INSERT INTO `t_area` VALUES ('1108', '100', '上甘岭区', '9-100-1108', '3', 'Shangganling Qu', '黑龙江省伊春市上甘岭区', 'SGL');
INSERT INTO `t_area` VALUES ('1109', '100', '嘉荫县', '9-100-1109', '3', 'Jiayin Xian', '黑龙江省伊春市嘉荫县', '2');
INSERT INTO `t_area` VALUES ('1110', '100', '铁力市', '9-100-1110', '3', 'Tieli Shi', '黑龙江省伊春市铁力市', 'TEL');
INSERT INTO `t_area` VALUES ('1113', '101', '向阳区', '9-101-1113', '3', 'Xiangyang  Qu ', '黑龙江省佳木斯市向阳区', 'XYQ');
INSERT INTO `t_area` VALUES ('1114', '101', '前进区', '9-101-1114', '3', 'Qianjin Qu', '黑龙江省佳木斯市前进区', 'QJQ');
INSERT INTO `t_area` VALUES ('1115', '101', '东风区', '9-101-1115', '3', 'Dongfeng Qu', '黑龙江省佳木斯市东风区', 'DFQ');
INSERT INTO `t_area` VALUES ('1117', '101', '桦南县', '9-101-1117', '3', 'Huanan Xian', '黑龙江省佳木斯市桦南县', 'HNH');
INSERT INTO `t_area` VALUES ('1118', '101', '桦川县', '9-101-1118', '3', 'Huachuan Xian', '黑龙江省佳木斯市桦川县', 'HCN');
INSERT INTO `t_area` VALUES ('1119', '101', '汤原县', '9-101-1119', '3', 'Tangyuan Xian', '黑龙江省佳木斯市汤原县', 'TYX');
INSERT INTO `t_area` VALUES ('1120', '101', '抚远县', '9-101-1120', '3', 'Fuyuan Xian', '黑龙江省佳木斯市抚远县', 'FUY');
INSERT INTO `t_area` VALUES ('1121', '101', '同江市', '9-101-1121', '3', 'Tongjiang Shi', '黑龙江省佳木斯市同江市', 'TOJ');
INSERT INTO `t_area` VALUES ('1122', '101', '富锦市', '9-101-1122', '3', 'Fujin Shi', '黑龙江省佳木斯市富锦市', 'FUJ');
INSERT INTO `t_area` VALUES ('1124', '102', '新兴区', '9-102-1124', '3', 'Xinxing Qu', '黑龙江省七台河市新兴区', 'XXQ');
INSERT INTO `t_area` VALUES ('1125', '102', '桃山区', '9-102-1125', '3', 'Taoshan Qu', '黑龙江省七台河市桃山区', 'TSC');
INSERT INTO `t_area` VALUES ('1126', '102', '茄子河区', '9-102-1126', '3', 'Qiezihe Qu', '黑龙江省七台河市茄子河区', 'QZI');
INSERT INTO `t_area` VALUES ('1127', '102', '勃利县', '9-102-1127', '3', 'Boli Xian', '黑龙江省七台河市勃利县', 'BLI');
INSERT INTO `t_area` VALUES ('1129', '103', '东安区', '9-103-1129', '3', 'Dong,an Qu', '黑龙江省牡丹江市东安区', 'DGA');
INSERT INTO `t_area` VALUES ('1130', '103', '阳明区', '9-103-1130', '3', 'Yangming Qu', '黑龙江省牡丹江市阳明区', 'YMQ');
INSERT INTO `t_area` VALUES ('1131', '103', '爱民区', '9-103-1131', '3', 'Aimin Qu', '黑龙江省牡丹江市爱民区', 'AMQ');
INSERT INTO `t_area` VALUES ('1132', '103', '西安区', '9-103-1132', '3', 'Xi,an Qu', '黑龙江省牡丹江市西安区', 'XAQ');
INSERT INTO `t_area` VALUES ('1133', '103', '东宁县', '9-103-1133', '3', 'Dongning Xian', '黑龙江省牡丹江市东宁县', 'DON');
INSERT INTO `t_area` VALUES ('1134', '103', '林口县', '9-103-1134', '3', 'Linkou Xian', '黑龙江省牡丹江市林口县', 'LKO');
INSERT INTO `t_area` VALUES ('1135', '103', '绥芬河市', '9-103-1135', '3', 'Suifenhe Shi', '黑龙江省牡丹江市绥芬河市', 'SFE');
INSERT INTO `t_area` VALUES ('1136', '103', '海林市', '9-103-1136', '3', 'Hailin Shi', '黑龙江省牡丹江市海林市', 'HLS');
INSERT INTO `t_area` VALUES ('1137', '103', '宁安市', '9-103-1137', '3', 'Ning,an Shi', '黑龙江省牡丹江市宁安市', 'NAI');
INSERT INTO `t_area` VALUES ('1138', '103', '穆棱市', '9-103-1138', '3', 'Muling Shi', '黑龙江省牡丹江市穆棱市', 'MLG');
INSERT INTO `t_area` VALUES ('1140', '104', '爱辉区', '9-104-1140', '3', 'Aihui Qu', '黑龙江省黑河市爱辉区', 'AHQ');
INSERT INTO `t_area` VALUES ('1141', '104', '嫩江县', '9-104-1141', '3', 'Nenjiang Xian', '黑龙江省黑河市嫩江县', 'NJH');
INSERT INTO `t_area` VALUES ('1142', '104', '逊克县', '9-104-1142', '3', 'Xunke Xian', '黑龙江省黑河市逊克县', 'XUK');
INSERT INTO `t_area` VALUES ('1143', '104', '孙吴县', '9-104-1143', '3', 'Sunwu Xian', '黑龙江省黑河市孙吴县', 'SUW');
INSERT INTO `t_area` VALUES ('1144', '104', '北安市', '9-104-1144', '3', 'Bei,an Shi', '黑龙江省黑河市北安市', 'BAS');
INSERT INTO `t_area` VALUES ('1145', '104', '五大连池市', '9-104-1145', '3', 'Wudalianchi Shi', '黑龙江省黑河市五大连池市', 'WDL');
INSERT INTO `t_area` VALUES ('1147', '105', '北林区', '9-105-1147', '3', 'Beilin Qu', '黑龙江省绥化市北林区', '2');
INSERT INTO `t_area` VALUES ('1148', '105', '望奎县', '9-105-1148', '3', 'Wangkui Xian', '黑龙江省绥化市望奎县', '2');
INSERT INTO `t_area` VALUES ('1149', '105', '兰西县', '9-105-1149', '3', 'Lanxi Xian', '黑龙江省绥化市兰西县', '2');
INSERT INTO `t_area` VALUES ('1150', '105', '青冈县', '9-105-1150', '3', 'Qinggang Xian', '黑龙江省绥化市青冈县', '2');
INSERT INTO `t_area` VALUES ('1151', '105', '庆安县', '9-105-1151', '3', 'Qing,an Xian', '黑龙江省绥化市庆安县', '2');
INSERT INTO `t_area` VALUES ('1152', '105', '明水县', '9-105-1152', '3', 'Mingshui Xian', '黑龙江省绥化市明水县', '2');
INSERT INTO `t_area` VALUES ('1153', '105', '绥棱县', '9-105-1153', '3', 'Suileng Xian', '黑龙江省绥化市绥棱县', '2');
INSERT INTO `t_area` VALUES ('1154', '105', '安达市', '9-105-1154', '3', 'Anda Shi', '黑龙江省绥化市安达市', '2');
INSERT INTO `t_area` VALUES ('1155', '105', '肇东市', '9-105-1155', '3', 'Zhaodong Shi', '黑龙江省绥化市肇东市', '2');
INSERT INTO `t_area` VALUES ('1156', '105', '海伦市', '9-105-1156', '3', 'Hailun Shi', '黑龙江省绥化市海伦市', '2');
INSERT INTO `t_area` VALUES ('1157', '106', '呼玛县', '9-106-1157', '3', 'Huma Xian', '黑龙江省大兴安岭地区呼玛县', 'HUM');
INSERT INTO `t_area` VALUES ('1158', '106', '塔河县', '9-106-1158', '3', 'Tahe Xian', '黑龙江省大兴安岭地区塔河县', 'TAH');
INSERT INTO `t_area` VALUES ('1159', '106', '漠河县', '9-106-1159', '3', 'Mohe Xian', '黑龙江省大兴安岭地区漠河县', 'MOH');
INSERT INTO `t_area` VALUES ('1160', '5003', '黄浦区', '10-5003-1160', '3', 'Huangpu Qu', '上海市上海市黄浦区', 'HGP');
INSERT INTO `t_area` VALUES ('1161', '5003', '卢湾区', '10-5003-1161', '3', 'Luwan Qu', '上海市上海市卢湾区', 'LWN');
INSERT INTO `t_area` VALUES ('1162', '5003', '徐汇区', '10-5003-1162', '3', 'Xuhui Qu', '上海市上海市徐汇区', 'XHI');
INSERT INTO `t_area` VALUES ('1163', '5003', '长宁区', '10-5003-1163', '3', 'Changning Qu', '上海市上海市长宁区', 'CNQ');
INSERT INTO `t_area` VALUES ('1164', '5003', '静安区', '10-5003-1164', '3', 'Jing,an Qu', '上海市上海市静安区', 'JAQ');
INSERT INTO `t_area` VALUES ('1165', '5003', '普陀区', '10-5003-1165', '3', 'Putuo Qu', '上海市上海市普陀区', 'PTQ');
INSERT INTO `t_area` VALUES ('1166', '5003', '闸北区', '10-5003-1166', '3', 'Zhabei Qu', '上海市上海市闸北区', 'ZBE');
INSERT INTO `t_area` VALUES ('1167', '5003', '虹口区', '10-5003-1167', '3', 'Hongkou Qu', '上海市上海市虹口区', 'HKQ');
INSERT INTO `t_area` VALUES ('1168', '5003', '杨浦区', '10-5003-1168', '3', 'Yangpu Qu', '上海市上海市杨浦区', 'YPU');
INSERT INTO `t_area` VALUES ('1169', '5003', '闵行区', '10-5003-1169', '3', 'Minhang Qu', '上海市上海市闵行区', 'MHQ');
INSERT INTO `t_area` VALUES ('1170', '5003', '宝山区', '10-5003-1170', '3', 'Baoshan Qu', '上海市上海市宝山区', 'BAO');
INSERT INTO `t_area` VALUES ('1171', '5003', '嘉定区', '10-5003-1171', '3', 'Jiading Qu', '上海市上海市嘉定区', 'JDG');
INSERT INTO `t_area` VALUES ('1172', '5003', '浦东新区', '10-5003-1172', '3', 'Pudong Xinqu', '上海市上海市浦东新区', 'PDX');
INSERT INTO `t_area` VALUES ('1173', '5003', '金山区', '10-5003-1173', '3', 'Jinshan Qu', '上海市上海市金山区', 'JSH');
INSERT INTO `t_area` VALUES ('1174', '5003', '松江区', '10-5003-1174', '3', 'Songjiang Qu', '上海市上海市松江区', 'SOJ');
INSERT INTO `t_area` VALUES ('1175', '5003', '青浦区', '10-5003-1175', '3', 'Qingpu  Qu', '上海市上海市青浦区', 'QPU');
INSERT INTO `t_area` VALUES ('1177', '5003', '奉贤区', '10-5003-1177', '3', 'Fengxian Qu', '上海市上海市奉贤区', 'FXI');
INSERT INTO `t_area` VALUES ('1178', '5003', '崇明县', '10-5003-1178', '3', 'Chongming Xian', '上海市上海市崇明县', 'CMI');
INSERT INTO `t_area` VALUES ('1180', '109', '玄武区', '11-109-1180', '3', 'Xuanwu Qu', '江苏省南京市玄武区', 'XWU');
INSERT INTO `t_area` VALUES ('1181', '109', '白下区', '11-109-1181', '3', 'Baixia Qu', '江苏省南京市白下区', 'BXQ');
INSERT INTO `t_area` VALUES ('1182', '109', '秦淮区', '11-109-1182', '3', 'Qinhuai Qu', '江苏省南京市秦淮区', 'QHU');
INSERT INTO `t_area` VALUES ('1183', '109', '建邺区', '11-109-1183', '3', 'Jianye Qu', '江苏省南京市建邺区', 'JYQ');
INSERT INTO `t_area` VALUES ('1184', '109', '鼓楼区', '11-109-1184', '3', 'Gulou Qu', '江苏省南京市鼓楼区', 'GLQ');
INSERT INTO `t_area` VALUES ('1185', '109', '下关区', '11-109-1185', '3', 'Xiaguan Qu', '江苏省南京市下关区', 'XGQ');
INSERT INTO `t_area` VALUES ('1186', '109', '浦口区', '11-109-1186', '3', 'Pukou Qu', '江苏省南京市浦口区', 'PKO');
INSERT INTO `t_area` VALUES ('1187', '109', '栖霞区', '11-109-1187', '3', 'Qixia Qu', '江苏省南京市栖霞区', 'QAX');
INSERT INTO `t_area` VALUES ('1188', '109', '雨花台区', '11-109-1188', '3', 'Yuhuatai Qu', '江苏省南京市雨花台区', 'YHT');
INSERT INTO `t_area` VALUES ('1189', '109', '江宁区', '11-109-1189', '3', 'Jiangning Qu', '江苏省南京市江宁区', '2');
INSERT INTO `t_area` VALUES ('1190', '109', '六合区', '11-109-1190', '3', 'Liuhe Qu', '江苏省南京市六合区', '2');
INSERT INTO `t_area` VALUES ('1191', '109', '溧水县', '11-109-1191', '3', 'Lishui Xian', '江苏省南京市溧水县', 'LIS');
INSERT INTO `t_area` VALUES ('1192', '109', '高淳县', '11-109-1192', '3', 'Gaochun Xian', '江苏省南京市高淳县', 'GCN');
INSERT INTO `t_area` VALUES ('1194', '110', '崇安区', '11-110-1194', '3', 'Chong,an Qu', '江苏省无锡市崇安区', 'CGA');
INSERT INTO `t_area` VALUES ('1195', '110', '南长区', '11-110-1195', '3', 'Nanchang Qu', '江苏省无锡市南长区', 'NCG');
INSERT INTO `t_area` VALUES ('1196', '110', '北塘区', '11-110-1196', '3', 'Beitang Qu', '江苏省无锡市北塘区', 'BTQ');
INSERT INTO `t_area` VALUES ('1197', '110', '锡山区', '11-110-1197', '3', 'Xishan Qu', '江苏省无锡市锡山区', '2');
INSERT INTO `t_area` VALUES ('1198', '110', '惠山区', '11-110-1198', '3', 'Huishan Qu', '江苏省无锡市惠山区', '2');
INSERT INTO `t_area` VALUES ('1199', '110', '滨湖区', '11-110-1199', '3', 'Binhu Qu', '江苏省无锡市滨湖区', '2');
INSERT INTO `t_area` VALUES ('1200', '110', '江阴市', '11-110-1200', '3', 'Jiangyin Shi', '江苏省无锡市江阴市', 'JIA');
INSERT INTO `t_area` VALUES ('1201', '110', '宜兴市', '11-110-1201', '3', 'Yixing Shi', '江苏省无锡市宜兴市', 'YIX');
INSERT INTO `t_area` VALUES ('1203', '111', '鼓楼区', '11-111-1203', '3', 'Gulou Qu', '江苏省徐州市鼓楼区', 'GLU');
INSERT INTO `t_area` VALUES ('1204', '111', '云龙区', '11-111-1204', '3', 'Yunlong Qu', '江苏省徐州市云龙区', 'YLF');
INSERT INTO `t_area` VALUES ('1206', '111', '贾汪区', '11-111-1206', '3', 'Jiawang Qu', '江苏省徐州市贾汪区', 'JWQ');
INSERT INTO `t_area` VALUES ('1207', '111', '泉山区', '11-111-1207', '3', 'Quanshan Qu', '江苏省徐州市泉山区', 'QSX');
INSERT INTO `t_area` VALUES ('1208', '111', '丰县', '11-111-1208', '3', 'Feng Xian', '江苏省徐州市丰县', 'FXN');
INSERT INTO `t_area` VALUES ('1209', '111', '沛县', '11-111-1209', '3', 'Pei Xian', '江苏省徐州市沛县', 'PEI');
INSERT INTO `t_area` VALUES ('1210', '111', '铜山区', '11-111-1210', '3', 'Tongshan Xian', '江苏省徐州市铜山区', '2');
INSERT INTO `t_area` VALUES ('1211', '111', '睢宁县', '11-111-1211', '3', 'Suining Xian', '江苏省徐州市睢宁县', 'SNI');
INSERT INTO `t_area` VALUES ('1212', '111', '新沂市', '11-111-1212', '3', 'Xinyi Shi', '江苏省徐州市新沂市', 'XYW');
INSERT INTO `t_area` VALUES ('1213', '111', '邳州市', '11-111-1213', '3', 'Pizhou Shi', '江苏省徐州市邳州市', 'PZO');
INSERT INTO `t_area` VALUES ('1215', '112', '天宁区', '11-112-1215', '3', 'Tianning Qu', '江苏省常州市天宁区', 'TNQ');
INSERT INTO `t_area` VALUES ('1216', '112', '钟楼区', '11-112-1216', '3', 'Zhonglou Qu', '江苏省常州市钟楼区', 'ZLQ');
INSERT INTO `t_area` VALUES ('1217', '112', '戚墅堰区', '11-112-1217', '3', 'Qishuyan Qu', '江苏省常州市戚墅堰区', 'QSY');
INSERT INTO `t_area` VALUES ('1218', '112', '新北区', '11-112-1218', '3', 'Xinbei Qu', '江苏省常州市新北区', '2');
INSERT INTO `t_area` VALUES ('1219', '112', '武进区', '11-112-1219', '3', 'Wujin Qu', '江苏省常州市武进区', '2');
INSERT INTO `t_area` VALUES ('1220', '112', '溧阳市', '11-112-1220', '3', 'Liyang Shi', '江苏省常州市溧阳市', 'LYR');
INSERT INTO `t_area` VALUES ('1221', '112', '金坛市', '11-112-1221', '3', 'Jintan Shi', '江苏省常州市金坛市', 'JTS');
INSERT INTO `t_area` VALUES ('1223', '113', '沧浪区', '11-113-1223', '3', 'Canglang Qu', '江苏省苏州市沧浪区', 'CLQ');
INSERT INTO `t_area` VALUES ('1224', '113', '平江区', '11-113-1224', '3', 'Pingjiang Qu', '江苏省苏州市平江区', 'PJQ');
INSERT INTO `t_area` VALUES ('1225', '113', '金阊区', '11-113-1225', '3', 'Jinchang Qu', '江苏省苏州市金阊区', 'JCA');
INSERT INTO `t_area` VALUES ('1226', '113', '虎丘区', '11-113-1226', '3', 'Huqiu Qu', '江苏省苏州市虎丘区', '2');
INSERT INTO `t_area` VALUES ('1227', '113', '吴中区', '11-113-1227', '3', 'Wuzhong Qu', '江苏省苏州市吴中区', '2');
INSERT INTO `t_area` VALUES ('1228', '113', '相城区', '11-113-1228', '3', 'Xiangcheng Qu', '江苏省苏州市相城区', '2');
INSERT INTO `t_area` VALUES ('1229', '113', '常熟市', '11-113-1229', '3', 'Changshu Shi', '江苏省苏州市常熟市', 'CGS');
INSERT INTO `t_area` VALUES ('1230', '113', '张家港市', '11-113-1230', '3', 'Zhangjiagang Shi ', '江苏省苏州市张家港市', 'ZJG');
INSERT INTO `t_area` VALUES ('1231', '113', '昆山市', '11-113-1231', '3', 'Kunshan Shi', '江苏省苏州市昆山市', 'KUS');
INSERT INTO `t_area` VALUES ('1232', '113', '吴江市', '11-113-1232', '3', 'Wujiang Shi', '江苏省苏州市吴江市', 'WUJ');
INSERT INTO `t_area` VALUES ('1233', '113', '太仓市', '11-113-1233', '3', 'Taicang Shi', '江苏省苏州市太仓市', 'TAC');
INSERT INTO `t_area` VALUES ('1235', '114', '崇川区', '11-114-1235', '3', 'Chongchuan Qu', '江苏省南通市崇川区', 'CCQ');
INSERT INTO `t_area` VALUES ('1236', '114', '港闸区', '11-114-1236', '3', 'Gangzha Qu', '江苏省南通市港闸区', 'GZQ');
INSERT INTO `t_area` VALUES ('1237', '114', '海安县', '11-114-1237', '3', 'Hai,an Xian', '江苏省南通市海安县', 'HIA');
INSERT INTO `t_area` VALUES ('1238', '114', '如东县', '11-114-1238', '3', 'Rudong Xian', '江苏省南通市如东县', 'RDG');
INSERT INTO `t_area` VALUES ('1239', '114', '启东市', '11-114-1239', '3', 'Qidong Shi', '江苏省南通市启东市', 'QID');
INSERT INTO `t_area` VALUES ('1240', '114', '如皋市', '11-114-1240', '3', 'Rugao Shi', '江苏省南通市如皋市', 'RGO');
INSERT INTO `t_area` VALUES ('1241', '114', '通州区', '11-114-1241', '3', 'Tongzhou Shi', '江苏省南通市通州区', '2');
INSERT INTO `t_area` VALUES ('1242', '114', '海门市', '11-114-1242', '3', 'Haimen Shi', '江苏省南通市海门市', 'HME');
INSERT INTO `t_area` VALUES ('1244', '115', '连云区', '11-115-1244', '3', 'Lianyun Qu', '江苏省连云港市连云区', 'LYB');
INSERT INTO `t_area` VALUES ('1245', '115', '新浦区', '11-115-1245', '3', 'Xinpu Qu', '江苏省连云港市新浦区', 'XPQ');
INSERT INTO `t_area` VALUES ('1246', '115', '海州区', '11-115-1246', '3', 'Haizhou Qu', '江苏省连云港市海州区', 'HIZ');
INSERT INTO `t_area` VALUES ('1247', '115', '赣榆县', '11-115-1247', '3', 'Ganyu Xian', '江苏省连云港市赣榆县', 'GYU');
INSERT INTO `t_area` VALUES ('1248', '115', '东海县', '11-115-1248', '3', 'Donghai Xian', '江苏省连云港市东海县', 'DHX');
INSERT INTO `t_area` VALUES ('1249', '115', '灌云县', '11-115-1249', '3', 'Guanyun Xian', '江苏省连云港市灌云县', 'GYS');
INSERT INTO `t_area` VALUES ('1250', '115', '灌南县', '11-115-1250', '3', 'Guannan Xian', '江苏省连云港市灌南县', 'GUN');
INSERT INTO `t_area` VALUES ('1252', '116', '清河区', '11-116-1252', '3', 'Qinghe Qu', '江苏省淮安市清河区', 'QHH');
INSERT INTO `t_area` VALUES ('1253', '116', '楚州区', '11-116-1253', '3', 'Chuzhou Qu', '江苏省淮安市楚州区', '2');
INSERT INTO `t_area` VALUES ('1254', '116', '淮阴区', '11-116-1254', '3', 'Huaiyin Qu', '江苏省淮安市淮阴区', '2');
INSERT INTO `t_area` VALUES ('1255', '116', '清浦区', '11-116-1255', '3', 'Qingpu Qu', '江苏省淮安市清浦区', 'QPQ');
INSERT INTO `t_area` VALUES ('1256', '116', '涟水县', '11-116-1256', '3', 'Lianshui Xian', '江苏省淮安市涟水县', 'LSI');
INSERT INTO `t_area` VALUES ('1257', '116', '洪泽县', '11-116-1257', '3', 'Hongze Xian', '江苏省淮安市洪泽县', 'HGZ');
INSERT INTO `t_area` VALUES ('1258', '116', '盱眙县', '11-116-1258', '3', 'Xuyi Xian', '江苏省淮安市盱眙县', 'XUY');
INSERT INTO `t_area` VALUES ('1259', '116', '金湖县', '11-116-1259', '3', 'Jinhu Xian', '江苏省淮安市金湖县', 'JHU');
INSERT INTO `t_area` VALUES ('1261', '117', '亭湖区', '11-117-1261', '3', 'Tinghu Qu', '江苏省盐城市亭湖区', '2');
INSERT INTO `t_area` VALUES ('1262', '117', '盐都区', '11-117-1262', '3', 'Yandu Qu', '江苏省盐城市盐都区', '2');
INSERT INTO `t_area` VALUES ('1263', '117', '响水县', '11-117-1263', '3', 'Xiangshui Xian', '江苏省盐城市响水县', 'XSH');
INSERT INTO `t_area` VALUES ('1264', '117', '滨海县', '11-117-1264', '3', 'Binhai Xian', '江苏省盐城市滨海县', 'BHI');
INSERT INTO `t_area` VALUES ('1265', '117', '阜宁县', '11-117-1265', '3', 'Funing Xian', '江苏省盐城市阜宁县', 'FNG');
INSERT INTO `t_area` VALUES ('1266', '117', '射阳县', '11-117-1266', '3', 'Sheyang Xian', '江苏省盐城市射阳县', 'SEY');
INSERT INTO `t_area` VALUES ('1267', '117', '建湖县', '11-117-1267', '3', 'Jianhu Xian', '江苏省盐城市建湖县', 'JIH');
INSERT INTO `t_area` VALUES ('1268', '117', '东台市', '11-117-1268', '3', 'Dongtai Shi', '江苏省盐城市东台市', 'DTS');
INSERT INTO `t_area` VALUES ('1269', '117', '大丰市', '11-117-1269', '3', 'Dafeng Shi', '江苏省盐城市大丰市', 'DFS');
INSERT INTO `t_area` VALUES ('1271', '118', '广陵区', '11-118-1271', '3', 'Guangling Qu', '江苏省扬州市广陵区', 'GGL');
INSERT INTO `t_area` VALUES ('1272', '118', '邗江区', '11-118-1272', '3', 'Hanjiang Qu', '江苏省扬州市邗江区', '2');
INSERT INTO `t_area` VALUES ('1273', '118', '维扬区', '11-118-1273', '3', 'Weiyang Qu', '江苏省扬州市维扬区', '2');
INSERT INTO `t_area` VALUES ('1274', '118', '宝应县', '11-118-1274', '3', 'Baoying Xian ', '江苏省扬州市宝应县', 'BYI');
INSERT INTO `t_area` VALUES ('1275', '118', '仪征市', '11-118-1275', '3', 'Yizheng Shi', '江苏省扬州市仪征市', 'YZE');
INSERT INTO `t_area` VALUES ('1276', '118', '高邮市', '11-118-1276', '3', 'Gaoyou Shi', '江苏省扬州市高邮市', 'GYO');
INSERT INTO `t_area` VALUES ('1277', '118', '江都市', '11-118-1277', '3', 'Jiangdu Shi', '江苏省扬州市江都市', 'JDU');
INSERT INTO `t_area` VALUES ('1279', '119', '京口区', '11-119-1279', '3', 'Jingkou Qu', '江苏省镇江市京口区', '2');
INSERT INTO `t_area` VALUES ('1280', '119', '润州区', '11-119-1280', '3', 'Runzhou Qu', '江苏省镇江市润州区', 'RZQ');
INSERT INTO `t_area` VALUES ('1281', '119', '丹徒区', '11-119-1281', '3', 'Dantu Qu', '江苏省镇江市丹徒区', '2');
INSERT INTO `t_area` VALUES ('1282', '119', '丹阳市', '11-119-1282', '3', 'Danyang Xian', '江苏省镇江市丹阳市', 'DNY');
INSERT INTO `t_area` VALUES ('1283', '119', '扬中市', '11-119-1283', '3', 'Yangzhong Shi', '江苏省镇江市扬中市', 'YZG');
INSERT INTO `t_area` VALUES ('1284', '119', '句容市', '11-119-1284', '3', 'Jurong Shi', '江苏省镇江市句容市', 'JRG');
INSERT INTO `t_area` VALUES ('1286', '120', '海陵区', '11-120-1286', '3', 'Hailing Qu', '江苏省泰州市海陵区', 'HIL');
INSERT INTO `t_area` VALUES ('1287', '120', '高港区', '11-120-1287', '3', 'Gaogang Qu', '江苏省泰州市高港区', 'GGQ');
INSERT INTO `t_area` VALUES ('1288', '120', '兴化市', '11-120-1288', '3', 'Xinghua Shi', '江苏省泰州市兴化市', 'XHS');
INSERT INTO `t_area` VALUES ('1289', '120', '靖江市', '11-120-1289', '3', 'Jingjiang Shi', '江苏省泰州市靖江市', 'JGJ');
INSERT INTO `t_area` VALUES ('1290', '120', '泰兴市', '11-120-1290', '3', 'Taixing Shi', '江苏省泰州市泰兴市', 'TXG');
INSERT INTO `t_area` VALUES ('1291', '120', '姜堰市', '11-120-1291', '3', 'Jiangyan Shi', '江苏省泰州市姜堰市', 'JYS');
INSERT INTO `t_area` VALUES ('1293', '121', '宿城区', '11-121-1293', '3', 'Sucheng Qu', '江苏省宿迁市宿城区', 'SCE');
INSERT INTO `t_area` VALUES ('1294', '121', '宿豫区', '11-121-1294', '3', 'Suyu Qu', '江苏省宿迁市宿豫区', '2');
INSERT INTO `t_area` VALUES ('1295', '121', '沭阳县', '11-121-1295', '3', 'Shuyang Xian', '江苏省宿迁市沭阳县', 'SYD');
INSERT INTO `t_area` VALUES ('1296', '121', '泗阳县', '11-121-1296', '3', 'Siyang Xian ', '江苏省宿迁市泗阳县', 'SIY');
INSERT INTO `t_area` VALUES ('1297', '121', '泗洪县', '11-121-1297', '3', 'Sihong Xian', '江苏省宿迁市泗洪县', 'SIH');
INSERT INTO `t_area` VALUES ('1299', '122', '上城区', '12-122-1299', '3', 'Shangcheng Qu', '浙江省杭州市上城区', 'SCQ');
INSERT INTO `t_area` VALUES ('1300', '122', '下城区', '12-122-1300', '3', 'Xiacheng Qu', '浙江省杭州市下城区', 'XCG');
INSERT INTO `t_area` VALUES ('1301', '122', '江干区', '12-122-1301', '3', 'Jianggan Qu', '浙江省杭州市江干区', 'JGQ');
INSERT INTO `t_area` VALUES ('1302', '122', '拱墅区', '12-122-1302', '3', 'Gongshu Qu', '浙江省杭州市拱墅区', 'GSQ');
INSERT INTO `t_area` VALUES ('1303', '122', '西湖区', '12-122-1303', '3', 'Xihu Qu ', '浙江省杭州市西湖区', 'XHU');
INSERT INTO `t_area` VALUES ('1304', '122', '滨江区', '12-122-1304', '3', 'Binjiang Qu', '浙江省杭州市滨江区', 'BJQ');
INSERT INTO `t_area` VALUES ('1305', '122', '萧山区', '12-122-1305', '3', 'Xiaoshan Qu', '浙江省杭州市萧山区', '2');
INSERT INTO `t_area` VALUES ('1306', '122', '余杭区', '12-122-1306', '3', 'Yuhang Qu', '浙江省杭州市余杭区', '2');
INSERT INTO `t_area` VALUES ('1307', '122', '桐庐县', '12-122-1307', '3', 'Tonglu Xian', '浙江省杭州市桐庐县', 'TLU');
INSERT INTO `t_area` VALUES ('1308', '122', '淳安县', '12-122-1308', '3', 'Chun,an Xian', '浙江省杭州市淳安县', 'CAZ');
INSERT INTO `t_area` VALUES ('1309', '122', '建德市', '12-122-1309', '3', 'Jiande Shi', '浙江省杭州市建德市', 'JDS');
INSERT INTO `t_area` VALUES ('1310', '122', '富阳市', '12-122-1310', '3', 'Fuyang Shi', '浙江省杭州市富阳市', 'FYZ');
INSERT INTO `t_area` VALUES ('1311', '122', '临安市', '12-122-1311', '3', 'Lin,an Shi', '浙江省杭州市临安市', 'LNA');
INSERT INTO `t_area` VALUES ('1313', '123', '海曙区', '12-123-1313', '3', 'Haishu Qu', '浙江省宁波市海曙区', 'HNB');
INSERT INTO `t_area` VALUES ('1314', '123', '江东区', '12-123-1314', '3', 'Jiangdong Qu', '浙江省宁波市江东区', 'JDO');
INSERT INTO `t_area` VALUES ('1315', '123', '江北区', '12-123-1315', '3', 'Jiangbei Qu', '浙江省宁波市江北区', 'JBQ');
INSERT INTO `t_area` VALUES ('1316', '123', '北仑区', '12-123-1316', '3', 'Beilun Qu', '浙江省宁波市北仑区', 'BLN');
INSERT INTO `t_area` VALUES ('1317', '123', '镇海区', '12-123-1317', '3', 'Zhenhai Qu', '浙江省宁波市镇海区', 'ZHF');
INSERT INTO `t_area` VALUES ('1318', '123', '鄞州区', '12-123-1318', '3', 'Yinzhou Qu', '浙江省宁波市鄞州区', '2');
INSERT INTO `t_area` VALUES ('1319', '123', '象山县', '12-123-1319', '3', 'Xiangshan Xian', '浙江省宁波市象山县', 'YSZ');
INSERT INTO `t_area` VALUES ('1320', '123', '宁海县', '12-123-1320', '3', 'Ninghai Xian', '浙江省宁波市宁海县', 'NHI');
INSERT INTO `t_area` VALUES ('1321', '123', '余姚市', '12-123-1321', '3', 'Yuyao Shi', '浙江省宁波市余姚市', 'YYO');
INSERT INTO `t_area` VALUES ('1322', '123', '慈溪市', '12-123-1322', '3', 'Cixi Shi', '浙江省宁波市慈溪市', 'CXI');
INSERT INTO `t_area` VALUES ('1323', '123', '奉化市', '12-123-1323', '3', 'Fenghua Shi', '浙江省宁波市奉化市', 'FHU');
INSERT INTO `t_area` VALUES ('1325', '124', '鹿城区', '12-124-1325', '3', 'Lucheng Qu', '浙江省温州市鹿城区', 'LUW');
INSERT INTO `t_area` VALUES ('1326', '124', '龙湾区', '12-124-1326', '3', 'Longwan Qu', '浙江省温州市龙湾区', 'LWW');
INSERT INTO `t_area` VALUES ('1327', '124', '瓯海区', '12-124-1327', '3', 'Ouhai Qu', '浙江省温州市瓯海区', 'OHQ');
INSERT INTO `t_area` VALUES ('1328', '124', '洞头县', '12-124-1328', '3', 'Dongtou Xian', '浙江省温州市洞头县', 'DTO');
INSERT INTO `t_area` VALUES ('1329', '124', '永嘉县', '12-124-1329', '3', 'Yongjia Xian', '浙江省温州市永嘉县', 'YJX');
INSERT INTO `t_area` VALUES ('1330', '124', '平阳县', '12-124-1330', '3', 'Pingyang Xian', '浙江省温州市平阳县', 'PYG');
INSERT INTO `t_area` VALUES ('1331', '124', '苍南县', '12-124-1331', '3', 'Cangnan Xian', '浙江省温州市苍南县', 'CAN');
INSERT INTO `t_area` VALUES ('1332', '124', '文成县', '12-124-1332', '3', 'Wencheng Xian', '浙江省温州市文成县', 'WCZ');
INSERT INTO `t_area` VALUES ('1333', '124', '泰顺县', '12-124-1333', '3', 'Taishun Xian', '浙江省温州市泰顺县', 'TSZ');
INSERT INTO `t_area` VALUES ('1334', '124', '瑞安市', '12-124-1334', '3', 'Rui,an Xian', '浙江省温州市瑞安市', 'RAS');
INSERT INTO `t_area` VALUES ('1335', '124', '乐清市', '12-124-1335', '3', 'Yueqing Shi', '浙江省温州市乐清市', 'YQZ');
INSERT INTO `t_area` VALUES ('1338', '125', '秀洲区', '12-125-1338', '3', 'Xiuzhou Qu', '浙江省嘉兴市秀洲区', '2');
INSERT INTO `t_area` VALUES ('1339', '125', '嘉善县', '12-125-1339', '3', 'Jiashan Xian', '浙江省嘉兴市嘉善县', 'JSK');
INSERT INTO `t_area` VALUES ('1340', '125', '海盐县', '12-125-1340', '3', 'Haiyan Xian', '浙江省嘉兴市海盐县', 'HYN');
INSERT INTO `t_area` VALUES ('1341', '125', '海宁市', '12-125-1341', '3', 'Haining Shi', '浙江省嘉兴市海宁市', 'HNG');
INSERT INTO `t_area` VALUES ('1342', '125', '平湖市', '12-125-1342', '3', 'Pinghu Shi', '浙江省嘉兴市平湖市', 'PHU');
INSERT INTO `t_area` VALUES ('1343', '125', '桐乡市', '12-125-1343', '3', 'Tongxiang Shi', '浙江省嘉兴市桐乡市', 'TXZ');
INSERT INTO `t_area` VALUES ('1345', '126', '吴兴区', '12-126-1345', '3', 'Wuxing Qu', '浙江省湖州市吴兴区', '2');
INSERT INTO `t_area` VALUES ('1346', '126', '南浔区', '12-126-1346', '3', 'Nanxun Qu', '浙江省湖州市南浔区', '2');
INSERT INTO `t_area` VALUES ('1347', '126', '德清县', '12-126-1347', '3', 'Deqing Xian', '浙江省湖州市德清县', 'DQX');
INSERT INTO `t_area` VALUES ('1348', '126', '长兴县', '12-126-1348', '3', 'Changxing Xian', '浙江省湖州市长兴县', 'CXG');
INSERT INTO `t_area` VALUES ('1349', '126', '安吉县', '12-126-1349', '3', 'Anji Xian', '浙江省湖州市安吉县', 'AJI');
INSERT INTO `t_area` VALUES ('1351', '127', '越城区', '12-127-1351', '3', 'Yuecheng Qu', '浙江省绍兴市越城区', 'YSX');
INSERT INTO `t_area` VALUES ('1352', '127', '绍兴县', '12-127-1352', '3', 'Shaoxing Xian', '浙江省绍兴市绍兴县', 'SXZ');
INSERT INTO `t_area` VALUES ('1353', '127', '新昌县', '12-127-1353', '3', 'Xinchang Xian', '浙江省绍兴市新昌县', 'XCX');
INSERT INTO `t_area` VALUES ('1354', '127', '诸暨市', '12-127-1354', '3', 'Zhuji Shi', '浙江省绍兴市诸暨市', 'ZHJ');
INSERT INTO `t_area` VALUES ('1355', '127', '上虞市', '12-127-1355', '3', 'Shangyu Shi', '浙江省绍兴市上虞市', 'SYZ');
INSERT INTO `t_area` VALUES ('1356', '127', '嵊州市', '12-127-1356', '3', 'Shengzhou Shi', '浙江省绍兴市嵊州市', 'SGZ');
INSERT INTO `t_area` VALUES ('1358', '128', '婺城区', '12-128-1358', '3', 'Wucheng Qu', '浙江省金华市婺城区', 'WCF');
INSERT INTO `t_area` VALUES ('1359', '128', '金东区', '12-128-1359', '3', 'Jindong Qu', '浙江省金华市金东区', '2');
INSERT INTO `t_area` VALUES ('1360', '128', '武义县', '12-128-1360', '3', 'Wuyi Xian', '浙江省金华市武义县', 'WYX');
INSERT INTO `t_area` VALUES ('1361', '128', '浦江县', '12-128-1361', '3', 'Pujiang Xian ', '浙江省金华市浦江县', 'PJG');
INSERT INTO `t_area` VALUES ('1362', '128', '磐安县', '12-128-1362', '3', 'Pan,an Xian', '浙江省金华市磐安县', 'PAX');
INSERT INTO `t_area` VALUES ('1363', '128', '兰溪市', '12-128-1363', '3', 'Lanxi Shi', '浙江省金华市兰溪市', 'LXZ');
INSERT INTO `t_area` VALUES ('1364', '128', '义乌市', '12-128-1364', '3', 'Yiwu Shi', '浙江省金华市义乌市', 'YWS');
INSERT INTO `t_area` VALUES ('1365', '128', '东阳市', '12-128-1365', '3', 'Dongyang Shi', '浙江省金华市东阳市', 'DGY');
INSERT INTO `t_area` VALUES ('1366', '128', '永康市', '12-128-1366', '3', 'Yongkang Shi', '浙江省金华市永康市', 'YKG');
INSERT INTO `t_area` VALUES ('1368', '129', '柯城区', '12-129-1368', '3', 'Kecheng Qu', '浙江省衢州市柯城区', 'KEC');
INSERT INTO `t_area` VALUES ('1369', '129', '衢江区', '12-129-1369', '3', 'Qujiang Qu', '浙江省衢州市衢江区', '2');
INSERT INTO `t_area` VALUES ('1370', '129', '常山县', '12-129-1370', '3', 'Changshan Xian', '浙江省衢州市常山县', 'CSN');
INSERT INTO `t_area` VALUES ('1371', '129', '开化县', '12-129-1371', '3', 'Kaihua Xian', '浙江省衢州市开化县', 'KHU');
INSERT INTO `t_area` VALUES ('1372', '129', '龙游县', '12-129-1372', '3', 'Longyou Xian ', '浙江省衢州市龙游县', 'LGY');
INSERT INTO `t_area` VALUES ('1373', '129', '江山市', '12-129-1373', '3', 'Jiangshan Shi', '浙江省衢州市江山市', 'JIS');
INSERT INTO `t_area` VALUES ('1375', '130', '定海区', '12-130-1375', '3', 'Dinghai Qu', '浙江省舟山市定海区', 'DHQ');
INSERT INTO `t_area` VALUES ('1376', '130', '普陀区', '12-130-1376', '3', 'Putuo Qu', '浙江省舟山市普陀区', 'PTO');
INSERT INTO `t_area` VALUES ('1377', '130', '岱山县', '12-130-1377', '3', 'Daishan Xian', '浙江省舟山市岱山县', 'DSH');
INSERT INTO `t_area` VALUES ('1378', '130', '嵊泗县', '12-130-1378', '3', 'Shengsi Xian', '浙江省舟山市嵊泗县', 'SSZ');
INSERT INTO `t_area` VALUES ('1380', '131', '椒江区', '12-131-1380', '3', 'Jiaojiang Qu', '浙江省台州市椒江区', 'JJT');
INSERT INTO `t_area` VALUES ('1381', '131', '黄岩区', '12-131-1381', '3', 'Huangyan Qu', '浙江省台州市黄岩区', 'HYT');
INSERT INTO `t_area` VALUES ('1382', '131', '路桥区', '12-131-1382', '3', 'Luqiao Qu', '浙江省台州市路桥区', 'LQT');
INSERT INTO `t_area` VALUES ('1383', '131', '玉环县', '12-131-1383', '3', 'Yuhuan Xian', '浙江省台州市玉环县', 'YHN');
INSERT INTO `t_area` VALUES ('1384', '131', '三门县', '12-131-1384', '3', 'Sanmen Xian', '浙江省台州市三门县', 'SMN');
INSERT INTO `t_area` VALUES ('1385', '131', '天台县', '12-131-1385', '3', 'Tiantai Xian', '浙江省台州市天台县', 'TTA');
INSERT INTO `t_area` VALUES ('1386', '131', '仙居县', '12-131-1386', '3', 'Xianju Xian', '浙江省台州市仙居县', 'XJU');
INSERT INTO `t_area` VALUES ('1387', '131', '温岭市', '12-131-1387', '3', 'Wenling Shi', '浙江省台州市温岭市', 'WLS');
INSERT INTO `t_area` VALUES ('1388', '131', '临海市', '12-131-1388', '3', 'Linhai Shi', '浙江省台州市临海市', 'LHI');
INSERT INTO `t_area` VALUES ('1390', '132', '莲都区', '12-132-1390', '3', 'Liandu Qu', '浙江省丽水市莲都区', '2');
INSERT INTO `t_area` VALUES ('1391', '132', '青田县', '12-132-1391', '3', 'Qingtian Xian', '浙江省丽水市青田县', '2');
INSERT INTO `t_area` VALUES ('1392', '132', '缙云县', '12-132-1392', '3', 'Jinyun Xian', '浙江省丽水市缙云县', '2');
INSERT INTO `t_area` VALUES ('1393', '132', '遂昌县', '12-132-1393', '3', 'Suichang Xian', '浙江省丽水市遂昌县', '2');
INSERT INTO `t_area` VALUES ('1394', '132', '松阳县', '12-132-1394', '3', 'Songyang Xian', '浙江省丽水市松阳县', '2');
INSERT INTO `t_area` VALUES ('1395', '132', '云和县', '12-132-1395', '3', 'Yunhe Xian', '浙江省丽水市云和县', '2');
INSERT INTO `t_area` VALUES ('1396', '132', '庆元县', '12-132-1396', '3', 'Qingyuan Xian', '浙江省丽水市庆元县', '2');
INSERT INTO `t_area` VALUES ('1397', '132', '景宁畲族自治县', '12-132-1397', '3', 'Jingning Shezu Zizhixian', '浙江省丽水市景宁畲族自治县', '2');
INSERT INTO `t_area` VALUES ('1398', '132', '龙泉市', '12-132-1398', '3', 'Longquan Shi', '浙江省丽水市龙泉市', '2');
INSERT INTO `t_area` VALUES ('1400', '133', '瑶海区', '13-133-1400', '3', 'Yaohai Qu', '安徽省合肥市瑶海区', '2');
INSERT INTO `t_area` VALUES ('1401', '133', '庐阳区', '13-133-1401', '3', 'Luyang Qu', '安徽省合肥市庐阳区', '2');
INSERT INTO `t_area` VALUES ('1402', '133', '蜀山区', '13-133-1402', '3', 'Shushan Qu', '安徽省合肥市蜀山区', '2');
INSERT INTO `t_area` VALUES ('1403', '133', '包河区', '13-133-1403', '3', 'Baohe Qu', '安徽省合肥市包河区', '2');
INSERT INTO `t_area` VALUES ('1404', '133', '长丰县', '13-133-1404', '3', 'Changfeng Xian', '安徽省合肥市长丰县', 'CFG');
INSERT INTO `t_area` VALUES ('1405', '133', '肥东县', '13-133-1405', '3', 'Feidong Xian', '安徽省合肥市肥东县', 'FDO');
INSERT INTO `t_area` VALUES ('1406', '133', '肥西县', '13-133-1406', '3', 'Feixi Xian', '安徽省合肥市肥西县', 'FIX');
INSERT INTO `t_area` VALUES ('1408', '134', '镜湖区', '13-134-1408', '3', 'Jinghu Qu', '安徽省芜湖市镜湖区', 'JHW');
INSERT INTO `t_area` VALUES ('1409', '134', '三山区', '13-134-1409', '3', 'Matang Qu', '安徽省芜湖市三山区', '2');
INSERT INTO `t_area` VALUES ('1410', '134', '弋江区', '13-134-1410', '3', 'Xinwu Qu', '安徽省芜湖市弋江区', '2');
INSERT INTO `t_area` VALUES ('1411', '134', '鸠江区', '13-134-1411', '3', 'Jiujiang Qu', '安徽省芜湖市鸠江区', 'JJW');
INSERT INTO `t_area` VALUES ('1413', '134', '繁昌县', '13-134-1413', '3', 'Fanchang Xian', '安徽省芜湖市繁昌县', 'FCH');
INSERT INTO `t_area` VALUES ('1414', '134', '南陵县', '13-134-1414', '3', 'Nanling Xian', '安徽省芜湖市南陵县', 'NLX');
INSERT INTO `t_area` VALUES ('1416', '135', '龙子湖区', '13-135-1416', '3', 'Longzihu Qu', '安徽省蚌埠市龙子湖区', '2');
INSERT INTO `t_area` VALUES ('1417', '135', '蚌山区', '13-135-1417', '3', 'Bangshan Qu', '安徽省蚌埠市蚌山区', '2');
INSERT INTO `t_area` VALUES ('1418', '135', '禹会区', '13-135-1418', '3', 'Yuhui Qu', '安徽省蚌埠市禹会区', '2');
INSERT INTO `t_area` VALUES ('1419', '135', '淮上区', '13-135-1419', '3', 'Huaishang Qu', '安徽省蚌埠市淮上区', '2');
INSERT INTO `t_area` VALUES ('1420', '135', '怀远县', '13-135-1420', '3', 'Huaiyuan Qu', '安徽省蚌埠市怀远县', 'HYW');
INSERT INTO `t_area` VALUES ('1421', '135', '五河县', '13-135-1421', '3', 'Wuhe Xian', '安徽省蚌埠市五河县', 'WHE');
INSERT INTO `t_area` VALUES ('1422', '135', '固镇县', '13-135-1422', '3', 'Guzhen Xian', '安徽省蚌埠市固镇县', 'GZX');
INSERT INTO `t_area` VALUES ('1424', '136', '大通区', '13-136-1424', '3', 'Datong Qu', '安徽省淮南市大通区', 'DTQ');
INSERT INTO `t_area` VALUES ('1425', '136', '田家庵区', '13-136-1425', '3', 'Tianjia,an Qu', '安徽省淮南市田家庵区', 'TJA');
INSERT INTO `t_area` VALUES ('1426', '136', '谢家集区', '13-136-1426', '3', 'Xiejiaji Qu', '安徽省淮南市谢家集区', 'XJJ');
INSERT INTO `t_area` VALUES ('1427', '136', '八公山区', '13-136-1427', '3', 'Bagongshan Qu', '安徽省淮南市八公山区', 'BGS');
INSERT INTO `t_area` VALUES ('1428', '136', '潘集区', '13-136-1428', '3', 'Panji Qu', '安徽省淮南市潘集区', 'PJI');
INSERT INTO `t_area` VALUES ('1429', '136', '凤台县', '13-136-1429', '3', 'Fengtai Xian', '安徽省淮南市凤台县', '2');
INSERT INTO `t_area` VALUES ('1431', '137', '金家庄区', '13-137-1431', '3', 'Jinjiazhuang Qu', '安徽省马鞍山市金家庄区', 'JJZ');
INSERT INTO `t_area` VALUES ('1432', '137', '花山区', '13-137-1432', '3', 'Huashan Qu', '安徽省马鞍山市花山区', 'HSM');
INSERT INTO `t_area` VALUES ('1433', '137', '雨山区', '13-137-1433', '3', 'Yushan Qu', '安徽省马鞍山市雨山区', 'YSQ');
INSERT INTO `t_area` VALUES ('1434', '137', '当涂县', '13-137-1434', '3', 'Dangtu Xian', '安徽省马鞍山市当涂县', 'DTU');
INSERT INTO `t_area` VALUES ('1436', '138', '杜集区', '13-138-1436', '3', 'Duji Qu', '安徽省淮北市杜集区', 'DJQ');
INSERT INTO `t_area` VALUES ('1437', '138', '相山区', '13-138-1437', '3', 'Xiangshan Qu', '安徽省淮北市相山区', 'XSA');
INSERT INTO `t_area` VALUES ('1438', '138', '烈山区', '13-138-1438', '3', 'Lieshan Qu', '安徽省淮北市烈山区', 'LHB');
INSERT INTO `t_area` VALUES ('1439', '138', '濉溪县', '13-138-1439', '3', 'Suixi Xian', '安徽省淮北市濉溪县', 'SXW');
INSERT INTO `t_area` VALUES ('1441', '139', '铜官山区', '13-139-1441', '3', 'Tongguanshan Qu', '安徽省铜陵市铜官山区', 'TGQ');
INSERT INTO `t_area` VALUES ('1442', '139', '狮子山区', '13-139-1442', '3', 'Shizishan Qu', '安徽省铜陵市狮子山区', 'SZN');
INSERT INTO `t_area` VALUES ('1444', '139', '铜陵县', '13-139-1444', '3', 'Tongling Xian', '安徽省铜陵市铜陵县', 'TLX');
INSERT INTO `t_area` VALUES ('1446', '140', '迎江区', '13-140-1446', '3', 'Yingjiang Qu', '安徽省安庆市迎江区', 'YJQ');
INSERT INTO `t_area` VALUES ('1447', '140', '大观区', '13-140-1447', '3', 'Daguan Qu', '安徽省安庆市大观区', 'DGQ');
INSERT INTO `t_area` VALUES ('1448', '140', '宜秀区', '13-140-1448', '3', 'Yixiu Qu', '安徽省安庆市宜秀区', '2');
INSERT INTO `t_area` VALUES ('1449', '140', '怀宁县', '13-140-1449', '3', 'Huaining Xian', '安徽省安庆市怀宁县', 'HNW');
INSERT INTO `t_area` VALUES ('1450', '140', '枞阳县', '13-140-1450', '3', 'Zongyang Xian', '安徽省安庆市枞阳县', 'ZYW');
INSERT INTO `t_area` VALUES ('1451', '140', '潜山县', '13-140-1451', '3', 'Qianshan Xian', '安徽省安庆市潜山县', 'QSW');
INSERT INTO `t_area` VALUES ('1452', '140', '太湖县', '13-140-1452', '3', 'Taihu Xian', '安徽省安庆市太湖县', 'THU');
INSERT INTO `t_area` VALUES ('1453', '140', '宿松县', '13-140-1453', '3', 'Susong Xian', '安徽省安庆市宿松县', 'SUS');
INSERT INTO `t_area` VALUES ('1454', '140', '望江县', '13-140-1454', '3', 'Wangjiang Xian', '安徽省安庆市望江县', 'WJX');
INSERT INTO `t_area` VALUES ('1455', '140', '岳西县', '13-140-1455', '3', 'Yuexi Xian', '安徽省安庆市岳西县', 'YXW');
INSERT INTO `t_area` VALUES ('1456', '140', '桐城市', '13-140-1456', '3', 'Tongcheng Shi', '安徽省安庆市桐城市', 'TCW');
INSERT INTO `t_area` VALUES ('1458', '141', '屯溪区', '13-141-1458', '3', 'Tunxi Qu', '安徽省黄山市屯溪区', 'TXN');
INSERT INTO `t_area` VALUES ('1459', '141', '黄山区', '13-141-1459', '3', 'Huangshan Qu', '安徽省黄山市黄山区', 'HSK');
INSERT INTO `t_area` VALUES ('1460', '141', '徽州区', '13-141-1460', '3', 'Huizhou Qu', '安徽省黄山市徽州区', 'HZQ');
INSERT INTO `t_area` VALUES ('1461', '141', '歙县', '13-141-1461', '3', 'She Xian', '安徽省黄山市歙县', 'SEX');
INSERT INTO `t_area` VALUES ('1462', '141', '休宁县', '13-141-1462', '3', 'Xiuning Xian', '安徽省黄山市休宁县', 'XUN');
INSERT INTO `t_area` VALUES ('1463', '141', '黟县', '13-141-1463', '3', 'Yi Xian', '安徽省黄山市黟县', 'YIW');
INSERT INTO `t_area` VALUES ('1464', '141', '祁门县', '13-141-1464', '3', 'Qimen Xian', '安徽省黄山市祁门县', 'QMN');
INSERT INTO `t_area` VALUES ('1466', '142', '琅琊区', '13-142-1466', '3', 'Langya Qu', '安徽省滁州市琅琊区', 'LYU');
INSERT INTO `t_area` VALUES ('1467', '142', '南谯区', '13-142-1467', '3', 'Nanqiao Qu', '安徽省滁州市南谯区', 'NQQ');
INSERT INTO `t_area` VALUES ('1468', '142', '来安县', '13-142-1468', '3', 'Lai,an Xian', '安徽省滁州市来安县', 'LAX');
INSERT INTO `t_area` VALUES ('1469', '142', '全椒县', '13-142-1469', '3', 'Quanjiao Xian', '安徽省滁州市全椒县', 'QJO');
INSERT INTO `t_area` VALUES ('1470', '142', '定远县', '13-142-1470', '3', 'Dingyuan Xian', '安徽省滁州市定远县', 'DYW');
INSERT INTO `t_area` VALUES ('1471', '142', '凤阳县', '13-142-1471', '3', 'Fengyang Xian', '安徽省滁州市凤阳县', 'FYG');
INSERT INTO `t_area` VALUES ('1472', '142', '天长市', '13-142-1472', '3', 'Tianchang Shi', '安徽省滁州市天长市', 'TNC');
INSERT INTO `t_area` VALUES ('1473', '142', '明光市', '13-142-1473', '3', 'Mingguang Shi', '安徽省滁州市明光市', 'MGG');
INSERT INTO `t_area` VALUES ('1475', '143', '颍州区', '13-143-1475', '3', 'Yingzhou Qu', '安徽省阜阳市颍州区', '2');
INSERT INTO `t_area` VALUES ('1476', '143', '颍东区', '13-143-1476', '3', 'Yingdong Qu', '安徽省阜阳市颍东区', '2');
INSERT INTO `t_area` VALUES ('1477', '143', '颍泉区', '13-143-1477', '3', 'Yingquan Qu', '安徽省阜阳市颍泉区', '2');
INSERT INTO `t_area` VALUES ('1478', '143', '临泉县', '13-143-1478', '3', 'Linquan Xian', '安徽省阜阳市临泉县', 'LQN');
INSERT INTO `t_area` VALUES ('1479', '143', '太和县', '13-143-1479', '3', 'Taihe Xian', '安徽省阜阳市太和县', 'TIH');
INSERT INTO `t_area` VALUES ('1480', '143', '阜南县', '13-143-1480', '3', 'Funan Xian', '安徽省阜阳市阜南县', 'FNX');
INSERT INTO `t_area` VALUES ('1481', '143', '颍上县', '13-143-1481', '3', 'Yingshang Xian', '安徽省阜阳市颍上县', '2');
INSERT INTO `t_area` VALUES ('1482', '143', '界首市', '13-143-1482', '3', 'Jieshou Shi', '安徽省阜阳市界首市', 'JSW');
INSERT INTO `t_area` VALUES ('1484', '144', '埇桥区', '13-144-1484', '3', 'Yongqiao Qu', '安徽省宿州市埇桥区', '2');
INSERT INTO `t_area` VALUES ('1485', '144', '砀山县', '13-144-1485', '3', 'Dangshan Xian', '安徽省宿州市砀山县', 'DSW');
INSERT INTO `t_area` VALUES ('1486', '144', '萧县', '13-144-1486', '3', 'Xiao Xian', '安徽省宿州市萧县', 'XIO');
INSERT INTO `t_area` VALUES ('1487', '144', '灵璧县', '13-144-1487', '3', 'Lingbi Xian', '安徽省宿州市灵璧县', 'LBI');
INSERT INTO `t_area` VALUES ('1488', '144', '泗县', '13-144-1488', '3', 'Si Xian ', '安徽省宿州市泗县', 'SIX');
INSERT INTO `t_area` VALUES ('1490', '145', '居巢区', '13-145-1490', '3', 'Juchao Qu', '安徽省巢湖市居巢区', '2');
INSERT INTO `t_area` VALUES ('1491', '145', '庐江县', '13-145-1491', '3', 'Lujiang Xian', '安徽省巢湖市庐江县', '2');
INSERT INTO `t_area` VALUES ('1492', '145', '无为县', '13-145-1492', '3', 'Wuwei Xian', '安徽省巢湖市无为县', '2');
INSERT INTO `t_area` VALUES ('1493', '145', '含山县', '13-145-1493', '3', 'Hanshan Xian', '安徽省巢湖市含山县', '2');
INSERT INTO `t_area` VALUES ('1494', '145', '和县', '13-145-1494', '3', 'He Xian ', '安徽省巢湖市和县', '2');
INSERT INTO `t_area` VALUES ('1496', '146', '金安区', '13-146-1496', '3', 'Jinan Qu', '安徽省六安市金安区', '2');
INSERT INTO `t_area` VALUES ('1497', '146', '裕安区', '13-146-1497', '3', 'Yuan Qu', '安徽省六安市裕安区', '2');
INSERT INTO `t_area` VALUES ('1498', '146', '寿县', '13-146-1498', '3', 'Shou Xian', '安徽省六安市寿县', '2');
INSERT INTO `t_area` VALUES ('1499', '146', '霍邱县', '13-146-1499', '3', 'Huoqiu Xian', '安徽省六安市霍邱县', '2');
INSERT INTO `t_area` VALUES ('1500', '146', '舒城县', '13-146-1500', '3', 'Shucheng Xian', '安徽省六安市舒城县', '2');
INSERT INTO `t_area` VALUES ('1501', '146', '金寨县', '13-146-1501', '3', 'Jingzhai Xian', '安徽省六安市金寨县', '2');
INSERT INTO `t_area` VALUES ('1502', '146', '霍山县', '13-146-1502', '3', 'Huoshan Xian', '安徽省六安市霍山县', '2');
INSERT INTO `t_area` VALUES ('1504', '147', '谯城区', '13-147-1504', '3', 'Qiaocheng Qu', '安徽省亳州市谯城区', '2');
INSERT INTO `t_area` VALUES ('1505', '147', '涡阳县', '13-147-1505', '3', 'Guoyang Xian', '安徽省亳州市涡阳县', '2');
INSERT INTO `t_area` VALUES ('1506', '147', '蒙城县', '13-147-1506', '3', 'Mengcheng Xian', '安徽省亳州市蒙城县', '2');
INSERT INTO `t_area` VALUES ('1507', '147', '利辛县', '13-147-1507', '3', 'Lixin Xian', '安徽省亳州市利辛县', '2');
INSERT INTO `t_area` VALUES ('1509', '148', '贵池区', '13-148-1509', '3', 'Guichi Qu', '安徽省池州市贵池区', '2');
INSERT INTO `t_area` VALUES ('1510', '148', '东至县', '13-148-1510', '3', 'Dongzhi Xian', '安徽省池州市东至县', '2');
INSERT INTO `t_area` VALUES ('1511', '148', '石台县', '13-148-1511', '3', 'Shitai Xian', '安徽省池州市石台县', '2');
INSERT INTO `t_area` VALUES ('1512', '148', '青阳县', '13-148-1512', '3', 'Qingyang Xian', '安徽省池州市青阳县', '2');
INSERT INTO `t_area` VALUES ('1514', '149', '宣州区', '13-149-1514', '3', 'Xuanzhou Qu', '安徽省宣城市宣州区', '2');
INSERT INTO `t_area` VALUES ('1515', '149', '郎溪县', '13-149-1515', '3', 'Langxi Xian', '安徽省宣城市郎溪县', '2');
INSERT INTO `t_area` VALUES ('1516', '149', '广德县', '13-149-1516', '3', 'Guangde Xian', '安徽省宣城市广德县', '2');
INSERT INTO `t_area` VALUES ('1517', '149', '泾县', '13-149-1517', '3', 'Jing Xian', '安徽省宣城市泾县', '2');
INSERT INTO `t_area` VALUES ('1518', '149', '绩溪县', '13-149-1518', '3', 'Jixi Xian', '安徽省宣城市绩溪县', '2');
INSERT INTO `t_area` VALUES ('1519', '149', '旌德县', '13-149-1519', '3', 'Jingde Xian', '安徽省宣城市旌德县', '2');
INSERT INTO `t_area` VALUES ('1520', '149', '宁国市', '13-149-1520', '3', 'Ningguo Shi', '安徽省宣城市宁国市', '2');
INSERT INTO `t_area` VALUES ('1522', '150', '鼓楼区', '14-150-1522', '3', 'Gulou Qu', '福建省福州市鼓楼区', 'GLR');
INSERT INTO `t_area` VALUES ('1523', '150', '台江区', '14-150-1523', '3', 'Taijiang Qu', '福建省福州市台江区', 'TJQ');
INSERT INTO `t_area` VALUES ('1524', '150', '仓山区', '14-150-1524', '3', 'Cangshan Qu', '福建省福州市仓山区', 'CSQ');
INSERT INTO `t_area` VALUES ('1525', '150', '马尾区', '14-150-1525', '3', 'Mawei Qu', '福建省福州市马尾区', 'MWQ');
INSERT INTO `t_area` VALUES ('1526', '150', '晋安区', '14-150-1526', '3', 'Jin,an Qu', '福建省福州市晋安区', 'JAF');
INSERT INTO `t_area` VALUES ('1527', '150', '闽侯县', '14-150-1527', '3', 'Minhou Qu', '福建省福州市闽侯县', 'MHO');
INSERT INTO `t_area` VALUES ('1528', '150', '连江县', '14-150-1528', '3', 'Lianjiang Xian', '福建省福州市连江县', 'LJF');
INSERT INTO `t_area` VALUES ('1529', '150', '罗源县', '14-150-1529', '3', 'Luoyuan Xian', '福建省福州市罗源县', 'LOY');
INSERT INTO `t_area` VALUES ('1530', '150', '闽清县', '14-150-1530', '3', 'Minqing Xian', '福建省福州市闽清县', 'MQG');
INSERT INTO `t_area` VALUES ('1531', '150', '永泰县', '14-150-1531', '3', 'Yongtai Xian', '福建省福州市永泰县', 'YTX');
INSERT INTO `t_area` VALUES ('1532', '150', '平潭县', '14-150-1532', '3', 'Pingtan Xian', '福建省福州市平潭县', 'PTN');
INSERT INTO `t_area` VALUES ('1533', '150', '福清市', '14-150-1533', '3', 'Fuqing Shi', '福建省福州市福清市', 'FQS');
INSERT INTO `t_area` VALUES ('1534', '150', '长乐市', '14-150-1534', '3', 'Changle Shi', '福建省福州市长乐市', 'CLS');
INSERT INTO `t_area` VALUES ('1536', '151', '思明区', '14-151-1536', '3', 'Siming Qu', '福建省厦门市思明区', 'SMQ');
INSERT INTO `t_area` VALUES ('1537', '151', '海沧区', '14-151-1537', '3', 'Haicang Qu', '福建省厦门市海沧区', '2');
INSERT INTO `t_area` VALUES ('1538', '151', '湖里区', '14-151-1538', '3', 'Huli Qu', '福建省厦门市湖里区', 'HLQ');
INSERT INTO `t_area` VALUES ('1539', '151', '集美区', '14-151-1539', '3', 'Jimei Qu', '福建省厦门市集美区', 'JMQ');
INSERT INTO `t_area` VALUES ('1540', '151', '同安区', '14-151-1540', '3', 'Tong,an Qu', '福建省厦门市同安区', 'TAQ');
INSERT INTO `t_area` VALUES ('1541', '151', '翔安区', '14-151-1541', '3', 'Xiangan Qu', '福建省厦门市翔安区', '2');
INSERT INTO `t_area` VALUES ('1543', '152', '城厢区', '14-152-1543', '3', 'Chengxiang Qu', '福建省莆田市城厢区', 'CXP');
INSERT INTO `t_area` VALUES ('1544', '152', '涵江区', '14-152-1544', '3', 'Hanjiang Qu', '福建省莆田市涵江区', 'HJQ');
INSERT INTO `t_area` VALUES ('1545', '152', '荔城区', '14-152-1545', '3', 'Licheng Qu', '福建省莆田市荔城区', '2');
INSERT INTO `t_area` VALUES ('1546', '152', '秀屿区', '14-152-1546', '3', 'Xiuyu Qu', '福建省莆田市秀屿区', '2');
INSERT INTO `t_area` VALUES ('1547', '152', '仙游县', '14-152-1547', '3', 'Xianyou Xian', '福建省莆田市仙游县', 'XYF');
INSERT INTO `t_area` VALUES ('1549', '153', '梅列区', '14-153-1549', '3', 'Meilie Qu', '福建省三明市梅列区', 'MLQ');
INSERT INTO `t_area` VALUES ('1550', '153', '三元区', '14-153-1550', '3', 'Sanyuan Qu', '福建省三明市三元区', 'SYB');
INSERT INTO `t_area` VALUES ('1551', '153', '明溪县', '14-153-1551', '3', 'Mingxi Xian', '福建省三明市明溪县', 'MXI');
INSERT INTO `t_area` VALUES ('1552', '153', '清流县', '14-153-1552', '3', 'Qingliu Xian', '福建省三明市清流县', 'QLX');
INSERT INTO `t_area` VALUES ('1553', '153', '宁化县', '14-153-1553', '3', 'Ninghua Xian', '福建省三明市宁化县', 'NGH');
INSERT INTO `t_area` VALUES ('1554', '153', '大田县', '14-153-1554', '3', 'Datian Xian', '福建省三明市大田县', 'DTM');
INSERT INTO `t_area` VALUES ('1555', '153', '尤溪县', '14-153-1555', '3', 'Youxi Xian', '福建省三明市尤溪县', 'YXF');
INSERT INTO `t_area` VALUES ('1556', '153', '沙县', '14-153-1556', '3', 'Sha Xian', '福建省三明市沙县', 'SAX');
INSERT INTO `t_area` VALUES ('1557', '153', '将乐县', '14-153-1557', '3', 'Jiangle Xian', '福建省三明市将乐县', 'JLE');
INSERT INTO `t_area` VALUES ('1558', '153', '泰宁县', '14-153-1558', '3', 'Taining Xian', '福建省三明市泰宁县', 'TNG');
INSERT INTO `t_area` VALUES ('1559', '153', '建宁县', '14-153-1559', '3', 'Jianning Xian', '福建省三明市建宁县', 'JNF');
INSERT INTO `t_area` VALUES ('1560', '153', '永安市', '14-153-1560', '3', 'Yong,an Shi', '福建省三明市永安市', 'YAF');
INSERT INTO `t_area` VALUES ('1562', '154', '鲤城区', '14-154-1562', '3', 'Licheng Qu', '福建省泉州市鲤城区', 'LCQ');
INSERT INTO `t_area` VALUES ('1563', '154', '丰泽区', '14-154-1563', '3', 'Fengze Qu', '福建省泉州市丰泽区', 'FZE');
INSERT INTO `t_area` VALUES ('1564', '154', '洛江区', '14-154-1564', '3', 'Luojiang Qu', '福建省泉州市洛江区', 'LJQ');
INSERT INTO `t_area` VALUES ('1565', '154', '泉港区', '14-154-1565', '3', 'Quangang Qu', '福建省泉州市泉港区', '2');
INSERT INTO `t_area` VALUES ('1566', '154', '惠安县', '14-154-1566', '3', 'Hui,an Xian', '福建省泉州市惠安县', 'HAF');
INSERT INTO `t_area` VALUES ('1567', '154', '安溪县', '14-154-1567', '3', 'Anxi Xian', '福建省泉州市安溪县', 'ANX');
INSERT INTO `t_area` VALUES ('1568', '154', '永春县', '14-154-1568', '3', 'Yongchun Xian', '福建省泉州市永春县', 'YCM');
INSERT INTO `t_area` VALUES ('1569', '154', '德化县', '14-154-1569', '3', 'Dehua Xian', '福建省泉州市德化县', 'DHA');
INSERT INTO `t_area` VALUES ('1570', '154', '金门县', '14-154-1570', '3', 'Jinmen Xian', '福建省泉州市金门县', 'JME');
INSERT INTO `t_area` VALUES ('1571', '154', '石狮市', '14-154-1571', '3', 'Shishi Shi', '福建省泉州市石狮市', 'SHH');
INSERT INTO `t_area` VALUES ('1572', '154', '晋江市', '14-154-1572', '3', 'Jinjiang Shi', '福建省泉州市晋江市', 'JJG');
INSERT INTO `t_area` VALUES ('1573', '154', '南安市', '14-154-1573', '3', 'Nan,an Shi', '福建省泉州市南安市', 'NAS');
INSERT INTO `t_area` VALUES ('1575', '155', '芗城区', '14-155-1575', '3', 'Xiangcheng Qu', '福建省漳州市芗城区', 'XZZ');
INSERT INTO `t_area` VALUES ('1576', '155', '龙文区', '14-155-1576', '3', 'Longwen Qu', '福建省漳州市龙文区', 'LWZ');
INSERT INTO `t_area` VALUES ('1577', '155', '云霄县', '14-155-1577', '3', 'Yunxiao Xian', '福建省漳州市云霄县', 'YXO');
INSERT INTO `t_area` VALUES ('1578', '155', '漳浦县', '14-155-1578', '3', 'Zhangpu Xian', '福建省漳州市漳浦县', 'ZPU');
INSERT INTO `t_area` VALUES ('1579', '155', '诏安县', '14-155-1579', '3', 'Zhao,an Xian', '福建省漳州市诏安县', 'ZAF');
INSERT INTO `t_area` VALUES ('1580', '155', '长泰县', '14-155-1580', '3', 'Changtai Xian', '福建省漳州市长泰县', 'CTA');
INSERT INTO `t_area` VALUES ('1581', '155', '东山县', '14-155-1581', '3', 'Dongshan Xian', '福建省漳州市东山县', 'DSN');
INSERT INTO `t_area` VALUES ('1582', '155', '南靖县', '14-155-1582', '3', 'Nanjing Xian', '福建省漳州市南靖县', 'NJX');
INSERT INTO `t_area` VALUES ('1583', '155', '平和县', '14-155-1583', '3', 'Pinghe Xian', '福建省漳州市平和县', 'PHE');
INSERT INTO `t_area` VALUES ('1584', '155', '华安县', '14-155-1584', '3', 'Hua,an Xian', '福建省漳州市华安县', 'HAN');
INSERT INTO `t_area` VALUES ('1585', '155', '龙海市', '14-155-1585', '3', 'Longhai Shi', '福建省漳州市龙海市', 'LHM');
INSERT INTO `t_area` VALUES ('1587', '156', '延平区', '14-156-1587', '3', 'Yanping Qu', '福建省南平市延平区', 'YPQ');
INSERT INTO `t_area` VALUES ('1588', '156', '顺昌县', '14-156-1588', '3', 'Shunchang Xian', '福建省南平市顺昌县', 'SCG');
INSERT INTO `t_area` VALUES ('1589', '156', '浦城县', '14-156-1589', '3', 'Pucheng Xian', '福建省南平市浦城县', 'PCX');
INSERT INTO `t_area` VALUES ('1590', '156', '光泽县', '14-156-1590', '3', 'Guangze Xian', '福建省南平市光泽县', 'GZE');
INSERT INTO `t_area` VALUES ('1591', '156', '松溪县', '14-156-1591', '3', 'Songxi Xian', '福建省南平市松溪县', 'SOX');
INSERT INTO `t_area` VALUES ('1592', '156', '政和县', '14-156-1592', '3', 'Zhenghe Xian', '福建省南平市政和县', 'ZGH');
INSERT INTO `t_area` VALUES ('1593', '156', '邵武市', '14-156-1593', '3', 'Shaowu Shi', '福建省南平市邵武市', 'SWU');
INSERT INTO `t_area` VALUES ('1594', '156', '武夷山市', '14-156-1594', '3', 'Wuyishan Shi', '福建省南平市武夷山市', 'WUS');
INSERT INTO `t_area` VALUES ('1595', '156', '建瓯市', '14-156-1595', '3', 'Jian,ou Shi', '福建省南平市建瓯市', 'JOU');
INSERT INTO `t_area` VALUES ('1596', '156', '建阳市', '14-156-1596', '3', 'Jianyang Shi', '福建省南平市建阳市', 'JNY');
INSERT INTO `t_area` VALUES ('1598', '157', '新罗区', '14-157-1598', '3', 'Xinluo Qu', '福建省龙岩市新罗区', 'XNL');
INSERT INTO `t_area` VALUES ('1599', '157', '长汀县', '14-157-1599', '3', 'Changting Xian', '福建省龙岩市长汀县', 'CTG');
INSERT INTO `t_area` VALUES ('1600', '157', '永定县', '14-157-1600', '3', 'Yongding Xian', '福建省龙岩市永定县', 'YDI');
INSERT INTO `t_area` VALUES ('1601', '157', '上杭县', '14-157-1601', '3', 'Shanghang Xian', '福建省龙岩市上杭县', 'SHF');
INSERT INTO `t_area` VALUES ('1602', '157', '武平县', '14-157-1602', '3', 'Wuping Xian', '福建省龙岩市武平县', 'WPG');
INSERT INTO `t_area` VALUES ('1603', '157', '连城县', '14-157-1603', '3', 'Liancheng Xian', '福建省龙岩市连城县', 'LCF');
INSERT INTO `t_area` VALUES ('1604', '157', '漳平市', '14-157-1604', '3', 'Zhangping Xian', '福建省龙岩市漳平市', 'ZGP');
INSERT INTO `t_area` VALUES ('1606', '158', '蕉城区', '14-158-1606', '3', 'Jiaocheng Qu', '福建省宁德市蕉城区', '2');
INSERT INTO `t_area` VALUES ('1607', '158', '霞浦县', '14-158-1607', '3', 'Xiapu Xian', '福建省宁德市霞浦县', '2');
INSERT INTO `t_area` VALUES ('1608', '158', '古田县', '14-158-1608', '3', 'Gutian Xian', '福建省宁德市古田县', '2');
INSERT INTO `t_area` VALUES ('1609', '158', '屏南县', '14-158-1609', '3', 'Pingnan Xian', '福建省宁德市屏南县', '2');
INSERT INTO `t_area` VALUES ('1610', '158', '寿宁县', '14-158-1610', '3', 'Shouning Xian', '福建省宁德市寿宁县', '2');
INSERT INTO `t_area` VALUES ('1611', '158', '周宁县', '14-158-1611', '3', 'Zhouning Xian', '福建省宁德市周宁县', '2');
INSERT INTO `t_area` VALUES ('1612', '158', '柘荣县', '14-158-1612', '3', 'Zherong Xian', '福建省宁德市柘荣县', '2');
INSERT INTO `t_area` VALUES ('1613', '158', '福安市', '14-158-1613', '3', 'Fu,an Shi', '福建省宁德市福安市', '2');
INSERT INTO `t_area` VALUES ('1614', '158', '福鼎市', '14-158-1614', '3', 'Fuding Shi', '福建省宁德市福鼎市', '2');
INSERT INTO `t_area` VALUES ('1616', '159', '东湖区', '15-159-1616', '3', 'Donghu Qu', '江西省南昌市东湖区', 'DHU');
INSERT INTO `t_area` VALUES ('1617', '159', '西湖区', '15-159-1617', '3', 'Xihu Qu ', '江西省南昌市西湖区', 'XHQ');
INSERT INTO `t_area` VALUES ('1618', '159', '青云谱区', '15-159-1618', '3', 'Qingyunpu Qu', '江西省南昌市青云谱区', 'QYP');
INSERT INTO `t_area` VALUES ('1619', '159', '湾里区', '15-159-1619', '3', 'Wanli Qu', '江西省南昌市湾里区', 'WLI');
INSERT INTO `t_area` VALUES ('1620', '159', '青山湖区', '15-159-1620', '3', 'Qingshanhu Qu', '江西省南昌市青山湖区', '2');
INSERT INTO `t_area` VALUES ('1621', '159', '南昌县', '15-159-1621', '3', 'Nanchang Xian', '江西省南昌市南昌县', 'NCA');
INSERT INTO `t_area` VALUES ('1622', '159', '新建县', '15-159-1622', '3', 'Xinjian Xian', '江西省南昌市新建县', 'XJN');
INSERT INTO `t_area` VALUES ('1623', '159', '安义县', '15-159-1623', '3', 'Anyi Xian', '江西省南昌市安义县', 'AYI');
INSERT INTO `t_area` VALUES ('1624', '159', '进贤县', '15-159-1624', '3', 'Jinxian Xian', '江西省南昌市进贤县', 'JXX');
INSERT INTO `t_area` VALUES ('1626', '160', '昌江区', '15-160-1626', '3', 'Changjiang Qu', '江西省景德镇市昌江区', 'CJG');
INSERT INTO `t_area` VALUES ('1627', '160', '珠山区', '15-160-1627', '3', 'Zhushan Qu', '江西省景德镇市珠山区', 'ZSJ');
INSERT INTO `t_area` VALUES ('1628', '160', '浮梁县', '15-160-1628', '3', 'Fuliang Xian', '江西省景德镇市浮梁县', 'FLX');
INSERT INTO `t_area` VALUES ('1629', '160', '乐平市', '15-160-1629', '3', 'Leping Shi', '江西省景德镇市乐平市', 'LEP');
INSERT INTO `t_area` VALUES ('1631', '161', '安源区', '15-161-1631', '3', 'Anyuan Qu', '江西省萍乡市安源区', 'AYQ');
INSERT INTO `t_area` VALUES ('1632', '161', '湘东区', '15-161-1632', '3', 'Xiangdong Qu', '江西省萍乡市湘东区', 'XDG');
INSERT INTO `t_area` VALUES ('1633', '161', '莲花县', '15-161-1633', '3', 'Lianhua Xian', '江西省萍乡市莲花县', 'LHG');
INSERT INTO `t_area` VALUES ('1634', '161', '上栗县', '15-161-1634', '3', 'Shangli Xian', '江西省萍乡市上栗县', 'SLI');
INSERT INTO `t_area` VALUES ('1635', '161', '芦溪县', '15-161-1635', '3', 'Lixi Xian', '江西省萍乡市芦溪县', 'LXP');
INSERT INTO `t_area` VALUES ('1637', '162', '庐山区', '15-162-1637', '3', 'Lushan Qu', '江西省九江市庐山区', 'LSV');
INSERT INTO `t_area` VALUES ('1638', '162', '浔阳区', '15-162-1638', '3', 'Xunyang Qu', '江西省九江市浔阳区', 'XYC');
INSERT INTO `t_area` VALUES ('1639', '162', '九江县', '15-162-1639', '3', 'Jiujiang Xian', '江西省九江市九江县', 'JUJ');
INSERT INTO `t_area` VALUES ('1640', '162', '武宁县', '15-162-1640', '3', 'Wuning Xian', '江西省九江市武宁县', 'WUN');
INSERT INTO `t_area` VALUES ('1641', '162', '修水县', '15-162-1641', '3', 'Xiushui Xian', '江西省九江市修水县', 'XSG');
INSERT INTO `t_area` VALUES ('1642', '162', '永修县', '15-162-1642', '3', 'Yongxiu Xian', '江西省九江市永修县', 'YOX');
INSERT INTO `t_area` VALUES ('1643', '162', '德安县', '15-162-1643', '3', 'De,an Xian', '江西省九江市德安县', 'DEA');
INSERT INTO `t_area` VALUES ('1644', '162', '星子县', '15-162-1644', '3', 'Xingzi Xian', '江西省九江市星子县', 'XZI');
INSERT INTO `t_area` VALUES ('1645', '162', '都昌县', '15-162-1645', '3', 'Duchang Xian', '江西省九江市都昌县', 'DUC');
INSERT INTO `t_area` VALUES ('1646', '162', '湖口县', '15-162-1646', '3', 'Hukou Xian', '江西省九江市湖口县', 'HUK');
INSERT INTO `t_area` VALUES ('1647', '162', '彭泽县', '15-162-1647', '3', 'Pengze Xian', '江西省九江市彭泽县', 'PZE');
INSERT INTO `t_area` VALUES ('1648', '162', '瑞昌市', '15-162-1648', '3', 'Ruichang Shi', '江西省九江市瑞昌市', 'RCG');
INSERT INTO `t_area` VALUES ('1650', '163', '渝水区', '15-163-1650', '3', 'Yushui Qu', '江西省新余市渝水区', 'YSR');
INSERT INTO `t_area` VALUES ('1651', '163', '分宜县', '15-163-1651', '3', 'Fenyi Xian', '江西省新余市分宜县', 'FYI');
INSERT INTO `t_area` VALUES ('1653', '164', '月湖区', '15-164-1653', '3', 'Yuehu Qu', '江西省鹰潭市月湖区', 'YHY');
INSERT INTO `t_area` VALUES ('1654', '164', '余江县', '15-164-1654', '3', 'Yujiang Xian', '江西省鹰潭市余江县', 'YUJ');
INSERT INTO `t_area` VALUES ('1655', '164', '贵溪市', '15-164-1655', '3', 'Guixi Shi', '江西省鹰潭市贵溪市', 'GXS');
INSERT INTO `t_area` VALUES ('1657', '165', '章贡区', '15-165-1657', '3', 'Zhanggong Qu', '江西省赣州市章贡区', 'ZGG');
INSERT INTO `t_area` VALUES ('1658', '165', '赣县', '15-165-1658', '3', 'Gan Xian', '江西省赣州市赣县', 'GXN');
INSERT INTO `t_area` VALUES ('1659', '165', '信丰县', '15-165-1659', '3', 'Xinfeng Xian ', '江西省赣州市信丰县', 'XNF');
INSERT INTO `t_area` VALUES ('1660', '165', '大余县', '15-165-1660', '3', 'Dayu Xian', '江西省赣州市大余县', 'DYX');
INSERT INTO `t_area` VALUES ('1661', '165', '上犹县', '15-165-1661', '3', 'Shangyou Xian', '江西省赣州市上犹县', 'SYO');
INSERT INTO `t_area` VALUES ('1662', '165', '崇义县', '15-165-1662', '3', 'Chongyi Xian', '江西省赣州市崇义县', 'CYX');
INSERT INTO `t_area` VALUES ('1663', '165', '安远县', '15-165-1663', '3', 'Anyuan Xian', '江西省赣州市安远县', 'AYN');
INSERT INTO `t_area` VALUES ('1664', '165', '龙南县', '15-165-1664', '3', 'Longnan Xian', '江西省赣州市龙南县', 'LNX');
INSERT INTO `t_area` VALUES ('1665', '165', '定南县', '15-165-1665', '3', 'Dingnan Xian', '江西省赣州市定南县', 'DNN');
INSERT INTO `t_area` VALUES ('1666', '165', '全南县', '15-165-1666', '3', 'Quannan Xian', '江西省赣州市全南县', 'QNN');
INSERT INTO `t_area` VALUES ('1667', '165', '宁都县', '15-165-1667', '3', 'Ningdu Xian', '江西省赣州市宁都县', 'NDU');
INSERT INTO `t_area` VALUES ('1668', '165', '于都县', '15-165-1668', '3', 'Yudu Xian', '江西省赣州市于都县', 'YUD');
INSERT INTO `t_area` VALUES ('1669', '165', '兴国县', '15-165-1669', '3', 'Xingguo Xian', '江西省赣州市兴国县', 'XGG');
INSERT INTO `t_area` VALUES ('1670', '165', '会昌县', '15-165-1670', '3', 'Huichang Xian', '江西省赣州市会昌县', 'HIC');
INSERT INTO `t_area` VALUES ('1671', '165', '寻乌县', '15-165-1671', '3', 'Xunwu Xian', '江西省赣州市寻乌县', 'XNW');
INSERT INTO `t_area` VALUES ('1672', '165', '石城县', '15-165-1672', '3', 'Shicheng Xian', '江西省赣州市石城县', 'SIC');
INSERT INTO `t_area` VALUES ('1673', '165', '瑞金市', '15-165-1673', '3', 'Ruijin Shi', '江西省赣州市瑞金市', 'RJS');
INSERT INTO `t_area` VALUES ('1674', '165', '南康市', '15-165-1674', '3', 'Nankang Shi', '江西省赣州市南康市', 'NNK');
INSERT INTO `t_area` VALUES ('1676', '166', '吉州区', '15-166-1676', '3', 'Jizhou Qu', '江西省吉安市吉州区', '2');
INSERT INTO `t_area` VALUES ('1677', '166', '青原区', '15-166-1677', '3', 'Qingyuan Qu', '江西省吉安市青原区', '2');
INSERT INTO `t_area` VALUES ('1678', '166', '吉安县', '15-166-1678', '3', 'Ji,an Xian', '江西省吉安市吉安县', '2');
INSERT INTO `t_area` VALUES ('1679', '166', '吉水县', '15-166-1679', '3', 'Jishui Xian', '江西省吉安市吉水县', '2');
INSERT INTO `t_area` VALUES ('1680', '166', '峡江县', '15-166-1680', '3', 'Xiajiang Xian', '江西省吉安市峡江县', '2');
INSERT INTO `t_area` VALUES ('1681', '166', '新干县', '15-166-1681', '3', 'Xingan Xian', '江西省吉安市新干县', '2');
INSERT INTO `t_area` VALUES ('1682', '166', '永丰县', '15-166-1682', '3', 'Yongfeng Xian', '江西省吉安市永丰县', '2');
INSERT INTO `t_area` VALUES ('1683', '166', '泰和县', '15-166-1683', '3', 'Taihe Xian', '江西省吉安市泰和县', '2');
INSERT INTO `t_area` VALUES ('1684', '166', '遂川县', '15-166-1684', '3', 'Suichuan Xian', '江西省吉安市遂川县', '2');
INSERT INTO `t_area` VALUES ('1685', '166', '万安县', '15-166-1685', '3', 'Wan,an Xian', '江西省吉安市万安县', '2');
INSERT INTO `t_area` VALUES ('1686', '166', '安福县', '15-166-1686', '3', 'Anfu Xian', '江西省吉安市安福县', '2');
INSERT INTO `t_area` VALUES ('1687', '166', '永新县', '15-166-1687', '3', 'Yongxin Xian ', '江西省吉安市永新县', '2');
INSERT INTO `t_area` VALUES ('1688', '166', '井冈山市', '15-166-1688', '3', 'Jinggangshan Shi', '江西省吉安市井冈山市', '2');
INSERT INTO `t_area` VALUES ('1690', '167', '袁州区', '15-167-1690', '3', 'Yuanzhou Qu', '江西省宜春市袁州区', '2');
INSERT INTO `t_area` VALUES ('1691', '167', '奉新县', '15-167-1691', '3', 'Fengxin Xian', '江西省宜春市奉新县', '2');
INSERT INTO `t_area` VALUES ('1692', '167', '万载县', '15-167-1692', '3', 'Wanzai Xian', '江西省宜春市万载县', '2');
INSERT INTO `t_area` VALUES ('1693', '167', '上高县', '15-167-1693', '3', 'Shanggao Xian', '江西省宜春市上高县', '2');
INSERT INTO `t_area` VALUES ('1694', '167', '宜丰县', '15-167-1694', '3', 'Yifeng Xian', '江西省宜春市宜丰县', '2');
INSERT INTO `t_area` VALUES ('1695', '167', '靖安县', '15-167-1695', '3', 'Jing,an Xian', '江西省宜春市靖安县', '2');
INSERT INTO `t_area` VALUES ('1696', '167', '铜鼓县', '15-167-1696', '3', 'Tonggu Xian', '江西省宜春市铜鼓县', '2');
INSERT INTO `t_area` VALUES ('1697', '167', '丰城市', '15-167-1697', '3', 'Fengcheng Shi', '江西省宜春市丰城市', '2');
INSERT INTO `t_area` VALUES ('1698', '167', '樟树市', '15-167-1698', '3', 'Zhangshu Shi', '江西省宜春市樟树市', '2');
INSERT INTO `t_area` VALUES ('1699', '167', '高安市', '15-167-1699', '3', 'Gao,an Shi', '江西省宜春市高安市', '2');
INSERT INTO `t_area` VALUES ('1701', '168', '临川区', '15-168-1701', '3', 'Linchuan Qu', '江西省抚州市临川区', '2');
INSERT INTO `t_area` VALUES ('1702', '168', '南城县', '15-168-1702', '3', 'Nancheng Xian', '江西省抚州市南城县', '2');
INSERT INTO `t_area` VALUES ('1703', '168', '黎川县', '15-168-1703', '3', 'Lichuan Xian', '江西省抚州市黎川县', '2');
INSERT INTO `t_area` VALUES ('1704', '168', '南丰县', '15-168-1704', '3', 'Nanfeng Xian', '江西省抚州市南丰县', '2');
INSERT INTO `t_area` VALUES ('1705', '168', '崇仁县', '15-168-1705', '3', 'Chongren Xian', '江西省抚州市崇仁县', '2');
INSERT INTO `t_area` VALUES ('1706', '168', '乐安县', '15-168-1706', '3', 'Le,an Xian', '江西省抚州市乐安县', '2');
INSERT INTO `t_area` VALUES ('1707', '168', '宜黄县', '15-168-1707', '3', 'Yihuang Xian', '江西省抚州市宜黄县', '2');
INSERT INTO `t_area` VALUES ('1708', '168', '金溪县', '15-168-1708', '3', 'Jinxi Xian', '江西省抚州市金溪县', '2');
INSERT INTO `t_area` VALUES ('1709', '168', '资溪县', '15-168-1709', '3', 'Zixi Xian', '江西省抚州市资溪县', '2');
INSERT INTO `t_area` VALUES ('1710', '168', '东乡县', '15-168-1710', '3', 'Dongxiang Xian', '江西省抚州市东乡县', '2');
INSERT INTO `t_area` VALUES ('1711', '168', '广昌县', '15-168-1711', '3', 'Guangchang Xian', '江西省抚州市广昌县', '2');
INSERT INTO `t_area` VALUES ('1713', '169', '信州区', '15-169-1713', '3', 'Xinzhou Qu', '江西省上饶市信州区', 'XZQ');
INSERT INTO `t_area` VALUES ('1714', '169', '上饶县', '15-169-1714', '3', 'Shangrao Xian ', '江西省上饶市上饶县', '2');
INSERT INTO `t_area` VALUES ('1715', '169', '广丰县', '15-169-1715', '3', 'Guangfeng Xian', '江西省上饶市广丰县', '2');
INSERT INTO `t_area` VALUES ('1716', '169', '玉山县', '15-169-1716', '3', 'Yushan Xian', '江西省上饶市玉山县', '2');
INSERT INTO `t_area` VALUES ('1717', '169', '铅山县', '15-169-1717', '3', 'Qianshan Xian', '江西省上饶市铅山县', '2');
INSERT INTO `t_area` VALUES ('1718', '169', '横峰县', '15-169-1718', '3', 'Hengfeng Xian', '江西省上饶市横峰县', '2');
INSERT INTO `t_area` VALUES ('1719', '169', '弋阳县', '15-169-1719', '3', 'Yiyang Xian', '江西省上饶市弋阳县', '2');
INSERT INTO `t_area` VALUES ('1720', '169', '余干县', '15-169-1720', '3', 'Yugan Xian', '江西省上饶市余干县', '2');
INSERT INTO `t_area` VALUES ('1721', '169', '鄱阳县', '15-169-1721', '3', 'Poyang Xian', '江西省上饶市鄱阳县', 'PYX');
INSERT INTO `t_area` VALUES ('1722', '169', '万年县', '15-169-1722', '3', 'Wannian Xian', '江西省上饶市万年县', '2');
INSERT INTO `t_area` VALUES ('1723', '169', '婺源县', '15-169-1723', '3', 'Wuyuan Xian', '江西省上饶市婺源县', '2');
INSERT INTO `t_area` VALUES ('1724', '169', '德兴市', '15-169-1724', '3', 'Dexing Shi', '江西省上饶市德兴市', '2');
INSERT INTO `t_area` VALUES ('1726', '170', '历下区', '16-170-1726', '3', 'Lixia Qu', '山东省济南市历下区', 'LXQ');
INSERT INTO `t_area` VALUES ('1727', '170', '市中区', '16-170-1727', '3', 'Shizhong Qu', '山东省济南市市中区', 'SZQ');
INSERT INTO `t_area` VALUES ('1728', '170', '槐荫区', '16-170-1728', '3', 'Huaiyin Qu', '山东省济南市槐荫区', 'HYF');
INSERT INTO `t_area` VALUES ('1729', '170', '天桥区', '16-170-1729', '3', 'Tianqiao Qu', '山东省济南市天桥区', 'TQQ');
INSERT INTO `t_area` VALUES ('1730', '170', '历城区', '16-170-1730', '3', 'Licheng Qu', '山东省济南市历城区', 'LCZ');
INSERT INTO `t_area` VALUES ('1731', '170', '长清区', '16-170-1731', '3', 'Changqing Qu', '山东省济南市长清区', '2');
INSERT INTO `t_area` VALUES ('1732', '170', '平阴县', '16-170-1732', '3', 'Pingyin Xian', '山东省济南市平阴县', 'PYL');
INSERT INTO `t_area` VALUES ('1733', '170', '济阳县', '16-170-1733', '3', 'Jiyang Xian', '山东省济南市济阳县', 'JYL');
INSERT INTO `t_area` VALUES ('1734', '170', '商河县', '16-170-1734', '3', 'Shanghe Xian', '山东省济南市商河县', 'SGH');
INSERT INTO `t_area` VALUES ('1735', '170', '章丘市', '16-170-1735', '3', 'Zhangqiu Shi', '山东省济南市章丘市', 'ZQS');
INSERT INTO `t_area` VALUES ('1737', '171', '市南区', '16-171-1737', '3', 'Shinan Qu', '山东省青岛市市南区', 'SNQ');
INSERT INTO `t_area` VALUES ('1738', '171', '市北区', '16-171-1738', '3', 'Shibei Qu', '山东省青岛市市北区', 'SBQ');
INSERT INTO `t_area` VALUES ('1739', '171', '四方区', '16-171-1739', '3', 'Sifang Qu', '山东省青岛市四方区', 'SFQ');
INSERT INTO `t_area` VALUES ('1740', '171', '黄岛区', '16-171-1740', '3', 'Huangdao Qu', '山东省青岛市黄岛区', 'HDO');
INSERT INTO `t_area` VALUES ('1741', '171', '崂山区', '16-171-1741', '3', 'Laoshan Qu', '山东省青岛市崂山区', 'LQD');
INSERT INTO `t_area` VALUES ('1742', '171', '李沧区', '16-171-1742', '3', 'Licang Qu', '山东省青岛市李沧区', 'LCT');
INSERT INTO `t_area` VALUES ('1743', '171', '城阳区', '16-171-1743', '3', 'Chengyang Qu', '山东省青岛市城阳区', 'CEY');
INSERT INTO `t_area` VALUES ('1744', '171', '胶州市', '16-171-1744', '3', 'Jiaozhou Shi', '山东省青岛市胶州市', 'JZS');
INSERT INTO `t_area` VALUES ('1745', '171', '即墨市', '16-171-1745', '3', 'Jimo Shi', '山东省青岛市即墨市', 'JMO');
INSERT INTO `t_area` VALUES ('1746', '171', '平度市', '16-171-1746', '3', 'Pingdu Shi', '山东省青岛市平度市', 'PDU');
INSERT INTO `t_area` VALUES ('1747', '171', '胶南市', '16-171-1747', '3', 'Jiaonan Shi', '山东省青岛市胶南市', 'JNS');
INSERT INTO `t_area` VALUES ('1748', '171', '莱西市', '16-171-1748', '3', 'Laixi Shi', '山东省青岛市莱西市', 'LXE');
INSERT INTO `t_area` VALUES ('1750', '172', '淄川区', '16-172-1750', '3', 'Zichuan Qu', '山东省淄博市淄川区', 'ZCQ');
INSERT INTO `t_area` VALUES ('1751', '172', '张店区', '16-172-1751', '3', 'Zhangdian Qu', '山东省淄博市张店区', 'ZDQ');
INSERT INTO `t_area` VALUES ('1752', '172', '博山区', '16-172-1752', '3', 'Boshan Qu', '山东省淄博市博山区', 'BSZ');
INSERT INTO `t_area` VALUES ('1753', '172', '临淄区', '16-172-1753', '3', 'Linzi Qu', '山东省淄博市临淄区', 'LZQ');
INSERT INTO `t_area` VALUES ('1754', '172', '周村区', '16-172-1754', '3', 'Zhoucun Qu', '山东省淄博市周村区', 'ZCN');
INSERT INTO `t_area` VALUES ('1755', '172', '桓台县', '16-172-1755', '3', 'Huantai Xian', '山东省淄博市桓台县', 'HTL');
INSERT INTO `t_area` VALUES ('1756', '172', '高青县', '16-172-1756', '3', 'Gaoqing Xian', '山东省淄博市高青县', 'GQG');
INSERT INTO `t_area` VALUES ('1757', '172', '沂源县', '16-172-1757', '3', 'Yiyuan Xian', '山东省淄博市沂源县', 'YIY');
INSERT INTO `t_area` VALUES ('1759', '173', '市中区', '16-173-1759', '3', 'Shizhong Qu', '山东省枣庄市市中区', 'SZZ');
INSERT INTO `t_area` VALUES ('1760', '173', '薛城区', '16-173-1760', '3', 'Xuecheng Qu', '山东省枣庄市薛城区', 'XEC');
INSERT INTO `t_area` VALUES ('1761', '173', '峄城区', '16-173-1761', '3', 'Yicheng Qu', '山东省枣庄市峄城区', 'YZZ');
INSERT INTO `t_area` VALUES ('1762', '173', '台儿庄区', '16-173-1762', '3', 'Tai,erzhuang Qu', '山东省枣庄市台儿庄区', 'TEZ');
INSERT INTO `t_area` VALUES ('1763', '173', '山亭区', '16-173-1763', '3', 'Shanting Qu', '山东省枣庄市山亭区', 'STG');
INSERT INTO `t_area` VALUES ('1764', '173', '滕州市', '16-173-1764', '3', 'Tengzhou Shi', '山东省枣庄市滕州市', 'TZO');
INSERT INTO `t_area` VALUES ('1766', '174', '东营区', '16-174-1766', '3', 'Dongying Qu', '山东省东营市东营区', 'DYQ');
INSERT INTO `t_area` VALUES ('1767', '174', '河口区', '16-174-1767', '3', 'Hekou Qu', '山东省东营市河口区', 'HKO');
INSERT INTO `t_area` VALUES ('1768', '174', '垦利县', '16-174-1768', '3', 'Kenli Xian', '山东省东营市垦利县', 'KLI');
INSERT INTO `t_area` VALUES ('1769', '174', '利津县', '16-174-1769', '3', 'Lijin Xian', '山东省东营市利津县', 'LJN');
INSERT INTO `t_area` VALUES ('1770', '174', '广饶县', '16-174-1770', '3', 'Guangrao Xian ', '山东省东营市广饶县', 'GRO');
INSERT INTO `t_area` VALUES ('1772', '175', '芝罘区', '16-175-1772', '3', 'Zhifu Qu', '山东省烟台市芝罘区', 'ZFQ');
INSERT INTO `t_area` VALUES ('1773', '175', '福山区', '16-175-1773', '3', 'Fushan Qu', '山东省烟台市福山区', 'FUS');
INSERT INTO `t_area` VALUES ('1774', '175', '牟平区', '16-175-1774', '3', 'Muping Qu', '山东省烟台市牟平区', 'MPQ');
INSERT INTO `t_area` VALUES ('1775', '175', '莱山区', '16-175-1775', '3', 'Laishan Qu', '山东省烟台市莱山区', 'LYT');
INSERT INTO `t_area` VALUES ('1776', '175', '长岛县', '16-175-1776', '3', 'Changdao Xian', '山东省烟台市长岛县', 'CDO');
INSERT INTO `t_area` VALUES ('1777', '175', '龙口市', '16-175-1777', '3', 'Longkou Shi', '山东省烟台市龙口市', 'LKU');
INSERT INTO `t_area` VALUES ('1778', '175', '莱阳市', '16-175-1778', '3', 'Laiyang Shi', '山东省烟台市莱阳市', 'LYD');
INSERT INTO `t_area` VALUES ('1779', '175', '莱州市', '16-175-1779', '3', 'Laizhou Shi', '山东省烟台市莱州市', 'LZG');
INSERT INTO `t_area` VALUES ('1780', '175', '蓬莱市', '16-175-1780', '3', 'Penglai Shi', '山东省烟台市蓬莱市', 'PLI');
INSERT INTO `t_area` VALUES ('1781', '175', '招远市', '16-175-1781', '3', 'Zhaoyuan Shi', '山东省烟台市招远市', 'ZYL');
INSERT INTO `t_area` VALUES ('1782', '175', '栖霞市', '16-175-1782', '3', 'Qixia Shi', '山东省烟台市栖霞市', 'QXS');
INSERT INTO `t_area` VALUES ('1783', '175', '海阳市', '16-175-1783', '3', 'Haiyang Shi', '山东省烟台市海阳市', 'HYL');
INSERT INTO `t_area` VALUES ('1785', '176', '潍城区', '16-176-1785', '3', 'Weicheng Qu', '山东省潍坊市潍城区', 'WCG');
INSERT INTO `t_area` VALUES ('1786', '176', '寒亭区', '16-176-1786', '3', 'Hanting Qu', '山东省潍坊市寒亭区', 'HNT');
INSERT INTO `t_area` VALUES ('1787', '176', '坊子区', '16-176-1787', '3', 'Fangzi Qu', '山东省潍坊市坊子区', 'FZQ');
INSERT INTO `t_area` VALUES ('1788', '176', '奎文区', '16-176-1788', '3', 'Kuiwen Qu', '山东省潍坊市奎文区', 'KWN');
INSERT INTO `t_area` VALUES ('1789', '176', '临朐县', '16-176-1789', '3', 'Linqu Xian', '山东省潍坊市临朐县', 'LNQ');
INSERT INTO `t_area` VALUES ('1790', '176', '昌乐县', '16-176-1790', '3', 'Changle Xian', '山东省潍坊市昌乐县', 'CLX');
INSERT INTO `t_area` VALUES ('1791', '176', '青州市', '16-176-1791', '3', 'Qingzhou Shi', '山东省潍坊市青州市', 'QGZ');
INSERT INTO `t_area` VALUES ('1792', '176', '诸城市', '16-176-1792', '3', 'Zhucheng Shi', '山东省潍坊市诸城市', 'ZCL');
INSERT INTO `t_area` VALUES ('1793', '176', '寿光市', '16-176-1793', '3', 'Shouguang Shi', '山东省潍坊市寿光市', 'SGG');
INSERT INTO `t_area` VALUES ('1794', '176', '安丘市', '16-176-1794', '3', 'Anqiu Shi', '山东省潍坊市安丘市', 'AQU');
INSERT INTO `t_area` VALUES ('1795', '176', '高密市', '16-176-1795', '3', 'Gaomi Shi', '山东省潍坊市高密市', 'GMI');
INSERT INTO `t_area` VALUES ('1796', '176', '昌邑市', '16-176-1796', '3', 'Changyi Shi', '山东省潍坊市昌邑市', 'CYL');
INSERT INTO `t_area` VALUES ('1798', '177', '市中区', '16-177-1798', '3', 'Shizhong Qu', '山东省济宁市市中区', 'SZU');
INSERT INTO `t_area` VALUES ('1799', '177', '任城区', '16-177-1799', '3', 'Rencheng Qu', '山东省济宁市任城区', 'RCQ');
INSERT INTO `t_area` VALUES ('1800', '177', '微山县', '16-177-1800', '3', 'Weishan Xian', '山东省济宁市微山县', 'WSA');
INSERT INTO `t_area` VALUES ('1801', '177', '鱼台县', '16-177-1801', '3', 'Yutai Xian', '山东省济宁市鱼台县', 'YTL');
INSERT INTO `t_area` VALUES ('1802', '177', '金乡县', '16-177-1802', '3', 'Jinxiang Xian', '山东省济宁市金乡县', 'JXG');
INSERT INTO `t_area` VALUES ('1803', '177', '嘉祥县', '16-177-1803', '3', 'Jiaxiang Xian', '山东省济宁市嘉祥县', 'JXP');
INSERT INTO `t_area` VALUES ('1804', '177', '汶上县', '16-177-1804', '3', 'Wenshang Xian', '山东省济宁市汶上县', 'WNS');
INSERT INTO `t_area` VALUES ('1805', '177', '泗水县', '16-177-1805', '3', 'Sishui Xian', '山东省济宁市泗水县', 'SSH');
INSERT INTO `t_area` VALUES ('1806', '177', '梁山县', '16-177-1806', '3', 'Liangshan Xian', '山东省济宁市梁山县', 'LSN');
INSERT INTO `t_area` VALUES ('1807', '177', '曲阜市', '16-177-1807', '3', 'Qufu Shi', '山东省济宁市曲阜市', 'QFU');
INSERT INTO `t_area` VALUES ('1808', '177', '兖州市', '16-177-1808', '3', 'Yanzhou Shi', '山东省济宁市兖州市', 'YZL');
INSERT INTO `t_area` VALUES ('1809', '177', '邹城市', '16-177-1809', '3', 'Zoucheng Shi', '山东省济宁市邹城市', 'ZCG');
INSERT INTO `t_area` VALUES ('1811', '178', '泰山区', '16-178-1811', '3', 'Taishan Qu', '山东省泰安市泰山区', 'TSQ');
INSERT INTO `t_area` VALUES ('1812', '178', '岱岳区', '16-178-1812', '3', 'Daiyue Qu', '山东省泰安市岱岳区', '2');
INSERT INTO `t_area` VALUES ('1813', '178', '宁阳县', '16-178-1813', '3', 'Ningyang Xian', '山东省泰安市宁阳县', 'NGY');
INSERT INTO `t_area` VALUES ('1814', '178', '东平县', '16-178-1814', '3', 'Dongping Xian', '山东省泰安市东平县', 'DPG');
INSERT INTO `t_area` VALUES ('1815', '178', '新泰市', '16-178-1815', '3', 'Xintai Shi', '山东省泰安市新泰市', 'XTA');
INSERT INTO `t_area` VALUES ('1816', '178', '肥城市', '16-178-1816', '3', 'Feicheng Shi', '山东省泰安市肥城市', 'FEC');
INSERT INTO `t_area` VALUES ('1818', '179', '环翠区', '16-179-1818', '3', 'Huancui Qu', '山东省威海市环翠区', 'HNC');
INSERT INTO `t_area` VALUES ('1819', '179', '文登市', '16-179-1819', '3', 'Wendeng Shi', '山东省威海市文登市', 'WDS');
INSERT INTO `t_area` VALUES ('1820', '179', '荣成市', '16-179-1820', '3', 'Rongcheng Shi', '山东省威海市荣成市', '2');
INSERT INTO `t_area` VALUES ('1821', '179', '乳山市', '16-179-1821', '3', 'Rushan Shi', '山东省威海市乳山市', 'RSN');
INSERT INTO `t_area` VALUES ('1823', '180', '东港区', '16-180-1823', '3', 'Donggang Qu', '山东省日照市东港区', 'DGR');
INSERT INTO `t_area` VALUES ('1824', '180', '岚山区', '16-180-1824', '3', 'Lanshan Qu', '山东省日照市岚山区', '2');
INSERT INTO `t_area` VALUES ('1825', '180', '五莲县', '16-180-1825', '3', 'Wulian Xian', '山东省日照市五莲县', 'WLN');
INSERT INTO `t_area` VALUES ('1826', '180', '莒县', '16-180-1826', '3', 'Ju Xian', '山东省日照市莒县', 'JUX');
INSERT INTO `t_area` VALUES ('1828', '181', '莱城区', '16-181-1828', '3', 'Laicheng Qu', '山东省莱芜市莱城区', 'LAC');
INSERT INTO `t_area` VALUES ('1829', '181', '钢城区', '16-181-1829', '3', 'Gangcheng Qu', '山东省莱芜市钢城区', 'GCQ');
INSERT INTO `t_area` VALUES ('1831', '182', '兰山区', '16-182-1831', '3', 'Lanshan Qu', '山东省临沂市兰山区', 'LLS');
INSERT INTO `t_area` VALUES ('1832', '182', '罗庄区', '16-182-1832', '3', 'Luozhuang Qu', '山东省临沂市罗庄区', 'LZU');
INSERT INTO `t_area` VALUES ('1833', '182', '河东区', '16-182-1833', '3', 'Hedong Qu', '山东省临沂市河东区', '2');
INSERT INTO `t_area` VALUES ('1834', '182', '沂南县', '16-182-1834', '3', 'Yinan Xian', '山东省临沂市沂南县', 'YNN');
INSERT INTO `t_area` VALUES ('1835', '182', '郯城县', '16-182-1835', '3', 'Tancheng Xian', '山东省临沂市郯城县', 'TCE');
INSERT INTO `t_area` VALUES ('1836', '182', '沂水县', '16-182-1836', '3', 'Yishui Xian', '山东省临沂市沂水县', 'YIS');
INSERT INTO `t_area` VALUES ('1837', '182', '苍山县', '16-182-1837', '3', 'Cangshan Xian', '山东省临沂市苍山县', 'CSH');
INSERT INTO `t_area` VALUES ('1838', '182', '费县', '16-182-1838', '3', 'Fei Xian', '山东省临沂市费县', 'FEI');
INSERT INTO `t_area` VALUES ('1839', '182', '平邑县', '16-182-1839', '3', 'Pingyi Xian', '山东省临沂市平邑县', 'PYI');
INSERT INTO `t_area` VALUES ('1840', '182', '莒南县', '16-182-1840', '3', 'Junan Xian', '山东省临沂市莒南县', 'JNB');
INSERT INTO `t_area` VALUES ('1841', '182', '蒙阴县', '16-182-1841', '3', 'Mengyin Xian', '山东省临沂市蒙阴县', 'MYL');
INSERT INTO `t_area` VALUES ('1842', '182', '临沭县', '16-182-1842', '3', 'Linshu Xian', '山东省临沂市临沭县', 'LSP');
INSERT INTO `t_area` VALUES ('1844', '183', '德城区', '16-183-1844', '3', 'Decheng Qu', '山东省德州市德城区', 'DCD');
INSERT INTO `t_area` VALUES ('1845', '183', '陵县', '16-183-1845', '3', 'Ling Xian', '山东省德州市陵县', 'LXL');
INSERT INTO `t_area` VALUES ('1846', '183', '宁津县', '16-183-1846', '3', 'Ningjin Xian', '山东省德州市宁津县', 'NGJ');
INSERT INTO `t_area` VALUES ('1847', '183', '庆云县', '16-183-1847', '3', 'Qingyun Xian', '山东省德州市庆云县', 'QYL');
INSERT INTO `t_area` VALUES ('1848', '183', '临邑县', '16-183-1848', '3', 'Linyi xian', '山东省德州市临邑县', 'LYM');
INSERT INTO `t_area` VALUES ('1849', '183', '齐河县', '16-183-1849', '3', 'Qihe Xian', '山东省德州市齐河县', 'QIH');
INSERT INTO `t_area` VALUES ('1850', '183', '平原县', '16-183-1850', '3', 'Pingyuan Xian', '山东省德州市平原县', 'PYN');
INSERT INTO `t_area` VALUES ('1851', '183', '夏津县', '16-183-1851', '3', 'Xiajin Xian', '山东省德州市夏津县', 'XAJ');
INSERT INTO `t_area` VALUES ('1852', '183', '武城县', '16-183-1852', '3', 'Wucheng Xian', '山东省德州市武城县', 'WUC');
INSERT INTO `t_area` VALUES ('1853', '183', '乐陵市', '16-183-1853', '3', 'Leling Shi', '山东省德州市乐陵市', 'LEL');
INSERT INTO `t_area` VALUES ('1854', '183', '禹城市', '16-183-1854', '3', 'Yucheng Shi', '山东省德州市禹城市', 'YCL');
INSERT INTO `t_area` VALUES ('1856', '184', '东昌府区', '16-184-1856', '3', 'Dongchangfu Qu', '山东省聊城市东昌府区', 'DCF');
INSERT INTO `t_area` VALUES ('1857', '184', '阳谷县', '16-184-1857', '3', 'Yanggu Xian ', '山东省聊城市阳谷县', 'YGU');
INSERT INTO `t_area` VALUES ('1858', '184', '莘县', '16-184-1858', '3', 'Shen Xian', '山东省聊城市莘县', 'SHN');
INSERT INTO `t_area` VALUES ('1859', '184', '茌平县', '16-184-1859', '3', 'Chiping Xian ', '山东省聊城市茌平县', 'CPG');
INSERT INTO `t_area` VALUES ('1860', '184', '东阿县', '16-184-1860', '3', 'Dong,e Xian', '山东省聊城市东阿县', 'DGE');
INSERT INTO `t_area` VALUES ('1861', '184', '冠县', '16-184-1861', '3', 'Guan Xian', '山东省聊城市冠县', 'GXL');
INSERT INTO `t_area` VALUES ('1862', '184', '高唐县', '16-184-1862', '3', 'Gaotang Xian', '山东省聊城市高唐县', 'GTG');
INSERT INTO `t_area` VALUES ('1863', '184', '临清市', '16-184-1863', '3', 'Linqing Xian', '山东省聊城市临清市', 'LQS');
INSERT INTO `t_area` VALUES ('1865', '185', '滨城区', '16-185-1865', '3', 'Bincheng Qu', '山东省滨州市滨城区', '2');
INSERT INTO `t_area` VALUES ('1866', '185', '惠民县', '16-185-1866', '3', 'Huimin Xian', '山东省滨州市惠民县', '2');
INSERT INTO `t_area` VALUES ('1867', '185', '阳信县', '16-185-1867', '3', 'Yangxin Xian', '山东省滨州市阳信县', '2');
INSERT INTO `t_area` VALUES ('1868', '185', '无棣县', '16-185-1868', '3', 'Wudi Xian', '山东省滨州市无棣县', '2');
INSERT INTO `t_area` VALUES ('1869', '185', '沾化县', '16-185-1869', '3', 'Zhanhua Xian', '山东省滨州市沾化县', '2');
INSERT INTO `t_area` VALUES ('1870', '185', '博兴县', '16-185-1870', '3', 'Boxing Xian', '山东省滨州市博兴县', '2');
INSERT INTO `t_area` VALUES ('1871', '185', '邹平县', '16-185-1871', '3', 'Zouping Xian', '山东省滨州市邹平县', '2');
INSERT INTO `t_area` VALUES ('1873', '186', '牡丹区', '16-186-1873', '3', 'Mudan Qu', '山东省菏泽市牡丹区', '2');
INSERT INTO `t_area` VALUES ('1874', '186', '曹县', '16-186-1874', '3', 'Cao Xian', '山东省菏泽市曹县', '2');
INSERT INTO `t_area` VALUES ('1875', '186', '单县', '16-186-1875', '3', 'Shan Xian', '山东省菏泽市单县', '2');
INSERT INTO `t_area` VALUES ('1876', '186', '成武县', '16-186-1876', '3', 'Chengwu Xian', '山东省菏泽市成武县', '2');
INSERT INTO `t_area` VALUES ('1877', '186', '巨野县', '16-186-1877', '3', 'Juye Xian', '山东省菏泽市巨野县', '2');
INSERT INTO `t_area` VALUES ('1878', '186', '郓城县', '16-186-1878', '3', 'Yuncheng Xian', '山东省菏泽市郓城县', '2');
INSERT INTO `t_area` VALUES ('1879', '186', '鄄城县', '16-186-1879', '3', 'Juancheng Xian', '山东省菏泽市鄄城县', '2');
INSERT INTO `t_area` VALUES ('1880', '186', '定陶县', '16-186-1880', '3', 'Dingtao Xian', '山东省菏泽市定陶县', '2');
INSERT INTO `t_area` VALUES ('1881', '186', '东明县', '16-186-1881', '3', 'Dongming Xian', '山东省菏泽市东明县', '2');
INSERT INTO `t_area` VALUES ('1883', '187', '中原区', '17-187-1883', '3', 'Zhongyuan Qu', '河南省郑州市中原区', 'ZYQ');
INSERT INTO `t_area` VALUES ('1884', '187', '二七区', '17-187-1884', '3', 'Erqi Qu', '河南省郑州市二七区', 'EQQ');
INSERT INTO `t_area` VALUES ('1885', '187', '管城回族区', '17-187-1885', '3', 'Guancheng Huizu Qu', '河南省郑州市管城回族区', 'GCH');
INSERT INTO `t_area` VALUES ('1886', '187', '金水区', '17-187-1886', '3', 'Jinshui Qu', '河南省郑州市金水区', 'JSU');
INSERT INTO `t_area` VALUES ('1887', '187', '上街区', '17-187-1887', '3', 'Shangjie Qu', '河南省郑州市上街区', 'SJE');
INSERT INTO `t_area` VALUES ('1888', '187', '惠济区', '17-187-1888', '3', 'Mangshan Qu', '河南省郑州市惠济区', '2');
INSERT INTO `t_area` VALUES ('1889', '187', '中牟县', '17-187-1889', '3', 'Zhongmou Xian', '河南省郑州市中牟县', 'ZMO');
INSERT INTO `t_area` VALUES ('1890', '187', '巩义市', '17-187-1890', '3', 'Gongyi Shi', '河南省郑州市巩义市', 'GYI');
INSERT INTO `t_area` VALUES ('1891', '187', '荥阳市', '17-187-1891', '3', 'Xingyang Shi', '河南省郑州市荥阳市', 'XYK');
INSERT INTO `t_area` VALUES ('1892', '187', '新密市', '17-187-1892', '3', 'Xinmi Shi', '河南省郑州市新密市', 'XMI');
INSERT INTO `t_area` VALUES ('1893', '187', '新郑市', '17-187-1893', '3', 'Xinzheng Shi', '河南省郑州市新郑市', 'XZG');
INSERT INTO `t_area` VALUES ('1894', '187', '登封市', '17-187-1894', '3', 'Dengfeng Shi', '河南省郑州市登封市', '2');
INSERT INTO `t_area` VALUES ('1896', '188', '龙亭区', '17-188-1896', '3', 'Longting Qu', '河南省开封市龙亭区', 'LTK');
INSERT INTO `t_area` VALUES ('1897', '188', '顺河回族区', '17-188-1897', '3', 'Shunhe Huizu Qu', '河南省开封市顺河回族区', 'SHR');
INSERT INTO `t_area` VALUES ('1898', '188', '鼓楼区', '17-188-1898', '3', 'Gulou Qu', '河南省开封市鼓楼区', 'GLK');
INSERT INTO `t_area` VALUES ('1899', '188', '禹王台区', '17-188-1899', '3', 'Yuwangtai Qu', '河南省开封市禹王台区', '2');
INSERT INTO `t_area` VALUES ('1900', '188', '金明区', '17-188-1900', '3', 'Jinming Qu', '河南省开封市金明区', '2');
INSERT INTO `t_area` VALUES ('1901', '188', '杞县', '17-188-1901', '3', 'Qi Xian', '河南省开封市杞县', 'QIX');
INSERT INTO `t_area` VALUES ('1902', '188', '通许县', '17-188-1902', '3', 'Tongxu Xian', '河南省开封市通许县', 'TXY');
INSERT INTO `t_area` VALUES ('1903', '188', '尉氏县', '17-188-1903', '3', 'Weishi Xian', '河南省开封市尉氏县', 'WSI');
INSERT INTO `t_area` VALUES ('1904', '188', '开封县', '17-188-1904', '3', 'Kaifeng Xian', '河南省开封市开封县', 'KFX');
INSERT INTO `t_area` VALUES ('1905', '188', '兰考县', '17-188-1905', '3', 'Lankao Xian', '河南省开封市兰考县', 'LKA');
INSERT INTO `t_area` VALUES ('1907', '189', '老城区', '17-189-1907', '3', 'Laocheng Qu', '河南省洛阳市老城区', 'LLY');
INSERT INTO `t_area` VALUES ('1908', '189', '西工区', '17-189-1908', '3', 'Xigong Qu', '河南省洛阳市西工区', 'XGL');
INSERT INTO `t_area` VALUES ('1909', '189', '瀍河回族区', '17-189-1909', '3', 'Chanhehuizu Qu', '河南省洛阳市瀍河回族区', '2');
INSERT INTO `t_area` VALUES ('1910', '189', '涧西区', '17-189-1910', '3', 'Jianxi Qu', '河南省洛阳市涧西区', 'JXL');
INSERT INTO `t_area` VALUES ('1911', '189', '吉利区', '17-189-1911', '3', 'Jili Qu', '河南省洛阳市吉利区', 'JLL');
INSERT INTO `t_area` VALUES ('1912', '189', '洛龙区', '17-189-1912', '3', 'Luolong Qu', '河南省洛阳市洛龙区', '2');
INSERT INTO `t_area` VALUES ('1913', '189', '孟津县', '17-189-1913', '3', 'Mengjin Xian', '河南省洛阳市孟津县', 'MGJ');
INSERT INTO `t_area` VALUES ('1914', '189', '新安县', '17-189-1914', '3', 'Xin,an Xian', '河南省洛阳市新安县', 'XAX');
INSERT INTO `t_area` VALUES ('1915', '189', '栾川县', '17-189-1915', '3', 'Luanchuan Xian', '河南省洛阳市栾川县', 'LCK');
INSERT INTO `t_area` VALUES ('1916', '189', '嵩县', '17-189-1916', '3', 'Song Xian', '河南省洛阳市嵩县', 'SON');
INSERT INTO `t_area` VALUES ('1917', '189', '汝阳县', '17-189-1917', '3', 'Ruyang Xian', '河南省洛阳市汝阳县', 'RUY');
INSERT INTO `t_area` VALUES ('1918', '189', '宜阳县', '17-189-1918', '3', 'Yiyang Xian', '河南省洛阳市宜阳县', 'YYY');
INSERT INTO `t_area` VALUES ('1919', '189', '洛宁县', '17-189-1919', '3', 'Luoning Xian', '河南省洛阳市洛宁县', 'LNI');
INSERT INTO `t_area` VALUES ('1920', '189', '伊川县', '17-189-1920', '3', 'Yichuan Xian', '河南省洛阳市伊川县', 'YCZ');
INSERT INTO `t_area` VALUES ('1921', '189', '偃师市', '17-189-1921', '3', 'Yanshi Shi', '河南省洛阳市偃师市', 'YST');
INSERT INTO `t_area` VALUES ('1923', '190', '新华区', '17-190-1923', '3', 'Xinhua Qu', '河南省平顶山市新华区', 'XHP');
INSERT INTO `t_area` VALUES ('1924', '190', '卫东区', '17-190-1924', '3', 'Weidong Qu', '河南省平顶山市卫东区', 'WDG');
INSERT INTO `t_area` VALUES ('1925', '190', '石龙区', '17-190-1925', '3', 'Shilong Qu', '河南省平顶山市石龙区', 'SIL');
INSERT INTO `t_area` VALUES ('1926', '190', '湛河区', '17-190-1926', '3', 'Zhanhe Qu', '河南省平顶山市湛河区', 'ZHQ');
INSERT INTO `t_area` VALUES ('1927', '190', '宝丰县', '17-190-1927', '3', 'Baofeng Xian', '河南省平顶山市宝丰县', 'BFG');
INSERT INTO `t_area` VALUES ('1928', '190', '叶县', '17-190-1928', '3', 'Ye Xian', '河南省平顶山市叶县', 'YEX');
INSERT INTO `t_area` VALUES ('1929', '190', '鲁山县', '17-190-1929', '3', 'Lushan Xian', '河南省平顶山市鲁山县', 'LUS');
INSERT INTO `t_area` VALUES ('1930', '190', '郏县', '17-190-1930', '3', 'Jia Xian', '河南省平顶山市郏县', 'JXY');
INSERT INTO `t_area` VALUES ('1931', '190', '舞钢市', '17-190-1931', '3', 'Wugang Shi', '河南省平顶山市舞钢市', 'WGY');
INSERT INTO `t_area` VALUES ('1932', '190', '汝州市', '17-190-1932', '3', 'Ruzhou Shi', '河南省平顶山市汝州市', 'RZO');
INSERT INTO `t_area` VALUES ('1934', '191', '文峰区', '17-191-1934', '3', 'Wenfeng Qu', '河南省安阳市文峰区', 'WFQ');
INSERT INTO `t_area` VALUES ('1935', '191', '北关区', '17-191-1935', '3', 'Beiguan Qu', '河南省安阳市北关区', 'BGQ');
INSERT INTO `t_area` VALUES ('1936', '191', '殷都区', '17-191-1936', '3', 'Yindu Qu', '河南省安阳市殷都区', '2');
INSERT INTO `t_area` VALUES ('1937', '191', '龙安区', '17-191-1937', '3', 'Longan Qu', '河南省安阳市龙安区', '2');
INSERT INTO `t_area` VALUES ('1938', '191', '安阳县', '17-191-1938', '3', 'Anyang Xian', '河南省安阳市安阳县', 'AYX');
INSERT INTO `t_area` VALUES ('1939', '191', '汤阴县', '17-191-1939', '3', 'Tangyin Xian', '河南省安阳市汤阴县', 'TYI');
INSERT INTO `t_area` VALUES ('1940', '191', '滑县', '17-191-1940', '3', 'Hua Xian', '河南省安阳市滑县', 'HUA');
INSERT INTO `t_area` VALUES ('1941', '191', '内黄县', '17-191-1941', '3', 'Neihuang Xian', '河南省安阳市内黄县', 'NHG');
INSERT INTO `t_area` VALUES ('1942', '191', '林州市', '17-191-1942', '3', 'Linzhou Shi', '河南省安阳市林州市', 'LZY');
INSERT INTO `t_area` VALUES ('1944', '192', '鹤山区', '17-192-1944', '3', 'Heshan Qu', '河南省鹤壁市鹤山区', 'HSF');
INSERT INTO `t_area` VALUES ('1945', '192', '山城区', '17-192-1945', '3', 'Shancheng Qu', '河南省鹤壁市山城区', 'SCB');
INSERT INTO `t_area` VALUES ('1946', '192', '淇滨区', '17-192-1946', '3', 'Qibin Qu', '河南省鹤壁市淇滨区', '2');
INSERT INTO `t_area` VALUES ('1947', '192', '浚县', '17-192-1947', '3', 'Xun Xian', '河南省鹤壁市浚县', 'XUX');
INSERT INTO `t_area` VALUES ('1948', '192', '淇县', '17-192-1948', '3', 'Qi Xian', '河南省鹤壁市淇县', 'QXY');
INSERT INTO `t_area` VALUES ('1950', '193', '红旗区', '17-193-1950', '3', 'Hongqi Qu', '河南省新乡市红旗区', 'HQQ');
INSERT INTO `t_area` VALUES ('1951', '193', '卫滨区', '17-193-1951', '3', 'Weibin Qu', '河南省新乡市卫滨区', '2');
INSERT INTO `t_area` VALUES ('1952', '193', '凤泉区', '17-193-1952', '3', 'Fengquan Qu', '河南省新乡市凤泉区', '2');
INSERT INTO `t_area` VALUES ('1953', '193', '牧野区', '17-193-1953', '3', 'Muye Qu', '河南省新乡市牧野区', '2');
INSERT INTO `t_area` VALUES ('1954', '193', '新乡县', '17-193-1954', '3', 'Xinxiang Xian', '河南省新乡市新乡县', 'XXX');
INSERT INTO `t_area` VALUES ('1955', '193', '获嘉县', '17-193-1955', '3', 'Huojia Xian', '河南省新乡市获嘉县', 'HOJ');
INSERT INTO `t_area` VALUES ('1956', '193', '原阳县', '17-193-1956', '3', 'Yuanyang Xian', '河南省新乡市原阳县', 'YYA');
INSERT INTO `t_area` VALUES ('1957', '193', '延津县', '17-193-1957', '3', 'Yanjin Xian', '河南省新乡市延津县', 'YJN');
INSERT INTO `t_area` VALUES ('1958', '193', '封丘县', '17-193-1958', '3', 'Fengqiu Xian', '河南省新乡市封丘县', 'FQU');
INSERT INTO `t_area` VALUES ('1959', '193', '长垣县', '17-193-1959', '3', 'Changyuan Xian', '河南省新乡市长垣县', 'CYU');
INSERT INTO `t_area` VALUES ('1960', '193', '卫辉市', '17-193-1960', '3', 'Weihui Shi', '河南省新乡市卫辉市', 'WHS');
INSERT INTO `t_area` VALUES ('1961', '193', '辉县市', '17-193-1961', '3', 'Huixian Shi', '河南省新乡市辉县市', 'HXS');
INSERT INTO `t_area` VALUES ('1963', '194', '解放区', '17-194-1963', '3', 'Jiefang Qu', '河南省焦作市解放区', 'JFQ');
INSERT INTO `t_area` VALUES ('1964', '194', '中站区', '17-194-1964', '3', 'Zhongzhan Qu', '河南省焦作市中站区', 'ZZQ');
INSERT INTO `t_area` VALUES ('1965', '194', '马村区', '17-194-1965', '3', 'Macun Qu', '河南省焦作市马村区', 'MCQ');
INSERT INTO `t_area` VALUES ('1966', '194', '山阳区', '17-194-1966', '3', 'Shanyang Qu', '河南省焦作市山阳区', 'SYC');
INSERT INTO `t_area` VALUES ('1967', '194', '修武县', '17-194-1967', '3', 'Xiuwu Xian', '河南省焦作市修武县', 'XUW');
INSERT INTO `t_area` VALUES ('1968', '194', '博爱县', '17-194-1968', '3', 'Bo,ai Xian', '河南省焦作市博爱县', 'BOA');
INSERT INTO `t_area` VALUES ('1969', '194', '武陟县', '17-194-1969', '3', 'Wuzhi Xian', '河南省焦作市武陟县', 'WZI');
INSERT INTO `t_area` VALUES ('1970', '194', '温县', '17-194-1970', '3', 'Wen Xian', '河南省焦作市温县', 'WEN');
INSERT INTO `t_area` VALUES ('1971', '194', '济源市', '17-194-1971', '3', 'Jiyuan Shi', '河南省焦作市济源市', '2');
INSERT INTO `t_area` VALUES ('1972', '194', '沁阳市', '17-194-1972', '3', 'Qinyang Shi', '河南省焦作市沁阳市', 'QYS');
INSERT INTO `t_area` VALUES ('1973', '194', '孟州市', '17-194-1973', '3', 'Mengzhou Shi', '河南省焦作市孟州市', 'MZO');
INSERT INTO `t_area` VALUES ('1975', '195', '华龙区', '17-195-1975', '3', 'Hualong Qu', '河南省濮阳市华龙区', '2');
INSERT INTO `t_area` VALUES ('1976', '195', '清丰县', '17-195-1976', '3', 'Qingfeng Xian', '河南省濮阳市清丰县', 'QFG');
INSERT INTO `t_area` VALUES ('1977', '195', '南乐县', '17-195-1977', '3', 'Nanle Xian', '河南省濮阳市南乐县', 'NLE');
INSERT INTO `t_area` VALUES ('1978', '195', '范县', '17-195-1978', '3', 'Fan Xian', '河南省濮阳市范县', 'FAX');
INSERT INTO `t_area` VALUES ('1979', '195', '台前县', '17-195-1979', '3', 'Taiqian Xian', '河南省濮阳市台前县', 'TQN');
INSERT INTO `t_area` VALUES ('1980', '195', '濮阳县', '17-195-1980', '3', 'Puyang Xian', '河南省濮阳市濮阳县', 'PUY');
INSERT INTO `t_area` VALUES ('1982', '196', '魏都区', '17-196-1982', '3', 'Weidu Qu', '河南省许昌市魏都区', 'WED');
INSERT INTO `t_area` VALUES ('1983', '196', '许昌县', '17-196-1983', '3', 'Xuchang Xian', '河南省许昌市许昌县', 'XUC');
INSERT INTO `t_area` VALUES ('1984', '196', '鄢陵县', '17-196-1984', '3', 'Yanling Xian', '河南省许昌市鄢陵县', 'YLY');
INSERT INTO `t_area` VALUES ('1985', '196', '襄城县', '17-196-1985', '3', 'Xiangcheng Xian', '河南省许昌市襄城县', 'XAC');
INSERT INTO `t_area` VALUES ('1986', '196', '禹州市', '17-196-1986', '3', 'Yuzhou Shi', '河南省许昌市禹州市', 'YUZ');
INSERT INTO `t_area` VALUES ('1987', '196', '长葛市', '17-196-1987', '3', 'Changge Shi', '河南省许昌市长葛市', 'CGE');
INSERT INTO `t_area` VALUES ('1989', '197', '源汇区', '17-197-1989', '3', 'Yuanhui Qu', '河南省漯河市源汇区', 'YHI');
INSERT INTO `t_area` VALUES ('1990', '197', '郾城区', '17-197-1990', '3', 'Yancheng Qu', '河南省漯河市郾城区', '2');
INSERT INTO `t_area` VALUES ('1991', '197', '召陵区', '17-197-1991', '3', 'Zhaoling Qu', '河南省漯河市召陵区', '2');
INSERT INTO `t_area` VALUES ('1992', '197', '舞阳县', '17-197-1992', '3', 'Wuyang Xian', '河南省漯河市舞阳县', 'WYG');
INSERT INTO `t_area` VALUES ('1993', '197', '临颍县', '17-197-1993', '3', 'Linying Xian', '河南省漯河市临颍县', 'LNY');
INSERT INTO `t_area` VALUES ('1995', '198', '湖滨区', '17-198-1995', '3', 'Hubin Qu', '河南省三门峡市湖滨区', 'HBI');
INSERT INTO `t_area` VALUES ('1996', '198', '渑池县', '17-198-1996', '3', 'Mianchi Xian', '河南省三门峡市渑池县', 'MCI');
INSERT INTO `t_area` VALUES ('1997', '198', '陕县', '17-198-1997', '3', 'Shan Xian', '河南省三门峡市陕县', 'SHX');
INSERT INTO `t_area` VALUES ('1998', '198', '卢氏县', '17-198-1998', '3', 'Lushi Xian', '河南省三门峡市卢氏县', 'LUU');
INSERT INTO `t_area` VALUES ('1999', '198', '义马市', '17-198-1999', '3', 'Yima Shi', '河南省三门峡市义马市', 'YMA');
INSERT INTO `t_area` VALUES ('2000', '198', '灵宝市', '17-198-2000', '3', 'Lingbao Shi', '河南省三门峡市灵宝市', 'LBS');
INSERT INTO `t_area` VALUES ('2002', '199', '宛城区', '17-199-2002', '3', 'Wancheng Qu', '河南省南阳市宛城区', 'WCN');
INSERT INTO `t_area` VALUES ('2003', '199', '卧龙区', '17-199-2003', '3', 'Wolong Qu', '河南省南阳市卧龙区', 'WOL');
INSERT INTO `t_area` VALUES ('2004', '199', '南召县', '17-199-2004', '3', 'Nanzhao Xian', '河南省南阳市南召县', 'NZO');
INSERT INTO `t_area` VALUES ('2005', '199', '方城县', '17-199-2005', '3', 'Fangcheng Xian', '河南省南阳市方城县', 'FCX');
INSERT INTO `t_area` VALUES ('2006', '199', '西峡县', '17-199-2006', '3', 'Xixia Xian', '河南省南阳市西峡县', 'XXY');
INSERT INTO `t_area` VALUES ('2007', '199', '镇平县', '17-199-2007', '3', 'Zhenping Xian', '河南省南阳市镇平县', 'ZPX');
INSERT INTO `t_area` VALUES ('2008', '199', '内乡县', '17-199-2008', '3', 'Neixiang Xian', '河南省南阳市内乡县', 'NXG');
INSERT INTO `t_area` VALUES ('2009', '199', '淅川县', '17-199-2009', '3', 'Xichuan Xian', '河南省南阳市淅川县', 'XCY');
INSERT INTO `t_area` VALUES ('2010', '199', '社旗县', '17-199-2010', '3', 'Sheqi Xian', '河南省南阳市社旗县', 'SEQ');
INSERT INTO `t_area` VALUES ('2011', '199', '唐河县', '17-199-2011', '3', 'Tanghe Xian', '河南省南阳市唐河县', 'TGH');
INSERT INTO `t_area` VALUES ('2012', '199', '新野县', '17-199-2012', '3', 'Xinye Xian', '河南省南阳市新野县', 'XYE');
INSERT INTO `t_area` VALUES ('2013', '199', '桐柏县', '17-199-2013', '3', 'Tongbai Xian', '河南省南阳市桐柏县', 'TBX');
INSERT INTO `t_area` VALUES ('2014', '199', '邓州市', '17-199-2014', '3', 'Dengzhou Shi', '河南省南阳市邓州市', 'DGZ');
INSERT INTO `t_area` VALUES ('2016', '200', '梁园区', '17-200-2016', '3', 'Liangyuan Qu', '河南省商丘市梁园区', 'LYY');
INSERT INTO `t_area` VALUES ('2017', '200', '睢阳区', '17-200-2017', '3', 'Suiyang Qu', '河南省商丘市睢阳区', 'SYA');
INSERT INTO `t_area` VALUES ('2018', '200', '民权县', '17-200-2018', '3', 'Minquan Xian', '河南省商丘市民权县', 'MQY');
INSERT INTO `t_area` VALUES ('2019', '200', '睢县', '17-200-2019', '3', 'Sui Xian', '河南省商丘市睢县', 'SUI');
INSERT INTO `t_area` VALUES ('2020', '200', '宁陵县', '17-200-2020', '3', 'Ningling Xian', '河南省商丘市宁陵县', 'NGL');
INSERT INTO `t_area` VALUES ('2021', '200', '柘城县', '17-200-2021', '3', 'Zhecheng Xian', '河南省商丘市柘城县', 'ZHC');
INSERT INTO `t_area` VALUES ('2022', '200', '虞城县', '17-200-2022', '3', 'Yucheng Xian', '河南省商丘市虞城县', 'YUC');
INSERT INTO `t_area` VALUES ('2023', '200', '夏邑县', '17-200-2023', '3', 'Xiayi Xian', '河南省商丘市夏邑县', 'XAY');
INSERT INTO `t_area` VALUES ('2024', '200', '永城市', '17-200-2024', '3', 'Yongcheng Shi', '河南省商丘市永城市', 'YOC');
INSERT INTO `t_area` VALUES ('2026', '201', '浉河区', '17-201-2026', '3', 'Shihe Qu', '河南省信阳市浉河区', 'SHU');
INSERT INTO `t_area` VALUES ('2027', '201', '平桥区', '17-201-2027', '3', 'Pingqiao Qu', '河南省信阳市平桥区', 'PQQ');
INSERT INTO `t_area` VALUES ('2028', '201', '罗山县', '17-201-2028', '3', 'Luoshan Xian', '河南省信阳市罗山县', 'LSE');
INSERT INTO `t_area` VALUES ('2029', '201', '光山县', '17-201-2029', '3', 'Guangshan Xian', '河南省信阳市光山县', 'GSX');
INSERT INTO `t_area` VALUES ('2030', '201', '新县', '17-201-2030', '3', 'Xin Xian', '河南省信阳市新县', 'XXI');
INSERT INTO `t_area` VALUES ('2031', '201', '商城县', '17-201-2031', '3', 'Shangcheng Xian', '河南省信阳市商城县', 'SCX');
INSERT INTO `t_area` VALUES ('2032', '201', '固始县', '17-201-2032', '3', 'Gushi Xian', '河南省信阳市固始县', 'GSI');
INSERT INTO `t_area` VALUES ('2033', '201', '潢川县', '17-201-2033', '3', 'Huangchuan Xian', '河南省信阳市潢川县', 'HCU');
INSERT INTO `t_area` VALUES ('2034', '201', '淮滨县', '17-201-2034', '3', 'Huaibin Xian', '河南省信阳市淮滨县', 'HBN');
INSERT INTO `t_area` VALUES ('2035', '201', '息县', '17-201-2035', '3', 'Xi Xian', '河南省信阳市息县', 'XIX');
INSERT INTO `t_area` VALUES ('2037', '202', '川汇区', '17-202-2037', '3', 'Chuanhui Qu', '河南省周口市川汇区', '2');
INSERT INTO `t_area` VALUES ('2038', '202', '扶沟县', '17-202-2038', '3', 'Fugou Xian', '河南省周口市扶沟县', '2');
INSERT INTO `t_area` VALUES ('2039', '202', '西华县', '17-202-2039', '3', 'Xihua Xian', '河南省周口市西华县', '2');
INSERT INTO `t_area` VALUES ('2040', '202', '商水县', '17-202-2040', '3', 'Shangshui Xian', '河南省周口市商水县', '2');
INSERT INTO `t_area` VALUES ('2041', '202', '沈丘县', '17-202-2041', '3', 'Shenqiu Xian', '河南省周口市沈丘县', '2');
INSERT INTO `t_area` VALUES ('2042', '202', '郸城县', '17-202-2042', '3', 'Dancheng Xian', '河南省周口市郸城县', '2');
INSERT INTO `t_area` VALUES ('2043', '202', '淮阳县', '17-202-2043', '3', 'Huaiyang Xian', '河南省周口市淮阳县', '2');
INSERT INTO `t_area` VALUES ('2044', '202', '太康县', '17-202-2044', '3', 'Taikang Xian', '河南省周口市太康县', '2');
INSERT INTO `t_area` VALUES ('2045', '202', '鹿邑县', '17-202-2045', '3', 'Luyi Xian', '河南省周口市鹿邑县', '2');
INSERT INTO `t_area` VALUES ('2046', '202', '项城市', '17-202-2046', '3', 'Xiangcheng Shi', '河南省周口市项城市', '2');
INSERT INTO `t_area` VALUES ('2048', '203', '驿城区', '17-203-2048', '3', 'Yicheng Qu', '河南省驻马店市驿城区', '2');
INSERT INTO `t_area` VALUES ('2049', '203', '西平县', '17-203-2049', '3', 'Xiping Xian', '河南省驻马店市西平县', '2');
INSERT INTO `t_area` VALUES ('2050', '203', '上蔡县', '17-203-2050', '3', 'Shangcai Xian', '河南省驻马店市上蔡县', '2');
INSERT INTO `t_area` VALUES ('2051', '203', '平舆县', '17-203-2051', '3', 'Pingyu Xian', '河南省驻马店市平舆县', '2');
INSERT INTO `t_area` VALUES ('2052', '203', '正阳县', '17-203-2052', '3', 'Zhengyang Xian', '河南省驻马店市正阳县', '2');
INSERT INTO `t_area` VALUES ('2053', '203', '确山县', '17-203-2053', '3', 'Queshan Xian', '河南省驻马店市确山县', '2');
INSERT INTO `t_area` VALUES ('2054', '203', '泌阳县', '17-203-2054', '3', 'Biyang Xian', '河南省驻马店市泌阳县', '2');
INSERT INTO `t_area` VALUES ('2055', '203', '汝南县', '17-203-2055', '3', 'Runan Xian', '河南省驻马店市汝南县', '2');
INSERT INTO `t_area` VALUES ('2056', '203', '遂平县', '17-203-2056', '3', 'Suiping Xian', '河南省驻马店市遂平县', '2');
INSERT INTO `t_area` VALUES ('2057', '203', '新蔡县', '17-203-2057', '3', 'Xincai Xian', '河南省驻马店市新蔡县', '2');
INSERT INTO `t_area` VALUES ('2059', '204', '江岸区', '18-204-2059', '3', 'Jiang,an Qu', '湖北省武汉市江岸区', 'JAA');
INSERT INTO `t_area` VALUES ('2060', '204', '江汉区', '18-204-2060', '3', 'Jianghan Qu', '湖北省武汉市江汉区', 'JHN');
INSERT INTO `t_area` VALUES ('2061', '204', '硚口区', '18-204-2061', '3', 'Qiaokou Qu', '湖北省武汉市硚口区', 'QKQ');
INSERT INTO `t_area` VALUES ('2062', '204', '汉阳区', '18-204-2062', '3', 'Hanyang Qu', '湖北省武汉市汉阳区', 'HYA');
INSERT INTO `t_area` VALUES ('2063', '204', '武昌区', '18-204-2063', '3', 'Wuchang Qu', '湖北省武汉市武昌区', 'WCQ');
INSERT INTO `t_area` VALUES ('2064', '204', '青山区', '18-204-2064', '3', 'Qingshan Qu', '湖北省武汉市青山区', 'QSN');
INSERT INTO `t_area` VALUES ('2065', '204', '洪山区', '18-204-2065', '3', 'Hongshan Qu', '湖北省武汉市洪山区', 'HSQ');
INSERT INTO `t_area` VALUES ('2066', '204', '东西湖区', '18-204-2066', '3', 'Dongxihu Qu', '湖北省武汉市东西湖区', 'DXH');
INSERT INTO `t_area` VALUES ('2067', '204', '汉南区', '18-204-2067', '3', 'Hannan Qu', '湖北省武汉市汉南区', 'HNQ');
INSERT INTO `t_area` VALUES ('2068', '204', '蔡甸区', '18-204-2068', '3', 'Caidian Qu', '湖北省武汉市蔡甸区', 'CDN');
INSERT INTO `t_area` VALUES ('2069', '204', '江夏区', '18-204-2069', '3', 'Jiangxia Qu', '湖北省武汉市江夏区', 'JXQ');
INSERT INTO `t_area` VALUES ('2070', '204', '黄陂区', '18-204-2070', '3', 'Huangpi Qu', '湖北省武汉市黄陂区', 'HPI');
INSERT INTO `t_area` VALUES ('2071', '204', '新洲区', '18-204-2071', '3', 'Xinzhou Qu', '湖北省武汉市新洲区', 'XNZ');
INSERT INTO `t_area` VALUES ('2073', '205', '黄石港区', '18-205-2073', '3', 'Huangshigang Qu', '湖北省黄石市黄石港区', 'HSG');
INSERT INTO `t_area` VALUES ('2074', '205', '西塞山区', '18-205-2074', '3', 'Xisaishan Qu', '湖北省黄石市西塞山区', '2');
INSERT INTO `t_area` VALUES ('2075', '205', '下陆区', '18-205-2075', '3', 'Xialu Qu', '湖北省黄石市下陆区', 'XAL');
INSERT INTO `t_area` VALUES ('2076', '205', '铁山区', '18-205-2076', '3', 'Tieshan Qu', '湖北省黄石市铁山区', 'TSH');
INSERT INTO `t_area` VALUES ('2077', '205', '阳新县', '18-205-2077', '3', 'Yangxin Xian', '湖北省黄石市阳新县', 'YXE');
INSERT INTO `t_area` VALUES ('2078', '205', '大冶市', '18-205-2078', '3', 'Daye Shi', '湖北省黄石市大冶市', 'DYE');
INSERT INTO `t_area` VALUES ('2080', '206', '茅箭区', '18-206-2080', '3', 'Maojian Qu', '湖北省十堰市茅箭区', 'MJN');
INSERT INTO `t_area` VALUES ('2081', '206', '张湾区', '18-206-2081', '3', 'Zhangwan Qu', '湖北省十堰市张湾区', 'ZWQ');
INSERT INTO `t_area` VALUES ('2082', '206', '郧县', '18-206-2082', '3', 'Yun Xian', '湖北省十堰市郧县', 'YUN');
INSERT INTO `t_area` VALUES ('2083', '206', '郧西县', '18-206-2083', '3', 'Yunxi Xian', '湖北省十堰市郧西县', 'YNX');
INSERT INTO `t_area` VALUES ('2084', '206', '竹山县', '18-206-2084', '3', 'Zhushan Xian', '湖北省十堰市竹山县', 'ZHS');
INSERT INTO `t_area` VALUES ('2085', '206', '竹溪县', '18-206-2085', '3', 'Zhuxi Xian', '湖北省十堰市竹溪县', 'ZXX');
INSERT INTO `t_area` VALUES ('2086', '206', '房县', '18-206-2086', '3', 'Fang Xian', '湖北省十堰市房县', 'FAG');
INSERT INTO `t_area` VALUES ('2087', '206', '丹江口市', '18-206-2087', '3', 'Danjiangkou Shi', '湖北省十堰市丹江口市', 'DJK');
INSERT INTO `t_area` VALUES ('2089', '207', '西陵区', '18-207-2089', '3', 'Xiling Qu', '湖北省宜昌市西陵区', 'XLQ');
INSERT INTO `t_area` VALUES ('2090', '207', '伍家岗区', '18-207-2090', '3', 'Wujiagang Qu', '湖北省宜昌市伍家岗区', 'WJG');
INSERT INTO `t_area` VALUES ('2091', '207', '点军区', '18-207-2091', '3', 'Dianjun Qu', '湖北省宜昌市点军区', 'DJN');
INSERT INTO `t_area` VALUES ('2092', '207', '猇亭区', '18-207-2092', '3', 'Xiaoting Qu', '湖北省宜昌市猇亭区', 'XTQ');
INSERT INTO `t_area` VALUES ('2093', '207', '夷陵区', '18-207-2093', '3', 'Yiling Qu', '湖北省宜昌市夷陵区', '2');
INSERT INTO `t_area` VALUES ('2094', '207', '远安县', '18-207-2094', '3', 'Yuan,an Xian', '湖北省宜昌市远安县', 'YAX');
INSERT INTO `t_area` VALUES ('2095', '207', '兴山县', '18-207-2095', '3', 'Xingshan Xian', '湖北省宜昌市兴山县', 'XSX');
INSERT INTO `t_area` VALUES ('2096', '207', '秭归县', '18-207-2096', '3', 'Zigui Xian', '湖北省宜昌市秭归县', 'ZGI');
INSERT INTO `t_area` VALUES ('2097', '207', '长阳土家族自治县', '18-207-2097', '3', 'Changyang Tujiazu Zizhixian', '湖北省宜昌市长阳土家族自治县', 'CYA');
INSERT INTO `t_area` VALUES ('2098', '207', '五峰土家族自治县', '18-207-2098', '3', 'Wufeng Tujiazu Zizhixian', '湖北省宜昌市五峰土家族自治县', 'WFG');
INSERT INTO `t_area` VALUES ('2099', '207', '宜都市', '18-207-2099', '3', 'Yidu Shi', '湖北省宜昌市宜都市', 'YID');
INSERT INTO `t_area` VALUES ('2100', '207', '当阳市', '18-207-2100', '3', 'Dangyang Shi', '湖北省宜昌市当阳市', 'DYS');
INSERT INTO `t_area` VALUES ('2101', '207', '枝江市', '18-207-2101', '3', 'Zhijiang Shi', '湖北省宜昌市枝江市', 'ZIJ');
INSERT INTO `t_area` VALUES ('2103', '208', '襄城区', '18-208-2103', '3', 'Xiangcheng Qu', '湖北省襄樊市襄城区', 'XXF');
INSERT INTO `t_area` VALUES ('2104', '208', '樊城区', '18-208-2104', '3', 'Fancheng Qu', '湖北省襄樊市樊城区', 'FNC');
INSERT INTO `t_area` VALUES ('2105', '208', '襄阳区', '18-208-2105', '3', 'Xiangyang Qu', '湖北省襄樊市襄阳区', '2');
INSERT INTO `t_area` VALUES ('2106', '208', '南漳县', '18-208-2106', '3', 'Nanzhang Xian', '湖北省襄樊市南漳县', 'NZH');
INSERT INTO `t_area` VALUES ('2107', '208', '谷城县', '18-208-2107', '3', 'Gucheng Xian', '湖北省襄樊市谷城县', 'GUC');
INSERT INTO `t_area` VALUES ('2108', '208', '保康县', '18-208-2108', '3', 'Baokang Xian', '湖北省襄樊市保康县', 'BKG');
INSERT INTO `t_area` VALUES ('2109', '208', '老河口市', '18-208-2109', '3', 'Laohekou Shi', '湖北省襄樊市老河口市', 'LHK');
INSERT INTO `t_area` VALUES ('2110', '208', '枣阳市', '18-208-2110', '3', 'Zaoyang Shi', '湖北省襄樊市枣阳市', 'ZOY');
INSERT INTO `t_area` VALUES ('2111', '208', '宜城市', '18-208-2111', '3', 'Yicheng Shi', '湖北省襄樊市宜城市', 'YCW');
INSERT INTO `t_area` VALUES ('2113', '209', '梁子湖区', '18-209-2113', '3', 'Liangzihu Qu', '湖北省鄂州市梁子湖区', 'LZI');
INSERT INTO `t_area` VALUES ('2114', '209', '华容区', '18-209-2114', '3', 'Huarong Qu', '湖北省鄂州市华容区', 'HRQ');
INSERT INTO `t_area` VALUES ('2115', '209', '鄂城区', '18-209-2115', '3', 'Echeng Qu', '湖北省鄂州市鄂城区', 'ECQ');
INSERT INTO `t_area` VALUES ('2117', '210', '东宝区', '18-210-2117', '3', 'Dongbao Qu', '湖北省荆门市东宝区', 'DBQ');
INSERT INTO `t_area` VALUES ('2118', '210', '掇刀区', '18-210-2118', '3', 'Duodao Qu', '湖北省荆门市掇刀区', '2');
INSERT INTO `t_area` VALUES ('2119', '210', '京山县', '18-210-2119', '3', 'Jingshan Xian', '湖北省荆门市京山县', 'JSA');
INSERT INTO `t_area` VALUES ('2120', '210', '沙洋县', '18-210-2120', '3', 'Shayang Xian', '湖北省荆门市沙洋县', 'SYF');
INSERT INTO `t_area` VALUES ('2121', '210', '钟祥市', '18-210-2121', '3', 'Zhongxiang Shi', '湖北省荆门市钟祥市', '2');
INSERT INTO `t_area` VALUES ('2123', '211', '孝南区', '18-211-2123', '3', 'Xiaonan Qu', '湖北省孝感市孝南区', 'XNA');
INSERT INTO `t_area` VALUES ('2124', '211', '孝昌县', '18-211-2124', '3', 'Xiaochang Xian', '湖北省孝感市孝昌县', 'XOC');
INSERT INTO `t_area` VALUES ('2125', '211', '大悟县', '18-211-2125', '3', 'Dawu Xian', '湖北省孝感市大悟县', 'DWU');
INSERT INTO `t_area` VALUES ('2126', '211', '云梦县', '18-211-2126', '3', 'Yunmeng Xian', '湖北省孝感市云梦县', 'YMX');
INSERT INTO `t_area` VALUES ('2127', '211', '应城市', '18-211-2127', '3', 'Yingcheng Shi', '湖北省孝感市应城市', 'YCG');
INSERT INTO `t_area` VALUES ('2128', '211', '安陆市', '18-211-2128', '3', 'Anlu Shi', '湖北省孝感市安陆市', 'ALU');
INSERT INTO `t_area` VALUES ('2129', '211', '汉川市', '18-211-2129', '3', 'Hanchuan Shi', '湖北省孝感市汉川市', 'HCH');
INSERT INTO `t_area` VALUES ('2131', '212', '沙市区', '18-212-2131', '3', 'Shashi Qu', '湖北省荆州市沙市区', 'SSJ');
INSERT INTO `t_area` VALUES ('2132', '212', '荆州区', '18-212-2132', '3', 'Jingzhou Qu', '湖北省荆州市荆州区', 'JZQ');
INSERT INTO `t_area` VALUES ('2133', '212', '公安县', '18-212-2133', '3', 'Gong,an Xian', '湖北省荆州市公安县', 'GGA');
INSERT INTO `t_area` VALUES ('2134', '212', '监利县', '18-212-2134', '3', 'Jianli Xian', '湖北省荆州市监利县', 'JLI');
INSERT INTO `t_area` VALUES ('2135', '212', '江陵县', '18-212-2135', '3', 'Jiangling Xian', '湖北省荆州市江陵县', 'JLX');
INSERT INTO `t_area` VALUES ('2136', '212', '石首市', '18-212-2136', '3', 'Shishou Shi', '湖北省荆州市石首市', 'SSO');
INSERT INTO `t_area` VALUES ('2137', '212', '洪湖市', '18-212-2137', '3', 'Honghu Shi', '湖北省荆州市洪湖市', 'HHU');
INSERT INTO `t_area` VALUES ('2138', '212', '松滋市', '18-212-2138', '3', 'Songzi Shi', '湖北省荆州市松滋市', 'SZF');
INSERT INTO `t_area` VALUES ('2140', '213', '黄州区', '18-213-2140', '3', 'Huangzhou Qu', '湖北省黄冈市黄州区', 'HZC');
INSERT INTO `t_area` VALUES ('2141', '213', '团风县', '18-213-2141', '3', 'Tuanfeng Xian', '湖北省黄冈市团风县', 'TFG');
INSERT INTO `t_area` VALUES ('2142', '213', '红安县', '18-213-2142', '3', 'Hong,an Xian', '湖北省黄冈市红安县', 'HGA');
INSERT INTO `t_area` VALUES ('2143', '213', '罗田县', '18-213-2143', '3', 'Luotian Xian', '湖北省黄冈市罗田县', 'LTE');
INSERT INTO `t_area` VALUES ('2144', '213', '英山县', '18-213-2144', '3', 'Yingshan Xian', '湖北省黄冈市英山县', 'YSE');
INSERT INTO `t_area` VALUES ('2145', '213', '浠水县', '18-213-2145', '3', 'Xishui Xian', '湖北省黄冈市浠水县', 'XSE');
INSERT INTO `t_area` VALUES ('2146', '213', '蕲春县', '18-213-2146', '3', 'Qichun Xian', '湖北省黄冈市蕲春县', 'QCN');
INSERT INTO `t_area` VALUES ('2147', '213', '黄梅县', '18-213-2147', '3', 'Huangmei Xian', '湖北省黄冈市黄梅县', 'HGM');
INSERT INTO `t_area` VALUES ('2148', '213', '麻城市', '18-213-2148', '3', 'Macheng Shi', '湖北省黄冈市麻城市', 'MCS');
INSERT INTO `t_area` VALUES ('2149', '213', '武穴市', '18-213-2149', '3', 'Wuxue Shi', '湖北省黄冈市武穴市', 'WXE');
INSERT INTO `t_area` VALUES ('2151', '214', '咸安区', '18-214-2151', '3', 'Xian,an Qu', '湖北省咸宁市咸安区', 'XAN');
INSERT INTO `t_area` VALUES ('2152', '214', '嘉鱼县', '18-214-2152', '3', 'Jiayu Xian', '湖北省咸宁市嘉鱼县', 'JYX');
INSERT INTO `t_area` VALUES ('2153', '214', '通城县', '18-214-2153', '3', 'Tongcheng Xian', '湖北省咸宁市通城县', 'TCX');
INSERT INTO `t_area` VALUES ('2154', '214', '崇阳县', '18-214-2154', '3', 'Chongyang Xian', '湖北省咸宁市崇阳县', 'CGY');
INSERT INTO `t_area` VALUES ('2155', '214', '通山县', '18-214-2155', '3', 'Tongshan Xian', '湖北省咸宁市通山县', 'TSA');
INSERT INTO `t_area` VALUES ('2156', '214', '赤壁市', '18-214-2156', '3', 'Chibi Shi', '湖北省咸宁市赤壁市', 'CBI');
INSERT INTO `t_area` VALUES ('2158', '215', '曾都区', '18-215-2158', '3', 'Zengdu Qu', '湖北省随州市曾都区', '2');
INSERT INTO `t_area` VALUES ('2159', '215', '广水市', '18-215-2159', '3', 'Guangshui Shi', '湖北省随州市广水市', '2');
INSERT INTO `t_area` VALUES ('2160', '216', '恩施市', '18-216-2160', '3', 'Enshi Shi', '湖北省恩施土家族苗族自治州恩施市', 'ESS');
INSERT INTO `t_area` VALUES ('2161', '216', '利川市', '18-216-2161', '3', 'Lichuan Shi', '湖北省恩施土家族苗族自治州利川市', 'LCE');
INSERT INTO `t_area` VALUES ('2162', '216', '建始县', '18-216-2162', '3', 'Jianshi Xian', '湖北省恩施土家族苗族自治州建始县', 'JSE');
INSERT INTO `t_area` VALUES ('2163', '216', '巴东县', '18-216-2163', '3', 'Badong Xian', '湖北省恩施土家族苗族自治州巴东县', 'BDG');
INSERT INTO `t_area` VALUES ('2164', '216', '宣恩县', '18-216-2164', '3', 'Xuan,en Xian', '湖北省恩施土家族苗族自治州宣恩县', 'XEN');
INSERT INTO `t_area` VALUES ('2165', '216', '咸丰县', '18-216-2165', '3', 'Xianfeng Xian', '湖北省恩施土家族苗族自治州咸丰县', 'XFG');
INSERT INTO `t_area` VALUES ('2166', '216', '来凤县', '18-216-2166', '3', 'Laifeng Xian', '湖北省恩施土家族苗族自治州来凤县', 'LFG');
INSERT INTO `t_area` VALUES ('2167', '216', '鹤峰县', '18-216-2167', '3', 'Hefeng Xian', '湖北省恩施土家族苗族自治州鹤峰县', 'HEF');
INSERT INTO `t_area` VALUES ('2168', '18', '仙桃市', '18-2168', '2', 'Xiantao Shi', '湖北省仙桃市', 'XNT');
INSERT INTO `t_area` VALUES ('2169', '18', '潜江市', '18-2169', '2', 'Qianjiang Shi', '湖北省潜江市', 'QNJ');
INSERT INTO `t_area` VALUES ('2170', '18', '天门市', '18-2170', '2', 'Tianmen Shi', '湖北省天门市', 'TMS');
INSERT INTO `t_area` VALUES ('2171', '18', '神农架林区', '18-2171', '2', 'Shennongjia Linqu', '湖北省神农架林区', 'SNJ');
INSERT INTO `t_area` VALUES ('2173', '218', '芙蓉区', '19-218-2173', '3', 'Furong Qu', '湖南省长沙市芙蓉区', 'FRQ');
INSERT INTO `t_area` VALUES ('2174', '218', '天心区', '19-218-2174', '3', 'Tianxin Qu', '湖南省长沙市天心区', 'TXQ');
INSERT INTO `t_area` VALUES ('2175', '218', '岳麓区', '19-218-2175', '3', 'Yuelu Qu', '湖南省长沙市岳麓区', 'YLU');
INSERT INTO `t_area` VALUES ('2176', '218', '开福区', '19-218-2176', '3', 'Kaifu Qu', '湖南省长沙市开福区', 'KFQ');
INSERT INTO `t_area` VALUES ('2177', '218', '雨花区', '19-218-2177', '3', 'Yuhua Qu', '湖南省长沙市雨花区', 'YHA');
INSERT INTO `t_area` VALUES ('2178', '218', '长沙县', '19-218-2178', '3', 'Changsha Xian', '湖南省长沙市长沙县', 'CSA');
INSERT INTO `t_area` VALUES ('2179', '218', '望城县', '19-218-2179', '3', 'Wangcheng Xian', '湖南省长沙市望城县', 'WCH');
INSERT INTO `t_area` VALUES ('2180', '218', '宁乡县', '19-218-2180', '3', 'Ningxiang Xian', '湖南省长沙市宁乡县', 'NXX');
INSERT INTO `t_area` VALUES ('2181', '218', '浏阳市', '19-218-2181', '3', 'Liuyang Shi', '湖南省长沙市浏阳市', 'LYS');
INSERT INTO `t_area` VALUES ('2183', '219', '荷塘区', '19-219-2183', '3', 'Hetang Qu', '湖南省株洲市荷塘区', 'HTZ');
INSERT INTO `t_area` VALUES ('2184', '219', '芦淞区', '19-219-2184', '3', 'Lusong Qu', '湖南省株洲市芦淞区', 'LZZ');
INSERT INTO `t_area` VALUES ('2185', '219', '石峰区', '19-219-2185', '3', 'Shifeng Qu', '湖南省株洲市石峰区', 'SFG');
INSERT INTO `t_area` VALUES ('2186', '219', '天元区', '19-219-2186', '3', 'Tianyuan Qu', '湖南省株洲市天元区', 'TYQ');
INSERT INTO `t_area` VALUES ('2187', '219', '株洲县', '19-219-2187', '3', 'Zhuzhou Xian', '湖南省株洲市株洲县', 'ZZX');
INSERT INTO `t_area` VALUES ('2188', '219', '攸县', '19-219-2188', '3', 'You Xian', '湖南省株洲市攸县', 'YOU');
INSERT INTO `t_area` VALUES ('2189', '219', '茶陵县', '19-219-2189', '3', 'Chaling Xian', '湖南省株洲市茶陵县', 'CAL');
INSERT INTO `t_area` VALUES ('2190', '219', '炎陵县', '19-219-2190', '3', 'Yanling Xian', '湖南省株洲市炎陵县', 'YLX');
INSERT INTO `t_area` VALUES ('2191', '219', '醴陵市', '19-219-2191', '3', 'Liling Shi', '湖南省株洲市醴陵市', 'LIL');
INSERT INTO `t_area` VALUES ('2193', '220', '雨湖区', '19-220-2193', '3', 'Yuhu Qu', '湖南省湘潭市雨湖区', 'YHU');
INSERT INTO `t_area` VALUES ('2194', '220', '岳塘区', '19-220-2194', '3', 'Yuetang Qu', '湖南省湘潭市岳塘区', 'YTG');
INSERT INTO `t_area` VALUES ('2195', '220', '湘潭县', '19-220-2195', '3', 'Xiangtan Qu', '湖南省湘潭市湘潭县', 'XTX');
INSERT INTO `t_area` VALUES ('2196', '220', '湘乡市', '19-220-2196', '3', 'Xiangxiang Shi', '湖南省湘潭市湘乡市', 'XXG');
INSERT INTO `t_area` VALUES ('2197', '220', '韶山市', '19-220-2197', '3', 'Shaoshan Shi', '湖南省湘潭市韶山市', 'SSN');
INSERT INTO `t_area` VALUES ('2199', '221', '珠晖区', '19-221-2199', '3', 'Zhuhui Qu', '湖南省衡阳市珠晖区', '2');
INSERT INTO `t_area` VALUES ('2200', '221', '雁峰区', '19-221-2200', '3', 'Yanfeng Qu', '湖南省衡阳市雁峰区', '2');
INSERT INTO `t_area` VALUES ('2201', '221', '石鼓区', '19-221-2201', '3', 'Shigu Qu', '湖南省衡阳市石鼓区', '2');
INSERT INTO `t_area` VALUES ('2202', '221', '蒸湘区', '19-221-2202', '3', 'Zhengxiang Qu', '湖南省衡阳市蒸湘区', '2');
INSERT INTO `t_area` VALUES ('2203', '221', '南岳区', '19-221-2203', '3', 'Nanyue Qu', '湖南省衡阳市南岳区', 'NYQ');
INSERT INTO `t_area` VALUES ('2204', '221', '衡阳县', '19-221-2204', '3', 'Hengyang Xian', '湖南省衡阳市衡阳县', 'HYO');
INSERT INTO `t_area` VALUES ('2205', '221', '衡南县', '19-221-2205', '3', 'Hengnan Xian', '湖南省衡阳市衡南县', 'HNX');
INSERT INTO `t_area` VALUES ('2206', '221', '衡山县', '19-221-2206', '3', 'Hengshan Xian', '湖南省衡阳市衡山县', 'HSH');
INSERT INTO `t_area` VALUES ('2207', '221', '衡东县', '19-221-2207', '3', 'Hengdong Xian', '湖南省衡阳市衡东县', 'HED');
INSERT INTO `t_area` VALUES ('2208', '221', '祁东县', '19-221-2208', '3', 'Qidong Xian', '湖南省衡阳市祁东县', 'QDX');
INSERT INTO `t_area` VALUES ('2209', '221', '耒阳市', '19-221-2209', '3', 'Leiyang Shi', '湖南省衡阳市耒阳市', 'LEY');
INSERT INTO `t_area` VALUES ('2210', '221', '常宁市', '19-221-2210', '3', 'Changning Shi', '湖南省衡阳市常宁市', 'CNS');
INSERT INTO `t_area` VALUES ('2212', '222', '双清区', '19-222-2212', '3', 'Shuangqing Qu', '湖南省邵阳市双清区', 'SGQ');
INSERT INTO `t_area` VALUES ('2213', '222', '大祥区', '19-222-2213', '3', 'Daxiang Qu', '湖南省邵阳市大祥区', 'DXS');
INSERT INTO `t_area` VALUES ('2214', '222', '北塔区', '19-222-2214', '3', 'Beita Qu', '湖南省邵阳市北塔区', 'BET');
INSERT INTO `t_area` VALUES ('2215', '222', '邵东县', '19-222-2215', '3', 'Shaodong Xian', '湖南省邵阳市邵东县', 'SDG');
INSERT INTO `t_area` VALUES ('2216', '222', '新邵县', '19-222-2216', '3', 'Xinshao Xian', '湖南省邵阳市新邵县', 'XSO');
INSERT INTO `t_area` VALUES ('2217', '222', '邵阳县', '19-222-2217', '3', 'Shaoyang Xian', '湖南省邵阳市邵阳县', 'SYW');
INSERT INTO `t_area` VALUES ('2218', '222', '隆回县', '19-222-2218', '3', 'Longhui Xian', '湖南省邵阳市隆回县', 'LGH');
INSERT INTO `t_area` VALUES ('2219', '222', '洞口县', '19-222-2219', '3', 'Dongkou Xian', '湖南省邵阳市洞口县', 'DGK');
INSERT INTO `t_area` VALUES ('2220', '222', '绥宁县', '19-222-2220', '3', 'Suining Xian', '湖南省邵阳市绥宁县', 'SNX');
INSERT INTO `t_area` VALUES ('2221', '222', '新宁县', '19-222-2221', '3', 'Xinning Xian', '湖南省邵阳市新宁县', 'XNI');
INSERT INTO `t_area` VALUES ('2222', '222', '城步苗族自治县', '19-222-2222', '3', 'Chengbu Miaozu Zizhixian', '湖南省邵阳市城步苗族自治县', 'CBU');
INSERT INTO `t_area` VALUES ('2223', '222', '武冈市', '19-222-2223', '3', 'Wugang Shi', '湖南省邵阳市武冈市', 'WGS');
INSERT INTO `t_area` VALUES ('2225', '223', '岳阳楼区', '19-223-2225', '3', 'Yueyanglou Qu', '湖南省岳阳市岳阳楼区', 'YYL');
INSERT INTO `t_area` VALUES ('2226', '223', '云溪区', '19-223-2226', '3', 'Yunxi Qu', '湖南省岳阳市云溪区', 'YXI');
INSERT INTO `t_area` VALUES ('2227', '223', '君山区', '19-223-2227', '3', 'Junshan Qu', '湖南省岳阳市君山区', 'JUS');
INSERT INTO `t_area` VALUES ('2228', '223', '岳阳县', '19-223-2228', '3', 'Yueyang Xian', '湖南省岳阳市岳阳县', 'YYX');
INSERT INTO `t_area` VALUES ('2229', '223', '华容县', '19-223-2229', '3', 'Huarong Xian', '湖南省岳阳市华容县', 'HRG');
INSERT INTO `t_area` VALUES ('2230', '223', '湘阴县', '19-223-2230', '3', 'Xiangyin Xian', '湖南省岳阳市湘阴县', 'XYN');
INSERT INTO `t_area` VALUES ('2231', '223', '平江县', '19-223-2231', '3', 'Pingjiang Xian', '湖南省岳阳市平江县', 'PJH');
INSERT INTO `t_area` VALUES ('2232', '223', '汨罗市', '19-223-2232', '3', 'Miluo Shi', '湖南省岳阳市汨罗市', 'MLU');
INSERT INTO `t_area` VALUES ('2233', '223', '临湘市', '19-223-2233', '3', 'Linxiang Shi', '湖南省岳阳市临湘市', 'LXY');
INSERT INTO `t_area` VALUES ('2235', '224', '武陵区', '19-224-2235', '3', 'Wuling Qu', '湖南省常德市武陵区', 'WLQ');
INSERT INTO `t_area` VALUES ('2236', '224', '鼎城区', '19-224-2236', '3', 'Dingcheng Qu', '湖南省常德市鼎城区', 'DCE');
INSERT INTO `t_area` VALUES ('2237', '224', '安乡县', '19-224-2237', '3', 'Anxiang Xian', '湖南省常德市安乡县', 'AXG');
INSERT INTO `t_area` VALUES ('2238', '224', '汉寿县', '19-224-2238', '3', 'Hanshou Xian', '湖南省常德市汉寿县', 'HSO');
INSERT INTO `t_area` VALUES ('2239', '224', '澧县', '19-224-2239', '3', 'Li Xian', '湖南省常德市澧县', 'LXX');
INSERT INTO `t_area` VALUES ('2240', '224', '临澧县', '19-224-2240', '3', 'Linli Xian', '湖南省常德市临澧县', 'LNL');
INSERT INTO `t_area` VALUES ('2241', '224', '桃源县', '19-224-2241', '3', 'Taoyuan Xian', '湖南省常德市桃源县', 'TOY');
INSERT INTO `t_area` VALUES ('2242', '224', '石门县', '19-224-2242', '3', 'Shimen Xian', '湖南省常德市石门县', 'SHM');
INSERT INTO `t_area` VALUES ('2243', '224', '津市市', '19-224-2243', '3', 'Jinshi Shi', '湖南省常德市津市市', 'JSS');
INSERT INTO `t_area` VALUES ('2245', '225', '永定区', '19-225-2245', '3', 'Yongding Qu', '湖南省张家界市永定区', 'YDQ');
INSERT INTO `t_area` VALUES ('2246', '225', '武陵源区', '19-225-2246', '3', 'Wulingyuan Qu', '湖南省张家界市武陵源区', 'WLY');
INSERT INTO `t_area` VALUES ('2247', '225', '慈利县', '19-225-2247', '3', 'Cili Xian', '湖南省张家界市慈利县', 'CLI');
INSERT INTO `t_area` VALUES ('2248', '225', '桑植县', '19-225-2248', '3', 'Sangzhi Xian', '湖南省张家界市桑植县', 'SZT');
INSERT INTO `t_area` VALUES ('2250', '226', '资阳区', '19-226-2250', '3', 'Ziyang Qu', '湖南省益阳市资阳区', 'ZYC');
INSERT INTO `t_area` VALUES ('2251', '226', '赫山区', '19-226-2251', '3', 'Heshan Qu', '湖南省益阳市赫山区', 'HSY');
INSERT INTO `t_area` VALUES ('2252', '226', '南县', '19-226-2252', '3', 'Nan Xian', '湖南省益阳市南县', 'NXN');
INSERT INTO `t_area` VALUES ('2253', '226', '桃江县', '19-226-2253', '3', 'Taojiang Xian', '湖南省益阳市桃江县', 'TJG');
INSERT INTO `t_area` VALUES ('2254', '226', '安化县', '19-226-2254', '3', 'Anhua Xian', '湖南省益阳市安化县', 'ANH');
INSERT INTO `t_area` VALUES ('2255', '226', '沅江市', '19-226-2255', '3', 'Yuanjiang Shi', '湖南省益阳市沅江市', 'YJS');
INSERT INTO `t_area` VALUES ('2257', '227', '北湖区', '19-227-2257', '3', 'Beihu Qu', '湖南省郴州市北湖区', 'BHQ');
INSERT INTO `t_area` VALUES ('2258', '227', '苏仙区', '19-227-2258', '3', 'Suxian Qu', '湖南省郴州市苏仙区', '2');
INSERT INTO `t_area` VALUES ('2259', '227', '桂阳县', '19-227-2259', '3', 'Guiyang Xian', '湖南省郴州市桂阳县', 'GYX');
INSERT INTO `t_area` VALUES ('2260', '227', '宜章县', '19-227-2260', '3', 'yizhang Xian', '湖南省郴州市宜章县', 'YZA');
INSERT INTO `t_area` VALUES ('2261', '227', '永兴县', '19-227-2261', '3', 'Yongxing Xian', '湖南省郴州市永兴县', 'YXX');
INSERT INTO `t_area` VALUES ('2262', '227', '嘉禾县', '19-227-2262', '3', 'Jiahe Xian', '湖南省郴州市嘉禾县', 'JAH');
INSERT INTO `t_area` VALUES ('2263', '227', '临武县', '19-227-2263', '3', 'Linwu Xian', '湖南省郴州市临武县', 'LWX');
INSERT INTO `t_area` VALUES ('2264', '227', '汝城县', '19-227-2264', '3', 'Rucheng Xian', '湖南省郴州市汝城县', 'RCE');
INSERT INTO `t_area` VALUES ('2265', '227', '桂东县', '19-227-2265', '3', 'Guidong Xian', '湖南省郴州市桂东县', 'GDO');
INSERT INTO `t_area` VALUES ('2266', '227', '安仁县', '19-227-2266', '3', 'Anren Xian', '湖南省郴州市安仁县', 'ARN');
INSERT INTO `t_area` VALUES ('2267', '227', '资兴市', '19-227-2267', '3', 'Zixing Shi', '湖南省郴州市资兴市', 'ZXG');
INSERT INTO `t_area` VALUES ('2270', '228', '冷水滩区', '19-228-2270', '3', 'Lengshuitan Qu', '湖南省永州市冷水滩区', 'LST');
INSERT INTO `t_area` VALUES ('2271', '228', '祁阳县', '19-228-2271', '3', 'Qiyang Xian', '湖南省永州市祁阳县', 'QJY');
INSERT INTO `t_area` VALUES ('2272', '228', '东安县', '19-228-2272', '3', 'Dong,an Xian', '湖南省永州市东安县', 'DOA');
INSERT INTO `t_area` VALUES ('2273', '228', '双牌县', '19-228-2273', '3', 'Shuangpai Xian', '湖南省永州市双牌县', 'SPA');
INSERT INTO `t_area` VALUES ('2274', '228', '道县', '19-228-2274', '3', 'Dao Xian', '湖南省永州市道县', 'DAO');
INSERT INTO `t_area` VALUES ('2275', '228', '江永县', '19-228-2275', '3', 'Jiangyong Xian', '湖南省永州市江永县', 'JYD');
INSERT INTO `t_area` VALUES ('2276', '228', '宁远县', '19-228-2276', '3', 'Ningyuan Xian', '湖南省永州市宁远县', 'NYN');
INSERT INTO `t_area` VALUES ('2277', '228', '蓝山县', '19-228-2277', '3', 'Lanshan Xian', '湖南省永州市蓝山县', 'LNS');
INSERT INTO `t_area` VALUES ('2278', '228', '新田县', '19-228-2278', '3', 'Xintian Xian', '湖南省永州市新田县', 'XTN');
INSERT INTO `t_area` VALUES ('2279', '228', '江华瑶族自治县', '19-228-2279', '3', 'Jianghua Yaozu Zizhixian', '湖南省永州市江华瑶族自治县', 'JHX');
INSERT INTO `t_area` VALUES ('2281', '229', '鹤城区', '19-229-2281', '3', 'Hecheng Qu', '湖南省怀化市鹤城区', 'HCG');
INSERT INTO `t_area` VALUES ('2282', '229', '中方县', '19-229-2282', '3', 'Zhongfang Xian', '湖南省怀化市中方县', 'ZFX');
INSERT INTO `t_area` VALUES ('2283', '229', '沅陵县', '19-229-2283', '3', 'Yuanling Xian', '湖南省怀化市沅陵县', 'YNL');
INSERT INTO `t_area` VALUES ('2284', '229', '辰溪县', '19-229-2284', '3', 'Chenxi Xian', '湖南省怀化市辰溪县', 'CXX');
INSERT INTO `t_area` VALUES ('2285', '229', '溆浦县', '19-229-2285', '3', 'Xupu Xian', '湖南省怀化市溆浦县', 'XUP');
INSERT INTO `t_area` VALUES ('2286', '229', '会同县', '19-229-2286', '3', 'Huitong Xian', '湖南省怀化市会同县', 'HTG');
INSERT INTO `t_area` VALUES ('2287', '229', '麻阳苗族自治县', '19-229-2287', '3', 'Mayang Miaozu Zizhixian', '湖南省怀化市麻阳苗族自治县', 'MYX');
INSERT INTO `t_area` VALUES ('2288', '229', '新晃侗族自治县', '19-229-2288', '3', 'Xinhuang Dongzu Zizhixian', '湖南省怀化市新晃侗族自治县', 'XHD');
INSERT INTO `t_area` VALUES ('2289', '229', '芷江侗族自治县', '19-229-2289', '3', 'Zhijiang Dongzu Zizhixian', '湖南省怀化市芷江侗族自治县', 'ZJX');
INSERT INTO `t_area` VALUES ('2290', '229', '靖州苗族侗族自治县', '19-229-2290', '3', 'Jingzhou Miaozu Dongzu Zizhixian', '湖南省怀化市靖州苗族侗族自治县', 'JZO');
INSERT INTO `t_area` VALUES ('2291', '229', '通道侗族自治县', '19-229-2291', '3', 'Tongdao Dongzu Zizhixian', '湖南省怀化市通道侗族自治县', 'TDD');
INSERT INTO `t_area` VALUES ('2292', '229', '洪江市', '19-229-2292', '3', 'Hongjiang Shi', '湖南省怀化市洪江市', 'HGJ');
INSERT INTO `t_area` VALUES ('2294', '230', '娄星区', '19-230-2294', '3', 'Louxing Qu', '湖南省娄底市娄星区', '2');
INSERT INTO `t_area` VALUES ('2295', '230', '双峰县', '19-230-2295', '3', 'Shuangfeng Xian', '湖南省娄底市双峰县', '2');
INSERT INTO `t_area` VALUES ('2296', '230', '新化县', '19-230-2296', '3', 'Xinhua Xian', '湖南省娄底市新化县', '2');
INSERT INTO `t_area` VALUES ('2297', '230', '冷水江市', '19-230-2297', '3', 'Lengshuijiang Shi', '湖南省娄底市冷水江市', '2');
INSERT INTO `t_area` VALUES ('2298', '230', '涟源市', '19-230-2298', '3', 'Lianyuan Shi', '湖南省娄底市涟源市', '2');
INSERT INTO `t_area` VALUES ('2299', '231', '吉首市', '19-231-2299', '3', 'Jishou Shi', '湖南省湘西土家族苗族自治州吉首市', 'JSO');
INSERT INTO `t_area` VALUES ('2300', '231', '泸溪县', '19-231-2300', '3', 'Luxi Xian', '湖南省湘西土家族苗族自治州泸溪县', 'LXW');
INSERT INTO `t_area` VALUES ('2301', '231', '凤凰县', '19-231-2301', '3', 'Fenghuang Xian', '湖南省湘西土家族苗族自治州凤凰县', 'FHX');
INSERT INTO `t_area` VALUES ('2302', '231', '花垣县', '19-231-2302', '3', 'Huayuan Xian', '湖南省湘西土家族苗族自治州花垣县', 'HYH');
INSERT INTO `t_area` VALUES ('2303', '231', '保靖县', '19-231-2303', '3', 'Baojing Xian', '湖南省湘西土家族苗族自治州保靖县', 'BJG');
INSERT INTO `t_area` VALUES ('2304', '231', '古丈县', '19-231-2304', '3', 'Guzhang Xian', '湖南省湘西土家族苗族自治州古丈县', 'GZG');
INSERT INTO `t_area` VALUES ('2305', '231', '永顺县', '19-231-2305', '3', 'Yongshun Xian', '湖南省湘西土家族苗族自治州永顺县', 'YSF');
INSERT INTO `t_area` VALUES ('2306', '231', '龙山县', '19-231-2306', '3', 'Longshan Xian', '湖南省湘西土家族苗族自治州龙山县', 'LSR');
INSERT INTO `t_area` VALUES ('2308', '232', '南沙区', '20-232-2308', '3', 'Nansha Qu', '广东省广州市南沙区', '2');
INSERT INTO `t_area` VALUES ('2309', '232', '荔湾区', '20-232-2309', '3', 'Liwan Qu', '广东省广州市荔湾区', 'LWQ');
INSERT INTO `t_area` VALUES ('2310', '232', '越秀区', '20-232-2310', '3', 'Yuexiu Qu', '广东省广州市越秀区', 'YXU');
INSERT INTO `t_area` VALUES ('2311', '232', '海珠区', '20-232-2311', '3', 'Haizhu Qu', '广东省广州市海珠区', 'HZU');
INSERT INTO `t_area` VALUES ('2312', '232', '天河区', '20-232-2312', '3', 'Tianhe Qu', '广东省广州市天河区', 'THQ');
INSERT INTO `t_area` VALUES ('2313', '232', '萝岗区', '20-232-2313', '3', 'Luogang Qu', '广东省广州市萝岗区', '2');
INSERT INTO `t_area` VALUES ('2314', '232', '白云区', '20-232-2314', '3', 'Baiyun Qu', '广东省广州市白云区', 'BYN');
INSERT INTO `t_area` VALUES ('2315', '232', '黄埔区', '20-232-2315', '3', 'Huangpu Qu', '广东省广州市黄埔区', 'HPU');
INSERT INTO `t_area` VALUES ('2316', '232', '番禺区', '20-232-2316', '3', 'Panyu Qu', '广东省广州市番禺区', 'PNY');
INSERT INTO `t_area` VALUES ('2317', '232', '花都区', '20-232-2317', '3', 'Huadu Qu', '广东省广州市花都区', 'HDU');
INSERT INTO `t_area` VALUES ('2318', '232', '增城市', '20-232-2318', '3', 'Zengcheng Shi', '广东省广州市增城市', 'ZEC');
INSERT INTO `t_area` VALUES ('2319', '232', '从化市', '20-232-2319', '3', 'Conghua Shi', '广东省广州市从化市', 'CNH');
INSERT INTO `t_area` VALUES ('2321', '233', '武江区', '20-233-2321', '3', 'Wujiang Qu', '广东省韶关市武江区', 'WJQ');
INSERT INTO `t_area` VALUES ('2322', '233', '浈江区', '20-233-2322', '3', 'Zhenjiang Qu', '广东省韶关市浈江区', 'ZJQ');
INSERT INTO `t_area` VALUES ('2323', '233', '曲江区', '20-233-2323', '3', 'Qujiang Qu', '广东省韶关市曲江区', '2');
INSERT INTO `t_area` VALUES ('2324', '233', '始兴县', '20-233-2324', '3', 'Shixing Xian', '广东省韶关市始兴县', 'SXX');
INSERT INTO `t_area` VALUES ('2325', '233', '仁化县', '20-233-2325', '3', 'Renhua Xian', '广东省韶关市仁化县', 'RHA');
INSERT INTO `t_area` VALUES ('2326', '233', '翁源县', '20-233-2326', '3', 'Wengyuan Xian', '广东省韶关市翁源县', 'WYN');
INSERT INTO `t_area` VALUES ('2327', '233', '乳源瑶族自治县', '20-233-2327', '3', 'Ruyuan Yaozu Zizhixian', '广东省韶关市乳源瑶族自治县', 'RYN');
INSERT INTO `t_area` VALUES ('2328', '233', '新丰县', '20-233-2328', '3', 'Xinfeng Xian', '广东省韶关市新丰县', 'XFY');
INSERT INTO `t_area` VALUES ('2329', '233', '乐昌市', '20-233-2329', '3', 'Lechang Shi', '广东省韶关市乐昌市', 'LEC');
INSERT INTO `t_area` VALUES ('2330', '233', '南雄市', '20-233-2330', '3', 'Nanxiong Shi', '广东省韶关市南雄市', 'NXS');
INSERT INTO `t_area` VALUES ('2332', '234', '罗湖区', '20-234-2332', '3', 'Luohu Qu', '广东省深圳市罗湖区', 'LHQ');
INSERT INTO `t_area` VALUES ('2333', '234', '福田区', '20-234-2333', '3', 'Futian Qu', '广东省深圳市福田区', 'FTN');
INSERT INTO `t_area` VALUES ('2334', '234', '南山区', '20-234-2334', '3', 'Nanshan Qu', '广东省深圳市南山区', 'NSN');
INSERT INTO `t_area` VALUES ('2335', '234', '宝安区', '20-234-2335', '3', 'Bao,an Qu', '广东省深圳市宝安区', 'BAQ');
INSERT INTO `t_area` VALUES ('2336', '234', '龙岗区', '20-234-2336', '3', 'Longgang Qu', '广东省深圳市龙岗区', 'LGG');
INSERT INTO `t_area` VALUES ('2337', '234', '盐田区', '20-234-2337', '3', 'Yan Tian Qu', '广东省深圳市盐田区', 'YTQ');
INSERT INTO `t_area` VALUES ('2339', '235', '香洲区', '20-235-2339', '3', 'Xiangzhou Qu', '广东省珠海市香洲区', 'XZQ');
INSERT INTO `t_area` VALUES ('2340', '235', '斗门区', '20-235-2340', '3', 'Doumen Qu', '广东省珠海市斗门区', 'DOU');
INSERT INTO `t_area` VALUES ('2341', '235', '金湾区', '20-235-2341', '3', 'Jinwan Qu', '广东省珠海市金湾区', 'JW Q');
INSERT INTO `t_area` VALUES ('2343', '236', '龙湖区', '20-236-2343', '3', 'Longhu Qu', '广东省汕头市龙湖区', 'LHH');
INSERT INTO `t_area` VALUES ('2344', '236', '金平区', '20-236-2344', '3', 'Jinping Qu', '广东省汕头市金平区', 'JPQ');
INSERT INTO `t_area` VALUES ('2345', '236', '濠江区', '20-236-2345', '3', 'Haojiang Qu', '广东省汕头市濠江区', 'HJ Q');
INSERT INTO `t_area` VALUES ('2346', '236', '潮阳区', '20-236-2346', '3', 'Chaoyang  Qu', '广东省汕头市潮阳区', 'CHY');
INSERT INTO `t_area` VALUES ('2347', '236', '潮南区', '20-236-2347', '3', 'Chaonan Qu', '广东省汕头市潮南区', 'CN Q');
INSERT INTO `t_area` VALUES ('2348', '236', '澄海区', '20-236-2348', '3', 'Chenghai QU', '广东省汕头市澄海区', 'CHS');
INSERT INTO `t_area` VALUES ('2349', '236', '南澳县', '20-236-2349', '3', 'Nan,ao Xian', '广东省汕头市南澳县', 'NAN');
INSERT INTO `t_area` VALUES ('2351', '237', '禅城区', '20-237-2351', '3', 'Chancheng Qu', '广东省佛山市禅城区', 'CC Q');
INSERT INTO `t_area` VALUES ('2352', '237', '南海区', '20-237-2352', '3', 'Nanhai Shi', '广东省佛山市南海区', 'NAH');
INSERT INTO `t_area` VALUES ('2353', '237', '顺德区', '20-237-2353', '3', 'Shunde Shi', '广东省佛山市顺德区', 'SUD');
INSERT INTO `t_area` VALUES ('2354', '237', '三水区', '20-237-2354', '3', 'Sanshui Shi', '广东省佛山市三水区', 'SJQ');
INSERT INTO `t_area` VALUES ('2355', '237', '高明区', '20-237-2355', '3', 'Gaoming Shi', '广东省佛山市高明区', 'GOM');
INSERT INTO `t_area` VALUES ('2357', '238', '蓬江区', '20-238-2357', '3', 'Pengjiang Qu', '广东省江门市蓬江区', 'PJJ');
INSERT INTO `t_area` VALUES ('2358', '238', '江海区', '20-238-2358', '3', 'Jianghai Qu', '广东省江门市江海区', 'JHI');
INSERT INTO `t_area` VALUES ('2359', '238', '新会区', '20-238-2359', '3', 'Xinhui Shi', '广东省江门市新会区', 'XIN');
INSERT INTO `t_area` VALUES ('2360', '238', '台山市', '20-238-2360', '3', 'Taishan Shi', '广东省江门市台山市', 'TSS');
INSERT INTO `t_area` VALUES ('2361', '238', '开平市', '20-238-2361', '3', 'Kaiping Shi', '广东省江门市开平市', 'KPS');
INSERT INTO `t_area` VALUES ('2362', '238', '鹤山市', '20-238-2362', '3', 'Heshan Shi', '广东省江门市鹤山市', 'HES');
INSERT INTO `t_area` VALUES ('2363', '238', '恩平市', '20-238-2363', '3', 'Enping Shi', '广东省江门市恩平市', 'ENP');
INSERT INTO `t_area` VALUES ('2365', '239', '赤坎区', '20-239-2365', '3', 'Chikan Qu', '广东省湛江市赤坎区', 'CKQ');
INSERT INTO `t_area` VALUES ('2366', '239', '霞山区', '20-239-2366', '3', 'Xiashan Qu', '广东省湛江市霞山区', 'XAS');
INSERT INTO `t_area` VALUES ('2367', '239', '坡头区', '20-239-2367', '3', 'Potou Qu', '广东省湛江市坡头区', 'PTU');
INSERT INTO `t_area` VALUES ('2368', '239', '麻章区', '20-239-2368', '3', 'Mazhang Qu', '广东省湛江市麻章区', 'MZQ');
INSERT INTO `t_area` VALUES ('2369', '239', '遂溪县', '20-239-2369', '3', 'Suixi Xian', '广东省湛江市遂溪县', 'SXI');
INSERT INTO `t_area` VALUES ('2370', '239', '徐闻县', '20-239-2370', '3', 'Xuwen Xian', '广东省湛江市徐闻县', 'XWN');
INSERT INTO `t_area` VALUES ('2371', '239', '廉江市', '20-239-2371', '3', 'Lianjiang Shi', '广东省湛江市廉江市', 'LJS');
INSERT INTO `t_area` VALUES ('2372', '239', '雷州市', '20-239-2372', '3', 'Leizhou Shi', '广东省湛江市雷州市', 'LEZ');
INSERT INTO `t_area` VALUES ('2373', '239', '吴川市', '20-239-2373', '3', 'Wuchuan Shi', '广东省湛江市吴川市', 'WCS');
INSERT INTO `t_area` VALUES ('2375', '240', '茂南区', '20-240-2375', '3', 'Maonan Qu', '广东省茂名市茂南区', 'MNQ');
INSERT INTO `t_area` VALUES ('2376', '240', '茂港区', '20-240-2376', '3', 'Maogang Qu', '广东省茂名市茂港区', 'MGQ');
INSERT INTO `t_area` VALUES ('2377', '240', '电白县', '20-240-2377', '3', 'Dianbai Xian', '广东省茂名市电白县', 'DBI');
INSERT INTO `t_area` VALUES ('2378', '240', '高州市', '20-240-2378', '3', 'Gaozhou Shi', '广东省茂名市高州市', 'GZO');
INSERT INTO `t_area` VALUES ('2379', '240', '化州市', '20-240-2379', '3', 'Huazhou Shi', '广东省茂名市化州市', 'HZY');
INSERT INTO `t_area` VALUES ('2380', '240', '信宜市', '20-240-2380', '3', 'Xinyi Shi', '广东省茂名市信宜市', 'XYY');
INSERT INTO `t_area` VALUES ('2382', '241', '端州区', '20-241-2382', '3', 'Duanzhou Qu', '广东省肇庆市端州区', 'DZQ');
INSERT INTO `t_area` VALUES ('2383', '241', '鼎湖区', '20-241-2383', '3', 'Dinghu Qu', '广东省肇庆市鼎湖区', 'DGH');
INSERT INTO `t_area` VALUES ('2384', '241', '广宁县', '20-241-2384', '3', 'Guangning Xian', '广东省肇庆市广宁县', 'GNG');
INSERT INTO `t_area` VALUES ('2385', '241', '怀集县', '20-241-2385', '3', 'Huaiji Xian', '广东省肇庆市怀集县', 'HJX');
INSERT INTO `t_area` VALUES ('2386', '241', '封开县', '20-241-2386', '3', 'Fengkai Xian', '广东省肇庆市封开县', 'FKX');
INSERT INTO `t_area` VALUES ('2387', '241', '德庆县', '20-241-2387', '3', 'Deqing Xian', '广东省肇庆市德庆县', 'DQY');
INSERT INTO `t_area` VALUES ('2388', '241', '高要市', '20-241-2388', '3', 'Gaoyao Xian', '广东省肇庆市高要市', 'GYY');
INSERT INTO `t_area` VALUES ('2389', '241', '四会市', '20-241-2389', '3', 'Sihui Shi', '广东省肇庆市四会市', 'SHI');
INSERT INTO `t_area` VALUES ('2391', '242', '惠城区', '20-242-2391', '3', 'Huicheng Qu', '广东省惠州市惠城区', 'HCQ');
INSERT INTO `t_area` VALUES ('2392', '242', '惠阳区', '20-242-2392', '3', 'Huiyang Shi', '广东省惠州市惠阳区', 'HUY');
INSERT INTO `t_area` VALUES ('2393', '242', '博罗县', '20-242-2393', '3', 'Boluo Xian', '广东省惠州市博罗县', 'BOL');
INSERT INTO `t_area` VALUES ('2394', '242', '惠东县', '20-242-2394', '3', 'Huidong Xian', '广东省惠州市惠东县', 'HID');
INSERT INTO `t_area` VALUES ('2395', '242', '龙门县', '20-242-2395', '3', 'Longmen Xian', '广东省惠州市龙门县', 'LMN');
INSERT INTO `t_area` VALUES ('2397', '243', '梅江区', '20-243-2397', '3', 'Meijiang Qu', '广东省梅州市梅江区', 'MJQ');
INSERT INTO `t_area` VALUES ('2398', '243', '梅县', '20-243-2398', '3', 'Mei Xian', '广东省梅州市梅县', 'MEX');
INSERT INTO `t_area` VALUES ('2399', '243', '大埔县', '20-243-2399', '3', 'Dabu Xian', '广东省梅州市大埔县', 'DBX');
INSERT INTO `t_area` VALUES ('2400', '243', '丰顺县', '20-243-2400', '3', 'Fengshun Xian', '广东省梅州市丰顺县', 'FES');
INSERT INTO `t_area` VALUES ('2401', '243', '五华县', '20-243-2401', '3', 'Wuhua Xian', '广东省梅州市五华县', 'WHY');
INSERT INTO `t_area` VALUES ('2402', '243', '平远县', '20-243-2402', '3', 'Pingyuan Xian', '广东省梅州市平远县', 'PYY');
INSERT INTO `t_area` VALUES ('2403', '243', '蕉岭县', '20-243-2403', '3', 'Jiaoling Xian', '广东省梅州市蕉岭县', 'JOL');
INSERT INTO `t_area` VALUES ('2404', '243', '兴宁市', '20-243-2404', '3', 'Xingning Shi', '广东省梅州市兴宁市', 'XNG');
INSERT INTO `t_area` VALUES ('2407', '244', '海丰县', '20-244-2407', '3', 'Haifeng Xian', '广东省汕尾市海丰县', 'HIF');
INSERT INTO `t_area` VALUES ('2408', '244', '陆河县', '20-244-2408', '3', 'Luhe Xian', '广东省汕尾市陆河县', 'LHY');
INSERT INTO `t_area` VALUES ('2409', '244', '陆丰市', '20-244-2409', '3', 'Lufeng Shi', '广东省汕尾市陆丰市', 'LUF');
INSERT INTO `t_area` VALUES ('2411', '245', '源城区', '20-245-2411', '3', 'Yuancheng Qu', '广东省河源市源城区', 'YCQ');
INSERT INTO `t_area` VALUES ('2412', '245', '紫金县', '20-245-2412', '3', 'Zijin Xian', '广东省河源市紫金县', 'ZJY');
INSERT INTO `t_area` VALUES ('2413', '245', '龙川县', '20-245-2413', '3', 'Longchuan Xian', '广东省河源市龙川县', 'LCY');
INSERT INTO `t_area` VALUES ('2414', '245', '连平县', '20-245-2414', '3', 'Lianping Xian', '广东省河源市连平县', 'LNP');
INSERT INTO `t_area` VALUES ('2415', '245', '和平县', '20-245-2415', '3', 'Heping Xian', '广东省河源市和平县', 'HPY');
INSERT INTO `t_area` VALUES ('2416', '245', '东源县', '20-245-2416', '3', 'Dongyuan Xian', '广东省河源市东源县', 'DYN');
INSERT INTO `t_area` VALUES ('2418', '246', '江城区', '20-246-2418', '3', 'Jiangcheng Qu', '广东省阳江市江城区', 'JCQ');
INSERT INTO `t_area` VALUES ('2419', '246', '阳西县', '20-246-2419', '3', 'Yangxi Xian', '广东省阳江市阳西县', 'YXY');
INSERT INTO `t_area` VALUES ('2420', '246', '阳东县', '20-246-2420', '3', 'Yangdong Xian', '广东省阳江市阳东县', 'YGD');
INSERT INTO `t_area` VALUES ('2421', '246', '阳春市', '20-246-2421', '3', 'Yangchun Shi', '广东省阳江市阳春市', 'YCU');
INSERT INTO `t_area` VALUES ('2423', '247', '清城区', '20-247-2423', '3', 'Qingcheng Qu', '广东省清远市清城区', 'QCQ');
INSERT INTO `t_area` VALUES ('2424', '247', '佛冈县', '20-247-2424', '3', 'Fogang Xian', '广东省清远市佛冈县', 'FGY');
INSERT INTO `t_area` VALUES ('2425', '247', '阳山县', '20-247-2425', '3', 'Yangshan Xian', '广东省清远市阳山县', 'YSN');
INSERT INTO `t_area` VALUES ('2426', '247', '连山壮族瑶族自治县', '20-247-2426', '3', 'Lianshan Zhuangzu Yaozu Zizhixian', '广东省清远市连山壮族瑶族自治县', 'LSZ');
INSERT INTO `t_area` VALUES ('2427', '247', '连南瑶族自治县', '20-247-2427', '3', 'Liannanyaozuzizhi Qu', '广东省清远市连南瑶族自治县', '2');
INSERT INTO `t_area` VALUES ('2428', '247', '清新县', '20-247-2428', '3', 'Qingxin Xian', '广东省清远市清新县', 'QGX');
INSERT INTO `t_area` VALUES ('2429', '247', '英德市', '20-247-2429', '3', 'Yingde Shi', '广东省清远市英德市', 'YDS');
INSERT INTO `t_area` VALUES ('2430', '247', '连州市', '20-247-2430', '3', 'Lianzhou Shi', '广东省清远市连州市', 'LZO');
INSERT INTO `t_area` VALUES ('2432', '250', '湘桥区', '20-250-2432', '3', 'Xiangqiao Qu', '广东省潮州市湘桥区', 'XQO');
INSERT INTO `t_area` VALUES ('2433', '250', '潮安县', '20-250-2433', '3', 'Chao,an Xian', '广东省潮州市潮安县', 'CAY');
INSERT INTO `t_area` VALUES ('2434', '250', '饶平县', '20-250-2434', '3', 'Raoping Xian', '广东省潮州市饶平县', 'RPG');
INSERT INTO `t_area` VALUES ('2436', '251', '榕城区', '20-251-2436', '3', 'Rongcheng Qu', '广东省揭阳市榕城区', 'RCH');
INSERT INTO `t_area` VALUES ('2437', '251', '揭东县', '20-251-2437', '3', 'Jiedong Xian', '广东省揭阳市揭东县', 'JDX');
INSERT INTO `t_area` VALUES ('2438', '251', '揭西县', '20-251-2438', '3', 'Jiexi Xian', '广东省揭阳市揭西县', 'JEX');
INSERT INTO `t_area` VALUES ('2439', '251', '惠来县', '20-251-2439', '3', 'Huilai Xian', '广东省揭阳市惠来县', 'HLY');
INSERT INTO `t_area` VALUES ('2440', '251', '普宁市', '20-251-2440', '3', 'Puning Shi', '广东省揭阳市普宁市', 'PNG');
INSERT INTO `t_area` VALUES ('2442', '252', '云城区', '20-252-2442', '3', 'Yuncheng Qu', '广东省云浮市云城区', 'YYF');
INSERT INTO `t_area` VALUES ('2443', '252', '新兴县', '20-252-2443', '3', 'Xinxing Xian', '广东省云浮市新兴县', 'XNX');
INSERT INTO `t_area` VALUES ('2444', '252', '郁南县', '20-252-2444', '3', 'Yunan Xian', '广东省云浮市郁南县', 'YNK');
INSERT INTO `t_area` VALUES ('2445', '252', '云安县', '20-252-2445', '3', 'Yun,an Xian', '广东省云浮市云安县', 'YUA');
INSERT INTO `t_area` VALUES ('2446', '252', '罗定市', '20-252-2446', '3', 'Luoding Shi', '广东省云浮市罗定市', 'LUO');
INSERT INTO `t_area` VALUES ('2448', '253', '兴宁区', '21-253-2448', '3', 'Xingning Qu', '广西壮族自治区南宁市兴宁区', 'XNE');
INSERT INTO `t_area` VALUES ('2449', '253', '青秀区', '21-253-2449', '3', 'Qingxiu Qu', '广西壮族自治区南宁市青秀区', '2');
INSERT INTO `t_area` VALUES ('2450', '253', '江南区', '21-253-2450', '3', 'Jiangnan Qu', '广西壮族自治区南宁市江南区', 'JNA');
INSERT INTO `t_area` VALUES ('2451', '253', '西乡塘区', '21-253-2451', '3', 'Xixiangtang Qu', '广西壮族自治区南宁市西乡塘区', '2');
INSERT INTO `t_area` VALUES ('2452', '253', '良庆区', '21-253-2452', '3', 'Liangqing Qu', '广西壮族自治区南宁市良庆区', '2');
INSERT INTO `t_area` VALUES ('2453', '253', '邕宁区', '21-253-2453', '3', 'Yongning Qu', '广西壮族自治区南宁市邕宁区', '2');
INSERT INTO `t_area` VALUES ('2454', '253', '武鸣县', '21-253-2454', '3', 'Wuming Xian', '广西壮族自治区南宁市武鸣县', 'WMG');
INSERT INTO `t_area` VALUES ('2455', '253', '隆安县', '21-253-2455', '3', 'Long,an Xian', '广西壮族自治区南宁市隆安县', '2');
INSERT INTO `t_area` VALUES ('2456', '253', '马山县', '21-253-2456', '3', 'Mashan Xian', '广西壮族自治区南宁市马山县', '2');
INSERT INTO `t_area` VALUES ('2457', '253', '上林县', '21-253-2457', '3', 'Shanglin Xian', '广西壮族自治区南宁市上林县', '2');
INSERT INTO `t_area` VALUES ('2458', '253', '宾阳县', '21-253-2458', '3', 'Binyang Xian', '广西壮族自治区南宁市宾阳县', '2');
INSERT INTO `t_area` VALUES ('2459', '253', '横县', '21-253-2459', '3', 'Heng Xian', '广西壮族自治区南宁市横县', '2');
INSERT INTO `t_area` VALUES ('2461', '254', '城中区', '21-254-2461', '3', 'Chengzhong Qu', '广西壮族自治区柳州市城中区', 'CZG');
INSERT INTO `t_area` VALUES ('2462', '254', '鱼峰区', '21-254-2462', '3', 'Yufeng Qu', '广西壮族自治区柳州市鱼峰区', 'YFQ');
INSERT INTO `t_area` VALUES ('2463', '254', '柳南区', '21-254-2463', '3', 'Liunan Qu', '广西壮族自治区柳州市柳南区', 'LNU');
INSERT INTO `t_area` VALUES ('2464', '254', '柳北区', '21-254-2464', '3', 'Liubei Qu', '广西壮族自治区柳州市柳北区', 'LBE');
INSERT INTO `t_area` VALUES ('2465', '254', '柳江县', '21-254-2465', '3', 'Liujiang Xian', '广西壮族自治区柳州市柳江县', 'LUJ');
INSERT INTO `t_area` VALUES ('2466', '254', '柳城县', '21-254-2466', '3', 'Liucheng Xian', '广西壮族自治区柳州市柳城县', 'LCB');
INSERT INTO `t_area` VALUES ('2467', '254', '鹿寨县', '21-254-2467', '3', 'Luzhai Xian', '广西壮族自治区柳州市鹿寨县', '2');
INSERT INTO `t_area` VALUES ('2468', '254', '融安县', '21-254-2468', '3', 'Rong,an Xian', '广西壮族自治区柳州市融安县', '2');
INSERT INTO `t_area` VALUES ('2469', '254', '融水苗族自治县', '21-254-2469', '3', 'Rongshui Miaozu Zizhixian', '广西壮族自治区柳州市融水苗族自治县', '2');
INSERT INTO `t_area` VALUES ('2470', '254', '三江侗族自治县', '21-254-2470', '3', 'Sanjiang Dongzu Zizhixian', '广西壮族自治区柳州市三江侗族自治县', '2');
INSERT INTO `t_area` VALUES ('2472', '255', '秀峰区', '21-255-2472', '3', 'Xiufeng Qu', '广西壮族自治区桂林市秀峰区', 'XUF');
INSERT INTO `t_area` VALUES ('2473', '255', '叠彩区', '21-255-2473', '3', 'Diecai Qu', '广西壮族自治区桂林市叠彩区', 'DCA');
INSERT INTO `t_area` VALUES ('2474', '255', '象山区', '21-255-2474', '3', 'Xiangshan Qu', '广西壮族自治区桂林市象山区', 'XSK');
INSERT INTO `t_area` VALUES ('2475', '255', '七星区', '21-255-2475', '3', 'Qixing Qu', '广西壮族自治区桂林市七星区', 'QXG');
INSERT INTO `t_area` VALUES ('2476', '255', '雁山区', '21-255-2476', '3', 'Yanshan Qu', '广西壮族自治区桂林市雁山区', 'YSA');
INSERT INTO `t_area` VALUES ('2477', '255', '阳朔县', '21-255-2477', '3', 'Yangshuo Xian', '广西壮族自治区桂林市阳朔县', 'YSO');
INSERT INTO `t_area` VALUES ('2478', '255', '临桂县', '21-255-2478', '3', 'Lingui Xian', '广西壮族自治区桂林市临桂县', 'LGI');
INSERT INTO `t_area` VALUES ('2479', '255', '灵川县', '21-255-2479', '3', 'Lingchuan Xian', '广西壮族自治区桂林市灵川县', 'LCU');
INSERT INTO `t_area` VALUES ('2480', '255', '全州县', '21-255-2480', '3', 'Quanzhou Xian', '广西壮族自治区桂林市全州县', 'QZO');
INSERT INTO `t_area` VALUES ('2481', '255', '兴安县', '21-255-2481', '3', 'Xing,an Xian', '广西壮族自治区桂林市兴安县', 'XAG');
INSERT INTO `t_area` VALUES ('2482', '255', '永福县', '21-255-2482', '3', 'Yongfu Xian', '广西壮族自治区桂林市永福县', 'YFU');
INSERT INTO `t_area` VALUES ('2483', '255', '灌阳县', '21-255-2483', '3', 'Guanyang Xian', '广西壮族自治区桂林市灌阳县', 'GNY');
INSERT INTO `t_area` VALUES ('2484', '255', '龙胜各族自治县', '21-255-2484', '3', 'Longsheng Gezu Zizhixian', '广西壮族自治区桂林市龙胜各族自治县', 'LSG');
INSERT INTO `t_area` VALUES ('2485', '255', '资源县', '21-255-2485', '3', 'Ziyuan Xian', '广西壮族自治区桂林市资源县', 'ZYU');
INSERT INTO `t_area` VALUES ('2486', '255', '平乐县', '21-255-2486', '3', 'Pingle Xian', '广西壮族自治区桂林市平乐县', 'PLE');
INSERT INTO `t_area` VALUES ('2487', '255', '荔蒲县', '21-255-2487', '3', 'Lipu Xian', '广西壮族自治区桂林市荔蒲县', '2');
INSERT INTO `t_area` VALUES ('2488', '255', '恭城瑶族自治县', '21-255-2488', '3', 'Gongcheng Yaozu Zizhixian', '广西壮族自治区桂林市恭城瑶族自治县', 'GGC');
INSERT INTO `t_area` VALUES ('2490', '256', '万秀区', '21-256-2490', '3', 'Wanxiu Qu', '广西壮族自治区梧州市万秀区', 'WXQ');
INSERT INTO `t_area` VALUES ('2491', '256', '蝶山区', '21-256-2491', '3', 'Dieshan Qu', '广西壮族自治区梧州市蝶山区', 'DES');
INSERT INTO `t_area` VALUES ('2492', '256', '长洲区', '21-256-2492', '3', 'Changzhou Qu', '广西壮族自治区梧州市长洲区', '2');
INSERT INTO `t_area` VALUES ('2493', '256', '苍梧县', '21-256-2493', '3', 'Cangwu Xian', '广西壮族自治区梧州市苍梧县', 'CAW');
INSERT INTO `t_area` VALUES ('2494', '256', '藤县', '21-256-2494', '3', 'Teng Xian', '广西壮族自治区梧州市藤县', '2');
INSERT INTO `t_area` VALUES ('2495', '256', '蒙山县', '21-256-2495', '3', 'Mengshan Xian', '广西壮族自治区梧州市蒙山县', 'MSA');
INSERT INTO `t_area` VALUES ('2496', '256', '岑溪市', '21-256-2496', '3', 'Cenxi Shi', '广西壮族自治区梧州市岑溪市', 'CEX');
INSERT INTO `t_area` VALUES ('2498', '257', '海城区', '21-257-2498', '3', 'Haicheng Qu', '广西壮族自治区北海市海城区', 'HCB');
INSERT INTO `t_area` VALUES ('2499', '257', '银海区', '21-257-2499', '3', 'Yinhai Qu', '广西壮族自治区北海市银海区', 'YHB');
INSERT INTO `t_area` VALUES ('2500', '257', '铁山港区', '21-257-2500', '3', 'Tieshangangqu ', '广西壮族自治区北海市铁山港区', 'TSG');
INSERT INTO `t_area` VALUES ('2501', '257', '合浦县', '21-257-2501', '3', 'Hepu Xian', '广西壮族自治区北海市合浦县', 'HPX');
INSERT INTO `t_area` VALUES ('2503', '258', '港口区', '21-258-2503', '3', 'Gangkou Qu', '广西壮族自治区防城港市港口区', 'GKQ');
INSERT INTO `t_area` VALUES ('2504', '258', '防城区', '21-258-2504', '3', 'Fangcheng Qu', '广西壮族自治区防城港市防城区', 'FCQ');
INSERT INTO `t_area` VALUES ('2505', '258', '上思县', '21-258-2505', '3', 'Shangsi Xian', '广西壮族自治区防城港市上思县', 'SGS');
INSERT INTO `t_area` VALUES ('2506', '258', '东兴市', '21-258-2506', '3', 'Dongxing Shi', '广西壮族自治区防城港市东兴市', 'DOX');
INSERT INTO `t_area` VALUES ('2508', '259', '钦南区', '21-259-2508', '3', 'Qinnan Qu', '广西壮族自治区钦州市钦南区', 'QNQ');
INSERT INTO `t_area` VALUES ('2509', '259', '钦北区', '21-259-2509', '3', 'Qinbei Qu', '广西壮族自治区钦州市钦北区', 'QBQ');
INSERT INTO `t_area` VALUES ('2510', '259', '灵山县', '21-259-2510', '3', 'Lingshan Xian', '广西壮族自治区钦州市灵山县', 'LSB');
INSERT INTO `t_area` VALUES ('2511', '259', '浦北县', '21-259-2511', '3', 'Pubei Xian', '广西壮族自治区钦州市浦北县', 'PBE');
INSERT INTO `t_area` VALUES ('2513', '260', '港北区', '21-260-2513', '3', 'Gangbei Qu', '广西壮族自治区贵港市港北区', 'GBE');
INSERT INTO `t_area` VALUES ('2514', '260', '港南区', '21-260-2514', '3', 'Gangnan Qu', '广西壮族自治区贵港市港南区', 'GNQ');
INSERT INTO `t_area` VALUES ('2515', '260', '覃塘区', '21-260-2515', '3', 'Tantang Qu', '广西壮族自治区贵港市覃塘区', '2');
INSERT INTO `t_area` VALUES ('2516', '260', '平南县', '21-260-2516', '3', 'Pingnan Xian', '广西壮族自治区贵港市平南县', 'PNN');
INSERT INTO `t_area` VALUES ('2517', '260', '桂平市', '21-260-2517', '3', 'Guiping Shi', '广西壮族自治区贵港市桂平市', 'GPS');
INSERT INTO `t_area` VALUES ('2519', '261', '玉州区', '21-261-2519', '3', 'Yuzhou Qu', '广西壮族自治区玉林市玉州区', 'YZO');
INSERT INTO `t_area` VALUES ('2520', '261', '容县', '21-261-2520', '3', 'Rong Xian', '广西壮族自治区玉林市容县', 'ROG');
INSERT INTO `t_area` VALUES ('2521', '261', '陆川县', '21-261-2521', '3', 'Luchuan Xian', '广西壮族自治区玉林市陆川县', 'LCJ');
INSERT INTO `t_area` VALUES ('2522', '261', '博白县', '21-261-2522', '3', 'Bobai Xian', '广西壮族自治区玉林市博白县', 'BBA');
INSERT INTO `t_area` VALUES ('2523', '261', '兴业县', '21-261-2523', '3', 'Xingye Xian', '广西壮族自治区玉林市兴业县', 'XGY');
INSERT INTO `t_area` VALUES ('2524', '261', '北流市', '21-261-2524', '3', 'Beiliu Shi', '广西壮族自治区玉林市北流市', 'BLS');
INSERT INTO `t_area` VALUES ('2526', '262', '右江区', '21-262-2526', '3', 'Youjiang Qu', '广西壮族自治区百色市右江区', '2');
INSERT INTO `t_area` VALUES ('2527', '262', '田阳县', '21-262-2527', '3', 'Tianyang Xian', '广西壮族自治区百色市田阳县', '2');
INSERT INTO `t_area` VALUES ('2528', '262', '田东县', '21-262-2528', '3', 'Tiandong Xian', '广西壮族自治区百色市田东县', '2');
INSERT INTO `t_area` VALUES ('2529', '262', '平果县', '21-262-2529', '3', 'Pingguo Xian', '广西壮族自治区百色市平果县', '2');
INSERT INTO `t_area` VALUES ('2530', '262', '德保县', '21-262-2530', '3', 'Debao Xian', '广西壮族自治区百色市德保县', '2');
INSERT INTO `t_area` VALUES ('2531', '262', '靖西县', '21-262-2531', '3', 'Jingxi Xian', '广西壮族自治区百色市靖西县', '2');
INSERT INTO `t_area` VALUES ('2532', '262', '那坡县', '21-262-2532', '3', 'Napo Xian', '广西壮族自治区百色市那坡县', '2');
INSERT INTO `t_area` VALUES ('2533', '262', '凌云县', '21-262-2533', '3', 'Lingyun Xian', '广西壮族自治区百色市凌云县', '2');
INSERT INTO `t_area` VALUES ('2534', '262', '乐业县', '21-262-2534', '3', 'Leye Xian', '广西壮族自治区百色市乐业县', '2');
INSERT INTO `t_area` VALUES ('2535', '262', '田林县', '21-262-2535', '3', 'Tianlin Xian', '广西壮族自治区百色市田林县', '2');
INSERT INTO `t_area` VALUES ('2536', '262', '西林县', '21-262-2536', '3', 'Xilin Xian', '广西壮族自治区百色市西林县', '2');
INSERT INTO `t_area` VALUES ('2537', '262', '隆林各族自治县', '21-262-2537', '3', 'Longlin Gezu Zizhixian', '广西壮族自治区百色市隆林各族自治县', '2');
INSERT INTO `t_area` VALUES ('2539', '263', '八步区', '21-263-2539', '3', 'Babu Qu', '广西壮族自治区贺州市八步区', '2');
INSERT INTO `t_area` VALUES ('2540', '263', '昭平县', '21-263-2540', '3', 'Zhaoping Xian', '广西壮族自治区贺州市昭平县', '2');
INSERT INTO `t_area` VALUES ('2541', '263', '钟山县', '21-263-2541', '3', 'Zhongshan Xian', '广西壮族自治区贺州市钟山县', '2');
INSERT INTO `t_area` VALUES ('2542', '263', '富川瑶族自治县', '21-263-2542', '3', 'Fuchuan Yaozu Zizhixian', '广西壮族自治区贺州市富川瑶族自治县', '2');
INSERT INTO `t_area` VALUES ('2544', '264', '金城江区', '21-264-2544', '3', 'Jinchengjiang Qu', '广西壮族自治区河池市金城江区', '2');
INSERT INTO `t_area` VALUES ('2545', '264', '南丹县', '21-264-2545', '3', 'Nandan Xian', '广西壮族自治区河池市南丹县', '2');
INSERT INTO `t_area` VALUES ('2546', '264', '天峨县', '21-264-2546', '3', 'Tian,e Xian', '广西壮族自治区河池市天峨县', '2');
INSERT INTO `t_area` VALUES ('2547', '264', '凤山县', '21-264-2547', '3', 'Fengshan Xian', '广西壮族自治区河池市凤山县', '2');
INSERT INTO `t_area` VALUES ('2548', '264', '东兰县', '21-264-2548', '3', 'Donglan Xian', '广西壮族自治区河池市东兰县', '2');
INSERT INTO `t_area` VALUES ('2549', '264', '罗城仫佬族自治县', '21-264-2549', '3', 'Luocheng Mulaozu Zizhixian', '广西壮族自治区河池市罗城仫佬族自治县', '2');
INSERT INTO `t_area` VALUES ('2550', '264', '环江毛南族自治县', '21-264-2550', '3', 'Huanjiang Maonanzu Zizhixian', '广西壮族自治区河池市环江毛南族自治县', '2');
INSERT INTO `t_area` VALUES ('2551', '264', '巴马瑶族自治县', '21-264-2551', '3', 'Bama Yaozu Zizhixian', '广西壮族自治区河池市巴马瑶族自治县', '2');
INSERT INTO `t_area` VALUES ('2552', '264', '都安瑶族自治县', '21-264-2552', '3', 'Du,an Yaozu Zizhixian', '广西壮族自治区河池市都安瑶族自治县', '2');
INSERT INTO `t_area` VALUES ('2553', '264', '大化瑶族自治县', '21-264-2553', '3', 'Dahua Yaozu Zizhixian', '广西壮族自治区河池市大化瑶族自治县', '2');
INSERT INTO `t_area` VALUES ('2554', '264', '宜州市', '21-264-2554', '3', 'Yizhou Shi', '广西壮族自治区河池市宜州市', '2');
INSERT INTO `t_area` VALUES ('2556', '265', '兴宾区', '21-265-2556', '3', 'Xingbin Qu', '广西壮族自治区来宾市兴宾区', '2');
INSERT INTO `t_area` VALUES ('2557', '265', '忻城县', '21-265-2557', '3', 'Xincheng Xian', '广西壮族自治区来宾市忻城县', '2');
INSERT INTO `t_area` VALUES ('2558', '265', '象州县', '21-265-2558', '3', 'Xiangzhou Xian', '广西壮族自治区来宾市象州县', '2');
INSERT INTO `t_area` VALUES ('2559', '265', '武宣县', '21-265-2559', '3', 'Wuxuan Xian', '广西壮族自治区来宾市武宣县', '2');
INSERT INTO `t_area` VALUES ('2560', '265', '金秀瑶族自治县', '21-265-2560', '3', 'Jinxiu Yaozu Zizhixian', '广西壮族自治区来宾市金秀瑶族自治县', '2');
INSERT INTO `t_area` VALUES ('2561', '265', '合山市', '21-265-2561', '3', 'Heshan Shi', '广西壮族自治区来宾市合山市', '2');
INSERT INTO `t_area` VALUES ('2563', '266', '江洲区', '21-266-2563', '3', 'Jiangzhou Qu', '广西壮族自治区崇左市江洲区', '2');
INSERT INTO `t_area` VALUES ('2564', '266', '扶绥县', '21-266-2564', '3', 'Fusui Xian', '广西壮族自治区崇左市扶绥县', '2');
INSERT INTO `t_area` VALUES ('2565', '266', '宁明县', '21-266-2565', '3', 'Ningming Xian', '广西壮族自治区崇左市宁明县', '2');
INSERT INTO `t_area` VALUES ('2566', '266', '龙州县', '21-266-2566', '3', 'Longzhou Xian', '广西壮族自治区崇左市龙州县', '2');
INSERT INTO `t_area` VALUES ('2567', '266', '大新县', '21-266-2567', '3', 'Daxin Xian', '广西壮族自治区崇左市大新县', '2');
INSERT INTO `t_area` VALUES ('2568', '266', '天等县', '21-266-2568', '3', 'Tiandeng Xian', '广西壮族自治区崇左市天等县', '2');
INSERT INTO `t_area` VALUES ('2569', '266', '凭祥市', '21-266-2569', '3', 'Pingxiang Shi', '广西壮族自治区崇左市凭祥市', '2');
INSERT INTO `t_area` VALUES ('2571', '267', '秀英区', '22-267-2571', '3', 'Xiuying Qu', '海南省海口市秀英区', 'XYH');
INSERT INTO `t_area` VALUES ('2572', '267', '龙华区', '22-267-2572', '3', 'LongHua Qu', '海南省海口市龙华区', 'LH');
INSERT INTO `t_area` VALUES ('2573', '267', '琼山区', '22-267-2573', '3', 'QiongShan Qu', '海南省海口市琼山区', 'QS');
INSERT INTO `t_area` VALUES ('2574', '267', '美兰区', '22-267-2574', '3', 'MeiLan Qu', '海南省海口市美兰区', 'ML');
INSERT INTO `t_area` VALUES ('2576', '22', '五指山市', '22-2576', '2', 'Wuzhishan Qu', '海南省五指山市', '2');
INSERT INTO `t_area` VALUES ('2577', '22', '琼海市', '22-2577', '2', 'Qionghai Shi', '海南省琼海市', '2');
INSERT INTO `t_area` VALUES ('2578', '22', '儋州市', '22-2578', '2', 'Danzhou Shi', '海南省儋州市', '2');
INSERT INTO `t_area` VALUES ('2579', '22', '文昌市', '22-2579', '2', 'Wenchang Shi', '海南省文昌市', '2');
INSERT INTO `t_area` VALUES ('2580', '22', '万宁市', '22-2580', '2', 'Wanning Shi', '海南省万宁市', '2');
INSERT INTO `t_area` VALUES ('2581', '22', '东方市', '22-2581', '2', 'Dongfang Shi', '海南省东方市', '2');
INSERT INTO `t_area` VALUES ('2582', '22', '定安县', '22-2582', '2', 'Ding,an Xian', '海南省定安县', '2');
INSERT INTO `t_area` VALUES ('2583', '22', '屯昌县', '22-2583', '2', 'Tunchang Xian', '海南省屯昌县', '2');
INSERT INTO `t_area` VALUES ('2584', '22', '澄迈县', '22-2584', '2', 'Chengmai Xian', '海南省澄迈县', '2');
INSERT INTO `t_area` VALUES ('2585', '22', '临高县', '22-2585', '2', 'Lingao Xian', '海南省临高县', '2');
INSERT INTO `t_area` VALUES ('2586', '22', '白沙黎族自治县', '22-2586', '2', 'Baisha Lizu Zizhixian', '海南省白沙黎族自治县', '2');
INSERT INTO `t_area` VALUES ('2587', '22', '昌江黎族自治县', '22-2587', '2', 'Changjiang Lizu Zizhixian', '海南省昌江黎族自治县', '2');
INSERT INTO `t_area` VALUES ('2588', '22', '乐东黎族自治县', '22-2588', '2', 'Ledong Lizu Zizhixian', '海南省乐东黎族自治县', '2');
INSERT INTO `t_area` VALUES ('2589', '22', '陵水黎族自治县', '22-2589', '2', 'Lingshui Lizu Zizhixian', '海南省陵水黎族自治县', '2');
INSERT INTO `t_area` VALUES ('2590', '22', '保亭黎族苗族自治县', '22-2590', '2', 'Baoting Lizu Miaozu Zizhixian', '海南省保亭黎族苗族自治县', '2');
INSERT INTO `t_area` VALUES ('2591', '22', '琼中黎族苗族自治县', '22-2591', '2', 'Qiongzhong Lizu Miaozu Zizhixian', '海南省琼中黎族苗族自治县', '2');
INSERT INTO `t_area` VALUES ('2592', '22', '西沙群岛', '22-2592', '2', 'Xisha Qundao', '海南省西沙群岛', '2');
INSERT INTO `t_area` VALUES ('2593', '22', '南沙群岛', '22-2593', '2', 'Nansha Qundao', '海南省南沙群岛', '2');
INSERT INTO `t_area` VALUES ('2594', '22', '中沙群岛的岛礁及其海域', '22-2594', '2', 'Zhongsha Qundao de Daojiao Jiqi Haiyu', '海南省中沙群岛的岛礁及其海域', '2');
INSERT INTO `t_area` VALUES ('2595', '5004', '万州区', '23-5004-2595', '3', 'Wanzhou Qu', '重庆市重庆市万州区', 'WZO ');
INSERT INTO `t_area` VALUES ('2596', '5004', '涪陵区', '23-5004-2596', '3', 'Fuling Qu', '重庆市重庆市涪陵区', 'FLG');
INSERT INTO `t_area` VALUES ('2597', '5004', '渝中区', '23-5004-2597', '3', 'Yuzhong Qu', '重庆市重庆市渝中区', 'YZQ');
INSERT INTO `t_area` VALUES ('2598', '5004', '大渡口区', '23-5004-2598', '3', 'Dadukou Qu', '重庆市重庆市大渡口区', 'DDK');
INSERT INTO `t_area` VALUES ('2599', '5004', '江北区', '23-5004-2599', '3', 'Jiangbei Qu', '重庆市重庆市江北区', 'JBE');
INSERT INTO `t_area` VALUES ('2600', '5004', '沙坪坝区', '23-5004-2600', '3', 'Shapingba Qu', '重庆市重庆市沙坪坝区', 'SPB');
INSERT INTO `t_area` VALUES ('2601', '5004', '九龙坡区', '23-5004-2601', '3', 'Jiulongpo Qu', '重庆市重庆市九龙坡区', 'JLP');
INSERT INTO `t_area` VALUES ('2602', '5004', '南岸区', '23-5004-2602', '3', 'Nan,an Qu', '重庆市重庆市南岸区', 'NAQ');
INSERT INTO `t_area` VALUES ('2603', '5004', '北碚区', '23-5004-2603', '3', 'Beibei Qu', '重庆市重庆市北碚区', 'BBE');
INSERT INTO `t_area` VALUES ('2604', '5004', '万盛区', '23-5004-2604', '3', 'Wansheng Qu', '重庆市重庆市万盛区', 'WSQ');
INSERT INTO `t_area` VALUES ('2605', '5004', '双桥区', '23-5004-2605', '3', 'Shuangqiao Qu', '重庆市重庆市双桥区', 'SQQ');
INSERT INTO `t_area` VALUES ('2606', '5004', '渝北区', '23-5004-2606', '3', 'Yubei Qu', '重庆市重庆市渝北区', 'YBE');
INSERT INTO `t_area` VALUES ('2607', '5004', '巴南区', '23-5004-2607', '3', 'Banan Qu', '重庆市重庆市巴南区', 'BNN');
INSERT INTO `t_area` VALUES ('2608', '5004', '黔江区', '23-5004-2608', '3', 'Qianjiang Qu', '重庆市重庆市黔江区', 'QJQ');
INSERT INTO `t_area` VALUES ('2609', '5004', '长寿区', '23-5004-2609', '3', 'Changshou Qu', '重庆市重庆市长寿区', 'CSQ');
INSERT INTO `t_area` VALUES ('2610', '5004', '綦江县', '23-5004-2610', '3', 'Qijiang Xian', '重庆市重庆市綦江县', 'QJG');
INSERT INTO `t_area` VALUES ('2611', '5004', '潼南县', '23-5004-2611', '3', 'Tongnan Xian', '重庆市重庆市潼南县', 'TNN');
INSERT INTO `t_area` VALUES ('2612', '5004', '铜梁县', '23-5004-2612', '3', 'Tongliang Xian', '重庆市重庆市铜梁县', 'TGL');
INSERT INTO `t_area` VALUES ('2613', '5004', '大足县', '23-5004-2613', '3', 'Dazu Xian', '重庆市重庆市大足县', 'DZX');
INSERT INTO `t_area` VALUES ('2614', '5004', '荣昌县', '23-5004-2614', '3', 'Rongchang Xian', '重庆市重庆市荣昌县', 'RGC');
INSERT INTO `t_area` VALUES ('2615', '5004', '璧山县', '23-5004-2615', '3', 'Bishan Xian', '重庆市重庆市璧山县', 'BSY');
INSERT INTO `t_area` VALUES ('2616', '5004', '梁平县', '23-5004-2616', '3', 'Liangping Xian', '重庆市重庆市梁平县', 'LGP');
INSERT INTO `t_area` VALUES ('2617', '5004', '城口县', '23-5004-2617', '3', 'Chengkou Xian', '重庆市重庆市城口县', 'CKO');
INSERT INTO `t_area` VALUES ('2618', '5004', '丰都县', '23-5004-2618', '3', 'Fengdu Xian', '重庆市重庆市丰都县', 'FDU');
INSERT INTO `t_area` VALUES ('2619', '5004', '垫江县', '23-5004-2619', '3', 'Dianjiang Xian', '重庆市重庆市垫江县', 'DJG');
INSERT INTO `t_area` VALUES ('2620', '5004', '武隆县', '23-5004-2620', '3', 'Wulong Xian', '重庆市重庆市武隆县', 'WLG');
INSERT INTO `t_area` VALUES ('2621', '5004', '忠县', '23-5004-2621', '3', 'Zhong Xian', '重庆市重庆市忠县', 'ZHX');
INSERT INTO `t_area` VALUES ('2622', '5004', '开县', '23-5004-2622', '3', 'Kai Xian', '重庆市重庆市开县', 'KAI');
INSERT INTO `t_area` VALUES ('2623', '5004', '云阳县', '23-5004-2623', '3', 'Yunyang Xian', '重庆市重庆市云阳县', 'YNY');
INSERT INTO `t_area` VALUES ('2624', '5004', '奉节县', '23-5004-2624', '3', 'Fengjie Xian', '重庆市重庆市奉节县', 'FJE');
INSERT INTO `t_area` VALUES ('2625', '5004', '巫山县', '23-5004-2625', '3', 'Wushan Xian', '重庆市重庆市巫山县', 'WSN');
INSERT INTO `t_area` VALUES ('2626', '5004', '巫溪县', '23-5004-2626', '3', 'Wuxi Xian', '重庆市重庆市巫溪县', 'WXX');
INSERT INTO `t_area` VALUES ('2627', '5004', '石柱土家族自治县', '23-5004-2627', '3', 'Shizhu Tujiazu Zizhixian', '重庆市重庆市石柱土家族自治县', 'SZY');
INSERT INTO `t_area` VALUES ('2628', '5004', '秀山土家族苗族自治县', '23-5004-2628', '3', 'Xiushan Tujiazu Miaozu Zizhixian', '重庆市重庆市秀山土家族苗族自治县', 'XUS');
INSERT INTO `t_area` VALUES ('2629', '5004', '酉阳土家族苗族自治县', '23-5004-2629', '3', 'Youyang Tujiazu Miaozu Zizhixian', '重庆市重庆市酉阳土家族苗族自治县', 'YUY');
INSERT INTO `t_area` VALUES ('2630', '5004', '彭水苗族土家族自治县', '23-5004-2630', '3', 'Pengshui Miaozu Tujiazu Zizhixian', '重庆市重庆市彭水苗族土家族自治县', 'PSU');
INSERT INTO `t_area` VALUES ('2636', '273', '锦江区', '24-273-2636', '3', 'Jinjiang Qu', '四川省成都市锦江区', 'JJQ');
INSERT INTO `t_area` VALUES ('2637', '273', '青羊区', '24-273-2637', '3', 'Qingyang Qu', '四川省成都市青羊区', 'QYQ');
INSERT INTO `t_area` VALUES ('2638', '273', '金牛区', '24-273-2638', '3', 'Jinniu Qu', '四川省成都市金牛区', 'JNU');
INSERT INTO `t_area` VALUES ('2639', '273', '武侯区', '24-273-2639', '3', 'Wuhou Qu', '四川省成都市武侯区', 'WHQ');
INSERT INTO `t_area` VALUES ('2640', '273', '成华区', '24-273-2640', '3', 'Chenghua Qu', '四川省成都市成华区', 'CHQ');
INSERT INTO `t_area` VALUES ('2641', '273', '龙泉驿区', '24-273-2641', '3', 'Longquanyi Qu', '四川省成都市龙泉驿区', 'LQY');
INSERT INTO `t_area` VALUES ('2642', '273', '青白江区', '24-273-2642', '3', 'Qingbaijiang Qu', '四川省成都市青白江区', 'QBJ');
INSERT INTO `t_area` VALUES ('2643', '273', '新都区', '24-273-2643', '3', 'Xindu Qu', '四川省成都市新都区', '2');
INSERT INTO `t_area` VALUES ('2644', '273', '温江区', '24-273-2644', '3', 'Wenjiang Qu', '四川省成都市温江区', '2');
INSERT INTO `t_area` VALUES ('2645', '273', '金堂县', '24-273-2645', '3', 'Jintang Xian', '四川省成都市金堂县', 'JNT');
INSERT INTO `t_area` VALUES ('2646', '273', '双流县', '24-273-2646', '3', 'Shuangliu Xian', '四川省成都市双流县', 'SLU');
INSERT INTO `t_area` VALUES ('2647', '273', '郫县', '24-273-2647', '3', 'Pi Xian', '四川省成都市郫县', 'PIX');
INSERT INTO `t_area` VALUES ('2648', '273', '大邑县', '24-273-2648', '3', 'Dayi Xian', '四川省成都市大邑县', 'DYI');
INSERT INTO `t_area` VALUES ('2649', '273', '蒲江县', '24-273-2649', '3', 'Pujiang Xian', '四川省成都市蒲江县', 'PJX');
INSERT INTO `t_area` VALUES ('2650', '273', '新津县', '24-273-2650', '3', 'Xinjin Xian', '四川省成都市新津县', 'XJC');
INSERT INTO `t_area` VALUES ('2651', '273', '都江堰市', '24-273-2651', '3', 'Dujiangyan Shi', '四川省成都市都江堰市', 'DJY');
INSERT INTO `t_area` VALUES ('2652', '273', '彭州市', '24-273-2652', '3', 'Pengzhou Shi', '四川省成都市彭州市', 'PZS');
INSERT INTO `t_area` VALUES ('2653', '273', '邛崃市', '24-273-2653', '3', 'Qionglai Shi', '四川省成都市邛崃市', 'QLA');
INSERT INTO `t_area` VALUES ('2654', '273', '崇州市', '24-273-2654', '3', 'Chongzhou Shi', '四川省成都市崇州市', 'CZO');
INSERT INTO `t_area` VALUES ('2656', '274', '自流井区', '24-274-2656', '3', 'Ziliujing Qu', '四川省自贡市自流井区', 'ZLJ');
INSERT INTO `t_area` VALUES ('2657', '274', '贡井区', '24-274-2657', '3', 'Gongjing Qu', '四川省自贡市贡井区', '2');
INSERT INTO `t_area` VALUES ('2658', '274', '大安区', '24-274-2658', '3', 'Da,an Qu', '四川省自贡市大安区', 'DAQ');
INSERT INTO `t_area` VALUES ('2659', '274', '沿滩区', '24-274-2659', '3', 'Yantan Qu', '四川省自贡市沿滩区', 'YTN');
INSERT INTO `t_area` VALUES ('2660', '274', '荣县', '24-274-2660', '3', 'Rong Xian', '四川省自贡市荣县', 'RGX');
INSERT INTO `t_area` VALUES ('2661', '274', '富顺县', '24-274-2661', '3', 'Fushun Xian', '四川省自贡市富顺县', 'FSH');
INSERT INTO `t_area` VALUES ('2663', '275', '东区', '24-275-2663', '3', 'Dong Qu', '四川省攀枝花市东区', 'DQP');
INSERT INTO `t_area` VALUES ('2664', '275', '西区', '24-275-2664', '3', 'Xi Qu', '四川省攀枝花市西区', 'XIQ');
INSERT INTO `t_area` VALUES ('2665', '275', '仁和区', '24-275-2665', '3', 'Renhe Qu', '四川省攀枝花市仁和区', 'RHQ');
INSERT INTO `t_area` VALUES ('2666', '275', '米易县', '24-275-2666', '3', 'Miyi Xian', '四川省攀枝花市米易县', 'MIY');
INSERT INTO `t_area` VALUES ('2667', '275', '盐边县', '24-275-2667', '3', 'Yanbian Xian', '四川省攀枝花市盐边县', 'YBN');
INSERT INTO `t_area` VALUES ('2669', '276', '江阳区', '24-276-2669', '3', 'Jiangyang Qu', '四川省泸州市江阳区', 'JYB');
INSERT INTO `t_area` VALUES ('2670', '276', '纳溪区', '24-276-2670', '3', 'Naxi Qu', '四川省泸州市纳溪区', 'NXI');
INSERT INTO `t_area` VALUES ('2671', '276', '龙马潭区', '24-276-2671', '3', 'Longmatan Qu', '四川省泸州市龙马潭区', 'LMT');
INSERT INTO `t_area` VALUES ('2672', '276', '泸县', '24-276-2672', '3', 'Lu Xian', '四川省泸州市泸县', 'LUX');
INSERT INTO `t_area` VALUES ('2673', '276', '合江县', '24-276-2673', '3', 'Hejiang Xian', '四川省泸州市合江县', 'HEJ');
INSERT INTO `t_area` VALUES ('2674', '276', '叙永县', '24-276-2674', '3', 'Xuyong Xian', '四川省泸州市叙永县', 'XYO');
INSERT INTO `t_area` VALUES ('2675', '276', '古蔺县', '24-276-2675', '3', 'Gulin Xian', '四川省泸州市古蔺县', 'GUL');
INSERT INTO `t_area` VALUES ('2677', '277', '旌阳区', '24-277-2677', '3', 'Jingyang Qu', '四川省德阳市旌阳区', 'JYF');
INSERT INTO `t_area` VALUES ('2678', '277', '中江县', '24-277-2678', '3', 'Zhongjiang Xian', '四川省德阳市中江县', 'ZGJ');
INSERT INTO `t_area` VALUES ('2679', '277', '罗江县', '24-277-2679', '3', 'Luojiang Xian', '四川省德阳市罗江县', 'LOJ');
INSERT INTO `t_area` VALUES ('2680', '277', '广汉市', '24-277-2680', '3', 'Guanghan Shi', '四川省德阳市广汉市', 'GHN');
INSERT INTO `t_area` VALUES ('2681', '277', '什邡市', '24-277-2681', '3', 'Shifang Shi', '四川省德阳市什邡市', 'SFS');
INSERT INTO `t_area` VALUES ('2682', '277', '绵竹市', '24-277-2682', '3', 'Jinzhou Shi', '四川省德阳市绵竹市', 'MZU');
INSERT INTO `t_area` VALUES ('2684', '278', '涪城区', '24-278-2684', '3', 'Fucheng Qu', '四川省绵阳市涪城区', 'FCM');
INSERT INTO `t_area` VALUES ('2685', '278', '游仙区', '24-278-2685', '3', 'Youxian Qu', '四川省绵阳市游仙区', 'YXM');
INSERT INTO `t_area` VALUES ('2686', '278', '三台县', '24-278-2686', '3', 'Santai Xian', '四川省绵阳市三台县', 'SNT');
INSERT INTO `t_area` VALUES ('2687', '278', '盐亭县', '24-278-2687', '3', 'Yanting Xian', '四川省绵阳市盐亭县', 'YTC');
INSERT INTO `t_area` VALUES ('2688', '278', '安县', '24-278-2688', '3', 'An Xian', '四川省绵阳市安县', 'AXN');
INSERT INTO `t_area` VALUES ('2689', '278', '梓潼县', '24-278-2689', '3', 'Zitong Xian', '四川省绵阳市梓潼县', 'ZTG');
INSERT INTO `t_area` VALUES ('2690', '278', '北川羌族自治县', '24-278-2690', '3', 'Beichuanqiangzuzizhi Qu', '四川省绵阳市北川羌族自治县', '2');
INSERT INTO `t_area` VALUES ('2691', '278', '平武县', '24-278-2691', '3', 'Pingwu Xian', '四川省绵阳市平武县', 'PWU');
INSERT INTO `t_area` VALUES ('2692', '278', '江油市', '24-278-2692', '3', 'Jiangyou Shi', '四川省绵阳市江油市', 'JYO');
INSERT INTO `t_area` VALUES ('2694', '279', '市中区', '24-279-2694', '3', 'Shizhong Qu', '四川省广元市市中区', 'SZM');
INSERT INTO `t_area` VALUES ('2695', '279', '元坝区', '24-279-2695', '3', 'Yuanba Qu', '四川省广元市元坝区', 'YBQ');
INSERT INTO `t_area` VALUES ('2696', '279', '朝天区', '24-279-2696', '3', 'Chaotian Qu', '四川省广元市朝天区', 'CTN');
INSERT INTO `t_area` VALUES ('2697', '279', '旺苍县', '24-279-2697', '3', 'Wangcang Xian', '四川省广元市旺苍县', 'WGC');
INSERT INTO `t_area` VALUES ('2698', '279', '青川县', '24-279-2698', '3', 'Qingchuan Xian', '四川省广元市青川县', 'QCX');
INSERT INTO `t_area` VALUES ('2699', '279', '剑阁县', '24-279-2699', '3', 'Jiange Xian', '四川省广元市剑阁县', 'JGE');
INSERT INTO `t_area` VALUES ('2700', '279', '苍溪县', '24-279-2700', '3', 'Cangxi Xian', '四川省广元市苍溪县', 'CXC');
INSERT INTO `t_area` VALUES ('2702', '280', '船山区', '24-280-2702', '3', 'Chuanshan Qu', '四川省遂宁市船山区', '2');
INSERT INTO `t_area` VALUES ('2703', '280', '安居区', '24-280-2703', '3', 'Anju Qu', '四川省遂宁市安居区', '2');
INSERT INTO `t_area` VALUES ('2704', '280', '蓬溪县', '24-280-2704', '3', 'Pengxi Xian', '四川省遂宁市蓬溪县', 'PXI');
INSERT INTO `t_area` VALUES ('2705', '280', '射洪县', '24-280-2705', '3', 'Shehong Xian', '四川省遂宁市射洪县', 'SHE');
INSERT INTO `t_area` VALUES ('2706', '280', '大英县', '24-280-2706', '3', 'Daying Xian', '四川省遂宁市大英县', 'DAY');
INSERT INTO `t_area` VALUES ('2708', '281', '市中区', '24-281-2708', '3', 'Shizhong Qu', '四川省内江市市中区', 'SZM');
INSERT INTO `t_area` VALUES ('2709', '281', '东兴区', '24-281-2709', '3', 'Dongxing Qu', '四川省内江市东兴区', 'DXQ');
INSERT INTO `t_area` VALUES ('2710', '281', '威远县', '24-281-2710', '3', 'Weiyuan Xian', '四川省内江市威远县', 'WYU');
INSERT INTO `t_area` VALUES ('2711', '281', '资中县', '24-281-2711', '3', 'Zizhong Xian', '四川省内江市资中县', 'ZZC');
INSERT INTO `t_area` VALUES ('2712', '281', '隆昌县', '24-281-2712', '3', 'Longchang Xian', '四川省内江市隆昌县', 'LCC');
INSERT INTO `t_area` VALUES ('2714', '282', '市中区', '24-282-2714', '3', 'Shizhong Qu', '四川省乐山市市中区', 'SZP');
INSERT INTO `t_area` VALUES ('2715', '282', '沙湾区', '24-282-2715', '3', 'Shawan Qu', '四川省乐山市沙湾区', 'SWN');
INSERT INTO `t_area` VALUES ('2716', '282', '五通桥区', '24-282-2716', '3', 'Wutongqiao Qu', '四川省乐山市五通桥区', 'WTQ');
INSERT INTO `t_area` VALUES ('2717', '282', '金口河区', '24-282-2717', '3', 'Jinkouhe Qu', '四川省乐山市金口河区', 'JKH');
INSERT INTO `t_area` VALUES ('2718', '282', '犍为县', '24-282-2718', '3', 'Qianwei Xian', '四川省乐山市犍为县', 'QWE');
INSERT INTO `t_area` VALUES ('2719', '282', '井研县', '24-282-2719', '3', 'Jingyan Xian', '四川省乐山市井研县', 'JYA');
INSERT INTO `t_area` VALUES ('2720', '282', '夹江县', '24-282-2720', '3', 'Jiajiang Xian', '四川省乐山市夹江县', 'JJC');
INSERT INTO `t_area` VALUES ('2721', '282', '沐川县', '24-282-2721', '3', 'Muchuan Xian', '四川省乐山市沐川县', 'MCH');
INSERT INTO `t_area` VALUES ('2722', '282', '峨边彝族自治县', '24-282-2722', '3', 'Ebian Yizu Zizhixian', '四川省乐山市峨边彝族自治县', 'EBN');
INSERT INTO `t_area` VALUES ('2723', '282', '马边彝族自治县', '24-282-2723', '3', 'Mabian Yizu Zizhixian', '四川省乐山市马边彝族自治县', 'MBN');
INSERT INTO `t_area` VALUES ('2724', '282', '峨眉山市', '24-282-2724', '3', 'Emeishan Shi', '四川省乐山市峨眉山市', 'EMS');
INSERT INTO `t_area` VALUES ('2726', '283', '顺庆区', '24-283-2726', '3', 'Shunqing Xian', '四川省南充市顺庆区', 'SQG');
INSERT INTO `t_area` VALUES ('2727', '283', '高坪区', '24-283-2727', '3', 'Gaoping Qu', '四川省南充市高坪区', 'GPQ');
INSERT INTO `t_area` VALUES ('2728', '283', '嘉陵区', '24-283-2728', '3', 'Jialing Qu', '四川省南充市嘉陵区', 'JLG');
INSERT INTO `t_area` VALUES ('2729', '283', '南部县', '24-283-2729', '3', 'Nanbu Xian', '四川省南充市南部县', 'NBU');
INSERT INTO `t_area` VALUES ('2730', '283', '营山县', '24-283-2730', '3', 'Yingshan Xian', '四川省南充市营山县', 'YGS');
INSERT INTO `t_area` VALUES ('2731', '283', '蓬安县', '24-283-2731', '3', 'Peng,an Xian', '四川省南充市蓬安县', 'PGA');
INSERT INTO `t_area` VALUES ('2732', '283', '仪陇县', '24-283-2732', '3', 'Yilong Xian', '四川省南充市仪陇县', 'YLC');
INSERT INTO `t_area` VALUES ('2733', '283', '西充县', '24-283-2733', '3', 'Xichong Xian', '四川省南充市西充县', 'XCO');
INSERT INTO `t_area` VALUES ('2734', '283', '阆中市', '24-283-2734', '3', 'Langzhong Shi', '四川省南充市阆中市', 'LZJ');
INSERT INTO `t_area` VALUES ('2736', '284', '东坡区', '24-284-2736', '3', 'Dongpo Qu', '四川省眉山市东坡区', '2');
INSERT INTO `t_area` VALUES ('2737', '284', '仁寿县', '24-284-2737', '3', 'Renshou Xian', '四川省眉山市仁寿县', '2');
INSERT INTO `t_area` VALUES ('2738', '284', '彭山县', '24-284-2738', '3', 'Pengshan Xian', '四川省眉山市彭山县', '2');
INSERT INTO `t_area` VALUES ('2739', '284', '洪雅县', '24-284-2739', '3', 'Hongya Xian', '四川省眉山市洪雅县', '2');
INSERT INTO `t_area` VALUES ('2740', '284', '丹棱县', '24-284-2740', '3', 'Danling Xian', '四川省眉山市丹棱县', '2');
INSERT INTO `t_area` VALUES ('2741', '284', '青神县', '24-284-2741', '3', 'Qingshen Xian', '四川省眉山市青神县', '2');
INSERT INTO `t_area` VALUES ('2743', '285', '翠屏区', '24-285-2743', '3', 'Cuiping Qu', '四川省宜宾市翠屏区', 'CPQ');
INSERT INTO `t_area` VALUES ('2744', '285', '宜宾县', '24-285-2744', '3', 'Yibin Xian', '四川省宜宾市宜宾县', 'YBX');
INSERT INTO `t_area` VALUES ('2745', '285', '南溪县', '24-285-2745', '3', 'Nanxi Xian', '四川省宜宾市南溪县', 'NNX');
INSERT INTO `t_area` VALUES ('2746', '285', '江安县', '24-285-2746', '3', 'Jiang,an Xian', '四川省宜宾市江安县', 'JAC');
INSERT INTO `t_area` VALUES ('2747', '285', '长宁县', '24-285-2747', '3', 'Changning Xian', '四川省宜宾市长宁县', 'CNX');
INSERT INTO `t_area` VALUES ('2748', '285', '高县', '24-285-2748', '3', 'Gao Xian', '四川省宜宾市高县', 'GAO');
INSERT INTO `t_area` VALUES ('2749', '285', '珙县', '24-285-2749', '3', 'Gong Xian', '四川省宜宾市珙县', 'GOG');
INSERT INTO `t_area` VALUES ('2750', '285', '筠连县', '24-285-2750', '3', 'Junlian Xian', '四川省宜宾市筠连县', 'JNL');
INSERT INTO `t_area` VALUES ('2751', '285', '兴文县', '24-285-2751', '3', 'Xingwen Xian', '四川省宜宾市兴文县', 'XWC');
INSERT INTO `t_area` VALUES ('2752', '285', '屏山县', '24-285-2752', '3', 'Pingshan Xian', '四川省宜宾市屏山县', 'PSC');
INSERT INTO `t_area` VALUES ('2754', '286', '广安区', '24-286-2754', '3', 'Guang,an Qu', '四川省广安市广安区', 'GAQ');
INSERT INTO `t_area` VALUES ('2755', '286', '岳池县', '24-286-2755', '3', 'Yuechi Xian', '四川省广安市岳池县', 'YCC');
INSERT INTO `t_area` VALUES ('2756', '286', '武胜县', '24-286-2756', '3', 'Wusheng Xian', '四川省广安市武胜县', 'WSG');
INSERT INTO `t_area` VALUES ('2757', '286', '邻水县', '24-286-2757', '3', 'Linshui Xian', '四川省广安市邻水县', 'LSH');
INSERT INTO `t_area` VALUES ('2760', '287', '通川区', '24-287-2760', '3', 'Tongchuan Qu', '四川省达州市通川区', '2');
INSERT INTO `t_area` VALUES ('2761', '287', '达县', '24-287-2761', '3', 'Da Xian', '四川省达州市达县', '2');
INSERT INTO `t_area` VALUES ('2762', '287', '宣汉县', '24-287-2762', '3', 'Xuanhan Xian', '四川省达州市宣汉县', '2');
INSERT INTO `t_area` VALUES ('2763', '287', '开江县', '24-287-2763', '3', 'Kaijiang Xian', '四川省达州市开江县', '2');
INSERT INTO `t_area` VALUES ('2764', '287', '大竹县', '24-287-2764', '3', 'Dazhu Xian', '四川省达州市大竹县', '2');
INSERT INTO `t_area` VALUES ('2765', '287', '渠县', '24-287-2765', '3', 'Qu Xian', '四川省达州市渠县', '2');
INSERT INTO `t_area` VALUES ('2766', '287', '万源市', '24-287-2766', '3', 'Wanyuan Shi', '四川省达州市万源市', '2');
INSERT INTO `t_area` VALUES ('2768', '288', '雨城区', '24-288-2768', '3', 'Yucheg Qu', '四川省雅安市雨城区', '2');
INSERT INTO `t_area` VALUES ('2769', '288', '名山县', '24-288-2769', '3', 'Mingshan Xian', '四川省雅安市名山县', '2');
INSERT INTO `t_area` VALUES ('2770', '288', '荥经县', '24-288-2770', '3', 'Yingjing Xian', '四川省雅安市荥经县', '2');
INSERT INTO `t_area` VALUES ('2771', '288', '汉源县', '24-288-2771', '3', 'Hanyuan Xian', '四川省雅安市汉源县', '2');
INSERT INTO `t_area` VALUES ('2772', '288', '石棉县', '24-288-2772', '3', 'Shimian Xian', '四川省雅安市石棉县', '2');
INSERT INTO `t_area` VALUES ('2773', '288', '天全县', '24-288-2773', '3', 'Tianquan Xian', '四川省雅安市天全县', '2');
INSERT INTO `t_area` VALUES ('2774', '288', '芦山县', '24-288-2774', '3', 'Lushan Xian', '四川省雅安市芦山县', '2');
INSERT INTO `t_area` VALUES ('2775', '288', '宝兴县', '24-288-2775', '3', 'Baoxing Xian', '四川省雅安市宝兴县', '2');
INSERT INTO `t_area` VALUES ('2777', '289', '巴州区', '24-289-2777', '3', 'Bazhou Qu', '四川省巴中市巴州区', '2');
INSERT INTO `t_area` VALUES ('2778', '289', '通江县', '24-289-2778', '3', 'Tongjiang Xian', '四川省巴中市通江县', '2');
INSERT INTO `t_area` VALUES ('2779', '289', '南江县', '24-289-2779', '3', 'Nanjiang Xian', '四川省巴中市南江县', '2');
INSERT INTO `t_area` VALUES ('2780', '289', '平昌县', '24-289-2780', '3', 'Pingchang Xian', '四川省巴中市平昌县', '2');
INSERT INTO `t_area` VALUES ('2782', '290', '雁江区', '24-290-2782', '3', 'Yanjiang Qu', '四川省资阳市雁江区', '2');
INSERT INTO `t_area` VALUES ('2783', '290', '安岳县', '24-290-2783', '3', 'Anyue Xian', '四川省资阳市安岳县', '2');
INSERT INTO `t_area` VALUES ('2784', '290', '乐至县', '24-290-2784', '3', 'Lezhi Xian', '四川省资阳市乐至县', '2');
INSERT INTO `t_area` VALUES ('2785', '290', '简阳市', '24-290-2785', '3', 'Jianyang Shi', '四川省资阳市简阳市', '2');
INSERT INTO `t_area` VALUES ('2786', '291', '汶川县', '24-291-2786', '3', 'Wenchuan Xian', '四川省阿坝藏族羌族自治州汶川县', 'WNC');
INSERT INTO `t_area` VALUES ('2787', '291', '理县', '24-291-2787', '3', 'Li Xian', '四川省阿坝藏族羌族自治州理县', 'LXC');
INSERT INTO `t_area` VALUES ('2788', '291', '茂县', '24-291-2788', '3', 'Mao Xian', '四川省阿坝藏族羌族自治州茂县', 'MAO');
INSERT INTO `t_area` VALUES ('2789', '291', '松潘县', '24-291-2789', '3', 'Songpan Xian', '四川省阿坝藏族羌族自治州松潘县', 'SOP');
INSERT INTO `t_area` VALUES ('2790', '291', '九寨沟县', '24-291-2790', '3', 'Jiuzhaigou Xian', '四川省阿坝藏族羌族自治州九寨沟县', 'JZG');
INSERT INTO `t_area` VALUES ('2791', '291', '金川县', '24-291-2791', '3', 'Jinchuan Xian', '四川省阿坝藏族羌族自治州金川县', 'JCH');
INSERT INTO `t_area` VALUES ('2792', '291', '小金县', '24-291-2792', '3', 'Xiaojin Xian', '四川省阿坝藏族羌族自治州小金县', 'XJX');
INSERT INTO `t_area` VALUES ('2793', '291', '黑水县', '24-291-2793', '3', 'Heishui Xian', '四川省阿坝藏族羌族自治州黑水县', 'HIS');
INSERT INTO `t_area` VALUES ('2794', '291', '马尔康县', '24-291-2794', '3', 'Barkam Xian', '四川省阿坝藏族羌族自治州马尔康县', 'BAK');
INSERT INTO `t_area` VALUES ('2795', '291', '壤塘县', '24-291-2795', '3', 'Zamtang Xian', '四川省阿坝藏族羌族自治州壤塘县', 'ZAM');
INSERT INTO `t_area` VALUES ('2796', '291', '阿坝县', '24-291-2796', '3', 'Aba(Ngawa) Xian', '四川省阿坝藏族羌族自治州阿坝县', 'ABX');
INSERT INTO `t_area` VALUES ('2797', '291', '若尔盖县', '24-291-2797', '3', 'ZoigeXian', '四川省阿坝藏族羌族自治州若尔盖县', 'ZOI');
INSERT INTO `t_area` VALUES ('2798', '291', '红原县', '24-291-2798', '3', 'Hongyuan Xian', '四川省阿坝藏族羌族自治州红原县', 'HOY');
INSERT INTO `t_area` VALUES ('2799', '292', '康定县', '24-292-2799', '3', 'Kangding(Dardo) Xian', '四川省甘孜藏族自治州康定县', 'KDX');
INSERT INTO `t_area` VALUES ('2800', '292', '泸定县', '24-292-2800', '3', 'Luding(Jagsamka) Xian', '四川省甘孜藏族自治州泸定县', 'LUD');
INSERT INTO `t_area` VALUES ('2801', '292', '丹巴县', '24-292-2801', '3', 'Danba(Rongzhag) Xian', '四川省甘孜藏族自治州丹巴县', 'DBA');
INSERT INTO `t_area` VALUES ('2802', '292', '九龙县', '24-292-2802', '3', 'Jiulong(Gyaisi) Xian', '四川省甘孜藏族自治州九龙县', 'JLC');
INSERT INTO `t_area` VALUES ('2803', '292', '雅江县', '24-292-2803', '3', 'Yajiang(Nyagquka) Xian', '四川省甘孜藏族自治州雅江县', 'YAJ');
INSERT INTO `t_area` VALUES ('2804', '292', '道孚县', '24-292-2804', '3', 'Dawu Xian', '四川省甘孜藏族自治州道孚县', 'DAW');
INSERT INTO `t_area` VALUES ('2805', '292', '炉霍县', '24-292-2805', '3', 'Luhuo(Zhaggo) Xian', '四川省甘孜藏族自治州炉霍县', 'LUH');
INSERT INTO `t_area` VALUES ('2806', '292', '甘孜县', '24-292-2806', '3', 'Garze Xian', '四川省甘孜藏族自治州甘孜县', 'GRZ');
INSERT INTO `t_area` VALUES ('2807', '292', '新龙县', '24-292-2807', '3', 'Xinlong(Nyagrong) Xian', '四川省甘孜藏族自治州新龙县', 'XLG');
INSERT INTO `t_area` VALUES ('2808', '292', '德格县', '24-292-2808', '3', 'DegeXian', '四川省甘孜藏族自治州德格县', 'DEG');
INSERT INTO `t_area` VALUES ('2809', '292', '白玉县', '24-292-2809', '3', 'Baiyu Xian', '四川省甘孜藏族自治州白玉县', 'BYC');
INSERT INTO `t_area` VALUES ('2810', '292', '石渠县', '24-292-2810', '3', 'Serxv Xian', '四川省甘孜藏族自治州石渠县', 'SER');
INSERT INTO `t_area` VALUES ('2811', '292', '色达县', '24-292-2811', '3', 'Sertar Xian', '四川省甘孜藏族自治州色达县', 'STX');
INSERT INTO `t_area` VALUES ('2812', '292', '理塘县', '24-292-2812', '3', 'Litang Xian', '四川省甘孜藏族自治州理塘县', 'LIT');
INSERT INTO `t_area` VALUES ('2813', '292', '巴塘县', '24-292-2813', '3', 'Batang Xian', '四川省甘孜藏族自治州巴塘县', 'BTC');
INSERT INTO `t_area` VALUES ('2814', '292', '乡城县', '24-292-2814', '3', 'Xiangcheng(Qagcheng) Xian', '四川省甘孜藏族自治州乡城县', 'XCC');
INSERT INTO `t_area` VALUES ('2815', '292', '稻城县', '24-292-2815', '3', 'Daocheng(Dabba) Xian', '四川省甘孜藏族自治州稻城县', 'DCX');
INSERT INTO `t_area` VALUES ('2816', '292', '得荣县', '24-292-2816', '3', 'Derong Xian', '四川省甘孜藏族自治州得荣县', 'DER');
INSERT INTO `t_area` VALUES ('2817', '293', '西昌市', '24-293-2817', '3', 'Xichang Shi', '四川省凉山彝族自治州西昌市', 'XCA');
INSERT INTO `t_area` VALUES ('2818', '293', '木里藏族自治县', '24-293-2818', '3', 'Muli Zangzu Zizhixian', '四川省凉山彝族自治州木里藏族自治县', 'MLI');
INSERT INTO `t_area` VALUES ('2819', '293', '盐源县', '24-293-2819', '3', 'Yanyuan Xian', '四川省凉山彝族自治州盐源县', 'YYU');
INSERT INTO `t_area` VALUES ('2820', '293', '德昌县', '24-293-2820', '3', 'Dechang Xian', '四川省凉山彝族自治州德昌县', 'DEC');
INSERT INTO `t_area` VALUES ('2821', '293', '会理县', '24-293-2821', '3', 'Huili Xian', '四川省凉山彝族自治州会理县', 'HLI');
INSERT INTO `t_area` VALUES ('2822', '293', '会东县', '24-293-2822', '3', 'Huidong Xian', '四川省凉山彝族自治州会东县', 'HDG');
INSERT INTO `t_area` VALUES ('2823', '293', '宁南县', '24-293-2823', '3', 'Ningnan Xian', '四川省凉山彝族自治州宁南县', 'NIN');
INSERT INTO `t_area` VALUES ('2824', '293', '普格县', '24-293-2824', '3', 'Puge Xian', '四川省凉山彝族自治州普格县', 'PGE');
INSERT INTO `t_area` VALUES ('2825', '293', '布拖县', '24-293-2825', '3', 'Butuo Xian', '四川省凉山彝族自治州布拖县', 'BTO');
INSERT INTO `t_area` VALUES ('2826', '293', '金阳县', '24-293-2826', '3', 'Jinyang Xian', '四川省凉山彝族自治州金阳县', 'JYW');
INSERT INTO `t_area` VALUES ('2827', '293', '昭觉县', '24-293-2827', '3', 'Zhaojue Xian', '四川省凉山彝族自治州昭觉县', 'ZJE');
INSERT INTO `t_area` VALUES ('2828', '293', '喜德县', '24-293-2828', '3', 'Xide Xian', '四川省凉山彝族自治州喜德县', 'XDE');
INSERT INTO `t_area` VALUES ('2829', '293', '冕宁县', '24-293-2829', '3', 'Mianning Xian', '四川省凉山彝族自治州冕宁县', 'MNG');
INSERT INTO `t_area` VALUES ('2830', '293', '越西县', '24-293-2830', '3', 'Yuexi Xian', '四川省凉山彝族自治州越西县', 'YXC');
INSERT INTO `t_area` VALUES ('2831', '293', '甘洛县', '24-293-2831', '3', 'Ganluo Xian', '四川省凉山彝族自治州甘洛县', 'GLO');
INSERT INTO `t_area` VALUES ('2832', '293', '美姑县', '24-293-2832', '3', 'Meigu Xian', '四川省凉山彝族自治州美姑县', 'MEG');
INSERT INTO `t_area` VALUES ('2833', '293', '雷波县', '24-293-2833', '3', 'Leibo Xian', '四川省凉山彝族自治州雷波县', 'LBX');
INSERT INTO `t_area` VALUES ('2835', '294', '南明区', '25-294-2835', '3', 'Nanming Qu', '贵州省贵阳市南明区', 'NMQ');
INSERT INTO `t_area` VALUES ('2836', '294', '云岩区', '25-294-2836', '3', 'Yunyan Qu', '贵州省贵阳市云岩区', 'YYQ');
INSERT INTO `t_area` VALUES ('2837', '294', '花溪区', '25-294-2837', '3', 'Huaxi Qu', '贵州省贵阳市花溪区', 'HXI');
INSERT INTO `t_area` VALUES ('2838', '294', '乌当区', '25-294-2838', '3', 'Wudang Qu', '贵州省贵阳市乌当区', 'WDQ');
INSERT INTO `t_area` VALUES ('2839', '294', '白云区', '25-294-2839', '3', 'Baiyun Qu', '贵州省贵阳市白云区', 'BYU');
INSERT INTO `t_area` VALUES ('2840', '294', '小河区', '25-294-2840', '3', 'Xiaohe Qu', '贵州省贵阳市小河区', '2');
INSERT INTO `t_area` VALUES ('2841', '294', '开阳县', '25-294-2841', '3', 'Kaiyang Xian', '贵州省贵阳市开阳县', 'KYG');
INSERT INTO `t_area` VALUES ('2842', '294', '息烽县', '25-294-2842', '3', 'Xifeng Xian', '贵州省贵阳市息烽县', 'XFX');
INSERT INTO `t_area` VALUES ('2843', '294', '修文县', '25-294-2843', '3', 'Xiuwen Xian', '贵州省贵阳市修文县', 'XWX');
INSERT INTO `t_area` VALUES ('2844', '294', '清镇市', '25-294-2844', '3', 'Qingzhen Shi', '贵州省贵阳市清镇市', 'QZN');
INSERT INTO `t_area` VALUES ('2845', '295', '钟山区', '25-295-2845', '3', 'Zhongshan Qu', '贵州省六盘水市钟山区', 'ZSQ');
INSERT INTO `t_area` VALUES ('2846', '295', '六枝特区', '25-295-2846', '3', 'Liuzhi Tequ', '贵州省六盘水市六枝特区', 'LZT');
INSERT INTO `t_area` VALUES ('2847', '295', '水城县', '25-295-2847', '3', 'Shuicheng Xian', '贵州省六盘水市水城县', 'SUC');
INSERT INTO `t_area` VALUES ('2848', '295', '盘县', '25-295-2848', '3', 'Pan Xian', '贵州省六盘水市盘县', '2');
INSERT INTO `t_area` VALUES ('2850', '296', '红花岗区', '25-296-2850', '3', 'Honghuagang Qu', '贵州省遵义市红花岗区', 'HHG');
INSERT INTO `t_area` VALUES ('2851', '296', '汇川区', '25-296-2851', '3', 'Huichuan Qu', '贵州省遵义市汇川区', '2');
INSERT INTO `t_area` VALUES ('2852', '296', '遵义县', '25-296-2852', '3', 'Zunyi Xian', '贵州省遵义市遵义县', 'ZYI');
INSERT INTO `t_area` VALUES ('2853', '296', '桐梓县', '25-296-2853', '3', 'Tongzi Xian', '贵州省遵义市桐梓县', 'TZI');
INSERT INTO `t_area` VALUES ('2854', '296', '绥阳县', '25-296-2854', '3', 'Suiyang Xian', '贵州省遵义市绥阳县', 'SUY');
INSERT INTO `t_area` VALUES ('2855', '296', '正安县', '25-296-2855', '3', 'Zhengan Xan', '贵州省遵义市正安县', '2');
INSERT INTO `t_area` VALUES ('2856', '296', '道真仡佬族苗族自治县', '25-296-2856', '3', 'Daozhen Gelaozu Miaozu Zizhixian', '贵州省遵义市道真仡佬族苗族自治县', 'DZN');
INSERT INTO `t_area` VALUES ('2857', '296', '务川仡佬族苗族自治县', '25-296-2857', '3', 'Wuchuan Gelaozu Miaozu Zizhixian', '贵州省遵义市务川仡佬族苗族自治县', 'WCU');
INSERT INTO `t_area` VALUES ('2858', '296', '凤冈县', '25-296-2858', '3', 'Fenggang Xian', '贵州省遵义市凤冈县', 'FGG');
INSERT INTO `t_area` VALUES ('2859', '296', '湄潭县', '25-296-2859', '3', 'Meitan Xian', '贵州省遵义市湄潭县', 'MTN');
INSERT INTO `t_area` VALUES ('2860', '296', '余庆县', '25-296-2860', '3', 'Yuqing Xian', '贵州省遵义市余庆县', 'YUQ');
INSERT INTO `t_area` VALUES ('2861', '296', '习水县', '25-296-2861', '3', 'Xishui Xian', '贵州省遵义市习水县', 'XSI');
INSERT INTO `t_area` VALUES ('2862', '296', '赤水市', '25-296-2862', '3', 'Chishui Shi', '贵州省遵义市赤水市', 'CSS');
INSERT INTO `t_area` VALUES ('2863', '296', '仁怀市', '25-296-2863', '3', 'Renhuai Shi', '贵州省遵义市仁怀市', 'RHS');
INSERT INTO `t_area` VALUES ('2865', '297', '西秀区', '25-297-2865', '3', 'Xixiu Qu', '贵州省安顺市西秀区', '2');
INSERT INTO `t_area` VALUES ('2866', '297', '平坝县', '25-297-2866', '3', 'Pingba Xian', '贵州省安顺市平坝县', '2');
INSERT INTO `t_area` VALUES ('2867', '297', '普定县', '25-297-2867', '3', 'Puding Xian', '贵州省安顺市普定县', '2');
INSERT INTO `t_area` VALUES ('2868', '297', '镇宁布依族苗族自治县', '25-297-2868', '3', 'Zhenning Buyeizu Miaozu Zizhixian', '贵州省安顺市镇宁布依族苗族自治县', '2');
INSERT INTO `t_area` VALUES ('2869', '297', '关岭布依族苗族自治县', '25-297-2869', '3', 'Guanling Buyeizu Miaozu Zizhixian', '贵州省安顺市关岭布依族苗族自治县', '2');
INSERT INTO `t_area` VALUES ('2870', '297', '紫云苗族布依族自治县', '25-297-2870', '3', 'Ziyun Miaozu Buyeizu Zizhixian', '贵州省安顺市紫云苗族布依族自治县', '2');
INSERT INTO `t_area` VALUES ('2871', '298', '铜仁市', '25-298-2871', '3', 'Tongren Shi', '贵州省铜仁地区铜仁市', 'TRS');
INSERT INTO `t_area` VALUES ('2872', '298', '江口县', '25-298-2872', '3', 'Jiangkou Xian', '贵州省铜仁地区江口县', 'JGK');
INSERT INTO `t_area` VALUES ('2873', '298', '玉屏侗族自治县', '25-298-2873', '3', 'Yuping Dongzu Zizhixian', '贵州省铜仁地区玉屏侗族自治县', 'YPG');
INSERT INTO `t_area` VALUES ('2874', '298', '石阡县', '25-298-2874', '3', 'Shiqian Xian', '贵州省铜仁地区石阡县', 'SQI');
INSERT INTO `t_area` VALUES ('2875', '298', '思南县', '25-298-2875', '3', 'Sinan Xian', '贵州省铜仁地区思南县', 'SNA');
INSERT INTO `t_area` VALUES ('2876', '298', '印江土家族苗族自治县', '25-298-2876', '3', 'Yinjiang Tujiazu Miaozu Zizhixian', '贵州省铜仁地区印江土家族苗族自治县', 'YJY');
INSERT INTO `t_area` VALUES ('2877', '298', '德江县', '25-298-2877', '3', 'Dejiang Xian', '贵州省铜仁地区德江县', 'DEJ');
INSERT INTO `t_area` VALUES ('2878', '298', '沿河土家族自治县', '25-298-2878', '3', 'Yanhe Tujiazu Zizhixian', '贵州省铜仁地区沿河土家族自治县', 'YHE');
INSERT INTO `t_area` VALUES ('2879', '298', '松桃苗族自治县', '25-298-2879', '3', 'Songtao Miaozu Zizhixian', '贵州省铜仁地区松桃苗族自治县', 'STM');
INSERT INTO `t_area` VALUES ('2880', '298', '万山特区', '25-298-2880', '3', 'Wanshan Tequ', '贵州省铜仁地区万山特区', 'WAS');
INSERT INTO `t_area` VALUES ('2881', '299', '兴义市', '25-299-2881', '3', 'Xingyi Shi', '贵州省黔西南布依族苗族自治州兴义市', 'XYI');
INSERT INTO `t_area` VALUES ('2882', '299', '兴仁县', '25-299-2882', '3', 'Xingren Xian', '贵州省黔西南布依族苗族自治州兴仁县', 'XRN');
INSERT INTO `t_area` VALUES ('2883', '299', '普安县', '25-299-2883', '3', 'Pu,an Xian', '贵州省黔西南布依族苗族自治州普安县', 'PUA');
INSERT INTO `t_area` VALUES ('2884', '299', '晴隆县', '25-299-2884', '3', 'Qinglong Xian', '贵州省黔西南布依族苗族自治州晴隆县', 'QLG');
INSERT INTO `t_area` VALUES ('2885', '299', '贞丰县', '25-299-2885', '3', 'Zhenfeng Xian', '贵州省黔西南布依族苗族自治州贞丰县', 'ZFG');
INSERT INTO `t_area` VALUES ('2886', '299', '望谟县', '25-299-2886', '3', 'Wangmo Xian', '贵州省黔西南布依族苗族自治州望谟县', 'WMO');
INSERT INTO `t_area` VALUES ('2887', '299', '册亨县', '25-299-2887', '3', 'Ceheng Xian', '贵州省黔西南布依族苗族自治州册亨县', 'CEH');
INSERT INTO `t_area` VALUES ('2888', '299', '安龙县', '25-299-2888', '3', 'Anlong Xian', '贵州省黔西南布依族苗族自治州安龙县', 'ALG');
INSERT INTO `t_area` VALUES ('2889', '300', '毕节市', '25-300-2889', '3', 'Bijie Shi', '贵州省毕节地区毕节市', 'BJE');
INSERT INTO `t_area` VALUES ('2890', '300', '大方县', '25-300-2890', '3', 'Dafang Xian', '贵州省毕节地区大方县', 'DAF');
INSERT INTO `t_area` VALUES ('2891', '300', '黔西县', '25-300-2891', '3', 'Qianxi Xian', '贵州省毕节地区黔西县', 'QNX');
INSERT INTO `t_area` VALUES ('2892', '300', '金沙县', '25-300-2892', '3', 'Jinsha Xian', '贵州省毕节地区金沙县', 'JSX');
INSERT INTO `t_area` VALUES ('2893', '300', '织金县', '25-300-2893', '3', 'Zhijin Xian', '贵州省毕节地区织金县', 'ZJN');
INSERT INTO `t_area` VALUES ('2894', '300', '纳雍县', '25-300-2894', '3', 'Nayong Xian', '贵州省毕节地区纳雍县', 'NYG');
INSERT INTO `t_area` VALUES ('2895', '300', '威宁彝族回族苗族自治县', '25-300-2895', '3', 'Weining Yizu Huizu Miaozu Zizhixian', '贵州省毕节地区威宁彝族回族苗族自治县', 'WNG');
INSERT INTO `t_area` VALUES ('2896', '300', '赫章县', '25-300-2896', '3', 'Hezhang Xian', '贵州省毕节地区赫章县', 'HZA');
INSERT INTO `t_area` VALUES ('2897', '301', '凯里市', '25-301-2897', '3', 'Kaili Shi', '贵州省黔东南苗族侗族自治州凯里市', 'KLS');
INSERT INTO `t_area` VALUES ('2898', '301', '黄平县', '25-301-2898', '3', 'Huangping Xian', '贵州省黔东南苗族侗族自治州黄平县', 'HPN');
INSERT INTO `t_area` VALUES ('2899', '301', '施秉县', '25-301-2899', '3', 'Shibing Xian', '贵州省黔东南苗族侗族自治州施秉县', 'SBG');
INSERT INTO `t_area` VALUES ('2900', '301', '三穗县', '25-301-2900', '3', 'Sansui Xian', '贵州省黔东南苗族侗族自治州三穗县', 'SAS');
INSERT INTO `t_area` VALUES ('2901', '301', '镇远县', '25-301-2901', '3', 'Zhenyuan Xian', '贵州省黔东南苗族侗族自治州镇远县', 'ZYX');
INSERT INTO `t_area` VALUES ('2902', '301', '岑巩县', '25-301-2902', '3', 'Cengong Xian', '贵州省黔东南苗族侗族自治州岑巩县', 'CGX');
INSERT INTO `t_area` VALUES ('2903', '301', '天柱县', '25-301-2903', '3', 'Tianzhu Xian', '贵州省黔东南苗族侗族自治州天柱县', 'TZU');
INSERT INTO `t_area` VALUES ('2904', '301', '锦屏县', '25-301-2904', '3', 'Jinping Xian', '贵州省黔东南苗族侗族自治州锦屏县', 'JPX');
INSERT INTO `t_area` VALUES ('2905', '301', '剑河县', '25-301-2905', '3', 'Jianhe Xian', '贵州省黔东南苗族侗族自治州剑河县', 'JHE');
INSERT INTO `t_area` VALUES ('2906', '301', '台江县', '25-301-2906', '3', 'Taijiang Xian', '贵州省黔东南苗族侗族自治州台江县', 'TJX');
INSERT INTO `t_area` VALUES ('2907', '301', '黎平县', '25-301-2907', '3', 'Liping Xian', '贵州省黔东南苗族侗族自治州黎平县', 'LIP');
INSERT INTO `t_area` VALUES ('2908', '301', '榕江县', '25-301-2908', '3', 'Rongjiang Xian', '贵州省黔东南苗族侗族自治州榕江县', 'RJG');
INSERT INTO `t_area` VALUES ('2909', '301', '从江县', '25-301-2909', '3', 'Congjiang Xian', '贵州省黔东南苗族侗族自治州从江县', 'COJ');
INSERT INTO `t_area` VALUES ('2910', '301', '雷山县', '25-301-2910', '3', 'Leishan Xian', '贵州省黔东南苗族侗族自治州雷山县', 'LSA');
INSERT INTO `t_area` VALUES ('2911', '301', '麻江县', '25-301-2911', '3', 'Majiang Xian', '贵州省黔东南苗族侗族自治州麻江县', 'MAJ');
INSERT INTO `t_area` VALUES ('2912', '301', '丹寨县', '25-301-2912', '3', 'Danzhai Xian', '贵州省黔东南苗族侗族自治州丹寨县', 'DZH');
INSERT INTO `t_area` VALUES ('2913', '302', '都匀市', '25-302-2913', '3', 'Duyun Shi', '贵州省黔南布依族苗族自治州都匀市', 'DUY');
INSERT INTO `t_area` VALUES ('2914', '302', '福泉市', '25-302-2914', '3', 'Fuquan Shi', '贵州省黔南布依族苗族自治州福泉市', 'FQN');
INSERT INTO `t_area` VALUES ('2915', '302', '荔波县', '25-302-2915', '3', 'Libo Xian', '贵州省黔南布依族苗族自治州荔波县', 'LBO');
INSERT INTO `t_area` VALUES ('2916', '302', '贵定县', '25-302-2916', '3', 'Guiding Xian', '贵州省黔南布依族苗族自治州贵定县', 'GDG');
INSERT INTO `t_area` VALUES ('2917', '302', '瓮安县', '25-302-2917', '3', 'Weng,an Xian', '贵州省黔南布依族苗族自治州瓮安县', 'WGA');
INSERT INTO `t_area` VALUES ('2918', '302', '独山县', '25-302-2918', '3', 'Dushan Xian', '贵州省黔南布依族苗族自治州独山县', 'DSX');
INSERT INTO `t_area` VALUES ('2919', '302', '平塘县', '25-302-2919', '3', 'Pingtang Xian', '贵州省黔南布依族苗族自治州平塘县', 'PTG');
INSERT INTO `t_area` VALUES ('2920', '302', '罗甸县', '25-302-2920', '3', 'Luodian Xian', '贵州省黔南布依族苗族自治州罗甸县', 'LOD');
INSERT INTO `t_area` VALUES ('2921', '302', '长顺县', '25-302-2921', '3', 'Changshun Xian', '贵州省黔南布依族苗族自治州长顺县', 'CSU');
INSERT INTO `t_area` VALUES ('2922', '302', '龙里县', '25-302-2922', '3', 'Longli Xian', '贵州省黔南布依族苗族自治州龙里县', 'LLI');
INSERT INTO `t_area` VALUES ('2923', '302', '惠水县', '25-302-2923', '3', 'Huishui Xian', '贵州省黔南布依族苗族自治州惠水县', 'HUS');
INSERT INTO `t_area` VALUES ('2924', '302', '三都水族自治县', '25-302-2924', '3', 'Sandu Suizu Zizhixian', '贵州省黔南布依族苗族自治州三都水族自治县', 'SDU');
INSERT INTO `t_area` VALUES ('2926', '303', '五华区', '26-303-2926', '3', 'Wuhua Qu', '云南省昆明市五华区', 'WHA');
INSERT INTO `t_area` VALUES ('2927', '303', '盘龙区', '26-303-2927', '3', 'Panlong Qu', '云南省昆明市盘龙区', 'PLQ');
INSERT INTO `t_area` VALUES ('2928', '303', '官渡区', '26-303-2928', '3', 'Guandu Qu', '云南省昆明市官渡区', 'GDU');
INSERT INTO `t_area` VALUES ('2929', '303', '西山区', '26-303-2929', '3', 'Xishan Qu', '云南省昆明市西山区', 'XSN');
INSERT INTO `t_area` VALUES ('2930', '303', '东川区', '26-303-2930', '3', 'Dongchuan Qu', '云南省昆明市东川区', 'DCU');
INSERT INTO `t_area` VALUES ('2931', '303', '呈贡县', '26-303-2931', '3', 'Chenggong Xian', '云南省昆明市呈贡县', 'CGD');
INSERT INTO `t_area` VALUES ('2932', '303', '晋宁县', '26-303-2932', '3', 'Jinning Xian', '云南省昆明市晋宁县', 'JND');
INSERT INTO `t_area` VALUES ('2933', '303', '富民县', '26-303-2933', '3', 'Fumin Xian', '云南省昆明市富民县', 'FMN');
INSERT INTO `t_area` VALUES ('2934', '303', '宜良县', '26-303-2934', '3', 'Yiliang Xian', '云南省昆明市宜良县', 'YIL');
INSERT INTO `t_area` VALUES ('2935', '303', '石林彝族自治县', '26-303-2935', '3', 'Shilin Yizu Zizhixian', '云南省昆明市石林彝族自治县', 'SLY');
INSERT INTO `t_area` VALUES ('2936', '303', '嵩明县', '26-303-2936', '3', 'Songming Xian', '云南省昆明市嵩明县', 'SMI');
INSERT INTO `t_area` VALUES ('2937', '303', '禄劝彝族苗族自治县', '26-303-2937', '3', 'Luchuan Yizu Miaozu Zizhixian', '云南省昆明市禄劝彝族苗族自治县', 'LUC');
INSERT INTO `t_area` VALUES ('2938', '303', '寻甸回族彝族自治县', '26-303-2938', '3', 'Xundian Huizu Yizu Zizhixian', '云南省昆明市寻甸回族彝族自治县', 'XDN');
INSERT INTO `t_area` VALUES ('2939', '303', '安宁市', '26-303-2939', '3', 'Anning Shi', '云南省昆明市安宁市', 'ANG');
INSERT INTO `t_area` VALUES ('2941', '304', '麒麟区', '26-304-2941', '3', 'Qilin Xian', '云南省曲靖市麒麟区', 'QLQ');
INSERT INTO `t_area` VALUES ('2942', '304', '马龙县', '26-304-2942', '3', 'Malong Xian', '云南省曲靖市马龙县', 'MLO');
INSERT INTO `t_area` VALUES ('2943', '304', '陆良县', '26-304-2943', '3', 'Luliang Xian', '云南省曲靖市陆良县', 'LLX');
INSERT INTO `t_area` VALUES ('2944', '304', '师宗县', '26-304-2944', '3', 'Shizong Xian', '云南省曲靖市师宗县', 'SZD');
INSERT INTO `t_area` VALUES ('2945', '304', '罗平县', '26-304-2945', '3', 'Luoping Xian', '云南省曲靖市罗平县', 'LPX');
INSERT INTO `t_area` VALUES ('2946', '304', '富源县', '26-304-2946', '3', 'Fuyuan Xian', '云南省曲靖市富源县', 'FYD');
INSERT INTO `t_area` VALUES ('2947', '304', '会泽县', '26-304-2947', '3', 'Huize Xian', '云南省曲靖市会泽县', 'HUZ');
INSERT INTO `t_area` VALUES ('2948', '304', '沾益县', '26-304-2948', '3', 'Zhanyi Xian', '云南省曲靖市沾益县', 'ZYD');
INSERT INTO `t_area` VALUES ('2949', '304', '宣威市', '26-304-2949', '3', 'Xuanwei Shi', '云南省曲靖市宣威市', 'XWS');
INSERT INTO `t_area` VALUES ('2951', '305', '红塔区', '26-305-2951', '3', 'Hongta Qu', '云南省玉溪市红塔区', 'HTA');
INSERT INTO `t_area` VALUES ('2952', '305', '江川县', '26-305-2952', '3', 'Jiangchuan Xian', '云南省玉溪市江川县', 'JGC');
INSERT INTO `t_area` VALUES ('2953', '305', '澄江县', '26-305-2953', '3', 'Chengjiang Xian', '云南省玉溪市澄江县', 'CGJ');
INSERT INTO `t_area` VALUES ('2954', '305', '通海县', '26-305-2954', '3', 'Tonghai Xian', '云南省玉溪市通海县', 'THI');
INSERT INTO `t_area` VALUES ('2955', '305', '华宁县', '26-305-2955', '3', 'Huaning Xian', '云南省玉溪市华宁县', 'HND');
INSERT INTO `t_area` VALUES ('2956', '305', '易门县', '26-305-2956', '3', 'Yimen Xian', '云南省玉溪市易门县', 'YMD');
INSERT INTO `t_area` VALUES ('2957', '305', '峨山彝族自治县', '26-305-2957', '3', 'Eshan Yizu Zizhixian', '云南省玉溪市峨山彝族自治县', 'ESN');
INSERT INTO `t_area` VALUES ('2958', '305', '新平彝族傣族自治县', '26-305-2958', '3', 'Xinping Yizu Daizu Zizhixian', '云南省玉溪市新平彝族傣族自治县', 'XNP');
INSERT INTO `t_area` VALUES ('2959', '305', '元江哈尼族彝族傣族自治县', '26-305-2959', '3', 'Yuanjiang Hanizu Yizu Daizu Zizhixian', '云南省玉溪市元江哈尼族彝族傣族自治县', 'YJA');
INSERT INTO `t_area` VALUES ('2961', '306', '隆阳区', '26-306-2961', '3', 'Longyang Qu', '云南省保山市隆阳区', '2');
INSERT INTO `t_area` VALUES ('2962', '306', '施甸县', '26-306-2962', '3', 'Shidian Xian', '云南省保山市施甸县', '2');
INSERT INTO `t_area` VALUES ('2963', '306', '腾冲县', '26-306-2963', '3', 'Tengchong Xian', '云南省保山市腾冲县', '2');
INSERT INTO `t_area` VALUES ('2964', '306', '龙陵县', '26-306-2964', '3', 'Longling Xian', '云南省保山市龙陵县', '2');
INSERT INTO `t_area` VALUES ('2965', '306', '昌宁县', '26-306-2965', '3', 'Changning Xian', '云南省保山市昌宁县', '2');
INSERT INTO `t_area` VALUES ('2967', '307', '昭阳区', '26-307-2967', '3', 'Zhaoyang Qu', '云南省昭通市昭阳区', '2');
INSERT INTO `t_area` VALUES ('2968', '307', '鲁甸县', '26-307-2968', '3', 'Ludian Xian', '云南省昭通市鲁甸县', '2');
INSERT INTO `t_area` VALUES ('2969', '307', '巧家县', '26-307-2969', '3', 'Qiaojia Xian', '云南省昭通市巧家县', '2');
INSERT INTO `t_area` VALUES ('2970', '307', '盐津县', '26-307-2970', '3', 'Yanjin Xian', '云南省昭通市盐津县', '2');
INSERT INTO `t_area` VALUES ('2971', '307', '大关县', '26-307-2971', '3', 'Daguan Xian', '云南省昭通市大关县', '2');
INSERT INTO `t_area` VALUES ('2972', '307', '永善县', '26-307-2972', '3', 'Yongshan Xian', '云南省昭通市永善县', '2');
INSERT INTO `t_area` VALUES ('2973', '307', '绥江县', '26-307-2973', '3', 'Suijiang Xian', '云南省昭通市绥江县', '2');
INSERT INTO `t_area` VALUES ('2974', '307', '镇雄县', '26-307-2974', '3', 'Zhenxiong Xian', '云南省昭通市镇雄县', '2');
INSERT INTO `t_area` VALUES ('2975', '307', '彝良县', '26-307-2975', '3', 'Yiliang Xian', '云南省昭通市彝良县', '2');
INSERT INTO `t_area` VALUES ('2976', '307', '威信县', '26-307-2976', '3', 'Weixin Xian', '云南省昭通市威信县', '2');
INSERT INTO `t_area` VALUES ('2977', '307', '水富县', '26-307-2977', '3', 'Shuifu Xian ', '云南省昭通市水富县', '2');
INSERT INTO `t_area` VALUES ('2979', '308', '古城区', '26-308-2979', '3', 'Gucheng Qu', '云南省丽江市古城区', '2');
INSERT INTO `t_area` VALUES ('2980', '308', '玉龙纳西族自治县', '26-308-2980', '3', 'Yulongnaxizuzizhi Xian', '云南省丽江市玉龙纳西族自治县', '2');
INSERT INTO `t_area` VALUES ('2981', '308', '永胜县', '26-308-2981', '3', 'Yongsheng Xian', '云南省丽江市永胜县', '2');
INSERT INTO `t_area` VALUES ('2982', '308', '华坪县', '26-308-2982', '3', 'Huaping Xian', '云南省丽江市华坪县', '2');
INSERT INTO `t_area` VALUES ('2983', '308', '宁蒗彝族自治县', '26-308-2983', '3', 'Ninglang Yizu Zizhixian', '云南省丽江市宁蒗彝族自治县', '2');
INSERT INTO `t_area` VALUES ('2985', '309', '思茅区', '26-309-2985', '3', 'Simao Qu', '云南省普洱市思茅区', '2');
INSERT INTO `t_area` VALUES ('2986', '309', '宁洱哈尼族彝族自治县', '26-309-2986', '3', 'Pu,er Hanizu Yizu Zizhixian', '云南省普洱市宁洱哈尼族彝族自治县', '2');
INSERT INTO `t_area` VALUES ('2987', '309', '墨江哈尼族自治县', '26-309-2987', '3', 'Mojiang Hanizu Zizhixian', '云南省普洱市墨江哈尼族自治县', '2');
INSERT INTO `t_area` VALUES ('2988', '309', '景东彝族自治县', '26-309-2988', '3', 'Jingdong Yizu Zizhixian', '云南省普洱市景东彝族自治县', '2');
INSERT INTO `t_area` VALUES ('2989', '309', '景谷傣族彝族自治县', '26-309-2989', '3', 'Jinggu Daizu Yizu Zizhixian', '云南省普洱市景谷傣族彝族自治县', '2');
INSERT INTO `t_area` VALUES ('2990', '309', '镇沅彝族哈尼族拉祜族自治县', '26-309-2990', '3', 'Zhenyuan Yizu Hanizu Lahuzu Zizhixian', '云南省普洱市镇沅彝族哈尼族拉祜族自治县', '2');
INSERT INTO `t_area` VALUES ('2991', '309', '江城哈尼族彝族自治县', '26-309-2991', '3', 'Jiangcheng Hanizu Yizu Zizhixian', '云南省普洱市江城哈尼族彝族自治县', '2');
INSERT INTO `t_area` VALUES ('2992', '309', '孟连傣族拉祜族佤族自治县', '26-309-2992', '3', 'Menglian Daizu Lahuzu Vazu Zizixian', '云南省普洱市孟连傣族拉祜族佤族自治县', '2');
INSERT INTO `t_area` VALUES ('2993', '309', '澜沧拉祜族自治县', '26-309-2993', '3', 'Lancang Lahuzu Zizhixian', '云南省普洱市澜沧拉祜族自治县', '2');
INSERT INTO `t_area` VALUES ('2994', '309', '西盟佤族自治县', '26-309-2994', '3', 'Ximeng Vazu Zizhixian', '云南省普洱市西盟佤族自治县', '2');
INSERT INTO `t_area` VALUES ('2996', '310', '临翔区', '26-310-2996', '3', 'Linxiang Qu', '云南省临沧市临翔区', '2');
INSERT INTO `t_area` VALUES ('2997', '310', '凤庆县', '26-310-2997', '3', 'Fengqing Xian', '云南省临沧市凤庆县', '2');
INSERT INTO `t_area` VALUES ('2998', '310', '云县', '26-310-2998', '3', 'Yun Xian', '云南省临沧市云县', '2');
INSERT INTO `t_area` VALUES ('2999', '310', '永德县', '26-310-2999', '3', 'Yongde Xian', '云南省临沧市永德县', '2');
INSERT INTO `t_area` VALUES ('3000', '310', '镇康县', '26-310-3000', '3', 'Zhenkang Xian', '云南省临沧市镇康县', '2');
INSERT INTO `t_area` VALUES ('3001', '310', '双江拉祜族佤族布朗族傣族自治县', '26-310-3001', '3', 'Shuangjiang Lahuzu Vazu Bulangzu Daizu Zizhixian', '云南省临沧市双江拉祜族佤族布朗族傣族自治县', '2');
INSERT INTO `t_area` VALUES ('3002', '310', '耿马傣族佤族自治县', '26-310-3002', '3', 'Gengma Daizu Vazu Zizhixian', '云南省临沧市耿马傣族佤族自治县', '2');
INSERT INTO `t_area` VALUES ('3003', '310', '沧源佤族自治县', '26-310-3003', '3', 'Cangyuan Vazu Zizhixian', '云南省临沧市沧源佤族自治县', '2');
INSERT INTO `t_area` VALUES ('3004', '311', '楚雄市', '26-311-3004', '3', 'Chuxiong Shi', '云南省楚雄彝族自治州楚雄市', 'CXS');
INSERT INTO `t_area` VALUES ('3005', '311', '双柏县', '26-311-3005', '3', 'Shuangbai Xian', '云南省楚雄彝族自治州双柏县', 'SBA');
INSERT INTO `t_area` VALUES ('3006', '311', '牟定县', '26-311-3006', '3', 'Mouding Xian', '云南省楚雄彝族自治州牟定县', 'MDI');
INSERT INTO `t_area` VALUES ('3007', '311', '南华县', '26-311-3007', '3', 'Nanhua Xian', '云南省楚雄彝族自治州南华县', 'NHA');
INSERT INTO `t_area` VALUES ('3008', '311', '姚安县', '26-311-3008', '3', 'Yao,an Xian', '云南省楚雄彝族自治州姚安县', 'YOA');
INSERT INTO `t_area` VALUES ('3009', '311', '大姚县', '26-311-3009', '3', 'Dayao Xian', '云南省楚雄彝族自治州大姚县', 'DYO');
INSERT INTO `t_area` VALUES ('3010', '311', '永仁县', '26-311-3010', '3', 'Yongren Xian', '云南省楚雄彝族自治州永仁县', 'YRN');
INSERT INTO `t_area` VALUES ('3011', '311', '元谋县', '26-311-3011', '3', 'Yuanmou Xian', '云南省楚雄彝族自治州元谋县', 'YMO');
INSERT INTO `t_area` VALUES ('3012', '311', '武定县', '26-311-3012', '3', 'Wuding Xian', '云南省楚雄彝族自治州武定县', 'WDX');
INSERT INTO `t_area` VALUES ('3013', '311', '禄丰县', '26-311-3013', '3', 'Lufeng Xian', '云南省楚雄彝族自治州禄丰县', 'LFX');
INSERT INTO `t_area` VALUES ('3014', '312', '个旧市', '26-312-3014', '3', 'Gejiu Shi', '云南省红河哈尼族彝族自治州个旧市', 'GJU');
INSERT INTO `t_area` VALUES ('3015', '312', '开远市', '26-312-3015', '3', 'Kaiyuan Shi', '云南省红河哈尼族彝族自治州开远市', 'KYD');
INSERT INTO `t_area` VALUES ('3016', '312', '蒙自市', '26-312-3016', '3', 'Mengzi Xian', '云南省红河哈尼族彝族自治州蒙自市', '2');
INSERT INTO `t_area` VALUES ('3017', '312', '屏边苗族自治县', '26-312-3017', '3', 'Pingbian Miaozu Zizhixian', '云南省红河哈尼族彝族自治州屏边苗族自治县', 'PBN');
INSERT INTO `t_area` VALUES ('3018', '312', '建水县', '26-312-3018', '3', 'Jianshui Xian', '云南省红河哈尼族彝族自治州建水县', 'JSD');
INSERT INTO `t_area` VALUES ('3019', '312', '石屏县', '26-312-3019', '3', 'Shiping Xian', '云南省红河哈尼族彝族自治州石屏县', 'SPG');
INSERT INTO `t_area` VALUES ('3020', '312', '弥勒县', '26-312-3020', '3', 'Mile Xian', '云南省红河哈尼族彝族自治州弥勒县', 'MIL');
INSERT INTO `t_area` VALUES ('3021', '312', '泸西县', '26-312-3021', '3', 'Luxi Xian', '云南省红河哈尼族彝族自治州泸西县', 'LXD');
INSERT INTO `t_area` VALUES ('3022', '312', '元阳县', '26-312-3022', '3', 'Yuanyang Xian', '云南省红河哈尼族彝族自治州元阳县', 'YYD');
INSERT INTO `t_area` VALUES ('3023', '312', '红河县', '26-312-3023', '3', 'Honghe Xian', '云南省红河哈尼族彝族自治州红河县', 'HHX');
INSERT INTO `t_area` VALUES ('3024', '312', '金平苗族瑶族傣族自治县', '26-312-3024', '3', 'Jinping Miaozu Yaozu Daizu Zizhixian', '云南省红河哈尼族彝族自治州金平苗族瑶族傣族自治县', 'JNP');
INSERT INTO `t_area` VALUES ('3025', '312', '绿春县', '26-312-3025', '3', 'Lvchun Xian', '云南省红河哈尼族彝族自治州绿春县', 'LCX');
INSERT INTO `t_area` VALUES ('3026', '312', '河口瑶族自治县', '26-312-3026', '3', 'Hekou Yaozu Zizhixian', '云南省红河哈尼族彝族自治州河口瑶族自治县', 'HKM');
INSERT INTO `t_area` VALUES ('3027', '313', '文山县', '26-313-3027', '3', 'Wenshan Xian', '云南省文山壮族苗族自治州文山县', 'WES');
INSERT INTO `t_area` VALUES ('3028', '313', '砚山县', '26-313-3028', '3', 'Yanshan Xian', '云南省文山壮族苗族自治州砚山县', 'YSD');
INSERT INTO `t_area` VALUES ('3029', '313', '西畴县', '26-313-3029', '3', 'Xichou Xian', '云南省文山壮族苗族自治州西畴县', 'XIC');
INSERT INTO `t_area` VALUES ('3030', '313', '麻栗坡县', '26-313-3030', '3', 'Malipo Xian', '云南省文山壮族苗族自治州麻栗坡县', 'MLP');
INSERT INTO `t_area` VALUES ('3031', '313', '马关县', '26-313-3031', '3', 'Maguan Xian', '云南省文山壮族苗族自治州马关县', 'MGN');
INSERT INTO `t_area` VALUES ('3032', '313', '丘北县', '26-313-3032', '3', 'Qiubei Xian', '云南省文山壮族苗族自治州丘北县', 'QBE');
INSERT INTO `t_area` VALUES ('3033', '313', '广南县', '26-313-3033', '3', 'Guangnan Xian', '云南省文山壮族苗族自治州广南县', 'GGN');
INSERT INTO `t_area` VALUES ('3034', '313', '富宁县', '26-313-3034', '3', 'Funing Xian', '云南省文山壮族苗族自治州富宁县', 'FND');
INSERT INTO `t_area` VALUES ('3035', '314', '景洪市', '26-314-3035', '3', 'Jinghong Shi', '云南省西双版纳傣族自治州景洪市', 'JHG');
INSERT INTO `t_area` VALUES ('3036', '314', '勐海县', '26-314-3036', '3', 'Menghai Xian', '云南省西双版纳傣族自治州勐海县', 'MHI');
INSERT INTO `t_area` VALUES ('3037', '314', '勐腊县', '26-314-3037', '3', 'Mengla Xian', '云南省西双版纳傣族自治州勐腊县', 'MLA');
INSERT INTO `t_area` VALUES ('3038', '315', '大理市', '26-315-3038', '3', 'Dali Shi', '云南省大理白族自治州大理市', 'DLS');
INSERT INTO `t_area` VALUES ('3039', '315', '漾濞彝族自治县', '26-315-3039', '3', 'Yangbi Yizu Zizhixian', '云南省大理白族自治州漾濞彝族自治县', 'YGB');
INSERT INTO `t_area` VALUES ('3040', '315', '祥云县', '26-315-3040', '3', 'Xiangyun Xian', '云南省大理白族自治州祥云县', 'XYD');
INSERT INTO `t_area` VALUES ('3041', '315', '宾川县', '26-315-3041', '3', 'Binchuan Xian', '云南省大理白族自治州宾川县', 'BCD');
INSERT INTO `t_area` VALUES ('3042', '315', '弥渡县', '26-315-3042', '3', 'Midu Xian', '云南省大理白族自治州弥渡县', 'MDU');
INSERT INTO `t_area` VALUES ('3043', '315', '南涧彝族自治县', '26-315-3043', '3', 'Nanjian Yizu Zizhixian', '云南省大理白族自治州南涧彝族自治县', 'NNJ');
INSERT INTO `t_area` VALUES ('3044', '315', '巍山彝族回族自治县', '26-315-3044', '3', 'Weishan Yizu Huizu Zizhixian', '云南省大理白族自治州巍山彝族回族自治县', 'WSY');
INSERT INTO `t_area` VALUES ('3045', '315', '永平县', '26-315-3045', '3', 'Yongping Xian', '云南省大理白族自治州永平县', 'YPX');
INSERT INTO `t_area` VALUES ('3046', '315', '云龙县', '26-315-3046', '3', 'Yunlong Xian', '云南省大理白族自治州云龙县', 'YLO');
INSERT INTO `t_area` VALUES ('3047', '315', '洱源县', '26-315-3047', '3', 'Eryuan Xian', '云南省大理白族自治州洱源县', 'EYN');
INSERT INTO `t_area` VALUES ('3048', '315', '剑川县', '26-315-3048', '3', 'Jianchuan Xian', '云南省大理白族自治州剑川县', 'JIC');
INSERT INTO `t_area` VALUES ('3049', '315', '鹤庆县', '26-315-3049', '3', 'Heqing Xian', '云南省大理白族自治州鹤庆县', 'HQG');
INSERT INTO `t_area` VALUES ('3050', '316', '瑞丽市', '26-316-3050', '3', 'Ruili Shi', '云南省德宏傣族景颇族自治州瑞丽市', 'RUI');
INSERT INTO `t_area` VALUES ('3051', '316', '芒市', '26-316-3051', '3', 'Luxi Shi', '云南省德宏傣族景颇族自治州芒市', '2');
INSERT INTO `t_area` VALUES ('3052', '316', '梁河县', '26-316-3052', '3', 'Lianghe Xian', '云南省德宏傣族景颇族自治州梁河县', 'LHD');
INSERT INTO `t_area` VALUES ('3053', '316', '盈江县', '26-316-3053', '3', 'Yingjiang Xian', '云南省德宏傣族景颇族自治州盈江县', 'YGJ');
INSERT INTO `t_area` VALUES ('3054', '316', '陇川县', '26-316-3054', '3', 'Longchuan Xian', '云南省德宏傣族景颇族自治州陇川县', 'LCN');
INSERT INTO `t_area` VALUES ('3055', '317', '泸水县', '26-317-3055', '3', 'Lushui Xian', '云南省怒江傈僳族自治州泸水县', 'LSX');
INSERT INTO `t_area` VALUES ('3056', '317', '福贡县', '26-317-3056', '3', 'Fugong Xian', '云南省怒江傈僳族自治州福贡县', 'FGO');
INSERT INTO `t_area` VALUES ('3057', '317', '贡山独龙族怒族自治县', '26-317-3057', '3', 'Gongshan Dulongzu Nuzu Zizhixian', '云南省怒江傈僳族自治州贡山独龙族怒族自治县', 'GSN');
INSERT INTO `t_area` VALUES ('3058', '317', '兰坪白族普米族自治县', '26-317-3058', '3', 'Lanping Baizu Pumizu Zizhixian', '云南省怒江傈僳族自治州兰坪白族普米族自治县', 'LPG');
INSERT INTO `t_area` VALUES ('3059', '318', '香格里拉县', '26-318-3059', '3', 'Xianggelila Xian', '云南省迪庆藏族自治州香格里拉县', '2');
INSERT INTO `t_area` VALUES ('3060', '318', '德钦县', '26-318-3060', '3', 'Deqen Xian', '云南省迪庆藏族自治州德钦县', 'DQN');
INSERT INTO `t_area` VALUES ('3061', '318', '维西傈僳族自治县', '26-318-3061', '3', 'Weixi Lisuzu Zizhixian', '云南省迪庆藏族自治州维西傈僳族自治县', 'WXI');
INSERT INTO `t_area` VALUES ('3063', '319', '城关区', '27-319-3063', '3', 'Chengguang Qu', '西藏自治区拉萨市城关区', 'CGN');
INSERT INTO `t_area` VALUES ('3064', '319', '林周县', '27-319-3064', '3', 'Lhvnzhub Xian', '西藏自治区拉萨市林周县', 'LZB');
INSERT INTO `t_area` VALUES ('3065', '319', '当雄县', '27-319-3065', '3', 'Damxung Xian', '西藏自治区拉萨市当雄县', 'DAM');
INSERT INTO `t_area` VALUES ('3066', '319', '尼木县', '27-319-3066', '3', 'Nyemo Xian', '西藏自治区拉萨市尼木县', 'NYE');
INSERT INTO `t_area` VALUES ('3067', '319', '曲水县', '27-319-3067', '3', 'Qvxv Xian', '西藏自治区拉萨市曲水县', 'QUX');
INSERT INTO `t_area` VALUES ('3068', '319', '堆龙德庆县', '27-319-3068', '3', 'Doilungdeqen Xian', '西藏自治区拉萨市堆龙德庆县', 'DOI');
INSERT INTO `t_area` VALUES ('3069', '319', '达孜县', '27-319-3069', '3', 'Dagze Xian', '西藏自治区拉萨市达孜县', 'DAG');
INSERT INTO `t_area` VALUES ('3070', '319', '墨竹工卡县', '27-319-3070', '3', 'Maizhokunggar Xian', '西藏自治区拉萨市墨竹工卡县', 'MAI');
INSERT INTO `t_area` VALUES ('3071', '320', '昌都县', '27-320-3071', '3', 'Qamdo Xian', '西藏自治区昌都地区昌都县', 'QAX');
INSERT INTO `t_area` VALUES ('3072', '320', '江达县', '27-320-3072', '3', 'Jomda Xian', '西藏自治区昌都地区江达县', 'JOM');
INSERT INTO `t_area` VALUES ('3073', '320', '贡觉县', '27-320-3073', '3', 'Konjo Xian', '西藏自治区昌都地区贡觉县', 'KON');
INSERT INTO `t_area` VALUES ('3074', '320', '类乌齐县', '27-320-3074', '3', 'Riwoqe Xian', '西藏自治区昌都地区类乌齐县', 'RIW');
INSERT INTO `t_area` VALUES ('3075', '320', '丁青县', '27-320-3075', '3', 'Dengqen Xian', '西藏自治区昌都地区丁青县', 'DEN');
INSERT INTO `t_area` VALUES ('3076', '320', '察雅县', '27-320-3076', '3', 'Chagyab Xian', '西藏自治区昌都地区察雅县', 'CHA');
INSERT INTO `t_area` VALUES ('3077', '320', '八宿县', '27-320-3077', '3', 'Baxoi Xian', '西藏自治区昌都地区八宿县', 'BAX');
INSERT INTO `t_area` VALUES ('3078', '320', '左贡县', '27-320-3078', '3', 'Zogang Xian', '西藏自治区昌都地区左贡县', 'ZOX');
INSERT INTO `t_area` VALUES ('3079', '320', '芒康县', '27-320-3079', '3', 'Mangkam Xian', '西藏自治区昌都地区芒康县', 'MAN');
INSERT INTO `t_area` VALUES ('3080', '320', '洛隆县', '27-320-3080', '3', 'Lhorong Xian', '西藏自治区昌都地区洛隆县', 'LHO');
INSERT INTO `t_area` VALUES ('3081', '320', '边坝县', '27-320-3081', '3', 'Banbar Xian', '西藏自治区昌都地区边坝县', 'BAN');
INSERT INTO `t_area` VALUES ('3082', '321', '乃东县', '27-321-3082', '3', 'Nedong Xian', '西藏自治区山南地区乃东县', 'NED');
INSERT INTO `t_area` VALUES ('3083', '321', '扎囊县', '27-321-3083', '3', 'Chanang(Chatang) Xian', '西藏自治区山南地区扎囊县', 'CNG');
INSERT INTO `t_area` VALUES ('3084', '321', '贡嘎县', '27-321-3084', '3', 'Gonggar Xian', '西藏自治区山南地区贡嘎县', 'GON');
INSERT INTO `t_area` VALUES ('3085', '321', '桑日县', '27-321-3085', '3', 'Sangri Xian', '西藏自治区山南地区桑日县', 'SRI');
INSERT INTO `t_area` VALUES ('3086', '321', '琼结县', '27-321-3086', '3', 'Qonggyai Xian', '西藏自治区山南地区琼结县', 'QON');
INSERT INTO `t_area` VALUES ('3087', '321', '曲松县', '27-321-3087', '3', 'Qusum Xian', '西藏自治区山南地区曲松县', 'QUS');
INSERT INTO `t_area` VALUES ('3088', '321', '措美县', '27-321-3088', '3', 'Comai Xian', '西藏自治区山南地区措美县', 'COM');
INSERT INTO `t_area` VALUES ('3089', '321', '洛扎县', '27-321-3089', '3', 'Lhozhag Xian', '西藏自治区山南地区洛扎县', 'LHX');
INSERT INTO `t_area` VALUES ('3090', '321', '加查县', '27-321-3090', '3', 'Gyaca Xian', '西藏自治区山南地区加查县', 'GYA');
INSERT INTO `t_area` VALUES ('3091', '321', '隆子县', '27-321-3091', '3', 'Lhvnze Xian', '西藏自治区山南地区隆子县', 'LHZ');
INSERT INTO `t_area` VALUES ('3092', '321', '错那县', '27-321-3092', '3', 'Cona Xian', '西藏自治区山南地区错那县', 'CON');
INSERT INTO `t_area` VALUES ('3093', '321', '浪卡子县', '27-321-3093', '3', 'Nagarze Xian', '西藏自治区山南地区浪卡子县', 'NAX');
INSERT INTO `t_area` VALUES ('3094', '322', '日喀则市', '27-322-3094', '3', 'Xigaze Shi', '西藏自治区日喀则地区日喀则市', 'XIG');
INSERT INTO `t_area` VALUES ('3095', '322', '南木林县', '27-322-3095', '3', 'Namling Xian', '西藏自治区日喀则地区南木林县', 'NAM');
INSERT INTO `t_area` VALUES ('3096', '322', '江孜县', '27-322-3096', '3', 'Gyangze Xian', '西藏自治区日喀则地区江孜县', 'GYZ');
INSERT INTO `t_area` VALUES ('3097', '322', '定日县', '27-322-3097', '3', 'Tingri Xian', '西藏自治区日喀则地区定日县', 'TIN');
INSERT INTO `t_area` VALUES ('3098', '322', '萨迦县', '27-322-3098', '3', 'Sa,gya Xian', '西藏自治区日喀则地区萨迦县', 'SGX');
INSERT INTO `t_area` VALUES ('3099', '322', '拉孜县', '27-322-3099', '3', 'Lhaze Xian', '西藏自治区日喀则地区拉孜县', 'LAZ');
INSERT INTO `t_area` VALUES ('3100', '322', '昂仁县', '27-322-3100', '3', 'Ngamring Xian', '西藏自治区日喀则地区昂仁县', 'NGA');
INSERT INTO `t_area` VALUES ('3101', '322', '谢通门县', '27-322-3101', '3', 'Xaitongmoin Xian', '西藏自治区日喀则地区谢通门县', 'XTM');
INSERT INTO `t_area` VALUES ('3102', '322', '白朗县', '27-322-3102', '3', 'Bainang Xian', '西藏自治区日喀则地区白朗县', 'BAI');
INSERT INTO `t_area` VALUES ('3103', '322', '仁布县', '27-322-3103', '3', 'Rinbung Xian', '西藏自治区日喀则地区仁布县', 'RIN');
INSERT INTO `t_area` VALUES ('3104', '322', '康马县', '27-322-3104', '3', 'Kangmar Xian', '西藏自治区日喀则地区康马县', 'KAN');
INSERT INTO `t_area` VALUES ('3105', '322', '定结县', '27-322-3105', '3', 'Dinggye Xian', '西藏自治区日喀则地区定结县', 'DIN');
INSERT INTO `t_area` VALUES ('3106', '322', '仲巴县', '27-322-3106', '3', 'Zhongba Xian', '西藏自治区日喀则地区仲巴县', 'ZHB');
INSERT INTO `t_area` VALUES ('3107', '322', '亚东县', '27-322-3107', '3', 'Yadong(Chomo) Xian', '西藏自治区日喀则地区亚东县', 'YDZ');
INSERT INTO `t_area` VALUES ('3108', '322', '吉隆县', '27-322-3108', '3', 'Gyirong Xian', '西藏自治区日喀则地区吉隆县', 'GIR');
INSERT INTO `t_area` VALUES ('3109', '322', '聂拉木县', '27-322-3109', '3', 'Nyalam Xian', '西藏自治区日喀则地区聂拉木县', 'NYA');
INSERT INTO `t_area` VALUES ('3110', '322', '萨嘎县', '27-322-3110', '3', 'Saga Xian', '西藏自治区日喀则地区萨嘎县', 'SAG');
INSERT INTO `t_area` VALUES ('3111', '322', '岗巴县', '27-322-3111', '3', 'Gamba Xian', '西藏自治区日喀则地区岗巴县', 'GAM');
INSERT INTO `t_area` VALUES ('3112', '323', '那曲县', '27-323-3112', '3', 'Nagqu Xian', '西藏自治区那曲地区那曲县', 'NAG');
INSERT INTO `t_area` VALUES ('3113', '323', '嘉黎县', '27-323-3113', '3', 'Lhari Xian', '西藏自治区那曲地区嘉黎县', 'LHR');
INSERT INTO `t_area` VALUES ('3114', '323', '比如县', '27-323-3114', '3', 'Biru Xian', '西藏自治区那曲地区比如县', 'BRU');
INSERT INTO `t_area` VALUES ('3115', '323', '聂荣县', '27-323-3115', '3', 'Nyainrong Xian', '西藏自治区那曲地区聂荣县', 'NRO');
INSERT INTO `t_area` VALUES ('3116', '323', '安多县', '27-323-3116', '3', 'Amdo Xian', '西藏自治区那曲地区安多县', 'AMD');
INSERT INTO `t_area` VALUES ('3117', '323', '申扎县', '27-323-3117', '3', 'Xainza Xian', '西藏自治区那曲地区申扎县', 'XZX');
INSERT INTO `t_area` VALUES ('3118', '323', '索县', '27-323-3118', '3', 'Sog Xian', '西藏自治区那曲地区索县', 'SOG');
INSERT INTO `t_area` VALUES ('3119', '323', '班戈县', '27-323-3119', '3', 'Bangoin Xian', '西藏自治区那曲地区班戈县', 'BGX');
INSERT INTO `t_area` VALUES ('3120', '323', '巴青县', '27-323-3120', '3', 'Baqen Xian', '西藏自治区那曲地区巴青县', 'BQE');
INSERT INTO `t_area` VALUES ('3121', '323', '尼玛县', '27-323-3121', '3', 'Nyima Xian', '西藏自治区那曲地区尼玛县', 'NYX');
INSERT INTO `t_area` VALUES ('3122', '324', '普兰县', '27-324-3122', '3', 'Burang Xian', '西藏自治区阿里地区普兰县', 'BUR');
INSERT INTO `t_area` VALUES ('3123', '324', '札达县', '27-324-3123', '3', 'Zanda Xian', '西藏自治区阿里地区札达县', 'ZAN');
INSERT INTO `t_area` VALUES ('3124', '324', '噶尔县', '27-324-3124', '3', 'Gar Xian', '西藏自治区阿里地区噶尔县', 'GAR');
INSERT INTO `t_area` VALUES ('3125', '324', '日土县', '27-324-3125', '3', 'Rutog Xian', '西藏自治区阿里地区日土县', 'RUT');
INSERT INTO `t_area` VALUES ('3126', '324', '革吉县', '27-324-3126', '3', 'Ge,gyai Xian', '西藏自治区阿里地区革吉县', 'GEG');
INSERT INTO `t_area` VALUES ('3127', '324', '改则县', '27-324-3127', '3', 'Gerze Xian', '西藏自治区阿里地区改则县', 'GER');
INSERT INTO `t_area` VALUES ('3128', '324', '措勤县', '27-324-3128', '3', 'Coqen Xian', '西藏自治区阿里地区措勤县', 'COQ');
INSERT INTO `t_area` VALUES ('3129', '325', '林芝县', '27-325-3129', '3', 'Nyingchi Xian', '西藏自治区林芝地区林芝县', 'NYI');
INSERT INTO `t_area` VALUES ('3130', '325', '工布江达县', '27-325-3130', '3', 'Gongbo,gyamda Xian', '西藏自治区林芝地区工布江达县', 'GOX');
INSERT INTO `t_area` VALUES ('3131', '325', '米林县', '27-325-3131', '3', 'Mainling Xian', '西藏自治区林芝地区米林县', 'MAX');
INSERT INTO `t_area` VALUES ('3132', '325', '墨脱县', '27-325-3132', '3', 'Metog Xian', '西藏自治区林芝地区墨脱县', 'MET');
INSERT INTO `t_area` VALUES ('3133', '325', '波密县', '27-325-3133', '3', 'Bomi(Bowo) Xian', '西藏自治区林芝地区波密县', 'BMI');
INSERT INTO `t_area` VALUES ('3134', '325', '察隅县', '27-325-3134', '3', 'Zayv Xian', '西藏自治区林芝地区察隅县', 'ZAY');
INSERT INTO `t_area` VALUES ('3135', '325', '朗县', '27-325-3135', '3', 'Nang Xian', '西藏自治区林芝地区朗县', 'NGX');
INSERT INTO `t_area` VALUES ('3137', '326', '新城区', '28-326-3137', '3', 'Xincheng Qu', '陕西省西安市新城区', 'XCK');
INSERT INTO `t_area` VALUES ('3138', '326', '碑林区', '28-326-3138', '3', 'Beilin Qu', '陕西省西安市碑林区', 'BLQ');
INSERT INTO `t_area` VALUES ('3139', '326', '莲湖区', '28-326-3139', '3', 'Lianhu Qu', '陕西省西安市莲湖区', 'LHU');
INSERT INTO `t_area` VALUES ('3140', '326', '灞桥区', '28-326-3140', '3', 'Baqiao Qu', '陕西省西安市灞桥区', 'BQQ');
INSERT INTO `t_area` VALUES ('3141', '326', '未央区', '28-326-3141', '3', 'Weiyang Qu', '陕西省西安市未央区', '2');
INSERT INTO `t_area` VALUES ('3142', '326', '雁塔区', '28-326-3142', '3', 'Yanta Qu', '陕西省西安市雁塔区', 'YTA');
INSERT INTO `t_area` VALUES ('3143', '326', '阎良区', '28-326-3143', '3', 'Yanliang Qu', '陕西省西安市阎良区', 'YLQ');
INSERT INTO `t_area` VALUES ('3144', '326', '临潼区', '28-326-3144', '3', 'Lintong Qu', '陕西省西安市临潼区', 'LTG');
INSERT INTO `t_area` VALUES ('3145', '326', '长安区', '28-326-3145', '3', 'Changan Qu', '陕西省西安市长安区', '2');
INSERT INTO `t_area` VALUES ('3146', '326', '蓝田县', '28-326-3146', '3', 'Lantian Xian', '陕西省西安市蓝田县', 'LNT');
INSERT INTO `t_area` VALUES ('3147', '326', '周至县', '28-326-3147', '3', 'Zhouzhi Xian', '陕西省西安市周至县', 'ZOZ');
INSERT INTO `t_area` VALUES ('3148', '326', '户县', '28-326-3148', '3', 'Hu Xian', '陕西省西安市户县', 'HUX');
INSERT INTO `t_area` VALUES ('3149', '326', '高陵县', '28-326-3149', '3', 'Gaoling Xian', '陕西省西安市高陵县', 'GLS');
INSERT INTO `t_area` VALUES ('3151', '327', '王益区', '28-327-3151', '3', 'Wangyi Qu', '陕西省铜川市王益区', '2');
INSERT INTO `t_area` VALUES ('3152', '327', '印台区', '28-327-3152', '3', 'Yintai Qu', '陕西省铜川市印台区', '2');
INSERT INTO `t_area` VALUES ('3153', '327', '耀州区', '28-327-3153', '3', 'Yaozhou Qu', '陕西省铜川市耀州区', '2');
INSERT INTO `t_area` VALUES ('3154', '327', '宜君县', '28-327-3154', '3', 'Yijun Xian', '陕西省铜川市宜君县', 'YJU');
INSERT INTO `t_area` VALUES ('3156', '328', '渭滨区', '28-328-3156', '3', 'Weibin Qu', '陕西省宝鸡市渭滨区', 'WBQ');
INSERT INTO `t_area` VALUES ('3157', '328', '金台区', '28-328-3157', '3', 'Jintai Qu', '陕西省宝鸡市金台区', 'JTQ');
INSERT INTO `t_area` VALUES ('3158', '328', '陈仓区', '28-328-3158', '3', 'Chencang Qu', '陕西省宝鸡市陈仓区', '2');
INSERT INTO `t_area` VALUES ('3159', '328', '凤翔县', '28-328-3159', '3', 'Fengxiang Xian', '陕西省宝鸡市凤翔县', 'FXG');
INSERT INTO `t_area` VALUES ('3160', '328', '岐山县', '28-328-3160', '3', 'Qishan Xian', '陕西省宝鸡市岐山县', 'QIS');
INSERT INTO `t_area` VALUES ('3161', '328', '扶风县', '28-328-3161', '3', 'Fufeng Xian', '陕西省宝鸡市扶风县', 'FFG');
INSERT INTO `t_area` VALUES ('3162', '328', '眉县', '28-328-3162', '3', 'Mei Xian', '陕西省宝鸡市眉县', 'MEI');
INSERT INTO `t_area` VALUES ('3163', '328', '陇县', '28-328-3163', '3', 'Long Xian', '陕西省宝鸡市陇县', 'LON');
INSERT INTO `t_area` VALUES ('3164', '328', '千阳县', '28-328-3164', '3', 'Qianyang Xian', '陕西省宝鸡市千阳县', 'QNY');
INSERT INTO `t_area` VALUES ('3165', '328', '麟游县', '28-328-3165', '3', 'Linyou Xian', '陕西省宝鸡市麟游县', 'LYP');
INSERT INTO `t_area` VALUES ('3166', '328', '凤县', '28-328-3166', '3', 'Feng Xian', '陕西省宝鸡市凤县', 'FEG');
INSERT INTO `t_area` VALUES ('3167', '328', '太白县', '28-328-3167', '3', 'Taibai Xian', '陕西省宝鸡市太白县', 'TBA');
INSERT INTO `t_area` VALUES ('3169', '329', '秦都区', '28-329-3169', '3', 'Qindu Qu', '陕西省咸阳市秦都区', 'QDU');
INSERT INTO `t_area` VALUES ('3170', '329', '杨陵区', '28-329-3170', '3', 'Yangling Qu', '陕西省咸阳市杨陵区', 'YGL');
INSERT INTO `t_area` VALUES ('3171', '329', '渭城区', '28-329-3171', '3', 'Weicheng Qu', '陕西省咸阳市渭城区', 'WIC');
INSERT INTO `t_area` VALUES ('3172', '329', '三原县', '28-329-3172', '3', 'Sanyuan Xian', '陕西省咸阳市三原县', 'SYN');
INSERT INTO `t_area` VALUES ('3173', '329', '泾阳县', '28-329-3173', '3', 'Jingyang Xian', '陕西省咸阳市泾阳县', 'JGY');
INSERT INTO `t_area` VALUES ('3174', '329', '乾县', '28-329-3174', '3', 'Qian Xian', '陕西省咸阳市乾县', 'QIA');
INSERT INTO `t_area` VALUES ('3175', '329', '礼泉县', '28-329-3175', '3', 'Liquan Xian', '陕西省咸阳市礼泉县', 'LIQ');
INSERT INTO `t_area` VALUES ('3176', '329', '永寿县', '28-329-3176', '3', 'Yongshou Xian', '陕西省咸阳市永寿县', 'YSH');
INSERT INTO `t_area` VALUES ('3177', '329', '彬县', '28-329-3177', '3', 'Bin Xian', '陕西省咸阳市彬县', 'BIX');
INSERT INTO `t_area` VALUES ('3178', '329', '长武县', '28-329-3178', '3', 'Changwu Xian', '陕西省咸阳市长武县', 'CWU');
INSERT INTO `t_area` VALUES ('3179', '329', '旬邑县', '28-329-3179', '3', 'Xunyi Xian', '陕西省咸阳市旬邑县', 'XNY');
INSERT INTO `t_area` VALUES ('3180', '329', '淳化县', '28-329-3180', '3', 'Chunhua Xian', '陕西省咸阳市淳化县', 'CHU');
INSERT INTO `t_area` VALUES ('3181', '329', '武功县', '28-329-3181', '3', 'Wugong Xian', '陕西省咸阳市武功县', 'WGG');
INSERT INTO `t_area` VALUES ('3182', '329', '兴平市', '28-329-3182', '3', 'Xingping Shi', '陕西省咸阳市兴平市', 'XPG');
INSERT INTO `t_area` VALUES ('3184', '330', '临渭区', '28-330-3184', '3', 'Linwei Qu', '陕西省渭南市临渭区', 'LWE');
INSERT INTO `t_area` VALUES ('3185', '330', '华县', '28-330-3185', '3', 'Hua Xian', '陕西省渭南市华县', 'HXN');
INSERT INTO `t_area` VALUES ('3186', '330', '潼关县', '28-330-3186', '3', 'Tongguan Xian', '陕西省渭南市潼关县', 'TGN');
INSERT INTO `t_area` VALUES ('3187', '330', '大荔县', '28-330-3187', '3', 'Dali Xian', '陕西省渭南市大荔县', 'DAL');
INSERT INTO `t_area` VALUES ('3188', '330', '合阳县', '28-330-3188', '3', 'Heyang Xian', '陕西省渭南市合阳县', 'HYK');
INSERT INTO `t_area` VALUES ('3189', '330', '澄城县', '28-330-3189', '3', 'Chengcheng Xian', '陕西省渭南市澄城县', 'CCG');
INSERT INTO `t_area` VALUES ('3190', '330', '蒲城县', '28-330-3190', '3', 'Pucheng Xian', '陕西省渭南市蒲城县', 'PUC');
INSERT INTO `t_area` VALUES ('3191', '330', '白水县', '28-330-3191', '3', 'Baishui Xian', '陕西省渭南市白水县', 'BSU');
INSERT INTO `t_area` VALUES ('3192', '330', '富平县', '28-330-3192', '3', 'Fuping Xian', '陕西省渭南市富平县', 'FPX');
INSERT INTO `t_area` VALUES ('3193', '330', '韩城市', '28-330-3193', '3', 'Hancheng Shi', '陕西省渭南市韩城市', 'HCE');
INSERT INTO `t_area` VALUES ('3194', '330', '华阴市', '28-330-3194', '3', 'Huayin Shi', '陕西省渭南市华阴市', 'HYI');
INSERT INTO `t_area` VALUES ('3196', '331', '宝塔区', '28-331-3196', '3', 'Baota Qu', '陕西省延安市宝塔区', 'BTA');
INSERT INTO `t_area` VALUES ('3197', '331', '延长县', '28-331-3197', '3', 'Yanchang Xian', '陕西省延安市延长县', 'YCA');
INSERT INTO `t_area` VALUES ('3198', '331', '延川县', '28-331-3198', '3', 'Yanchuan Xian', '陕西省延安市延川县', 'YCT');
INSERT INTO `t_area` VALUES ('3199', '331', '子长县', '28-331-3199', '3', 'Zichang Xian', '陕西省延安市子长县', 'ZCA');
INSERT INTO `t_area` VALUES ('3200', '331', '安塞县', '28-331-3200', '3', 'Ansai Xian', '陕西省延安市安塞县', 'ANS');
INSERT INTO `t_area` VALUES ('3201', '331', '志丹县', '28-331-3201', '3', 'Zhidan Xian', '陕西省延安市志丹县', 'ZDN');
INSERT INTO `t_area` VALUES ('3202', '331', '吴起县', '28-331-3202', '3', 'Wuqi Xian', '陕西省延安市吴起县', '2');
INSERT INTO `t_area` VALUES ('3203', '331', '甘泉县', '28-331-3203', '3', 'Ganquan Xian', '陕西省延安市甘泉县', 'GQN');
INSERT INTO `t_area` VALUES ('3204', '331', '富县', '28-331-3204', '3', 'Fu Xian', '陕西省延安市富县', 'FUX');
INSERT INTO `t_area` VALUES ('3205', '331', '洛川县', '28-331-3205', '3', 'Luochuan Xian', '陕西省延安市洛川县', 'LCW');
INSERT INTO `t_area` VALUES ('3206', '331', '宜川县', '28-331-3206', '3', 'Yichuan Xian', '陕西省延安市宜川县', 'YIC');
INSERT INTO `t_area` VALUES ('3207', '331', '黄龙县', '28-331-3207', '3', 'Huanglong Xian', '陕西省延安市黄龙县', 'HGL');
INSERT INTO `t_area` VALUES ('3208', '331', '黄陵县', '28-331-3208', '3', 'Huangling Xian', '陕西省延安市黄陵县', 'HLG');
INSERT INTO `t_area` VALUES ('3210', '332', '汉台区', '28-332-3210', '3', 'Hantai Qu', '陕西省汉中市汉台区', 'HTQ');
INSERT INTO `t_area` VALUES ('3211', '332', '南郑县', '28-332-3211', '3', 'Nanzheng Xian', '陕西省汉中市南郑县', 'NZG');
INSERT INTO `t_area` VALUES ('3212', '332', '城固县', '28-332-3212', '3', 'Chenggu Xian', '陕西省汉中市城固县', 'CGU');
INSERT INTO `t_area` VALUES ('3213', '332', '洋县', '28-332-3213', '3', 'Yang Xian', '陕西省汉中市洋县', 'YGX');
INSERT INTO `t_area` VALUES ('3214', '332', '西乡县', '28-332-3214', '3', 'Xixiang Xian', '陕西省汉中市西乡县', 'XXA');
INSERT INTO `t_area` VALUES ('3215', '332', '勉县', '28-332-3215', '3', 'Mian Xian', '陕西省汉中市勉县', 'MIA');
INSERT INTO `t_area` VALUES ('3216', '332', '宁强县', '28-332-3216', '3', 'Ningqiang Xian', '陕西省汉中市宁强县', 'NQG');
INSERT INTO `t_area` VALUES ('3217', '332', '略阳县', '28-332-3217', '3', 'Lueyang Xian', '陕西省汉中市略阳县', 'LYC');
INSERT INTO `t_area` VALUES ('3218', '332', '镇巴县', '28-332-3218', '3', 'Zhenba Xian', '陕西省汉中市镇巴县', 'ZBA');
INSERT INTO `t_area` VALUES ('3219', '332', '留坝县', '28-332-3219', '3', 'Liuba Xian', '陕西省汉中市留坝县', 'LBA');
INSERT INTO `t_area` VALUES ('3220', '332', '佛坪县', '28-332-3220', '3', 'Foping Xian', '陕西省汉中市佛坪县', 'FPG');
INSERT INTO `t_area` VALUES ('3222', '333', '榆阳区', '28-333-3222', '3', 'Yuyang Qu', '陕西省榆林市榆阳区', '2');
INSERT INTO `t_area` VALUES ('3223', '333', '神木县', '28-333-3223', '3', 'Shenmu Xian', '陕西省榆林市神木县', '2');
INSERT INTO `t_area` VALUES ('3224', '333', '府谷县', '28-333-3224', '3', 'Fugu Xian', '陕西省榆林市府谷县', '2');
INSERT INTO `t_area` VALUES ('3225', '333', '横山县', '28-333-3225', '3', 'Hengshan Xian', '陕西省榆林市横山县', '2');
INSERT INTO `t_area` VALUES ('3226', '333', '靖边县', '28-333-3226', '3', 'Jingbian Xian', '陕西省榆林市靖边县', '2');
INSERT INTO `t_area` VALUES ('3227', '333', '定边县', '28-333-3227', '3', 'Dingbian Xian', '陕西省榆林市定边县', '2');
INSERT INTO `t_area` VALUES ('3228', '333', '绥德县', '28-333-3228', '3', 'Suide Xian', '陕西省榆林市绥德县', '2');
INSERT INTO `t_area` VALUES ('3229', '333', '米脂县', '28-333-3229', '3', 'Mizhi Xian', '陕西省榆林市米脂县', '2');
INSERT INTO `t_area` VALUES ('3230', '333', '佳县', '28-333-3230', '3', 'Jia Xian', '陕西省榆林市佳县', '2');
INSERT INTO `t_area` VALUES ('3231', '333', '吴堡县', '28-333-3231', '3', 'Wubu Xian', '陕西省榆林市吴堡县', '2');
INSERT INTO `t_area` VALUES ('3232', '333', '清涧县', '28-333-3232', '3', 'Qingjian Xian', '陕西省榆林市清涧县', '2');
INSERT INTO `t_area` VALUES ('3233', '333', '子洲县', '28-333-3233', '3', 'Zizhou Xian', '陕西省榆林市子洲县', '2');
INSERT INTO `t_area` VALUES ('3235', '334', '汉滨区', '28-334-3235', '3', 'Hanbin Qu', '陕西省安康市汉滨区', '2');
INSERT INTO `t_area` VALUES ('3236', '334', '汉阴县', '28-334-3236', '3', 'Hanyin Xian', '陕西省安康市汉阴县', '2');
INSERT INTO `t_area` VALUES ('3237', '334', '石泉县', '28-334-3237', '3', 'Shiquan Xian', '陕西省安康市石泉县', '2');
INSERT INTO `t_area` VALUES ('3238', '334', '宁陕县', '28-334-3238', '3', 'Ningshan Xian', '陕西省安康市宁陕县', '2');
INSERT INTO `t_area` VALUES ('3239', '334', '紫阳县', '28-334-3239', '3', 'Ziyang Xian', '陕西省安康市紫阳县', '2');
INSERT INTO `t_area` VALUES ('3240', '334', '岚皋县', '28-334-3240', '3', 'Langao Xian', '陕西省安康市岚皋县', '2');
INSERT INTO `t_area` VALUES ('3241', '334', '平利县', '28-334-3241', '3', 'Pingli Xian', '陕西省安康市平利县', '2');
INSERT INTO `t_area` VALUES ('3242', '334', '镇坪县', '28-334-3242', '3', 'Zhenping Xian', '陕西省安康市镇坪县', '2');
INSERT INTO `t_area` VALUES ('3243', '334', '旬阳县', '28-334-3243', '3', 'Xunyang Xian', '陕西省安康市旬阳县', '2');
INSERT INTO `t_area` VALUES ('3244', '334', '白河县', '28-334-3244', '3', 'Baihe Xian', '陕西省安康市白河县', '2');
INSERT INTO `t_area` VALUES ('3246', '335', '商州区', '28-335-3246', '3', 'Shangzhou Qu', '陕西省商洛市商州区', '2');
INSERT INTO `t_area` VALUES ('3247', '335', '洛南县', '28-335-3247', '3', 'Luonan Xian', '陕西省商洛市洛南县', '2');
INSERT INTO `t_area` VALUES ('3248', '335', '丹凤县', '28-335-3248', '3', 'Danfeng Xian', '陕西省商洛市丹凤县', '2');
INSERT INTO `t_area` VALUES ('3249', '335', '商南县', '28-335-3249', '3', 'Shangnan Xian', '陕西省商洛市商南县', '2');
INSERT INTO `t_area` VALUES ('3250', '335', '山阳县', '28-335-3250', '3', 'Shanyang Xian', '陕西省商洛市山阳县', '2');
INSERT INTO `t_area` VALUES ('3251', '335', '镇安县', '28-335-3251', '3', 'Zhen,an Xian', '陕西省商洛市镇安县', '2');
INSERT INTO `t_area` VALUES ('3252', '335', '柞水县', '28-335-3252', '3', 'Zhashui Xian', '陕西省商洛市柞水县', '2');
INSERT INTO `t_area` VALUES ('3254', '336', '城关区', '29-336-3254', '3', 'Chengguan Qu', '甘肃省兰州市城关区', 'CLZ');
INSERT INTO `t_area` VALUES ('3255', '336', '七里河区', '29-336-3255', '3', 'Qilihe Qu', '甘肃省兰州市七里河区', 'QLH');
INSERT INTO `t_area` VALUES ('3256', '336', '西固区', '29-336-3256', '3', 'Xigu Qu', '甘肃省兰州市西固区', 'XGU');
INSERT INTO `t_area` VALUES ('3257', '336', '安宁区', '29-336-3257', '3', 'Anning Qu', '甘肃省兰州市安宁区', 'ANQ');
INSERT INTO `t_area` VALUES ('3258', '336', '红古区', '29-336-3258', '3', 'Honggu Qu', '甘肃省兰州市红古区', 'HOG');
INSERT INTO `t_area` VALUES ('3259', '336', '永登县', '29-336-3259', '3', 'Yongdeng Xian', '甘肃省兰州市永登县', 'YDG');
INSERT INTO `t_area` VALUES ('3260', '336', '皋兰县', '29-336-3260', '3', 'Gaolan Xian', '甘肃省兰州市皋兰县', 'GAL');
INSERT INTO `t_area` VALUES ('3261', '336', '榆中县', '29-336-3261', '3', 'Yuzhong Xian', '甘肃省兰州市榆中县', 'YZX');
INSERT INTO `t_area` VALUES ('3264', '338', '金川区', '29-338-3264', '3', 'Jinchuan Qu', '甘肃省金昌市金川区', 'JCU');
INSERT INTO `t_area` VALUES ('3265', '338', '永昌县', '29-338-3265', '3', 'Yongchang Xian', '甘肃省金昌市永昌县', 'YCF');
INSERT INTO `t_area` VALUES ('3267', '339', '白银区', '29-339-3267', '3', 'Baiyin Qu', '甘肃省白银市白银区', 'BYB');
INSERT INTO `t_area` VALUES ('3268', '339', '平川区', '29-339-3268', '3', 'Pingchuan Qu', '甘肃省白银市平川区', 'PCQ');
INSERT INTO `t_area` VALUES ('3269', '339', '靖远县', '29-339-3269', '3', 'Jingyuan Xian', '甘肃省白银市靖远县', 'JYH');
INSERT INTO `t_area` VALUES ('3270', '339', '会宁县', '29-339-3270', '3', 'Huining xian', '甘肃省白银市会宁县', 'HNI');
INSERT INTO `t_area` VALUES ('3271', '339', '景泰县', '29-339-3271', '3', 'Jingtai Xian', '甘肃省白银市景泰县', 'JGT');
INSERT INTO `t_area` VALUES ('3274', '340', '秦州区', '29-340-3274', '3', 'Beidao Qu', '甘肃省天水市秦州区', '2');
INSERT INTO `t_area` VALUES ('3275', '340', '清水县', '29-340-3275', '3', 'Qingshui Xian', '甘肃省天水市清水县', 'QSG');
INSERT INTO `t_area` VALUES ('3276', '340', '秦安县', '29-340-3276', '3', 'Qin,an Xian', '甘肃省天水市秦安县', 'QNA');
INSERT INTO `t_area` VALUES ('3277', '340', '甘谷县', '29-340-3277', '3', 'Gangu Xian', '甘肃省天水市甘谷县', 'GGU');
INSERT INTO `t_area` VALUES ('3278', '340', '武山县', '29-340-3278', '3', 'Wushan Xian', '甘肃省天水市武山县', 'WSX');
INSERT INTO `t_area` VALUES ('3279', '340', '张家川回族自治县', '29-340-3279', '3', 'Zhangjiachuan Huizu Zizhixian', '甘肃省天水市张家川回族自治县', 'ZJC');
INSERT INTO `t_area` VALUES ('3281', '341', '凉州区', '29-341-3281', '3', 'Liangzhou Qu', '甘肃省武威市凉州区', '2');
INSERT INTO `t_area` VALUES ('3282', '341', '民勤县', '29-341-3282', '3', 'Minqin Xian', '甘肃省武威市民勤县', '2');
INSERT INTO `t_area` VALUES ('3283', '341', '古浪县', '29-341-3283', '3', 'Gulang Xian', '甘肃省武威市古浪县', '2');
INSERT INTO `t_area` VALUES ('3284', '341', '天祝藏族自治县', '29-341-3284', '3', 'Tianzhu Zangzu Zizhixian', '甘肃省武威市天祝藏族自治县', '2');
INSERT INTO `t_area` VALUES ('3286', '342', '甘州区', '29-342-3286', '3', 'Ganzhou Qu', '甘肃省张掖市甘州区', '2');
INSERT INTO `t_area` VALUES ('3287', '342', '肃南裕固族自治县', '29-342-3287', '3', 'Sunan Yugurzu Zizhixian', '甘肃省张掖市肃南裕固族自治县', '2');
INSERT INTO `t_area` VALUES ('3288', '342', '民乐县', '29-342-3288', '3', 'Minle Xian', '甘肃省张掖市民乐县', '2');
INSERT INTO `t_area` VALUES ('3289', '342', '临泽县', '29-342-3289', '3', 'Linze Xian', '甘肃省张掖市临泽县', '2');
INSERT INTO `t_area` VALUES ('3290', '342', '高台县', '29-342-3290', '3', 'Gaotai Xian', '甘肃省张掖市高台县', '2');
INSERT INTO `t_area` VALUES ('3291', '342', '山丹县', '29-342-3291', '3', 'Shandan Xian', '甘肃省张掖市山丹县', '2');
INSERT INTO `t_area` VALUES ('3293', '343', '崆峒区', '29-343-3293', '3', 'Kongdong Qu', '甘肃省平凉市崆峒区', '2');
INSERT INTO `t_area` VALUES ('3294', '343', '泾川县', '29-343-3294', '3', 'Jingchuan Xian', '甘肃省平凉市泾川县', '2');
INSERT INTO `t_area` VALUES ('3295', '343', '灵台县', '29-343-3295', '3', 'Lingtai Xian', '甘肃省平凉市灵台县', '2');
INSERT INTO `t_area` VALUES ('3296', '343', '崇信县', '29-343-3296', '3', 'Chongxin Xian', '甘肃省平凉市崇信县', '2');
INSERT INTO `t_area` VALUES ('3297', '343', '华亭县', '29-343-3297', '3', 'Huating Xian', '甘肃省平凉市华亭县', '2');
INSERT INTO `t_area` VALUES ('3298', '343', '庄浪县', '29-343-3298', '3', 'Zhuanglang Xian', '甘肃省平凉市庄浪县', '2');
INSERT INTO `t_area` VALUES ('3299', '343', '静宁县', '29-343-3299', '3', 'Jingning Xian', '甘肃省平凉市静宁县', '2');
INSERT INTO `t_area` VALUES ('3301', '344', '肃州区', '29-344-3301', '3', 'Suzhou Qu', '甘肃省酒泉市肃州区', '2');
INSERT INTO `t_area` VALUES ('3302', '344', '金塔县', '29-344-3302', '3', 'Jinta Xian', '甘肃省酒泉市金塔县', '2');
INSERT INTO `t_area` VALUES ('3304', '344', '肃北蒙古族自治县', '29-344-3304', '3', 'Subei Monguzu Zizhixian', '甘肃省酒泉市肃北蒙古族自治县', '2');
INSERT INTO `t_area` VALUES ('3305', '344', '阿克塞哈萨克族自治县', '29-344-3305', '3', 'Aksay Kazakzu Zizhixian', '甘肃省酒泉市阿克塞哈萨克族自治县', '2');
INSERT INTO `t_area` VALUES ('3306', '344', '玉门市', '29-344-3306', '3', 'Yumen Shi', '甘肃省酒泉市玉门市', '2');
INSERT INTO `t_area` VALUES ('3307', '344', '敦煌市', '29-344-3307', '3', 'Dunhuang Shi', '甘肃省酒泉市敦煌市', '2');
INSERT INTO `t_area` VALUES ('3309', '345', '西峰区', '29-345-3309', '3', 'Xifeng Qu', '甘肃省庆阳市西峰区', '2');
INSERT INTO `t_area` VALUES ('3310', '345', '庆城县', '29-345-3310', '3', 'Qingcheng Xian', '甘肃省庆阳市庆城县', '2');
INSERT INTO `t_area` VALUES ('3311', '345', '环县', '29-345-3311', '3', 'Huan Xian', '甘肃省庆阳市环县', '2');
INSERT INTO `t_area` VALUES ('3312', '345', '华池县', '29-345-3312', '3', 'Huachi Xian', '甘肃省庆阳市华池县', '2');
INSERT INTO `t_area` VALUES ('3313', '345', '合水县', '29-345-3313', '3', 'Heshui Xian', '甘肃省庆阳市合水县', '2');
INSERT INTO `t_area` VALUES ('3314', '345', '正宁县', '29-345-3314', '3', 'Zhengning Xian', '甘肃省庆阳市正宁县', '2');
INSERT INTO `t_area` VALUES ('3315', '345', '宁县', '29-345-3315', '3', 'Ning Xian', '甘肃省庆阳市宁县', '2');
INSERT INTO `t_area` VALUES ('3316', '345', '镇原县', '29-345-3316', '3', 'Zhenyuan Xian', '甘肃省庆阳市镇原县', '2');
INSERT INTO `t_area` VALUES ('3318', '346', '安定区', '29-346-3318', '3', 'Anding Qu', '甘肃省定西市安定区', '2');
INSERT INTO `t_area` VALUES ('3319', '346', '通渭县', '29-346-3319', '3', 'Tongwei Xian', '甘肃省定西市通渭县', '2');
INSERT INTO `t_area` VALUES ('3320', '346', '陇西县', '29-346-3320', '3', 'Longxi Xian', '甘肃省定西市陇西县', '2');
INSERT INTO `t_area` VALUES ('3321', '346', '渭源县', '29-346-3321', '3', 'Weiyuan Xian', '甘肃省定西市渭源县', '2');
INSERT INTO `t_area` VALUES ('3322', '346', '临洮县', '29-346-3322', '3', 'Lintao Xian', '甘肃省定西市临洮县', '2');
INSERT INTO `t_area` VALUES ('3323', '346', '漳县', '29-346-3323', '3', 'Zhang Xian', '甘肃省定西市漳县', '2');
INSERT INTO `t_area` VALUES ('3324', '346', '岷县', '29-346-3324', '3', 'Min Xian', '甘肃省定西市岷县', '2');
INSERT INTO `t_area` VALUES ('3326', '347', '武都区', '29-347-3326', '3', 'Wudu Qu', '甘肃省陇南市武都区', '2');
INSERT INTO `t_area` VALUES ('3327', '347', '成县', '29-347-3327', '3', 'Cheng Xian', '甘肃省陇南市成县', '2');
INSERT INTO `t_area` VALUES ('3328', '347', '文县', '29-347-3328', '3', 'Wen Xian', '甘肃省陇南市文县', '2');
INSERT INTO `t_area` VALUES ('3329', '347', '宕昌县', '29-347-3329', '3', 'Dangchang Xian', '甘肃省陇南市宕昌县', '2');
INSERT INTO `t_area` VALUES ('3330', '347', '康县', '29-347-3330', '3', 'Kang Xian', '甘肃省陇南市康县', '2');
INSERT INTO `t_area` VALUES ('3331', '347', '西和县', '29-347-3331', '3', 'Xihe Xian', '甘肃省陇南市西和县', '2');
INSERT INTO `t_area` VALUES ('3332', '347', '礼县', '29-347-3332', '3', 'Li Xian', '甘肃省陇南市礼县', '2');
INSERT INTO `t_area` VALUES ('3333', '347', '徽县', '29-347-3333', '3', 'Hui Xian', '甘肃省陇南市徽县', '2');
INSERT INTO `t_area` VALUES ('3334', '347', '两当县', '29-347-3334', '3', 'Liangdang Xian', '甘肃省陇南市两当县', '2');
INSERT INTO `t_area` VALUES ('3335', '348', '临夏市', '29-348-3335', '3', 'Linxia Shi', '甘肃省临夏回族自治州临夏市', 'LXR');
INSERT INTO `t_area` VALUES ('3336', '348', '临夏县', '29-348-3336', '3', 'Linxia Xian', '甘肃省临夏回族自治州临夏县', 'LXF');
INSERT INTO `t_area` VALUES ('3337', '348', '康乐县', '29-348-3337', '3', 'Kangle Xian', '甘肃省临夏回族自治州康乐县', 'KLE');
INSERT INTO `t_area` VALUES ('3338', '348', '永靖县', '29-348-3338', '3', 'Yongjing Xian', '甘肃省临夏回族自治州永靖县', 'YJG');
INSERT INTO `t_area` VALUES ('3339', '348', '广河县', '29-348-3339', '3', 'Guanghe Xian', '甘肃省临夏回族自治州广河县', 'GHX');
INSERT INTO `t_area` VALUES ('3340', '348', '和政县', '29-348-3340', '3', 'Hezheng Xian', '甘肃省临夏回族自治州和政县', 'HZG');
INSERT INTO `t_area` VALUES ('3341', '348', '东乡族自治县', '29-348-3341', '3', 'Dongxiangzu Zizhixian', '甘肃省临夏回族自治州东乡族自治县', 'DXZ');
INSERT INTO `t_area` VALUES ('3342', '348', '积石山保安族东乡族撒拉族自治县', '29-348-3342', '3', 'Jishishan Bonanzu Dongxiangzu Salarzu Zizhixian', '甘肃省临夏回族自治州积石山保安族东乡族撒拉族自治县', 'JSN');
INSERT INTO `t_area` VALUES ('3343', '349', '合作市', '29-349-3343', '3', 'Hezuo Shi', '甘肃省甘南藏族自治州合作市', 'HEZ');
INSERT INTO `t_area` VALUES ('3344', '349', '临潭县', '29-349-3344', '3', 'Lintan Xian', '甘肃省甘南藏族自治州临潭县', 'LTN');
INSERT INTO `t_area` VALUES ('3345', '349', '卓尼县', '29-349-3345', '3', 'Jone', '甘肃省甘南藏族自治州卓尼县', 'JON');
INSERT INTO `t_area` VALUES ('3346', '349', '舟曲县', '29-349-3346', '3', 'Zhugqu Xian', '甘肃省甘南藏族自治州舟曲县', 'ZQU');
INSERT INTO `t_area` VALUES ('3347', '349', '迭部县', '29-349-3347', '3', 'Tewo Xian', '甘肃省甘南藏族自治州迭部县', 'TEW');
INSERT INTO `t_area` VALUES ('3348', '349', '玛曲县', '29-349-3348', '3', 'Maqu Xian', '甘肃省甘南藏族自治州玛曲县', 'MQU');
INSERT INTO `t_area` VALUES ('3349', '349', '碌曲县', '29-349-3349', '3', 'Luqu Xian', '甘肃省甘南藏族自治州碌曲县', 'LQU');
INSERT INTO `t_area` VALUES ('3350', '349', '夏河县', '29-349-3350', '3', 'Xiahe Xian', '甘肃省甘南藏族自治州夏河县', 'XHN');
INSERT INTO `t_area` VALUES ('3352', '350', '城东区', '30-350-3352', '3', 'Chengdong Qu', '青海省西宁市城东区', 'CDQ');
INSERT INTO `t_area` VALUES ('3353', '350', '城中区', '30-350-3353', '3', 'Chengzhong Qu', '青海省西宁市城中区', 'CZQ');
INSERT INTO `t_area` VALUES ('3354', '350', '城西区', '30-350-3354', '3', 'Chengxi Qu', '青海省西宁市城西区', 'CXQ');
INSERT INTO `t_area` VALUES ('3355', '350', '城北区', '30-350-3355', '3', 'Chengbei Qu', '青海省西宁市城北区', 'CBE');
INSERT INTO `t_area` VALUES ('3356', '350', '大通回族土族自治县', '30-350-3356', '3', 'Datong Huizu Tuzu Zizhixian', '青海省西宁市大通回族土族自治县', 'DAT');
INSERT INTO `t_area` VALUES ('3357', '350', '湟中县', '30-350-3357', '3', 'Huangzhong Xian', '青海省西宁市湟中县', '2');
INSERT INTO `t_area` VALUES ('3358', '350', '湟源县', '30-350-3358', '3', 'Huangyuan Xian', '青海省西宁市湟源县', '2');
INSERT INTO `t_area` VALUES ('3359', '351', '平安县', '30-351-3359', '3', 'Ping,an Xian', '青海省海东地区平安县', 'PAN');
INSERT INTO `t_area` VALUES ('3360', '351', '民和回族土族自治县', '30-351-3360', '3', 'Minhe Huizu Tuzu Zizhixian', '青海省海东地区民和回族土族自治县', 'MHE');
INSERT INTO `t_area` VALUES ('3361', '351', '乐都县', '30-351-3361', '3', 'Ledu Xian', '青海省海东地区乐都县', 'LDU');
INSERT INTO `t_area` VALUES ('3362', '351', '互助土族自治县', '30-351-3362', '3', 'Huzhu Tuzu Zizhixian', '青海省海东地区互助土族自治县', 'HZT');
INSERT INTO `t_area` VALUES ('3363', '351', '化隆回族自治县', '30-351-3363', '3', 'Hualong Huizu Zizhixian', '青海省海东地区化隆回族自治县', 'HLO');
INSERT INTO `t_area` VALUES ('3364', '351', '循化撒拉族自治县', '30-351-3364', '3', 'Xunhua Salazu Zizhixian', '青海省海东地区循化撒拉族自治县', 'XUH');
INSERT INTO `t_area` VALUES ('3365', '352', '门源回族自治县', '30-352-3365', '3', 'Menyuan Huizu Zizhixian', '青海省海北藏族自治州门源回族自治县', 'MYU');
INSERT INTO `t_area` VALUES ('3366', '352', '祁连县', '30-352-3366', '3', 'Qilian Xian', '青海省海北藏族自治州祁连县', 'QLN');
INSERT INTO `t_area` VALUES ('3367', '352', '海晏县', '30-352-3367', '3', 'Haiyan Xian', '青海省海北藏族自治州海晏县', 'HIY');
INSERT INTO `t_area` VALUES ('3368', '352', '刚察县', '30-352-3368', '3', 'Gangca Xian', '青海省海北藏族自治州刚察县', 'GAN');
INSERT INTO `t_area` VALUES ('3369', '353', '同仁县', '30-353-3369', '3', 'Tongren Xian', '青海省黄南藏族自治州同仁县', 'TRN');
INSERT INTO `t_area` VALUES ('3370', '353', '尖扎县', '30-353-3370', '3', 'Jainca Xian', '青海省黄南藏族自治州尖扎县', 'JAI');
INSERT INTO `t_area` VALUES ('3371', '353', '泽库县', '30-353-3371', '3', 'Zekog Xian', '青海省黄南藏族自治州泽库县', 'ZEK');
INSERT INTO `t_area` VALUES ('3372', '353', '河南蒙古族自治县', '30-353-3372', '3', 'Henan Mongolzu Zizhixian', '青海省黄南藏族自治州河南蒙古族自治县', 'HNM');
INSERT INTO `t_area` VALUES ('3373', '354', '共和县', '30-354-3373', '3', 'Gonghe Xian', '青海省海南藏族自治州共和县', 'GHE');
INSERT INTO `t_area` VALUES ('3374', '354', '同德县', '30-354-3374', '3', 'Tongde Xian', '青海省海南藏族自治州同德县', 'TDX');
INSERT INTO `t_area` VALUES ('3375', '354', '贵德县', '30-354-3375', '3', 'Guide Xian', '青海省海南藏族自治州贵德县', 'GID');
INSERT INTO `t_area` VALUES ('3376', '354', '兴海县', '30-354-3376', '3', 'Xinghai Xian', '青海省海南藏族自治州兴海县', 'XHA');
INSERT INTO `t_area` VALUES ('3377', '354', '贵南县', '30-354-3377', '3', 'Guinan Xian', '青海省海南藏族自治州贵南县', 'GNN');
INSERT INTO `t_area` VALUES ('3378', '355', '玛沁县', '30-355-3378', '3', 'Maqen Xian', '青海省果洛藏族自治州玛沁县', 'MAQ');
INSERT INTO `t_area` VALUES ('3379', '355', '班玛县', '30-355-3379', '3', 'Baima Xian', '青海省果洛藏族自治州班玛县', 'BMX');
INSERT INTO `t_area` VALUES ('3380', '355', '甘德县', '30-355-3380', '3', 'Gade Xian', '青海省果洛藏族自治州甘德县', 'GAD');
INSERT INTO `t_area` VALUES ('3381', '355', '达日县', '30-355-3381', '3', 'Tarlag Xian', '青海省果洛藏族自治州达日县', 'TAR');
INSERT INTO `t_area` VALUES ('3382', '355', '久治县', '30-355-3382', '3', 'Jigzhi Xian', '青海省果洛藏族自治州久治县', 'JUZ');
INSERT INTO `t_area` VALUES ('3383', '355', '玛多县', '30-355-3383', '3', 'Madoi Xian', '青海省果洛藏族自治州玛多县', 'MAD');
INSERT INTO `t_area` VALUES ('3384', '356', '玉树县', '30-356-3384', '3', 'Yushu Xian', '青海省玉树藏族自治州玉树县', 'YSK');
INSERT INTO `t_area` VALUES ('3385', '356', '杂多县', '30-356-3385', '3', 'Zadoi Xian', '青海省玉树藏族自治州杂多县', 'ZAD');
INSERT INTO `t_area` VALUES ('3386', '356', '称多县', '30-356-3386', '3', 'Chindu Xian', '青海省玉树藏族自治州称多县', 'CHI');
INSERT INTO `t_area` VALUES ('3387', '356', '治多县', '30-356-3387', '3', 'Zhidoi Xian', '青海省玉树藏族自治州治多县', 'ZHI');
INSERT INTO `t_area` VALUES ('3388', '356', '囊谦县', '30-356-3388', '3', 'Nangqen Xian', '青海省玉树藏族自治州囊谦县', 'NQN');
INSERT INTO `t_area` VALUES ('3389', '356', '曲麻莱县', '30-356-3389', '3', 'Qumarleb Xian', '青海省玉树藏族自治州曲麻莱县', 'QUM');
INSERT INTO `t_area` VALUES ('3390', '357', '格尔木市', '30-357-3390', '3', 'Golmud Shi', '青海省海西蒙古族藏族自治州格尔木市', 'GOS');
INSERT INTO `t_area` VALUES ('3391', '357', '德令哈市', '30-357-3391', '3', 'Delhi Shi', '青海省海西蒙古族藏族自治州德令哈市', 'DEL');
INSERT INTO `t_area` VALUES ('3392', '357', '乌兰县', '30-357-3392', '3', 'Ulan Xian', '青海省海西蒙古族藏族自治州乌兰县', 'ULA');
INSERT INTO `t_area` VALUES ('3393', '357', '都兰县', '30-357-3393', '3', 'Dulan Xian', '青海省海西蒙古族藏族自治州都兰县', 'DUL');
INSERT INTO `t_area` VALUES ('3394', '357', '天峻县', '30-357-3394', '3', 'Tianjun Xian', '青海省海西蒙古族藏族自治州天峻县', 'TJN');
INSERT INTO `t_area` VALUES ('3396', '358', '兴庆区', '31-358-3396', '3', 'Xingqing Qu', '宁夏回族自治区银川市兴庆区', '2');
INSERT INTO `t_area` VALUES ('3397', '358', '西夏区', '31-358-3397', '3', 'Xixia Qu', '宁夏回族自治区银川市西夏区', '2');
INSERT INTO `t_area` VALUES ('3398', '358', '金凤区', '31-358-3398', '3', 'Jinfeng Qu', '宁夏回族自治区银川市金凤区', '2');
INSERT INTO `t_area` VALUES ('3399', '358', '永宁县', '31-358-3399', '3', 'Yongning Xian', '宁夏回族自治区银川市永宁县', 'YGN');
INSERT INTO `t_area` VALUES ('3400', '358', '贺兰县', '31-358-3400', '3', 'Helan Xian', '宁夏回族自治区银川市贺兰县', 'HLN');
INSERT INTO `t_area` VALUES ('3401', '358', '灵武市', '31-358-3401', '3', 'Lingwu Shi', '宁夏回族自治区银川市灵武市', '2');
INSERT INTO `t_area` VALUES ('3403', '359', '大武口区', '31-359-3403', '3', 'Dawukou Qu', '宁夏回族自治区石嘴山市大武口区', 'DWK');
INSERT INTO `t_area` VALUES ('3404', '359', '惠农区', '31-359-3404', '3', 'Huinong Qu', '宁夏回族自治区石嘴山市惠农区', '2');
INSERT INTO `t_area` VALUES ('3405', '359', '平罗县', '31-359-3405', '3', 'Pingluo Xian', '宁夏回族自治区石嘴山市平罗县', 'PLO');
INSERT INTO `t_area` VALUES ('3407', '360', '利通区', '31-360-3407', '3', 'Litong Qu', '宁夏回族自治区吴忠市利通区', 'LTW');
INSERT INTO `t_area` VALUES ('3408', '360', '盐池县', '31-360-3408', '3', 'Yanchi Xian', '宁夏回族自治区吴忠市盐池县', 'YCY');
INSERT INTO `t_area` VALUES ('3409', '360', '同心县', '31-360-3409', '3', 'Tongxin Xian', '宁夏回族自治区吴忠市同心县', 'TGX');
INSERT INTO `t_area` VALUES ('3410', '360', '青铜峡市', '31-360-3410', '3', 'Qingtongxia Xian', '宁夏回族自治区吴忠市青铜峡市', 'QTX');
INSERT INTO `t_area` VALUES ('3412', '361', '原州区', '31-361-3412', '3', 'Yuanzhou Qu', '宁夏回族自治区固原市原州区', '2');
INSERT INTO `t_area` VALUES ('3413', '361', '西吉县', '31-361-3413', '3', 'Xiji Xian', '宁夏回族自治区固原市西吉县', '2');
INSERT INTO `t_area` VALUES ('3414', '361', '隆德县', '31-361-3414', '3', 'Longde Xian', '宁夏回族自治区固原市隆德县', '2');
INSERT INTO `t_area` VALUES ('3415', '361', '泾源县', '31-361-3415', '3', 'Jingyuan Xian', '宁夏回族自治区固原市泾源县', '2');
INSERT INTO `t_area` VALUES ('3416', '361', '彭阳县', '31-361-3416', '3', 'Pengyang Xian', '宁夏回族自治区固原市彭阳县', '2');
INSERT INTO `t_area` VALUES ('3418', '362', '沙坡头区', '31-362-3418', '3', 'Shapotou Qu', '宁夏回族自治区中卫市沙坡头区', '2');
INSERT INTO `t_area` VALUES ('3419', '362', '中宁县', '31-362-3419', '3', 'Zhongning Xian', '宁夏回族自治区中卫市中宁县', '2');
INSERT INTO `t_area` VALUES ('3420', '362', '海原县', '31-362-3420', '3', 'Haiyuan Xian', '宁夏回族自治区中卫市海原县', '2');
INSERT INTO `t_area` VALUES ('3422', '363', '天山区', '32-363-3422', '3', 'Tianshan Qu', '新疆维吾尔自治区乌鲁木齐市天山区', 'TSL');
INSERT INTO `t_area` VALUES ('3423', '363', '沙依巴克区', '32-363-3423', '3', 'Saybag Qu', '新疆维吾尔自治区乌鲁木齐市沙依巴克区', 'SAY');
INSERT INTO `t_area` VALUES ('3424', '363', '新市区', '32-363-3424', '3', 'Xinshi Qu', '新疆维吾尔自治区乌鲁木齐市新市区', 'XSU');
INSERT INTO `t_area` VALUES ('3425', '363', '水磨沟区', '32-363-3425', '3', 'Shuimogou Qu', '新疆维吾尔自治区乌鲁木齐市水磨沟区', 'SMG');
INSERT INTO `t_area` VALUES ('3426', '363', '头屯河区', '32-363-3426', '3', 'Toutunhe Qu', '新疆维吾尔自治区乌鲁木齐市头屯河区', 'TTH');
INSERT INTO `t_area` VALUES ('3427', '363', '达坂城区', '32-363-3427', '3', 'Dabancheng Qu', '新疆维吾尔自治区乌鲁木齐市达坂城区', '2');
INSERT INTO `t_area` VALUES ('3428', '363', '米东区', '32-363-3428', '3', 'Midong Qu', '新疆维吾尔自治区乌鲁木齐市米东区', '2');
INSERT INTO `t_area` VALUES ('3429', '363', '乌鲁木齐县', '32-363-3429', '3', 'Urumqi Xian', '新疆维吾尔自治区乌鲁木齐市乌鲁木齐县', 'URX');
INSERT INTO `t_area` VALUES ('3431', '364', '独山子区', '32-364-3431', '3', 'Dushanzi Qu', '新疆维吾尔自治区克拉玛依市独山子区', 'DSZ');
INSERT INTO `t_area` VALUES ('3432', '364', '克拉玛依区', '32-364-3432', '3', 'Karamay Qu', '新疆维吾尔自治区克拉玛依市克拉玛依区', 'KRQ');
INSERT INTO `t_area` VALUES ('3433', '364', '白碱滩区', '32-364-3433', '3', 'Baijiantan Qu', '新疆维吾尔自治区克拉玛依市白碱滩区', 'BJT');
INSERT INTO `t_area` VALUES ('3434', '364', '乌尔禾区', '32-364-3434', '3', 'Orku Qu', '新疆维吾尔自治区克拉玛依市乌尔禾区', 'ORK');
INSERT INTO `t_area` VALUES ('3435', '365', '吐鲁番市', '32-365-3435', '3', 'Turpan Shi', '新疆维吾尔自治区吐鲁番地区吐鲁番市', 'TUR');
INSERT INTO `t_area` VALUES ('3436', '365', '鄯善县', '32-365-3436', '3', 'Shanshan(piqan) Xian', '新疆维吾尔自治区吐鲁番地区鄯善县', 'SSX');
INSERT INTO `t_area` VALUES ('3437', '365', '托克逊县', '32-365-3437', '3', 'Toksun Xian', '新疆维吾尔自治区吐鲁番地区托克逊县', 'TOK');
INSERT INTO `t_area` VALUES ('3438', '366', '哈密市', '32-366-3438', '3', 'Hami(kumul) Shi', '新疆维吾尔自治区哈密地区哈密市', 'HAM');
INSERT INTO `t_area` VALUES ('3439', '366', '巴里坤哈萨克自治县', '32-366-3439', '3', 'Barkol Kazak Zizhixian', '新疆维吾尔自治区哈密地区巴里坤哈萨克自治县', 'BAR');
INSERT INTO `t_area` VALUES ('3440', '366', '伊吾县', '32-366-3440', '3', 'Yiwu(Araturuk) Xian', '新疆维吾尔自治区哈密地区伊吾县', 'YWX');
INSERT INTO `t_area` VALUES ('3441', '367', '昌吉市', '32-367-3441', '3', 'Changji Shi', '新疆维吾尔自治区昌吉回族自治州昌吉市', 'CJS');
INSERT INTO `t_area` VALUES ('3442', '367', '阜康市', '32-367-3442', '3', 'Fukang Shi', '新疆维吾尔自治区昌吉回族自治州阜康市', 'FKG');
INSERT INTO `t_area` VALUES ('3444', '367', '呼图壁县', '32-367-3444', '3', 'Hutubi Xian', '新疆维吾尔自治区昌吉回族自治州呼图壁县', 'HTB');
INSERT INTO `t_area` VALUES ('3445', '367', '玛纳斯县', '32-367-3445', '3', 'Manas Xian', '新疆维吾尔自治区昌吉回族自治州玛纳斯县', 'MAS');
INSERT INTO `t_area` VALUES ('3446', '367', '奇台县', '32-367-3446', '3', 'Qitai Xian', '新疆维吾尔自治区昌吉回族自治州奇台县', 'QTA');
INSERT INTO `t_area` VALUES ('3447', '367', '吉木萨尔县', '32-367-3447', '3', 'Jimsar Xian', '新疆维吾尔自治区昌吉回族自治州吉木萨尔县', 'JIM');
INSERT INTO `t_area` VALUES ('3448', '367', '木垒哈萨克自治县', '32-367-3448', '3', 'Mori Kazak Zizhixian', '新疆维吾尔自治区昌吉回族自治州木垒哈萨克自治县', 'MOR');
INSERT INTO `t_area` VALUES ('3449', '368', '博乐市', '32-368-3449', '3', 'Bole(Bortala) Shi', '新疆维吾尔自治区博尔塔拉蒙古自治州博乐市', 'BLE');
INSERT INTO `t_area` VALUES ('3450', '368', '精河县', '32-368-3450', '3', 'Jinghe(Jing) Xian', '新疆维吾尔自治区博尔塔拉蒙古自治州精河县', 'JGH');
INSERT INTO `t_area` VALUES ('3451', '368', '温泉县', '32-368-3451', '3', 'Wenquan(Arixang) Xian', '新疆维吾尔自治区博尔塔拉蒙古自治州温泉县', 'WNQ');
INSERT INTO `t_area` VALUES ('3452', '369', '库尔勒市', '32-369-3452', '3', 'Korla Shi', '新疆维吾尔自治区巴音郭楞蒙古自治州库尔勒市', 'KOR');
INSERT INTO `t_area` VALUES ('3453', '369', '轮台县', '32-369-3453', '3', 'Luntai(Bugur) Xian', '新疆维吾尔自治区巴音郭楞蒙古自治州轮台县', 'LTX');
INSERT INTO `t_area` VALUES ('3454', '369', '尉犁县', '32-369-3454', '3', 'Yuli(Lopnur) Xian', '新疆维吾尔自治区巴音郭楞蒙古自治州尉犁县', 'YLI');
INSERT INTO `t_area` VALUES ('3455', '369', '若羌县', '32-369-3455', '3', 'Ruoqiang(Qakilik) Xian', '新疆维吾尔自治区巴音郭楞蒙古自治州若羌县', 'RQG');
INSERT INTO `t_area` VALUES ('3456', '369', '且末县', '32-369-3456', '3', 'Qiemo(Qarqan) Xian', '新疆维吾尔自治区巴音郭楞蒙古自治州且末县', 'QMO');
INSERT INTO `t_area` VALUES ('3457', '369', '焉耆回族自治县', '32-369-3457', '3', 'Yanqi Huizu Zizhixian', '新疆维吾尔自治区巴音郭楞蒙古自治州焉耆回族自治县', 'YQI');
INSERT INTO `t_area` VALUES ('3458', '369', '和静县', '32-369-3458', '3', 'Hejing Xian', '新疆维吾尔自治区巴音郭楞蒙古自治州和静县', 'HJG');
INSERT INTO `t_area` VALUES ('3459', '369', '和硕县', '32-369-3459', '3', 'Hoxud Xian', '新疆维吾尔自治区巴音郭楞蒙古自治州和硕县', 'HOX');
INSERT INTO `t_area` VALUES ('3460', '369', '博湖县', '32-369-3460', '3', 'Bohu(Bagrax) Xian', '新疆维吾尔自治区巴音郭楞蒙古自治州博湖县', 'BHU');
INSERT INTO `t_area` VALUES ('3461', '370', '阿克苏市', '32-370-3461', '3', 'Aksu Shi', '新疆维吾尔自治区阿克苏地区阿克苏市', 'AKS');
INSERT INTO `t_area` VALUES ('3462', '370', '温宿县', '32-370-3462', '3', 'Wensu Xian', '新疆维吾尔自治区阿克苏地区温宿县', 'WSU');
INSERT INTO `t_area` VALUES ('3463', '370', '库车县', '32-370-3463', '3', 'Kuqa Xian', '新疆维吾尔自治区阿克苏地区库车县', 'KUQ');
INSERT INTO `t_area` VALUES ('3464', '370', '沙雅县', '32-370-3464', '3', 'Xayar Xian', '新疆维吾尔自治区阿克苏地区沙雅县', 'XYR');
INSERT INTO `t_area` VALUES ('3465', '370', '新和县', '32-370-3465', '3', 'Xinhe(Toksu) Xian', '新疆维吾尔自治区阿克苏地区新和县', 'XHT');
INSERT INTO `t_area` VALUES ('3466', '370', '拜城县', '32-370-3466', '3', 'Baicheng(Bay) Xian', '新疆维吾尔自治区阿克苏地区拜城县', 'BCG');
INSERT INTO `t_area` VALUES ('3467', '370', '乌什县', '32-370-3467', '3', 'Wushi(Uqturpan) Xian', '新疆维吾尔自治区阿克苏地区乌什县', 'WSH');
INSERT INTO `t_area` VALUES ('3468', '370', '阿瓦提县', '32-370-3468', '3', 'Awat Xian', '新疆维吾尔自治区阿克苏地区阿瓦提县', 'AWA');
INSERT INTO `t_area` VALUES ('3469', '370', '柯坪县', '32-370-3469', '3', 'Kalpin Xian', '新疆维吾尔自治区阿克苏地区柯坪县', 'KAL');
INSERT INTO `t_area` VALUES ('3470', '371', '阿图什市', '32-371-3470', '3', 'Artux Shi', '新疆维吾尔自治区克孜勒苏柯尔克孜自治州阿图什市', 'ART');
INSERT INTO `t_area` VALUES ('3471', '371', '阿克陶县', '32-371-3471', '3', 'Akto Xian', '新疆维吾尔自治区克孜勒苏柯尔克孜自治州阿克陶县', 'AKT');
INSERT INTO `t_area` VALUES ('3472', '371', '阿合奇县', '32-371-3472', '3', 'Akqi Xian', '新疆维吾尔自治区克孜勒苏柯尔克孜自治州阿合奇县', 'AKQ');
INSERT INTO `t_area` VALUES ('3473', '371', '乌恰县', '32-371-3473', '3', 'Wuqia(Ulugqat) Xian', '新疆维吾尔自治区克孜勒苏柯尔克孜自治州乌恰县', 'WQA');
INSERT INTO `t_area` VALUES ('3474', '372', '喀什市', '32-372-3474', '3', 'Kashi (Kaxgar) Shi', '新疆维吾尔自治区喀什地区喀什市', 'KHG');
INSERT INTO `t_area` VALUES ('3475', '372', '疏附县', '32-372-3475', '3', 'Shufu Xian', '新疆维吾尔自治区喀什地区疏附县', 'SFU');
INSERT INTO `t_area` VALUES ('3476', '372', '疏勒县', '32-372-3476', '3', 'Shule Xian', '新疆维吾尔自治区喀什地区疏勒县', 'SHL');
INSERT INTO `t_area` VALUES ('3477', '372', '英吉沙县', '32-372-3477', '3', 'Yengisar Xian', '新疆维吾尔自治区喀什地区英吉沙县', 'YEN');
INSERT INTO `t_area` VALUES ('3478', '372', '泽普县', '32-372-3478', '3', 'Zepu(Poskam) Xian', '新疆维吾尔自治区喀什地区泽普县', 'ZEP');
INSERT INTO `t_area` VALUES ('3479', '372', '莎车县', '32-372-3479', '3', 'Shache(Yarkant) Xian', '新疆维吾尔自治区喀什地区莎车县', 'SHC');
INSERT INTO `t_area` VALUES ('3480', '372', '叶城县', '32-372-3480', '3', 'Yecheng(Kargilik) Xian', '新疆维吾尔自治区喀什地区叶城县', 'YEC');
INSERT INTO `t_area` VALUES ('3481', '372', '麦盖提县', '32-372-3481', '3', 'Markit Xian', '新疆维吾尔自治区喀什地区麦盖提县', 'MAR');
INSERT INTO `t_area` VALUES ('3482', '372', '岳普湖县', '32-372-3482', '3', 'Yopurga Xian', '新疆维吾尔自治区喀什地区岳普湖县', 'YOP');
INSERT INTO `t_area` VALUES ('3483', '372', '伽师县', '32-372-3483', '3', 'Jiashi(Payzawat) Xian', '新疆维吾尔自治区喀什地区伽师县', 'JSI');
INSERT INTO `t_area` VALUES ('3484', '372', '巴楚县', '32-372-3484', '3', 'Bachu(Maralbexi) Xian', '新疆维吾尔自治区喀什地区巴楚县', 'BCX');
INSERT INTO `t_area` VALUES ('3485', '372', '塔什库尔干塔吉克自治县', '32-372-3485', '3', 'Taxkorgan Tajik Zizhixian', '新疆维吾尔自治区喀什地区塔什库尔干塔吉克自治县', 'TXK');
INSERT INTO `t_area` VALUES ('3486', '373', '和田市', '32-373-3486', '3', 'Hotan Shi', '新疆维吾尔自治区和田地区和田市', 'HTS');
INSERT INTO `t_area` VALUES ('3487', '373', '和田县', '32-373-3487', '3', 'Hotan Xian', '新疆维吾尔自治区和田地区和田县', 'HOT');
INSERT INTO `t_area` VALUES ('3488', '373', '墨玉县', '32-373-3488', '3', 'Moyu(Karakax) Xian', '新疆维吾尔自治区和田地区墨玉县', 'MOY');
INSERT INTO `t_area` VALUES ('3489', '373', '皮山县', '32-373-3489', '3', 'Pishan(Guma) Xian', '新疆维吾尔自治区和田地区皮山县', 'PSA');
INSERT INTO `t_area` VALUES ('3490', '373', '洛浦县', '32-373-3490', '3', 'Lop Xian', '新疆维吾尔自治区和田地区洛浦县', 'LOP');
INSERT INTO `t_area` VALUES ('3491', '373', '策勒县', '32-373-3491', '3', 'Qira Xian', '新疆维吾尔自治区和田地区策勒县', 'QIR');
INSERT INTO `t_area` VALUES ('3492', '373', '于田县', '32-373-3492', '3', 'Yutian(Keriya) Xian', '新疆维吾尔自治区和田地区于田县', 'YUT');
INSERT INTO `t_area` VALUES ('3493', '373', '民丰县', '32-373-3493', '3', 'Minfeng(Niya) Xian', '新疆维吾尔自治区和田地区民丰县', 'MFG');
INSERT INTO `t_area` VALUES ('3494', '374', '伊宁市', '32-374-3494', '3', 'Yining(Gulja) Shi', '新疆维吾尔自治区伊犁哈萨克自治州伊宁市', '2');
INSERT INTO `t_area` VALUES ('3495', '374', '奎屯市', '32-374-3495', '3', 'Kuytun Shi', '新疆维吾尔自治区伊犁哈萨克自治州奎屯市', '2');
INSERT INTO `t_area` VALUES ('3496', '374', '伊宁县', '32-374-3496', '3', 'Yining(Gulja) Xian', '新疆维吾尔自治区伊犁哈萨克自治州伊宁县', '2');
INSERT INTO `t_area` VALUES ('3497', '374', '察布查尔锡伯自治县', '32-374-3497', '3', 'Qapqal Xibe Zizhixian', '新疆维吾尔自治区伊犁哈萨克自治州察布查尔锡伯自治县', '2');
INSERT INTO `t_area` VALUES ('3498', '374', '霍城县', '32-374-3498', '3', 'Huocheng Xin', '新疆维吾尔自治区伊犁哈萨克自治州霍城县', '2');
INSERT INTO `t_area` VALUES ('3499', '374', '巩留县', '32-374-3499', '3', 'Gongliu(Tokkuztara) Xian', '新疆维吾尔自治区伊犁哈萨克自治州巩留县', '2');
INSERT INTO `t_area` VALUES ('3500', '374', '新源县', '32-374-3500', '3', 'Xinyuan(Kunes) Xian', '新疆维吾尔自治区伊犁哈萨克自治州新源县', '2');
INSERT INTO `t_area` VALUES ('3501', '374', '昭苏县', '32-374-3501', '3', 'Zhaosu(Mongolkure) Xian', '新疆维吾尔自治区伊犁哈萨克自治州昭苏县', '2');
INSERT INTO `t_area` VALUES ('3502', '374', '特克斯县', '32-374-3502', '3', 'Tekes Xian', '新疆维吾尔自治区伊犁哈萨克自治州特克斯县', '2');
INSERT INTO `t_area` VALUES ('3503', '374', '尼勒克县', '32-374-3503', '3', 'Nilka Xian', '新疆维吾尔自治区伊犁哈萨克自治州尼勒克县', '2');
INSERT INTO `t_area` VALUES ('3504', '375', '塔城市', '32-375-3504', '3', 'Tacheng(Qoqek) Shi', '新疆维吾尔自治区塔城地区塔城市', 'TCS');
INSERT INTO `t_area` VALUES ('3505', '375', '乌苏市', '32-375-3505', '3', 'Usu Shi', '新疆维吾尔自治区塔城地区乌苏市', 'USU');
INSERT INTO `t_area` VALUES ('3506', '375', '额敏县', '32-375-3506', '3', 'Emin(Dorbiljin) Xian', '新疆维吾尔自治区塔城地区额敏县', 'EMN');
INSERT INTO `t_area` VALUES ('3507', '375', '沙湾县', '32-375-3507', '3', 'Shawan Xian', '新疆维吾尔自治区塔城地区沙湾县', 'SWX');
INSERT INTO `t_area` VALUES ('3508', '375', '托里县', '32-375-3508', '3', 'Toli Xian', '新疆维吾尔自治区塔城地区托里县', 'TLI');
INSERT INTO `t_area` VALUES ('3509', '375', '裕民县', '32-375-3509', '3', 'Yumin(Qagantokay) Xian', '新疆维吾尔自治区塔城地区裕民县', 'YMN');
INSERT INTO `t_area` VALUES ('3510', '375', '和布克赛尔蒙古自治县', '32-375-3510', '3', 'Hebukesaiermengguzizhi Xian', '新疆维吾尔自治区塔城地区和布克赛尔蒙古自治县', '2');
INSERT INTO `t_area` VALUES ('3511', '376', '阿勒泰市', '32-376-3511', '3', 'Altay Shi', '新疆维吾尔自治区阿勒泰地区阿勒泰市', 'ALT');
INSERT INTO `t_area` VALUES ('3512', '376', '布尔津县', '32-376-3512', '3', 'Burqin Xian', '新疆维吾尔自治区阿勒泰地区布尔津县', 'BUX');
INSERT INTO `t_area` VALUES ('3513', '376', '富蕴县', '32-376-3513', '3', 'Fuyun(Koktokay) Xian', '新疆维吾尔自治区阿勒泰地区富蕴县', 'FYN');
INSERT INTO `t_area` VALUES ('3514', '376', '福海县', '32-376-3514', '3', 'Fuhai(Burultokay) Xian', '新疆维吾尔自治区阿勒泰地区福海县', 'FHI');
INSERT INTO `t_area` VALUES ('3515', '376', '哈巴河县', '32-376-3515', '3', 'Habahe(Kaba) Xian', '新疆维吾尔自治区阿勒泰地区哈巴河县', 'HBH');
INSERT INTO `t_area` VALUES ('3516', '376', '青河县', '32-376-3516', '3', 'Qinghe(Qinggil) Xian', '新疆维吾尔自治区阿勒泰地区青河县', 'QHX');
INSERT INTO `t_area` VALUES ('3517', '376', '吉木乃县', '32-376-3517', '3', 'Jeminay Xian', '新疆维吾尔自治区阿勒泰地区吉木乃县', 'JEM');
INSERT INTO `t_area` VALUES ('3518', '32', '石河子市', '32-3518', '2', 'Shihezi Shi', '新疆维吾尔自治区石河子市', 'SHZ');
INSERT INTO `t_area` VALUES ('3519', '32', '阿拉尔市', '32-3519', '2', 'Alaer Shi', '新疆维吾尔自治区阿拉尔市', '2');
INSERT INTO `t_area` VALUES ('3520', '32', '图木舒克市', '32-3520', '2', 'Tumushuke Shi', '新疆维吾尔自治区图木舒克市', '2');
INSERT INTO `t_area` VALUES ('3521', '32', '五家渠市', '32-3521', '2', 'Wujiaqu Shi', '新疆维吾尔自治区五家渠市', '2');
INSERT INTO `t_area` VALUES ('4000', '340', '麦积区', '29-340-4000', '3', 'Maiji Qu', '甘肃省天水市麦积区', '2');
INSERT INTO `t_area` VALUES ('4001', '5004', '江津区', '23-5004-4001', '3', 'Jiangjin Qu', '重庆市重庆市江津区', 'JJQ');
INSERT INTO `t_area` VALUES ('4002', '5004', '合川区', '23-5004-4002', '3', 'Hechuan Qu', '重庆市重庆市合川区', 'HCQ');
INSERT INTO `t_area` VALUES ('4003', '5004', '永川区', '23-5004-4003', '3', 'Yongchuan Qu', '重庆市重庆市永川区', 'YCQ');
INSERT INTO `t_area` VALUES ('4004', '5004', '南川区', '23-5004-4004', '3', 'Nanchuan Qu', '重庆市重庆市南川区', 'NCQ');
INSERT INTO `t_area` VALUES ('4006', '134', '芜湖县', '13-134-4006', '3', 'Wuhu Xian', '安徽省芜湖市芜湖县', 'WHX');
INSERT INTO `t_area` VALUES ('4100', '106', '加格达奇区', '9-106-4100', '3', 'Jiagedaqi Qu', '黑龙江省大兴安岭地区加格达奇区', '2');
INSERT INTO `t_area` VALUES ('4101', '106', '松岭区', '9-106-4101', '3', 'Songling Qu', '黑龙江省大兴安岭地区松岭区', '2');
INSERT INTO `t_area` VALUES ('4102', '106', '新林区', '9-106-4102', '3', 'Xinlin Qu', '黑龙江省大兴安岭地区新林区', '2');
INSERT INTO `t_area` VALUES ('4103', '106', '呼中区', '9-106-4103', '3', 'Huzhong Qu', '黑龙江省大兴安岭地区呼中区', '2');
INSERT INTO `t_area` VALUES ('4200', '125', '南湖区', '12-125-4200', '3', 'Nanhu Qu', '浙江省嘉兴市南湖区', '2');
INSERT INTO `t_area` VALUES ('4300', '162', '共青城市', '15-162-4300', '3', 'Gongqingcheng Shi', '江西省九江市共青城市', '2');
INSERT INTO `t_area` VALUES ('4400', '360', '红寺堡区', '31-360-4400', '3', 'Hongsibao Qu', '宁夏回族自治区吴忠市红寺堡区', '2');
INSERT INTO `t_area` VALUES ('4500', '344', '瓜州县', '29-344-4500', '3', 'Guazhou Xian', '甘肃省酒泉市瓜州县', '2');
INSERT INTO `t_area` VALUES ('4600', '215', '随县', '18-215-4600', '3', 'Sui Xian', '湖北省随州市随县', '2');
INSERT INTO `t_area` VALUES ('4700', '228', '零陵区', '19-228-4700', '3', 'Lingling Qu', '湖南省永州市零陵区', '2');
INSERT INTO `t_area` VALUES ('4800', '263', '平桂管理区', '21-263-4800', '3', 'Pingguiguanli Qu', '广西壮族自治区贺州市平桂管理区', '2');
INSERT INTO `t_area` VALUES ('4900', '279', '利州区', '24-279-4900', '3', 'Lizhou Qu', '四川省广元市利州区', '2');
INSERT INTO `t_area` VALUES ('5000', '286', '华蓥市', '24-286-5000', '3', 'Huaying Shi', '四川省广安市华蓥市', 'HYC');
INSERT INTO `t_area` VALUES ('5001', '2', '北京市', '2-5001', '2', 'Bei Jing Shi', '北京市北京市', 'BJS');
INSERT INTO `t_area` VALUES ('5002', '3', '天津市', '3-5002', '2', 'Tian Jin Shi', '天津市天津市', 'TJS');
INSERT INTO `t_area` VALUES ('5003', '10', '上海市', '10-5003', '2', 'Shang Hai Shi', '上海市上海市', 'SHS');
INSERT INTO `t_area` VALUES ('5004', '23', '重庆市', '23-5004', '2', 'Chong Qing Shi', '重庆市重庆市', 'CQS');

INSERT INTO `t_bank` VALUES ('102', '102', '中国工商银行');
INSERT INTO `t_bank` VALUES ('103', '103', '中国农业银行');
INSERT INTO `t_bank` VALUES ('104', '104', '中国银行');
INSERT INTO `t_bank` VALUES ('105', '105', '中国建设银行');
INSERT INTO `t_bank` VALUES ('201', '201', '国家开发银行');
INSERT INTO `t_bank` VALUES ('202', '202', '中国进出口银行');
INSERT INTO `t_bank` VALUES ('203', '203', '中国农业发展银行');
INSERT INTO `t_bank` VALUES ('301', '301', '交通银行');
INSERT INTO `t_bank` VALUES ('302', '302', '中信银行');
INSERT INTO `t_bank` VALUES ('303', '303', '中国光大银行');
INSERT INTO `t_bank` VALUES ('304', '304', '华夏银行');
INSERT INTO `t_bank` VALUES ('305', '305', '中国民生银行');
INSERT INTO `t_bank` VALUES ('306', '306', '广东发展银行');
INSERT INTO `t_bank` VALUES ('307', '307', '深圳发展银行');
INSERT INTO `t_bank` VALUES ('308', '308', '招商银行');
INSERT INTO `t_bank` VALUES ('309', '309', '兴业银行');
INSERT INTO `t_bank` VALUES ('310', '310', '上海浦东发展银行');
INSERT INTO `t_bank` VALUES ('313', '313', '城市商业银行');
INSERT INTO `t_bank` VALUES ('314', '314', '农村商业银行（江苏）');
INSERT INTO `t_bank` VALUES ('315', '315', '恒丰银行');
INSERT INTO `t_bank` VALUES ('316', '316', '浙商银行');
INSERT INTO `t_bank` VALUES ('317', '317', '农村合作银行');
INSERT INTO `t_bank` VALUES ('318', '318', '渤海银行股份有限公司');
INSERT INTO `t_bank` VALUES ('319', '319', '徽商银行股份有限公司');
INSERT INTO `t_bank` VALUES ('320', '320', '镇银行有限责任公司');
INSERT INTO `t_bank` VALUES ('401', '401', '城市信用社');
INSERT INTO `t_bank` VALUES ('402', '402', '农村信用社（含北京农村商业银行）');
INSERT INTO `t_bank` VALUES ('403', '403', '中国邮政储蓄银行');


INSERT INTO `t_identifyType` VALUES ('1', '身份证', '身份证备注');
INSERT INTO `t_identifyType` VALUES ('2', '军官证', '军官证备注');
INSERT INTO `t_identifyType` VALUES ('3', '户口本', '户口本备注');


INSERT INTO `t_permission` VALUES ('1', 'accountAdd', '添加帐号', '/account/add', '帐号权限');
INSERT INTO `t_permission` VALUES ('3', 'accountEdit', '修改帐号', '/account/update', '帐号权限');
INSERT INTO `t_permission` VALUES ('4', 'accountList', '查询帐号', '/account/list', '帐号权限');
INSERT INTO `t_permission` VALUES ('5', 'userAdd', '添加会员', '/user/add', '会员权限');
INSERT INTO `t_permission` VALUES ('6', 'userDelete', '删除会员', '/user/delete', '会员权限');
INSERT INTO `t_permission` VALUES ('7', 'userEdit', '修改会员', '/user/update', '会员权限');
INSERT INTO `t_permission` VALUES ('8', 'userList', '查询会员', '/user/list,/user/list-data,/user/identityList,/user/identityList-data', '会员权限');
INSERT INTO `t_permission` VALUES ('9', 'setDiscount', '设置会员折扣', '/discount/set', '会员权限');
INSERT INTO `t_permission` VALUES ('14', 'productCategorySysAdd', '添加商品系统分类', '/productCategory/manager/add', '商品分类权限');
INSERT INTO `t_permission` VALUES ('15', 'productCategorySysDelete', '删除商品系统分类', '/productCategory/manager/delete', '商品分类权限');
INSERT INTO `t_permission` VALUES ('16', 'productCategorySysEdit', '修改商品系统分类', '/productCategory/manager/add', '商品分类权限');
INSERT INTO `t_permission` VALUES ('17', 'productCategorySysList', '查询商品系统分类', '/productCategory/manager', '商品分类权限');
INSERT INTO `t_permission` VALUES ('18', 'productAdd', '添加库存', '/product/add', '库存权限');
INSERT INTO `t_permission` VALUES ('19', 'productDelete', '删除库存', '/product/delete', '库存权限');
INSERT INTO `t_permission` VALUES ('20', 'productEdit', '修改库存', '/product/update', '库存权限');
INSERT INTO `t_permission` VALUES ('21', 'productList', '查询库存', '/product/list,/productStock/preview,/productStock/uploadImage,/productUpload/saveImage,/product/shopList/list,/product/shopList/list-data', '库存权限');
INSERT INTO `t_permission` VALUES ('22', 'productAttributeAdd', '添加商品属性', '/productAttribute/add', '商品属性权限');
INSERT INTO `t_permission` VALUES ('23', 'productAttributeDelete', '删除商品属性', '/productAttribute/delete', '商品属性权限');
INSERT INTO `t_permission` VALUES ('24', 'productAttributeEdit', '修改商品属性', '/productAttribute/update', '商品属性权限');
INSERT INTO `t_permission` VALUES ('25', 'productAttributeList', '查询商品属性', '/productAttribute/list,/productAttribute/list/list-data', '商品属性权限');
INSERT INTO `t_permission` VALUES ('26', 'productAttributeValueAdd', '添加商品属性值', '/productAttributeValue/add', '商品属性值权限');
INSERT INTO `t_permission` VALUES ('27', 'productAttributeValueDelete', '删除商品属性值', '/productAttributeValue/delete', '商品属性值权限');
INSERT INTO `t_permission` VALUES ('28', 'productAttributeValueEdit', '修改商品属性值', '/productAttributeValue/update', '商品属性值权限');
INSERT INTO `t_permission` VALUES ('29', 'productAttributeValueList', '查询商品属性值', '/productAttributeValue/list,/productAttributeValue/list/list-data', '商品属性值权限');
INSERT INTO `t_permission` VALUES ('34', 'productImport', '商品导入', '/productUpload/list,/product/list/list-data,/productUpload/save1', '商品权限');
INSERT INTO `t_permission` VALUES ('35', 'report', '统计报表', '/report/list', '统计报表权限');
INSERT INTO `t_permission` VALUES ('36', 'roleAdd', '添加角色', '/role/add', '角色权限');
INSERT INTO `t_permission` VALUES ('37', 'roleDelete', '删除角色', '/role/delete', '角色权限');
INSERT INTO `t_permission` VALUES ('38', 'roleEdit', '修改角色', '/role/update', '角色权限');
INSERT INTO `t_permission` VALUES ('39', 'roleList', '查询角色', '/role/list', '角色权限');
INSERT INTO `t_permission` VALUES ('40', 'rolePermission', '角色授权', '/role/permission', '角色权限');
INSERT INTO `t_permission` VALUES ('41', 'orderList', '订单列表', '/order/list,/order/list-data', '订单权限');
INSERT INTO `t_permission` VALUES ('42', 'deviceList', '设备列表', '/device/list,/device/list-data', '设备权限');
INSERT INTO `t_permission` VALUES ('43', 'deviceAdd', '添加设备', '/device/add', '设备权限');
INSERT INTO `t_permission` VALUES ('44', 'deviceDelete', '删除设备', '/device/delete', '设备权限');
INSERT INTO `t_permission` VALUES ('45', 'deviceEdit', '修改设备', '/device/update', '设备权限');
INSERT INTO `t_permission` VALUES ('46', 'agentList', '代理商列表', '/agent/list,/agent/list/list-data,/agent/businessList,/agent/businessList/list-data', '代理商权限');
INSERT INTO `t_permission` VALUES ('47', 'agentAdd', '添加代理商', '/agent/add,/agent/business/add', '代理商权限');
INSERT INTO `t_permission` VALUES ('48', 'agentEdit', '修改代理商', '/agent/update', '代理商权限');
INSERT INTO `t_permission` VALUES ('49', 'shopList', '商家列表', '/shop/list,/shop/list-data', '商家权限');
INSERT INTO `t_permission` VALUES ('50', 'shopAdd', '商家新增', '/shop/add', '商家权限');
INSERT INTO `t_permission` VALUES ('51', 'shopDelete', '商家删除', '/shop/delete', '商家权限');
INSERT INTO `t_permission` VALUES ('52', 'shopEdit', '商家修改', '/shop/update', '商家权限');
INSERT INTO `t_permission` VALUES ('53', 'reportList', '列表', 'userReport/list,userReport/list/list-data', '举报权限');
INSERT INTO `t_permission` VALUES ('54', 'reportEdit', '删除', 'userReport/delete', '举报权限');
INSERT INTO `t_permission` VALUES ('55', 'apkAdd', 'APK新增', '/apk/add', 'APK新增权限');
INSERT INTO `t_permission` VALUES ('56', 'apkList', 'APK列表', '/apk/list', 'APK列表权限');
INSERT INTO `t_permission` VALUES ('57', 'settlementsRecordList', '账户流水记录列表', '/settlementsRecord/ist', '账户流水记录列表');
INSERT INTO `t_permission` VALUES ('58', 'withdrawList', '提现流水记录列表', '/withdraw/expenditure/list,/withdraw/withdrawData/list', '提现流水记录列表');
INSERT INTO `t_permission` VALUES ('59', 'businessbankList', '银行卡管理列表', '/businessbank/list', '银行卡管理列表');
INSERT INTO `t_permission` VALUES ('60', 'payaccountList', '支付密码管理列表', '/payaccount/list', '支付密码管理列表');
INSERT INTO `t_permission` VALUES ('61', 'publishList', '发布审核列表', '/publish/list', '发布审核列表');
INSERT INTO `t_permission` VALUES ('62', 'productStockList', '查询商品', '/productStock/list,/productStock/list-data,/productStock/shopList/list,/productStock/shopList/list-data', '商品权限');
INSERT INTO `t_permission` VALUES ('63', 'productStockAdd', '增加商品', '/produtStock/add', '商品权限');
INSERT INTO `t_permission` VALUES ('64', 'productStockDelete', '删除商品', '/productStock/delete', '商品权限');
INSERT INTO `t_permission` VALUES ('65', 'productStockEdit', '修改商品', '/productStock/update', '商品权限');
INSERT INTO `t_permission` VALUES ('66', 'activityList', '查询活动', '/activity/list', '活动权限');
INSERT INTO `t_permission` VALUES ('67', 'activityAdd', '添加活动', '/activity/add', '活动权限');
INSERT INTO `t_permission` VALUES ('68', 'activityEdit', '修改活动', '/activity/update', '活动权限');
INSERT INTO `t_permission` VALUES ('69', 'activityDelete', '删除活动', '/activity/delete', '活动权限');
INSERT INTO `t_permission` VALUES ('70', 'activityDetailAdd', '添加活动详情', '/ActivityDetail/Add', '活动权限');
INSERT INTO `t_permission` VALUES ('71', 'activityDetailList', '查看活动详情', '/ActivityDetail/list', '活动权限');
INSERT INTO `t_permission` VALUES ('72', 'productCategoryAdd', '添加商品自定义分类', '/productCategory/manager/add', '商品分类权限');
INSERT INTO `t_permission` VALUES ('73', 'productCategoryDelete', '删除商品自定义分类', '/productCategory/manager/delete', '商品分类权限');
INSERT INTO `t_permission` VALUES ('74', 'productCategoryEdit', '修改商品自定义分类', '/productCategory/manager/add', '商品分类权限');
INSERT INTO `t_permission` VALUES ('75', 'productCategoryList', '查询商品自定义分类', '/productCategory/manager', '商品分类权限');
INSERT INTO `t_permission` VALUES ('76', 'activityResultList', '活动结果审核列表', '/activityResultAudit/list', '活动审核权限');
INSERT INTO `t_permission` VALUES ('77', 'activityResultAudit', '活动结果审核', '/activityResultAudit/audit', '活动审核权限');

INSERT INTO `t_publish_category` VALUES ('1', '帮厨');
INSERT INTO `t_publish_category` VALUES ('2', '代驾');
INSERT INTO `t_publish_category` VALUES ('3', '保姆');


INSERT INTO `t_report_type` VALUES ('1', '暴力信息');
INSERT INTO `t_report_type` VALUES ('2', '色情信息');
INSERT INTO `t_report_type` VALUES ('3', '欺诈信息');
INSERT INTO `t_report_type` VALUES ('4', '假货次品');
INSERT INTO `t_report_type` VALUES ('5', '其他');


INSERT INTO `t_role` VALUES ('1', '管理员', '200', null, '2015-06-05 15:49:25');
INSERT INTO `t_role` VALUES ('2', '代理商', '100', null, '2015-06-05 15:49:30');
INSERT INTO `t_role` VALUES ('3', '商家', '90', null, '2015-06-05 15:49:32');

INSERT INTO `t_role_permission` VALUES ('1', '1');
INSERT INTO `t_role_permission` VALUES ('1', '3');
INSERT INTO `t_role_permission` VALUES ('1', '4');
INSERT INTO `t_role_permission` VALUES ('1', '5');
INSERT INTO `t_role_permission` VALUES ('1', '6');
INSERT INTO `t_role_permission` VALUES ('1', '7');
INSERT INTO `t_role_permission` VALUES ('1', '8');
INSERT INTO `t_role_permission` VALUES ('1', '9');
INSERT INTO `t_role_permission` VALUES ('1', '10');
INSERT INTO `t_role_permission` VALUES ('1', '11');
INSERT INTO `t_role_permission` VALUES ('1', '12');
INSERT INTO `t_role_permission` VALUES ('1', '13');
INSERT INTO `t_role_permission` VALUES ('1', '14');
INSERT INTO `t_role_permission` VALUES ('1', '15');
INSERT INTO `t_role_permission` VALUES ('1', '16');
INSERT INTO `t_role_permission` VALUES ('1', '17');
INSERT INTO `t_role_permission` VALUES ('1', '18');
INSERT INTO `t_role_permission` VALUES ('1', '19');
INSERT INTO `t_role_permission` VALUES ('1', '20');
INSERT INTO `t_role_permission` VALUES ('1', '21');
INSERT INTO `t_role_permission` VALUES ('1', '22');
INSERT INTO `t_role_permission` VALUES ('1', '23');
INSERT INTO `t_role_permission` VALUES ('1', '24');
INSERT INTO `t_role_permission` VALUES ('1', '25');
INSERT INTO `t_role_permission` VALUES ('1', '26');
INSERT INTO `t_role_permission` VALUES ('1', '27');
INSERT INTO `t_role_permission` VALUES ('1', '28');
INSERT INTO `t_role_permission` VALUES ('1', '29');
INSERT INTO `t_role_permission` VALUES ('1', '34');
INSERT INTO `t_role_permission` VALUES ('1', '35');
INSERT INTO `t_role_permission` VALUES ('1', '36');
INSERT INTO `t_role_permission` VALUES ('1', '37');
INSERT INTO `t_role_permission` VALUES ('1', '38');
INSERT INTO `t_role_permission` VALUES ('1', '39');
INSERT INTO `t_role_permission` VALUES ('1', '40');
INSERT INTO `t_role_permission` VALUES ('1', '41');
INSERT INTO `t_role_permission` VALUES ('1', '42');
INSERT INTO `t_role_permission` VALUES ('1', '43');
INSERT INTO `t_role_permission` VALUES ('1', '44');
INSERT INTO `t_role_permission` VALUES ('1', '45');
INSERT INTO `t_role_permission` VALUES ('1', '46');
INSERT INTO `t_role_permission` VALUES ('1', '47');
INSERT INTO `t_role_permission` VALUES ('1', '48');
INSERT INTO `t_role_permission` VALUES ('1', '49');
INSERT INTO `t_role_permission` VALUES ('1', '50');
INSERT INTO `t_role_permission` VALUES ('1', '51');
INSERT INTO `t_role_permission` VALUES ('1', '52');
INSERT INTO `t_role_permission` VALUES ('1', '53');
INSERT INTO `t_role_permission` VALUES ('1', '54');
INSERT INTO `t_role_permission` VALUES ('1', '55');
INSERT INTO `t_role_permission` VALUES ('1', '56');
INSERT INTO `t_role_permission` VALUES ('1', '57');
INSERT INTO `t_role_permission` VALUES ('1', '58');
INSERT INTO `t_role_permission` VALUES ('1', '59');
INSERT INTO `t_role_permission` VALUES ('1', '60');
INSERT INTO `t_role_permission` VALUES ('1', '61');
INSERT INTO `t_role_permission` VALUES ('1', '62');
INSERT INTO `t_role_permission` VALUES ('1', '63');
INSERT INTO `t_role_permission` VALUES ('1', '64');
INSERT INTO `t_role_permission` VALUES ('1', '65');
INSERT INTO `t_role_permission` VALUES ('1', '66');
INSERT INTO `t_role_permission` VALUES ('1', '67');
INSERT INTO `t_role_permission` VALUES ('1', '68');
INSERT INTO `t_role_permission` VALUES ('1', '69');
INSERT INTO `t_role_permission` VALUES ('1', '70');
INSERT INTO `t_role_permission` VALUES ('1', '76');
INSERT INTO `t_role_permission` VALUES ('1', '77');
INSERT INTO `t_role_permission` VALUES ('2', '3');
INSERT INTO `t_role_permission` VALUES ('2', '4');
INSERT INTO `t_role_permission` VALUES ('2', '5');
INSERT INTO `t_role_permission` VALUES ('2', '6');
INSERT INTO `t_role_permission` VALUES ('2', '7');
INSERT INTO `t_role_permission` VALUES ('2', '8');
INSERT INTO `t_role_permission` VALUES ('2', '9');
INSERT INTO `t_role_permission` VALUES ('2', '10');
INSERT INTO `t_role_permission` VALUES ('2', '11');
INSERT INTO `t_role_permission` VALUES ('2', '12');
INSERT INTO `t_role_permission` VALUES ('2', '13');
INSERT INTO `t_role_permission` VALUES ('2', '14');
INSERT INTO `t_role_permission` VALUES ('2', '15');
INSERT INTO `t_role_permission` VALUES ('2', '16');
INSERT INTO `t_role_permission` VALUES ('2', '17');
INSERT INTO `t_role_permission` VALUES ('2', '18');
INSERT INTO `t_role_permission` VALUES ('2', '19');
INSERT INTO `t_role_permission` VALUES ('2', '20');
INSERT INTO `t_role_permission` VALUES ('2', '21');
INSERT INTO `t_role_permission` VALUES ('2', '22');
INSERT INTO `t_role_permission` VALUES ('2', '23');
INSERT INTO `t_role_permission` VALUES ('2', '24');
INSERT INTO `t_role_permission` VALUES ('2', '25');
INSERT INTO `t_role_permission` VALUES ('2', '29');
INSERT INTO `t_role_permission` VALUES ('2', '34');
INSERT INTO `t_role_permission` VALUES ('2', '35');
INSERT INTO `t_role_permission` VALUES ('2', '36');
INSERT INTO `t_role_permission` VALUES ('2', '37');
INSERT INTO `t_role_permission` VALUES ('2', '38');
INSERT INTO `t_role_permission` VALUES ('2', '39');
INSERT INTO `t_role_permission` VALUES ('2', '40');
INSERT INTO `t_role_permission` VALUES ('2', '41');
INSERT INTO `t_role_permission` VALUES ('2', '42');
INSERT INTO `t_role_permission` VALUES ('2', '43');
INSERT INTO `t_role_permission` VALUES ('2', '44');
INSERT INTO `t_role_permission` VALUES ('2', '45');
INSERT INTO `t_role_permission` VALUES ('2', '46');
INSERT INTO `t_role_permission` VALUES ('2', '47');
INSERT INTO `t_role_permission` VALUES ('2', '48');
INSERT INTO `t_role_permission` VALUES ('2', '49');
INSERT INTO `t_role_permission` VALUES ('2', '50');
INSERT INTO `t_role_permission` VALUES ('2', '51');
INSERT INTO `t_role_permission` VALUES ('2', '52');
INSERT INTO `t_role_permission` VALUES ('2', '53');
INSERT INTO `t_role_permission` VALUES ('2', '54');
INSERT INTO `t_role_permission` VALUES ('2', '55');
INSERT INTO `t_role_permission` VALUES ('2', '56');
INSERT INTO `t_role_permission` VALUES ('2', '57');
INSERT INTO `t_role_permission` VALUES ('2', '58');
INSERT INTO `t_role_permission` VALUES ('2', '59');
INSERT INTO `t_role_permission` VALUES ('2', '60');
INSERT INTO `t_role_permission` VALUES ('2', '61');
INSERT INTO `t_role_permission` VALUES ('3', '1');
INSERT INTO `t_role_permission` VALUES ('3', '3');
INSERT INTO `t_role_permission` VALUES ('3', '4');
INSERT INTO `t_role_permission` VALUES ('3', '5');
INSERT INTO `t_role_permission` VALUES ('3', '6');
INSERT INTO `t_role_permission` VALUES ('3', '7');
INSERT INTO `t_role_permission` VALUES ('3', '8');
INSERT INTO `t_role_permission` VALUES ('3', '9');
INSERT INTO `t_role_permission` VALUES ('3', '10');
INSERT INTO `t_role_permission` VALUES ('3', '11');
INSERT INTO `t_role_permission` VALUES ('3', '12');
INSERT INTO `t_role_permission` VALUES ('3', '13');
INSERT INTO `t_role_permission` VALUES ('3', '17');
INSERT INTO `t_role_permission` VALUES ('3', '18');
INSERT INTO `t_role_permission` VALUES ('3', '19');
INSERT INTO `t_role_permission` VALUES ('3', '20');
INSERT INTO `t_role_permission` VALUES ('3', '21');
INSERT INTO `t_role_permission` VALUES ('3', '22');
INSERT INTO `t_role_permission` VALUES ('3', '23');
INSERT INTO `t_role_permission` VALUES ('3', '24');
INSERT INTO `t_role_permission` VALUES ('3', '25');
INSERT INTO `t_role_permission` VALUES ('3', '26');
INSERT INTO `t_role_permission` VALUES ('3', '27');
INSERT INTO `t_role_permission` VALUES ('3', '28');
INSERT INTO `t_role_permission` VALUES ('3', '29');
INSERT INTO `t_role_permission` VALUES ('3', '34');
INSERT INTO `t_role_permission` VALUES ('3', '35');
INSERT INTO `t_role_permission` VALUES ('3', '36');
INSERT INTO `t_role_permission` VALUES ('3', '37');
INSERT INTO `t_role_permission` VALUES ('3', '38');
INSERT INTO `t_role_permission` VALUES ('3', '39');
INSERT INTO `t_role_permission` VALUES ('3', '40');
INSERT INTO `t_role_permission` VALUES ('3', '41');
INSERT INTO `t_role_permission` VALUES ('3', '42');
INSERT INTO `t_role_permission` VALUES ('3', '43');
INSERT INTO `t_role_permission` VALUES ('3', '44');
INSERT INTO `t_role_permission` VALUES ('3', '45');
INSERT INTO `t_role_permission` VALUES ('3', '46');
INSERT INTO `t_role_permission` VALUES ('3', '47');
INSERT INTO `t_role_permission` VALUES ('3', '48');
INSERT INTO `t_role_permission` VALUES ('3', '49');
INSERT INTO `t_role_permission` VALUES ('3', '50');
INSERT INTO `t_role_permission` VALUES ('3', '51');
INSERT INTO `t_role_permission` VALUES ('3', '52');
INSERT INTO `t_role_permission` VALUES ('3', '53');
INSERT INTO `t_role_permission` VALUES ('3', '54');
INSERT INTO `t_role_permission` VALUES ('3', '55');
INSERT INTO `t_role_permission` VALUES ('3', '56');
INSERT INTO `t_role_permission` VALUES ('3', '57');
INSERT INTO `t_role_permission` VALUES ('3', '58');
INSERT INTO `t_role_permission` VALUES ('3', '59');
INSERT INTO `t_role_permission` VALUES ('3', '60');
INSERT INTO `t_role_permission` VALUES ('3', '61');
INSERT INTO `t_role_permission` VALUES ('3', '62');
INSERT INTO `t_role_permission` VALUES ('3', '63');
INSERT INTO `t_role_permission` VALUES ('3', '64');
INSERT INTO `t_role_permission` VALUES ('3', '65');
INSERT INTO `t_role_permission` VALUES ('3', '66');
INSERT INTO `t_role_permission` VALUES ('3', '67');
INSERT INTO `t_role_permission` VALUES ('3', '68');
INSERT INTO `t_role_permission` VALUES ('3', '69');
INSERT INTO `t_role_permission` VALUES ('3', '70');
INSERT INTO `t_role_permission` VALUES ('3', '71');
INSERT INTO `t_role_permission` VALUES ('3', '72');
INSERT INTO `t_role_permission` VALUES ('3', '73');
INSERT INTO `t_role_permission` VALUES ('3', '74');
INSERT INTO `t_role_permission` VALUES ('3', '75');

INSERT INTO `t_shop_type` VALUES ('1', '居家服务', null, '1');
INSERT INTO `t_shop_type` VALUES ('2', '干洗', null, '2');
INSERT INTO `t_shop_type` VALUES ('3', '宠物', null, '3');
INSERT INTO `t_shop_type` VALUES ('4', '便利购物', null, '4');
INSERT INTO `t_shop_type` VALUES ('5', '生鲜配送', null, '5');
INSERT INTO `t_shop_type` VALUES ('6', '外卖快餐', null, '6');

INSERT INTO `t_sys_config` VALUES ('sql_version', '0');

