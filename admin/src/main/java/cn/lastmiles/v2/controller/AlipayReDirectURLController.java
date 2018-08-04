/**
 * createDate : 2016年9月6日下午3:47:25
 */
package cn.lastmiles.v2.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alipay.api.response.AlipayOpenAuthTokenAppQueryResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;

import cn.lastmiles.alipay.bean.Auth;
import cn.lastmiles.alipay.external.AlipayAuthInvoke;
import cn.lastmiles.alipay.external.AlipayShopInvoke;
import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenRequestBuilder;
import cn.lastmiles.alipay.model.result.auth.AlipayOpenAuthTokenAppResult;
import cn.lastmiles.alipay.model.result.auth.AlipayOpenAuthTokenQueryResult;
import cn.lastmiles.alipay.model.status.auth.GrantType;
import cn.lastmiles.bean.CacheKeys;
import cn.lastmiles.cache.CacheService;
import cn.lastmiles.common.service.IdService;
import cn.lastmiles.common.utils.StringUtils;
import cn.lastmiles.service.ShopService;
import cn.lastmiles.v2.service.AlipayOperationService;

@RequestMapping("alipayDirectUrl")
@Controller
public class AlipayReDirectURLController {
	private static final Logger logger = LoggerFactory.getLogger(AlipayReDirectURLController.class);
	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	@Autowired
	private AlipayOperationService alipayOperationService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private CacheService cacheService; // 缓存
	@Autowired
	private IdService idService;
	
	@RequestMapping(value="redirectSuccessURL")
	public String redirectSuccessURL(){
		logger.debug("授权重定向成功页面...");
		return "v2/alipayDirectUrl/success";
	}
	
	@RequestMapping(value="redirectErrorURL")
	public String redirectErrorURL(){
		logger.debug("授权重定向失败页面...");
		return "v2/alipayDirectUrl/error";
	}
	
	@RequestMapping(value="auth")
	public String alipayAuth(String app_id,String app_auth_code,String storeId,RedirectAttributes model){
		logger.debug("接收到的app_id是:{},app_auth_code是:{},storeId是:{}",app_id,app_auth_code,storeId);
		
		if( StringUtils.isBlank(storeId) ){
			model.addAttribute("errorMessge", "商家ID参数错误!");
			return "redirect:/alipayDirectUrl/redirectErrorURL";
		}
		
		AlipayAuthInvoke aai = new AlipayAuthInvoke();
		Auth auth = new Auth();
		Object object = cacheService.get(CacheKeys.ALIPAYAUTHKEY + storeId);
		if( null != object ){ // 之前授过权限则刷新授权码
			auth.setGrantType(GrantType.REFRESH_TOKEN);
			auth.setRefreshToken(object+"");
		} else {
			List<Map<String, Object>> list = alipayOperationService.findAuthByStoreId(storeId);
			if(list.isEmpty() || list.size() <= 0 ){
				auth.setGrantType(GrantType.AUTHORIZATION_CODE);
				auth.setCode(app_auth_code);							
			} else {
				auth.setGrantType(GrantType.REFRESH_TOKEN);
				
				String refreshToken = list.get(0).get("app_refresh_token")+"";
				logger.debug("从数据库中取出的刷新令牌是：{}",refreshToken);
				
				auth.setRefreshToken(refreshToken);
			}
		}
		
		Map<String,Object> getAuth = aai.getAuthToken(auth);
		if( null == getAuth.get("errorParameters")){
			AlipayOpenAuthTokenAppResult getResult = (AlipayOpenAuthTokenAppResult) getAuth.get("responseParameters");
			AlipayAuthTokenRequestBuilder getBuilder = (AlipayAuthTokenRequestBuilder) getAuth.get("requestParameters");
			AlipayOpenAuthTokenAppResponse getResponseBody = getResult.getResponse();
			System.out.println("1");
			if(getResult.isOperationSuccess()){ // 授权成功
				String appAuthToken = getResponseBody.getAppAuthToken();
				cacheService.set(CacheKeys.ALIPAYAUTHKEY + storeId, appAuthToken);// 把生成的授权Token放进缓存
				
				// 异步保存入数据库中
				threadPoolTaskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						Long getAuthID = idService.getId();
						alipayOperationService.getAuthRequestBody(storeId,getAuthID,getBuilder);
						alipayOperationService.getAuthResponseBody(storeId,getAuthID,getResponseBody);
						// 保存授权的token
						shopService.updateAlipayAuthToken(storeId,appAuthToken);
					}
				});
				Map<String,Object> queryAuth = aai.queryAuthToken(appAuthToken);
				if( null == queryAuth.get("errorParameters")){
					AlipayOpenAuthTokenQueryResult queryResult = (AlipayOpenAuthTokenQueryResult) queryAuth.get("responseParameters");
					AlipayOpenAuthTokenAppQueryResponse queryResponseBody = queryResult.getResponse();
					if(queryResult.isOperationSuccess()){
						 model.addAttribute("queryAuthList",queryResponseBody.toString());
						 return "redirect:/alipayDirectUrl/redirectSuccessURL";
					} else {
						System.out.println("2");
						model.addAttribute("queryAuthError",getResponseBody.getSubMsg());
						return "redirect:/alipayDirectUrl/redirectErrorURL";
					}
				} else {
					System.out.println('3');
					model.addAttribute("errorMessge", queryAuth.get("errorParameters"));
					return "redirect:/alipayDirectUrl/redirectErrorURL";
				}
			} else {
				// 异步保存入数据库中
				threadPoolTaskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						Long getAuthID = idService.getId();
						alipayOperationService.getAuthRequestBody(storeId,getAuthID,getBuilder);
						alipayOperationService.getAuthResponseBody(storeId,getAuthID,getResponseBody);
					}
				});
				System.out.println(4);
				model.addAttribute("getAuthError",getResponseBody.getSubMsg());
				return "redirect:/alipayDirectUrl/redirectErrorURL";
			}
		} else {
			System.out.println(5);
			model.addAttribute("errorMessge", getAuth.get("errorParameters"));
			return "redirect:/alipayDirectUrl/redirectErrorURL";
		}
	}
	
	@RequestMapping(value="asyncNotice")
	public String asyncNotice(HttpServletRequest request,Model model){
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		     String paramName = (String) iter.next();
		     String paramValue = ((String[]) requestParams.get(paramName))[0];
		     System.out.println("收到参数："+paramName + " = "+ paramValue );
		     /*若你在notify_url后添加了自定义参数如http://www.alipay.com?a=a,请不要加入params*/
		     params.put(paramName, paramValue);
		}
		
		logger.debug("异步过来的信息是：{}",params.toString());
		
		// 异步保存入数据库中
		threadPoolTaskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				Long asyncID = idService.getId();
				alipayOperationService.saveAsyncNotice(asyncID,params);
			}
		});
		
		AlipayShopInvoke asi = new AlipayShopInvoke();
		boolean verifyResult = asi.rsaCheckV2(params);
		System.out.println("验证结果：" + verifyResult);
		if(verifyResult){ // 是支付宝发送过来的
			model.addAttribute("asyncResult",params);			
			return "v2/alipayAsyncNotice/add";
		}
		model.addAttribute("errorMsg","请注意：非支付宝发送过来的异步通知...");
		return "v2/alipayAsyncNotice/error";
	}
}