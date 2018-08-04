package cn.lastmiles.pay.pos.wxpay.service;

import cn.lastmiles.pay.pos.wxpay.common.Configure;
import cn.lastmiles.pay.pos.wxpay.common.HttpsRequest;
import cn.lastmiles.pay.pos.wxpay.common.Signature;
import cn.lastmiles.pay.pos.wxpay.protocol.BaseReqData;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/**
 * User: rizenguo
 * Date: 2014/12/10
 * Time: 15:44
 * 服务的基类
 */
public class BaseService{
	
	private Configure configure;

    //API的地址
    private String apiURL;

    //发请求的HTTPS请求器
    private IServiceRequest serviceRequest;

	public BaseService(String api,Configure configure) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		this.apiURL = api;
        this.configure= configure;
        /*
        Class c = Class.forName(Configure.HttpsRequestClassName);
        serviceRequest = (IServiceRequest) c.newInstance();
        */
        try {
			serviceRequest =  new HttpsRequest(configure);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	protected String sendPost(BaseReqData baseReqData) throws UnrecoverableKeyException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		baseReqData.setAppid(configure.getAppID());//微信分配的公众号ID（开通公众号之后可以获取到）
		baseReqData.setMch_id(configure.getMchID());//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
		baseReqData.setSign(Signature.getSign(baseReqData.toMap(),configure.getKey()));//把签名数据设置到Sign这个属性中
		return serviceRequest.sendPost(apiURL,baseReqData);
    }

    protected String sendPost(Object xmlObj) throws UnrecoverableKeyException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return serviceRequest.sendPost(apiURL,xmlObj);
    }

    /**
     * 供商户想自定义自己的HTTP请求器用
     * @param request 实现了IserviceRequest接口的HttpsRequest
     */
    public void setServiceRequest(IServiceRequest request){
        serviceRequest = request;
    }

	public Configure getConfigure() {
		return configure;
	}

	public void setConfigure(Configure configure) {
		this.configure = configure;
	}
}
