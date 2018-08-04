package cn.lastmiles.pay.alipay;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088021174383713";
	
	public static String partner_email = "hzd@lastmiles.cn";
	// 商户的私钥
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALyMpXvdG1LpKKcdnntRFv2aDV+dL8iggs8b5+0GErEPQ4zV3FL9lDiqxg2FSMpajN60eu3g7vjyBGwjs3qH/IZof7RK9iztVv737XFoxVqGddwc/VwL1Rqfzfa9BdFzEo+mm+FNvBavpWLeGKfgOYN6FM11OUCgvZk0p4ob2M//AgMBAAECgYAUe/M+kbb2Ov5/qWgeXaDxzFrHmHwof8e2WIHJ7m75bX8ZPeLB4WrI/USnE2PlGBFV4KW6UkWkJib02KwXE88LW3F5vJd0XNWEpO5cIoe18qwdV3eNT7UsDAFzEhdf55cMZu2MHbUUeHDH1Woh43HY+JSPy5JPnyq7EFZ19pKEgQJBAN3+aBRU8CrfddWaNtcG1ubcNku903LaQYPEgtGf7jljHojdOWUu7IMINC4btO5HIAVKnYnOuPx/zJaiA7y59DUCQQDZbrPVXVHkDaF+1lrAx2nwELr5V4YJEgMQC8mCUkGbzvBTdqLmyFcipTn4Zx0HGQ+qYS9W6v2FYRlOk/fO/tHjAkAFDA2tuKilMONq8dChXuRv5z3MxwoRqGio6ETZIzu1Tk2Zav4tEL4L1JnrGnIooLFXrXSbd3m5M+ag3hobplapAkEAvyZKZMYgqxusoUQzDo2BPbKHnZLrW+gA10d1hONpmF9p+gYvGjKAIA4fk5ia9VV75y5/HquB65M8W09MKSbuPwJAENlpkuSeRs48gBuUBfCawaiG/kofYh2zD6GZ8Og83dO1OZmbY2+GqlABzofvzxl7gPTGmuMqdgDjq0m3Q9mWDQ==";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";
	// 退款签名方式 不需修改
	public  static String refund_sign_type = "MD5";
    //支付宝提供给商户的服务接入网关URL(新)
	public  static String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	// 退款时MD5 加密时使用
	public static String key = "y79q4nl8d2onzsr3g38fh5cszn8e8v0q";
	//退款回调地址
	public static String refund_notify_url="http://商户网关地址/refund_fastpay_by_platform_pwd-JAVA-UTF-8/notify_url.jsp";

}
