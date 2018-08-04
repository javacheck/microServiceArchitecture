package cn.lastmiles.service.sms;

public interface SMSTemplate {
	String mobile = "020-28871503";
	/**
	 * 注册验证码
	 */
	String REGISTERCODE = "欢迎成为莱麦科技会员，您的验证码${code}，请在注册页面填写";
	
	/**
	 * 短信登陆
	 */
	String LOGINCODE = "您的验证码${code}，5分钟内有效，请在登录页面填写";
	
	/**
	 * 修改密码
	 */
	String REQUESTPASSWORDCODE = "您的验证码${code}，5分钟内有效，请在登录页面填写";
	
	
	/**
	 * 修改绑定手机号码
	 */
	String UPDATEMOBILE = "您正在修改绑定手机，验证码${code}，如非本人操作，请忽略";
	
	/**
	 * 支付密码手机验证
	 */
	String PAYMOBILEVERIFICATION = "您的支付验证码为${code}，如非本人操作，请忽略";
	
	/**
	 * 修改支付密码
	 */
	String UPDATEPAYPASSWORD = "您正在修改或者设置支付密码，验证码${code}，如非本人操作，请及时联系客服热线"+mobile;
	
	/**
	 * 找回密码
	 */
	String FINDPASSWORD = "您正在找回登录密码，验证码${code}，如非本人操作，请忽略";
	
	/**
	 * 证件审核不通过
	 */
	String IdAUDITFAIL = "您的证件审核不通过，请检查您的证件是否正确！";
	
	/**
	 * (API-SHOP)修改登录密码
	 */
	String UPDATELOGINPWGETVERIFYCODE = "您的手机验证码为${code}，5分钟内有效，请在修改登录密码页面填写";
	/**
	 * (API-SHOP)修改绑定手机
	 */
	String UPDATEOLDMOBILEGETVERIFYCODE = "您的手机验证码为${code}，5分钟内有效，请在修改绑定页面填写";
	/**
	 * (API-SHOP)修改绑定手机
	 */
	String UPDATENEWMOBILEGETVERIFYCODE = "您的手机验证码为${code}，5分钟内有效，请在修改绑定页面填写";
	/**
	 * (API-SHOP)找回密码
	 */
	String SHOPFINDPASSWORD = "您正在找回登录密码，验证码${code}，如非本人操作，请忽略";
	
	/**
	 * (POS)会员卡手机短信验证
	 */
	String USERCARDMOBILEGETVERIFYCODE = "验证码：${code}。欢迎您使用${storeName}的会员卡进行支付，支付订单号为${orderId}。";
	/**
	 * (POS)会员卡修改手机获取短信验证
	 */
	String USERCARDUPDATEMOBILCODE = "您的手机验证码为${code}，5分钟内有效，请在修改会员卡手机号页面填写。";
	
	/**
	 * 找回会员卡密码                                          
	 */
	String FINDUSERCARDPASSWORD = "您正在找回会员卡密码，验证码:${code}，如非本人操作，请忽略";
	
	
	String USERBIRTHDAY = "尊敬的${storeName}会员您好，预祝您生日快乐！我们特地送上一张50/150/300元生日消费券，快和亲朋好友一起来${storeName}庆祝生日吧！（消费券生日当天有效）回T退订";

	String BOOKINGREMINDE = "尊敬的${userName}先生/女士您好，您预定的${storeName}VIP专场已经准备好了，我们在玩吧等你哟！回T退订";
	
	String BALANCEREMINDE = "尊敬的${storeName}会员，您的会员卡余额已不足50元，便于下次能潇洒的喊“刷我的卡”，请及时充值。回T退订";

}
