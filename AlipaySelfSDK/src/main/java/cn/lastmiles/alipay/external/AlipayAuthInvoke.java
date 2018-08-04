/**
 * createDate : 2016年7月25日下午2:57:40
 * 第三方应用授权
 */
package cn.lastmiles.alipay.external;

import java.util.HashMap;
import java.util.Map;
import cn.lastmiles.alipay.bean.Auth;
import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenQueryRequestBuilder;
import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenRequestBuilder;
import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenQueryRequestBuilder.BizContent;
import cn.lastmiles.alipay.model.result.auth.AlipayOpenAuthTokenAppResult;
import cn.lastmiles.alipay.model.result.auth.AlipayOpenAuthTokenQueryResult;
import cn.lastmiles.alipay.model.status.auth.GrantType;
import cn.lastmiles.alipay.service.abs.AbsAlipayAuthService;
import cn.lastmiles.alipay.service.impl.AlipayAuthServiceImpl;

public class AlipayAuthInvoke {

	private static AbsAlipayAuthService authService = new AlipayAuthServiceImpl.ClientBuilder().build();
	
	public Map<String,Object> getAuthToken(Auth auth){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		if( null == auth.getGrantType()){
			returnMap.put("errorParameters","grantType should not be NULL!");
			return returnMap;
		}
		
		AlipayAuthTokenRequestBuilder builder = new AlipayAuthTokenRequestBuilder();
		String grantType = auth.getGrantType().name();
		 
		builder.setGrantType(grantType);
		builder.setCode(auth.getCode());
		builder.setRefreshToken(auth.getRefreshToken());
		
		AlipayOpenAuthTokenAppResult result = authService.getOpenAuthToken(builder);
		if( null == result.getParametersError()){
			returnMap.put("requestParameters", builder);
			returnMap.put("responseParameters",result);			
		} else {
			returnMap.put("errorParameters",result.getParametersError());
		}
	    return returnMap;
	}
	
	public Map<String,Object> queryAuthToken(String appAuthToken){
		
		 AlipayAuthTokenQueryRequestBuilder builder = new AlipayAuthTokenQueryRequestBuilder();
		 
		 BizContent bizContent = new BizContent();
		 bizContent.setAppAuthToken(appAuthToken);
		 builder.setBizContent(bizContent);		 
		
		 Map<String,Object> returnMap = new HashMap<String,Object>();
		 
		 AlipayOpenAuthTokenQueryResult result = authService.queryOpenAuthToken(builder);
	     
		 if( null == result.getParametersError()){
			 returnMap.put("requestParameters", builder);
			 returnMap.put("responseParameters",result);			
		 } else {
			 returnMap.put("errorParameters",result.getParametersError());
		 }
	     return returnMap;
	}
	
	/**
	 * 单元测试
	 */
	public static void main(String[] args) {
		AlipayAuthInvoke acf = new AlipayAuthInvoke();
		// 授权测试
		Auth auth = new Auth();
		auth.setGrantType(GrantType.REFRESH_TOKEN);
		auth.setRefreshToken("201609BB5b9738208b2d4dbc8bbe44e3d29bfX71");
		Map<String,Object> get = acf.getAuthToken(auth);
		System.out.println("授权测试: 错误提示：" + get.get("errorParameters") + "---> 请求参数：" + get.get("requestParameters") + "---> 响应参数：" + get.get("responseParameters"));
		
		// 查权测试
		Map<String,Object> query = acf.queryAuthToken("201609BB46f0fe1b545d4495b6786424d4c85F71");
		System.out.println("查权测试: 错误提示：" + query.get("errorParameters") + "---> 请求参数：" + query.get("requestParameters") + "---> 响应参数：" + query.get("responseParameters"));
	}
}