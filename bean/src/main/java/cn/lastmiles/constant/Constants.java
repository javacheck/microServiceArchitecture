package cn.lastmiles.constant;

public interface Constants {
	String WEB_REGISTER_COOKIE_KEY = "web_register_cookie_key";
	
	String REQUEST_SUCCESS_KEY = "request_success_key";

	String MENU_SESSION_KEY = "menu_session_key";

	String RESOURCES_SESSION_KEY = "resources_session_key";

	String LOGINNAME_SESSION_KEY = "loginName_session_key";

	String ACCOUNT_SESSION_KEY = "account_session_key";
	
	String STORE_SESSION_KEY = "store_session_key";

	String PERMISSION_SESSION_KEY = "permission_session_key";

	String ROLE_SESSION_KEY = "role_session_key";

	public final static int INVALID = 0;

	public final static int NORMAL = 1;

	public final static int DELETED = -1;

	public final static int SELECTALL = -1;// 全选
	
	public interface SysConfig {
		String RECOMMENDED_NAME = "USER_RECOMMENDED";//推荐用户key
		String RECOMMENDED = "RECOMMENDED";//推荐用户
		String NOTRECOMMENDED = "NOTRECOMMENDED";//不推荐用户
	}
	
	public interface OrderReturnGoods{
		/**
		 * 储值
		 */
		int RETURNTYPE_STOREDVALUE = 0;
		/**
		 * 现金
		 */
		int RETURNTYPE_CASH = 1 ;
	}
	
	public interface RoomBooking{
		/**
		 * 预定中
		 */
		int STATUS_BOOKING = 0;//
		/**
		 * 已开台
		 */
		int STATUS_FOUNDING = 1;//
		/**
		 * 已取消
		 */
		int STATUS_CANCEL = 3;//
	}
	public interface Room{
		/**
		 * 空闲
		 */
		int STATUS_IDLE = 1;//
		/**
		 * 使用中
		 */
		int STATUS_USERED = 2;//
		/**
		 * 预定中
		 */
		int STATUS_BOOKED = 3;//
		/**
		 * 关闭
		 */
		int STATUS_CLOSE = 0;//
	}

	public interface OrderItem {
		Integer STATUS_NO_PAY = 2;//未支付
		Integer STATUSNORMAL = 1;//正常
		Integer STATUSCANCEL = 0;//取消
		int  TYPE_ROOM = 0;//房间
		int  TYPE_PRODUCT = 1;//商品
		int  TYPE_PACKAGE = 2;//套餐
	}

	public interface CashGift {
		Integer STATUSNORMAL = 0;// 未使用
		Integer STATUSUSED = 1;// 已使用

		// 类型 0表示折扣卷，1表示现金卷
		Integer TYPEDISCOUNT = 0;
		Integer TYPECASH = 1;
	}

	public interface PayChannel {
		// 0 支付宝 1 微信支付 2 刷卡 3 现金，4银联在线,7货到付款

		// 所有支付方式
		String[] ALLPAYTYPEIDS = new String[] { "1", "7" };
		String[] ALLPAYTYPEDESC = new String[] { "微信支付", "货到付款" };

		// 在线支付方式
		String[] ONLINEPAYTYPEIDS = new String[] { "1" };
		String[] ONLINEPAYTYPEDESC = new String[] { "微信支付" };

		// 货到付款方式
		String[] OFFLINEPAYTYPEIDS = new String[] { "7" };
		String[] OFFLINEPAYTYPEDESC = new String[] { "货到付款" };
	}

	public interface PayRecord {
		// 1表示银联，2微信
		int CHANNEL_UNIONPAY = 1;
		int CHANNEL_WEIXIN = 2;
		int CHANNEL_ALIPAY = 3;

		// 1成功，2失败
		int STATUS_SUCCESS = 1;
		int STATUS_FAIL = 2;
	}

	public interface User {
		int IDAUDIT_ING = 0; // 身份审核中
		int IDAUDIT_SUCCESS = 1;// 身份审核通过
		int IDAUDIT_FAIL = 2;// 身份审核失败
	}

	public interface ActivityDetail {
		Integer STATUS_NORMAL = 1;// 正常
		Integer STATUS_INVALID = 0; // 无效
	}

	public interface ActivityResult {
		// 状态 0 表示审核中 , 1表示审核通过，2表示审核不通过
		Integer STATUS_PENDING = 0; // 审核中
		Integer STATUS_THROUGH = 1; // 审核通过
		Integer STATUS_FAILURE = 2; // 审核不通过
	}

	public interface Activity {
		Integer STATUS_NORMAL = 1;// 正常
		Integer STATUS_INVALID = 0; // 无效
	}

	// 商家代理商银行卡信息
	public interface BusinessBank {
		int isDefault = 1; // 默认支付账户
		int notDefault = 0; // 非默认支付账户
		int STORE_TYPE = 0;// 商家
		int AGENT_TYPE = 1;// 代理商
	}

	// 默认的角色Id
	public interface Role {
		Long ROLE_ADMIN_ID = 1L;// 管理员角色ID
		Long ROLE_AGENT_ID = 2L;// 代理商角色ID
		Long ROLE_STORE_ID = 3L;// 商家角色ID
		Long ROLE_MOVIE_STORE_ID = 4l;// 影视类型商家
	}

	// 帐号类型
	public interface Account {
		public final static int ACCOUNT_TYPE_ADMIN = 1;// 管理员
		public final static int ACCOUNT_TYPE_AGENT = 2;// 代理商
		public final static int ACCOUNT_TYPE_STORE = 3;// 商家
		public final static Long SUPER_ACCOUNT_ID = 1L; // 超级管理员ID
	}

	// 库存
	public interface ProductStock {
		public final static Double Store_Infinite = -99d;// 库存最大值
		/**上架商品*/
		public final static int STORE_SALEING = 0;// 
		public final static int STORE_OFF_SHELVES = 1;// 下架商品

		public final static String ATTRIBUTECODE_SEGMENTATION = "-";// 属性分隔符
		/**
		 * 有属性
		 */
		public final static int TYPE_ON = 0;
		/**
		 * 无属性
		 */
		public final static int TYPE_OFF = 1; 
		/**
		 * POS端上架0
		 */
		public final static int POS_UP = 0;
		/**
		 * POS端下架1
		 */
		public final static int POS_DOWN = 1;
		/**
		 * APP端上架2
		 */
		public final static int APP_UP = 2;
		/**
		 * APP端下架3
		 */
		public final static int APP_DOWN = 3;
		/**
		 * 4全部上架
		 */
		public final static int ALL_UP = 4;
		/**
		 * 5全部下架
		 */
		public final static int ALL_DOWN = 5;
	}

	// 用户消费记录
	public interface UserBalanceRecord {
		public final static int TYPE_SPEND = 1;// 消费
		public final static int TYPE_RECHARGE = 2;// 充值
		public final static int TYPE_REGISTER = 3;// 注册
		public final static int TYPE_ORDER_CANCEL = 4;// 订单取消返款
		public final static int TYPE_RECOMMENDED = 5;// 推荐人 获赠
	}

	// 商品分类
	public interface ProductCategory {
		public final static int PRODUCT_CATEGORY_SYSTEM = 0; // 系统分类
		public final static int PRODUCT_CATEGORY_CUSTOM = 1; // 自定义分类
		public final static int PRODUCT_CATEGORY_FATHER_ADD = -1; // 添加顶级分类

	}

	public interface Store {
		public final static int STORE_OPERATING = 1; // 营业中
		public final static int STORE_GRADUATION = 0; // 结业
		public final static int STORE_MAINSHOP = 1; // 总店
		public final static int STORE_SHOP = 0 ; // 商家
		public final static int STORE_ISSHARE = 1; // 共享
		public final static int STORE_NOTSHARE = 0; // 不共享
		int UNIFIEDPOINTRULE = 0;//统一使用总部积分规则
		int UNUNIFIEDPOINTRULE = 1;//不使用总部积分规则
	}

	public interface SettlementsRecord {
		int STATUS_NOT_SETTLED = 0;// 未从冻结余额到余额
		int STATUS_SETTLED = 1;// 已从冻结余额划分到余额
	}

	// 支付帐号
	public interface PayAccount {
		int PAY_ACCOUNT_TYPE_STORE = 0;// 商家
		int PAY_ACCOUNT_TYPE_AGENT = 1;// 代理商
		int PAY_ACCOUNT_TYPE_USER = 2;// 用户
		int PAY_ACCOUNT_TYPE_PLATFORMP = -1;// 平台
		long PAY_ACCOUNT_OWNERID_PLATFORMPRICE = -1;// 平台
	}

	// 订单
	public interface OrderRefund {
		int STATUS_ING = 0; // 退款中
		int STATUS_SUCESS = 1; // 成功
		int STATUS_FAILURE = 2; // 失败
	}

	// 订单
	public interface Order {
		int ORDER_SOURCE_APP = 0;// 从APP下的订单
		int ORDER_SOURCE_DEVICES = 1;// 从终端下的订单

		// 订单状态 0待付款 1待确认(已支付,线下没有支付) 2商家取消 3已完成,4待签收(商家已发货),5待发货
		// (商家确认),7退换货，8待评价(已签收)，9缺货,10用户取消 ，11超时未支付，系统取消,12退款中，13已退款，99交易关闭
		int TYPE_NO_PAY = 0; // 未支付
		int TYPE_PAY = 1; // 已支付                            //接单时间
		int TYPE_CANCEL = 2; // 订单取消
		int TYPE_FINISHED = 3; // 订单完成
		int TYPE_WAITING_RECEIVING = 4;// 待签收(商家已发货)  //签收时间
		int TYPE_WAITING_DELIVER = 5;// 5待发货 (商家确认)    //发货时间
		int TYPE_STORE_CONFIRM = 6;//
		int TYPE_REJECTED = 7;// 7退换货
		int TYPE_WAITING_EVALUATE = 8;// 8待评价(已签收)      //评价时间
		int TYPE_WAITING_STOCKOUT = 9;// 9缺货
		int TYPE_USER_CANCEL = 10;// 10用户取消                //取消时间
		int TYPE_SYS_CANCEL = 11;// 系统超时取消              
		int TYPE_REFUND_ING = 12;// 退款中                     
		int TYPE_REFUND_SUCCESS = 13;// 已退款                 //退款成功时间
		int TYPE_REFUND_FAIL = 14;// 退款失败                  //退款失败时间
		int TYPE_BANK_CARD_REVOKE = 15;//刷卡撤销
		int TYPE_PAYING = 100;//支付中（线下）
		int TYPE_CLOSED = 99;

		int PAYMENT_ALIPAY = 0;// 支付宝
		int PAYMENT_MICRO_CHANNEL_PAY = 1;// 微信支付
		int PAYMENT_CASH_PAY = 3;// 现金支付
		int PAYMENT_CARD_PAY = 2;// 刷卡支付
		int PAYMENT_BAIDU_PAY = 6;// 百度支付
		int PAYMENT_BALANCE_PAY = 5;// 余额支付
		int PAYMENT_UNION_PAY = 4; // 银联
		int PAYMENT_LINE_PAY = 7; // 货到付款
		int PAYMENT_APP_BALANCE_PAY = 8;// APP余额支付
		int PAYMENT_USER_CARD_BALANCE_PAY = 9;// 会员卡余额支付

		int PAYMENT_TOTAL_PAY = -10;// 所有支付方法统计

		int SETTLEMENT_STATUS_NOT = 0;// 未结算
		int SETTLEMENT_STATUS_DONT = 1;// 已结算

		Integer PRINTSTATUS = 1;
		Integer NOTPRINTSTATUS = 0;

		int PRINTED_NOT = 0; // 未打印
		int PRINTED_OK = 1; // 已打印

		int DELIVERY_SHOP = 0;// 店铺自己送
		int DELIVERY_USER = 1;// 用户自己取
		int DELIVERY_EXPRESS = 2; // 快递
		int DELIVERY_ON_THIRD = 3; // 第三方配送

	}

	public interface Status {
		public final static int SELECT_ALL = -10;// 全选
		public final static int NO_STORE = -1; // 无店铺
		public final static int NO_USER = -1; // 无会员
	}

	public interface pay {
		public final static int SELECT_ALL = -10;// 全选
		public final static int NO_STORE = -1; // 无店铺
		public final static int NO_USER = -1; // 无会员
	}

	public interface PublishType {// 发布类型
		public final static int TYPE_BUY = 0;// 买买买
		public final static int TYPE_SALE = 1;// 卖卖卖
	}

	public interface Publish {
		// 0 待审，1 已审 ，2 取消,3审核不通过
		int TYPE_AUDITING = 0;
		int TYPE_AUDITED = 1;
		int TYPE_CANCEL = 2;
		int TYPE_AUDITED_FAIL = 3;

		int TYPE_BUY = 0;
		int TYPE_SALE = 1;
	}

	public interface PublishImage {
		// 1 描述图片 2 证书图片
		int IMAGE_TYPE_DES = 1;
		int IMAGE_TYPE_CER = 2;
	}

	public interface isPaid {// 是否支付
		public final static int NOT_PAID = 0;// 未支付

		public final static int PAID = 1;// 已经支付
	}

	public interface paymentMethod {// 支付方式
		public final static int LINE_PAYMENT = 1;// 支付方式线下支付
	}

	public interface BookingStatus {// 发布类型
		public final static int BOOKING_CONFIRMING = 0;// 确认中

		public final static int BOOKING_CONFIRM = 1;// 已确认

		public final static int BOOKING_CANCEL = 2;// 取消

		public final static int BOOKING_COMPLETION = 3;// 服务完成

		public final static int BOOKING_SUCCESS = 4;// 订单完成
	}

	public interface WithdrawStatus { // 提现 状态
		public final static int WITHDRAW_PROGRESS = 0; // 处理中
		public final static int WITHDRAW_SUCCESS = 1; // 成功
		public final static int WITHDRAW_FAILURE = 2; // 失败
		public final static int WITHDRAW_AUDIT_SUCCESS = 3;// 审核成功
		public final static int WITHDRAW_AUDIT_FAILURE = 4; // 审核失败
	}

	public interface Withdraw {
		int WITHDRAW_TYPE_STORE = 0;// 商家
		int WITHDRAW_TYPE_AGENT = 1;// 代理商
		int WITHDRAW_TYPE_USER = 2;// 用户
	}

	public interface PayAccountStatus { // 支付状态
		public final static int PAYACCOUNT_INACTIVE = 0;// 未激活
		public final static int PAYACCOUNT_NORMAL = 1; // 正常
		public final static int PAYACCOUNT_FREEZE = 2; // 冻结
		public final static int PAYACCOUNT_CLOSE = 3; // 销户
		public final static int PAYACCOUNT_REPORT = 4; // 挂失
		public final static int PAYACCOUNT_LOCK = 5; // 锁定
	}

	public interface Type { // 全部
		public final static int ALl = 10;
	}

	public interface Product { // 全部

		public final static int PRODUCT_HAVE_ATTRIBUTE = 0;

		public final static int PRODUCT_NO_ATTRIBUTE = 1;
	}

	public interface LoginRecordType { // 登录记录
		public final static int LOGIN_STORE = 0; // 0表示商家
		public final static int LOGIN_USER = 1; // 1表示用户
	}

	public interface MessageType { // 信息类型
		// 1发布通知，2审核通过，3审核不通过，4撤销，5预约，6确认成功，7拒绝预约，8发布方中途取消，9预约方中途取消 ，10 用户注册送6元
		public final static int MESSAGE_PUBLISH_NOTICE = 1;// 发布通知
		public final static int MESSAGE_AUDIT_YES = 2;// 审核通过
		public final static int MESSAGE_AUDIT_NO = 3;// 审核不通过
		public final static int MESSAGE_AUDIT_REVOCATION = 4;// 撤销
		public final static int MESSAGE_ORDER = 5;// 预约
		public final static int MESSAGE_CONFIRMATION_SUCCESS = 6;// 确认成功
		public final static int MESSAGE_REFUSE_ORDER = 7;// 拒绝预约
		public final static int MESSAGE_PUBLISH_MIDWAY_CANCEL = 8;// 发布方中途取消
		public final static int MESSAGE_ORDER_MIDWAY_CANCEL = 9;// 预约方中途取消
		public final static int MESSAGE_ADD_USER = 10;// 用户注册送6元
		public final static int MESSAGE_RECOMMENDED = 11;// 推荐人的得到钱通知
		public final static int MESSAGE_RECOMMENDED_MAX = 12;// 推荐人上线
		public final static int MESSAGE_ORDER_ADD = 13;// 下单成功 发送商户
		public final static int MESSAGE_ORDER_USER_CANCEL = 14;// 用户取消订单
		String MESSAGE_PUBLISH_NOTICE_CONTENT = "亲，您的发布信息，“麦麦”正在加紧审核中，请耐心等待哟！";
		String MESSAGE_AUDIT_YES_CONTENT = "亲，您的发布信息已通过审核咯，马上开始您的奇妙之旅吧！";
		String MESSAGE_AUDIT_NO_CONTENT = "亲，您发布的信息未能通过审核，是不是含有非法信息或内容描述不清晰呢？“麦麦”需要一个绿色的成长环境，请您修改后再重新发布吧！";
		String MESSAGE_AUDIT_REVOCATION_CONTENT = "呜呜呜，您所发布的信息#title#因被举报存在#reason#原因，已被删除，“麦麦”感觉很抱歉呀！";
		String MESSAGE_ORDER_CONTENT = "铛铛铛铛，您发布服务信息#title#已有人预约咯，请及时处理吧。如2个小时内未确认，系统将自动解除预约哟。";
		String MESSAGE_CONFIRMATION_SUCCESS_CONTENT = "哇哦，您好厉害呀！您已成功预约了#title#服务。“麦麦”提醒您一定要记得遵照约定完成服务哟！";
		String MESSAGE_REFUSE_ORDER_CONTENT = "哦哦，“麦麦”好抱歉呀，您预约的#title#被拒绝了。要不再找一个呗？下一个也许会更好噢!";
		String MESSAGE_PUBLISH_MIDWAY_CANCEL_CONTENT = "唉，您预约的#title#被发布者取消了，请放心，麦麦会对他/她进行信誉记录的！";
		String MESSAGE_ORDER_MIDWAY_CANCEL_CONTENT = "呐，您发布的#title#被预约者取消了，请放心，麦麦会对他/她进行信誉记录的！";
		String MESSAGE_ADD_USER_CONTENT = "恭喜您获得6元现金券，系统已经自动打入你的账户余额。请在“我的”界面查看余额即可。消费时可直接抵扣消费金额。";
		String MESSAGE_RECOMMENDED_CONTENT = "哇，您推荐号码#title#已注册成功咯，查看余额有惊喜哟。";
		String MESSAGE_RECOMMENDED_MAX_CONTENT = "哇～你完成了“推荐新用户拿奖励”活动，获得了1500元奖励哟。这等好事，叫上朋友一起来吧！";
		String MESSAGE_ORDER_ADD_CONTENT = "您有新订单#title#等待发货，请及时安排发货，使会员有良好的购物体验。";
		String MESSAGE_ORDER_USER_CANCEL_CONTENT = "您的订单#title#，会员已取消，请及时处理。";
	}

	public interface Apk { // APK版本
		public final static int lm_apk = 0; // 0表示随身社区APP
		public final static int Shop_apk = 1; // 1表示商户APP
		static int 	POS_APK = 2;
		static int 	App_center_APK = 3;
	}

	public interface SalesPromotionCategory {
		int STATUS_NOT_RUNNING = 0;// 未开始
		int STATUS_RUNNING = 1;// 进行中
		int STATUS_RUNED = 2;// 已结束
		public final static int unlimited = -1; // 无限制
	}
	
	/** 促销管理第二期 */
	public interface Promotion{
		/** 全部 */
		int TYPE_OR_STATUS_ALL = -10; 
		/** 首单 */
		int TYPE_FIRST_ORDER = 1; 
		/** 满减 */
		int TYPE_FULL_SUBTRACT = 2;
		/** 折扣-3- */
		int TYPE_DISCOUNT = 3; 
		/** 组合 */
		int TYPE_COMBINATION = 4; 
		/** 优惠卷 */
		int TYPE_PREFERENTIAL_VOLUME = 20; 
		/**免运费*/
		int TYPE_NO_FREIGHT = 10; 
		/** 关闭 */
		int STATUS_OFF = 0; 
		/**  打开  */
		int STATUS_ON = 1; 
	}
	/** 促销管理第二期 */
	public interface PromotionCoupon{
		//自动
				int ISSUETYPE_AUTO  =0;
				//手动
				int ISSUETYPE_UP  =1;
				//线下
				int ISSUETYPE_DOWN  =2;
				
		//正常
		int STATUS_NORMAL  =1;
		//无效
		int STATUS_INVALID  =0;
		//结束
		int STATUS_END  =2;
		//0所有用户，1新注册用户，2已经注册用户
		int RANGE_NEWUSER=1;
		int RANGE_ALLUSER=0;
		int RANGE_REGISTER=2;
		//类型 0表示折扣卷，1表示现金卷
		/**折扣卷*/
		int TYPEDISCOUNT = 0;
		/**现金卷*/
		int TYPECASH = 1;
	}
	public interface UserCard{
		/**
		 * 启用
		 */
		int STATUS_ON = 1;
		/**
		 * 禁用
		 */
		int STATUS_OFF = 2;
		/**
		 * 挂失
		 */
		int STATUS_LOSS = 3;
	}
	public interface UserCardRecord{
		/**
		 * 1充值
		 */
		int TYPE_RECHARGE = 1;
		/**
		 * 2消费
		 */
		int TYPE_CONSUMER = 2;
		/**
		 * 3积分兑换
		 */
		int TYPE_EXCHANGE = 3;
		/**
		 * 4积分抵扣
		 */
		int TYPE_DEDUCTION = 4;
		
		int TYPE_WEIXIN	 = 5;//微信公众号过来的积分\
		
		int TYPE_SERVICE_PACKAGE = 6;//购买服务套餐
		
		/**
		 * 7退换货
		 */
		int TYPE_RETURNGOODS = 7; 
		
		/**
		 * 1按照会员等级折扣
		 */
		int DISCOUNTTYPE_USER = 1;
		/**
		 * 2固定折扣
		 */
		int DISCOUNTTYPE_FIXATION = 2;
		/**
		 * 3无折扣
		 */
		int DISCOUNTTYPE_NO = 3; 
	}
	
	public interface RoomDateSetting{
		/**
		 * 1普通日期
		 */
		int TYPE_GENERAL = 1;
		/**
		 * 2周末
		 */
		int TYPE_WEEKEND = 2;
		/**
		 * 3特殊时间
		 */
		int TYPE_SPECIAL = 3;
		/**
		 * 4节假日
		 */
		int TYPE_HOLIDAYS = 4;
		/**
		 * 5节假日时间段
		 */
		int TYPE_FESTIVALS = 5;
	}
	
	public interface UserLevelDefinition {
		Integer AUTOMODE = 0;//自动升级
		Integer UNAUTOMODE = 1;//手动升级
	}
	
	public interface InformMiddleDevice{
		/**
		 * 已读
		 */
		int ISREAD = 0;
		/**
		 * 未读
		 */
		int NOTREAD = 1;
	}
}
