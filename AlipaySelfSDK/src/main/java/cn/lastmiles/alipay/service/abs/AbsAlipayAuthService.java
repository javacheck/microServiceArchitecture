/**
 * createDate : 2016年9月2日上午11:13:40
 * 第三方应用授权
 */
package cn.lastmiles.alipay.service.abs;

import cn.lastmiles.alipay.config.Constants;
import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenQueryRequestBuilder;
import cn.lastmiles.alipay.model.builder.auth.AlipayAuthTokenRequestBuilder;
import cn.lastmiles.alipay.model.result.auth.AlipayOpenAuthTokenAppResult;
import cn.lastmiles.alipay.model.result.auth.AlipayOpenAuthTokenQueryResult;
import cn.lastmiles.alipay.model.status.auth.AuthStatus;
import cn.lastmiles.alipay.service.inter.AlipayAuthService;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayOpenAuthTokenAppQueryRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.response.AlipayOpenAuthTokenAppQueryResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;

public class AbsAlipayAuthService extends AbsAlipayService implements AlipayAuthService{
	protected AlipayClient client;

	@Override
	public AlipayOpenAuthTokenAppResult getOpenAuthToken(AlipayAuthTokenRequestBuilder builder) {
		logger.info("getOpenAuthToken --->> builder: {}",builder);
		
		String validate = validateBuilder(builder);
		if(!Constants.VALIDATESUCCESS.equals(validate)){
			return new AlipayOpenAuthTokenAppResult(validate);
		}
		
		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		
        request.setBizContent(builder.toJsonString());
        
        logger.info("getOpenAuthToken --->> bizContent: {}",request.getBizContent());
        
        AlipayOpenAuthTokenAppResponse response = (AlipayOpenAuthTokenAppResponse) getResponse(client, request);
        
        logger.info("getOpenAuthToken --->> response: {} ",response.toString());
        
        AlipayOpenAuthTokenAppResult result = new AlipayOpenAuthTokenAppResult(response);
        
        if (response != null && Constants.SUCCESS.equals(response.getCode())) {
            result.setAuthStatus(AuthStatus.SUCCESS);
        } 
        return result;
	}

	@Override
	public AlipayOpenAuthTokenQueryResult queryOpenAuthToken(AlipayAuthTokenQueryRequestBuilder builder) {
		logger.info("queryOpenAuthToken --->> builder: {}",builder);

		String validate = validateBuilder(builder);
		if(!Constants.VALIDATESUCCESS.equals(validate)){
			return new AlipayOpenAuthTokenQueryResult(validate);
		}
		
		AlipayOpenAuthTokenAppQueryRequest request = new AlipayOpenAuthTokenAppQueryRequest();
		
		request.setBizContent(builder.toJsonString());
		
		logger.info("queryOpenAuthToken --->> bizContent: {}",request.getBizContent());
		
		AlipayOpenAuthTokenAppQueryResponse response = (AlipayOpenAuthTokenAppQueryResponse) getResponse(client, request);
		
		logger.info("queryOpenAuthToken --->> response: {}",response.toString());
		
		AlipayOpenAuthTokenQueryResult result = new AlipayOpenAuthTokenQueryResult(response);
		
        if (response != null && Constants.SUCCESS.equals(response.getCode())) {
            result.setAuthStatus(AuthStatus.SUCCESS);
        } 
        return result;
	}
}