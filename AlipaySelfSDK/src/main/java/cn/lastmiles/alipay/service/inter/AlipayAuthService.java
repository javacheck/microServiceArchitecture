/**
 * createDate : 2016年9月1日上午10:08:28
 * 第三方应用授权
 */
package cn.lastmiles.alipay.service.inter;

import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenQueryRequestBuilder;
import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenRequestBuilder;
import cn.lastmiles.alipay.model.result.auth.AlipayOpenAuthTokenAppResult;
import cn.lastmiles.alipay.model.result.auth.AlipayOpenAuthTokenQueryResult;

public interface AlipayAuthService {
	/**
	 * *********************************************************************
	 @ 换取应用授权令牌
	 * 接口地址：alipay.open.auth.token.app
	 * *********************************************************************
	 * 在应用授权的场景下，商户把名下应用授权给ISV后，支付宝会给ISV颁发应用授权码app_auth_code，
	 * ISV可通过获取到的app_auth_code换取app_auth_token。
	 * app_auth_code作为换取app_auth_token的票据，每次用户授权带上的app_auth_code将不一样，
	 * app_auth_code只能使用一次，一天（从当前时间算起的24小时）未被使用自动过期。
	 * 刷新应用授权令牌，ISV可通过获取到的refresh_token刷新app_auth_token，
	 * 刷新后老的refresh_token会在一段时间后失效（失效时间为接口返回的re_expires_in）。
	 */
	public AlipayOpenAuthTokenAppResult getOpenAuthToken(AlipayAuthTokenRequestBuilder builder);
	
	/**
	 * *********************************************************************
	 @ 查询某个应用授权AppAuthToken的授权信息
	 * 接口地址：alipay.open.auth.token.app.query
	 * *********************************************************************
	 * 当商户把服务窗、店铺等接口的权限授权给ISV之后，支付宝会给ISV颁发一个app_auth_token。
	 * 如若授权成功之后，ISV想知道用户的授权信息，如授权者、授权接口列表等信息，
	 * 可以调用本接口查询某个app_auth_token对应的授权信息
	 */
	public AlipayOpenAuthTokenQueryResult queryOpenAuthToken(AlipayAuthTokenQueryRequestBuilder builder);
}