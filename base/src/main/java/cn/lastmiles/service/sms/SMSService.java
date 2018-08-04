package cn.lastmiles.service.sms;

public interface SMSService {
	/**
	 * 发送短信
	 * 
	 * @param template
	 *            短信模版 查看SMSTemplate类
	 * @param parameter
	 *            短信模版中${key}根据key值替换成value,比如 欢迎成为莱麦科技会员，您的验证码${code}，请在注册页面填写
	 *            ，那么map中有一个key为code的值，会把${code}换成value
	 *            ,SMSParameter.getInstance("key1","value1","key2","value2");
	 * @param mobiles
	 *            手机号码
	 */
	public void send(String template, SMSParameter parameter, String... mobiles);

	/**
	 * 通道2
	 * @param template
	 * @param parameter
	 * @param mobiles
	 */
	public void send2(String template, SMSParameter parameter, String... mobiles);

}
