package cn.lastmiles.bean;
/**
 * createDate : 2015年8月12日 下午5:42:32 
 */

public interface CacheKeys {
	/**
	 * 支付密码过期标识
	 */
	String PAYPASSWORDONEDAYLOCKKEY = "onedaylock:paypassword:key:";
	/**
	 * 支付密码找回标识
	 */
	String PAYPASSWORDFORGETRECOVEREDKEY = "forget:recovered:paypassword:key:";
	/**
	 * 支付密码找回标识
	 */
	String ALIPAYAUTHKEY = "alipay:auth:key:";
}
