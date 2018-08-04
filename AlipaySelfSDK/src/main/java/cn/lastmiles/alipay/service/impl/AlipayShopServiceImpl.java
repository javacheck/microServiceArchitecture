/**
 * createDate : 2016年7月25日下午5:20:51
 * 口碑店铺
 */
package cn.lastmiles.alipay.service.impl;

import org.apache.commons.lang.StringUtils;
import cn.lastmiles.alipay.config.Configs;
import cn.lastmiles.alipay.service.abs.AbsAlipayShopService;
import com.alipay.api.DefaultAlipayClient;

public class AlipayShopServiceImpl extends AbsAlipayShopService{
		
	public AlipayShopServiceImpl(ClientBuilder builder) {
		if (StringUtils.isEmpty(builder.getServiceUrl())) {
	        throw new NullPointerException("getServiceUrl should not be NULL!");
	    }
        if (StringUtils.isEmpty(builder.getAppid())) {
            throw new NullPointerException("getAppid should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getResPrivateKey())) {
            throw new NullPointerException("getResPrivateKey should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getFormat())) {
            throw new NullPointerException("getFormat should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getCharset())) {
            throw new NullPointerException("getCharset should not be NULL!");
        }
        if (StringUtils.isEmpty(builder.getAlipayPublicKey())) {
            throw new NullPointerException("getAlipayPublicKey should not be NULL!");
        }
        client = new DefaultAlipayClient(builder.getServiceUrl(), 
        		builder.getAppid(), 
        		builder.getResPrivateKey(),
                builder.getFormat(), 
                builder.getCharset(), 
                builder.getAlipayPublicKey());
	}
    
	 public static class ClientBuilder {
        private String serviceUrl;
        private String appid;
        private String resPrivateKey;
        private String format;
        private String charset;
        private String alipayPublicKey;
        
        static{
     		Configs.init("zfbinfo.properties");
     	}
        
        public String getServiceUrl() {
			return serviceUrl;
		}

		public void setServiceUrl(String serviceUrl) {
			this.serviceUrl = serviceUrl;
		}

		public String getAppid() {
			return appid;
		}

		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getResPrivateKey() {
			return resPrivateKey;
		}

		public void setResPrivateKey(String resPrivateKey) {
			this.resPrivateKey = resPrivateKey;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public String getCharset() {
			return charset;
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}

		public String getAlipayPublicKey() {
			return alipayPublicKey;
		}

		public void setAlipayPublicKey(String alipayPublicKey) {
			this.alipayPublicKey = alipayPublicKey;
		}

		public AlipayShopServiceImpl build() {
            if (StringUtils.isEmpty(serviceUrl)) {
            	serviceUrl = Configs.getOpenApiDomain(); 
            }
            if (StringUtils.isEmpty(appid)) {
                appid = Configs.getAppid();
            }
            if (StringUtils.isEmpty(resPrivateKey)) {
            	resPrivateKey = Configs.getPrivateKey();
            }
            if (StringUtils.isEmpty(format)) {
                format = "json";
            }
            if (StringUtils.isEmpty(charset)) {
                charset = "utf-8";
            }
            if (StringUtils.isEmpty(alipayPublicKey)) {
                alipayPublicKey = Configs.getAlipayPublicKey();
            }
            return new AlipayShopServiceImpl(this);
        }
	  }
 }